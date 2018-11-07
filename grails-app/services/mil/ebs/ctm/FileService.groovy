package mil.ebs.ctm

import mil.ebs.ctm.upload.FileUpload
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartFile

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileService {

//    def springSecurityService

    final static String INBOUND_DIRECTORY = "inbound"
    final static String OUTBOUND_DIRECTORY = "outbound"

    boolean transactional = true

// *********************************************************************************************
// file consumption functions
// *********************************************************************************************

    /**
     *
     * @param pFile (MultipartFile) -
     * @param pType (String) -
     * @param pConference (Conference) -
     * @param pComment (String) -
     * @param pAccount (Account) -
     * @return String -
     */
    def FileUpload uploadFile(MultipartFile pFile, final String pType, final Conference pConference, final String pComment, final Account pAccount) {
        String returnFileName = "";

        String fileName = "C" + pConference.id + "_" + pType + "." + pFile.getFileItem().name.substring(pFile.getFileItem().name.lastIndexOf(".") + 1)

        def servletContext = ServletContextHolder.servletContext
        def storagePath = servletContext.getRealPath(INBOUND_DIRECTORY)

        // Create storage path directory if it does not exist
        def storagePathDirectory = new File(storagePath)
        if (!storagePathDirectory.exists()) {
            print "CREATING DIRECTORY ${storagePath}: "
            if (storagePathDirectory.mkdirs()) {
                println "SUCCESS"
            } else {
                println "FAILED"
            }
        }

        // set the uploaded file bytes
        byte[] fileBytes = pFile.bytes

        String status = "Loaded"

        // Store file
        if (!pFile.isEmpty()) {
            def storageName = "${storagePath}/${fileName}"
            encryptFile(fileBytes, new File(storageName))
            returnFileName = storageName
        } else {
            println "File ${pFile.inspect()} was empty!"
        }

        String comments = pComment != null ? pComment : pType + " File Upload"

        // process uploaded file
        FileUpload upload = storeFileUploadMetaData(fileName, pType, pConference, pAccount, comments, status)

        copyFile(returnFileName, "/ctm/" + upload.getFileName())
        removeInboundFile(returnFileName)

        return upload
    }

    /**
     *
     * @param pFileName (String) -
     * @param pFileType (String) -
     * @param pEval (Eval) -
     * @param pAccount (Account) -
     * @param pComments (String) -
     * @return FileUpload -
     */
    def FileUpload storeFileUploadMetaData(final String pFileName, final String pFileType, final Conference pConference, final Account pAccount, final String pComments, final String pStatus) {
        FileUpload upload = new FileUpload()

        upload.setFileName(pFileName)
        upload.setFileDate(new Date())
        upload.setLineCount(0)
        upload.setFileType(pFileType)
        upload.setConference(pConference)
        upload.setComments(pComments)

        // add the current account's name here...
        upload.loadedBy = pAccount.shortName() != null ? pAccount.shortName() : "Localhost"

        // data was loaded
        upload.setStatus(pStatus)

        def fileList = FileUpload.findAllByConferenceAndFileType(pConference, pFileType)
        for (fileUpload in fileList) {
            deleteFile(Long.toString(fileUpload.id))
        }

        // save load status
        upload.save(flush: true)

        return upload
    }

    /**
     * This function removes the file from the system.
     *
     * @param pId (String) - file id
     * @return boolean - true file removed successfully | false file failed to be removed
     */
    def boolean deleteFile(final String pId) {
        FileUpload currentFile = FileUpload.get(pId)
        if (!currentFile) {
            return false
        }

        try {
            currentFile.delete(flush: true)
            removeFile(new File("/ctm/${currentFile.fileName}"))
        } catch (DataIntegrityViolationException ignore) {
            return false
        }

        return true
    }

// *********************************************************************************************
// file retrieval functions
// *********************************************************************************************

    /**
     * This function retrieves a given file (filename) from a specific destination (outbound).
     * Once this file has been retrieved via a file handler it will be decrypted before being
     * transmitted via the stream back to the user's browser.
     *
     * @param pFileType (String) -
     * @param pConference (Conference) -
     * @return File - file handler for the object that will be passed to the stream
     * @throws FileNotFoundException -
     */
    def File retrieveFile(final String pFileType, final Conference pConference)
            throws FileNotFoundException
    {
        return retrieveFile(FileUpload.findByConferenceAndFileType(pConference, pFileType))
    }

    /**
     * This function retrieves a given file (filename) from a specific destination (outbound).
     * Once this file has been retrieved via a file handler it will be decrypted before being
     * transmitted via the stream back to the user's browser.
     *
     * @param pFileId (String) -
     * @return File - file handler for the object that will be passed to the stream
     * @throws FileNotFoundException -
     */
    def File retrieveFile(final String pFileId)
            throws FileNotFoundException
    {
        return retrieveFile(FileUpload.get(pFileId))
    }

    /**
     * This function retrieves a given file (filename) from a specific destination (outbound).
     * Once this file has been retrieved via a file handler it will be decrypted before being
     * transmitted via the stream back to the user's browser.
     *
     * @param pFileUpload (FileUpload) -
     * @return File - file handler for the object that will be passed to the stream
     * @throws FileNotFoundException -
     */
    def File retrieveFile(final FileUpload pFileUpload)
            throws FileNotFoundException
    {
        def storagePath = ServletContextHolder.servletContext.getRealPath(OUTBOUND_DIRECTORY)

        // Create storage path directory if it does not exist
        def storagePathDirectory = new File(storagePath)
        if (!storagePathDirectory.exists()) {
            print "CREATING DIRECTORY ${storagePath}: "
            if (storagePathDirectory.mkdirs()) {
                println "SUCCESS"
            } else {
                println "FAILED"
            }
        }

        // remove any existing files that may exist in the "outbound" directory
        removeOutboundFile(pFileUpload.fileName)

        // create a new file to store the unencrypted data from the encrypted file
        def storageFile = new File("${storagePath}/${pFileUpload.fileName}")
        decryptFile(new File("/ctm/${pFileUpload.fileName}"), storageFile)

        // return this file handle to the controller to stream back to the user's browser
        return storageFile
    }

    /**
     *
     * @param pBaos (ByteArrayOutputStream) -
     * @param pConferenceId (String) -
     * @return
     * @throws FileNotFoundException -
     */
    def ZipOutputStream retrieveAllFiles(ByteArrayOutputStream pBaos, final String pConferenceId)
            throws FileNotFoundException
    {
        ZipOutputStream zipFile = new ZipOutputStream(pBaos)

        for (fileUpload in getFiles(Long.parseLong(pConferenceId))) {
            File file = retrieveFile(fileUpload.getId().toString())

            zipFile.putNextEntry(new ZipEntry(fileUpload.getFileName()))
            file.withInputStream { i -> zipFile << i }
            zipFile.closeEntry()
        }

        zipFile.finish()

        return zipFile
    }

// *********************************************************************************************
// file management functions
// *********************************************************************************************

    /**
     * This file will determine if the given file (filename) exists in the outbound directory.
     * If this file exists it will remove it before copying a newer version from the repository.
     *
     * @param pFileName (String) - name of the file to be removed
     * @return null - nothing is passed back from the function.
     */
    def removeOutboundFile(final String pFileName) {
        def storagePath = ServletContextHolder.servletContext.getRealPath(OUTBOUND_DIRECTORY)
        removeFile(new File("${storagePath}/${pFileName}"))
    }

    /**
     * This file will determine if the given file (filename) exists in the inbound directory.
     *
     * @param pFileName (String) - name of the file to be removed
     * @return null - nothing is passed back from the function.
     */
    def removeInboundFile(final String pFileName) {
        def storagePath = ServletContextHolder.servletContext.getRealPath(INBOUND_DIRECTORY)
        removeFile(new File("${storagePath}/${pFileName}"))
    }

    /**
     * This function removes a file if it current exists.
     *
     * @param pFile (File) - file that is to be removed if it exists?
     * @return null - nothing is passed back from the function.
     */
    private static void removeFile(final File pFile) {
        if (pFile.exists()) {
            pFile.delete()
        }
    }

    /**
     * This function copies a SourceFile to a DestinationFile.
     *
     * @param pSourceFile (String) - Source file to be copied from
     * @param pDestinationFile (String) - Destination file for copied file
     */
    private static copyFile(String pSourceFile, String pDestinationFile) {

        // remove any stored file in the destination path - no version is processed always overwrite
        removeFile(new File(pDestinationFile))

        try {
            InputStream is = new FileInputStream(new File(pSourceFile))
            OutputStream out = new FileOutputStream(new File(pDestinationFile))

            byte[] buf = new byte[1024]
            int len
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len)
            }

            is.close()
            out.close()
        } catch (FileNotFoundException ignored) {
            // do nothing
        } catch (IOException ignored) {
            // do nothing
        }
    }

// *********************************************************************************************
// encryption/decryption functions
// *********************************************************************************************

    /**
     * This function takes a given unencrypted file and encrypts it to the destination file.
     *
     * @param pUploadedFile (File) - file that is currently unencrypted
     * @param pFile (File) - file to be encrypted into
     * @return null - nothing is passed back from the function.
     */
    private static void encryptFile(final MultipartFile pUploadedFile, final File pFile) {
        removeFile(pFile)
        pFile.append(encryptor().encrypt(pUploadedFile.bytes))
    }

    /**
     * This function takes a given unencrypted file and encrypts it to the destination file.
     *
     * @param byte[] (File) - file that is currently unencrypted
     * @param pFile (File) - file to be encrypted into
     * @return null - nothing is passed back from the function.
     */
    private static void encryptFile(final byte[] pUploadedFile, final File pFile) {
        removeFile(pFile)
        pFile.append(encryptor().encrypt(pUploadedFile))
    }

    /**
     * This function takes a given encrypted file and decrypts it to the destination file.
     *
     * @param pSourceFile (File) - file that is currently encrypted
     * @param pDestinationFile (File) - file to be decrypted into
     * @return null - nothing is passed back from the function.
     * @throws FileNotFoundException -
     */
    private static void decryptFile(final File pSourceFile, final File pDestinationFile)
            throws FileNotFoundException
    {
        removeFile(pDestinationFile)
        pDestinationFile.append(encryptor().decrypt(pSourceFile.readBytes()))
    }

    /**
     * This function sets up the encryptor that will be used by the encryptFile and decryptFile functions.
     *
     * @return encryptor (StandardPBEByteEncryptor) - this is the encryptor engine to be used
     * to encrypt or decrypt.
     */
    private static StandardPBEByteEncryptor encryptor() {
        StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor()
        encryptor.setPassword("S^p#rS#cr#t^ltr@P@ssw0rd")
        encryptor.setAlgorithm("PBEWithSHA256And256BitAES-CBC-BC")

        return encryptor;
    }

// *********************************************************************************************
// Conference file management functions
// *********************************************************************************************

    /**
     * This function returns a "set" of associated files for a given Conference (by using the Conference ID).
     *
     * @param pConference (Conference) - Conference to lookup associated files
     * @return Set<FileUploadVw> - set of all "current" files associated with the given Conference ID
     */
    Set<FileUpload> getFiles(final Conference pConference) {
        return FileUpload.findAllByConference(pConference).collect { it } as Set
    }

    /**
     * This function returns a "set" of associated files for a given Conference (by using the Conference ID).
     *
     * @param pConferenceId (Long) - Conference id to lookup associated files
     * @return Set<FileUploadVw> - set of all "current" files associated with the given Conference ID
     */
    Set<FileUpload> getFiles(final Long pConferenceId) {
        return getFiles(Conference.get(pConferenceId))
    }

    /**
     *
     * @param pConference (Conference) -
     * @param pFileType (String) -
     * @return Set<FileUpload> -
     */
    Set<FileUpload> getFileList(final Conference pConference, final String pFileType) {
        return FileUpload.findAllByConferenceAndFileType(pConference, pFileType)
    }

}

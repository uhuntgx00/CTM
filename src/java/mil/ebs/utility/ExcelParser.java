package mil.ebs.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class ExcelParser<T> {

    // list of file error messages
    private List<String> fileErrorList;
    // list of row indexes with errors
    private List<Integer> rowsWithErrorsList;

    // count of rows of data
    private int validRows;
    // count of blank rows
    private int blankRows;
    // count of rows with errors
    private int errorRows;

    // list of T data records
    private List<T> dataList;


    public ExcelParser() {
        // list of data records
        dataList = new ArrayList<T>(1);

        // list of error messages
        fileErrorList = new ArrayList<String>();

        // list of row indexes with errors
        rowsWithErrorsList = new ArrayList<Integer>();

        // counter of the rows with not missing data or errors
        validRows = 0;

        // counter of the rows with no data
        blankRows = 0;

        // counter of the number of rows with errors
        errorRows = 0;
    }

    public int getValidRowCount() {
        return validRows;
    }

    public int getErrorRowCount() {
        return errorRows;
    }

    public int getBlankRowCount() {
        return blankRows;
    }

    public void incErrorRows() {
        errorRows++;
    }

    public void incValidRows() {
        validRows++;
    }

    public void incBlankRows() {
        blankRows++;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void add(final T pT) {
        dataList.add(pT);
    }

    public List<String> getFileErrorList() {
        return fileErrorList;
    }

    // parse needs overwritten
    abstract public void parse(final Workbook pWorkbook, final Date pFileAsOfDate);

    public boolean fileHasErrors() {
        return (fileErrorList != null && fileErrorList.size() > 0);
    }

    private String excelColumn(int pIndex) {
        String columnLabel = "";
        boolean iteration = false; //determines if multiple iterations were performed

        // divide the number by 26 to convert to the appropriate letter.
        do {
            // handles index above 26
            int remainder = pIndex % 26;

            // Compensate for 2 or more iterations.
            if (iteration && pIndex < 25) {
                remainder--;
            }

            columnLabel = (char) (remainder + 'A') + columnLabel;
            pIndex = (pIndex - remainder) / 26;

            iteration = true;
        } while (pIndex > 0);

        return columnLabel;
    }

    public boolean checkColumns(final Row pRow, final String... pCheckNames) {
        boolean result = true;

        for (int i = 0; i < pCheckNames.length; i++) {
            String columnName = pRow.getCell(i) != null ? pRow.getCell(i).getRichStringCellValue().getString() : "";
            if (!columnName.toUpperCase().contains(pCheckNames[i].toUpperCase())) {
                fileErrorList.add("Incorrect Column: " + (isValue(columnName) ? columnName : "blank") + " is invalid for Column <b>" + excelColumn(i) + "</b>");
                result = false;
            }
        }

        return result;
    }

    public boolean rowHasErrors(final int pRowIndex) {
        return rowsWithErrorsList.contains(pRowIndex);
    }

    public boolean isValue(final String pValue) {
        return (pValue != null && !pValue.equals(""));
    }

    public boolean isPopulatedCell(final Cell pCell) {
        return (pCell != null && !pCell.toString().equals(""));
    }

    public boolean isPopulatedRow(final int pTotalNumberOfColumns, final Row pRow) {
        boolean hasData = false;

        // needs any column to contain data
        for (int i = 0; i <= pTotalNumberOfColumns; i++) {
            // checks row to determine if any cells contains data
            if (isPopulatedCell(pRow.getCell(i))) {
                hasData = true;
                break;
            }
        }

        return hasData;
    }

    public Long parseLong(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Long num = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            num = pCell != null && isPopulatedCell(pCell)? (long) pCell.getNumericCellValue() : null;
        } catch (Exception e) {
            addErrorToList(pRowNumber, pFieldName, e.toString());
        }

        if (num == null && pRequired) {
            addErrorToList(pRowNumber, pFieldName, "Missing value");
        }

        return num;
    }

    public int parseInt(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Long num = parseLong(pRowNumber, pFieldName, pCell, pRequired);
        //return int value otherwise 0
        return num!=null?num.intValue():0;
    }

    public Double parseDouble(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Double num = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            num = pCell != null ? pCell.getNumericCellValue() : null;
        } catch (Exception e) {
            addErrorToList(pRowNumber, pFieldName, e.toString());
        }
        if (num == null && pRequired) {
            addErrorToList(pRowNumber, pFieldName, "Missing value");
        }

        return num;
    }

    public Double parseFormattedDouble(final int pRowNumber, final String pFieldName, final Cell pCell, final String pFormat, final boolean pRequired) {
        // retrieves numeric cell's value
        DecimalFormat df = new DecimalFormat(pFormat);
        Double num = parseDouble(pRowNumber, pFieldName, pCell, pRequired);

        // trims the fraction portion of the value according to format
        num = new Double(df.format(num));

        // determine the format's allowable number of digits before the decimal place
        int fInteger = pFormat.split("\\.", 2)[0].length();

        // determine the parsed value's number of digits before the decimal place
        int vInteger =  num.toString().split("\\.", 2)[0].length();

        // if the values number of digits exceeds the allowable number of digits log error
        if(vInteger > fInteger){
            addErrorToList(pRowNumber, pFieldName, "Value does not match expected number format " + pFormat);
        }

        return num;
    }

    public String parseValue(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        String value = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            value = isPopulatedCell(pCell) ? pCell.getRichStringCellValue().getString() : "";
        } catch (Exception e) {
            addErrorToList(pRowNumber, pFieldName, e.toString());
        } finally {
            if ((value == null || "".equals(value)) && pRequired) {
                addErrorToList(pRowNumber, pFieldName, "Missing value");
            }
        }

        return value;
    }

    public Date parseDate(final int pRowNumber, final String pFieldName, final Cell pCell, final String pAcceptedDateFormat, final boolean pRequired) {
        Date dbDate = null;
        // pAcceptedDateFormat must be an valid date format
        DateFormat df = new SimpleDateFormat(pAcceptedDateFormat);

        try {
            // retrieves date - if a parsable date
            dbDate = pCell != null ? pCell.getDateCellValue() : null;
            // formats date to remove time portion of date
            if(dbDate != null){
                dbDate = df.parse(df.format(dbDate));
            }
        } catch (ParseException pe) {
            addErrorToList(pRowNumber, pFieldName, "Unable to parse date (date format must be \" + pAcceptedDateFormat + \")");
        } catch (NumberFormatException nfe) {
            addErrorToList(pRowNumber, pFieldName, "Unable to parse date (Cell must be of type date)");
        } catch (IllegalStateException ise) {
            // cell is CELL_TYPE_STRING - attempt using acceptable format
            try {
                dbDate = df.parse(pCell.getRichStringCellValue().getString());
            } catch (ParseException exception) {
                addErrorToList(pRowNumber, pFieldName, "Unable to parse date (date format must be " + pAcceptedDateFormat + ")");
            }
        }

        if (dbDate == null && pRequired) {
            addErrorToList(pRowNumber, pFieldName, "Missing value");
        }

        return dbDate;
    }

    public void addErrorToList(final int pRow, final String pField, final String pErrorMsg) {
        fileErrorList.add(pErrorMsg + " in <b>row</b>: " + pRow + " <b>column</b>: " + pField);
        rowsWithErrorsList.add(pRow);
    }


}

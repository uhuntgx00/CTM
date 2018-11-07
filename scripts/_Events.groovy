import java.text.SimpleDateFormat

eventCompileStart = { kind ->
	def buildNumber = metadata.'app.buildNumber'
	def formatter = new SimpleDateFormat("MMM dd, yyyy")
	def buildDate = formatter.format(new Date(System.currentTimeMillis()))

	if (!buildNumber) {
		buildNumber = 1
	} else {
		buildNumber = Integer.valueOf(buildNumber) + 1
	}

	metadata.'app.buildNumber' = buildNumber.toString()
	metadata.'app.buildDate' = buildDate
	metadata.'app.buildProfile' = grailsEnv

	metadata.persist()

	println "**** Compile Starting on Build #${buildNumber}"
} 

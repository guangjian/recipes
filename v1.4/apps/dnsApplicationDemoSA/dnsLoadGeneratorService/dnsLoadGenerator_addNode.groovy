
/**********************************************************************************************************************
 * Parameters:
 * 		arg[0] = IP address of dns VM. (The dns VM will pass it's IP address when invoking the method.)
 */

import org.cloudifysource.dsl.context.ServiceContextFactory

// parameters passed by dnsSlave-service.groovy
def dnsVmIp = args[0]
def numberSlaves = args[1]

context = ServiceContextFactory.getServiceContext()

config = new ConfigSlurper().parse(new File("dnsLoadGenerator-service.properties").toURL())
webServerDirectory=config.webServerDirectory
webServerHtdocs=config.webServerHtdocs
webServerCgibin=config.webServerCgibin
paintMainPageCgi=config.paintMainPageCgi
paintStopPageCgi=config.paintStopPageCgi


serverList=config.serverList
serverListPreamble=config.serverListPreamble
serverListFile="${webServerDirectory}/${webServerCgibin}/${serverList}"

if ( "${dnsVmIp}" ==~ "Slave:.*" )
{
	Builder = new AntBuilder()
	
	// modify the web page painter to account for the parameterized number of DNS servers.
	// If the "BASENUMSLAVES_SUBSTITUTE" string is already replaced, this will be a no-op.
	Builder.sequential {
		replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/${paintMainPageCgi}",
			match:"BASENUMSLAVES_SUBSTITUTE",
			replace:"${numberSlaves}")

		replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/${paintStopPageCgi}",
			match:"BASENUMSLAVES_SUBSTITUTE",
			replace:"${numberSlaves}")		
		
		//Now need to reset the permissions to be executable because the replaceregexp changes the perms to read
		chmod(file:"${webServerDirectory}/${webServerCgibin}/*", perm:'755')
	}
	
}

println "addNode: About to add ${dnsVmIp} to ${serverListFile} ..."
def dnsServerListFile = new File("${serverListFile}")
def dnsServerListText = dnsServerListFile.text


// Update the server list file on the dnsLoadGenerator instance if necessary.
if ( dnsServerListText.contains(dnsVmIp)) {
	println "addNode: Not adding ${dnsVmIp} to ${serverListFile} because it's already there..."	
}
else { 
	def modifiedText = dnsServerListText.replace("${serverListPreamble}", "${serverListPreamble}" + System.getProperty("line.separator") + "${dnsVmIp}")				
	dnsServerListFile.text = modifiedText
	println "addNode: Added ${dnsVmIp} to ${serverListFile} text is now : ${modifiedText}..."
}

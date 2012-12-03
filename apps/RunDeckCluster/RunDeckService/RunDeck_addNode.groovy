
/**********************************************************************************************************************
 * Parameters:
 * 		arg[0] = IP address of remotenode VM. (The remotenode service will pass the VM's IP address when invoking the method.)
 */

import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.dsl.context.ServiceContextFactory

def remoteNodeVmIp = args[0]

context = ServiceContextFactory.getServiceContext()

config = new ConfigSlurper().parse(new File("dnsLoadGenerator-service.properties").toURL())
webServerDirectory=config.webServerDirectory
webServerHtdocs=config.webServerHtdocs
webServerCgibin=config.webServerCgibin
serverList=config.serverList
serverListPreamble=config.serverListPreamble
serverListFile="${webServerDirectory}/${webServerCgibin}/${serverList}"


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

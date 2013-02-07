/**********************************************************************************************************************
 * Parameters:
 * 		arg[0] = IP address of dns VM. (The dns VM will pass it's IP address when invoking the method.)
 */
import org.cloudifysource.dsl.context.ServiceContextFactory

def dnsVmIp = args[0]

context = ServiceContextFactory.getServiceContext()

config = new ConfigSlurper().parse(new File("dnsLoadGenerator-service.properties").toURL())
webServerDirectory=config.webServerDirectory
webServerHtdocs=config.webServerHtdocs
webServerCgibin=config.webServerCgibin
serverList=config.serverList
serverListPreamble=config.serverListPreamble
serverListFile="${webServerDirectory}/${webServerCgibin}/${serverList}"

println "removeNode: About to remove ${dnsVmIp} from ${serverListFile} ..."
def dnsServerListFile = new File("${serverListFile}")
def dnsServerListText = dnsServerListFile.text
def modifiedText = dnsServerListText.replace("${dnsVmIp}", "")
dnsServerListFile.text = modifiedText

println "removeNode: Removed ${dnsVmIp} from ${serverListFile} text is now : ${modifiedText}..."

	
import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

// Then here's the logic

context = ServiceContextFactory.getServiceContext()

def hostIp

if (  context.isLocalCloud()  ) {
	hostIp = InetAddress.getLocalHost().getHostAddress()
}
else {
	hostIp = System.getenv()["CLOUDIFY_AGENT_ENV_PRIVATE_IP"]
}

// hostIp=InetAddress.localHost.hostAddress

println "HostIP is: $hostIp"

def mysqlService = context.waitForService("mysql", 300, TimeUnit.SECONDS)
mysqlHostInstances = mysqlService.waitForInstances(mysqlService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

mysqlServerIP = mysqlHostInstances[0].hostAddress
if (mysqlServerIP == null) {
        mysqlServerIP = hostIp
}

println "Retrieving mySqlServerIP: $mysqlServerIP"

def nfsService = context.waitForService("nfsServer", 300, TimeUnit.SECONDS)
nfsHostInstances = nfsService.waitForInstances(nfsService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

nfsServerIP = nfsHostInstances[0].hostAddress
if (nfsServerIP == null) {
        nfsServerIP = hostIp
}

println "Retrieving nfsServerIP: $nfsServerIP"

def ctxPath=("default" == context.applicationName)?"":"${context.applicationName}"
println "Setting ctxPath: $ctxPath"

def instanceID = context.instanceId
println "Setting instanceID: $ctxPath"

def haService = context.waitForService("HAproxy", 180, TimeUnit.SECONDS)
println "Invoking addNode, http://${hostIp}:80/${ctxPath}"
haService.invoke("addNode", "${hostIp}" as String)
println "AddNode complete"
			
Builder = new AntBuilder()

println "Start: copy config and execute start.sh"
Builder.sequential {
		replaceregexp(file:"${context.serviceDirectory}/config.php",
					match:"MYSQLSERVERIP",
					replace:"$mysqlServerIP")		
		copy(file:"${context.serviceDirectory}/config.php", tofile:"/var/www/html/owncloud/config/config.php")
		chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")					
		exec(executable:"${context.serviceDirectory}/start.sh",osfamily:"unix") {
			arg value:"$nfsServerIP"
			}
println "Finished attempting to copy config and execute start.sh"
}
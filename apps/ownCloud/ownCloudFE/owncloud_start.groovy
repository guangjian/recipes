import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

// Then here's the logic

context = ServiceContextFactory.getServiceContext()

hostIp=InetAddress.localHost.hostAddress
println "HostIP is: $hostIP"

def mysqlService = context.waitForService("mysql", 300, TimeUnit.SECONDS)
mysqlHostInstances = mysqlService.waitForInstances(mysqlService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

mysqlServerIP = mysqlHostInstances[0].hostAddress
if (mysqlServerIP == null) {
        mysqlServerIP = hostIp
}

println "Retrieving mySqlServerIP: $mysqlServerIP"

def nfsService = context.waitForService("NFS", 300, TimeUnit.SECONDS)
nfsHostInstances = nfsService.waitForInstances(nfsService.numberOfPlannedInstances, 300, TimeUnit.SECONDS)

nfsServerIP = nfsHostInstances[0].hostAddress
if (nfsServerIP == null) {
        nfsServerIP = hostIp
}

println "Retrieving nfsServerIP: $nfsServerIP"

Builder = new AntBuilder()

println "Start: copy config and execute start.sh"
Builder.sequential {
		replaceregexp(file:"${context.serviceDirectory}/config.php",
					match:"MYSQLSERVERIP",
					replace:"$mysqlServerIP")		
		copy(file:"${context.serviceDirectory}/config.php", tofile:"/var/www/html/owncloud/config/config.php")					
		exec(executable:"${context.serviceDirectory}/start.sh",osfamily:"unix") {
			arg value:"$NFSSERVERIP"
			}
println "Finished attempting to copy config and execute start.sh"

}

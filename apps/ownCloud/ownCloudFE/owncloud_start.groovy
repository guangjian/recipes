import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import  java.util.concurrent.TimeUnit

// Then here's the logic

context = ServiceContextFactory.getServiceContext()

def mysqlService = context.waitForService("mysql", 300, TimeUnit.SECONDS)
mysqlHostInstances = mysqlService.waitForInstances(mysqlService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

def nfsService = context.waitForService("NFS", 300, TimeUnit.SECONDS)
nfsHostInstances = nfsService.waitForInstances(nfsService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

hostIp=InetAddress.localHost.hostAddress

// Logic for owncloud recipe to get IPs of MySQL and NFS servers:

mysqlServerIP = mysqlHostInstances[0].hostAddress
if (mysqlServerIP == null) {
        mysqlServerIP = hostIp
}

nfsServerIP = nfsHostInstances[0].hostAddress
if (nfsServerIP == null) {
        nfsServerIP = hostIp
}

Builder = new AntBuilder()

Builder.sequential {
		replaceregexp(file:"${context.serviceDirectory}/config.php",
					match:"MYSQLSERVERIP",
					replace:"$mysqlServerIP")		
		copy(file:"${context.serviceDirectory}/config.php", tofile:"/var/www/html/owncloud/config/config.php")					
		exec(executable:"${context.serviceDirectory}/start.sh",osfamily:"unix") {
			arg value:"$NFSSERVERIP"
			}
}

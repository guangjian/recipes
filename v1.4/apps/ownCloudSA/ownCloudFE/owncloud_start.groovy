import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

config=new ConfigSlurper().parse(new File('owncloud-service.properties').toURL())
ownCloudDir=config.ownCloudDir
ctlScript=config.ctlScript
script="${ownCloudDir}/${ctlScript}"

// Then here's the logic

context = ServiceContextFactory.getServiceContext()

hostIp=InetAddress.localHost.hostAddress
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

Builder = new AntBuilder()

println "Start: tinker with the config.php that is already in place and then execute start.sh"
// NOTE: Although there is a config.php file as part of this recipe, we don't actually use it in this MWC2013, self-contained
// version. Instead we just tinker with the file that is already in place as part of the image.
Builder.sequential {
		// Point DB setting at the mysql server.
		replaceregexp(file:"/opt/owncloud-4.5.5-0/apps/owncloud/htdocs/config/config.php",
					match:"localhost",
					replace:"$mysqlServerIP")	
		// Point the data directory at the NFS mounted directory
		replaceregexp(file:"/opt/owncloud-4.5.5-0/apps/owncloud/htdocs/config/config.php",
			match:"/opt/owncloud-4.5.5-0/apps/owncloud/data",
			replace:"/opt/ocStorage")
		
		// Configure and set up the NFS mounted directory
		chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")					
		exec(executable:"${context.serviceDirectory}/start.sh",osfamily:"unix") {
			arg value:"$nfsServerIP"
		}
		
		// Now start the web service
		echo(message:"mysql_start.groovy: Running ${script}  ...")
		exec(executable:"${script}", osfamily:"unix",failonerror: "true") {
			arg(line:"start")
			arg(line:"apache")
		}
}
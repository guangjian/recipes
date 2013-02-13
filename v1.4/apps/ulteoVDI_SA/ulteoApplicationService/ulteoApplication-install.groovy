import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.utils.ServiceUtils;
import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.context.ServiceContextFactory

println "###### > Installing Ulteo Session Application"

config = new ConfigSlurper().parse(new File("ulteoApplication.properties").toURL())
def context = ServiceContextFactory.getServiceContext()

def ulteoManagerService = context.waitForService("ulteoManagerService", 180, TimeUnit.SECONDS)
sessionManagerInstances = ulteoManagerService.waitForInstances(ulteoManagerService.numberOfPlannedInstances, 60, TimeUnit.SECONDS)

// Get the IP of the session manager which the manager stored earlier.
managerIP=context.attributes.thisApplication["sessionManagerIP"]

builder = new AntBuilder()

builder.sequential {
	chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")
	echo(message:"Running ${context.serviceDirectory}/installOnLinux.sh ...")
	exec(executable: "${context.serviceDirectory}/installOnLinux.sh", osfamily:"unix", failonerror: "true" ) {
		arg value: "${managerIP}"
	}
}

// Place the  scripts in place
println("Moving shell scripts to their proper home")
newbuilder = new AntBuilder()
newbuilder.sequential {
	copy(file:"${context.serviceDirectory}/${config.numVdiSessions}" , 		tofile:"/root/${config.numVdiSessions}")
	copy(file:"${context.serviceDirectory}/${config.numActiveServers}" , 		tofile:"/root/${config.numActiveServers}")
	chmod(dir:"/root", perm:"+x", includes:"*.sh")
}


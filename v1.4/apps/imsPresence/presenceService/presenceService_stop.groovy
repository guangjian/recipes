/*******************************************************************************
* Suspend the presence server
* This code will try to suspend the server even if it is already suspended.
*******************************************************************************/
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("presenceService-service.properties").toURL())

vSphereServer=config.vSphereServer
vSphereUser=config.vSphereUser
vSpherePassword=config.vSpherePassword
vSphereVmId=config.vSphereVmId
suspendPresenceOnStop=config.suspendPresenceOnStop

if ( suspendPresenceOnStop ) {

	builder = new AntBuilder()
	
	builder.sequential {		
		echo(message:"presenceService_stop.groovy: suspending presence server VM ....")
		
		//chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.py")
		
		// Running python script to unsuspend/start the VM.
		exec(executable:"${context.serviceDirectory}/presenceService_stop.py", osfamily:"unix") {
			arg(line:"${vSphereServer}")
			arg(line:"${vSphereUser}")
			arg(line:"${vSpherePassword}")
			arg(line:"${vSphereVmId}")
		}
	}
}
else
{
	echo(message:"presenceService_stop.groovy: skipping suspend ...")
}

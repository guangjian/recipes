/*******************************************************************************
* Start up or unsuspend the presence server
* This code will try to unsuspend the server even if it is already unsuspended.
*******************************************************************************/
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("presenceService-service.properties").toURL())

vSphereServer=config.vSphereServer
vSphereUser=config.vSphereUser
vSpherePassword=config.vSpherePassword
vSphereVmId=config.vSphereVmId

builder = new AntBuilder()

builder.sequential {		
	echo(message:"presenceService_start.groovy: starting presence server VM ....")
	
	chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.py *.cgi")
	 
	
	// Running python script to unsuspend/start the VM.
	exec(executable:"${context.serviceDirectory}/presenceService_start.py", osfamily:"unix") {
		arg(line:"${vSphereServer}")
		arg(line:"${vSphereUser}")
		arg(line:"${vSpherePassword}")
		arg(line:"${vSphereVmId}")
	}
}

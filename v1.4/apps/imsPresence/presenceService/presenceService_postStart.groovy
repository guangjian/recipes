/*******************************************************************************
* Set up a web interface that let's the user suspend/unsuspend the VM
*******************************************************************************/
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("presenceService-service.properties").toURL())

vSphereServer=config.vSphereServer
vSphereUser=config.vSphereUser
vSpherePassword=config.vSpherePassword
vSphereVmId=config.vSphereVmId

webServerDirectory=config.webServerDirectory
webServerCgibin=config.webServerCgibin
webServerHtml=config.webServerHtml

// Trying this prebuilding of the directory variable to see if it fixes problems when calling chmod
webServerCgibinDir="${webServerDirectory}/${webServerCgibin}"

builder = new AntBuilder()

builder.sequential {

	echo(message:"presenceService_postStart.groovy: Copying files to cgi-bin: ${webServerCgibinDir}")
	copy(todir:"${webServerCgibinDir}") {
		fileset(dir:"${context.serviceDirectory}", includes: "*.py *.cgi")
	}
	
	echo(message:"presenceService_postStart.groovy: Copying the /var/www/html files.")
	
	copy(todir:"${webServerDirectory}/${webServerHtml}") {
		fileset(dir:"${context.serviceDirectory}", includes: "*.html *.css *.png")
	}
	
	echo(message:"presenceService_postStart.groovy: Substituting text in scripts to point at vSphere")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/paintPage.py",
		match:"VSPHERESERVER",
		replace:"${vSphereServer}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/paintPage.py",
		match:"VSPHEREUSER",
		replace:"${vSphereUser}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/paintPage.py",
		match:"VSPHEREPASSWORD",
		replace:"${vSpherePassword}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/paintPage.py",
		match:"VSPHEREVM",
		replace:"${vSphereVmId}")
	
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_start.cgi",
		match:"VSPHERESERVER",
		replace:"${vSphereServer}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_start.cgi",
		match:"VSPHEREUSER",
		replace:"${vSphereUser}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_start.cgi",
		match:"VSPHEREPASSWORD",
		replace:"${vSpherePassword}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_start.cgi",
		match:"VSPHEREVM",
		replace:"${vSphereVmId}")
	
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_stop.cgi",
		match:"VSPHERESERVER",
		replace:"${vSphereServer}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_stop.cgi",
		match:"VSPHEREUSER",
		replace:"${vSphereUser}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_stop.cgi",
		match:"VSPHEREPASSWORD",
		replace:"${vSpherePassword}")
	replaceregexp(file:"${webServerDirectory}/${webServerCgibin}/presenceService_stop.cgi",
		match:"VSPHEREVM",
		replace:"${vSphereVmId}")
	
	//YOU HAVE TO do these chmods AFTER the replaceregexp since the replaceregexp apparently changes the file permissions.
	echo(message:"presenceService_postStart.groovy: making cgi-bin files executable")
	chmod(dir:"${webServerCgibinDir}", perm:"755", includes:"*.py *cgi")

}

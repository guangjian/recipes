/*******************************************************************************
* Set up a web interface that let's the user suspend/unsuspend the VM
*******************************************************************************/
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("webService-service.properties").toURL())

greetingText=config.greetingText

webServerDirectory=config.webServerDirectory
webServerCgibin=config.webServerCgibin
webServerHtml=config.webServerHtml

builder = new AntBuilder()

builder.sequential {

	echo(message:"webService_postStart.groovy: Copying the /var/www/html files.")
	
	copy(todir:"${webServerDirectory}/${webServerHtml}") {
		fileset(dir:"${context.serviceDirectory}", includes: "*.html *.css *.png")
	}
	
	echo(message:"presenceService_postStart.groovy: Substituting greeting text in index.html file")
	replaceregexp(file:"${webServerDirectory}/${webServerHtml}/index.html",
		match:"Hola Mundo",
		replace:"${greetingText}")

}

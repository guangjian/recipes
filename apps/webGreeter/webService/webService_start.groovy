/*******************************************************************************
* Set up a web interface that let's the user suspend/unsuspend the VM
*******************************************************************************/
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("webService-service.properties").toURL())

greetingText=config.WebPageGreeting

webServerDirectory=config.webServerDirectory
webServerCgibin=config.webServerCgibin
webServerHtml=config.webServerHtml

builder = new AntBuilder()

builder.sequential {

	echo(message:"webService_start.groovy: creating index.html file.")
	
	echo(message:"${greetingText}", file:"${webServerDirectory}/${webServerHtml}/index.html")
	
	exec(executable: 'service', osfamily:"unix") {
							 arg value:"httpd"
							 arg value:"restart"
	}

}

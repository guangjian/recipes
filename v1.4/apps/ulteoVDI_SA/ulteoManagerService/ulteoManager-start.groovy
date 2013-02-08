 import groovy.util.ConfigSlurper
 import org.cloudifysource.usm.USMUtils
 import org.cloudifysource.dsl.context.ServiceContextFactory
 
println "###### > Starting Ulteo Session Manager"

context = ServiceContextFactory.getServiceContext()
def startScript
startScript="${context.serviceDirectory}/run.sh"

builder = new AntBuilder()
builder.sequential {
	echo(message:"Starting on ulteoManager")
	exec(executable:"${startScript}", osfamily:"unix",failonerror: "true")
}

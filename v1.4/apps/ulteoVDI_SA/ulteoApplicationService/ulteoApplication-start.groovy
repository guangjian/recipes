 import groovy.util.ConfigSlurper
 import org.cloudifysource.usm.USMUtils
 import org.cloudifysource.dsl.context.ServiceContextFactory
 
println "###### > Starting Ulteo Session Application"

context = ServiceContextFactory.getServiceContext()

def startScript
startScript="${context.serviceDirectory}/run.sh"

println "###### > Invoke Start"

builder = new AntBuilder()
builder.sequential {
	exec(executable:"${startScript}", osfamily:"unix",failonerror: "true")
}



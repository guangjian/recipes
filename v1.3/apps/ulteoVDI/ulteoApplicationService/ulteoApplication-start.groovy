 import groovy.util.ConfigSlurper
 import org.hyperic.sigar.OperatingSystem
 import org.cloudifysource.usm.USMUtils
 import org.cloudifysource.dsl.context.ServiceContextFactory
 
println "###### > Starting Ulteo Session Application"

context = ServiceContextFactory.getServiceContext()
def os = OperatingSystem.getInstance()
def currVendor=os.getVendor()
def startScript
startScript="${context.serviceDirectory}/run.sh"

println "###### > Wait for Manager to be available"

println "###### > Invoke Start"

builder = new AntBuilder()
builder.sequential {
	exec(executable:"${startScript}", osfamily:"unix",failonerror: "true")
}



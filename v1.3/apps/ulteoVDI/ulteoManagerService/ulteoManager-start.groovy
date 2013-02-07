 import groovy.util.ConfigSlurper
 import org.hyperic.sigar.OperatingSystem
 import org.cloudifysource.usm.USMUtils
 import org.cloudifysource.dsl.context.ServiceContextFactory
 
println "###### > Starting Ulteo Session Manager"

context = ServiceContextFactory.getServiceContext()
def os = OperatingSystem.getInstance()
def currVendor=os.getVendor()
def startScript
startScript="${context.serviceDirectory}/run.sh"

builder = new AntBuilder()
builder.sequential {
	echo(message:"Starting on >> ${os} Vendor >> ${currVendor}")
	exec(executable:"${startScript}", osfamily:"unix",failonerror: "true")
}

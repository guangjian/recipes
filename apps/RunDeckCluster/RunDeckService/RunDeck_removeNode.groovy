
/**********************************************************************************************************************
 * Parameters:
 * 		arg[0] = IP address of remotenode VM. (The remotenode service will pass the VM's IP address when invoking the method.)
 */

import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

def remotenode_ip = args[0]
println "RunDeckService_removeNode.groovy: Removing host with IP: ${remotenode_ip}"
context = ServiceContextFactory.getServiceContext()

config = new ConfigSlurper().parse(new File("RunDeck-service.properties").toURL())
project_dir="${config.rundeck_config_dir}/${config.rundeck_project_name}"
resources_file="${project_dir}/etc/${config.rundeck_resources_xml}"

// Add the new server into resources.xml
Builder = new AntBuilder()
Builder.sequential {
	// find the line with the vm IP in it and delete it
	replaceregexp(file:"${resources_file}", match:"^<node.*${remotenode_ip}.*>$", replace:"", flags:"m");
}


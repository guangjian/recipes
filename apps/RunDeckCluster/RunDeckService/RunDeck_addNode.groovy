
/**********************************************************************************************************************
 * Parameters:
 * 		arg[0] = IP address of remotenode VM. (The remotenode service will pass the VM's IP address when invoking the method.)
 */

import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

def remotenode_ip = args[0]

context = ServiceContextFactory.getServiceContext()

config = new ConfigSlurper().parse(new File("RunDeck-service.properties").toURL())
project_dir="${config.rundeck_config_dir}/${config.rundeck_project_name}"
resources_file="${project_dir}/etc/${config.rundeck_resources_xml}"

// Add the new server into resources.xml
Builder = new AntBuilder()
Builder.sequential {
	// First replace the last line of the resources.xml file with the new resource entry.
	replaceregexp(file:"${resources_file}", match:"</project>", replace:"<node name=\"node-${remotenode_ip}\" description=\"node-${remotenode_ip}\" hostname=\"${remotenode_ip}\" username=\"root\"/>");
	// Now add back in the </project> line which is needed at the end of the file.
	echo(message:"</project>\n", file:"${resources_file}", append:"true");
}


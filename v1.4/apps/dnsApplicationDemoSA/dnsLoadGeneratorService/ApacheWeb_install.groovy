/*******************************************************************************
* Copyright (c) 2011 GigaSpaces Technologies Ltd. All rights reserved
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("ApacheWeb-service.properties").toURL())

osConfig = ServiceUtils.isWindows() ? config.win32 : config.linux

//---------------------------------------------
println "###### > Do Update Sudoers file"
serverXmlFile = new File("/etc/sudoers")
sudText = serverXmlFile.text
sudText = sudText.replaceAll('Defaults    requiretty', '#Defaults    requiretty')
serverXmlFile.text = sudText
//---------------------------------------------

def installLinuxHttpd(context,builder,currVendor,installScript) {

	if ( context.isLocalCloud() ) {
		if ( context.attributes.thisApplication["installing"] == null || context.attributes.thisApplication["installing"] == false ) {
			context.attributes.thisApplication["installing"] = true
		}
		else {
			while ( context.attributes.thisApplication["installing"] == true ) {
				println "ApacheWeb_install.groovy: waiting for other installation to complete on localCloud"
				sleep 10000			
			}
		}
	}

	builder.sequential {
		echo(message:"ApacheWeb_install.groovy: Chmodding +x ${context.serviceDirectory} ...")
		chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")

		echo(message:"ApacheWeb_install.groovy: Running ${context.serviceDirectory}/${installScript} ...")
		exec(executable: "${context.serviceDirectory}/${installScript}",failonerror: "true")
	}
	
	if ( context.isLocalCloud() ) {
		context.attributes.thisApplication["installing"] = false
		println "ApacheWeb_install.groovy: Finished installing on localCloud"
	}	
}

builder = new AntBuilder()

installLinuxHttpd(context,builder,"unix", "install.sh")



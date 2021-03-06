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
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("ApacheWeb-service.properties").toURL())

builder = new AntBuilder()

def confScript
confScript="${context.serviceDirectory}/configureApacheConf.sh"



builder.sequential {		
	echo(message:"ApacheWeb_postinstall.groovy: Running ${confScript} ...")
	exec(executable: "${confScript}",failonerror: "true") {
		arg(value:"80")		
		arg(value:"${config.currentPort}")
		arg(value:"${context.serviceDirectory}")
	}	
}


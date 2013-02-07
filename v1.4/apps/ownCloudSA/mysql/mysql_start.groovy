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
import org.hyperic.sigar.OperatingSystem
import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory

config=new ConfigSlurper().parse(new File('mysql-service.properties').toURL())
osConfig=ServiceUtils.isWindows() ? config.win64 : config.linux
context = ServiceContextFactory.getServiceContext()

ownCloudDir=config.ownCloudDir
ctlScript=config.ctlScript

script="${ownCloudDir}/${ctlScript}"

builder = new AntBuilder()

// Run control script to crank up MySQL on this instance.
builder.sequential {
	echo(message:"mysql_start.groovy: Running ${script}  ...")
	exec(executable:"${script}", osfamily:"unix",failonerror: "true") {
		arg(line:"start")
		arg(line:"mysql")
	}
}

println "mysql_start.groovy: End"
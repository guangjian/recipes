import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.context.ServiceContextFactory
import org.cloudifysource.dsl.utils.ServiceUtils;

/*
 * Version 1: Basic deployment. Cannot be scaled.
 * Version 2: Support manual scaling (i.e. via cloudify shell use set-instances RunDeckRemoteNodes X to set the number of instances wanted
 */

service {
	
	name "RunDeckRemoteNodes"
	type "APP_SERVER"

	elastic true
	numInstances "${Number_of_RemoteNodes}"
	minAllowedInstances 1
	maxAllowedInstances 100

	compute {
		// Use template with root access - to make things easier
		template "tmpl_20130117_1"
	}

	lifecycle {	
	
		preStart "RemoteNode_preStart.groovy"
			
		startDetectionTimeoutSecs 800
		startDetection {		
			ServiceUtils.isPortOccupied(currentPort)
		}	
					
		locator {			
			return []
        }	
	
		postStart {
			def RunDeckService = context.waitForService("RunDeckService", 180, TimeUnit.SECONDS)
			def hostAddress=System.getenv()["CLOUDIFY_AGENT_ENV_PRIVATE_IP"]
			RunDeckService.invoke("addNode", "${hostAddress}" as String)
		}
		
		postStop {
			def RunDeckService = context.waitForService("RunDeckService", 180, TimeUnit.SECONDS)
			def hostAddress=System.getenv()["CLOUDIFY_AGENT_ENV_PRIVATE_IP"]
			RunDeckService.invoke("removeNode", "${hostAddress}" as String)
		}


	}
	
	userInterface {
		metricGroups = ([
			metricGroup {
				name "process"
				metrics([
					"Total Process Cpu Time"
				])
			}
		])
	
		widgetGroups = ([
			widgetGroup {
				name "Total Process Cpu Time"
				widgets([
					balanceGauge{metric = "Total Process Cpu Time"},
					barLineChart {
						metric "Total Process Cpu Time"
						axisYUnit Unit.REGULAR
					}
				])
			}
		])
	}
	
}

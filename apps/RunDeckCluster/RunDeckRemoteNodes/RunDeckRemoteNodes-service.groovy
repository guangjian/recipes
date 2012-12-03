import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.context.ServiceContextFactory
import java.util.concurrent.TimeUnit

service {
	
	name "RunDeckRemoteNodes"
	type "APP_SERVER"

	elastic true
	numInstances 1
	minAllowedInstances 1
	maxAllowedInstances 20

	compute {
		// Use template with root access - to make things easier
		template "RunDeckRemoteNodeTemplate"
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
/*		
		start {
			def RunDeckService = context.waitForService("RunDeckService", 180, TimeUnit.SECONDS)
			def hostAddress=System.getenv()["CLOUDIFY_AGENT_ENV_PRIVATE_IP"]
			RunDeckService.invoke("addNode", "${hostAddress}" as String)
		}
		
		stop {
			def RunDeckService = context.waitForService("dnsLoadGeneratorService", 180, TimeUnit.SECONDS)
			def hostAddress=System.getenv()["CLOUDIFY_AGENT_ENV_PRIVATE_IP"]
			dnsLoadGeneratorService.invoke("removeNode", "${hostAddress}" as String)
		}
*/
		
		monitors {
			value="/root/bin/check_num_remotenodes_wanted.sh".execute().text
			return ["Number of Remote Nodes Wanted":value as Integer]
		}

	}

	
	userInterface {
			
		metricGroups = ([
			metricGroup {
				name "Number of Remote Nodes Wanted"
				metrics([
					"Number of Remote Nodes Wanted"
				])
			}
		])
			
		widgetGroups = ([
			widgetGroup {
				name "Number of Remote Nodes Wanted"
				widgets ([
					balanceGauge{metric = "Number of Remote Nodes Wanted"},
					barLineChart{
						metric "Number of Remote Nodes Wanted"
						axisYUnit Unit.REGULAR
					}
				])
			},
		])
	}
	
	context = ServiceContextFactory.getServiceContext()
	def remoteNodesService = context.waitForService("RunDeckRemoteNodes", 300, TimeUnit.SECONDS)	
	def num_actual_instances=remoteNodesService.numberOfActualInstances
	println "Number of actual remotenodes deployed is: ${remoteNodesService}."
	// Once additional VMs have been added or removed (scaling has occured), the scaling rules will
	// be disabled this number of seconds.
	scaleCooldownInSeconds 20
	samplingPeriodInSeconds 1


	scalingRules ([
		scalingRule {

			serviceStatistics {
				metric "Number of Remote Nodes Wanted"
				movingTimeRangeInSeconds 10
			}

			highThreshold {
				value num_actual_instances
				num_instances_to_increase=statistics.averageOfAverages - num_actual_instances
				instancesIncrease num_instances_to_increase
			}

			lowThreshold {
				value num_actual_instances
				num_instances_to_decrease=num_actual_instances - statistics.averageOfAverages
				instancesDecrease num_instances_to_decrease
			}
		}
	])
}

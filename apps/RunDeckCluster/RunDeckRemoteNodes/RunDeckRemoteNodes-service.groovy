
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
				value 200
				instancesIncrease 1
			}

			lowThreshold {
				value 10
				instancesDecrease 1
			}
		}
	])
}

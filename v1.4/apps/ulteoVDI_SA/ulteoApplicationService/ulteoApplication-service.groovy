import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.context.ServiceContextFactory
import org.cloudifysource.dsl.utils.ServiceUtils;

service {

	name "ulteoApplicationService"
	type "WEB_SERVER"
	icon "ulteo.jpg"

    elastic true
	numInstances 1
	minAllowedInstances 1
	maxAllowedInstances 2
	
	compute {
		template "ulteoApplicationService_template"
	}	
	
	lifecycle{

		install "ulteoApplication-install.groovy"	
		start "ulteoApplication-start.groovy"	
		preStop "ulteoApplication-stop.groovy"

		locator {
			def ovdPId= ServiceUtils.ProcessUtils.getPidsWithQuery("State.Name.eq=ulteo-ovd-subsystem")
			println "IPs are : ${ovdPId}"
			return ovdPId
		 }
		
		startDetectionTimeoutSecs 120
		startDetection {
			ServiceUtils.arePortsOccupied([1112,1113,3389])
		}
		
	 monitors{
				def ulteoManagerService = context.waitForService("ulteoManagerService", 180, TimeUnit.SECONDS)
				sessionManagerInstances = ulteoManagerService.waitForInstances(ulteoManagerService.numberOfPlannedInstances, 180, TimeUnit.SECONDS)
				managerIP=sessionManagerInstances[0].hostAddress
				
				println "Checking ulteoManager DB at ${managerIP} to see if App Servers should be scaled"
				
				numSessions = "/root/numVdiSessions.sh ${managerIP} root root".execute().text
				numActiveServers = "/root/numActiveServers.sh ${managerIP} root root".execute().text

				println "Number of VDI sessions --->  : " + numSessions
				println "Number of Servers in Use ---> :" + numActiveServers
			 	return ["Number of VDI Sessions":numSessions as Integer, "Number of Servers in Use":numActiveServers as Integer]
	      }	
	}
	
	userInterface {

		metricGroups = ([
			metricGroup {

				name "process"

				metrics([
					"Number of VDI Sessions",
					"Number of Servers in Use",
					"Process Cpu Usage",
					"Total Process Virtual Memory",
					"Num Of Active Threads"
				])
			}
		])


		widgetGroups = ([
			widgetGroup {
				name "Number of VDI Sessions"
				widgets ([
					balanceGauge{metric = "Number of VDI Sessions"},
					barLineChart{
						metric "Number of VDI Sessions"
						axisYUnit Unit.REGULAR
					}
				])
			},
			widgetGroup {
				name "Number of Servers in Use"
				widgets ([
					balanceGauge{metric = "Number of Servers in Use"},
					barLineChart{
						metric "Number of Servers in Use"
						axisYUnit Unit.REGULAR
					}
				])
			},
		
		])
	}

	scaleCooldownInSeconds 10
	samplingPeriodInSeconds 1

	scalingRules ([
		scalingRule {
        
			serviceStatistics {
				metric "Number of VDI Sessions"
				//statistics Statistics.maximumOfAverages
				movingTimeRangeInSeconds 10
			}

			highThreshold {
				value 1.9
				instancesIncrease 1
			}

			lowThreshold {
				value 1.1
				instancesDecrease 1
			}
		}
	])
}

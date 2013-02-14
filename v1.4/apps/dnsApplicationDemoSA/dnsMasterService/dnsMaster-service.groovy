import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.context.ServiceContextFactory
import org.cloudifysource.dsl.utils.ServiceUtils;


service {

	name "dnsMasterService"
	type "WEB_SERVER"
	icon "dns_bind.gif"

	 elastic true
	numInstances 1
	minAllowedInstances 1
	maxAllowedInstances 4
	
	compute {
		template "dnsAppDemoSA_template"
	}
	
	lifecycle{

		install "dnsMaster-install.groovy"
		postInstall "dnsMaster-postinstall.groovy"
		start "dnsMaster-start.groovy"
		
		def instanceID = context.instanceId

		preStop "dnsMaster-stop.groovy"

		startDetectionTimeoutSecs 120
		startDetection {
			ServiceUtils.isPortOccupied(53)
		}
		
		postStart {
			def dnsLoadGeneratorService = context.waitForService("dnsLoadGeneratorService", 180, TimeUnit.SECONDS)
			
			// Assumes the default network IP is on eth0.
			// A possibly dangerous assumption but no other way to determine the IP to use.
			// Optimally, the default networking information would be passed by cloudband to the recipe in some way.
			// Then intelligent choices could be made based on that info.
			NetworkInterface ni = NetworkInterface.getByName("eth0");
			Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
			def hostAddress
			while(inetAddresses.hasMoreElements()) {
				InetAddress ia = inetAddresses.nextElement();
				if(!ia.isLinkLocalAddress()) {
					ipAddr=ia.getHostAddress();
					println("Found sessionManager IP: " + ipAddr);
					hostAddress = ipAddr;
				}
			}
			
			dnsLoadGeneratorService.invoke("addNode", "Master:${hostAddress}" as String, "SHOULDNOTSEETHIS" as String)
		}
		
		postStop {
			def dnsLoadGeneratorService = context.waitForService("dnsLoadGeneratorService", 180, TimeUnit.SECONDS)
			
			// Assumes the default network IP is on eth0.
			// A possibly dangerous assumption but no other way to determine the IP to use.
			// Optimally, the default networking information would be passed by cloudband to the recipe in some way.
			// Then intelligent choices could be made based on that info.
			NetworkInterface ni = NetworkInterface.getByName("eth0");
			Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
			def hostAddress
			while(inetAddresses.hasMoreElements()) {
				InetAddress ia = inetAddresses.nextElement();
				if(!ia.isLinkLocalAddress()) {
					ipAddr=ia.getHostAddress();
					println("Found sessionManager IP: " + ipAddr);
					hostAddress = ipAddr;
				}
			}			
			
			dnsLoadGeneratorService.invoke("removeNode", "Master:${hostAddress}" as String)
		}
		
		locator {	
			return  [] as LinkedList	
		}
	}
	
	customCommands ([
		"addSlave" : "dnsMaster_addSlave.groovy",
		"removeSlave" : "dnsMaster_removeSlave.groovy"
	])
	
	userInterface {

		metricGroups = ([
			metricGroup {

				name "process"

				metrics([
					"Process Cpu Usage",
					"Total Process Virtual Memory",
					"Num Of Active Threads",
					"Total Process Cpu Time"
				])
			}
		])

		widgetGroups = ([
			widgetGroup {
				name "Process Cpu Usage"
				widgets ([
					balanceGauge{metric = "Process Cpu Usage"},
					barLineChart{
						metric "Process Cpu Usage"
						axisYUnit Unit.PERCENTAGE
					}
				])
			},
			widgetGroup {
				name "Total Process Virtual Memory"
				widgets([
					balanceGauge{metric = "Total Process Virtual Memory"},
					barLineChart {
						metric "Total Process Virtual Memory"
						axisYUnit Unit.MEMORY
					}
				])
			},
			widgetGroup {
				name "Num Of Active Threads"
				widgets ([
					balanceGauge{metric = "Num Of Active Threads"},
					barLineChart{
						metric "Num Of Active Threads"
						axisYUnit Unit.REGULAR
					}
				])
			},
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

//	scaleCooldownInSeconds 20
//	samplingPeriodInSeconds 1
//
//	scalingRules ([
//		scalingRule {
//
//			serviceStatistics {
//				metric "Total Process Cpu Time"
//				timeStatistics Statistics.averageCpuPercentage
//			    instancesStatistics Statistics.maximum
//				movingTimeRangeInSeconds 2
//			}
//
//			highThreshold {
//				value 15
//				instancesIncrease 1
//			}
//
//			lowThreshold {
//				value 8
//				instancesDecrease 1
//			}
//		}
//	])
	
}

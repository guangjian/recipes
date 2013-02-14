import java.util.concurrent.TimeUnit
import org.cloudifysource.dsl.context.ServiceContextFactory
import org.cloudifysource.dsl.utils.ServiceUtils;

service {

	name "dnsSlaveService"
	type "WEB_SERVER"
	icon "dns_bind.gif"

    elastic "${ScalingOn}"
	numInstances "${BaseNumberOfDnsServers}"
	minAllowedInstances "${BaseNumberOfDnsServers}"
	maxAllowedInstances "${MaxAllowedDnsServers}"
	
	compute {
		template "dnsAppDemoSA_template"
	}
	
	lifecycle{
		
		monitors {
			value="/var/named/named_monitor.sh".execute().text
			return ["DNS Request Rate":value as Integer]
		}

		install "dnsSlave-install.groovy"
 	    postInstall "dnsSlave-postinstall.groovy"
		start "dnsSlave-start.groovy"
		preStop "dnsSlave-stop.groovy"

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
			
			// We pass in the basenumberofdnsservers parameter so that the code over on the dnsLoadGenerator service knows to expect this many to start
			dnsLoadGeneratorService.invoke("addNode", "Slave:${hostAddress}" as String, "${BaseNumberOfDnsServers}" as String)
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
			
			dnsLoadGeneratorService.invoke("removeNode", "Slave:${hostAddress}" as String)
		}
		
		locator {	
			return  [] as LinkedList	
		}
	}
	
	userInterface {

		metricGroups = ([
			metricGroup {

				name "DNS"

				metrics([
					"DNS Request Rate"
				])
			}
		])

		widgetGroups = ([
			widgetGroup {
				name "DNS Request Rate"
				widgets ([
					balanceGauge{metric = "DNS Request Rate"},
					barLineChart{
						metric "DNS Request Rate"
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
				metric "DNS Request Rate"
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

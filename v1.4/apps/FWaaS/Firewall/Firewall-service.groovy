
service {
	
	name "Firewall"

	
	type "APP_SERVER"
	elastic false
	numInstances 1	
	
	compute {
		template "FWaaS_template"
	}	
	
	lifecycle{
 
		start "Firewall_start.sh"
				
		startDetectionTimeoutSecs 900
		startDetection {			
			ServiceUtils.isPortOccupied(22)
		}	
		
		stopDetection {	
			!ServiceUtils.isPortOccupied(22)
		}
		
			
		// Using null locator at this time to just test out the ability to deploy the thing.
		locator {	
			return  [] as LinkedList	
			 
        }	
	}
	

	userInterface {
		metricGroups = ([
			metricGroup {
				name "server"

				metrics([
					"Server Uptime",
				])
			}
		])

		widgetGroups = ([
			widgetGroup {
           			name "Server Uptime"
            		widgets ([
               		barLineChart{
                  		metric "Server Uptime"
                  		axisYUnit Unit.REGULAR
							},
            		])
						
			}, 

		])
	}  
}

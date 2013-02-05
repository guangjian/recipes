service {
	
	name "webService"

	
	type "WEB_SERVER"
	elastic false
	numInstances 1	
	
	compute {
		template "tmpl_webGreeter"
	}	
	
	lifecycle{
 
		start "webService_start.groovy"
				
		startDetectionTimeoutSecs 900
		startDetection {			
			ServiceUtils.isPortOccupied(80)
		}	
		
		stopDetection {	
			!ServiceUtils.isPortOccupied(80)
		}
		
			
		// Nothing to locate really.
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


import groovy.sql.*

service {
	
	name "presenceService"

	
	type "APP_SERVER"
	elastic false
	numInstances 1	
	
	compute {
		template "tmpl_20130121c5_2"
	}	
	
	lifecycle{
 
		// This does the unsuspend right away.
		start "presenceService_start.groovy"
		
		// This sets up a web interface.
		// We do this after the start since we want the unsuspend to occur as quickly as possible.
		postStart "presenceService_postStart.groovy"
		
		
		stop "presenceService_stop.groovy"
				
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

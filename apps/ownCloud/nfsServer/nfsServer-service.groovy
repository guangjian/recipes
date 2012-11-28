
import groovy.sql.*

service {
	
	name "NFS"

	
	type "APP_SERVER"
	elastic false
	numInstances 1	
	
	compute {
		template "SMALL_LINUX"
	}	
	
	lifecycle{
 
		install "nfsServer_install.sh"

		start "nfsServer_start.sh"
				
		startDetectionTimeoutSecs 900
		startDetection {			
			ServiceUtils.isPortOccupied(2049)
		}	
		
		stopDetection {	
			!ServiceUtils.isPortOccupied(2049)
		}
		
			
		// Nothing to locate really.
		// Well I could look for the nfs daemon ... but for now, we'll not
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

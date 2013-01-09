import java.util.concurrent.TimeUnit;

service {
	name "owncloud"
	type "WEB_SERVER"
	
  elastic false
	numInstances 2
	minAllowedInstances 2
	maxAllowedInstances 4
	
	compute {
		template "QUAD_ROOT_ACCESS"
	}

	lifecycle {
		install "install.sh"
		postInstall "ownCloudFE_postInstall.groovy"
		start "owncloud_start.groovy"
		preStop "owncloud_stop.groovy"
		
		locator {
			return [] as LinkedList
			}
			
		}



}

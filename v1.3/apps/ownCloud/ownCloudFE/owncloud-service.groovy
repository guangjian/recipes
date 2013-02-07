import java.util.concurrent.TimeUnit;

service {
	name "owncloud"
	type "WEB_SERVER"
	
  elastic false
	numInstances 1
	minAllowedInstances 1
	maxAllowedInstances 2
	
	compute {
		template "BIG_MEDIUM"
	}

	lifecycle {
		install "install.sh"
		start "owncloud_start.groovy"

		locator {
			return [] as LinkedList
			}


		}



}

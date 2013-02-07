import java.util.concurrent.TimeUnit;

service {
	name "owncloud"
	type "WEB_SERVER"
	
  elastic false
	numInstances 1
	minAllowedInstances 1
	maxAllowedInstances 2
	
	// See wiki for template inventory and description.
	compute {
		template "tmpl_20130121c5_4"
	}

	lifecycle {
		start "owncloud_start.groovy"

		locator {
			return [] as LinkedList
			}


		}



}

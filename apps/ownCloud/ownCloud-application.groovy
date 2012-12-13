application {
	
	name="ownCloud"
	
	service {
		name = "mysql"
	}
	
	service {
		name = "nfsServer"
	}

	service {
		name = "HAproxy"
	}

	service {
		name = "ownCloudFE"
			dependsOn = ["mysql", "nfsServer", "HAproxy" ]
	}
}

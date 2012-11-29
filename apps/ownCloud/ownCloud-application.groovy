application {
	
	name="ownCloud"
	
	service {
		name = "mysql"
	}
	
	service {
		name = "nfsServer"
	}

	service {
		name = "ownCloudFE"
			dependsOn = ["mysql", "nfsServer" ]
	}
}

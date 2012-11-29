application {
	
	name="ownCloud"
	
	service {
		name = "mysql"
	}
	
	service {
		name = "nfsServer"
	}

	service {
		name = "ownClouFE"
			dependsOn = ["mysql", "nfsServer" ]
	}
}

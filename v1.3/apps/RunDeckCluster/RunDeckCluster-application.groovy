application {
	
	name="RunDeckCluster"
	
	service {
		name = "RunDeckRemoteNodes"
			dependsOn = ["RunDeckService"]
		
	}
	
	service {
		name = "RunDeckService"
	}
}

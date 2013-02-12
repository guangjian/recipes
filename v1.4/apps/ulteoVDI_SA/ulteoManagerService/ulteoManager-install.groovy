import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory

println "###### > Installing Ulteo Session Manager"

def context = ServiceContextFactory.getServiceContext()

// Store the sessionManager IP for later use by the applicationServers
// Assumes the default network IP is on eth0. 
// A possibly dangerous assumption but no other way to determine the IP to use.
// Optimally, the default networking information would be passed by cloudband to the recipe in some way.
// Then intelligent choices could be made based on that info.
NetworkInterface ni = NetworkInterface.getByName("eth0");
Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();

while(inetAddresses.hasMoreElements()) {
	InetAddress ia = inetAddresses.nextElement();
	if(!ia.isLinkLocalAddress()) {
		ipAddr=ia.getHostAddress();
		println("Found sessionManager IP: " + ipAddr);
		context.attributes.thisApplication["sessionManagerIP"] = ipAddr;
	}
}

// run the install script
builder = new AntBuilder()
builder.sequential {
	chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")
	echo(message:"Running ${context.serviceDirectory}/installOnLinux.sh ...")
	exec(executable: "${context.serviceDirectory}/installOnLinux.sh", osfamily:"unix", failonerror: "true")
}



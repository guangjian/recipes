import org.cloudifysource.usm.USMUtils
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory

println "###### > Installing Ulteo Session Manager"

context = ServiceContextFactory.getServiceContext()

builder = new AntBuilder()
builder.sequential {
	chmod(dir:"${context.serviceDirectory}", perm:"+x", includes:"*.sh")
	echo(message:"Running ${context.serviceDirectory}/installOnLinux.sh ...")
	exec(executable: "${context.serviceDirectory}/installOnLinux.sh", osfamily:"unix", failonerror: "true")
}



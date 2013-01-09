
import org.hyperic.sigar.OperatingSystem
import org.cloudifysource.dsl.utils.ServiceUtils;
import org.cloudifysource.dsl.context.ServiceContextFactory

context = ServiceContextFactory.getServiceContext()
config = new ConfigSlurper().parse(new File("ownCloudFE-service.properties").toURL())

php_ini_file="/etc/php.ini"
upload_max_filesize="${config.upload_max_filesize}"
post_max_size="${config.post_max_size}"


// Tweak the PHP settings to allow larger files to be uploaded

builder = new AntBuilder()
builder.sequential {
	// Now substitute in the various bits based on this specific installation
	replaceregexp(file:"${php_ini_file}",
		match:"upload_max_filesize.*=.*",
		replace:"upload_max_filesize = ${upload_max_filesize}",
		flags: "g")
	replaceregexp(file:"${php_ini_file}",
		match:"post_max_size.*=.*",
		replace:"post_max_size = ${upload_max_filesize}",
		flags: "g")
}
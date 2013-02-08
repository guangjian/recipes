import groovy.util.ConfigSlurper
import java.util.concurrent.TimeUnit


println "###### > Stopping Ulteo Session Manager"

builder = new AntBuilder();
builder.sequential { 
		exec(executable: 'service' ) {
						 arg value:"httpd"
						 arg value:"stop"
						}
}

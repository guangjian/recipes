 import groovy.util.ConfigSlurper

println "###### > Starting DNS Slave Service"

builder = new AntBuilder();

builder.sequential { 
		exec(executable: 'service' ) {
						 arg value:"named"
						 arg value:"restart"
						}
}

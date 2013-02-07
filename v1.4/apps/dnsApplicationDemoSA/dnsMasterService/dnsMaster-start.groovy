 import groovy.util.ConfigSlurper

println "###### > Starting DNS Master Service"

builder = new AntBuilder();
						
builder.sequential { 
		exec(executable: '/usr/sbin/named' ) {
						 arg value:"-g"
						}
}

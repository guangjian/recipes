#!/bin/bash

# args:
# $1 A command separated list of my.cnf section names
# $2 A command separated list of my.cnf variable names
# $3 A command separated list of my.cnf values for the above variable names
# 
# 

sectionNames=$1
variableNames=$2
newValues=$3


# args:
# $1 the error code of the last command (should be explicitly passed)
# $2 the message to print in case of an error
# 
# an error message is printed and the script exists with the provided error code
function error_exit {
	echo "$2 : error code: $1"
	exit ${1}
}

function killMySqlProcess {
	ps -ef | grep -iE "mysql" | grep -ivE "gigaspaces|GSC|GSA|grep"
	if [ $? -eq 0 ] ; then 
		ps -ef | grep -iE "mysql" | grep -ivE "gigaspaces|GSC|GSA|grep" | awk '{print $2}' | xargs kill -9
	fi  
}

export PATH=$PATH:/usr/sbin:/sbin:/usr/bin || error_exit $? "Failed on: export PATH=$PATH:/usr/sbin:/sbin"

# The existence of the usingYum file in the ext folder will later serve as a flag that "we" are on Red Hat or CentOS or Fedora or Amazon
echo "Using yum. Updating yum on one of the following : Red Hat, CentOS, Fedora, Amazon. " > usingYum
yum -y -q update || error_exit $? "Failed on: yum -y -q update"

echo "#1 Killing old mysql process if exists..."
killMySqlProcess

echo "Removing previous mysql installation if exists..."
yum -y -q remove mysql mysql-server

# The following two statements are used since in some cases, there are leftovers after uninstall
echo "Removing old stuff if exists..."
rm -rf /usr/lib/mysql* || error_exit $? "Failed on: rm -rf /usr/lib/mysql*"
rm -rf /var/lib/mysql* || error_exit $? "Failed on: rm -rf /var/lib/mysql*"
rm -rf /usr/share/mysql* || error_exit $? "Failed on: rm -rf /usr/sharemysql*"
rm -rf /usr/bin/mysql* || error_exit $? "Failed on: rm -rf /usr/bin/mysql*"
rm -rf /var/run/mysql* || error_exit $? "Failed on: rm -rf /var/run/mysql*"
rm -rf /var/bin/mysql* || error_exit $? "Failed on: rm -rf /var/bin/mysql*"
rm -rf /etc/mysql* || error_exit $? "Failed on: rm -rf /etc/mysql*"
rm -rf /etc/rc.d/init.d/mysql* || error_exit $? "Failed on: rm -rf /etc/rc.d/init.d/mysql*"
rm -rf /usr/libexec/mysql* || error_exit $? "Failed on: rm -rf /usr/libexec/mysqld*" 
rm -rf /etc/my.cnf || error_exit $? "Failed on: rm -rf /etc/my.cnf" 
rm -rf /var/log/mysql* || error_exit $? "Failed on: rm -rf /var/log/mysql*" 
#rm -f /home/`whoami`/{.,}*mysql* || error_exit $? "Failed on: rm -f /home/`whoami`/{.,}*mysql*" 

echo "Using yum. Installing mysql on one of the following : Red Hat, CentOS, Fedora, Amazon"
yum install -y -q mysql mysql-server || error_exit $? "Failed on: yum install -y -q mysql mysql-server "
echo "Reinstalling mysql-libs ..."
yum reinstall -y -q mysql-libs || error_exit $? "Failed on: yum install -y -q mysql mysql-server "

echo "Killing old mysql process if exists b4 ending the installation..."
killMySqlProcess

# This allows customization of my.cnf from the properties file.
sectionNamesLen=`expr length "$sectionNames"`
if [ $sectionNamesLen -gt 0 ] ; then

	myCnfPath=`find / -name "my.cnf"`
	if [ -f "${myCnfPath}" ] ; then

		sectionNames=$1
		variableNames=$2
		newValues=$3

		IFS=, read -a sectionNamesArr <<< "$sectionNames"
		IFS=, read -a variableNamesArr <<< "$variableNames"
		IFS=, read -a newValuesArr <<< "$newValues"
		echo "IFS is ${IFS}"
		echo "${sectionNamesArr[@]}"
		echo "${variableNamesArr[@]}"
		echo "${newValuesArr[@]}"

		variableCounter=${#variableNamesArr[@]}

		for (( i=0; i<${variableCounter}; i++ ));
		do
			currSection="\[${sectionNamesArr[$i]}\]"
			currVariable="${variableNamesArr[$i]}"
			currNewValue="${newValuesArr[$i]}"
			currNewLine="${currVariable}=${currNewValue}"
			echo "Commenting $currVariable in $myCnfPath ... "
			sed -i -e "s/^$currVariable/#$currVariable/g" $myCnfPath
			
			jointStr="${currSection}\n${currNewLine}"
			echo "Setting ${currNewLine} in the ${currSection} section of $myCnfPath ... "
			sed -i -e "s/$currSection/$jointStr/g" $myCnfPath		
		done
	fi
fi
echo "End of $0"



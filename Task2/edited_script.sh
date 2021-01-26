#!/bin/bash
function isinstalled
{
	echo "Checking is $1 installed"
	if yum list installed "$1"; #Checking is argument is installed by yum, as result change global vaiable result
	then
		echo "$1 is already installed"
		result=1
	else
		echo "$1 isn't installed"
		result=0
	fi
}
function javaVer
{
        a=$( (java -version) 2>&1)
        if [[ "$a" == *"OpenJDK Runtime"* ]]	#Checking if java installed  TODO make check of java version but it bot so important cause if java is lower version than maven needed(1.7) maven will install it
        then
                result=1
	else
		result=0
        fi
}
public=1	#Variable that controlls output stream
mvns=1		#Scope of variables that controlled installition of packages if user would like to not install some packages some of this variables will became 0
gits=1
psqls=1

while [ -n "$1" ]
do
case "$1" in
-v) public=0 ;;
-verbose) public=0 ;;
-h) echo "Hi this script made for installation java 1.8, maven, git, PostgreSql"; echo "If you want to install all this packages just execute this script"; echo "-<v> or -<verbose> will show installition process"; echo "-<mvn> wouldnt install maven on your machine"; echo "-<git> wouldnt install git on your computer"; echo "-<psql> wouldn't install PostgreSql on your computer"; echo "You can combibe all flags and install as you want"; echo "NOTE*: If you already installed PostGreSql but didn't delted it properly (with initdb) script wouldnt work properly"; echo "NOTE**: Java would install on your machine with any parameters"; exit 0 ;;
-mvn)echo "Maven woudnt install on your machine"; mvns=0 ;;
-git)echo "Git woudnt install on your machine"; gits=0 ;;
-psql)echo "PostGreSql woudnt install on your machine"l; psqls=0;;
*) echo "$1 is not an option" ;;
esac
shift
done

mvn="maven"
git="git"
psql="postgresql-server"
javaVer		#Checking if java installed
one=1
if [ $result -eq $one ];then			#If java installed result equals 1
        echo "Checking next package"
else
        echo "Installing java"
	if [ $public -eq $one ];then
                sudo yum install java-1.8.0-openjdk-devel -y > /dev/null 	#Installing java but transfer output stream in /dev/null
	else
		sudo dnf install java-1.8.0-openjdk-devel -y			#Installing java output stream wouldn't hided
	fi
fi
if [ $mvns -eq $one ];then		#Checking if user would like to install maven
	isinstalled "$mvn"		#Checking if maven installed on machine
else
	result=1
	echo "User doesn't want to install maven"
fi
username=$USER				#Getting username and keep it in variable
if [ $result -eq $one ];then
	echo "Checking next package"
else
	 echo "Installing maven"
	if [ $public -eq $one ];then
                sudo dnf install maven -y  > /dev/null				#Installing maven, output stream is transfered
        else
                sudo dnf install maven -y					#Installing maven, output stream isn't transfered
        fi
fi
if [ $gits -eq $one ];then		#Checking if user'd like to install git
        isinstalled "$git"		#Checking is git already installed on machine
else
        result=1
        echo "User doesn't want to install git"
fi
if [ $result -eq $one ];then
        echo "Checking next package"
else
        echo "Installing git"
	if [ $public -eq $one ];then
                sudo dnf install git -y  > /dev/null				#Installing git, output stream is transfered
        else
                sudo dnf install git -y						#Installing git, output stream isn't transfered
        fi
fi
if [ $psqls -eq $one ];then		#Checking if user would like to install PostGreSql
        isinstalled "$psql"		#Checking if PostGreSql is already installed
else
        result=1
        echo "User doesn't want to install psql"
fi
if [ $result -eq $one ];then
        echo "Ending the script ..."
else
        echo "Installing postgresql"
	if [ $public -eq $one ];then						#There is 2 blocks of codes only difference is that in first block output stream is transfered in /dev/null but in second block of code output isn't transfered
                sudo dnf module enable postgresql:12  > /dev/null		#Choosing postgresql version if user cannot have 12 version installation will broke TODO make if that checked version and install last one
        	sudo dnf install postgresql-server -y  > /dev/null		#Installing postgresql version
        	echo "postgresql-server installed"				#TODO Here we dont check but if user deleted postGreSql not correct and initdb folder is already created than we cannot setup user
        	sudo postgresql-setup --initdb  > /dev/null			#Creating initdb folder
        	sudo systemctl start postgresql  > /dev/null			#starting postgresql
        	sudo systemctl enable postgresql  > /dev/null
        	sudo -i -u postgres  > /dev/null				#going inside postgres
        	createuser "$username" > /dev/null				#creating new user in postgres that have same username as user in system
        	createdb "$username" > /dev/null				#creating database of our user it helps our user to creates dbs
        	exit
        else
                 sudo dnf module enable postgresql:12
        	sudo dnf install postgresql-server -y
        	echo "postgresql-server installed"
        	sudo postgresql-setup --initdb
        	sudo systemctl start postgresql
        	sudo systemctl enable postgresql
        	sudo -i -u postgres
        	createuser "$username"
        	createdb "$username"
        	exit
        fi
fi

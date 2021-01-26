#!/bin/bash
isDB=0;
function DbExist(){
	isDB=1;
}
out=0
year=""
month=""
lat=""
lng=""
db=""

while [ -n "$1" ]
do
case "$1" in
-v | -verbose) out=1 ;;
-h)	echo "If you want to run this script you should have to install Postgresql 9.5, set up user w/ username 'postgres' and password 'root' and database named 'postgres' that belongs to user postrges, install java maven and git" ;
	echo "To startup this script correctly u have to choose startup options: ";
	echo "Write -db if you want to save data in database or -f if u want save data to file";
	echo "-m and month number to choose month";
	echo "-y and year number to choose year";
	echo "-lng and needed longitude";
	echo "-lat and needed latitude";
	echo "To correct start you need to run script with 5 parametars";
	echo "If you want to clear database run -clear";
	exit 0;;
-y) year="$2";;
-m) month="$2" ;;
-lat) lat="$2";;
-lng) lng="$2";;
-db) db="-db";;
-f) db="-f";;
-clear);;
*) echo "$1 is not an option" ;;
esac
shift
done
RunScript=0
echo "Please input password to postgres";
if [ isDB -eq 0]; then
	cd ..
	cd sql_part/
	psql -U postgres -f script.sql
	\q
fi
cd ..
cd java_part/
mvn compile exec:java -Dexec.mainClass="by.sanko.epamLab.task3.main.Main" \
  -Dexec.args="$db -y $year -m $month -lat $lat -lng $lng"

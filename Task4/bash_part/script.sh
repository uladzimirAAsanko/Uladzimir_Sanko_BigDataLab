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
java=0
a=""
sas=""
a1=0
a2=0
a3=0
a4=0
a5=0
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
-y) year="-y $2";;
-m) month="-m $2" ;;
-lat) lat="-lat $2";;
-lng) lng="-lng $2";;
-db) db="-db";;
-f) db="-f";;
-java) java=1;;
-a) a="-a";;
-sas) sas="-sas";;
-1) a1=1;;
-2) a2=1;;
-3) a3=1;;
-4) a4=1;;
-5) a5=1;;
-6) a6=1;;
esac
shift
done
RunScript=0
cd ..
cd sql_part/
psql -U postgres -f create_db_script.sql
\q
if [ a1 -eq 1 ]; then
	psql -U postgres -f 1_most_dng_streets.sql
fi
if [ a2 -eq 1 ]; then
        psql -U postrgres -f 2_comp_monthes.sql
fi
if [ a3 -eq 1 ]; then
        psql -U postgres -f 3_crimes_with_statuses.sql
fi
if [ a4 -eq 1 ]; then
        psql -U postgres -f 4_ethnicity.sql
fi
if [ a5 -eq 1 ]; then
        psql -U postgres -f 5_most_probable.sql
fi
if [ a6 -eq 1 ]; then
        psql -U postgres -f 6_correlation.sql
fi


cd ..
if [ java -eq 1 ]; then
	cd java_part/target
	java -jar java_part-1.0-SNAPSHOT-shaded.jar -a $db $a $sas $lat -lng -d 2018-1:2018-1
fi

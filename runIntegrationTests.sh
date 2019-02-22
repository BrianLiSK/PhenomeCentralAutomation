#!/usr/bin/env bash

PCZipLocation=""
PCZipName=""

browser="chrome"
PCInstancePort="8083"
PCInstanceURL="localhost:$PCInstancePort"
PCInstanceStopPort="8087"
emailUIPort="8085"
curDate=date
PCFolderName=PCInstance_$curDate


CURL_OUTPUT_FILE=curlOutput.temp
CURL_OUTPUT_FILE2=curlOutput2.temp

# Taken from start.sh
# Ensure that the commands below are always started in the directory where this script is
# located. To do this we compute the location of the current script.
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
PRGDIR=`dirname "$PRG"`
cd "$PRGDIR"

echo "ProgramDir is listed as: $PRGDIR"

rm $CURL_OUTPUT_FILE
rm $CURL_OUTPUT_FILE2
rm stdout.out

#mkdir $PCFolderName
#cp $PCZipLocation $PCFolderName/
#unzip $PCFolderName/$PCZipName 
#cd $PCZipName

startServer() {
	#bash --rcfile ./start.sh ;
	./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/start.sh 2>&1 | tee stdout.out &
	#./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/start.sh  &> serverOut.log &
	sleep 60
	echo "Started server"
}

stopServer() {
	./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/stop.sh | tee -a stdout.out
	wait $!
	echo "Stopped Server"
}

#Make a CURL request to see if the server has started or not
checkServer() {
	curl $PCInstanceURL &>> $CURL_OUTPUT_FILE
	return=$?

	if test "$return" != "0"; then
		echo "the curl command failed with: $return"
	else
		echo "curl command sucess"
	fi
}

#Make a CURL request to see if the server has started or not
checkServer2() {
	curl $PCInstanceURL &>> $CURL_OUTPUT_FILE2
	return=$?

	if test "$return" != "0"; then
		echo "the curl command failed with: $return"
	else
		echo "curl command sucess"
	fi
}

checkServer

startServer

checkServer2

stopServer



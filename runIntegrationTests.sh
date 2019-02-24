#!/usr/bin/env bash

browser="chrome"
PCInstancePort="8083"
PCInstanceURL="localhost:$PCInstancePort"
PCInstanceStopPort="8087"
emailUIPort="8085"
outGoingEmailPort="1025"
curDate=$(date +'%s')
zipExtract="PCInstance_$curDate/"


PCZipLocation=""
PCZipName=""
distPath="../standalone/distribution/"
distZip="phenomecentral-standalone*.zip"
logFile="logFile$curDate.txt"
startPCInstanceCommand="./start.sh"
stopPCInstanceCommand="./stop.sh"
startSMTPCommand="java -jar MockMock.jar"
SMTPPID=""
startingMessage="Phenotips is initializing"


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

echo "ProgramDir is calculated as: $PRGDIR"

# rm $CURL_OUTPUT_FILE
# rm $CURL_OUTPUT_FILE2
# rm stdout.out

#mkdir $zipExtract
#cp $PCZipLocation $zipExtract/
#unzip $zipExtract/$PCZipName 
#cd $PCZipName

touch "$logFile"
pwdir="$(pwd)"
logFile="$pwdir$logFile"

extractZip() {
	# Go to distribution folder of PC and check for zips there.
	# Should do this because ls might give full path of file (instead of just filename) if we do not cd into the directory. Dependent on unix flavour.
	cd "$distPath"

	# ls giving filenames only sorted by most recently modified descending
	local numberOfZipsFound=$(ls $distZip -t1 | wc -l)
	PCZipName=$(ls $distZip -t1 | head -n 1)

	if [[ $numberOfZipsFound -eq 0 ]]; then
		echo "No zips following pattern of $distZip were found in $distPath. Exiting." | tee -a "$logFile"
		exit
	elif [[ $numberOfZipsFound -gt 1 ]]; then
		echo "More than one zip following pattern of $distZip were found in $distPath. Not sure which one to use. Exiting." | tee -a "$logFile"
		exit
	else
		echo "Found $PCZipName in $distPath" | tee -a "$logFile"
		# Return to where we were
		cd -

		mkdir "$zipExtract"
		unzip $distPath$PCZipName -d "$zipExtract" 2>&1 | tee -a "$logFile"

		echo "Extracted $PCZipName to $zipExtract" | tee -a "$logFile"
	fi
}

startInstance() {
	zipDir=${PCZipName%????} # Cut off last 4 chars of PCZipName
	cd "$zipExtract$PCZipName"
	echo "Starting server on port $PCInstancePort and stop port $PCInstanceStopPort" | tee -a "$logFile"
	"$startPCInstanceCommand $PCInstancePort $PCInstanceStopPort" 2>&1 | tee -a "$logFile" &
	sleep 60
	echo "Waited 60 seconds for server to start. Now check with curl command" | tee -a "$logFile"
}

checkForStart() {
	local curlResult=$(curl "$PCInstanceURL")
	local curlReturn=$?

	if test "curlReturn" != "0"; then
		echo "Curl to $PCInstanceURL has failed. Wait 10 secs on try again" | tee -a "$logFile"
		sleep 10
		checkForStart
	else
		echo "Response recieved on curl to $PCInstanceURL." | tee -a "$logFile"
		local startingMessageFound=$(curl "$PCInstanceURL" | grep -c "$startingMessage")

		if [[ "$startingMessageFound" -eq 0 ]]; then
			echo "It seems instance has sucessfully started and is ready." | tee -a "$logFile"
		else
			echo "Instance is not ready yet. Wait 10 seconds and ping again" | tee -a "$logFile"
			sleep 10
			checkForStart
		fi
	fi
}

stopInstance() {
	echo "Stopping instance that was started on port $PCInstancePort and stop port $PCInstanceStopPort" | tee -a "$logFile"
	"$stopPCInstanceCommand $PCInstanceStopPort"
}

startSMTP() {
	echo "Starting SMTP (MockMock). Listening on $outGoingEmailPort. Email UI is at $emailUIPort" | tee -a "$logFile"
	"$startSMTPCommand -p $outGoingEmailPort -h $emailUIPort" &
	SMTPPID=$!
	sleep 30
}

stopSMTP() {
	echo "Killing SMTP" | tee -a "$logFile"
	kill -INT $SMTPPID
}

extractZip
startSMTP
startInstance
#checkForStart
stopInstance
stopSMTP

startServer() {
	#bash --rcfile ./start.sh ;
	./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/start.sh 2>&1 | tee -a stdout.out &
	#./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/start.sh  &> serverOut.log &
	sleep 60
	echo "Started server"
}

stopServer() {
	./../pcZipExtract/pt_1.4.4/phenomecentral-standalone-1.2-SNAPSHOT/stop.sh | tee -a -a stdout.out
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

# checkServer

# startServer

# checkServer2

# stopServer



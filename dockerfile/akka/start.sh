#!/bin/bash

if [ -z $1 ]
then
    echo "start parmeter is empty!"
    exit 1
fi

if [ $1 = "outstream" ]; then
   java -Dfile.encoding=utf8 -cp /opt/akka/lib/*:/opt/akka/config/:/opt/akka/smack-akka.jar com.smack.DataOutputServer 2551
else
    java -Dfile.encoding=utf8 -cp /opt/akka/lib/*:/opt/akka/config/:/opt/akka/smack-akka.jar com.smack.DataInputServer 2551
fi
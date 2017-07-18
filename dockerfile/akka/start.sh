#!/bin/bash

if [ -z $1 ]
then
    echo "start parmeter is empty!"
    exit 1
fi

RPC_HOST=`ping rpc -c 1 | sed '1{s/[^(]*(//;s/).*//;q}'`

echo $RPC_HOST

if [ $1 = "outstream" ]; then
   sed -i "s#RPC_HOST#$RPC_HOST#" /opt/akka/config/data-output.conf
   java -Dfile.encoding=utf8 -cp /opt/akka/ext/*:/opt/akka/lib/*:/opt/akka/config/:/opt/akka/smack-akka.jar com.smack.DataOutputServer 2551
else
    sed -i "s#RPC_HOST#$RPC_HOST#" /opt/akka/config/data-input.conf
    java -Dfile.encoding=utf8 -cp /opt/akka/ext/*:/opt/akka/lib/*:/opt/akka/config/:/opt/akka/smack-akka.jar com.smack.DataInputServer 2551
fi
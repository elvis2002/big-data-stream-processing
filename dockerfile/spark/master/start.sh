#!/bin/bash

NET_IP=${POD_NET_IP:-127.0.0.1}

/usr/spark-2.1.0/bin/spark-class org.apache.spark.deploy.master.Master --host $NET_IP
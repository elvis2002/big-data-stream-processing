FROM java:openjdk-8-jdk

RUN echo "Asia/Shanghai" > /etc/timezone;dpkg-reconfigure -f noninteractive tzdata

RUN apt-get update && apt-get install -y net-tools vim telnet

VOLUME /data

RUN apt-get update \
 && apt-get install -y curl unzip \
    python3 python3-setuptools \
 && easy_install3 pip py4j \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

ENV PYTHONHASHSEED 0
ENV PYTHONIOENCODING UTF-8

RUN mkdir -p /opt/bin

ADD start.sh /opt/bin/

ENV HD_VERSION 2.7.3

RUN wget http://www-eu.apache.org/dist/hadoop/common/hadoop-$HD_VERSION/hadoop-$HD_VERSION.tar.gz -O /opt/bin/hadoop-$HD_VERSION.tar.gz && tar -zxf /opt/bin/hadoop-$HD_VERSION.tar.gz -C /usr/local/ && rm -rf /opt/bin/hadoop-$HD_VERSION.tar.gz && rm -rf /usr/local/hadoop-$HD_VERSION/share/doc 

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

ENV PATH $PATH:$JAVA_HOME/bin

#spark start

ENV HADOOP_HOME /usr/local/hadoop-$HD_VERSION
ENV SPARK_VERSION 2.1.0
ENV SPARK_PACKAGE spark-$SPARK_VERSION-bin-without-hadoop
ENV SPARK_HOME /usr/spark-$SPARK_VERSION
ENV SPARK_DIST_CLASSPATH="$HADOOP_HOME/etc/hadoop/*:$HADOOP_HOME/share/hadoop/common/lib/*:$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/hdfs/*:$HADOOP_HOME/share/hadoop/hdfs/lib/*:$HADOOP_HOME/share/hadoop/hdfs/*:$HADOOP_HOME/share/hadoop/yarn/lib/*:$HADOOP_HOME/share/hadoop/yarn/*:$HADOOP_HOME/share/hadoop/mapreduce/lib/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/tools/lib/*"
ENV PATH $PATH:$SPARK_HOME/bin
RUN curl -sL --retry 3 \
  "http://d3kbcqa49mib13.cloudfront.net/$SPARK_PACKAGE.tgz" \
  | gunzip \
  | tar x -C /usr/ \
  && mv /usr/$SPARK_PACKAGE $SPARK_HOME \
  && rm -rf $SPARK_HOME/examples $SPARK_HOME/ec2

RUN chmod +x /opt/bin/*.sh && chmod 1777 tmp

CMD ["/opt/bin/start.sh"]

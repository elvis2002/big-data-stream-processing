FROM java:openjdk-8-jdk

RUN echo "Asia/Shanghai" > /etc/timezone;dpkg-reconfigure -f noninteractive tzdata

RUN apt-get update && apt-get install -y net-tools vim telnet

RUN mkdir -p /opt/bin

COPY flink /opt/flink

COPY flink-app /opt/flink-app

COPY start.sh /opt/bin/start.sh

RUN chmod +x /opt/bin/*.sh

CMD ["/opt/bin/start.sh"]

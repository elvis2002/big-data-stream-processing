FROM spark_master:2.7.3_2.10_1

RUN mkdir -p /opt/bin

COPY spark /opt/spark

COPY start.sh /opt/bin/start.sh

RUN chmod +x /opt/bin/*.sh

CMD ["/opt/bin/start.sh"]
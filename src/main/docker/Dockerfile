FROM java:8
VOLUME /tmp
EXPOSE 9000
ADD transactions-0.0.1.jar app.jar
COPY run.sh /usr/bin/run.sh

ENV JAVA_OPTS="-Xmx512m"

RUN bash -c 'touch /app.jar' && chmod a+x /usr/bin/run.sh

ENTRYPOINT ["/usr/bin/run.sh"]
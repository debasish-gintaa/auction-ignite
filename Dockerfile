FROM centos
RUN yum install -y java-11-openjdk-devel
VOLUME /tmp
ADD target/demo-0.0.1-SNAPSHOT.jar myjfilter.jar
RUN sh -c 'touch /myjfilter.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myjfilter.jar"]
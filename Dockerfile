FROM anapsix/alpine-java
MAINTAINER Michael Smith
COPY build/libs/courses-all-1.0.jar /root/run.jar
CMD ["java","-jar","/root/run.jar"]
EXPOSE 4567

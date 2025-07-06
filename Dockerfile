FROM openjdk:17-jdk-oracle
MAINTAINER example.com
EXPOSE 8080
COPY target/demo-0.1.jar demo-0.1.jar
ENTRYPOINT ["java","-jar","/demo-0.1.jar"]
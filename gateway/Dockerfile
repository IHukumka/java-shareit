FROM amazoncorretto:11-alpine-jdk
#ENV TZ=Europe/Moscow
COPY target/*.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar"]
FROM openjdk:8-jdk-alpine
RUN addgroup -S springUser && adduser -S springUser -G springUser
USER springUser:springUser
ADD maven/server-1.0-SNAPSHOT.jar .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} server-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/server-1.0-SNAPSHOT.jar"]
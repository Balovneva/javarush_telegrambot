FROM openjdk:17-jdk
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=name
ENV BOT_TOKEN=token
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dbot.username=${BOT_NAME}","-Dbot.token=${BOT_TOKEN}","-jar","/app.jar"]
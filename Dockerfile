FROM openjdk:17-jdk
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=test_javarush_community_bal_bot
ENV BOT_TOKEN=6327388309:AAFTDUepCEGY3hRCpN-5OebHgCnwrfg6Tag
ENV BOT_DB_USERNAME=jrtb_db_user
ENV BOT_DB_PASSWORD=jrtb_db_password
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dbot.username=${BOT_NAME}","-Dbot.token=${BOT_TOKEN}","-Dbot.datasource.username=${BOT_DB_USERNAME}","-Dbot.datasource.password=${BOT_DB_PASSWORD}","-jar","/app.jar"]
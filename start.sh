#!/bin/bash

# Pull new changes
git pull

# Prepare jar
mvn clean
mvn package

# Ensure, that docker-compose stopped
docker-compose stop

# Add environment variables
export BOT_NAME=$1
export BOT_TOKEN=$2
export BOT_DB_USERNAME='jrtb_db_user'
export BOT_DB_PASSWORD='jrtb_db_password'

# Start new deployment
docker-compose up --build -d
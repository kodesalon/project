#!/bin/bash

REPOSITORY=/home/ec2-user/app/jenkins

echo "> Build 파일 복사"

cp $REPOSITORY/build/libs/*.jar $REPOSITORY/jar/

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/jar/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
    -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-prod-db.yml \
		-Dlogging.config=classpath:/logback-spring.xml \
    -Dspring.profiles.active=prod \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

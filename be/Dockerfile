FROM eclipse-temurin:17-jdk-alpine
COPY ./be/build/libs/*SNAPSHOT.jar airdnb.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "airdnb.jar"]

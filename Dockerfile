# Based on https://spring.io/guides/gs/spring-boot-docker/
FROM lpicanco/java11-alpine
COPY build/libs/recipe-services-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources src/main/resources
ARG branch
ENV branch_env=$branch
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${branch_env}","/app.jar"]


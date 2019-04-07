FROM openjdk:8-alpine

COPY target/uberjar/githubinfo.jar /githubinfo/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/githubinfo/app.jar"]

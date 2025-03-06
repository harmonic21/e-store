FROM openjdk:21-oracle
RUN mkdir /app
COPY ./simple-electronic-store/target/*.jar /app/application.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/application.jar"]
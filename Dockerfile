FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=target/leaf-server.jar
ADD ${JAR_FILE} /leaf-server.jar
ENTRYPOINT ["java","-jar","/leaf-server.jar"]
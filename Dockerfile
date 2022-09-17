FROM openjdk:11
EXPOSE 8080
ADD target/leaf-server.jar /leaf-server.jar

# wait-for-it from: https://github.com/vishnubob/wait-for-it
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
# CMD ["./wait-for-it.sh", "leaf-mysql:3306", "--", "./wait-for-it.sh", "leaf-redis:6379", "--", "java","-jar","/leaf-server.jar"]

CMD ["java","-jar","/leaf-server.jar"]
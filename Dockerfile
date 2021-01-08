FROM adoptopenjdk/openjdk11

COPY target/spring-kaka-test-0.0.1-SNAPSHOT.jar /spring-kafka-test.jar

CMD ["java", "-jar", "/spring-kafka-test.jar"]
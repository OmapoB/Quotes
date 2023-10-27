FROM openjdk
ADD target/Quotes-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "Quotes-0.0.1-SNAPSHOT.jar"]
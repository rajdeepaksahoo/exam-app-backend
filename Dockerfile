FROM openjdk:17
LABEL maintainer ="howtodoinjava"
ADD target/exam-0.0.1-SNAPSHOT.jar exam-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","exam-0.0.1-SNAPSHOT.jar"]
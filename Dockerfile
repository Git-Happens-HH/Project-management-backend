FROM eclipse-temurin:25-jdk AS builder
WORKDIR /opt/app
COPY ./project-management-app/mvn/ .mvn
COPY ./project-management-app/mvnw ./project-management-app/pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY ./project-management-app/src ./src
RUN ./mvnw clean install -DskipTests 
RUN find ./target -type f -name '.jar' -exec cp {} /opt/app/app.jar ; -quit
FROM eclipse-temurin:25-jre-alpine
COPY --from=builder /opt/app/.jar /opt/app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
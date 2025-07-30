######################################################################
# Create Image
######################################################################

# Base Image (JDK17 version)
FROM openjdk:17-jdk

# Set Default Work Directory
WORKDIR /user-admin-app

# Build file directory
ARG JAR_FILE=target/UserAdminApplication-0.0.1-SNAPSHOT.jar

# Copy Host directory file to Container directory
COPY ${JAR_FILE} app.jar

# Documentation
EXPOSE 80

ENV SERVER_PORT=80

######################################################################
# Related to Container
######################################################################

# Execute jar ( Related to Container )
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dserver.port=${SERVER_PORT}", "-jar", "/user-admin-app/app.jar"]
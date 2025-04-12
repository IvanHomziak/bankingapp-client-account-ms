# Base image
FROM eclipse-temurin:18-jdk

# Install required dependencies for build
RUN apt-get update && \
    apt-get install -y git curl maven && \
    apt-get clean

# Set environment variables
ENV DB_HOST=${DB_HOST}
ENV DB_NAME=${DB_NAME}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}

# Expose application port
EXPOSE 8083

# Set working directory
WORKDIR /app

# Clone and build bankingapp-common
RUN git clone https://github.com/IvanHomziak/bankingapp-common.git && \
    cd bankingapp-common && \
    mvn clean install -DskipTests

# Go back to /app directory
WORKDIR /app

# Copy project files (excluding `src` to use caching efficiently)
COPY pom.xml .

# Resolve dependencies (this speeds up builds by using caching)
RUN mvn dependency:resolve

# Copy the rest of the source code
COPY src src

# Build the project
RUN mvn clean package -DskipTests

# Run the application
ENTRYPOINT ["java", "-jar", "target/client-account-ms.jar"]
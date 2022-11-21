# Building the image
# 	docker build -t gauge-java .
# Running the image
# 	docker run  --rm -it -v ${PWD}/reports:/gauge/reports gauge-java


FROM maven:3.8.6-eclipse-temurin-11

# Install gauge
RUN apt-get update \
    && apt-get install -y unzip \
    && curl -Ssl https://downloads.gauge.org/stable | sh

# Set working directory
WORKDIR /gauge
 
# Copy the local source folder
# COPY . .

# Install gauge plugins
RUN gauge install \
    && gauge install screenshot

RUN gauge config runner_connection_timeout 9000000 \
    && gauge config runner_request_timeout  9000000

# RUN mvn compile

# ENTRYPOINT ["mvn", "test"]

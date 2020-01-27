# use a Maven image to build the artifacts
FROM maven:3.6-jdk-11-slim as maven

# stupid image has a volume at ~/.m2
ENV MAVEN_OPTS="-Dmaven.repo.local=/.m2/"

# copy the manifest files
COPY ./pom.xml ./pom.xml
COPY ./dependency-reduced-pom.xml ./dependency-reduced-pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy the source tree
COPY ./src ./src
COPY ./config.yml ./config.yml

# build using release configuration
RUN mvn install -B -DskipTests

# only need a JRE to run anything
FROM openjdk:11-jre-slim

# set deployment directory
WORKDIR /opt/appcelerator/petstore

# update system and dependencies
RUN apt-get update && apt-get upgrade -y

# add required files for config/app
COPY --from=maven config.yml ./
COPY --from=maven target/platform-*.jar ./

# download Tini to handle the custom entrypoints required for safe exit
ADD https://github.com/krallin/tini/releases/download/v0.18.0/tini ./tini
RUN chmod +x ./tini

# reduce privileges by not running container as root
RUN chgrp -R 0 /opt/appcelerator && \
    chmod -R g=u /opt/appcelerator && \
    chmod g+x /opt/appcelerator/petstore/platform-*.jar
USER 1001

# add tini as the entrypoint
ENTRYPOINT ["./tini", "--"]

# set startup commands to run server
CMD java -jar platform-*.jar server config.yml

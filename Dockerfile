# To begin with, we use a base Maven image to build the application.
FROM maven:3.6-jdk-11-slim as maven

# Here we copy over any manifest files for Maven, and attempt to
# resolve as many dependencies as possible early in the build.
# This has the potential to speed up future builds by already
# having cached dependencies in a Docker layer.
COPY ./pom.xml ./pom.xml
COPY ./dependency-reduced-pom.xml ./dependency-reduced-pom.xml

# This will attempt to load all dependencies in advance.
RUN mvn dependency:go-offline -B

# Here we copy over the source files we need, as well as any
# configuration files. This has to be after the dependency
# stuff, otherwise changing a file would cause the dependencies
# to have be to re-resolved on build.
COPY ./src ./src
COPY ./config.yml ./config.yml

# Finally we run a build to generate our output artifact.
RUN mvn install -B

# At this point we switch context to a smaller image with only
# a JRE. We've already built the app, so we don't need Maven or
# a JDK installation. This helps keep final images small.
FROM openjdk:11-jre-slim

# We select a working directory with an Axway namespace.
WORKDIR /opt/axway/petstore

# Although not strictly necessary for the demo, we update the
# system dependencies to ensure everything is latest. This
# should run early in the image build so it's cached in any
# future builds, because it takes a fairly long time.
RUN apt-get update && \
	apt-get upgrade -y && \
	rm -rf /var/lib/apt/lists/*

# As part of the Journey To The Cloud criteria, we include Tini to ensure
# that our application correctly listens to signals from Docker for things
# such as termination. It acts as a wrapper to our application.
ADD https://github.com/krallin/tini/releases/download/v0.18.0/tini ./tini
RUN chmod +x ./tini
ENTRYPOINT ["./tini", "--"]

# Now we can copy across the artifacts we need from our build
# image, directly into the working directory for the service.
COPY --from=maven config.yml ./
COPY --from=maven target/platform-*.jar ./

# Again for the JTTC critera, we set a non-root user for the image so
# that the application is not running as the root user when started.
RUN chgrp -R 0 /opt/axway && \
	chmod -R g=u /opt/axway && \
	chmod g+x /opt/axway/petstore/platform-*.jar
USER 1001

# Finally we configure the startup command to run our service!
CMD java -jar platform-*.jar server config.yml

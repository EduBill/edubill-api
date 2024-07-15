# Stage 1: Build a minimal JDK using jlink
FROM alpine:3.18 as build
RUN apk --no-cache add openjdk17

RUN /usr/lib/jvm/default-jvm/bin/jlink \
    --compress=2 \
    --module-path /usr/lib/jvm/default-jvm/jmods \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --output /jdk-minimal

# Stage 2: Create the final image
FROM alpine:3.18
COPY --from=build /jdk-minimal /opt/jdk/

ARG JAR_FILE=build/libs/edubillApi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["/opt/jdk/bin/java","-jar","/app.jar"]
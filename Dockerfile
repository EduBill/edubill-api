FROM alpine:3.18 AS build
RUN apk --no-cache add openjdk17

RUN /usr/lib/jvm/default-jvm/bin/jlink \
    --compress=2 \
    --module-path /usr/lib/jvm/default-jvm/jmods \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --output /jdk-minimal

# Stage 2: Create the final image
FROM alpine:3.18
COPY --from=build /jdk-minimal /opt/jdk/

ARG JAR_FILE=/build/libs/edubillApi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY .env /app/.env

RUN echo "#!/bin/sh" > /app/start.sh \
    && echo "export \$(cat /app/.env | xargs)" >> /app/start.sh \
    && echo "/opt/jdk/bin/java -Dspring.profiles.active=\${SPRING_PROFILE} -jar /app.jar" >> /app/start.sh \
    && chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]


FROM registry.xx.com/x/x/spring-webflux-example-image:latest as build
LABEL org.label-schema.docker.cmd="docker run -p 8081:8081 -d -v /app-env.yml:/application-env.yml registry.xx.com/x/x/spring-webflux-example:latest"
ENV JAVA_AGENT_DOWNLOAD_URL=https://xxx
WORKDIR /home/appuser
RUN apk --no-cache add curl tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata \
    && addgroup -S appuser \
    && addgroup -S appuser -G appusers \
    && chmod g+r,g+w,g+X -R /home/appuser/
USER appuser
COPY build/libs/spring-webflux-example-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Dfile.encoding=UTF-8","-jar","/home/appuser"]
CMD ["java","-jar","/home/appuser/app.jar"]
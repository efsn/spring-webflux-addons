# FROM openjdk:8-jdk-alpine as build
FROM registry.cn-shanghai.aliyuncs.com/ikea_common_image/openjdk:8-jdk as build

WORKDIR /home

add . /home
run sh ./gradlew clean spring-webflux-example:bootJar

FROM registry.cn-shanghai.aliyuncs.com/ikea_common_image/openjdk:8-jdk

LABEL org.label-schema.docker.cmd="docker run -p 8080:8080 -d registry.cn-shanghai.aliyuncs.com/pax-app/spring-webflux-example:latest"

WORKDIR /home/paxappuser

COPY --from=build /home/spring-webflux-example/build/libs/spring-webflux-example-*.jar app.jar

RUN apk --no-cache add curl tzdata bash&& cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata \
    && addgroup -S paxappusers \
    && adduser -S paxappuser -G paxappusers \
    && chmod g+r,g+w,g+X -R /home/paxappuser/

USER paxappuser

EXPOSE 8080

ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Dfile.encoding=UTF-8","-jar","/home/paxappuser/app.jar"]

CMD ["java","-jar","/home/paxappuser/app.jar"]

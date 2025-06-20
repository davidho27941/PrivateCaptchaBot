FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY private_captcha_bot/pom.xml private_captcha_bot/pom.xml
RUN mvn -f private_captcha_bot/pom.xml dependency:go-offline
COPY private_captcha_bot/src private_captcha_bot/src
RUN mvn -f private_captcha_bot/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app
COPY --from=build /build/private_captcha_bot/target/private_captcha_bot-*.jar ./app.jar
COPY --from=build /build/private_captcha_bot/target/lib ./lib
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]

FROM eclipse-temurin:21-jdk AS build
WORKDIR /build
COPY PublicPreviewServer.java .
RUN javac -encoding UTF-8 PublicPreviewServer.java

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/PublicPreviewServer.class ./
COPY public ./public
USER 10001
EXPOSE 8080
CMD ["java", "PublicPreviewServer"]

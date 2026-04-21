# Vấn đề: Mỗi lần thay đổi code dù nhỏ, Docker sẽ copy lại toàn bộ .jar (~50-100MB).
# Docker layer cache không hoạt động hiệu quả.
#
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY build/libs/app-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]


#dùng multi-stage build

# ── Stage 1: Build (nếu muốn build bên trong Docker) ────────
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build app, bỏ qua test (test chạy ở CI riêng)
RUN ./gradlew bootJar -x test --no-daemon

# ── Stage 2: Extract layers ──────────────────────────────────
FROM eclipse-temurin:21-jre AS layers
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ── Stage 3: Final runtime image ─────────────────────────────
FROM eclipse-temurin:21-jre
WORKDIR /app

# Tạo user riêng, không chạy bằng root
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

EXPOSE 8080

# JVM flags tối ưu cho container
#-XX:+UseContainerSupportJVM        => đọc RAM/CPU limit của container thay vì của host
#-XX:MaxRAMPercentage=75.0          => Heap tối đa 75% RAM container (tránh OOM kill)
#-Djava.security.egd=file:/dev/./urandom => Tăng tốc khởi động (tránh block chờ entropy)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]
# JDK 21 기반 이미지
FROM eclipse-temurin:21-jdk

# 작업 디렉토리 생성
WORKDIR /app

# 빌드된 JAR 복사 (Maven 기준 target 폴더)
COPY target/*.jar
# 공식 OpenJDK 런타임 이미지를 기반으로 실행
FROM openjdk:17-jdk-alpine

# 컨테이너 내 작업 디렉토리를 설정
WORKDIR /app

# 빌드 컨텍스트에서 애플리케이션 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 애플리케이션이 실행되는 포트를 노출
EXPOSE 8080

# 애플리케이션을 실행하는 명령어를 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

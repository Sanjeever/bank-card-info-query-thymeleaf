# 使用 Maven 官方镜像作为构建阶段基础镜像
FROM maven:3.9.2-amazoncorretto-17 AS build

# 设置工作目录
WORKDIR /app

# 将 Maven 配置文件和源代码复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 打包项目（跳过测试）
RUN mvn clean package -DskipTests

# 使用 OpenJDK 17 作为运行阶段基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制打包好的 jar 文件
COPY --from=build /app/target/bank-card-info-query-thymeleaf-1.0.0.jar app.jar

# 暴露 8080 端口
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]

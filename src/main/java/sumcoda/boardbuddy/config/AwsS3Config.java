package sumcoda.boardbuddy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
public class AwsS3Config {

    // S3에 등록된 버킷 이름
    @Value("${spring.cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.bucket.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                // EC2 인스턴스 프로파일(IAM Role) 자동 사용
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
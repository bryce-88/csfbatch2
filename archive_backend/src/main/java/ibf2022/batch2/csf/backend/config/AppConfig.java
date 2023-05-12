package ibf2022.batch2.csf.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {
    
    @Bean
    public WebMvcConfigurer configureCors() {
    return new EnableCors("/**", "*");
    }

    @Value("${DO_STORAGE_KEY}")
    private String accessKey; 
 
    @Value("${DO_STORAGE_SECRETKEY}")
    private String secretKey; 
 
    @Value("${DO_STORAGE_ENDPOINT}")
    private String endPoint; 
 
    @Value("${DO_STORAGE_ENDPOINT_REGION}")
    private String endPointRegion;
    
   
    @Bean
    public AmazonS3 createS3Client(){
         BasicAWSCredentials cred =
                 new BasicAWSCredentials(accessKey, secretKey);
         EndpointConfiguration ep = 
                 new EndpointConfiguration(endPoint, endPointRegion);
 
         return AmazonS3ClientBuilder.standard()
             .withEndpointConfiguration(ep)
             .withCredentials(new AWSStaticCredentialsProvider(cred))
             .build();
    }

    @Value("${MONGO_URL}")
    private String mongoUrl;

    @Bean
    public MongoTemplate createMongoTemplate(){
        //Create a MongoClient
        MongoClient client = MongoClients.create(mongoUrl);

        return new MongoTemplate(client, "photos");
    }


}

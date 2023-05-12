package ibf2022.batch2.csf.backend.repositories;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${DO_STORAGE_BUCKETNAME}")
    private String bucketName;

	private static final Logger log = LoggerFactory.getLogger(ImageRepository.class);

	//For S3 upload
	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public String upload(MultipartFile file, String name, String title, String comments) throws IOException {

		Map<String, String> userData = new HashMap<>();

        userData.put("name", name);
		userData.put("date", LocalDateTime.now().toString());
        userData.put("title", title);
        userData.put("comments", comments);
		userData.put("originalFilename", file.getOriginalFilename());
	

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

		log.info(">>>> imageRepo original file name: " + file.getOriginalFilename());
		StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");
		int count = 0;
		String filenameExt = "";
		while(tk.hasMoreTokens()){
			if(count == 1){
				filenameExt = tk.nextToken();
				break;
			}else{
				filenameExt = tk.nextToken();
				count++;
			}
		}

		// TODO: find a way to differentiate between jpeg, png and gif
		// if(filenameExt.equals("blob"))
		// 	filenameExt = filenameExt + ".png";

		PutObjectRequest putRequest = 
		new PutObjectRequest(
		bucketName, "%s.%s".formatted(title, filenameExt)
					, file.getInputStream(), metadata);
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);

        return "%s.%s.%s.%s.%s".formatted(userData.get("date"),
										userData.get("title"),
										userData.get("name"),
										userData.get("comments"),
										userData.get("originalFilename"));
	}
}

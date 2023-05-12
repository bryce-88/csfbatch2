package ibf2022.batch2.csf.backend.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.User;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import ibf2022.batch2.csf.backend.services.UnzipService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping
public class UploadController {

	@Autowired
	ImageRepository imgRepo;

	@Autowired
	ArchiveRepository arcRepo;

	@Autowired
	UnzipService unzipSvc;

	private static final Logger log = LoggerFactory.getLogger(UploadController.class);

	private static final String DIGITALOCEAN_SUFFIX = "https://csfbatch2.sgp1.digitaloceanspaces.com/";

	// TODO: Task 2, Task 3, Task 4
	@PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> upload(
		@RequestPart MultipartFile file,
		@RequestPart String name,
		@RequestPart String title,
		@RequestPart String comments
	) throws IOException {

		User user = new User();
		List<String> urlList = new LinkedList<>();

		//tested, able to unzip into individual files using Postman
		//path = "src/main/resources/static/"+title
		UnzipService.unzip(file, title);

		File dirPath = new File("src/main/resources/static/"+title);
		File[] images = dirPath.listFiles();

		// Create new array of MultipartFile objects
        MultipartFile[] multipartFiles = new MultipartFile[images.length];

        // Loop through each file and convert to MultipartFile
        for (int i = 0; i < images.length; i++) {
            FileInputStream input = new FileInputStream(images[i]);
            MultipartFile multipartFile = new MockMultipartFile(images[i].getName(), images[i].getName(),
                    "application/octet-stream", input);
            multipartFiles[i] = multipartFile;
        }


		//if there are files in unzipped folder, proceed
		if (images != null) {
			//use as bundleId, one common UUID for one zip file
			String key = UUID.randomUUID().toString()
            .substring(0, 8);
		
			//each file upload will return the following separated by '.'
			//date[0], title[2], name[3], comment[4], originalFilename[5]&[6] including file extension 
		  	for (MultipartFile i : multipartFiles) {
				try {
					String imageKey = imgRepo.upload(i, name, title, comments);
					log.info(">> file uploaded, imageKey: " + imageKey);
					//from console log:
					//2023-05-12T13:38:13.226.title.bryce.comments.6a017ee66ba427970d01b8d1500891970c.png
					//notice the extra '.' in the datetime, omit the milliseconds
					//hence, title will be index [2] below

					//TODO: able to upload but somehow not the individual files..
					if (null != imageKey) {
						String[] imageKeyInfo = imageKey.split(".");
						user.setBundleId(key);
						log.info(">>> imageKeyInfo Date: ", imageKeyInfo[0]);
						user.setDate(imageKeyInfo[0]);
						user.setTitle(imageKeyInfo[2]);
						user.setName(imageKeyInfo[3]);
						user.setComments(imageKeyInfo[4]);
						urlList.add(DIGITALOCEAN_SUFFIX+imageKeyInfo[5]+imageKeyInfo[6]);
						log.info(">>> Printing for loop: " + user.toString());
					}
				} catch (IOException e) {
					return ResponseEntity.status(500).body("error : failed to archive files");
				}
			}
			user.setUrls(urlList);
		}

		//Task 4
		ObjectId mongoId = arcRepo.recordBundle(user);
		log.info(">>> if updated on Mongo: " + mongoId);
		
		try{ 
			if (null != mongoId) {
				JsonObject returnMessage = Json.createObjectBuilder().add("bundleId", mongoId.toString()).build();
				return ResponseEntity.ok().body(returnMessage.toString());
				// return ResponseEntity.status(201).body("bundleId : " + user.getBundleId());
			} else {
				return ResponseEntity.status(500).body("error : failed to archive files");
			}
		} catch (Exception ex) {
			return ResponseEntity.status(500).body("error : failed to archive files");
		}
	}
	

	// TODO: Task 5
	@GetMapping(path="/bundle")
	public ResponseEntity<String> getBundleByBundleId(@RequestParam String bundleId) {

		Document result = arcRepo.getBundleByBundleId(bundleId);

		if (null != result) {
			return ResponseEntity.status(201).body(result.toString());
		} else {
			return ResponseEntity.status(500).body("Failed to retrieve documents");
		}
	}

	// TODO: Task 6


}

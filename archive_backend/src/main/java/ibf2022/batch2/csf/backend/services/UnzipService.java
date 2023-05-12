package ibf2022.batch2.csf.backend.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UnzipService {

    private static final Logger log = LoggerFactory.getLogger(UnzipService.class);

    public static void unzip(MultipartFile file, String title) throws IOException {

        log.info(">>>> unzip service called");
        
        File targetPath = new File("src/main/resources/static/"+title);
        ZipInputStream zipStream = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = null;
        
        while ((zipEntry = zipStream.getNextEntry()) != null) {
            
            if (zipEntry.isDirectory()) {
                continue;
            }
            
            File outputFile = new File(targetPath + "/" + zipEntry.getName());
            
            File parent = outputFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            
            FileOutputStream outStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = zipStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            outStream.close();
        }
        
        zipStream.close();
    }

}

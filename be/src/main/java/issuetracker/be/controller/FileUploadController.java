package issuetracker.be.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadController {

  private final AmazonS3 amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @PostMapping("/upload")
  public URL uploadFile(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("파일이 포함되지 않았습니다.");
    }
    try {
      String fileName = file.getOriginalFilename();
      String fileKey = "img/" + fileName;
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());
      amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);
      return amazonS3Client.getUrl(bucket, fileKey);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
    }
  }

}
package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.IAwsS3Controller;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAwsS3Service;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.AwsS3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/s3")
public class AwsS3Controller implements IAwsS3Controller {
    private final AwsS3ServiceImpl awsS3ServiceImpl ;

    @Autowired
    public AwsS3Controller(AwsS3ServiceImpl awsS3ServiceImpl){
        this.awsS3ServiceImpl = awsS3ServiceImpl;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value="file") MultipartFile file) {
        awsS3ServiceImpl.uploadFile(file);
        String response = "The "+file.getOriginalFilename()+" file was successfully uploaded to S3";
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> listFiles() {
        return new ResponseEntity<List<String>>(awsS3ServiceImpl.getObjectsFromS3(), HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestParam("key") String key) {
        InputStreamResource resource  = new InputStreamResource(awsS3ServiceImpl.downloadFile(key));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+key+"\"").body(resource);
    }

    @GetMapping(value = "/url")
    public ResponseEntity<?> getObjectUrl(String key) {
        String objectUrl = awsS3ServiceImpl.getImagenUrl(key);
        return ResponseEntity.ok(objectUrl);
    }
}

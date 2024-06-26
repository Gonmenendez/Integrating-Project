package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAwsS3Service;

import com.amazonaws.services.s3.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3ServiceImpl implements IAwsS3Service {
    private static final Logger logger = LogManager.getLogger(AwsS3ServiceImpl.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public void uploadFile(MultipartFile file) {
        File mainFile = new File(file.getOriginalFilename());
        try (FileOutputStream stream = new FileOutputStream(mainFile)) {
            stream.write(file.getBytes());
            String newFileName = System.currentTimeMillis() + "_" + mainFile.getName();
            logger.info("Uploading file with name... " + newFileName);
            PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, mainFile);
            s3Client.putObject(request);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getObjectsFromS3() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        List<String> list = objects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        return list;
    }

    @Override
    public InputStream downloadFile(String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        S3Object object = s3Client.getObject(request);
        return object.getObjectContent();
    }

    @Override
    public String getImagenUrl(String key) {
        /*GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
        URL signedUrl = s3Client.generatePresignedUrl(request);
        return signedUrl.toString();*/
        String url = s3Client.getUrl(bucketName, key).toString();
        return url;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, file.getInputStream(), null);
            s3Client.putObject(request);
            return getImagenUrl(newFileName);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Failed to upload image to AWS S3", e);
        }
    }
}

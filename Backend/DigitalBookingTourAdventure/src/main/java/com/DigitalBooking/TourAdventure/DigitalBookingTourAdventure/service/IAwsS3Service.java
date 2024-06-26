package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IAwsS3Service {
    void uploadFile(MultipartFile file);
    List<String> getObjectsFromS3();
    InputStream downloadFile(String key);
    String getImagenUrl(String key);
    String uploadImage(MultipartFile file);
}

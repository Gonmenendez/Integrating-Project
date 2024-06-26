package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IAwsS3Controller {
    ResponseEntity<String> uploadFile(@RequestPart(value="file") MultipartFile file);
    ResponseEntity<List<String>> listFiles();
    ResponseEntity<Resource> download(@RequestParam("key") String key);
    ResponseEntity<?> getObjectUrl(@RequestParam("key") String key);
}

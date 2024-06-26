package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TourHasImagesException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ITourImageService {
    List<TourImageDTO> getAllImage();
    Optional<TourImageDTO> getImageById(Long id) throws BadIdRequestException;
    List<TourImageDTO> getImagesByTourId(Long tourId) throws BadIdRequestException;
    List<TourImage> postImage(TourDTO tourDTO, MultipartFile imageMain, MultipartFile[] images) throws TourHasImagesException;
    void deleteImageById(Long id) throws BadIdRequestException;
}

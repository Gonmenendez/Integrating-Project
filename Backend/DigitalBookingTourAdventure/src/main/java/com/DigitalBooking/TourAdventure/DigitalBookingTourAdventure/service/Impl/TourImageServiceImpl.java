package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.TourImageRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.ITourImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TourImageServiceImpl implements ITourImageService {
    private final TourImageRepositoryJPA tourImageRepositoryJPA;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AwsS3ServiceImpl awsS3ServiceImpl;

    @Autowired
    public TourImageServiceImpl(TourImageRepositoryJPA tourImageRepositoryJPA, ModelMapper modelMapper) {
        this.tourImageRepositoryJPA = tourImageRepositoryJPA;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<TourImageDTO> getAllImage() {
        List<TourImageDTO> tourImageDTOS = new ArrayList<>();
        List<TourImage> tourImages = tourImageRepositoryJPA.findAll();

        for (TourImage tourImage : tourImages) {
            tourImage.getTour().getId();

            TourImageDTO tourImageDTO = modelMapper.map(tourImage, TourImageDTO.class);
            tourImageDTOS.add(tourImageDTO);
        }
        return tourImageDTOS;
    }

    @Transactional
    public Optional<TourImageDTO> getImageById(Long id) throws BadIdRequestException {
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        Optional<TourImageDTO> tourImageDTOOpt = null;
        Optional<TourImage> tourImage = tourImageRepositoryJPA.findById(id);
        if (tourImage.isPresent()){
            TourImageDTO tourImageDTO = modelMapper.map(tourImage.get(), TourImageDTO.class);
            tourImageDTOOpt = Optional.of(tourImageDTO);
        }else{
            tourImageDTOOpt = Optional.empty();
        }
        return tourImageDTOOpt;
    }

    @Transactional
    public List<TourImageDTO> getImagesByTourId(Long tourId) throws BadIdRequestException {
        if (tourId <= 0 || tourId == null) {
            throw new BadIdRequestException("Invalid tour ID: " + tourId);
        }

        List<TourImage> tourImages = tourImageRepositoryJPA.findByTourId(tourId);
        if (tourImages.isEmpty()) {
            throw new BadIdRequestException("Tour images not found for tour ID: " + tourId);
        }

        List<TourImageDTO> tourImageDTOs = new ArrayList<>();
        for (TourImage tourImage : tourImages) {
            TourImageDTO tourImageDTO = modelMapper.map(tourImage, TourImageDTO.class);
            tourImageDTOs.add(tourImageDTO);
        }

        return tourImageDTOs;
    }

    @Transactional
    public List<TourImage> postImage(TourDTO tourDTO, MultipartFile imageMain, MultipartFile[] images) throws TourHasImagesException {
        TourImageDTO tourImageDTO = new TourImageDTO();
        Tour tour = modelMapper.map(tourDTO, Tour.class);
        // Check if there are existing images for the tour
        List<TourImage> existingImages = tourImageRepositoryJPA.findByTourId(tour.getId());
        if (!existingImages.isEmpty()) {
            throw new TourHasImagesException("Tour already has images");
        }

        List<TourImage> tourImages = new ArrayList<>();

        //ImageMain
        String imageMainUrl = awsS3ServiceImpl.uploadImage(imageMain);

        TourImage tourImageMain = new TourImage();
        tourImageMain.setUrlImage(imageMainUrl);
        tourImageMain.setIsPrincipal(true);
        tourImageMain.setTour(tour);

        tourImages.add(tourImageMain);

        for (MultipartFile image : images) {
            TourImage tourImage = new TourImage();

            String imageUrl = awsS3ServiceImpl.uploadImage(image);

            tourImage.setUrlImage(imageUrl);
            tourImage.setIsPrincipal(false);
            tourImage.setTour(tour);

            tourImages.add(tourImage);
        }

        // Save the new images in the database
        tourImages = tourImageRepositoryJPA.saveAll(tourImages);
        return tourImages;
    }



    @Transactional
    public void deleteImageById(Long id) throws BadIdRequestException {
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        tourImageRepositoryJPA.deleteById(id);
    }
}
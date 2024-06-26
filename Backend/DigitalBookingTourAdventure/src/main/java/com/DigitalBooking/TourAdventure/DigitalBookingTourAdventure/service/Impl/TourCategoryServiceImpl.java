package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourCategory;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadNameRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleAlreadyExistException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.TourCategoryRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.ICategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class TourCategoryServiceImpl implements ICategoryService {
    private final TourCategoryRepositoryJPA tourCategoryRepositoryJPA;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AwsS3ServiceImpl awsS3ServiceImpl;

    @Autowired
    public TourCategoryServiceImpl(TourCategoryRepositoryJPA tourCategoryRepositoryJPA, ModelMapper modelMapper) {
        this.tourCategoryRepositoryJPA = tourCategoryRepositoryJPA;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<TourCategoryDTO> getAllCategory() {
        List<TourCategoryDTO> tourCategoryDTOS = new ArrayList<>();
        List<TourCategory> tourCategories = tourCategoryRepositoryJPA.findAll();

        for (TourCategory tourCategory : tourCategories) {
            tourCategory.getTours().size();

            TourCategoryDTO tourCategoryDTO = modelMapper.map(tourCategory, TourCategoryDTO.class);

            // Mapear los tours a TourDTO y agregarlos a TourCategoryDTO
            Set<TourDTO> tourDTOs = new HashSet<>();
            for (Tour tour : tourCategory.getTours()) {
                TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);
                tourDTO.setCategories(null);
                tourDTOs.add(tourDTO);
            }
            tourCategoryDTO.setTours(tourDTOs);

            tourCategoryDTOS.add(tourCategoryDTO);
        }
        return tourCategoryDTOS;
    }

    @Transactional
    public Optional<TourCategoryDTO> getCategoryById(Long id) throws BadIdRequestException {
        if (id <= 0 || id == null) {
            throw new BadIdRequestException("Invalid id: " + id);
        }

        Optional<TourCategoryDTO> tourCategoryDTOOpt = null;
        Optional<TourCategory> tourCategory = tourCategoryRepositoryJPA.findById(id);

        if (tourCategory.isPresent()) {
            TourCategoryDTO tourCategoryDTO = modelMapper.map(tourCategory.get(), TourCategoryDTO.class);

            // Obtener la lista de tours y establecer la propiedad "categories" como null
            Set<TourDTO> tourDTOs = new HashSet<>();
            for (Tour tour : tourCategory.get().getTours()) {
                TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);
                tourDTO.setCategories(null);
                tourDTOs.add(tourDTO);
            }
            tourCategoryDTO.setTours(tourDTOs);

            tourCategoryDTOOpt = Optional.of(tourCategoryDTO);
        } else {
            tourCategoryDTOOpt = Optional.empty();
        }

        return tourCategoryDTOOpt;
    }

    @Transactional
    public Optional<TourCategoryDTO> getCategoryByTitle(String title) throws BadNameRequestException, CategoryTitleNotFoundException {
        if (title.isEmpty()) {
            throw new BadNameRequestException("Invalid name: " + title);
        }

        Optional<TourCategory> tourCategory = tourCategoryRepositoryJPA.findByCategoryTitle(title);
        if (tourCategory.isPresent()) {
            TourCategoryDTO tourCategoryDTO = modelMapper.map(tourCategory.get(), TourCategoryDTO.class);
            return Optional.of(tourCategoryDTO);
        } else {
            throw new CategoryTitleNotFoundException("Category not found: " + title);
        }
    }

    @Transactional
    public TourCategoryDTO postCategory(TourCategoryDTO tourCategoryDTO) throws CategoryTitleNotFoundException {
        TourCategory tourCategory = modelMapper.map(tourCategoryDTO, TourCategory.class);
        if(tourCategoryRepositoryJPA.findByCategoryTitle(tourCategory.getTitle()).isPresent()){
            throw new CategoryTitleNotFoundException("Category name already exist: " + tourCategory.getTitle());
        }
        tourCategory = tourCategoryRepositoryJPA.save(tourCategory);
        tourCategoryDTO = modelMapper.map(tourCategory, TourCategoryDTO.class);
        return tourCategoryDTO;
    }

    @Transactional
    public TourCategoryDTO postCategoryWithImage(TourCategoryDTO tourCategoryDTO, MultipartFile imageFile) throws CategoryTitleNotFoundException, CategoryTitleAlreadyExistException {
        TourCategory tourCategory = modelMapper.map(tourCategoryDTO, TourCategory.class);
        if(tourCategoryRepositoryJPA.findByCategoryTitle(tourCategory.getTitle()).isPresent()){
            throw new CategoryTitleAlreadyExistException("Category name already exist: " + tourCategory.getTitle());
        }
        // Guardar la imagen en el bucket de AWS S3
        String imageUrl = awsS3ServiceImpl.uploadImage(imageFile);
        System.out.println(imageUrl);

        tourCategory = tourCategoryRepositoryJPA.save(tourCategory);

        // Guardar la URL en la entidad de Tour
        tourCategory.setUrlImage(imageUrl);
        tourCategoryDTO = modelMapper.map(tourCategory, TourCategoryDTO.class);
        return tourCategoryDTO;
    }

    @Transactional
    public void deleteCategoryById(Long id) throws BadIdRequestException {
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        tourCategoryRepositoryJPA.deleteById(id);
    }
}

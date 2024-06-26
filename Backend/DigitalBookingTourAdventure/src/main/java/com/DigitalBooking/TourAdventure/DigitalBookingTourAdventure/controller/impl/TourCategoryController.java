package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.ITourCategoryController;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleAlreadyExistException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TourNameAlreadyExistsException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.TourCategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Controller
@RequestMapping("tourCategory")
public class TourCategoryController implements ITourCategoryController<TourCategoryDTO> {

    private final TourCategoryServiceImpl tourCategoryService;
    @Autowired
    public TourCategoryController(TourCategoryServiceImpl tourCategoryService) {
        this.tourCategoryService = tourCategoryService;
    }
    @PostMapping
    public ResponseEntity<?> postTourCategory(TourCategoryDTO tourCategoryDTO) throws CategoryTitleNotFoundException {
       return ResponseEntity.ok(tourCategoryService.postCategory(tourCategoryDTO));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postTourCategoryWithImage(@RequestParam("data") String data, @RequestParam("imageFile") MultipartFile imageFile){
        // Convertir el JSON a un objeto TourCategoryDTO
        ObjectMapper objectMapper = new ObjectMapper();
        TourCategoryDTO tourCategoryDTO;
        try {
            tourCategoryDTO = objectMapper.readValue(data, TourCategoryDTO.class);
        } catch (JsonProcessingException e) {
            // Manejar el error de parseo del JSON
            return ResponseEntity.badRequest().body("Invalid JSON data");
        }
        // Llamar al servicio con la entidad TourCategory y la imagen
        try {
            return ResponseEntity.ok(tourCategoryService.postCategoryWithImage(tourCategoryDTO, imageFile));
        } catch (CategoryTitleAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Category Title Already Exist");
        }
        catch (CategoryTitleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Category Title Not Found");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTourCategory() {
        return ResponseEntity.ok(tourCategoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourCategoryById(Long id) throws BadIdRequestException {
        ResponseEntity resp = null;
        Optional<TourCategoryDTO> tourCategoryDTOOpt = tourCategoryService.getCategoryById(id);

        if (id != null && tourCategoryDTOOpt.isPresent()){
            resp = ResponseEntity.ok(tourCategoryDTOOpt.get());
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found  Id: " + id);
        }
        return resp;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTourCategoryById(Long id) throws BadIdRequestException {
        ResponseEntity resp = null;
        if(id != null && tourCategoryService.getCategoryById(id).isPresent()){
            tourCategoryService.deleteCategoryById(id);
            resp = ResponseEntity.ok("Removed tourCategory with Id: " + id);
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("The category was not deleted because it was not found with Id: " + id);
        }
        return resp;
    }
}

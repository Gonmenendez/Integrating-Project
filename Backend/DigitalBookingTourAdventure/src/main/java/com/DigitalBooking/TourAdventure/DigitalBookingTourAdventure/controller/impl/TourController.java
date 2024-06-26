package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.ITourController;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.TourServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("tour")
public class TourController implements ITourController<TourDTO> {

    private final TourServiceImpl tourService;

    @Autowired
    public TourController(TourServiceImpl tourservice){
        this.tourService = tourservice;
    }

    @GetMapping
    public ResponseEntity<?> getAllTour(){
        return ResponseEntity.ok(tourService.getAllTour());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<TourDTO>> getAllPageTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<TourDTO> tourPage = tourService.getAllPageTour(page, size);

        if (!tourPage.isEmpty()) {
            return ResponseEntity.ok(tourPage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable("id") Long id) throws BadIdRequestException {
        ResponseEntity resp = null;
        Optional<TourDTO> tourDTOOpt = tourService.getTourById(id);

        if (id != null && tourDTOOpt.isPresent()){
            resp = ResponseEntity.ok(tourDTOOpt.get());
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Id: " + id);
        }
        return resp;
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Page<TourDTO>> getTourByName(
            @PathVariable("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity resp = null;
        Page<TourDTO> tourPage = tourService.getToursByName(name, page, size);

        if (name != null && !tourPage.isEmpty()){
            resp = ResponseEntity.ok(tourPage);
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Name: " + name);
        }
        return resp;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<TourDTO>> getTourByCategory(
            @PathVariable("category") String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws CategoryTitleNotFoundException {
        Page<TourDTO> tourPage = tourService.getToursByCategory(category, page, size);

        if (!tourPage.isEmpty()) {
            return ResponseEntity.ok(tourPage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/random-tours/{numberOfTours}")
    public ResponseEntity<?> getRandomTours(@PathVariable("numberOfTours") int numberOfTours) {
        List<TourDTO> randomTours = tourService.getRandomTours(numberOfTours);

        if (!randomTours.isEmpty()) {
            return ResponseEntity.ok(randomTours);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<?> getToursByAvailabilityBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate){
        List<TourDTO> toursAvailability = tourService.getToursByAvailability(startDate, endDate);
        ResponseEntity resp = null;
        if(!toursAvailability.isEmpty()){
            resp = ResponseEntity.ok(toursAvailability);
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tour availability was found between: " + startDate + "and "+ endDate);
        }
        return resp;
    }

    @PostMapping
    public ResponseEntity<?> postTour(@RequestBody TourDTO tourDTO) throws TourNameAlreadyExistsException, BadNameRequestException, BadIdRequestException, CategoryTitleNotFoundException, ParseException {
        return ResponseEntity.ok(tourService.postTour(tourDTO));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postTourWithImage(@RequestParam("dataTour") String dataTour, @RequestParam("imageMain") MultipartFile imageMain,@RequestParam("imageFiles") MultipartFile[] imageFiles){
        // Convertir el JSON a un objeto TourDTO
        ObjectMapper objectMapper = new ObjectMapper();
        TourDTO tourDTO;
        TourImageDTO tourImageDTO;
        try {
            tourDTO = objectMapper.readValue(dataTour, TourDTO.class);
        } catch (JsonProcessingException e) {
            // Manejar el error de parseo del JSON
            return ResponseEntity.badRequest().body("Invalid JSON data");
        }
        // Llamar al servicio con la entidad Tour y la imagen
        try {
            return ResponseEntity.ok(tourService.postTourWithImagen(tourDTO, imageMain, imageFiles));
        }
        catch (TourNameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Tour Name Already Exists");
        }
        catch (CategoryTitleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Category Title Not Found");
        } catch (BadIdRequestException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Bad Id Request");
        } catch (TourHasImagesException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the service: Tour Has Images");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTourById(@PathVariable("id") Long id) throws BadIdRequestException{
        ResponseEntity resp = null;
        if(id != null && tourService.getTourById(id).isPresent()){
            tourService.deleteTourById(id);
            resp = ResponseEntity.ok("Removed tour with Id: " + id);
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("The tour was not deleted because it was not found with Id: " + id);
        }
        return resp;
    }
}

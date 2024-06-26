package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.key.LocalDateKeyDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TourDTO {
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private Double price;
    private Double score;
//    @JsonSerialize(using = LocalTimeSerializer.class)
//    @JsonDeserialize(using = LocalTimeDeserializer.class)
//    private LocalTime duration;

    private String duration;

//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    private LocalDate dates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startTime;

    private Set<TourCategoryDTO> categories;
    private Set<LocationDTO> locations;
    private Set<TourImageDTO> images;
    @Column(nullable = false)
    private Integer quantity;

    @JsonDeserialize(keyUsing = LocalDateKeyDeserializer.class)
    private Map<LocalDate, Integer> availability = new HashMap<>();

    private List<BookingLineDTO> bookingLinesDTO;

    public TourDTO() {
        this.locations = new HashSet<>();
        this.images = new HashSet<>();
    }

    public TourDTO(Set<LocationDTO> locations, Set<TourImageDTO> images, Map<LocalDate, Integer> availability) {
        this.locations = locations;
        this.images = images;
        this.availability = availability;
    }

    public void addLocation(LocationDTO location) {
        locations.add(location);
        location.getTours().add(this);
    }

    public void addImage(TourImageDTO image) {
        images.add(image);
        image.setTour(this);
    }
    public void removeImage(TourImageDTO image) {
        images.remove(image);
        image.setTour(null);
    }

    public void addBookingLine(BookingLineDTO bookingLineDTO) {
        bookingLinesDTO.add(bookingLineDTO);
        bookingLineDTO.setTourDTO(this);
    }

    public void removeBookingLine(BookingLineDTO bookingLineDTO) {
        bookingLinesDTO.remove(bookingLineDTO);
        bookingLineDTO.setTourDTO(null);
    }


    public TourDTO(String name, String description, String shortDescription, Double price, Double score, String duration, LocalTime startTime, Set<TourCategoryDTO> categories,  Set<TourImageDTO> images) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.score = score;
        this.duration = duration;
        this.startTime = startTime;
        this.categories = categories;
        this.images = images;
    }

    public TourDTO(String name, String description, String shortDescription, Double price, Double score, String duration, LocalTime startTime, Set<TourCategoryDTO> categories, Set<LocationDTO> locations, Set<TourImageDTO> images, Integer quantity, Map<LocalDate, Integer> availability, List<BookingLineDTO> bookingLinesDTO) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.score = score;
        this.duration = duration;
        this.startTime = startTime;
        this.categories = categories;
        this.locations = locations;
        this.images = images;
        this.quantity = quantity;
        this.availability = availability;
        this.bookingLinesDTO = bookingLinesDTO;
    }

    public Set<TourCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<TourCategoryDTO> categories) {
        this.categories = categories;
    }

    public Set<TourImageDTO> getImages() {
        return images;
    }

    public void setImages(Set<TourImageDTO> images) {
        this.images = images;
    }

    public Map<LocalDate, Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<LocalDate, Integer> availability) {
        this.availability = availability;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public List<BookingLineDTO> getBookingLinesDTO() {
        return bookingLinesDTO;
    }

    public void setBookingLinesDTO(List<BookingLineDTO> bookingLinesDTO) {
        this.bookingLinesDTO = bookingLinesDTO;
    }
}

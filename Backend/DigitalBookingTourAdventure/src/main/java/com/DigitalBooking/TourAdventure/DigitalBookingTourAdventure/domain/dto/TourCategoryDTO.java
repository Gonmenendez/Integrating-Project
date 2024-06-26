package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TourCategoryDTO {

    private Long id;
    private String title;
    private String description;
    private String urlImage;
    private Set<TourDTO> tours;

    public void addTour(TourDTO tour) {
        this.tours.add(tour);
        tour.setCategories(new HashSet<>(tour.getCategories()));
    }

    public TourCategoryDTO() {
        this.tours = new HashSet<>();
    }

    public Set<TourDTO> getTours() {
        return tours;
    }

    public void setTours(Set<TourDTO> tours) {
        this.tours = tours;
    }

    public TourCategoryDTO(String title, String description, String urlImage) {
        this.title = title;
        this.description = description;
        this.urlImage = urlImage;
    }

}

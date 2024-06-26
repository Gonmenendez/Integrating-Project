package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TourImageDTO implements Serializable {
    private Long id;
    private String urlImage;
    private Boolean isPrincipal;
    @JsonIgnore
    private TourDTO tour;

    public TourImageDTO() {
        this.tour = new TourDTO();
    }

    public TourImageDTO(String urlImage, Boolean isPrincipal, TourDTO tourDTO) {
        this.urlImage = urlImage;
        this.isPrincipal = isPrincipal;
        this.tour = tourDTO;
    }
}


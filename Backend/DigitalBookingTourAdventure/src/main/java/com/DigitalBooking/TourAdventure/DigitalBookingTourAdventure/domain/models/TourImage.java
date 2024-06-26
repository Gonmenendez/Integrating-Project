package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tour_image")
public class TourImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String urlImage;

    private Boolean isPrincipal;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    public TourImage(String urlImage, Boolean isPrincipal, Tour tour) {
        this.urlImage = urlImage;
        this.isPrincipal = isPrincipal;
        this.tour = tour;
    }
}

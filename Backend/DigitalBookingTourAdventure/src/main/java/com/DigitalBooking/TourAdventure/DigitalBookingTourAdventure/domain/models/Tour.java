package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String description;
    private String shortDescription;
    private Double price;
    private Double score;

//    @JsonSerialize(using = LocalTimeSerializer.class)
//    @JsonDeserialize(using = LocalTimeDeserializer.class)
//    private LocalTime duration;

    private String duration;

    // Ya teniamos un duration como LocalTime, pero como tengo entendido que la duración es algo
    // más informativo que cualquier otra cosa (ya que la duración no va a afectar la disponibilidad
    // de las fechas y me parece que tampoco vamos a utilizar por otro lado, entonces la puse como
    // String para que pueda guardar algo como "2 dias" o "4 horas".
    // También podemos cambiar de String a Duration. Seria algo asi:
    // private Duration duration;
    // Y para crear la variable seria:
    // Duration duration = Duration.ofDays(3); // duración de 3 dias
    // Duration duration = Duration.ofHours(4); // duración de 4 horas
    // Duration duration = Duration.ofMinutes(90); // duración de 90 minutos

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    private LocalDate dates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startTime;

    // Creo que deberiamos usar String xq esto ocaciona errores.

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "tour_category",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_category_id")
    )
    private Set<TourCategory> categories;

    @ManyToMany
    @JoinTable(
            name = "tour_location",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private Set<TourImage> images;

    private Integer quantity;

//    @ManyToMany(mappedBy = "tours")
//    private List<Booking> bookings;

    //    public void addBooking(Booking booking) {
//        this.bookings.add(booking);
//        booking.getTours().add(this);
//    }

    @ElementCollection
    @CollectionTable(
            name = "tour_availability",
            joinColumns = @JoinColumn(name = "tour_id")
    )
    @MapKeyColumn(name = "tour_date")
    @Column(name = "available_quantity")
    private Map<LocalDate, Integer> availability = new HashMap<>();
    @JsonBackReference
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private Set<BookingLine> bookingLines;

    public void addBookingLine(BookingLine bookingLine) {
        bookingLines.add(bookingLine);
        bookingLine.setTour(this);
    }

    public void removeBookingLine(BookingLine bookingLine) {
        bookingLines.remove(bookingLine);
        bookingLine.setTour(null);
    }

    // Este codigo arriba crea una tabla llamada tour_availability que contiene las columnas tour_id,
    // tour_date y available_quantity. El tour_date va a servir como key.
    // Tanto para crear cuanto para actualizar se usa de la seguinte manera:
    //     LocalDate dateToAdd = LocalDate.of(2023, 7, 1);
    //     Integer reservationCount = 5;
    //     tour.getAvailability().put(dateToAdd, reservationCount);
    // Para hacer un get seria:
    //     LocalDate date = LocalDate.of(2023, 6, 30);
    //     int reservationCount = availability.get(date);
    // Y para remover seria:
    //     LocalDate date = LocalDate.of(2023, 6, 30);
    //     availability.remove(date);


    public void addLocation(Location location) {
        this.locations.add(location);
        location.getTours().add(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.getTours().remove(this);
    }

    public Tour(String name, String description, String shortDescription, Double price, Double score, String duration, LocalTime startTime, Set<TourCategory> categories, Set<Location> locations, Set<TourImage> images, Integer quantity, Map<LocalDate, Integer> availability, Set<BookingLine> bookingLines) {
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
        this.bookingLines = bookingLines;
    }

    public Tour(String name, String description, String shortDescription, Double price, Double score, String duration, LocalTime startTime, Set<TourCategory> categories, Set<TourImage> images, Integer quantity) {
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.score = score;
        this.duration = duration;
        this.startTime = startTime;
        this.categories = categories;
        this.images = images;
        this.quantity = quantity;
    }

    public Tour() {
    }

    public Set<BookingLine> getBookingLines() {
        return bookingLines;
    }

    public void setBookingLines(Set<BookingLine> bookingLines) {
        this.bookingLines = bookingLines;
    }
}

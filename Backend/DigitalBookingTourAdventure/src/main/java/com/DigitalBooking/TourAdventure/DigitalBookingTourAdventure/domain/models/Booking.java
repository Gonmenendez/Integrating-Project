package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany
//    @JoinTable(
//            name = "booking_tour",
//            joinColumns = @JoinColumn(name = "booking_id"),
//            inverseJoinColumns = @JoinColumn(name = "tour_id")
//    )
//    private List<Tour> tours;

    private BigDecimal totalPrice;

    @Column(nullable = false)
    private Boolean deleted = false;
    @Column(nullable = false)
    private Boolean status = true;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingLine> bookingLines;

    public Booking(User user, List<BookingLine> bookingLines) {
        this.user = user;
        this.bookingLines = bookingLines;
    }
    public Booking() {
    }

public void addBookingLine(BookingLine bookingLine) {
        bookingLines.add(bookingLine);
        bookingLine.setBooking(this);
    }
}

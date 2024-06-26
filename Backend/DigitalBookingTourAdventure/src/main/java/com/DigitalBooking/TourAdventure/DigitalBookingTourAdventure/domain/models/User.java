package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;

    private Boolean isAccountVerified;

    private Boolean isAccountExpired;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VerificationToken verificationToken;

    @ElementCollection
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private List<GrantedAuthority> authorities;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public User(String name, String lastName, String email, String password, UserRoles userRole, Boolean isAccountVerified, Boolean isAccountExpired) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.isAccountVerified = isAccountVerified;
        this.isAccountExpired = isAccountExpired;
        this.loadAuthorities();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());
//        return Collections.singletonList(grantedAuthority);

        return authorities;
    }

    public void loadAuthorities(){

        this.authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.name()));

        if (userRole == UserRoles.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("GET_ALL_USERS"));
            authorities.add(new SimpleGrantedAuthority("POST_TOUR"));
        } else {
//            authorities.add(new SimpleGrantedAuthority("PAYMENT_CREATE"));
//            authorities.add(new SimpleGrantedAuthority("PROFILE_VIEW"));
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        // Add logic to define if this account is locked or not
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        // add logic to define if this account's credentials are expired or not
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountVerified;
    }

}

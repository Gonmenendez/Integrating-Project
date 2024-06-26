//package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.config;
//
//import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
//import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.UserRoles;
//import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.UserRepository;
//
//import lombok.AllArgsConstructor;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@AllArgsConstructor
//public class DataLoader implements ApplicationRunner {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        userRepository.save(User.builder()
//                .name("Admin")
//                .lastName("DefaultAdmin")
//                .email("admin@gmail.com")
//                .password(passwordEncoder.encode("adMin01$"))
//                .userRole(UserRoles.ADMIN)
//                .isAccountVerified(true)
//                .isAccountExpired(false)
//                .build());
////        userRepository.save(new User("User", "DefaultUser", "user@gmail.com", "uSer02#", UserRoles.USER, true));
//    }
//}

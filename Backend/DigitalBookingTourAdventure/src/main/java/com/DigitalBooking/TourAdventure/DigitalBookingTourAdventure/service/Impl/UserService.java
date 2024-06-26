package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters.UserDTOToUserConverter;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters.UserToUserDTOConverter;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.UserRoles;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.UserRepository;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserToUserDTOConverter converter;


    @Override
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .filter(Objects::nonNull)
                .map(user -> {
                    UserDTO userDTO = converter.convert(user);

                    return userDTO;
                })
                .collect(Collectors.toList());

        return userDTOs;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("Account not confirmed!");
        }

        return user;
    }

    @Override
    public UserDetails signUp(SignUpRequest signUpRequest) {

        try {

            User user = findUserByEmail(signUpRequest.getEmail());

            if (user != null && !user.isAccountNonExpired()) {
                user.setIsAccountExpired(false);
                return userRepository.save(user);
            } else {
                return userRepository.save(User.builder()
                        .name(signUpRequest.getName())
                        .lastName(signUpRequest.getLastName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .userRole(UserRoles.USER)
                        .isAccountVerified(false)
                        .isAccountExpired(false)
                        .build());
            }
        } catch (DataIntegrityViolationException e) {
            throw new ErrorResponseException(HttpStatus.CONFLICT,
                    ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                            "User already exist"), e);
        }
    }

    @Override
    public void registrationConfirm(UserDetails userDetails){

        User user = (User) userDetails;
        user.setIsAccountVerified(true);
        userRepository.save(user);

    }

    private User findUserByEmail(String email){

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        return null;
    }

    @Override
    public void setAccountExpired(UserDetails userDetails){

        User user = findUserByEmail(userDetails.getUsername());

        if(user != null){
            user.setIsAccountExpired(true);
            userRepository.save(user);
        }

    }

    @Override
    public UserDetails getUserByEmail(String email){
        return this.findUserByEmail(email);

    }

    @Override
    public UserDTO getUserById(Long id) {

        return converter.convert((User)findUserById(id));
    }

    private UserDetails findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User id not found"));
    }
    @Override
    public void deleteUserById(Long id){
        UserDetails userDetails = this.findUserById(id);
        this.setAccountExpired(userDetails);

    }

    @Override
    public void addAuthorityToUser(Long id, String authority) {
        UserDetails userDetails = this.findUserById(id);

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(userDetails.getAuthorities());

        if (!userHasAuthority(userDetails, authority)) {
            updatedAuthorities.add(new SimpleGrantedAuthority(authority));

            User user = (User) userDetails;
            user.setAuthorities(updatedAuthorities);

            userRepository.save(user);
        }
    }

    private boolean userHasAuthority(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    @Override
    public void removeAuthorityFromUser(Long id, String authority) {
        UserDetails userDetails = findUserById(id);

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(userDetails.getAuthorities());

        if (userHasAuthority(userDetails, authority)) {
            updatedAuthorities.removeIf(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));

            User user = (User) userDetails;
            user.setAuthorities(updatedAuthorities);

            userRepository.save(user);
        }
    }

}

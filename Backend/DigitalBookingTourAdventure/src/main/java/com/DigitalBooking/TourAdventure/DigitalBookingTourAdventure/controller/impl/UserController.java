package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return ResponseEntity.ok("Removed user with Id: " + id);
    }

    @PostMapping("/{id}/authorities")
    public ResponseEntity<String> addAuthorityToUser(@PathVariable Long id, @RequestParam String authority) {
        userService.addAuthorityToUser(id, authority);
        return ResponseEntity.ok("Authority granted.");
    }

    @DeleteMapping("/{id}/authorities/{authority}")
    public ResponseEntity<String> removeAuthorityFromUser(@PathVariable Long id, @PathVariable String authority) {
        userService.removeAuthorityFromUser(id, authority);
        return ResponseEntity.ok("Authority removed successfully");
    }
}

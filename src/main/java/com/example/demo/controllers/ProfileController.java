package com.example.demo.controllers;

import com.example.demo.models.Profile;
import com.example.demo.payload.request.newProfileRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ProfileRepository;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    
    @Autowired
    ProfileRepository profileRepository;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        //return profileRepository.getRole(email);
        return ResponseEntity.ok(new MessageResponse("Hello Ahmed"));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestParam String email){
        //return profileRepository.getRole(email);
        char c = email.trim().charAt(0);
        boolean isDigit = (c >= '0' && c <= '9');
        if (isDigit){
            return ResponseEntity.ok(profileRepository.getRole("+" + email.trim()));
        }
        return ResponseEntity.ok(profileRepository.getRole(email));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getFullName")
    public ResponseEntity<?> getFullName(@RequestParam String email){
        //return profileRepository.getRole(email);
        char c = email.trim().charAt(0);
        boolean isDigit = (c >= '0' && c <= '9');
        if (isDigit){
            return ResponseEntity.ok(profileRepository.getFullName("+" + email.trim()));
        }
        return ResponseEntity.ok(profileRepository.getFullName(email));
    }

    @PostMapping("/setProfile")
    public ResponseEntity<?> setRole(@RequestBody newProfileRequest newProfile){
        Profile profile = new Profile();
        profile.setUsername(newProfile.getEmail());
        profile.setRole(newProfile.getRole());
        profile.setFullName(newProfile.getFullName());
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return ResponseEntity.ok(new MessageResponse("role was created successfully!"));
    }
}

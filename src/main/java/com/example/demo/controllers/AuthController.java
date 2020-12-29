package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestParam String token) throws FirebaseAuthException {
		/*
		 * if (userRepository.existsByUsername(signUpRequest.getUsername())) { return
		 * ResponseEntity .badRequest() .body(new
		 * MessageResponse("Error: Username is already taken!")); }
		 * 
		 * if (userRepository.existsByEmail(signUpRequest.getEmail())) { return
		 * ResponseEntity .badRequest() .body(new
		 * MessageResponse("Error: Email is already in use!")); }
		 */
		
		// Create new user's account
		FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
		
		User user = new User(decodedToken.getEmail(), 
							 decodedToken.getEmail(),
							 "123456");


		Set<Role> roles = new HashSet<>();
		
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);
	
	/*
		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(adminRole);
	*/
	/*
		Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(modRole);
	*/
		Role teacherRole = roleRepository.findByName(ERole.ROLE_Teacher)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(teacherRole);
	/*
		Role studentRole = roleRepository.findByName(ERole.ROLE_Student)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(studentRole);
			*/
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}


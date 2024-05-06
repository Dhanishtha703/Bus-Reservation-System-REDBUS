package com.mohit.brs.controller;


import com.mohit.brs.config.JwtHelper;
import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.*;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TripScheduleRepository;
import com.mohit.brs.service.StopService;
import com.mohit.brs.service.TripScheduleService;
import com.mohit.brs.service.TripService;
import com.mohit.brs.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StopService stopService;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    TripService tripService;

    @Autowired
    TripScheduleRepository tripScheduleRepository;

    @Autowired
    TripScheduleService tripScheduleService;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (userService.isUserExists(userRegistrationDto.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(userRegistrationDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @GetMapping("/login")
    public ResponseEntity<?> generateJwtToken(@RequestBody UserLoginDto userLoginDto) throws Exception {

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

            // Set authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token and return as response
            String jwtToken = jwtHelper.generateTokenCustom(authentication);
            return ResponseEntity.ok().body("JWT Token: " + jwtToken);
        } catch (Exception e) {
            // If authentication fails, return an error response
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/getAllStops")
    public ResponseEntity<?> getAllStops( Principal principal){

        if(!isUser(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }
        List<Stop> stopList =  stopService.getAllStops();

        return new ResponseEntity<>(stopList,HttpStatus.OK);


    }

    @GetMapping("/searchTrip")
    public ResponseEntity<?> searchTripBetweenTwoStops(Principal principal,
                                                       @RequestParam("sourceStopName") String sourceStopName,
                                                       @RequestParam("destinationStopName") String destinationStopName) {

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

        Stop source = stopRepository.findByName(sourceStopName);
        Stop destination = stopRepository.findByName(destinationStopName);

        if (source != null && destination != null) {
            // Both source and destination are found, proceed with retrieving trips
            Long sourceId = source.getStopId();
            Long destinationId = destination.getStopId();
            List<Trip> tripList = tripService.getTripBetweenTwoStops(sourceId, destinationId);

            ModelMapper modelMapper = new ModelMapper();
            List<TripDto> newTripList = new ArrayList<>();
            for (Trip trip : tripList) {
                TripDto tripDto = modelMapper.map(trip, TripDto.class);
                newTripList.add(tripDto);
            }

            return new ResponseEntity<>(newTripList, HttpStatus.OK);
        } else {
            // If either source or destination is null, return a meaningful error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source or destination stop not found");
        }
    }

    @GetMapping("/searchTripSchedule")
    public ResponseEntity<?> filterTripScheduleBasedOnDate(Principal principal, @RequestParam String tripDate){

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

       List<?> tripScheduleList =  tripScheduleService.getTripScheduleListByDate(tripDate);


        return new ResponseEntity<>(tripScheduleList , HttpStatus.OK);
    }

//    @PostMapping("/bookTicket")
//    public ResponseEntity<?> bookTicketForTrip(Principal principal, @PathVariable Long tripScheduleId, @RequestBody TicketDto ticketDto){
//
//        if (!isUser(principal)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
//        }
//        TripSchedule tripSchedule = tripScheduleRepository.findById(tripScheduleId)
//                .orElseThrow(() -> new IllegalArgumentException("No Schedule for this Trip found"));
//
////        if(tripSchedule != null){
////            tripSchedule.setAvailableSeats(tripSchedule.getAvailableSeats() - ticketDto.);
////        }
//    }

    private boolean isUser(Principal principal) {

        if (principal == null) {
            return false;
        }

        String username = principal.getName();
        User user = userService.findByEmail(username);
        Role role = user.getRole();

        // Check if user has ADMIN role
        if(role != null && role.getRole().toString().equalsIgnoreCase("ROLE_USER")){
            return true;
        }
        return false;

    }

}
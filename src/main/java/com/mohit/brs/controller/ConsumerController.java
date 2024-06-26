package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.TicketDto;
import com.mohit.brs.model.request.TripDto;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TicketRepository;
import com.mohit.brs.repository.TripScheduleRepository;
import com.mohit.brs.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class ConsumerController {

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
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/getAllStops")
    public ResponseEntity<?> getAllStops(Principal principal){

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

    @PostMapping("/bookTicket")
    public ResponseEntity<?> bookTicketForTrip(Principal principal, @RequestBody TicketDto ticketDto){

        if (!isUser(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only USER can access this endpoint");
        }

        try {
            // Retrieve passenger, trip schedule, and other details from the request
            User passenger = userService.findByEmail(ticketDto.getPassengerEmailId());

            if(passenger == null){
                return ResponseEntity.badRequest().body("User is Not Present !!!!");
            }


            TripSchedule tripSchedule = tripScheduleRepository.findById(ticketDto.getTripScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("No Schedule for this Trip found !!"));

            if (!ticketDto.getJourneyDate().equals(tripSchedule.getTripDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select correct trip date for this trip schedule");

            }

            // Check if there are available seats
            int availableSeats = tripSchedule.getAvailableSeats();
            if (availableSeats <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available seats for this trip schedule");
            }

            // Check if the user has already booked a ticket for this trip schedule
//            boolean alreadyBooked = tripSchedule.getTicketsSold().stream()
//                    .anyMatch(ticket -> ticket.getPassenger().equals(passenger));

            Ticket alreadyBooked = ticketRepository.findBySeatNumber(ticketDto.getSeatNumber());

            if (alreadyBooked != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat is already Booked");
            }

            // Book the ticket
            Ticket ticket = ticketService.bookTicket(ticketDto, passenger);

            // Add the ticket to the trip schedule's soldedTickets set
            tripSchedule.getTicketsSold().add(ticket);

            // Update the available seats
            tripSchedule.setAvailableSeats(availableSeats - 1);

            //Save the changes to trip schedule
            tripScheduleRepository.save(tripSchedule);

            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while booking the ticket");
        }
    }

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

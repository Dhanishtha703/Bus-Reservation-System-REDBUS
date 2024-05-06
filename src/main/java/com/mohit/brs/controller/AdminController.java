package com.mohit.brs.controller;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.*;
import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.*;
import com.mohit.brs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    AgencyService agencyService;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    BusService busService;

    @Autowired
    TripService tripService;

    @Autowired
    StopService stopService;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripScheduleRepository tripScheduleRepository;

    @Autowired
    TripScheduleService tripScheduleService;

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDTO profileDTO, Principal principal) {
        String username = principal.getName();

        // Check if user is ADMIN
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        profileService.updateProfile(profileDTO, username);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/add-agency")
    public ResponseEntity<String> addAgency(@RequestBody AgencyDTO agencyDTO, Principal principal){
        String username = principal.getName();
        User user = userService.findByEmail(username);

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        agencyService.addAgency(agencyDTO,user);

        return ResponseEntity.ok("Agency Added successfully");

    }

    @PostMapping("/add-buses")
    public ResponseEntity<String> addBusesToAgency(@RequestBody BusDto busDto, Principal principal , @RequestParam Long agencyId){

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }
        if(agencyService.getAgencyById(agencyId) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("agency id does not exists");
        }
        Agency agency = agencyService.getAgencyById(agencyId);

        busService.addBus(busDto ,agency);

        return ResponseEntity.ok("Agency Added successfully");

    }

    @PostMapping("/add-stop")
    public ResponseEntity<String> addStop(@RequestBody StopDto stopDto, Principal principal) {

        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }
        try {
            stopService.addStop(stopDto);
            return ResponseEntity.ok("Stop Added successfully");
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding stop: " + e.getMessage());
        }
    }

    @PostMapping("/add-trip")
    public ResponseEntity<String> addTripsToAgency(@RequestBody TripDto tripDto, Principal principal ){

        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

        String agencyCode = tripDto.getAgencyCode();
        if(!agencyExistsByCode(agencyCode)){
            return ResponseEntity.badRequest().body("Agency is Not Present !!!!");
        }

        String busCode = tripDto.getBusCode();
        if(!busExistsByCode(busCode)){
            return ResponseEntity.badRequest().body("Bus is Not Present !!!!");
        }

        String sourceStopName = tripDto.getSourceStopName();
        if(sourceExists(sourceStopName) == null){
            return ResponseEntity.badRequest().body("Source Not Present !!!");
        }

        String destinationStopName = tripDto.getDestinationStopName();
        if(sourceExists(destinationStopName) == null){
            return ResponseEntity.badRequest().body("Destination Not Present !!!");
        }

       tripService.addTrip(tripDto);

        return ResponseEntity.ok("Trip Added successfully");

    }

    @PostMapping("/add-tripSchedule")
    public ResponseEntity<?> addTripSchedule(@RequestBody TripScheduleDto tripScheduleDto, Principal principal, @RequestParam Long tripId){
        if(!isAdmin(principal)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only ADMIN can access this endpoint");
        }

//        Long tripId = tripScheduleDto.getTripDetail().getTripId();
//        if(!isTripIdExists(tripId)){
//            return ResponseEntity.badRequest().body("Trip Id is not Present !!");
//        }
        // Retrieve the Trip object from the tripId
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        tripScheduleService.addTripScheduleById(tripScheduleDto , trip);
        return ResponseEntity.ok("Trip Schedule added successfully");
    }

    private boolean isTripIdExists(Long tripId) {
        Optional<Trip> trip =  tripRepository.findById(tripId);
        System.out.println(trip.toString());

        if(trip.isEmpty()){
            return false;
        }
        return true;
    }

    private Stop sourceExists(String stopName) {

        Stop stopByName = stopRepository.findByName(stopName);

        return stopByName;
    }

    private boolean agencyExistsByCode(String agencyCode) {
        Agency agency = agencyRepository.findByCode(agencyCode);

        if(agency == null){
            return false;
        }
        return true;
    }

    private boolean busExistsByCode(String busCode) {
        Bus bus = busRepository.findByCode(busCode);
        if(bus == null){
            return false;
        }
        return true;
    }


    private boolean isAdmin(Principal principal) {

        if (principal == null) {
            return false;
        }

        String username = principal.getName();
        User user = userService.findByEmail(username);
        Role role = user.getRole();


        // Check if user has ADMIN role
        if(role != null && role.getRole().toString().equalsIgnoreCase("ROLE_ADMIN")){
            return true;
        }
        return false;

    }

}

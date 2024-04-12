package com.devlukas.hotelreservationsystem.controllers.hotel;

import com.devlukas.hotelreservationsystem.controllers.hotel.converter.HotelToResponse;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/user")
public class HotelControllerUserUsage {

    private final HotelService hotelService;

    private final HotelToResponse hotelToResponse;

    public HotelControllerUserUsage(HotelService hotelService, HotelToResponse hotelToResponse) {
        this.hotelService = hotelService;
        this.hotelToResponse = hotelToResponse;
    }

    @GetMapping
    public ResponseEntity<Result> findAllHotels(@RequestParam(required = false) String state,
                                                @RequestParam(required = false) String city,
                                                HttpServletRequest request) {
        List<Hotel> hotels;

        if(!(state == null) && !state.isBlank()) {
            hotels = this.hotelService.findByState(state);
        } else if (!(city == null) && !city.isBlank()) {
            hotels = this.hotelService.findByCity(city);
        } else {
            hotels = this.hotelService.findAll();
        }

        var response = hotels.stream().map(this.hotelToResponse::convert).toList();

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Find all success")
                        .localDateTime(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }
}

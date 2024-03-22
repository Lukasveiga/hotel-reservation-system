package com.devlukas.hotelreservationsystem.controllers.hotel;

import com.devlukas.hotelreservationsystem.controllers.hotel.converter.HotelToResponse;
import com.devlukas.hotelreservationsystem.controllers.hotel.converter.RequestToHotel;
import com.devlukas.hotelreservationsystem.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.controllers.system.Result;
import com.devlukas.hotelreservationsystem.services.hotel.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel")
public class HotelController {

    private final HotelService hotelService;

    private final RequestToHotel requestToHotel;

    private final HotelToResponse hotelToResponse;

    public HotelController(HotelService hotelService, RequestToHotel requestToHotel, HotelToResponse hotelToResponse) {
        this.hotelService = hotelService;
        this.requestToHotel = requestToHotel;
        this.hotelToResponse = hotelToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveHotel(@RequestBody HotelRequestBody requestBody, HttpServletRequest request) {
        var newHotel = this.requestToHotel.convert(requestBody);
        var savedHotel = this.hotelService.save(Objects.requireNonNull(newHotel));
        var response = this.hotelToResponse.convert(savedHotel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add success")
                        .localDateTime(LocalDateTime.now())
                        .data(response).build());
    }
}

package com.devlukas.hotelreservationsystem.controllers.hotelAdmin;

import com.devlukas.hotelreservationsystem.controllers.hotelAdmin.converter.HotelAdminToResponse;
import com.devlukas.hotelreservationsystem.controllers.hotelAdmin.converter.RequestToHotelAdmin;
import com.devlukas.hotelreservationsystem.controllers.hotelAdmin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.services.hotelAdmin.HotelAdminService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel-admin")
public class HotelAdminController {

    private final HotelAdminService hotelAdminService;

    private final RequestToHotelAdmin requestToHotelAdmin;

    private final HotelAdminToResponse hotelAdminToResponse;

    public HotelAdminController(HotelAdminService hotelAdminService, RequestToHotelAdmin requestToHotelAdmin, HotelAdminToResponse hotelAdminToResponse) {
        this.hotelAdminService = hotelAdminService;
        this.requestToHotelAdmin = requestToHotelAdmin;
        this.hotelAdminToResponse = hotelAdminToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveHotelAdmin(@RequestBody HotelAdminRequestBody requestBody, HttpServletRequest request) {
        var hotel = this.requestToHotelAdmin.convert(requestBody);
        var savedHotel = this.hotelAdminService.save(Objects.requireNonNull(hotel));
        var response = this.hotelAdminToResponse.convert(savedHotel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add success")
                        .localDateTime(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }
}

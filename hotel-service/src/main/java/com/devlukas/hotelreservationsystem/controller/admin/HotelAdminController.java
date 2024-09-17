package com.devlukas.hotelreservationsystem.controller.admin;

import com.devlukas.hotelreservationsystem.config.api.ResultBody;
import com.devlukas.hotelreservationsystem.controller.admin.converter.HotelAdminEntityToResponse;
import com.devlukas.hotelreservationsystem.controller.admin.converter.HotelAdminRequestToEntity;
import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.usecases.admin.CreateHotelAdmin;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.endpoint.base}/admin")
public class HotelAdminController {

    private final CreateHotelAdmin createHotelAdmin;
    private final HotelAdminRequestToEntity hotelAdminRequestToEntity;
    private final HotelAdminEntityToResponse hotelAdminEntityToResponse;

    public HotelAdminController(CreateHotelAdmin createHotelAdmin, HotelAdminRequestToEntity hotelAdminRequestToEntity, HotelAdminEntityToResponse hotelAdminEntityToResponse) {
        this.createHotelAdmin = createHotelAdmin;
        this.hotelAdminRequestToEntity = hotelAdminRequestToEntity;
        this.hotelAdminEntityToResponse = hotelAdminEntityToResponse;
    }

    @PostMapping
    public ResponseEntity<ResultBody> create(@RequestBody @Validated HotelAdminRequestBody requestBody, HttpServletRequest request) {
        var hotelAdmin = this.hotelAdminRequestToEntity.convert(requestBody);
        var savedHotelAdmin = this.createHotelAdmin.execute(hotelAdmin);
        var response =this.hotelAdminEntityToResponse.convert(savedHotelAdmin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ResultBody.builder()
                                .path(request.getRequestURI())
                                .flag(true)
                                .dateTime(LocalDateTime.now())
                                .message("Hotel admin created successfully")
                                .data(response)
                                .build()
                );
    }
}

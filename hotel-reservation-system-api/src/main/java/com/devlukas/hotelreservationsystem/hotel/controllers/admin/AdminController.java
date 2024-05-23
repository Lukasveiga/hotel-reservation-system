package com.devlukas.hotelreservationsystem.hotel.controllers.admin;

import com.devlukas.hotelreservationsystem.hotel.controllers.admin.converter.AdminToResponse;
import com.devlukas.hotelreservationsystem.hotel.controllers.admin.converter.RequestToAdmin;
import com.devlukas.hotelreservationsystem.hotel.controllers.admin.dto.AdminRequestBody;
import com.devlukas.hotelreservationsystem.hotel.services.admin.AdminService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/account")
public class AdminController {

    private final AdminService adminService;

    private final RequestToAdmin requestToAdmin;

    private final AdminToResponse adminToResponse;

    public AdminController(AdminService adminService, RequestToAdmin requestToAdmin, AdminToResponse adminToResponse) {
        this.adminService = adminService;
        this.requestToAdmin = requestToAdmin;
        this.adminToResponse = adminToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveHotelAdmin(@RequestBody @Validated AdminRequestBody requestBody, HttpServletRequest request) {
        var hotel = this.requestToAdmin.convert(requestBody);
        var savedHotel = this.adminService.save(Objects.requireNonNull(hotel));
        var response = this.adminToResponse.convert(savedHotel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add hotel admin account success")
                        .localDateTime(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }
}

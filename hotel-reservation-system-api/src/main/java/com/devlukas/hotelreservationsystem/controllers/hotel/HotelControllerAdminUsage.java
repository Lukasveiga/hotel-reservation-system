package com.devlukas.hotelreservationsystem.controllers.hotel;

import com.devlukas.hotelreservationsystem.controllers.hotel.converter.HotelToResponse;
import com.devlukas.hotelreservationsystem.controllers.hotel.converter.RequestToHotel;
import com.devlukas.hotelreservationsystem.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/admin")
public class HotelControllerAdminUsage {

    private final HotelService hotelService;

    private final RequestToHotel requestToHotel;

    private final HotelToResponse hotelToResponse;

    public HotelControllerAdminUsage(HotelService hotelService, RequestToHotel requestToHotel, HotelToResponse hotelToResponse) {
        this.hotelService = hotelService;
        this.requestToHotel = requestToHotel;
        this.hotelToResponse = hotelToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveHotel(@RequestBody HotelRequestBody requestBody, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var newHotel = this.requestToHotel.convert(requestBody);
        var savedHotel = this.hotelService.save(Objects.requireNonNull(newHotel), hotelAdminCNPJ);
        var response = this.hotelToResponse.convert(savedHotel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add success")
                        .localDateTime(LocalDateTime.now())
                        .data(response).build());
    }

    @GetMapping
    public ResponseEntity<Result> findAllHotels(HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var hotels = this.hotelService.findAllByCNPJ(hotelAdminCNPJ);
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

    @GetMapping("/{hotelId}")
    public ResponseEntity<Result> findHotelById(@PathVariable Long hotelId, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);
        var response = this.hotelToResponse.convert(hotel);

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Find success")
                        .localDateTime(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<Result> updateHotel( @PathVariable Long hotelId, @RequestBody HotelRequestBody requestBody, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var updatedHotel = this.hotelService.update(hotelId, hotelAdminCNPJ, Objects.requireNonNull(this.requestToHotel.convert(requestBody)));
        var response = this.hotelToResponse.convert(updatedHotel);

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Update success")
                        .localDateTime(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Result> deleteHotel(@PathVariable Long hotelId, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        this.hotelService.delete(hotelId, hotelAdminCNPJ);

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Delete success")
                        .localDateTime(LocalDateTime.now())
                        .data(null)
                        .build()
        );
    }

    private String getTokenAttribute(String attributeKey) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) authentication).getTokenAttributes().get(attributeKey).toString();
    }
}

package com.devlukas.hotelreservationsystem.hotel.controllers.hotel;

import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.converter.HotelToResponse;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.converter.RequestToHotel;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.ConvenienceRequestBody;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/admin")
public class HotelControllerAdmin {

    private final HotelService hotelService;

    private final RequestToHotel requestToHotel;

    private final HotelToResponse hotelToResponse;

    public HotelControllerAdmin(HotelService hotelService, RequestToHotel requestToHotel, HotelToResponse hotelToResponse) {
        this.hotelService = hotelService;
        this.requestToHotel = requestToHotel;
        this.hotelToResponse = hotelToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveHotel(@RequestBody @Validated HotelRequestBody requestBody, HttpServletRequest request) {
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
    public ResponseEntity<Result> updateHotel(@PathVariable Long hotelId, @RequestBody HotelRequestBody requestBody, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var updatedHotel = this.hotelService.updateBasicHotelInfo(hotelId, hotelAdminCNPJ, Objects.requireNonNull(this.requestToHotel.convert(requestBody)));
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

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Result> addHotelConvenience(@PathVariable Long hotelId, @RequestBody ConvenienceRequestBody convenienceRequestBody,
                                                      HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        this.hotelService.addConvenience(hotelId, hotelAdminCNPJ, convenienceRequestBody.description());

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add convenience success")
                        .localDateTime(LocalDateTime.now())
                        .build());
    }

    @PatchMapping("/{hotelId}/convenience/{convenienceId}")
    public ResponseEntity<Result> removeHotelConvenience(@PathVariable Long hotelId, @PathVariable Long convenienceId,
                                                         HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");

        var deletedRows = this.hotelService.removeConvenience(hotelId, hotelAdminCNPJ, convenienceId);

        var message = "Convenience already removed or not exist";

        if(deletedRows > 0) {
            message = "Remove convenience success";
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message(message)
                        .localDateTime(LocalDateTime.now())
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

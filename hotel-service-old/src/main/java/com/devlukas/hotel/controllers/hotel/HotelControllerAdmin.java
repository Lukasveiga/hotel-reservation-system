package com.devlukas.hotel.controllers.hotel;

import com.devlukas.hotel.controllers.hotel.converter.HotelToResponse;
import com.devlukas.hotel.controllers.hotel.converter.RequestToHotel;
import com.devlukas.hotel.controllers.hotel.dto.ConvenienceRequestBody;
import com.devlukas.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotel.services.hotel.HotelService;
import com.devlukas.system.Result;
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
@RequestMapping("${api.endpoint.base-url}/hotel-admin")
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
        var adminId = getTokenAttribute("id");

        var newHotel = this.requestToHotel.convert(requestBody);
        var savedHotel = this.hotelService.save(Objects.requireNonNull(newHotel), Long.getLong(adminId));
        var response = this.hotelToResponse.convert(savedHotel);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add success")
                        .localDateTime(LocalDateTime.now())
                        .data(response).build());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Result> findHotelById(@PathVariable Long hotelId, HttpServletRequest request) {
        var adminId = getTokenAttribute("id");

        var hotel = this.hotelService.findOne(hotelId, Long.getLong(adminId));
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
    public ResponseEntity<Result> updateHotel(@PathVariable Long hotelId, @RequestBody @Validated HotelRequestBody requestBody, HttpServletRequest request) {
        var adminId = getTokenAttribute("id");

        var updatedHotel = this.hotelService.updateBasicHotelInfo(hotelId, Long.getLong(adminId), Objects.requireNonNull(this.requestToHotel.convert(requestBody)));
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

    @PatchMapping("/{hotelId}/convenience")
    public ResponseEntity<Result> addHotelConvenience(@PathVariable Long hotelId, @RequestBody @Validated ConvenienceRequestBody convenienceRequestBody,
                                                      HttpServletRequest request) {
        var adminId = getTokenAttribute("id");

        this.hotelService.addConvenience(hotelId, Long.getLong(adminId), convenienceRequestBody.description());

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
        var adminId = getTokenAttribute("id");

        var deletedRows = this.hotelService.removeConvenience(hotelId, Long.getLong(adminId), convenienceId);

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
        var adminId = getTokenAttribute("id");

        this.hotelService.delete(hotelId, Long.getLong(adminId));

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

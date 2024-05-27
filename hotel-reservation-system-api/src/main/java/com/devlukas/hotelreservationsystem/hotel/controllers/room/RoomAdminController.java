package com.devlukas.hotelreservationsystem.hotel.controllers.room;

import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel-admin/{hotelId}/room")
public class RoomAdminController {

    /* TODO:
     *   - Save room give a hotel id
     *   - Find room by id and hotel id
     *   - Find all rooms give a hotel id
     *   - Update room by id give a hotel id
     *   - Delete room by id give a hotel id
     * */

    private final HotelService hotelService;

    public RoomAdminController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<Result> saveRoom(@PathVariable("hotelId") Long hotelId,  HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Create success")
                        .localDateTime(LocalDateTime.now())
                        .data(hotel)
                        .build()
        );
    }

    private String getTokenAttribute(String attributeKey) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) authentication).getTokenAttributes().get(attributeKey).toString();
    }
}

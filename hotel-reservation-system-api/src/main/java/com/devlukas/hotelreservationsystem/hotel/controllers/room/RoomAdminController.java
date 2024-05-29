package com.devlukas.hotelreservationsystem.hotel.controllers.room;

import com.devlukas.hotelreservationsystem.hotel.controllers.room.converter.RequestToRoom;
import com.devlukas.hotelreservationsystem.hotel.controllers.room.converter.RoomToResponse;
import com.devlukas.hotelreservationsystem.hotel.controllers.room.dto.RoomRequestBody;
import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.hotel.services.room.RoomService;
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

    private final RoomService roomService;

    private final RequestToRoom requestToRoomConverter;

    private final RoomToResponse roomToResponseConverter;

    public RoomAdminController(HotelService hotelService, RoomService roomService, RequestToRoom requestToRoomConverter, RoomToResponse roomToResponseConverter) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.requestToRoomConverter = requestToRoomConverter;
        this.roomToResponseConverter = roomToResponseConverter;
    }

    @PostMapping
    public ResponseEntity<Result> saveRoom(@PathVariable("hotelId") Long hotelId, @RequestBody @Validated RoomRequestBody roomRequestBody,
                                           HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        var savedRoom = this.roomService.save(hotel.getId(),
                Objects.requireNonNull(this.requestToRoomConverter.convert(roomRequestBody)));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Add success")
                        .localDateTime(LocalDateTime.now())
                        .data(this.roomToResponseConverter.convert(savedRoom))
                        .build()
        );
    }

    private String getTokenAttribute(String attributeKey) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) authentication).getTokenAttributes().get(attributeKey).toString();
    }
}

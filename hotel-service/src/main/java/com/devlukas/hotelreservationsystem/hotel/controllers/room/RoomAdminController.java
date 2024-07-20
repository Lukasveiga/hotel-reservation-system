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

    @GetMapping
    public ResponseEntity<Result> findAllRooms(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        var rooms = this.roomService.findAll(hotel.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Find all success")
                        .localDateTime(LocalDateTime.now())
                        .data(rooms.stream().map(this.roomToResponseConverter::convert))
                        .build()
        );
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Result> findRoomById(@PathVariable("hotelId") Long hotelId, @PathVariable("roomId") Long roomId,
                                               HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        var room = this.roomService.findById(hotel.getId(), roomId);

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Find one success")
                        .localDateTime(LocalDateTime.now())
                        .data(this.roomToResponseConverter.convert(room))
                        .build()
        );
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Result> updateRoom(@PathVariable("hotelId") Long hotelId, @PathVariable("roomId") Long roomId,
                                             @RequestBody @Validated RoomRequestBody roomRequestBody, HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        var room = this.requestToRoomConverter.convert(roomRequestBody);

        var updateRoom = this.roomService.update(hotel.getId(), roomId, Objects.requireNonNull(room));

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Update success")
                        .localDateTime(LocalDateTime.now())
                        .data(this.roomToResponseConverter.convert(updateRoom))
                        .build()
        );
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Result> deleteRoom(@PathVariable("hotelId") Long hotelId, @PathVariable("roomId") Long roomId,
                                             HttpServletRequest request) {
        var hotelAdminCNPJ = getTokenAttribute("sub");
        var hotel = this.hotelService.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);

        this.roomService.delete(hotel.getId(), roomId);

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

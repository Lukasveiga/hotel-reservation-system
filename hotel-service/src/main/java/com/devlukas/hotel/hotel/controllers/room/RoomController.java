package com.devlukas.hotel.hotel.controllers.room;

import com.devlukas.hotel.hotel.services.room.RoomService;
import com.devlukas.hotel.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/{hotelId}/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<Result> findAllRooms(@PathVariable("hotelId") Long hotelId, HttpServletRequest request) {
        var rooms = this.roomService.findAll(hotelId);

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Find all success")
                        .localDateTime(LocalDateTime.now())
                        .data(rooms)
                        .build()
        );
    }
}

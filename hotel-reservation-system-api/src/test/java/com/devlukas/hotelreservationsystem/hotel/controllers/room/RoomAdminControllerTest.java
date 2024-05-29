package com.devlukas.hotelreservationsystem.hotel.controllers.room;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.room.dto.RoomRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.room.Room;
import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.hotel.services.room.RoomService;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.devlukas.hotelreservationsystem.hotel.utils.RoomUtils;
import com.devlukas.hotelreservationsystem.system.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithJwt("hotel-admin.json")
class RoomAdminControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

    @MockBean
    RoomService roomService;

    @Value("${api.endpoint.base-url}/hotel-admin/{hotelId}/room")
    String BASE_URL;

    @Autowired
    ObjectMapper objectMapper;

    Room room;

    Hotel hotel;

    @BeforeEach
    void setUp() {
        room = RoomUtils.generateRoomEntity();
        room.setId(1L);
        hotel = HotelUtils.generateHotelEntity();
        hotel.setId(1L);
        room.setHotel(hotel);
    }

    @Test
    void testSaveRoomSuccess() throws Exception {
        // Given
        var requestBody = new RoomRequestBody(
                room.getName(),
                room.getSize(),
                room.getBedsNumber(),
                room.getPrice(),
                room.getMaxGuestsNumber()
        );

        var request = this.objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.save(anyLong(), any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(post(BASE_URL, hotel.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(room.getName()))
                .andExpect(jsonPath("$.data.size").value(room.getSize()))
                .andExpect(jsonPath("$.data.bedsNumber").value(room.getBedsNumber()))
                .andExpect(jsonPath("$.data.price").value(room.getPrice()))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value(room.getMaxGuestsNumber()))
                .andExpect(jsonPath("$.data.situation").value("Available"))
                .andExpect(jsonPath("$.data.hotelId").value(hotel.getId()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveRoomErrorHotelNotFound() throws Exception {
        // Given
        var requestBody = new RoomRequestBody(
                room.getName(),
                room.getSize(),
                room.getBedsNumber(),
                room.getPrice(),
                room.getMaxGuestsNumber()
        );

        var request = this.objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenThrow(new ObjectNotFoundException("Hotel", hotel.getId()));

        // When - Then
        this.mockMvc.perform(post(BASE_URL, hotel.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + hotel.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveRoomErrorEmptyOrNullArgumentsProvided() throws Exception {
        // Given
        var requestBody = new RoomRequestBody(
                null,
                null,
                null,
                null,
                null
        );

        var request = this.objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.save(anyLong(), any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(post(BASE_URL, hotel.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.size").value("Cannot be null"))
                .andExpect(jsonPath("$.data.bedsNumber").value("Cannot be null"))
                .andExpect(jsonPath("$.data.price").value("Cannot be null"))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value("Cannot be null"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveRoomErrorInvalidNumericArguments() throws Exception {
        // Given
        var requestBody = new RoomRequestBody(
                room.getName(),
                -1.0,
                -1,
                -1,
                -1
        );

        var request = this.objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.save(anyLong(), any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(post(BASE_URL, hotel.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.size").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.bedsNumber").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.price").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value("Cannot be less than zero"))
                .andDo(MockMvcResultHandlers.print());
    }
}
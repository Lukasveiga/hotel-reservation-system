package com.devlukas.hotel.hotel.controllers.room;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.devlukas.hotel.ControllerTestConfig;
import com.devlukas.hotel.hotel.controllers.room.dto.RoomRequestBody;
import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import com.devlukas.hotel.hotel.entities.room.Room;
import com.devlukas.hotel.hotel.services.hotel.HotelService;
import com.devlukas.hotel.hotel.services.room.RoomService;
import com.devlukas.hotel.hotel.utils.HotelUtils;
import com.devlukas.hotel.hotel.utils.RoomUtils;
import com.devlukas.hotel.system.exceptions.ObjectNotFoundException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void testFindRoomByIdSuccess() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.findById(anyLong(), anyLong()))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}",
                        hotel.getId().toString()) + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(room.getName()))
                .andExpect(jsonPath("$.data.size").value(room.getSize()))
                .andExpect(jsonPath("$.data.bedsNumber").value(room.getBedsNumber()))
                .andExpect(jsonPath("$.data.price").value(room.getPrice()))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value(room.getMaxGuestsNumber()))
                .andExpect(jsonPath("$.data.situation").value(room.getSituation().getValue()))
                .andExpect(jsonPath("$.data.hotelId").value(hotel.getId()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindRoomByIdErrorHotelNotFound() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenThrow(new ObjectNotFoundException("Hotel", hotel.getId()));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}",
                        hotel.getId().toString()) + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + hotel.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindRoomByIdErrorRoomNotFound() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.findById(anyLong(), anyLong()))
                .thenThrow(new ObjectNotFoundException("Room", room.getId()));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}",
                        hotel.getId().toString()) + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Room with id " + room.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllRoomsSuccess() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        when(this.roomService.findAll(anyLong()))
                .thenReturn(List.of(room));

        // When - Then
        this.mockMvc.perform(get(BASE_URL, hotel.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value(room.getName()))
                .andExpect(jsonPath("$.data[0].size").value(room.getSize()))
                .andExpect(jsonPath("$.data[0].bedsNumber").value(room.getBedsNumber()))
                .andExpect(jsonPath("$.data[0].price").value(room.getPrice()))
                .andExpect(jsonPath("$.data[0].maxGuestsNumber").value(room.getMaxGuestsNumber()))
                .andExpect(jsonPath("$.data[0].situation").value(room.getSituation().getValue()))
                .andExpect(jsonPath("$.data[0].hotelId").value(hotel.getId()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllRoomsErrorHotelNotFound() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenThrow(new ObjectNotFoundException("Hotel", hotel.getId()));

        // When - Then
        this.mockMvc.perform(get(BASE_URL, hotel.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
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
    void testUpdateRoomSuccess() throws Exception {
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

        when(this.roomService.update(anyLong(), anyLong(),any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(room.getName()))
                .andExpect(jsonPath("$.data.size").value(room.getSize()))
                .andExpect(jsonPath("$.data.bedsNumber").value(room.getBedsNumber()))
                .andExpect(jsonPath("$.data.price").value(room.getPrice()))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value(room.getMaxGuestsNumber()))
                .andExpect(jsonPath("$.data.situation").value(room.getSituation().getValue()))
                .andExpect(jsonPath("$.data.hotelId").value(hotel.getId()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateRoomErrorHotelNotFound() throws Exception {
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
        this.mockMvc.perform(put(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + hotel.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateRoomErrorRoomNotFound() throws Exception {
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

        when(this.roomService.update(anyLong(), anyLong(),any(Room.class)))
                .thenThrow(new ObjectNotFoundException("Room", hotel.getId()));

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Room with id " + room.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateErrorEmptyOrNullArgumentsProvided() throws Exception {
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

        when(this.roomService.update(anyLong(), anyLong(),any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
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
    void testUpdateRoomErrorInvalidNumericArguments() throws Exception {
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

        when(this.roomService.update(anyLong(), anyLong(),any(Room.class)))
                .thenReturn(room);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.size").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.bedsNumber").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.price").value("Cannot be less than zero"))
                .andExpect(jsonPath("$.data.maxGuestsNumber").value("Cannot be less than zero"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteRoomSuccess() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        doNothing().when(this.roomService)
                .delete(anyLong(), anyLong());

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteRoomErrorHotelNotFound() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenThrow(new ObjectNotFoundException("Hotel", hotel.getId()));

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + hotel.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteRoomErrorRoomNotFound() throws Exception {
        // Given
        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        doThrow(new ObjectNotFoundException("Room", hotel.getId()))
                .when(this.roomService).delete(anyLong(), anyLong());

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/{roomId}", hotel.getId(), room.getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL.replace("{hotelId}", hotel.getId().toString())
                        + "/" + room.getId()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Room with id " + room.getId()))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}
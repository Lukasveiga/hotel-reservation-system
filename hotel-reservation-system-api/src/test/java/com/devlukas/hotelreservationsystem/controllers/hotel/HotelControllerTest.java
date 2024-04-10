package com.devlukas.hotelreservationsystem.controllers.hotel;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.devlukas.hotelreservationsystem.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.utils.HotelUtils;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithJwt("hotel-admin.json")
class HotelControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}/hotel")
    String BASE_URL;

    Hotel hotel;

    HotelAddress address;

    @BeforeEach
    void setUp() {
        address = HotelUtils.generateHotelAddress();
        address.setId(1L);
        hotel = HotelUtils.generateHotelEntity(address);
        hotel.setId(1L);
    }

    @Test
    void testSaveHotelSuccess() throws Exception {
        // Given
        var requestBody = new HotelRequestBody(
                hotel.getName(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getDescription(),
                hotel.getAddress()
        );

        var request = objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.save(any(Hotel.class), anyString()))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(requestBody.name()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdSuccess() throws Exception {
        // Given
        var id = 1L;

        when(this.hotelService.findById(id))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(hotel.getName()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(hotel.getPhone()))
                .andExpect(jsonPath("$.data.email").value(hotel.getEmail()))
                .andExpect(jsonPath("$.data.description").value(hotel.getDescription()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 1L;

        when(this.hotelService.findById(id))
                .thenThrow(new ObjectNotFoundException("Hotel", id));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllHotelsSuccess() throws Exception {
        // Given
        when(this.hotelService.findAll())
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value(hotel.getName()))
                .andExpect(jsonPath("$.data[0].CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data[0].phone").value(hotel.getPhone()))
                .andExpect(jsonPath("$.data[0].email").value(hotel.getEmail()))
                .andExpect(jsonPath("$.data[0].description").value(hotel.getDescription()))
                .andExpect(jsonPath("$.data[0].address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateHotelSuccess() throws Exception {
        // Given
        var id = 1L;

        var requestBody = new HotelRequestBody(
                hotel.getName(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getDescription(),
                hotel.getAddress()
        );

        var request = objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.update(anyLong(),any(Hotel.class)))
               .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(requestBody.name()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 1L;

        var requestBody = new HotelRequestBody(
                hotel.getName(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getDescription(),
                hotel.getAddress()
        );

        var request = objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.update(anyLong(),any(Hotel.class)))
                .thenThrow(new ObjectNotFoundException("Hotel", id));

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteHotelByIdSuccess() throws Exception {
        // Given
        var id = 1L;

        doNothing().when(this.hotelService)
                .delete(id);

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 1L;

        doThrow(new ObjectNotFoundException("Hotel", id))
                .when(this.hotelService).delete(anyLong());

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}
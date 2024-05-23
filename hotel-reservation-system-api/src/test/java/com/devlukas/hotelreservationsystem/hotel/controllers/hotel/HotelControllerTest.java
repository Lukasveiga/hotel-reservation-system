package com.devlukas.hotelreservationsystem.hotel.controllers.hotel;

import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.devlukas.hotelreservationsystem.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HotelControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

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
    void testFindAllHotelsSuccess() throws Exception {
        // Given
        when(this.hotelService.findAll())
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
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
    void testFindAllHotelsPageableSuccess() throws Exception {
        // Given
        when(this.hotelService.findAllPageable(anyInt(), anyInt()))
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get(BASE_URL +"?page=1&size=5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
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
    void testFindHotelsByState() throws Exception {
        // Given
        when(this.hotelService.findByState(anyString()))
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter?state=" + hotel.getAddress().getState()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
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
    void testFindHotelsByCity() throws Exception {
        // Given
        when(this.hotelService.findByCity(anyString()))
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter?city=" + hotel.getAddress().getCity()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
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
    void testFindHotelsFilterNullParams() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testInternalSeverError() throws Exception {
        // Given
        when(this.hotelService.findByCity(anyString()))
                .thenThrow(new RuntimeException());

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter?city=" + hotel.getAddress().getCity()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Internal Server Error"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testInvalidUrlPath() throws Exception {
        // Given
        when(this.hotelService.findByCity(anyString()))
                .thenReturn(List.of(hotel));

        // When - Then
        this.mockMvc.perform(get("/invalid-url").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/invalid-url"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("API endpoint not found"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}
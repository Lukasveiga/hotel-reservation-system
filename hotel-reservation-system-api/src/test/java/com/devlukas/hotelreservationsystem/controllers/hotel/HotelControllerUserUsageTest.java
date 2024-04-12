package com.devlukas.hotelreservationsystem.controllers.hotel;

import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.utils.HotelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HotelControllerUserUsageTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

    @Value("${api.endpoint.base-url}/hotel/user")
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
}
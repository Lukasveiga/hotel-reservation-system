package com.devlukas.hotelreservationsystem.controllers.hotelAdmin;

import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.controllers.hotelAdmin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.services.exceptions.UniqueIdentifierAlreadyExistsException;
import com.devlukas.hotelreservationsystem.services.hotelAdmin.HotelAdminService;
import com.devlukas.hotelreservationsystem.utils.HotelAdminUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class HotelAdminControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelAdminService hotelAdminService;

    @Autowired
    ObjectMapper objectMapper;

    HotelAdmin hotelAdmin;

    @Value("${api.endpoint.base-url}/hotel-admin")
    String BASE_URL;

    @BeforeEach
    void setUp() {
        hotelAdmin = HotelAdminUtils.generateHotelAdminEntity();
    }

    @Test
    void testSaveNewUserAdminAccountSuccess() throws Exception {
        // Given
        var hotelAdminDto = new HotelAdminRequestBody(
                hotelAdmin.getCNPJ(), hotelAdmin.getPassword(), hotelAdmin.getRoles());

        var hotelAdminDtoJson = this.objectMapper.writeValueAsString(hotelAdminDto);

        when(this.hotelAdminService.save(any(HotelAdmin.class)))
                .thenReturn(hotelAdmin);

        // When - Then
        this.mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(hotelAdminDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel-admin"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.CNPJ").value(hotelAdminDto.CNPJ()))
                .andExpect(jsonPath("$.data.roles").value(hotelAdminDto.roles()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveNewUserAdminAccountErrorUniqueIdentifierAlreadyExists() throws Exception {
        // Given
        var hotelAdminDto = new HotelAdminRequestBody(
                hotelAdmin.getCNPJ(), hotelAdmin.getPassword(), hotelAdmin.getRoles());

        var hotelAdminDtoJson = this.objectMapper.writeValueAsString(hotelAdminDto);

        when(this.hotelAdminService.save(any(HotelAdmin.class)))
                .thenThrow(new UniqueIdentifierAlreadyExistsException("CNPJ"));

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelAdminDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel-admin"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("The CNPJ provided has already been registered in the database"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

}
package com.devlukas.hotelreservationsystem.controller.admin;


import com.devlukas.hotelreservationsystem.configuration.ControllerTestConfiguration;
import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.usecases.admin.CreateHotelAdmin;
import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExistsException;
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

class HotelAdminControllerTest extends ControllerTestConfiguration {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreateHotelAdmin createHotelAdmin;

    @Value("${api.endpoint.base}")
    String base_url;

    @Autowired
    ObjectMapper objectMapper;

    HotelAdmin hotelAdminTest;

    HotelAdminRequestBody request;

    @BeforeEach
    void setUp() {
        hotelAdminTest = HotelAdminUtils.generateHotelAdmin();
        request = HotelAdminUtils.generateHotelAdminRequestBody();
    }

    @Test
    void test_Create_Success() throws Exception {
        // Given
        var requestJson = this.objectMapper.writeValueAsString(request);

        when(this.createHotelAdmin.execute(any(HotelAdmin.class)))
                .thenReturn(hotelAdminTest);

        // When - Then
        this.mockMvc.perform(post(base_url + "/admin").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(base_url + "/admin"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.message").value("Hotel admin created successfully"))
                .andExpect(jsonPath("$.data.id").value(hotelAdminTest.getId()))
                .andExpect(jsonPath("$.data.email").value(hotelAdminTest.getEmail()))
                .andExpect(jsonPath("$.data.phone").value(hotelAdminTest.getPhone()))
                .andExpect(jsonPath("$.data.cnpj").value(hotelAdminTest.getCnpj()))
                .andExpect(jsonPath("$.data.roles").value(hotelAdminTest.getRoles()))
                .andExpect(jsonPath("$.data.isActive").value(hotelAdminTest.isActive()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void test_Create_Bad_Request_When_Unique_Identifier_Provided_Already_Exists() throws Exception {
        // Given
        var requestJson = this.objectMapper.writeValueAsString(request);

        when(this.createHotelAdmin.execute(any(HotelAdmin.class)))
                .thenThrow(new UniqueIdentifierAlreadyExistsException("email/cnpj"));

        // When - Then
        this.mockMvc.perform(post(base_url + "/admin").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(base_url + "/admin"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.message").value("The %s provided has already been registered".formatted("email/cnpj")))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void test_Create_Bad_Request_When_Invalid_Cnpj_Is_Provided() throws Exception {
        // Given
        var requestJson = this.objectMapper.writeValueAsString(request);

        when(this.createHotelAdmin.execute(any(HotelAdmin.class)))
                .thenThrow(new IllegalArgumentException("Invalid CNPJ"));

        // When - Then
        this.mockMvc.perform(post(base_url + "/admin").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(base_url + "/admin"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.message").value("Invalid CNPJ"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void test_Create_Bad_Request_When_Invalid_Arguments_Are_Provided() throws Exception{
        // Given
        var invalidRequest = new HotelAdminRequestBody("", "", "", "");

        var requestJson = this.objectMapper.writeValueAsString(invalidRequest);

        // When - Then
        this.mockMvc.perform(post(base_url + "/admin").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(base_url + "/admin"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.data.email").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.password").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.phone").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.cnpj").value("Cannot be empty or null"))
                .andDo(MockMvcResultHandlers.print());
    }
}
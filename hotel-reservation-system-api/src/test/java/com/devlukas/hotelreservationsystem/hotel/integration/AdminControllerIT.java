package com.devlukas.hotelreservationsystem.hotel.integration;

import com.devlukas.hotelreservationsystem.IntegrationTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.admin.dto.AdminRequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminControllerIT extends IntegrationTestConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value(value = "${api.endpoint.base-url}/hotel/account")
    String BASE_URL;

    AdminRequestBody request;

    String requestJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        request = new AdminRequestBody("18.104.644/0001-95", "test12345");

        requestJson = this.objectMapper.writeValueAsString(request);
    }

    @Test
    @Order(1)
    void testCreateHotelAdminAccountSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add hotel admin account success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.CNPJ").value(request.CNPJ()))
                .andExpect(jsonPath("$.data.roles").value("hotelAdmin"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testCreateHotelAdminAccountBadRequestUniqueIdentifierAlreadyExists() throws Exception {
        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("The CNPJ provided has already been registered"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testCreateHotelAdminAccountBadRequestEmptyOrNullArgumentsProvided() throws Exception {
        // Given
        var invalidRequest = new AdminRequestBody(null, null);

        var invalidRequestJson = this.objectMapper.writeValueAsString(invalidRequest);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.password").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.CNPJ").value("Cannot be empty or null"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveNewHotelAdminAccountErrorInvalidPasswordLengthProvided() throws Exception {
        // Given
        var invalidPassword = "12345";
        var invalidRequest = new AdminRequestBody("85.576.423/0001-07", invalidPassword);

        var invalidRequestJson = this.objectMapper.writeValueAsString(invalidRequest);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.password").value("Must to be at least 8 characters long"))
                .andDo(MockMvcResultHandlers.print());
    }
}

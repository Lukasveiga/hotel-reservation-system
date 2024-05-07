package com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin;

import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.system.exceptions.UniqueIdentifierAlreadyExistsException;
import com.devlukas.hotelreservationsystem.hotel.services.hotelAdmin.HotelAdminService;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelAdminUtils;
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
    void testSaveNewHotelAdminAccountSuccess() throws Exception {
        // Given
        var hotelAdminDto = new HotelAdminRequestBody(
                hotelAdmin.getCNPJ(), hotelAdmin.getPassword());

        var hotelAdminDtoJson = this.objectMapper.writeValueAsString(hotelAdminDto);

        when(this.hotelAdminService.save(any(HotelAdmin.class)))
                .thenReturn(hotelAdmin);

        // When - Then
        this.mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(hotelAdminDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel-admin"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add hotel admin account success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.CNPJ").value(hotelAdminDto.CNPJ()))
                .andExpect(jsonPath("$.data.roles").value("hotelAdmin"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveNewHotelAdminAccountErrorUniqueIdentifierAlreadyExists() throws Exception {
        // Given
        var hotelAdminDto = new HotelAdminRequestBody(
                hotelAdmin.getCNPJ(), hotelAdmin.getPassword());

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

    @Test
    void testSaveNewHotelAdminAccountErrorEmptyOrNullArgumentsProvided() throws Exception {
        // Given
        var hotelAdminDto = new HotelAdminRequestBody(null, null);

        var hotelAdminDtoJson = this.objectMapper.writeValueAsString(hotelAdminDto);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelAdminDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel-admin"))
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
        var hotelAdminDto = new HotelAdminRequestBody(
                hotelAdmin.getCNPJ(), invalidPassword);

        var hotelAdminDtoJson = this.objectMapper.writeValueAsString(hotelAdminDto);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelAdminDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/api/v1/hotel-admin"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.password").value("Must to be at least 8 characters long"))
                .andDo(MockMvcResultHandlers.print());
    }
}
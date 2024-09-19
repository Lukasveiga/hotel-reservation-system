package com.devlukas.hotelreservationsystem.controller.admin;


import com.devlukas.hotelreservationsystem.configuration.ControllerTestConfiguration;
import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminResponseBody;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.usecases.admin.CreateHotelAdmin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    HotelAdmin hotelAdminTest = new HotelAdmin();

    @BeforeEach
    void setUp() {
        hotelAdminTest.setId(1L);
        hotelAdminTest.setEmail("hotel_admin_test@email.com");
        hotelAdminTest.setPhone("(55)97777-5555");
        hotelAdminTest.setCnpj("72.797.458/0001-24");
        hotelAdminTest.setRoles("admin");
    }

    @Test
    void test_Create_Success() throws Exception {
        // Given
        var request = new HotelAdminRequestBody(
                "test@email.com",
                "password",
                "(55)988774422",
                "28.315.742/0001-25"
        );

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
                .andExpect(jsonPath("$.data.isActive").value(hotelAdminTest.isActive()));
    }
}
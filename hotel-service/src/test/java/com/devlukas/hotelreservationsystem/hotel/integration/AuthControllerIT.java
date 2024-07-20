package com.devlukas.hotelreservationsystem.hotel.integration;

import com.devlukas.hotelreservationsystem.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT extends IntegrationTestConfig {

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}/hotel/login")
    String BASE_URL;

    @Test
    void testGetLoginInfoSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("06.596.172/0001-566", "test12345")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Hotel Admin Access Token"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.hotelAdminInfo.CNPJ").value("06.596.172/0001-566"))
                .andExpect(jsonPath("$.data.hotelAdminInfo.password").doesNotExist())
                .andExpect(jsonPath("$.data.hotelAdminInfo.roles").value("hotelAdmin"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetLoginInfoUnauthorizedInvalidUsername() throws Exception {
        // given
        var invalidCNPJ = "06.596.172/0001-333";

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(httpBasic(invalidCNPJ, "test12345")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Incorrect credentials"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetLoginInfoUnauthorizedInvalidPassword() throws Exception {
        // given
        var invalidPassword = "test777777";

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("06.596.172/0001-566",invalidPassword)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Incorrect credentials"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetLoginInfoUnauthorizedEmptyCredentials() throws Exception {
        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("","")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Incorrect credentials"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}

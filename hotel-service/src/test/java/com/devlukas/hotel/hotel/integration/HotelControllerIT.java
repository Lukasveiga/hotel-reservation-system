package com.devlukas.hotel.hotel.integration;

import com.devlukas.hotel.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HotelControllerIT extends IntegrationTestConfig {

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}/hotel")
    String BASE_URL;

    @Test
    void testFindAllHotelsSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllHotelsPageableSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL +"?page=1&size=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllHotelsPageableErrorPageIndexZero() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL +"?page=0&size=5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Page index must not be less than zero"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllHotelsPageableErrorPageSizeZero() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL +"?page=1&size=0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Page size must not be less than one"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelsByState() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter?state=" + "trans").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Hotel Transilvânia"))
                .andExpect(jsonPath("$.data[0].address.state").value("Transilvânia"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelsByCity() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/filter?city=" + "beve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/filter"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Hotel Beverly Wilshire"))
                .andExpect(jsonPath("$.data[0].address.city").value("Beverly Hills"))
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
}

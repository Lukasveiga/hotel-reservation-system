package com.devlukas.hotelreservationsystem.hotel.integration;

import com.devlukas.hotelreservationsystem.IntegrationTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HotelAdminController extends IntegrationTestConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}/hotel")
    String BASE_URL;

    String hotelCNPJ = "06.596.172/0001-566";

    String hotelAdminAccessToken;

    Hotel hotel;

    HotelAddress address;

    Convenience convenience;

    @BeforeEach
    void setUp() throws Exception {
        this.hotelAdminAccessToken = getAccessToken(hotelCNPJ, "test12345");
        address = HotelUtils.generateHotelAddress();
        convenience = HotelUtils.generateConvenience();
        hotel = HotelUtils.generateHotelEntity(address);
    }

    @Test
    void testSaveHotelSuccess() throws Exception {
        // Given
        var requestBody = new HotelRequestBody(
                hotel.getName(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getDescription(),
                hotel.getAddress(),
                hotel.getConveniences()
        );

        var request = objectMapper.writeValueAsString(requestBody);

        // When - Then
        this.mockMvc.perform(post(BASE_URL + "/admin")
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/admin"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(requestBody.name()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotelCNPJ))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address.country").value("Brazil"))
                .andExpect(jsonPath("$.data.conveniences").isEmpty())
                .andExpect(jsonPath("$.data.assessments").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdSuccess() throws Exception {
        // Given
        var id = 1;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/admin" + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/admin" + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 111L;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/admin" + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/admin" +  "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAllHotelsSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/admin")
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/admin"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessToken(String username, String password) throws Exception {
        var result = this.mockMvc.perform(post(BASE_URL + "/login")
                .with(httpBasic(username, password)));

        var mvcResult = result.andReturn();
        var stringContent = mvcResult.getResponse().getContentAsString();
        var jsonObject = new JSONObject(stringContent);

        return "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }
}

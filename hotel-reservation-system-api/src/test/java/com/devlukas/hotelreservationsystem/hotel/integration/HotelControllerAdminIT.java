package com.devlukas.hotelreservationsystem.hotel.integration;

import com.devlukas.hotelreservationsystem.IntegrationTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.ConvenienceRequestBody;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelControllerAdminIT extends IntegrationTestConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}/hotel-admin")
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
    @Order(1)
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
        this.mockMvc.perform(post(BASE_URL)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(BASE_URL))
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
    @Order(2)
    void testFindHotelByIdSuccess() throws Exception {
        // Given
        var id = 1;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(3)
    void testFindHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 111L;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(4)
    void findAllHotelsSuccess() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(5)
    void testUpdateHotelSuccess() throws Exception {
        // Given
        var id = 1L;

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
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(requestBody.name()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotelCNPJ))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(6)
    void testUpdateHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 111L;

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
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(7)
    void testAddHotelConvenienceSuccess() throws Exception {
        // Given
        var id = 1L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id + "/convenience")
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id + "/convenience"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add convenience success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(8)
    void testAddHotelConvenienceErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 111L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id + "/convenience")
                        .header("Authorization", this.hotelAdminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id + "/convenience"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(9)
    void testDeleteHotelByIdSuccess() throws Exception {
        // Given
        var id = 1L;

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(10)
    void testDeleteHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 111L;

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveHotelErrorMissingCredentials() throws Exception {
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
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdErrorMissingCredentials() throws Exception {
        // Given
        var id = 1;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAllHotelsErrorMissingCredentials() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateHotelErrorMissingCredentials() throws Exception {
        // Given
        var id = 1L;

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
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testAddHotelConvenienceErrorMissingCredentials() throws Exception {
        // Given
        var id = 1L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteHotelByIdErrorMissingCredentials() throws Exception {
        // Given
        var id = 1L;

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Login credentials are missing"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveHotelErrorInvalidBearerToken() throws Exception {
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
        this.mockMvc.perform(post(BASE_URL)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdErrorInvalidBearerToken() throws Exception {
        // Given
        var id = 1;

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAllHotelsErrorInvalidBearerToken() throws Exception {
        // When - Then
        this.mockMvc.perform(get(BASE_URL)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateHotelErrorInvalidBearerToken() throws Exception {
        // Given
        var id = 1L;

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
        this.mockMvc.perform(put(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testAddHotelConvenienceErrorInvalidBearerToken() throws Exception {
        // Given
        var id = 1L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteHotelByIdErrorInvalidBearerToken() throws Exception {
        // Given
        var id = 1L;

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id)
                        .header("Authorization", this.hotelAdminAccessToken + "invalid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Access token provided is expired, revoked, malformed, or invalid for other reasons"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessToken(String username, String password) throws Exception {
        var result = this.mockMvc.perform(post("/api/v1/hotel/login")
                .with(httpBasic(username, password)));

        var mvcResult = result.andReturn();
        var stringContent = mvcResult.getResponse().getContentAsString();
        var jsonObject = new JSONObject(stringContent);

        return "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }
}

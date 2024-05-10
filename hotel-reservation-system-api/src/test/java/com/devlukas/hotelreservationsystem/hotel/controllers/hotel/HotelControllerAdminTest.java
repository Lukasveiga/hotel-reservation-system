package com.devlukas.hotelreservationsystem.hotel.controllers.hotel;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.devlukas.hotelreservationsystem.ControllerTestConfig;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.ConvenienceRequestBody;
import com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.system.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.hotel.services.hotel.HotelService;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithJwt("hotel-admin.json")
class HotelControllerAdminTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}/hotel/admin")
    String BASE_URL;

    Hotel hotel;

    HotelAddress address;

    Convenience convenience;

    @BeforeEach
    void setUp() {
        address = HotelUtils.generateHotelAddress();
        address.setId(1L);
        convenience = HotelUtils.generateConvenience();
        hotel = HotelUtils.generateHotelEntity(address);
        hotel.setId(1L);
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

        when(this.hotelService.save(any(Hotel.class), anyString()))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(requestBody.name()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andExpect(jsonPath("$.data.conveniences").isEmpty())
                .andExpect(jsonPath("$.data.assessments").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSaveHotelSuccessErrorEmptyOrNullArgumentsProvided() throws Exception {
        // Given
        var requestBody = new HotelRequestBody(
                null,
                null,
                null,
                hotel.getDescription(),
                null,
                hotel.getConveniences()
        );

        var request = objectMapper.writeValueAsString(requestBody);

        when(this.hotelService.save(any(Hotel.class), anyString()))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.phone").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.email").value("Cannot be empty or null"))
                .andExpect(jsonPath("$.data.address").value("Cannot be null"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdSuccess() throws Exception {
        // Given
        var id = hotel.getId();

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(hotel.getName()))
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(hotel.getPhone()))
                .andExpect(jsonPath("$.data.email").value(hotel.getEmail()))
                .andExpect(jsonPath("$.data.description").value(hotel.getDescription()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andExpect(jsonPath("$.data.conveniences").isEmpty())
                .andExpect(jsonPath("$.data.assessments").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = hotel.getId();

        when(this.hotelService.findByIdAndCNPJ(anyLong(), anyString()))
                .thenThrow(new ObjectNotFoundException("Hotel", id));

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + id)
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
    void findAllHotelsSuccess() throws Exception {
        // Given
        when(this.hotelService.findAllByCNPJ(anyString()))
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
                .andExpect(jsonPath("$.data[0].conveniences").isEmpty())
                .andExpect(jsonPath("$.data[0].assessments").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
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

        when(this.hotelService.updateBasicHotelInfo(anyLong(),anyString(),any(Hotel.class)))
                .thenReturn(hotel);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + id)
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
                .andExpect(jsonPath("$.data.CNPJ").value(hotel.getCNPJ()))
                .andExpect(jsonPath("$.data.phone").value(requestBody.phone()))
                .andExpect(jsonPath("$.data.email").value(requestBody.email()))
                .andExpect(jsonPath("$.data.description").value(requestBody.description()))
                .andExpect(jsonPath("$.data.address").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateHotelByIdErrorObjectNotFoundException() throws Exception {
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

        when(this.hotelService.updateBasicHotelInfo(anyLong(),anyString(),any(Hotel.class)))
                .thenThrow(new ObjectNotFoundException("Hotel", id));

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + id)
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
    void testAddHotelConvenienceSuccess() throws Exception {
        // Given
        var id = 1L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        doNothing().when(this.hotelService)
                .addConvenience(anyLong(), anyString(), anyString());


        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add convenience success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testAddHotelConvenienceErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 1L;

        var convenienceRequestBody = new ConvenienceRequestBody("New Convenience");
        var convenienceRequestBodyJson = objectMapper.writeValueAsString(convenienceRequestBody);

        doThrow(new ObjectNotFoundException("Hotel", id))
                .when(this.hotelService).addConvenience(anyLong(), anyString(), anyString());

        // When - Then
        this.mockMvc.perform(patch(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convenienceRequestBodyJson)
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
    void testDeleteHotelByIdSuccess() throws Exception {
        // Given
        var id = 1L;

        doNothing().when(this.hotelService)
                .delete(anyLong(), anyString());

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteHotelByIdErrorObjectNotFoundException() throws Exception {
        // Given
        var id = 1L;

        doThrow(new ObjectNotFoundException("Hotel", id))
                .when(this.hotelService).delete(anyLong(), anyString());

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.message").value("Could not found Hotel with id " + id))
                .andExpect(jsonPath("$.localDateTime").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}
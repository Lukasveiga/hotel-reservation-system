package com.devlukas.hotelreservationsystem.services.hotel;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.utils.HotelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HotelServiceTest implements ServiceTestConfig {

    @Mock
    HotelRepository hotelRepository;

    @InjectMocks
    HotelService hotelService;

    Hotel hotel;

    HotelAddress address;

    String hotelAdminCNPJ = "87.933.894/0001-50";

    @BeforeEach
    void setUp() {
        address = HotelUtils.generateHotelAddress();
        hotel = HotelUtils.generateHotelEntity(address);
    }

    @Test
    void testSaveNewHotelSuccess() {
        // Given
        when(this.hotelRepository.save(hotel))
                .thenReturn(hotel);

        // When
        var savedHotel = this.hotelService.save(hotel, hotelAdminCNPJ);

        // Then
        assertThat(savedHotel).usingRecursiveAssertion().isEqualTo(hotel);
        assertThat(savedHotel.getCNPJ()).isEqualTo(hotelAdminCNPJ);
    }

    @Test
    void testFindAllHotelsSuccess() {
        // Given
        when(this.hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        // When
        var hotels = this.hotelService.findAll();

        // Then
        assertThat(hotels.size()).isEqualTo(1);
        assertThat(hotels.get(0)).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindAllHotelsByCNPJSuccess() {
        // Given
        when(this.hotelRepository.findByCNPJ(anyString()))
                .thenReturn(List.of(hotel));

        // When
        var hotels = this.hotelService.findAllByCNPJ(hotelAdminCNPJ);

        // Then
        assertThat(hotels.size()).isEqualTo(1);
        assertThat(hotels.get(0)).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindByIdAndCNPJSuccess() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        // When
        var foundHotel = this.hotelService.findByIdAndCNPJ(1L, hotelAdminCNPJ);

        // Then
        assertThat(foundHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindByIdAndCNPJErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.findByIdAndCNPJ(1L, hotelAdminCNPJ))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
    }

    @Test
    void testFindByHotelByState() {
        // Given
        when(this.hotelRepository.findByState(anyString()))
                .thenReturn(List.of(hotel));

        // When
        var result = this.hotelService.findByState(hotel.getAddress().getState());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindHotelByCity() {
        // Given
        when(this.hotelRepository.findByCity(anyString()))
                .thenReturn(List.of(hotel));

        // When
        var result = this.hotelService.findByCity(hotel.getAddress().getCity());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testUpdateHotelSuccess() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        when(this.hotelRepository.save(hotel))
                .thenReturn(hotel);

        // When
        var updatedHotel = this.hotelService.update(1L, hotelAdminCNPJ, updateHotel);

        // Then
        assertThat(updatedHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testUpdateHotelErrorHotelNotFound() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.update(1L, hotelAdminCNPJ, updateHotel))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(hotelRepository, times(0)).save(any(Hotel.class));
    }

    @Test
    void testDeleteHotelSuccess() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        doNothing().when(this.hotelRepository).deleteById(anyLong());

        // When
        this.hotelService.delete(1L,hotelAdminCNPJ);

        // Then
        verify(this.hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(this.hotelRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteHotelErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then


        assertThatThrownBy(() -> this.hotelService.delete(1L, hotelAdminCNPJ))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(this.hotelRepository, times(0)).deleteById(anyLong());
    }
}
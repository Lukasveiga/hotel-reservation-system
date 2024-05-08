package com.devlukas.hotelreservationsystem.hotel.services.hotel;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import com.devlukas.hotelreservationsystem.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest implements ServiceTestConfig {

    @Mock
    HotelRepository hotelRepository;

    @InjectMocks
    HotelService hotelService;

    Hotel hotel;

    HotelAddress address;

    @BeforeEach
    void setUp() {
        address = HotelUtils.generateHotelAddress();
        address.setId(1L);
        hotel = HotelUtils.generateHotelEntity(address);
        hotel.setId(1L);
    }

    @Test
    void testSaveNewHotelSuccess() {
        // Given
        when(this.hotelRepository.save(hotel))
                .thenReturn(hotel);

        // When
        var savedHotel = this.hotelService.save(hotel, hotel.getCNPJ());

        // Then
        assertThat(savedHotel).usingRecursiveAssertion().isEqualTo(hotel);
        assertThat(savedHotel.getCNPJ()).isEqualTo(hotel.getCNPJ());
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
    void testFindAllHotelsPageableSuccess() {
        // Given
        when(this.hotelRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(hotel)));

        // When
        var hotels = this.hotelService.findAllPageable(1,1);

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
        var hotels = this.hotelService.findAllByCNPJ(hotel.getCNPJ());

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
        var foundHotel = this.hotelService.findByIdAndCNPJ(hotel.getId(), hotel.getCNPJ());

        // Then
        assertThat(foundHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindByIdAndCNPJErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.findByIdAndCNPJ(hotel.getId(), hotel.getCNPJ()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
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
    void testUpdateBasicHotelInfoHotelSuccess() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        when(this.hotelRepository.save(hotel))
                .thenReturn(hotel);

        // When
        var updatedHotel = this.hotelService.updateBasicHotelInfo(hotel.getId(), hotel.getCNPJ(), updateHotel);

        // Then
        assertThat(updatedHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testUpdateBasicHotelInfoHotelErrorHotelNotFound() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.updateBasicHotelInfo(hotel.getId(), hotel.getCNPJ(), updateHotel))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(hotelRepository, times(0)).save(any(Hotel.class));
    }

    @Test
    void testAddConvenienceSuccess() {
        // Given
        var newConvenience = HotelUtils.generateConvenience();

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        doNothing().when(this.hotelRepository)
                .addConvenience(anyLong(), anyString());

        // When
        this.hotelService.addConvenience(hotel.getId(), hotel.getCNPJ(), newConvenience.getDescription());

        // Then
        verify(this.hotelRepository, times(1)).addConvenience(anyLong(), anyString());
    }

    @Test
    void testAddConvenienceErrorHotelNotFound() {
        // Given
        var newConvenience = HotelUtils.generateConvenience();

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.addConvenience(hotel.getId(), hotel.getCNPJ(), newConvenience.getDescription()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(hotelRepository, times(0)).save(any(Hotel.class));
    }

    @Test
    void testRemoveConvenienceSuccess() {
        // Given
        var convenienceId = 1L;

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        when(this.hotelRepository.removeConvenience(anyLong(), anyLong()))
                .thenReturn(1);

        // When
        var result = this.hotelService.removeConvenience(hotel.getId(), hotel.getCNPJ(), convenienceId);

        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void testRemoveConvenienceErrorHotelNotFound() {
        // Given
        var convenienceId = 1L;

        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.removeConvenience(hotel.getId(), hotel.getCNPJ(), convenienceId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(hotelRepository, times(0)).removeConvenience(anyLong(), anyLong());
    }

    @Test
    void testDeleteHotelSuccess() {
        // Given
        when(this.hotelRepository.findByIdAndCNPJ(anyLong(), anyString()))
                .thenReturn(Optional.of(hotel));

        doNothing().when(this.hotelRepository).deleteById(anyLong());

        // When
        this.hotelService.delete(hotel.getId(), hotel.getCNPJ());

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


        assertThatThrownBy(() -> this.hotelService.delete(hotel.getId(), hotel.getCNPJ()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(hotelRepository, times(1)).findByIdAndCNPJ(anyLong(), anyString());
        verify(this.hotelRepository, times(0)).deleteById(anyLong());
    }
}
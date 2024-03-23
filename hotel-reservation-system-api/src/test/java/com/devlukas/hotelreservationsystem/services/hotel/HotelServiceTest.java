package com.devlukas.hotelreservationsystem.services.hotel;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.services.exceptions.UniqueIdentifierAlreadyExistsException;
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
        var savedHotel = this.hotelService.save(hotel);

        // Then
        assertThat(savedHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testSaveNewHotelErrorUniqueIdentifierAlreadyExists() {
        // Given
        when(this.hotelRepository.findByCNPJ("80.826.515/0001-84"))
                .thenReturn(Optional.of(hotel));

        // When - Then
        assertThatThrownBy(() -> this.hotelService.save(hotel))
                .isInstanceOf(UniqueIdentifierAlreadyExistsException.class)
                .hasMessage("The CNPJ provided has already been registered in the database");
        verify(hotelRepository, times(1)).findByCNPJ(anyString());
        verify(hotelRepository, times(0)).save(any(Hotel.class));
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
    void testFindByIdSuccess() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        // When
        var foundHotel = this.hotelService.findById(1L);

        // Then
        assertThat(foundHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testFindByIdErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.findById(1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
    }

    @Test
    void testUpdateHotelSuccess() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        when(this.hotelRepository.save(hotel))
                .thenReturn(hotel);

        // When
        var updatedHotel = this.hotelService.update(1L, updateHotel);

        // Then
        assertThat(updatedHotel).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testUpdateHotelErrorHotelNotFound() {
        // Given
        var updateHotel = HotelUtils.generateHotelEntity(address);

        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.hotelService.update(1L, updateHotel))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
        verify(hotelRepository, times(1)).findById(anyLong());
        verify(hotelRepository, times(0)).save(any(Hotel.class));
    }

    @Test
    void testDeleteHotelSuccess() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        doNothing().when(this.hotelRepository).deleteById(anyLong());

        // When
        this.hotelService.delete(1L);

        // Then
        verify(this.hotelRepository, times(1)).findById(anyLong());
        verify(this.hotelRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteHotelErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then


        assertThatThrownBy(() -> this.hotelService.delete(1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id 1");
        verify(hotelRepository, times(1)).findById(anyLong());
        verify(this.hotelRepository, times(0)).deleteById(anyLong());
    }
}
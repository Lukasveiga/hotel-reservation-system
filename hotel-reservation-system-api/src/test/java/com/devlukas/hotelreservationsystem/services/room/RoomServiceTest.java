package com.devlukas.hotelreservationsystem.services.room;


import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.entities.room.Room;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.repositories.RoomRepository;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.utils.HotelUtils;
import com.devlukas.hotelreservationsystem.utils.RoomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RoomServiceTest implements ServiceTestConfig {

    @Mock
    RoomRepository roomRepository;

    @Mock
    HotelRepository hotelRepository;

    @InjectMocks
    RoomService roomService;

    Hotel hotel;

    HotelAddress address;

    Room room;

    @BeforeEach
    void setUp() {
        address = HotelUtils.generateHotelAddress();
        hotel = HotelUtils.generateHotelEntity(address);
        hotel.setId(1L);
        room = RoomUtils.generateRoomEntity();
    }

    @Test
    void testSaveNewRoomSuccess() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.of(hotel));

        when(this.roomRepository.save(room))
                .thenReturn(room);

        // When
        var savedHotel = this.roomService.save(hotel.getId(), room);

        // Then
        assertThat(savedHotel).usingRecursiveAssertion().isEqualTo(room);
        assertThat(savedHotel.getHotel()).usingRecursiveAssertion().isEqualTo(hotel);
    }

    @Test
    void testSaveNewRoomErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.roomService.save(hotel.getId(), room))
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(this.roomRepository, times(0)).save(room);
    }

    @Test
    void testFindAllRoomsSuccess() {
        // Given
        hotel.addRoom(room);

        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        // When
        var rooms = this.roomService.findAll(hotel.getId());

        // Then
        assertThat(rooms.size()).isEqualTo(1);
        assertThat(rooms.get(0)).usingRecursiveAssertion().isEqualTo(room);
    }

    @Test
    void testFindAllRoomsErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.roomService.findAll(hotel.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
        verify(this.roomRepository, times(0)).save(room);
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        hotel.addRoom(room);

        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        when(this.roomRepository.findByIdAndHotelId(hotel.getId(), room.getId()))
                .thenReturn(Optional.of(room));

        // When
        var foundRoom = this.roomService.findById(hotel.getId(), room.getId());

        // Then
        assertThat(foundRoom).usingRecursiveAssertion().isEqualTo(room);
    }

    @Test
    void testFindByIdErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.roomService.findById(hotel.getId(), room.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

    @Test
    void testFindByIdErrorRoomNotFound() {
        // Given
        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        doThrow(new ObjectNotFoundException("Hotel", hotel.getId()))
                .when(this.roomRepository).findByIdAndHotelId(hotel.getId(), room.getId());

        // When - Then
        assertThatThrownBy(() -> this.roomService.findById(hotel.getId(), room.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

    @Test
    void testUpdateRoomSuccess() {
        // Given
        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        when(this.roomRepository.findByIdAndHotelId(hotel.getId(), room.getId()))
                .thenReturn(Optional.of(room));

        when(this.roomRepository.save(room))
                .thenReturn(room);

        // When
        var updatedRoom = this.roomService.update(hotel.getId(), room.getId(), room);

        // Then
        assertThat(updatedRoom).usingRecursiveAssertion().isEqualTo(room);
    }

    @Test
    void testUpdateRoomErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.roomService.update(hotel.getId(), room.getId(), room))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

    @Test
    void testUpdateRoomErrorRoomNotFound() {
        // Given
        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        doThrow(new ObjectNotFoundException("Hotel", hotel.getId()))
                .when(this.roomRepository).findByIdAndHotelId(hotel.getId(), room.getId());

        // When - Then
        assertThatThrownBy(() -> this.roomService.update(hotel.getId(), room.getId(), room))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

    @Test
    void testDeleteRoomSuccess() {
        // Given
        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        when(this.roomRepository.findByIdAndHotelId(hotel.getId(), room.getId()))
                .thenReturn(Optional.of(room));

        doNothing().when(this.roomRepository)
                .delete(room);

        // When
        this.roomService.delete(hotel.getId(), room.getId());

        // Then
        verify(this.hotelRepository, times(1)).findById(hotel.getId());
        verify(this.roomRepository, times(1)).findByIdAndHotelId(hotel.getId(), room.getId());
        verify(this.roomRepository, times(1)).delete(room);
    }

    @Test
    void testDeleteRoomErrorHotelNotFound() {
        // Given
        when(this.hotelRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.roomService.delete(hotel.getId(), room.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

    @Test
    void testDeleteRoomErrorRoomNotFound() {
        // Given
        when(this.hotelRepository.findById(hotel.getId()))
                .thenReturn(Optional.of(hotel));

        doThrow(new ObjectNotFoundException("Hotel", hotel.getId()))
                .when(this.roomRepository).findByIdAndHotelId(hotel.getId(), room.getId());

        // When - Then
        assertThatThrownBy(() -> this.roomService.delete(hotel.getId(), room.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Hotel with id " + hotel.getId());
    }

}
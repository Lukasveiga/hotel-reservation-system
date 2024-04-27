package com.devlukas.hotelreservationsystem.hotel.services.hotelAdmin;

import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.hotel.repositories.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.system.exceptions.UniqueIdentifierAlreadyExistsException;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelAdminUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class HotelAdminServiceTest implements ServiceTestConfig {

    @InjectMocks
    HotelAdminService hotelAdminService;

    @Mock
    HotelAdminRepository hotelAdminRepository;

    @Mock
    PasswordEncoder encoder;

    HotelAdmin hotelAdmin;

    @BeforeEach
    void setUp() {
        hotelAdmin = HotelAdminUtils.generateHotelAdminEntity();
    }

    @Test
    void testSaveHotelAdminAccountSuccess() {
        // Given
        when(this.hotelAdminRepository.save(any(HotelAdmin.class)))
                .thenReturn(hotelAdmin);

        when(this.encoder.encode(anyString()))
                .thenReturn("encodedPassword");

        // When
        var savedHotelAdmin = this.hotelAdminService.save(hotelAdmin);

        // Then
        Assertions.assertThat(savedHotelAdmin.getId()).isEqualTo(hotelAdmin.getId());
        Assertions.assertThat(savedHotelAdmin.getCNPJ()).isEqualTo(hotelAdmin.getCNPJ());
        Assertions.assertThat(savedHotelAdmin.getRoles()).isEqualTo(hotelAdmin.getRoles());
        Assertions.assertThat(savedHotelAdmin.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void testSaveHotelAdminAccountErrorUniqueIdentifierAlreadyExists() {
        // Given
        when(this.hotelAdminRepository.findByCNPJ(anyString()))
                .thenReturn(Optional.of(hotelAdmin));

        // When - then
        Assertions.assertThatThrownBy(() -> this.hotelAdminService.save(hotelAdmin))
                .isInstanceOf(UniqueIdentifierAlreadyExistsException.class)
                .hasMessage("The CNPJ provided has already been registered in the database");
        verify(this.hotelAdminRepository, times(0)).save(hotelAdmin);
    }



}
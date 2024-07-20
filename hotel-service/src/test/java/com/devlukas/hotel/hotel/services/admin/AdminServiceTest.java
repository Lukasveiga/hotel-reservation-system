package com.devlukas.hotel.hotel.services.admin;

import com.devlukas.hotel.ServiceTestConfig;
import com.devlukas.hotel.hotel.entities.admin.Admin;
import com.devlukas.hotel.hotel.repositories.AdminRepository;
import com.devlukas.hotel.system.exceptions.UniqueIdentifierAlreadyExistsException;
import com.devlukas.hotel.hotel.utils.AdminUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;


class AdminServiceTest implements ServiceTestConfig {

    @InjectMocks
    AdminService adminService;

    @Mock
    AdminRepository adminRepository;

    @Mock
    PasswordEncoder encoder;

    Admin admin;

    @BeforeEach
    void setUp() {
        admin = AdminUtils.generateHotelAdminEntity();
    }

    @Test
    void testSaveHotelAdminAccountSuccess() {
        // Given
        when(this.adminRepository.save(any(Admin.class)))
                .thenReturn(admin);

        when(this.encoder.encode(anyString()))
                .thenReturn("encodedPassword");

        // When
        var savedHotelAdmin = this.adminService.save(admin);

        // Then
        Assertions.assertThat(savedHotelAdmin.getId()).isEqualTo(admin.getId());
        Assertions.assertThat(savedHotelAdmin.getCNPJ()).isEqualTo(admin.getCNPJ());
        Assertions.assertThat(savedHotelAdmin.getRoles()).isEqualTo(admin.getRoles());
        Assertions.assertThat(savedHotelAdmin.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void testSaveHotelAdminAccountErrorUniqueIdentifierAlreadyExists() {
        // Given
        when(this.adminRepository.findByCNPJ(anyString()))
                .thenReturn(Optional.of(admin));

        // When - then
        Assertions.assertThatThrownBy(() -> this.adminService.save(admin))
                .isInstanceOf(UniqueIdentifierAlreadyExistsException.class)
                .hasMessage("The CNPJ provided has already been registered");
        verify(this.adminRepository, times(0)).save(admin);
    }



}
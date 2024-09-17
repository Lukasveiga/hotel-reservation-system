package com.devlukas.hotelreservationsystem.usecases.hotelAdmin;

import com.devlukas.hotelreservationsystem.configuration.UsecasesTestConfiguration;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.repository.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CreateHotelAdminTest implements UsecasesTestConfiguration {

    @Mock
    HotelAdminRepository mockRepository;

    @Mock
    PasswordEncoder mockEncoder;

    @InjectMocks
    CreateHotelAdmin sut;

    HotelAdmin hotelAdminTest = new HotelAdmin();

    @BeforeEach
    void setUp() {
        hotelAdminTest.setId(1L);
        hotelAdminTest.setEmail("hotel_admin_test@email.com");
        hotelAdminTest.setPhone("(55)97777-5555");
        hotelAdminTest.setCnpj("72.797.458/0001-24");
        hotelAdminTest.setRoles("admin");
    }

    @Test
    void test_CreateHotelAdmin_Should_Throw_Exception_When_Cnpj_Already_Exists() {
        when(this.mockRepository.findByCnpj(anyString()))
                .thenReturn(Optional.of(hotelAdminTest));

        Assertions.assertThatThrownBy(() -> this.sut.execute(hotelAdminTest))
                .isInstanceOf(UniqueIdentifierAlreadyExists.class)
                .hasMessage("The %s provided has already been registered".formatted("cnpj"));
    }

    @Test
    void test_CreateHotelAdmin_Should_Throw_Exception_When_Email_Already_Exists() {
        when(this.mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(hotelAdminTest));

        Assertions.assertThatThrownBy(() -> this.sut.execute(hotelAdminTest))
                .isInstanceOf(UniqueIdentifierAlreadyExists.class)
                .hasMessage("The %s provided has already been registered".formatted("email"));
    }

    @Test
    void test_CreateHotelAdmin_Success() {
        when(this.mockRepository.save(hotelAdminTest))
                .thenReturn(hotelAdminTest);

        var encodedPassword = "encodedPassword";

        when(this.mockEncoder.encode(hotelAdminTest.getPassword()))
                .thenReturn(encodedPassword);

        var savedHotelAdmin = this.sut.execute(hotelAdminTest);

        Assertions.assertThat(savedHotelAdmin)
                .usingRecursiveAssertion()
                .ignoringFields("password")
                .isEqualTo(hotelAdminTest);
        Assertions.assertThat(savedHotelAdmin.getPassword()).isEqualTo(encodedPassword);
    }

}
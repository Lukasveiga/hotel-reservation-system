package com.devlukas.hotelreservationsystem.hotel.repositories;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.utils.HotelUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    HotelRepository hotelRepository;

    @PersistenceContext
    EntityManager entityManager;

    HotelAddress addressOne;

    HotelAddress addressTwo;

    Hotel hotelOne;

    Hotel hotelTwo;

    @BeforeEach
    void setUp() {
        addressOne = new HotelAddress("Brazil", "Parahyba", "Cuité", "Bairro do Ró",
                "Rua da Já", "12", "520425-70");

        addressTwo = new HotelAddress("Brazil", "São Paulo", "São Paulo", "Bairro do Ró",
                "Rua da Já", "12", "520425-70");

        hotelOne = this.hotelRepository.saveAndFlush(HotelUtils.generateHotelEntity(addressOne));
        hotelTwo = this.hotelRepository.saveAndFlush(HotelUtils.generateHotelEntity(addressTwo));
    }

    @Test
    void testFindByStateSuccess() {
        // Given
        var state = addressOne.getState();

        // When
        var result = this.hotelRepository.findByState(state);

        // Then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0)).usingRecursiveAssertion().isEqualTo(hotelOne);
    }

    @Test
    void testFindByCitySuccess() {
        // Given
        var city = addressOne.getCity();

        // When
        var result = this.hotelRepository.findByCity(city);

        // Then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0)).usingRecursiveAssertion().isEqualTo(hotelOne);
    }

    @Test
    void testAddConvenienceSuccess() {
        // Given
        var hotelId = hotelOne.getId();
        var convenienceDescription = "New convenience";

        // When
        entityManager.clear(); // clear the cache and force hotelRepository.findByIdAndCNPJ from database

        this.hotelRepository.addConvenience(hotelId, convenienceDescription);
        var result = this.hotelRepository.findByIdAndCNPJ(hotelOne.getId(), hotelOne.getCNPJ())
                .orElseGet(Hotel::new);

        // Then
        Assertions.assertThat(result.getConveniences()).hasSize(1);
        Assertions.assertThat(result.getConveniences().stream()
                .filter(c -> c.getDescription().equals(convenienceDescription)).findFirst())
                .isNotEmpty();
    }

    @Test
    void testRemoveConvenienceSuccess() {
        // First Step
        // Given
        var hotelId = hotelOne.getId();
        var convenienceDescription = "New convenience";

        // When
        entityManager.clear(); // clear the cache and force hotelRepository.findByIdAndCNPJ from database

        this.hotelRepository.addConvenience(hotelId, convenienceDescription);
        var result = this.hotelRepository.findByIdAndCNPJ(hotelOne.getId(), hotelOne.getCNPJ())
                .orElseGet(Hotel::new);

        // Then
        Assertions.assertThat(result.getConveniences()).hasSize(1);

        // Second Step
        // Given
        var convenience = result.getConveniences().stream()
                .filter(c -> c.getDescription().equals(convenienceDescription)).findFirst().orElseGet(Convenience::new);
        // When
        entityManager.clear();

        this.hotelRepository.removeConvenience(hotelOne.getId(), convenience.getId());

        var result2 = this.hotelRepository.findByIdAndCNPJ(hotelOne.getId(), hotelOne.getCNPJ())
                .orElseGet(Hotel::new);

        Assertions.assertThat(result2.getConveniences()).hasSize(0);
    }
}
package com.devlukas.hotelreservationsystem.hotel.repositories;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCNPJ(String CNPJ);

    Optional<Hotel> findByIdAndCNPJ(Long id, String CNPJ);

    @Query(value = "SELECT * FROM hotel INNER JOIN hotel_address ON " +
            "hotel_address.id = hotel.address_id WHERE hotel_address.state = ?1", nativeQuery = true)
    List<Hotel> findByState(String state);

    @Query(value = "SELECT * FROM hotel INNER JOIN hotel_address ON " +
            "hotel_address.id = hotel.address_id WHERE hotel_address.city = ?1", nativeQuery = true)
    List<Hotel> findByCity(String city);
}

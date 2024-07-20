package com.devlukas.hotel.hotel.repositories;

import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCNPJ(String CNPJ);

    Optional<Hotel> findByIdAndCNPJ(Long id, String CNPJ);

    @Query(value = "SELECT hotel.* FROM hotel INNER JOIN hotel_address ON " +
            "hotel_address.id = hotel.address_id WHERE LOWER(hotel_address.state) LIKE LOWER(concat('%',?1, '%'))", nativeQuery = true)
    List<Hotel> findByState(String state);

    @Query(value = "SELECT hotel.* FROM hotel INNER JOIN hotel_address ON " +
            "hotel_address.id = hotel.address_id WHERE LOWER(hotel_address.city) LIKE LOWER(concat('%',?1, '%'))", nativeQuery = true)
    List<Hotel> findByCity(String city);

    @Modifying
    @Query(value = "INSERT INTO convenience (description, hotel_id) VALUES (?2, ?1)", nativeQuery = true)
    void addConvenience(Long hotelId, String description);

    @Modifying
    @Query(value = "DELETE FROM convenience WHERE id = ?2 AND hotel_id = ?1", nativeQuery = true)
    int removeConvenience(Long hotelId, Long convenienceId);
}

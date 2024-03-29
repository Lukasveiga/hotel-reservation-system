package com.devlukas.hotelreservationsystem.services.hotel;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.services.exceptions.UniqueIdentifierAlreadyExistsException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository repository;

    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Hotel save(Hotel newHotel) {
        this.repository.findByCNPJ(newHotel.getCNPJ())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExistsException("CNPJ");});
        return this.repository.save(newHotel);
    }

    public List<Hotel> findAll() {
        return this.repository.findAll();
    }

    public Hotel findById(Long hotelId) {
        return this.repository.findById(hotelId)
                .orElseThrow(() -> new ObjectNotFoundException("Hotel", hotelId));
    }

    @Transactional
    public Hotel update(long hotelId, Hotel updateHotel) {
        var oldHotel = this.findById(hotelId);
        oldHotel.setName(updateHotel.getName());
        oldHotel.setEmail(updateHotel.getEmail());
        oldHotel.setPhone(updateHotel.getPhone());
        oldHotel.setAddress(updateHotel.getAddress());
        oldHotel.setDescription(updateHotel.getDescription());
        return this.repository.save(oldHotel);
    }

    @Transactional
    public void delete(long hotelId) {
        this.findById(hotelId);
        this.repository.deleteById(hotelId);
    }
}

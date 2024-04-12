package com.devlukas.hotelreservationsystem.services.hotel;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
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
    public Hotel save(Hotel newHotel, String hotelAdminCNPJ) {
        newHotel.setCNPJ(hotelAdminCNPJ);
        return this.repository.save(newHotel);
    }

    public List<Hotel> findAll() {
        return this.repository.findAll();
    }

    public List<Hotel> findAllByCNPJ(String CNPJ) {
        return this.repository.findByCNPJ(CNPJ);
    }

    public Hotel findByIdAndCNPJ(Long hotelId, String CNPJ) {
        return this.repository.findByIdAndCNPJ(hotelId, CNPJ)
                .orElseThrow(() -> new ObjectNotFoundException("Hotel", hotelId));
    }

    @Transactional
    public Hotel update(long hotelId, String CNPJ, Hotel updateHotel) {
        var oldHotel = this.findByIdAndCNPJ(hotelId, CNPJ);
        oldHotel.setName(updateHotel.getName());
        oldHotel.setEmail(updateHotel.getEmail());
        oldHotel.setPhone(updateHotel.getPhone());
        oldHotel.setAddress(updateHotel.getAddress());
        oldHotel.setDescription(updateHotel.getDescription());
        return this.repository.save(oldHotel);
    }

    @Transactional
    public void delete(long hotelId, String CNPJ) {
        this.findByIdAndCNPJ(hotelId, CNPJ);
        this.repository.deleteById(hotelId);
    }
}

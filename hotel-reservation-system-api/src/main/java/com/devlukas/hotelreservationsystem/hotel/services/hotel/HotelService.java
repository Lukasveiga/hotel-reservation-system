package com.devlukas.hotelreservationsystem.hotel.services.hotel;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.system.exceptions.ObjectNotFoundException;
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
        newHotel.getConveniences().forEach(c -> c.setHotel(newHotel));
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

    public List<Hotel> findByState(String state) {
        return this.repository.findByState(state);
    }

    public List<Hotel> findByCity(String city) {
        return this.repository.findByCity(city);
    }

    @Transactional
    public Hotel updateBasicHotelInfo(long hotelId, String CNPJ, Hotel updateHotel) {
        var oldHotel = this.findByIdAndCNPJ(hotelId, CNPJ);
        oldHotel.setName(updateHotel.getName());
        oldHotel.setEmail(updateHotel.getEmail());
        oldHotel.setPhone(updateHotel.getPhone());
        oldHotel.setAddress(updateHotel.getAddress());
        oldHotel.setDescription(updateHotel.getDescription());
        return this.repository.save(oldHotel);
    }

    @Transactional
    public void addConvenience(long hotelId, String CNPJ, String convenienceDescription) {
        var hotel = this.findByIdAndCNPJ(hotelId, CNPJ);
        this.repository.addConvenience(hotel.getId(), convenienceDescription);
    }

    @Transactional
    public int removeConvenience(Long hotelId, String hotelAdminCNPJ, Long convenienceId) {
        var hotel = this.findByIdAndCNPJ(hotelId, hotelAdminCNPJ);
        return this.repository.removeConvenience(hotel.getId(), convenienceId);

    }

    @Transactional
    public void delete(long hotelId, String CNPJ) {
        this.findByIdAndCNPJ(hotelId, CNPJ);
        this.repository.deleteById(hotelId);
    }
}

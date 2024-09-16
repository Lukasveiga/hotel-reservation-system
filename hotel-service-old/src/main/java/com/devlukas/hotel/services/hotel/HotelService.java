package com.devlukas.hotel.services.hotel;

import com.devlukas.administration.services.LocalAdminService;
import com.devlukas.hotel.entities.hotel.Hotel;
import com.devlukas.hotel.repositories.HotelRepository;
import com.devlukas.system.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository repository;
    private final LocalAdminService localAdminService;

    public HotelService(HotelRepository repository, LocalAdminService localAdminService) {
        this.repository = repository;
        this.localAdminService = localAdminService;
    }

    @Transactional
    public Hotel save(Hotel hotel, Long adminId) {
        var admin = this.localAdminService.findById(adminId);
        hotel.setLocalAdmin(admin);
        return this.repository.save(hotel);
    }

    public List<Hotel> findAll() {
        return this.repository.findAll();
    }

    public List<Hotel> findAllPageable(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return this.repository.findAll(pageable).getContent();
    }

    public Hotel findOne(Long hotelId, Long adminId) {
        return this.repository.findByIdAndLocalAdminId(hotelId, adminId)
                .orElseThrow(() -> new ObjectNotFoundException("Hotel", hotelId));
    }

    public List<Hotel> findByState(String state) {
        return this.repository.findByState(state);
    }

    public List<Hotel> findByCity(String city) {
        return this.repository.findByCity(city);
    }

    @Transactional
    public Hotel updateBasicHotelInfo(long hotelId, Long adminId, Hotel updateHotel) {
        var oldHotel = this.findOne(hotelId, adminId);
        oldHotel.setName(updateHotel.getName());
        oldHotel.setEmail(updateHotel.getEmail());
        oldHotel.setPhone(updateHotel.getPhone());
        oldHotel.setAddress(updateHotel.getAddress());
        oldHotel.setDescription(updateHotel.getDescription());
        return this.repository.save(oldHotel);
    }

    @Transactional
    public void addConvenience(long hotelId, Long adminId, String convenienceDescription) {
        var hotel = this.findOne(hotelId, adminId);
        this.repository.addConvenience(hotel.getId(), convenienceDescription);
    }

    @Transactional
    public int removeConvenience(Long hotelId, Long adminId, Long convenienceId) {
        var hotel = this.findOne(hotelId, adminId);
        return this.repository.removeConvenience(hotel.getId(), convenienceId);
    }

    @Transactional
    public void delete(long hotelId, Long adminId) {
        this.findOne(hotelId, adminId);
        this.repository.deleteById(hotelId);
    }
}

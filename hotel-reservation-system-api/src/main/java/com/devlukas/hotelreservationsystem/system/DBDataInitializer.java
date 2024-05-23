package com.devlukas.hotelreservationsystem.system;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import com.devlukas.hotelreservationsystem.hotel.entities.admin.Admin;
import com.devlukas.hotelreservationsystem.hotel.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.hotel.services.admin.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev", "test"})
public class DBDataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    private final AdminService adminService;

    public DBDataInitializer(HotelRepository hotelRepository, AdminService adminService) {
        this.hotelRepository = hotelRepository;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {

        var hotelAdmin = new Admin();
        hotelAdmin.setId(1L);
        hotelAdmin.setCNPJ("06.596.172/0001-566");
        hotelAdmin.setPassword("test12345");

        adminService.save(hotelAdmin);

        var description = "Um resort de luxo cinco estrelas comandado pelo Conde Drácula, onde os monstros podem se divertir e" +
                "possam descansar do árduo trabalho de perseguir e assustar os humanos.";


        var address = new HotelAddress("Romênia", "Transilvânia", "Brasov", "Bran",
                "Strada General Traian Mosoiu", "222", "985205");

        var hotel = new Hotel("Hotel Transilvânia", "06.596.172/0001-566", "(11)95555-5555",
                "dracula_vamp@bloodmail.com", description, address);

        var conveniences = List.of(
                new Convenience("Quartos Amigáveis para Monstros"),
                new Convenience("Serviços de Disfarce"),
                new Convenience("Refeições Específicas para Criaturas"),
                new Convenience("Spa e Centro de Bem-Estar"),
                new Convenience("Aceitação de Criptomoedas"),
                new Convenience("Conforto Criatural")
        );

        conveniences.forEach(con -> {
            hotel.addConveniences(con);
            con.setHotel(hotel);
        });

        this.hotelRepository.save(hotel);

        var description2 = "O Hotel Beverly Wilshire é um ícone luxuoso situado no coração de Beverly Hills, conhecido " +
                "por sua elegância atemporal e serviço impecável.";


        var address2 = new HotelAddress("Estados Unidos", "Califórnia", "Beverly Hills", "Beverly Grove",
                "Wilshire Boulevard", "9500", "90212");

        var hotel2 = new Hotel("Hotel Beverly Wilshire", "06.596.172/0001-568", "+1 (310) 275-5200",
                "info@beverlywilshire.com", description2, address2);

        var conveniences2 = List.of(
                new Convenience("Spa de luxo com uma variedade de tratamentos rejuvenescedores"),
                new Convenience("Piscina externa aquecida e cabanas privativas"),
                new Convenience("Restaurantes premiados que oferecem uma experiência gastronômica refinada"),
                new Convenience("Academia totalmente equipada com treinadores pessoais disponíveis"),
                new Convenience("Aceitação de Criptomoedas"),
                new Convenience("Serviço de concierge dedicado para atender às necessidades dos hóspedes")
        );

        conveniences2.forEach(con -> {
            hotel2.addConveniences(con);
            con.setHotel(hotel2);
        });

        this.hotelRepository.save(hotel2);
    }
}

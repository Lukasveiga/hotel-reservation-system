package com.devlukas.hotelreservationsystem;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Tag("Controller")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTestConfig {}

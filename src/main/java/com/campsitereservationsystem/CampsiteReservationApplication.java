package com.campsitereservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CampsiteReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampsiteReservationApplication.class, args);
    }

}

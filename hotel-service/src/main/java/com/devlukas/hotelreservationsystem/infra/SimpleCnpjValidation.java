package com.devlukas.hotelreservationsystem.infra;

import com.devlukas.hotelreservationsystem.ports.CnpjValidation;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;

@Component
public class SimpleCnpjValidation implements CnpjValidation {

    @Override
    public boolean validate(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Check if the CNPJ length is 14 digits
        if (cnpj.length() != 14) {
            return false;
        }

        // Check if CNPJ consists of repeated numbers
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // First validation digit
            char dig13 = calculateVerificationDigit(cnpj, 12);
            // Second validation digit
            char dig14 = calculateVerificationDigit(cnpj, 13);

            // Compare calculated digits with the input
            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));

        } catch (InputMismatchException e) {
            return false;
        }
    }

    private static char calculateVerificationDigit(String cnpj, int length) {
        int[] weight = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;

        // Calculate sum of products of digits and their respective weights
        for (int i = 0; i < length; i++) {
            sum += (cnpj.charAt(i) - '0') * weight[i + (weight.length - length)];
        }

        // Calculate the remainder of the sum divided by 11
        int remainder = sum % 11;
        return (remainder < 2) ? '0' : (char) ((11 - remainder) + '0');
    }
}

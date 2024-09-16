package com.devlukas.administration.controllers.chainAdmin.converter;

import com.devlukas.administration.controllers.chainAdmin.dto.ChainAdminRequestBody;
import com.devlukas.administration.entities.ChainAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChainAdminRequestToEntity implements Converter<ChainAdminRequestBody, ChainAdmin> {

    @Override
    public ChainAdmin convert(ChainAdminRequestBody source) {
        var chainAdmin = new ChainAdmin();
        chainAdmin.setCNPJ(source.CNPJ());
        chainAdmin.setEmail(source.email());
        chainAdmin.setPassword(source.password());
        return chainAdmin;
    }
}

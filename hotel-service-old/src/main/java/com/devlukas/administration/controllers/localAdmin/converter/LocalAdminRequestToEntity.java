package com.devlukas.administration.controllers.localAdmin.converter;

import com.devlukas.administration.controllers.localAdmin.dto.LocalAdminRequestBody;
import com.devlukas.administration.entities.LocalAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocalAdminRequestToEntity implements Converter<LocalAdminRequestBody, LocalAdmin> {

    @Override
    public LocalAdmin convert(LocalAdminRequestBody source) {
        var localAdmin = new LocalAdmin();
        localAdmin.setEmail(source.email());
        localAdmin.setPhone(source.phone());
        localAdmin.setPassword(source.password());
        return localAdmin;
    }
}

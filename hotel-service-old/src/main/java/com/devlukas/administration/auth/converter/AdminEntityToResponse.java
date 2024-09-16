package com.devlukas.administration.auth.converter;

import com.devlukas.administration.entities.Admin;
import com.devlukas.administration.auth.dto.AdminResponseBody;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdminEntityToResponse implements Converter<Admin, AdminResponseBody> {
    @Override
    public AdminResponseBody convert(Admin source) {
        return new AdminResponseBody(source.getId(), source.getCNPJ(), source.getRoles());
    }
}

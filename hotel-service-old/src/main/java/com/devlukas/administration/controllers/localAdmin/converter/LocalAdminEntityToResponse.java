package com.devlukas.administration.controllers.localAdmin.converter;

import com.devlukas.administration.controllers.localAdmin.dto.LocalAdminResponseBody;
import com.devlukas.administration.entities.LocalAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocalAdminEntityToResponse implements Converter<LocalAdmin, LocalAdminResponseBody> {

    @Override
    public LocalAdminResponseBody convert(LocalAdmin source) {
        return new LocalAdminResponseBody(
                source.getId(),
                source.getCNPJ(),
                source.getEmail(),
                source.getPhone(),
                source.getHotel().getId(),
                source.getRoles()
        );
    }
}

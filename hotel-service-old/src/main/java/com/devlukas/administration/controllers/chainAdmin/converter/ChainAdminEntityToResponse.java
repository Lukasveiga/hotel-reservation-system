package com.devlukas.administration.controllers.chainAdmin.converter;

import com.devlukas.administration.controllers.chainAdmin.dto.ChainAdminResponseBody;
import com.devlukas.administration.entities.Admin;
import com.devlukas.administration.entities.ChainAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChainAdminEntityToResponse implements Converter<ChainAdmin, ChainAdminResponseBody> {

    @Override
    public ChainAdminResponseBody convert(ChainAdmin source) {
        return new ChainAdminResponseBody(
                source.getId(),
                source.getCNPJ(),
                source.getEmail(),
                source.getPhone(),
                source.getLocalAdmins().stream().map(Admin::getId).toList(),
                source.getRoles()
        );
    }
}

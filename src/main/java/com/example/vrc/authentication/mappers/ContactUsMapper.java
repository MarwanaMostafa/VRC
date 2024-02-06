package com.example.vrc.authentication.mappers;

import com.example.vrc.authentication.DTOs.ContactUsDTO;
import com.example.vrc.authentication.models.ContactUs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactUsMapper {
    ContactUs toEntity(ContactUsDTO contactUsDTO);
}

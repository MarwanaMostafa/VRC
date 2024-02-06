package com.example.vrc.authentication.services.impl;

import com.example.vrc.authentication.DTOs.ContactUsDTO;
import com.example.vrc.authentication.mappers.ContactUsMapper;
import com.example.vrc.authentication.repositories.ContactUsRepository;
import com.example.vrc.authentication.services.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;
    @Autowired
    private ContactUsMapper contactUsMapper;
    @Override
    public String createComplain(ContactUsDTO contactUsDTO) throws ResponseStatusException {

        this.contactUsRepository.save(this.contactUsMapper.toEntity(contactUsDTO));
        return "Complaint created successfully.";
    }
}

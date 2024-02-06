package com.example.vrc.authentication.services;

import com.example.vrc.authentication.DTOs.ContactUsDTO;
import org.springframework.web.server.ResponseStatusException;

public interface ContactUsService {
    String createComplain(ContactUsDTO contactUsDTO)throws ResponseStatusException;
}

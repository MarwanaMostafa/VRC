package com.example.vrc.authentication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Complaints")
public class ContactUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComplaint")
    private Long numberOfComplaint;

    @Column(name = "firstName",nullable = false)
    private String firstName;
    @Column(name = "secondName",nullable = false)
    private String secondName;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "complaint",nullable = false)
    private String complaint;

}

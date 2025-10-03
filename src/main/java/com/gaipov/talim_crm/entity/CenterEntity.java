package com.gaipov.talim_crm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "centers")
@Data
public class CenterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
}

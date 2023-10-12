package com.example.dashboard.entity;

import com.example.dashboard.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "role_id")
    private int id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name= "role_description")
    private String roleDesc;
}


package com.example.dashboard.entity;

import com.example.dashboard.enums.ActiveStatus;
import lombok.Data;

@Data
public class ClientUserDTO {
    private String username;
    private ActiveStatus activeStatus;
}

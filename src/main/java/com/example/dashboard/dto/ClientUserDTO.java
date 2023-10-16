package com.example.dashboard.dto;

import com.example.dashboard.enums.ActiveStatus;
import lombok.Data;

@Data
public class ClientUserDTO {
    private String username;
    private ActiveStatus activeStatus;
}

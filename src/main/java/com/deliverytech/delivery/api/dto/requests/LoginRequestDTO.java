package com.deliverytech.delivery.api.dto.requests;

import com.deliverytech.delivery.api.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
    private String email;
    private String senha;
    private Role role;
}

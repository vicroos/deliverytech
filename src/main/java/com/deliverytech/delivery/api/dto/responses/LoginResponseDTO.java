package com.deliverytech.delivery.api.dto.responses;

import com.deliverytech.delivery.api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String senha;
    private Role role;
    
}

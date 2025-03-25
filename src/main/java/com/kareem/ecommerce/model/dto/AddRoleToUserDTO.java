package com.kareem.ecommerce.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddRoleToUserDTO {
    private Long userId;
    private Long roleId;
}

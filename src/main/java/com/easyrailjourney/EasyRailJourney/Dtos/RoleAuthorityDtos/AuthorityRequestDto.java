package com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthorityRequestDto {


    @NotNull (message="Authoiry name is required")
    private String name;
}
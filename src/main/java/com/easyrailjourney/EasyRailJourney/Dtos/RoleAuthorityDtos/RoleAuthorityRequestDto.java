package com.easyrailjourney.EasyRailJourney.Dtos.RoleAuthorityDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleAuthorityRequestDto {

 
    @NotNull(message="role id is missing for Assigning Authority")
    private Long roleId;

    @NotNull(message="authority id is missing for Assignng to  Role")
    private Long authorityId;
}
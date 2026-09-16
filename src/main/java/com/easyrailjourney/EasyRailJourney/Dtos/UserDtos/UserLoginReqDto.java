package com.easyrailjourney.EasyRailJourney.Dtos.UserDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginReqDto {

    @NotBlank (message="profileName required")
    String profileName;

    @NotBlank (message="password required")
    String password;

}

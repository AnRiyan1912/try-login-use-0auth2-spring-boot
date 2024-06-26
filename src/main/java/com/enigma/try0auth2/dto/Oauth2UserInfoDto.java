package com.enigma.try0auth2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Oauth2UserInfoDto {
    private String id;
    private String name;
    private String email;
    private String picture;
}

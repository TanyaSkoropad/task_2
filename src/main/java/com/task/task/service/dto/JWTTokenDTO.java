package com.task.task.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@Builder
public class JWTTokenDTO extends UserDto{

    @JsonProperty("jwt_token")
    private String jwtToken;

    public JWTTokenDTO(String jwtToken, UserDto userDto) {
        super(userDto);
        this.jwtToken = jwtToken;
    }
}

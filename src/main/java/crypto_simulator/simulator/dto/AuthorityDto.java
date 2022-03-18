package crypto_simulator.simulator.dto;

import lombok.*;

// original : https://github.com/SilverNine/spring-boot-jwt-tutorial

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {
    private String authorityName;
}
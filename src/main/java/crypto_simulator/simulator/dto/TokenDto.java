package crypto_simulator.simulator.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// original : https://github.com/SilverNine/spring-boot-jwt-tutorial
public class TokenDto {
    private String token;
}

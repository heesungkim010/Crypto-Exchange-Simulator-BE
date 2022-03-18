package crypto_simulator.simulator.dto;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// original : https://github.com/SilverNine/spring-boot-jwt-tutorial
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    //@Size(min = 3, max = 50)
    private String username;

    @NotNull
    //@Size(min = 3, max = 100)
    private String password;
}

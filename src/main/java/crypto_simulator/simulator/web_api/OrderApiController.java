package crypto_simulator.simulator.web_api;

import crypto_simulator.simulator.AppConfig;
import crypto_simulator.simulator.domain.InitialSettings;
import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderApiController {

    private final AppConfig appConfig;

    @PostMapping("/api/neworder")
    public void ex( ){ // request received
        //START HERE
    }

}

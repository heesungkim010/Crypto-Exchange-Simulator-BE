package crypto_simulator.simulator.web;

import crypto_simulator.simulator.service.FilledOrderService;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilledOrderApiController {
    private final FilledOrderService filledOrderService;
    private final MemberService memberService;
    private final PositionService positionService;


}

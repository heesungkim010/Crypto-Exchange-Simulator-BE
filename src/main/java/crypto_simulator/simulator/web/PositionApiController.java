package crypto_simulator.simulator.web;

import crypto_simulator.simulator.service.FilledOrderService;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import crypto_simulator.simulator.web.login.SessionConst;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PositionApiController {
    private final FilledOrderService filledOrderService;
    private final MemberService memberService;
    private final PositionService positionService;


    @GetMapping("/api/position/{ticker}") // check member with session
    public positionTickerResponse getPositionTicker(@PathVariable String ticker,
                      HttpServletRequest request){
        HttpSession session = request.getSession(false);
        log.info("{}", session);
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            return new positionTickerResponse(false);
        }else{
            return new positionTickerResponse(true);
        }
    }

    @Data
    class positionTickerResponse{
        private boolean hasSession;

        public positionTickerResponse(boolean hasSession) {
            this.hasSession = hasSession;
        }
    }
}

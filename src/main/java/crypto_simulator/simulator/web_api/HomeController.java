package crypto_simulator.simulator.web_api;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final PositionService positionService;

    @GetMapping("/")
    public CreateMemberResponse home(@RequestBody CreateMemberRequest request){ // request received
        log.info("home");
        Member member = new Member();
        member.setUserId(request.getName());

        //when
        Long savedId = memberService.join(member);
        positionService.initiatePosition(member);

        CreateMemberResponse response = new CreateMemberResponse(member.getId()); //response made
        return response;
    }

    @Data
    static class CreateMemberRequest{
        private String name;
    }

    @Data
    class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
}
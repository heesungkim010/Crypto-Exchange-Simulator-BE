package crypto_simulator.simulator.web_api;

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
public class MemberApiController {
    private final MemberService memberService;
    private final PositionService positionService;

    @PostMapping("/api/members")
    public CreateMemberResponse registerUser(@RequestBody CreateMemberRequest request){ // request received

        //Create New Member
        // 1. create new member with request
        // 2. join the new member(memberService)
        // 3. initiate Positions(positionService)
        //TODO : deal with the exceptions when duplicated memberId
        Member member = new Member(request.userId, request.password,
                InitialSettings.initialBalanceUsd, InitialSettings.initialOrderedUsd);
        Long savedId = memberService.join(member);
        positionService.initiatePosition(member);

        CreateMemberResponse response = new CreateMemberResponse(
                member.getUserId(), true);
        //response == { memberId , registrationResult }
        return response;
    }

    @Data
    static class CreateMemberRequest{
        private String userId;
        private String password;
    }

    @Data
    class CreateMemberResponse{
        private String memberId;
        private boolean registrationResult;
        public CreateMemberResponse(String id, boolean didRegister){
            this.memberId = id;
            this.registrationResult = didRegister;
        }
    }
}
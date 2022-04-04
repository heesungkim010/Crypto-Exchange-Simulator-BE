package crypto_simulator.simulator.web;

import crypto_simulator.simulator.domain.InitialSettings;
import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final PositionService positionService;

    @GetMapping("/api/members/prac")
    public CreateMemberResponse prac(){
        log.info("prac");
        CreateMemberResponse response = new CreateMemberResponse(
                "abcde", true);

        //response == { memberId , registrationResult }
        return response;
    }

    @PostMapping("/api/members/register")
    public CreateMemberResponse registerMember(@RequestBody CreateMemberRequest request) throws NoSuchAlgorithmException { // request received

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

    @PostMapping("/api/members/login")
    public LoginMemberResponse loginMember(@RequestBody LoginMemberRequest request,
                                           HttpServletRequest servletRequest) throws NoSuchAlgorithmException { // request received

        // Login Member
        // 1. check if id-pw matches
        //    if matches : login success, set session.
        //    else: login fail. re-try
        //TODO : deal with the exceptions when duplicated memberId
        Member loginMember = memberService.login(request.getUserId(), request.getPassword());

        if (loginMember == null){ // login fail. re-try
            return new LoginMemberResponse(false); // did_login : false
        }

        //if matches : login success, set session.
        HttpSession session = servletRequest.getSession(true);
        // if no session , return new session
        // (false) : if no session, return null
        session.setAttribute("loginmember", loginMember);
        //save session-data

        return new LoginMemberResponse(true); //did_login : true
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

    @Data
    static class LoginMemberRequest{
        private String userId;
        private String password;
    }

    @Data
    class LoginMemberResponse{
        private boolean didLogin;
        public LoginMemberResponse(boolean didLogin){
            this.didLogin = didLogin;
        }
    }
}
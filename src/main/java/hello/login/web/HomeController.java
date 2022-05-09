package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        //로그인 한 사용자
        Member loginMember = memberRepository.findById(memberId);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보 조회
        Object sessionMember = (Member)sessionManager.getSession(request);

        //로그인
        if (sessionMember == null) {
            return "home";
        }

        model.addAttribute("member", sessionMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //세션 조회
        HttpSession session = request.getSession(false);

        //세션이 없으면 home
        if (session == null) {
            return "home";
        }

        //세션이 유지되먼 loginHome
        model.addAttribute("member", session.getAttribute(SessionConst.LOGIN_MEMBER));
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {

        //세션이 없으면 home
        if (member == null) {
            return "home";
        }

        //세션이 유지되먼 loginHome
        model.addAttribute("member", member);
        return "loginHome";
    }
}
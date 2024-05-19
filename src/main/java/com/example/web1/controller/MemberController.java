package com.example.web1.controller;

import com.example.web1.DTO.MemberDTO;
import com.example.web1.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;
  // 회원가입 페이지 출력 요청
  @GetMapping("/member/save")
  public String saveForm() {
    return "save";
  }

  @PostMapping("/member/save")    // name값을 requestparam에 담아온다
  public String save(@ModelAttribute MemberDTO memberDTO) {
    System.out.println("MemberController.save");
    System.out.println("memberDTO = " + memberDTO);
    memberService.save(memberDTO);

    return "index";
  }
  @GetMapping("/member/login")
  public String loginForm(){
    return "index";
  }
  @PostMapping("/member/login") // session : 로그인 유지
  public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
    MemberDTO loginResult = memberService.login(memberDTO);
    if (loginResult != null) {
      // login 성공
      session.setAttribute("loginEmail", loginResult.getMemberEmail());
      return "menu";
    } else {
      // login 실패
      return "index";
    }
  }
}

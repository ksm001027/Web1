package com.example.web1.controller;

import com.example.web1.DTO.MemberDTO;
import com.example.web1.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  // 회원가입 페이지 출력 요청
  @GetMapping("/member/save")
  public String saveForm() {
    return "save";
  }

  @PostMapping("/member/save")
  public String save(@ModelAttribute MemberDTO memberDTO) {
    System.out.println("MemberController.save");
    System.out.println("memberDTO = " + memberDTO);
    memberService.save(memberDTO);
    return "index";
  }

  @GetMapping("/member/login")
  public String loginForm() {
    return "index";
  }

  @PostMapping("/member/login")
  public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
    MemberDTO loginResult = memberService.login(memberDTO);
    if (loginResult != null) {
      // login 성공
      session.setAttribute("loginEmail", loginResult.getMemberEmail());
      return "menu";
    } else {
      // login 실패
      model.addAttribute("loginError", "ID나 비밀번호가 틀렸습니다.");
      return "index";
    }
  }
}

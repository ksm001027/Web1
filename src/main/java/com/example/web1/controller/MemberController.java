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

  @GetMapping("/member/save")
  public String saveForm() {
    return "save";
  }

  @PostMapping("/member/save")
  public String save(@ModelAttribute MemberDTO memberDTO) {
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
      session.setAttribute("loginEmail", loginResult.getMemberEmail());
      session.setAttribute("memberId", loginResult.getId()); // 로그인한 사용자의 ID를 세션에 저장
      System.out.println("Logged in with memberId: " + loginResult.getId()); // 로그 추가
      return "menu";
    } else {
      model.addAttribute("loginError", "ID나 비밀번호가 틀렸습니다.");
      return "index";
    }
  }
}

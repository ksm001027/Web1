package com.example.web1.service;

import com.example.web1.DTO.MemberDTO;
import com.example.web1.model.MemberEntity;
import com.example.web1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;

  public void save(MemberDTO memberDTO) {
    MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
    memberRepository.save(memberEntity);
  }

  public MemberDTO login(MemberDTO memberDTO) {
    MemberEntity memberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail())
      .orElse(null);
    if (memberEntity != null && memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
      return MemberDTO.toMemberDTO(memberEntity);
    }
    return null;
  }
}

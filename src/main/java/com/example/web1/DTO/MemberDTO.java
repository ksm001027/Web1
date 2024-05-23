package com.example.web1.DTO;

import com.example.web1.model.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
  private Long id;
  private String memberEmail;
  private String memberPassword;
  private String memberName;

  public static MemberDTO toMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getMemberId());
    memberDTO.setMemberEmail(memberEntity.getMemberEmail());
    memberDTO.setMemberName(memberEntity.getMemberName());
    memberDTO.setMemberPassword(memberEntity.getMemberPassword());
    return memberDTO;
  }
}

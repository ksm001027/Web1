package com.example.web1.model;

import com.example.web1.DTO.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "member_table") //database에 해당 이름의 테이블 생성
public class MemberEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
  @Column(name = "member_id")
  private Long memberId;

  @Column(unique = true)
  private String memberEmail;

  @Column
  private String memberPassword;

  @Column
  private String memberName;

  public static MemberEntity toMemberEntity(MemberDTO memberDTO){
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setMemberId(memberDTO.getId());
    memberEntity.setMemberEmail(memberDTO.getMemberEmail());
    memberEntity.setMemberName(memberDTO.getMemberName());
    memberEntity.setMemberPassword(memberDTO.getMemberPassword());
    return memberEntity;
  }
}

package com.example.web1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "file_table")
public class FileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String filename;

  @Column
  private String filepath;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  // 기타 필요한 메서드들
}

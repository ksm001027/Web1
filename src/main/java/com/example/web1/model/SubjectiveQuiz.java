package com.example.web1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subjective_quiz")
public class SubjectiveQuiz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private String answer; // 주관식 퀴즈 정답

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  public SubjectiveQuiz() {
  }

  public SubjectiveQuiz(String title, String question, String answer, MemberEntity member) {
    this.title = title;
    this.question = question;
    this.answer = answer;
    this.member = member;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public MemberEntity getMember() {
    return member;
  }

  public void setMember(MemberEntity member) {
    this.member = member;
  }
}

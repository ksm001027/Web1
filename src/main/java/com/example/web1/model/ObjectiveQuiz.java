package com.example.web1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "objective_quiz")
public class ObjectiveQuiz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quiz_title", nullable = false)
  private String title;

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private String option1;

  @Column(nullable = false)
  private String option2;

  @Column(nullable = false)
  private String option3;

  @Column(nullable = false)
  private String option4;

  @Column(name = "correct_answer", nullable = false)
  private int correctAnswerIndex;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  public ObjectiveQuiz() {
  }

  public ObjectiveQuiz(String title, String question, String option1, String option2, String option3, String option4, int correctAnswerIndex, MemberEntity member) {
    this.title = title;
    this.question = question;
    this.option1 = option1;
    this.option2 = option2;
    this.option3 = option3;
    this.option4 = option4;
    this.correctAnswerIndex = correctAnswerIndex;
    this.member = member;
  }

  // Getters and setters
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

  public String getOption1() {
    return option1;
  }

  public void setOption1(String option1) {
    this.option1 = option1;
  }

  public String getOption2() {
    return option2;
  }

  public void setOption2(String option2) {
    this.option2 = option2;
  }

  public String getOption3() {
    return option3;
  }

  public void setOption3(String option3) {
    this.option3 = option3;
  }

  public String getOption4() {
    return option4;
  }

  public void setOption4(String option4) {
    this.option4 = option4;
  }

  public int getCorrectAnswerIndex() {
    return correctAnswerIndex;
  }

  public void setCorrectAnswerIndex(int correctAnswerIndex) {
    this.correctAnswerIndex = correctAnswerIndex;
  }

  public MemberEntity getMember() {
    return member;
  }

  public void setMember(MemberEntity member) {
    this.member = member;
  }
}

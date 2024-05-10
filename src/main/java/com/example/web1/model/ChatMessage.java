package com.example.web1.model;

public class ChatMessage {
  private String content;
  private String sender;
  private MessageType type; // CHAT, JOIN, LEAVE 등의 메시지 타입 구분

  // 메시지 타입을 열거형으로 정의
  public enum MessageType {
    CHAT,
    JOIN,
    LEAVE
  }

  // 기본 생성자
  public ChatMessage() {
  }

  // 모든 필드를 포함한 생성자
  public ChatMessage(String content, String sender, MessageType type) {
    this.content = content;
    this.sender = sender;
    this.type = type;
  }

  // Getter와 Setter 메소드
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }
}

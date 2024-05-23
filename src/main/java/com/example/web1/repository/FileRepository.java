package com.example.web1.repository;

import com.example.web1.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
  Optional<FileEntity> findByFilename(String filename);
  List<FileEntity> findByMember_MemberId(Long memberId); // 특정 회원의 파일 목록 조회
}

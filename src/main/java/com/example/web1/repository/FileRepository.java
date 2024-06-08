package com.example.web1.repository;

import com.example.web1.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
  List<FileEntity> findByMember_MemberId(Long memberId);

  Optional<FileEntity> findByFilename(String filename);

  Optional<FileEntity> findByFilenameAndMember_MemberId(String filename, Long memberId);

  Optional<FileEntity> findByIdAndMember_MemberId(Long id, Long memberId);
}

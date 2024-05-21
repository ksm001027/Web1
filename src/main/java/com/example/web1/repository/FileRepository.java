package com.example.web1.repository;

import com.example.web1.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
  FileEntity findByFilename(String filename);
}

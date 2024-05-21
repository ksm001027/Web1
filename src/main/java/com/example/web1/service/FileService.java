package com.example.web1.service;

import com.example.web1.model.FileEntity;
import com.example.web1.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

  @Value("${upload.path}")
  private String uploadPath;

  private final FileRepository fileRepository;

  private List<FileEntity> uploadedFiles = new ArrayList<>();

  public FileService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public void saveFile(MultipartFile file) throws Exception {
    String filename = file.getOriginalFilename();
    Path path = Paths.get(uploadPath, filename);

    Files.copy(file.getInputStream(), path);

    FileEntity fileEntity = new FileEntity();
    fileEntity.setFilename(filename);
    fileEntity.setFilepath(path.toString());
    uploadedFiles.add(fileEntity);
    fileRepository.save(fileEntity);
  }

  public List<FileEntity> getAllFiles() {
    return new ArrayList<>(uploadedFiles);
  }

  public Path getFilePath(String filename) {
    FileEntity fileEntity = uploadedFiles.stream()
      .filter(file -> file.getFilename().equals(filename))
      .findFirst()
      .orElse(null);

    if (fileEntity != null) {
      return Paths.get(fileEntity.getFilepath());
    } else {
      throw new RuntimeException("파일을 찾을 수 없습니다.: " + filename);
    }
  }

  public void clearUploadedFiles() {
    uploadedFiles.clear();
  }
}

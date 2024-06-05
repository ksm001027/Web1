package com.example.web1.service;

import com.example.web1.model.FileEntity;
import com.example.web1.model.MemberEntity;
import com.example.web1.repository.FileRepository;
import com.example.web1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FileService {

  @Value("${upload.path}")
  private String uploadPath;

  private final FileRepository fileRepository;
  private final MemberRepository memberRepository;

  // 임시 세션 ID 저장소 (예시: 간단한 ConcurrentHashMap 사용)
  private final ConcurrentHashMap<String, Long> temporarySessions = new ConcurrentHashMap<>();

  public void saveFile(MultipartFile file, Long memberId) throws Exception {
    System.out.println("Saving file for memberId: " + memberId); // 로그 추가
    String filename = file.getOriginalFilename();
    Path path = Paths.get(uploadPath, filename);

    Files.copy(file.getInputStream(), path);

    MemberEntity member = memberRepository.findById(memberId)
      .orElseThrow(() -> new RuntimeException("Member not found"));

    FileEntity fileEntity = new FileEntity();
    fileEntity.setFilename(filename);
    fileEntity.setFilepath(path.toString());
    fileEntity.setMember(member);

    fileRepository.save(fileEntity);
  }

  public List<FileEntity> getAllFiles() {
    return fileRepository.findAll();
  }

  public List<FileEntity> getFilesByMemberId(Long memberId) {
    return fileRepository.findByMember_MemberId(memberId);
  }

  public Path getFilePath(String filename) {
    FileEntity fileEntity = fileRepository.findByFilename(filename)
      .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다.: " + filename));
    return Paths.get(fileEntity.getFilepath());
  }

  public Path getFilePathForMember(String filename, Long memberId) {
    FileEntity fileEntity = fileRepository.findByFilenameAndMember_MemberId(filename, memberId)
      .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다.: " + filename));
    return Paths.get(fileEntity.getFilepath());
  }

  public void deleteFile(Long fileId, Long memberId) throws Exception {
    FileEntity fileEntity = fileRepository.findById(fileId)
      .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다.: " + fileId));

    if (!fileEntity.getMember().getMemberId().equals(memberId)) {
      throw new RuntimeException("해당 파일을 삭제할 권한이 없습니다.");
    }

    Path path = Paths.get(fileEntity.getFilepath());
    Files.deleteIfExists(path);

    fileRepository.delete(fileEntity);
  }

  public String createTemporarySession(Long memberId) {
    String tempSessionId = UUID.randomUUID().toString();
    temporarySessions.put(tempSessionId, memberId);
    return tempSessionId;
  }

  public Long validateTemporarySession(String tempSessionId) {
    return temporarySessions.get(tempSessionId);
  }
}

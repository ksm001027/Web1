package com.example.web1.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {

  @Value("${upload.path}")
  private String uploadPath;

<<<<<<< HEAD
  @GetMapping("/upload")
  public String showUploadForm(HttpSession session) {
    session.setAttribute("files", new ArrayList<String>()); // 파일 목록 초기화
    return "uploadForm"; // uploadForm.html 반환
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
    try {
      Path path = Paths.get(uploadPath + file.getOriginalFilename());
      Files.copy(file.getInputStream(), path);

      List<String> files = (List<String>) session.getAttribute("files");
      if (files == null) {
        files = new ArrayList<>();
        session.setAttribute("files", files);
      }
      files.add(file.getOriginalFilename());
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 성공하였습니다!: " + file.getOriginalFilename());
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패하였습니다.: " + e.getMessage());
=======
  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes, HttpSession session) {
    List<String> uploadedFiles = (List<String>) session.getAttribute("files");
    if (uploadedFiles == null) {
      uploadedFiles = new ArrayList<>();
      session.setAttribute("files", uploadedFiles);
>>>>>>> 8161ea90530f3c65cdfb35b8ed7111d94e2ffc04
    }

    for (MultipartFile file : files) {
      if (file.isEmpty()) continue; // 비어 있는 파일은 무시

      // 파일명 중복 처리
      String originalFilename = file.getOriginalFilename();
      String filename = originalFilename;
      Path path = Paths.get(uploadPath + filename);
      int count = 0;

      // 같은 이름의 파일이 존재하면, 이름 변경
      while (Files.exists(path)) {
        count++;
        filename = originalFilename.replaceAll("(?i)(\\.\\w+$)", "_" + count + "$1");
        path = Paths.get(uploadPath + filename);
      }

      try {
        Files.copy(file.getInputStream(), path);
        uploadedFiles.add(filename);
        redirectAttributes.addFlashAttribute("message", "파일 업로드에 성공하였습니다!: " + filename);
      } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패하였습니다.: " + e.getMessage());
      }
    }
    return "redirect:/";
  }

  @GetMapping("/downloads")
  public String showFiles(Model model, HttpSession session) {
    List<String> files = (List<String>) session.getAttribute("files");
    model.addAttribute("files", files);
    return "download"; // 다운로드 페이지 뷰 반환
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
    try {
      Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (!resource.exists() || !resource.isReadable()) {
        throw new RuntimeException("파일을 읽을 수 없습니다.: " + filename);
      }
      String mimeType = Files.probeContentType(filePath);
      if (mimeType == null) {
        mimeType = "application/octet-stream";
      }
      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(mimeType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    } catch (Exception e) {
      throw new RuntimeException("오류: " + e.getMessage());
    }
  }

  @GetMapping("/uploadForm")
  public String showUploadForm() {
    return "uploadForm";  // Thymeleaf가 처리할 수 있도록 뷰 이름 반환
  }
}

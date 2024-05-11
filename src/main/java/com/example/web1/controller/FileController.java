package com.example.web1.controller;

import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping("/")
  public String index() {
    return "uploadForm";  // Thymeleaf 템플릿 이름 반환
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    try {
      Path path = Paths.get(uploadPath + file.getOriginalFilename());
      Files.copy(file.getInputStream(), path);
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 성공하였습니다!: " + file.getOriginalFilename());
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패하였습니다.: " + e.getMessage());
    }
    return "redirect:/";
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename, HttpServletResponse response) {
    try {
      Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (!resource.exists() || !resource.isReadable()) {
        throw new RuntimeException("파일을 읽을 수 없습니다.: " + filename);
      }

      // 파일 MIME 타입 추정
      String mimeType = Files.probeContentType(filePath);
      if (mimeType == null) {
        mimeType = "application/octet-stream"; // 기본값
      }

      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(mimeType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    } catch (Exception e) {
      throw new RuntimeException("오류: " + e.getMessage());
    }
  }



  @GetMapping("/downloads")
  public String showFiles(Model model) {
    List<String> fileList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(uploadPath))) {
      for (Path entry : stream) {
        fileList.add(entry.getFileName().toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    model.addAttribute("files", fileList);
    return "download";
  }


  // 파일 목록을 불러오는 메서드 추가
  @GetMapping("/files")
  public String listUploadedFiles(Model model) {
    List<String> fileList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(uploadPath))) {
      for (Path entry : stream) {
        fileList.add(entry.getFileName().toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    model.addAttribute("files", fileList);
    return "uploadForm";
  }
}

package com.example.web1.controller;

import com.example.web1.model.FileEntity;
import com.example.web1.service.FileService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {

  @Value("${app.server.address}")
  private String serverAddress;

  private final FileService fileService;

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("files") MultipartFile[] files, HttpSession session, RedirectAttributes redirectAttributes) {
    Long memberId = (Long) session.getAttribute("memberId");
    System.out.println("Uploading files for memberId: " + memberId);
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    try {
      for (MultipartFile file : files) {
        fileService.saveFile(file, memberId);
      }
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 성공하였습니다!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패하였습니다.: " + e.getMessage());
    }

    return "redirect:/uploadForm";
  }

  @GetMapping("/downloads")
  public String showFiles(HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    List<FileEntity> files = fileService.getFilesByMemberId(memberId);
    model.addAttribute("files", files);
    model.addAttribute("memberId", memberId);
    model.addAttribute("serverAddress", serverAddress);  // serverAddress 변수를 모델에 추가
    return "download";
  }


  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename, HttpSession session) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      return ResponseEntity.status(401).body(null); // Unauthorized
    }

    try {
      Path filePath = fileService.getFilePath(filename);
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
  public String showUploadForm(HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    List<FileEntity> files = fileService.getFilesByMemberId(memberId);
    model.addAttribute("files", files);
    model.addAttribute("memberId", memberId);
    return "uploadForm";
  }

  @PostMapping("/delete")
  public String deleteFile(@RequestParam("fileId") Long fileId, HttpSession session, RedirectAttributes redirectAttributes) {
    Long memberId = (Long) session.getAttribute("memberId");
    System.out.println("Deleting file for memberId: " + memberId);
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    try {
      fileService.deleteFile(fileId, memberId);
      redirectAttributes.addFlashAttribute("message", "파일 삭제에 성공하였습니다!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "파일 삭제에 실패하였습니다.: " + e.getMessage());
    }

    return "redirect:/uploadForm";
  }
}

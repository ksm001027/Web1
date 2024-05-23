package com.example.web1.controller;

import com.example.web1.model.FileEntity;
import com.example.web1.service.FileService;
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
import java.util.List;

@Controller
public class FileController {

  @Value("${app.server.address}")
  private String serverAddress;

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
    try {
      for (MultipartFile file : files) {
        fileService.saveFile(file);
      }
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 성공하였습니다!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패하였습니다.: " + e.getMessage());
    }

    return "redirect:/uploadForm";
  }

  @GetMapping("/downloads")
  public String showFiles(Model model) {
    List<FileEntity> files = fileService.getAllFiles();
    model.addAttribute("files", files);
    return "download";
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
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
  public String showUploadForm() {
    return "uploadForm";
  }
}

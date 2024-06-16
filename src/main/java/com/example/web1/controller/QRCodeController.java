package com.example.web1.controller;

import com.example.web1.service.FileService;
import com.example.web1.service.SurveyService;
import com.example.web1.service.QuizService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QRCodeController {

  @Value("${app.server.address}")
  private String serverAddress;

  private final FileService fileService;
  private final SurveyService surveyService;
  private final QuizService quizService;

  @GetMapping("/generate-qr")
  public ResponseEntity<byte[]> generateQRCode(
    @RequestParam(value = "memberId", required = false) Long memberId,
    @RequestParam("purpose") String purpose,
    @RequestParam(value = "id", required = false) Long id, // Chat에는 id가 필요 없으므로 optional로 설정
    @RequestParam(value = "url", required = false) String url) { // 채팅 URL
    try {
      // 채팅용 QR 코드 생성
      if ("chat".equals(purpose)) {
        if (url == null) {
          return ResponseEntity.badRequest().body(null); // URL이 없으면 잘못된 요청
        }
        return generateQRCodeFromUrl(url);
      }

      // 기타 용도 (퀴즈, 설문조사, 파일 다운로드)용 QR 코드 생성
      if (memberId == null) {
        return ResponseEntity.badRequest().body(null); // memberId가 없으면 잘못된 요청
      }

      String tempSessionId;
      switch (purpose) {
        case "fileDownload":
          tempSessionId = fileService.createTemporarySession(memberId);
          break;
        case "objectiveSurveyAnswer":
        case "subjectiveSurveyAnswer":
          tempSessionId = surveyService.createTemporarySession(memberId);
          break;
        case "objectiveQuiz":
        case "subjectiveQuiz":
          tempSessionId = quizService.createTemporarySession(memberId);
          break;
        default:
          throw new IllegalArgumentException("Unknown purpose: " + purpose);
      }

      String finalUrl = generateUrlWithSession(purpose, id, tempSessionId);
      return generateQRCodeFromUrl(finalUrl);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  private String generateUrlWithSession(String purpose, Long id, String tempSessionId) {
    switch (purpose) {
      case "fileDownload":
        return serverAddress + "/redirect-download?tempSessionId=" + tempSessionId;
      case "objectiveSurveyAnswer":
      case "subjectiveSurveyAnswer":
        return serverAddress + "/survey/" + purpose + "/" + id + "?tempSessionId=" + tempSessionId;
      case "objectiveQuiz":
      case "subjectiveQuiz":
        return serverAddress + "/quiz/" + purpose + "/" + id + "?tempSessionId=" + tempSessionId;
      default:
        throw new IllegalArgumentException("Unknown purpose: " + purpose);
    }
  }

  private ResponseEntity<byte[]> generateQRCodeFromUrl(String url) {
    try {
      int width = 350;
      int height = 350;

      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

      BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
      byte[] qrImageBytes = baos.toByteArray();

      return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType.IMAGE_PNG)
        .header("QRCodeURL", url)
        .body(qrImageBytes);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}

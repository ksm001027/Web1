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
import org.springframework.ui.Model; // 추가
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
    @RequestParam("memberId") Long memberId,
    @RequestParam("purpose") String purpose,
    @RequestParam("id") Long id) {
    try {
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

      String url = generateUrlWithSession(purpose, id, tempSessionId);

      int width = 350;
      int height = 350;

      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

      BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
      byte[] qrImageBytes = baos.toByteArray();

      // 로그 추가
      System.out.println("Generated QR Code URL: " + url);

      return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType.IMAGE_PNG)
        .header("QRCodeURL", url)
        .body(qrImageBytes);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  private String generateUrlWithSession(String purpose, Long id, String tempSessionId) {
    String url;
    switch (purpose) {
      case "fileDownload":
        url = serverAddress + "/redirect-download?tempSessionId=" + tempSessionId;
        break;
      case "objectiveSurveyAnswer":
      case "subjectiveSurveyAnswer":
        url = serverAddress + "/survey/" + purpose + "/" + id + "?tempSessionId=" + tempSessionId;
        break;
      case "objectiveQuiz":
      case "subjectiveQuiz":
        url = serverAddress + "/quiz/" + purpose + "/" + id + "?tempSessionId=" + tempSessionId;
        break;
      default:
        throw new IllegalArgumentException("Unknown purpose: " + purpose);
    }
    return url;
  }
}

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
  public ResponseEntity<byte[]> generateQRCode(@RequestParam("memberId") Long memberId, @RequestParam("purpose") String purpose, @RequestParam("id") Long id) {
    try {
      String tempSessionId;
      if (purpose.equals("fileDownload")) {
        tempSessionId = fileService.createTemporarySession(memberId);
      } else if (purpose.equals("survey")) {
        tempSessionId = surveyService.createTemporarySession(memberId);
      } else {
        tempSessionId = quizService.createTemporarySession(memberId);
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

      System.out.println("Generated QR Code URL: " + url); // 로그 추가

      return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_PNG).header("QRCodeURL", url).body(qrImageBytes);
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
        return serverAddress + "/survey/objectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      case "subjectiveSurveyAnswer":
        return serverAddress + "/survey/subjectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      case "objectiveQuiz":
        return serverAddress + "/quiz/objectiveQuiz/" + id + "?tempSessionId=" + tempSessionId;
      case "subjectiveQuiz":
        return serverAddress + "/quiz/subjectiveQuiz/" + id + "?tempSessionId=" + tempSessionId;
      default:
        throw new IllegalArgumentException("Unknown purpose: " + purpose);
    }
  }
}

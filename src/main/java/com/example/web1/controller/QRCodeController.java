package com.example.web1.controller;

import com.google.zxing.BarcodeFormat;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
<<<<<<< HEAD
import org.springframework.http.MediaType;
=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class QRCodeController {

<<<<<<< HEAD
  @GetMapping(value = "/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generateQRCode(@RequestParam("url") String url) {
    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
      String qrText = "http://your-server-address/chat-room"; // 서버 URL을 QR 코드로 변환
=======
  @Value("${app.server.address}")
  private String serverAddress;

  @GetMapping("/generate-qr")
  public ResponseEntity<byte[]> generateQRCode(@RequestParam("url") String url) {
    try {
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
      int width = 350;
      int height = 350;

      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

<<<<<<< HEAD
      ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
      byte[] pngData = pngOutputStream.toByteArray();
=======
      BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
      byte[] qrImageBytes = baos.toByteArray();
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d

      return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(pngData);
    } catch (WriterException | IOException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(null);
    }
  }
}

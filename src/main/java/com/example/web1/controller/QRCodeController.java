package com.example.web1.controller;

import com.google.zxing.BarcodeFormat;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
public class QRCodeController {

  @GetMapping(value = "/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generateQRCode(@RequestParam("url") String url) {
    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

      ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
      byte[] pngData = pngOutputStream.toByteArray();

      return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(pngData);
    } catch (WriterException | IOException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(null);
    }
  }
}

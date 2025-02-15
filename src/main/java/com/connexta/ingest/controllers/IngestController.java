/*
 * Copyright (c) 2019 Connexta, LLC
 *
 * Released under the GNU Lesser General Public License version 3; see
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */
package com.connexta.ingest.controllers;

import com.connexta.ingest.rest.spring.IngestApi;
import com.connexta.ingest.service.api.IngestService;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
public class IngestController implements IngestApi {

  @NotNull private final IngestService ingestService;

  @Override
  public ResponseEntity<Void> ingest(
      String acceptVersion, MultipartFile file, String correlationId, MultipartFile metacard) {
    String fileName = file.getOriginalFilename();
    log.info("Ingest request received fileName={}", fileName);
    log.info("Ignoring attached metacard");
    InputStream inputStream;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      throw new ValidationException("Could not open attachment");
    }
    ingestService.ingest(file.getSize(), file.getContentType(), inputStream, fileName);

    return ResponseEntity.accepted().build();
  }
}

package com.projects.vocalPad.controller;

import com.projects.vocalPad.services.NoteToVoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequestMapping("/voice")
@Tag(name = "Notes to Voice Conversion APIs" , description = "Endpoints to download or listen note converted to voice")
public class NoteToVoiceController {

    @Autowired
    private NoteToVoiceService noteToVoiceService;

    @GetMapping("/download/{noteId}")
    @Operation(summary = "Download voice note as file")
    public ResponseEntity<InputStreamResource> download(@PathVariable String noteId) {
        try {
            File audioFile = noteToVoiceService.download(noteId);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(audioFile));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + audioFile.getName() + ".mp3")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(audioFile.length())
                    .body(resource);
        } catch (RuntimeException | FileNotFoundException e) {
            log.error("Failed to download file", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listen/{noteId}")
    @Operation(summary = "Listen voice note")
    public ResponseEntity<Resource> listen(@PathVariable String noteId) {
        try {
            ObjectId id = new ObjectId(noteId);
            String fileName = noteToVoiceService.getFileName(id);
            String headerValue = "inline; filename={fileName}.mp3";
            headerValue = headerValue.replace("{fileName}", fileName);

            Resource audio = noteToVoiceService.listen(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(audio);
        } catch (RuntimeException e) {
            log.error("Unable to convert to audio ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.projects.vocalpad.services;

import com.projects.vocalpad.entity.Note;
import com.projects.vocalpad.entity.User;
import com.projects.vocalpad.repo.NotesRepository;
import com.projects.vocalpad.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class NoteToVoiceService {

    @Autowired
    private NotesRepository nRepo;

    @Autowired
    private UserRepository uRepo;

    @Value("${voice_id}")
    private String voiceId;

    @Value("${voice_api_key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private String finalUrl;

    private String finalStreamingUrl;

    @PostConstruct
    private void initUrls() {
        String apiUrl = "https://api.elevenlabs.io/v1/text-to-speech/{voice_id}";
        finalUrl = apiUrl.replace("{voice_id}", voiceId);

        String apiUrlStreaming = "https://api.elevenlabs.io/v1/text-to-speech/{voice_id}/stream";
        finalStreamingUrl = apiUrlStreaming.replace("{voice_id}", voiceId);
    }

    public File download(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);

        ObjectId noteId = new ObjectId(id);
        Note note = nRepo.findById(noteId).orElse(null);

        if(user!=null && note != null ) {
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent){
                String noteTitle = note.getTitle();
                String noteContent = note.getContent();

                HttpHeaders header = new HttpHeaders();
                header.set("xi-api-key", apiKey);
                header.setContentType(MediaType.APPLICATION_JSON);


                String body = "{\n  \"text\": \"Note title - {noteTitle} Note Content - {noteContent}\"\n}";
                body = body.replace("{noteTitle}", noteTitle);
                body = body.replace("{noteContent}", noteContent);

                HttpEntity<String> entity = new HttpEntity<>(body, header);

                ResponseEntity<Resource> response = restTemplate.exchange(finalUrl, HttpMethod.POST, entity, Resource.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody()!=null) {
                    String fileName = getFileName(noteId);

                    File audioFile = new File(fileName);

                    try (InputStream inputStream = response.getBody().getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(audioFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        return audioFile;
                    } catch (RuntimeException | IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    @PostMapping("/stream")
    public Resource listen(ObjectId noteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        Note note = nRepo.findById(noteId).orElse(null);

        if(user!=null && note != null ) {
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent){
                String noteTitle = note.getTitle();
                String noteContent = note.getContent();

                HttpHeaders header = new HttpHeaders();
                header.set("xi-api-key", apiKey);
                header.add("content-type", "application/json");


                String body = "{\n  \"text\": \"Note title - {noteTitle} Content - {noteContent}\"\n}";
                body = body.replace("{noteTitle}", noteTitle);
                body = body.replace("{noteContent}", noteContent);

                HttpEntity<String> entity = new HttpEntity<>(body, header);

                ResponseEntity<Resource> response = restTemplate.exchange(finalStreamingUrl, HttpMethod.POST, entity, Resource.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                }
            }
        }
        return null;
    }

    public String getFileName(ObjectId noteId) {
        Note note = nRepo.findById(noteId).orElse(null);
        if(note!=null){
            return note.getTitle();
        }
        return null;
    }
}
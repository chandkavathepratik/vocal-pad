package com.projects.vocalpad.controller;

import com.projects.vocalpad.dto.NoteDTO;
import com.projects.vocalpad.entity.Note;
import com.projects.vocalpad.services.NotesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@Slf4j
@Tag(name = "Notes Management APIs" , description = "Endpoints to manage notes of specific logged in user")
public class NotesController {

    @Autowired
    private NotesService nService;

    @PostMapping
    @Operation(summary = "Creates a new note.")
    public ResponseEntity<String> createNewNote(@RequestBody NoteDTO myNote){
        try {
            nService.createNewNote(myNote);
            return new ResponseEntity<>("Entry added", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Retrieves all notes of user")
    public ResponseEntity<List<Note>> getAllNotes() {
        try{
            return new ResponseEntity<>(nService.getAllNotes(), HttpStatus.FOUND);
        }
        catch (Exception e){
            log.error("Failed to fetch notes", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{noteId}")
    @Operation(summary = "Get specific note or open note")
    public ResponseEntity<Note> getNote(@PathVariable String noteId) {
        try{
            return new ResponseEntity<>(nService.getNote(noteId), HttpStatus.FOUND);
        } catch (RuntimeException e) {
            log.error("Failed to get note", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{noteId}")
    @Operation(summary = "Update note title or content")
    public ResponseEntity<String> updateNote(@RequestBody NoteDTO updatedNote, @PathVariable String noteId){
        try {
            nService.updateNote(updatedNote, noteId);
            return new ResponseEntity<>("Note updated..!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to update", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Delete a note")
    public ResponseEntity<Boolean> deleteNote(@PathVariable String noteId){
        try {
            return new ResponseEntity<>(nService.deleteNote(noteId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete note", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{text}")
    @Operation(summary = "Get all notes matching with the field text and content")
    public ResponseEntity<List<Note>> search(@PathVariable String text){
        try {
            return new ResponseEntity<List<Note>>(nService.search(text), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/trash")
    @Operation(summary = "Get deleted notes")
    public ResponseEntity<List<Note>> getTrash() {
        try{
            return new ResponseEntity<>(nService.getTrash(), HttpStatus.FOUND);
        }
        catch (Exception e){
            log.error("Failed to fetch trash", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/empty-trash")
    @Operation(summary = "Empty trash or bin of user")
    public ResponseEntity<String> emptyTrash(){
        try {
            return new ResponseEntity<>(nService.emptyTrash(), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to empty trash", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/trash-delete/{noteId}")
    @Operation(summary = "Permanently delete note from trash or bin of user")
    public ResponseEntity<String> deleteTrashNote(@PathVariable String noteId) {
        try {
            return new ResponseEntity<>(nService.deleteTrashNote(noteId), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to note from trash", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/restore/{noteId}")
    public ResponseEntity<String> restoreNote(@PathVariable String noteId){
        try {
            return new ResponseEntity<>(nService.restoreNote(noteId), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to restore note from trash", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/archives")
    @Operation(summary = "get archived note")
    public ResponseEntity<List<Note>> getArchive() {
        try{
            return new ResponseEntity<>(nService.getArchive(), HttpStatus.FOUND);
        } catch (RuntimeException e) {
            log.error("Failed to get archived notes", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/archive/{noteId}")
    @Operation(summary = "Archive note")
    public ResponseEntity<String> archiveNote(@PathVariable String noteId) {
        try{
            nService.archiveNote(noteId);
            return new ResponseEntity<>("Archived", HttpStatus.FOUND);
        } catch (RuntimeException e) {
            log.error("Failed to archive note", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/unarchive/{noteId}")
    @Operation(summary = "Remove note from archive")
    public ResponseEntity<String> removeArchivedNote(@PathVariable String noteId) {
        try{
            nService.removeArchivedNote(noteId);
            return new ResponseEntity<>("Removed from archive", HttpStatus.FOUND);
        } catch (RuntimeException e) {
            log.error("Failed to archive note", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
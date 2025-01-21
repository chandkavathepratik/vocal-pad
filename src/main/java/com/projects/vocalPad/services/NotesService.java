package com.projects.vocalPad.services;

import com.projects.vocalPad.dto.NoteDTO;
import com.projects.vocalPad.entity.Note;
import com.projects.vocalPad.entity.User;
import com.projects.vocalPad.repo.NotesRepoImpl;
import com.projects.vocalPad.repo.NotesRepository;
import com.projects.vocalPad.repo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotesService {

    @Autowired
    private NotesRepository nRepo;

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private NotesRepoImpl nRepoImpl;

    @Autowired
    private UserService uService;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void createNewNote(NoteDTO myNote) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        if(user!=null){
            Note note = new Note();
            note.setTitle(myNote.getTitle());
            note.setContent(myNote.getContent());
            note.setDate(LocalDateTime.now());

            Note saved = nRepo.save(note);
            user.getNotes().add(saved);
            uRepo.save(user);
        }

    }

    public List<Note> getAllNotes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        if(user != null){
            return user.getNotes();
        }
        return List.of();
    }

    @Transactional
    public void updateNote(NoteDTO updatedNote, String noteId) {
        ObjectId id = new ObjectId(noteId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        Note note = nRepo.findById(id).orElse(null);

        if(user!=null && note!=null) {
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent) {
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                Note saved = nRepo.save(note);
                user.getNotes().add(saved);
                uRepo.save(user);
            }
        }
    }

    @Transactional
    public boolean deleteNote(String noteId) {
        ObjectId id = new ObjectId(noteId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        Note note = nRepo.findById(id).orElse(null);

        if(user!=null && note!=null) {
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent) {
                user.getNotes().remove(note);
                user.getTrash().add(note);
                uRepo.save(user);
                return true;
            }
        }
        return false;
    }

    public Note getNote(String noteId) {
        ObjectId id = new ObjectId(noteId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        Note note = nRepo.findById(id).orElse(null);

        if(user!=null && note != null ) {
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent) {
                return note;
            }
        }
        return null;
    }

    public List<Note> search(String text) {
        return nRepoImpl.searchByText(text);
    }

    public List<Note> getTrash() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = uRepo.findByUsername(username);
        return user.getTrash();
    }

    public List<Note> getArchive() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = uRepo.findByUsername(username);
        return user.getArchive();
    }

    @Transactional
    public void archiveNote(String noteId) {
        ObjectId id = new ObjectId(noteId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = uRepo.findByUsername(username);
        Note note = nRepo.findById(id).orElse(null);

        if(user!=null && note!=null){
            boolean isPresent = user.getNotes().contains(note);
            if(isPresent){
                user.getArchive().add(note);
                user.getNotes().remove(note);
                uRepo.save(user);
            }
        }

    }

    @Transactional
    public String emptyTrash() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = uRepo.findByUsername(username);

        if(user!=null) {
            nRepo.deleteAll(user.getTrash());
            user.getTrash().clear();
            uRepo.save(user);
            return "Trash cleared";
        }
        return "Failed to clear trash";
    }

    @Transactional
    public void removeArchivedNote(String noteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username  = auth.getName();
        User user = uRepo.findByUsername(username);
        ObjectId id = new ObjectId(noteId);
        Note note = nRepo.findById(id).orElse(null);
        if(user!=null && note!=null && user.getArchive().contains(note)){
            user.getArchive().remove(note);
            user.getNotes().add(note);
            uRepo.save(user);
        }
    }
}
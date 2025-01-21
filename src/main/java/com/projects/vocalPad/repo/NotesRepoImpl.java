package com.projects.vocalPad.repo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.projects.vocalPad.entity.Note;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NotesRepoImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoConverter converter;

    private MongoDatabase getDatabase() {
        return mongoTemplate.getDb();
    }

//search not working
    public List<Note> searchByText(String text) {
        List<Note>  notes = new ArrayList<>();

        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = database.getCollection("notes");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("index", "vocal-pad-notes")
                                .append("text",
                        new Document("query", text)
                                .append("path", Arrays.asList("title", "content")))),
                new Document("$sort",
                        new Document("date", 1L))));

        result.forEach(doc -> notes.add(converter.read(Note.class, doc)));
        return notes;
    }
}
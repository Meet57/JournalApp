package com.meet.myFirstProject.controller;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController
{
    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public ResponseEntity<String> addEntry(@RequestBody JournalEntry journalEntry)
    {
        try
        {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>("Entry saved successfully!", HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntries()
    {
        List<JournalEntry> entries = journalEntryService.getAll();
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id)
    {
        Optional<JournalEntry> journalEntry = Optional.ofNullable(journalEntryService.getById(id));
        if (journalEntry.isPresent())
        {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<String> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry)
    {
        journalEntry.setId(id);
        Optional<JournalEntry> existingEntry = Optional.ofNullable(journalEntryService.getById(id));

        if (existingEntry.isPresent())
        {
            journalEntryService.updateEntry(journalEntry);
            return new ResponseEntity<>("Entry updated successfully!", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId id)
    {
        Optional<JournalEntry> journalEntry = Optional.ofNullable(journalEntryService.getById(id));

        if (journalEntry.isPresent())
        {
            journalEntryService.deleteEntry(id);
            return new ResponseEntity<>("Entry deleted successfully!", HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
        }
    }
}

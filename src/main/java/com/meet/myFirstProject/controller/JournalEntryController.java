package com.meet.myFirstProject.controller;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController
{
    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public String addEntry(@RequestBody JournalEntry journalEntry)
    {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return "Entry saved successfully!";
    }

    @GetMapping
    public List<JournalEntry> getAllEntries()
    {
        return journalEntryService.getAll();
    }

    @GetMapping("id/{id}")
    public JournalEntry getEntryById(@PathVariable ObjectId id)
    {
        return journalEntryService.getById(id);
    }

    @PutMapping("id/{id}")
    public String updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry)
    {
        journalEntry.setId(id);  // Make sure to set the correct ID for the update operation
        journalEntryService.updateEntry(journalEntry);
        return "Entry updated successfully!";
    }

    @DeleteMapping("id/{id}")
    public String deleteEntry(@PathVariable ObjectId id)
    {
        journalEntryService.deleteEntry(id);
        return "Entry deleted successfully!";
    }
}

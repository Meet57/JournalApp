package com.meet.myFirstProject.service;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveEntry(JournalEntry journalEntry)
    {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll()
    {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getById(ObjectId id)
    {
        return journalEntryRepository.findById(id).orElse(null);
    }

    public void updateEntry(JournalEntry journalEntry)
    {
        if (journalEntryRepository.existsById(journalEntry.getId())) {
            journalEntryRepository.save(journalEntry);  // Will update the existing entry
        } else {
            throw new RuntimeException("Entry not found for id: " + journalEntry.getId());
        }
    }

    public void deleteEntry(ObjectId id)
    {
        if (journalEntryRepository.existsById(id)) {
            journalEntryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Entry not found for id: " + id);
        }
    }
}

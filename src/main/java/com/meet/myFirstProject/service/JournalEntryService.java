package com.meet.myFirstProject.service;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName)
    {
        try
        {
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            User user = userService.findByUserName(userName);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException("Something wend bad", e);
        }
    }

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
        if (journalEntryRepository.existsById(journalEntry.getId()))
        {
            journalEntryRepository.save(journalEntry);  // Will update the existing entry
        }
        else
        {
            throw new RuntimeException("Entry not found for id: " + journalEntry.getId());
        }
    }

    public void deleteEntry(ObjectId id, String userName)
    {
        if (journalEntryRepository.existsById(id))
        {
            journalEntryRepository.deleteById(id);
            User byUserName = userService.findByUserName(userName);
            byUserName.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            userService.saveEntry(byUserName);
        }
        else
        {
            throw new RuntimeException("Entry not found for id: " + id);
        }
    }
}

package com.meet.myFirstProject.service;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

            // Update the user's journal entries
            User user = userService.findByUserName(userName);
            if (user != null)
            {
                user.getJournalEntries().add(saved);
                userService.saveUserWithoutChangingPassword(user);
            }
            else
            {
                throw new RuntimeException("User not found for username: " + userName);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred while saving the journal entry.", e);
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
        if (journalEntry.getId() == null)
        {
            throw new IllegalArgumentException("Journal entry ID cannot be null for update.");
        }

        if (journalEntryRepository.existsById(journalEntry.getId()))
        {
            journalEntryRepository.save(journalEntry); // Will update the existing entry
        }
        else
        {
            throw new RuntimeException("Entry not found for ID: " + journalEntry.getId());
        }
    }

    @Transactional
    public void deleteEntry(ObjectId id, String userName)
    {
        try
        {
            User user = userService.findByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if (removed)
            {
                userService.saveUserWithoutChangingPassword(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred while deleting the journal entry.", e);
        }
    }
}

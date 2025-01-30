package com.meet.myFirstProject.controller;

import com.meet.myFirstProject.entity.JournalEntry;
import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.service.JournalEntryService;
import com.meet.myFirstProject.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> addEntry(@RequestBody JournalEntry journalEntry)
    {
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry, auth.getName());
            return new ResponseEntity<>("Journal entry created successfully!", HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Failed to save the journal entry. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllEntriesOfUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(authentication.getName());

        if (user == null)
        {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> entries = user.getJournalEntries();
        if (entries == null || entries.isEmpty())
        {
            return new ResponseEntity<>("No journal entries found for the user.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Object> getEntryById(@PathVariable ObjectId id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());

        if (user == null)
        {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> matchingEntries = user.getJournalEntries().stream()
                .filter(entry -> entry.getId().equals(id))
                .toList();

        if (!matchingEntries.isEmpty())
        {
            Optional<JournalEntry> journalEntry = Optional.ofNullable(journalEntryService.getById(id));
            if (journalEntry.isPresent())
            {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Journal entry not found with ID: " + id, HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<String> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());

        if (user == null)
        {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> matchingEntries = user.getJournalEntries().stream()
                .filter(entry -> entry.getId().equals(id))
                .toList();

        if (!matchingEntries.isEmpty())
        {
            Optional<JournalEntry> existingEntry = Optional.ofNullable(journalEntryService.getById(id));
            if (existingEntry.isPresent())
            {
                JournalEntry oldEntry = existingEntry.get();

                // Update fields only if provided in the request
                oldEntry.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty()
                        ? journalEntry.getTitle()
                        : oldEntry.getTitle());
                oldEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty()
                        ? journalEntry.getContent()
                        : oldEntry.getContent());
                oldEntry.setDate(LocalDateTime.now());

                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>("Journal entry updated successfully!", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Journal entry not found with ID: " + id, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<JournalEntry> journalEntry = Optional.ofNullable(journalEntryService.getById(id));

        if (journalEntry.isPresent())
        {
            journalEntryService.deleteEntry(id, authentication.getName());
            return new ResponseEntity<>("Journal entry deleted successfully!", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Journal entry not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

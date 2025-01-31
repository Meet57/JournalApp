package com.meet.myFirstProject.service;

import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService
{
    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUserWithoutChangingPassword(User userEntry)
    {
        userRepository.save(userEntry);
    }

    public void saveUser(User userEntry)
    {
        try
        {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
            userEntry.setRoles(List.of("USER"));
            userRepository.save(userEntry);
        }
        catch (Exception e)
        {
            log.warn(e.getMessage());
        }
    }

    public void saveAdmin(User userEntry)
    {
        userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
        userEntry.setRoles(List.of("ADMIN", "USER"));
        userRepository.save(userEntry);
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public User getById(ObjectId id)
    {
        return userRepository.findById(id).orElse(null);
    }

    public void updateEntry(User journalEntry)
    {
        if (userRepository.existsById(journalEntry.getId()))
        {
            userRepository.save(journalEntry);  // Will update the existing entry
        }
        else
        {
            throw new RuntimeException("Entry not found for id: " + journalEntry.getId());
        }
    }

    public void deleteEntry(ObjectId id)
    {
        if (userRepository.existsById(id))
        {
            userRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Entry not found for id: " + id);
        }
    }

    public User findByUserName(String userName)
    {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
    }
}

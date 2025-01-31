package com.meet.myFirstProject.service;

import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest
{
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserName()
    {
        assertNotNull(userRepository.findByUserName("admin"));
    }

//    @Disabled
    @Test
    public void testFindByUserName_noJournal()
    {
        Optional<User> admin2 = userRepository.findByUserName("admin2");
        assertTrue(admin2.get().getJournalEntries().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "1,11,12",
            "3,3,6"
    })
    public void test(int a,int b,int c)
    {
        assertEquals(c, a+b);
    }
}

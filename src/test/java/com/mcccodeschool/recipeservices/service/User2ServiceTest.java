package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.User2DTO;
import com.mcccodeschool.recipeservices.dto.UserInfoDTO;
import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.repository.User2Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class User2ServiceTest {

    @Autowired
    User2Service user2Service;

    @Autowired
    private ModelMapper modelMapper;

    Random rand = new Random();


    User2DTO user2DTO(){
        User2DTO user2DTO = new User2DTO();
        user2DTO.setId(123L);
        user2DTO.setUsername("test");
        user2DTO.setEnabled(true);
        user2DTO.setEmail("test" + String.valueOf(rand.nextInt(999)) + "@test.com");
        user2DTO.setProvider("testProvider");
        user2DTO.setProviderId("123");
        return user2DTO;
    }

    @Test
    public void mapsDtoToPayload(){
//        User2DTO user2DTO = new User2DTO();
    }

    @Test
    void savesUser(){
//        User2DTO user2DTO = new User2DTO();
        User2DTO savedUser2DTO = user2Service.saveUser(user2DTO());
        System.out.println(savedUser2DTO);
        Assertions.assertNotNull(savedUser2DTO.getId());
//        Assertions.assertEquals(Long.valueOf(savedUser2DTO.getId()), 123L);
    }


}

package com.example.cs393backend.ServiceTests;

import com.example.cs393backend.dto.UserDto;
import com.example.cs393backend.entity.UserEntity;
import com.example.cs393backend.repository.UserRepository;
import com.example.cs393backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import com.example.cs393backend.util.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testUser");
        userDto.setEmail("test@example.com");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUser");
        userEntity.setEmail("test@example.com");
    }

    @Test
    void findAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));
        when(userMapper.userEntityToDto(any(UserEntity.class))).thenReturn(userDto);

        List<UserDto> result = userService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));

        verify(userRepository).findAll();
        verify(userMapper).userEntityToDto(any(UserEntity.class));
    }

    @Test
    void findUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userMapper.userEntityToDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userRepository).findById(anyLong());
        verify(userMapper).userEntityToDto(any(UserEntity.class));
    }

    @Test
    void createUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.userDtoToEntity(any(UserDto.class))).thenReturn(userEntity);
        when(userMapper.userEntityToDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).userDtoToEntity(any(UserDto.class));
        verify(userMapper).userEntityToDto(any(UserEntity.class));
    }

    @Test
    void updateUser() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(1L);
        updatedUserDto.setUsername("updatedUser");
        updatedUserDto.setEmail("updated@example.com");

        when(userMapper.userEntityToDto(any(UserEntity.class))).thenReturn(updatedUserDto);
        UserDto result = userService.updateUser(1L, updatedUserDto);


        assertNotNull(result);
        assertEquals(updatedUserDto.getUsername(), result.getUsername());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());

        verify(userRepository).findById(anyLong());
        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).userEntityToDto(any(UserEntity.class));
    }


    @Test
    void deleteUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }



}

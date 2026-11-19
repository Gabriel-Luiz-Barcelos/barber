package com.cesul.barber.services;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.entities.User;
import com.cesul.barber.mappers.UserMapper;
import com.cesul.barber.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    public UsersService(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public UserCreationDTO create(UserCreationDTO dto) {
        validate(dto);

        User user = userMapper.toEntity(dto);
        user.setId(UUID.randomUUID());

        return userMapper.toDTO(usersRepository.save(user));
    }

    public List<UserCreationDTO> findAll() {
        return usersRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public Optional<UserCreationDTO> findById(UUID id) {
        return usersRepository.findById(id).map(userMapper::toDTO);
    }

    public Optional<UserCreationDTO> update(UUID id, UserCreationDTO updateDate) {
        validate(updateDate);

        Optional<User> userOptional = usersRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        user.setName(updateDate.getName());
        user.setEmail(updateDate.getEmail());
        user.setAvatarUrl(updateDate.getAvatarUrl());

        return Optional.of(userMapper.toDTO(usersRepository.save(user)));
    }

    public Optional<UserCreationDTO> updateAvatar(UUID id, Map<String, String> request) {
        Optional<User> userOptional = usersRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        if (request.containsKey("avatarUrl")) {
            user.setAvatarUrl(request.get("avatarUrl"));
        }

        return Optional.of(userMapper.toDTO(usersRepository.save(user)));
    }

    public boolean delete(UUID id) {
        return usersRepository.deleteById(id);
    }

    public boolean clear() {
        usersRepository.clear();
        return true;
    }

    private void validate(UserCreationDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome é obrigatório.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank() || !dto.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O e-mail é obrigatório e deve ser válido.");
        }
    }
}
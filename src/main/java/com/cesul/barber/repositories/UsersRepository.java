package com.cesul.barber.repositories;

import com.cesul.barber.entities.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsersRepository {

    private final List<User> users = new ArrayList<>();

    public User save(User user) {
        int index = users.indexOf(user); // usa o equals (por ID)
        if (index >= 0) {
            users.set(index, user);
        } else {
            users.add(user);
        }
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public boolean deleteById(UUID id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public void clear() {
        users.clear();
    }
}
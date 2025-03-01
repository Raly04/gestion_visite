package org.example.daos;

import org.example.models.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Integer> {
    Optional<User> findByUsername(String username);
}

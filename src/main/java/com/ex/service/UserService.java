package com.ex.service;

import com.ex.entity.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}

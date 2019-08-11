package com.ex.service;


import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.dao.UserDao;

import com.ex.entity.History;
import com.ex.entity.Rating;
import com.ex.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * UserServiceImpl implements UserService
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RatingsDao ratingsDao;
    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     *
     To save the user in the database, we also fill in the columns in the "ratings" table with
     zeros and the empty history in the "history" table
     * @param user of class User
     */

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
        ratingsDao.save(new Rating(0L,0L,user.getUsername(),"0000"));
        historyDao.save(new History(user.getUsername(),""));
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
package com.ex.dao;

import com.ex.entity.History;
import com.ex.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDao extends JpaRepository<History, Integer> {

    History findByUsername(String username);
}

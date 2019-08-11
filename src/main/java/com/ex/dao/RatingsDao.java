package com.ex.dao;
import com.ex.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingsDao extends JpaRepository<Rating, Integer> {

    Rating findByUsername(String username);

}

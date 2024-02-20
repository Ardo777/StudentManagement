package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    List<Message> findAllByFrom_Id(int id);
}

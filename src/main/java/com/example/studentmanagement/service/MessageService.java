package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageService {
    Message save(String message, int toId, User fromId);

    List<Message> allMessages(int id);

}

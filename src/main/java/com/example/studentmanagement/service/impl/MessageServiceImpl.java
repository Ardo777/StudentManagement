package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.MessageRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Message save(String message, int toId, User fromId) {
        Date date = new Date();
        Optional<User> byId = userRepository.findById(toId);
        if (byId.isPresent()){
            User toUser = byId.get();
            Message message1 = new Message(0,message,fromId,toUser,date);
            return messageRepository.save(message1);
        }else return null;
    }

    @Override
    public List<Message> allMessages(int id) {
        return messageRepository.findAllByFrom_Id(id);
    }
}

package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${picture.upload.directory}")


    private String uploadDirectory;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final MailServiceImpl mailService;

    @Override
    public User save(User user, MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            user.setUserType(UserType.STUDENT);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                File file = new File(uploadDirectory, picName);
                multipartFile.transferTo(file);
                user.setPicName(picName);
            }
//            String  message= String.valueOf(System.currentTimeMillis());
//            String firstSixCharacters = message.substring(0, Math.min(message.length(), 6));
            String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
            String uuid = lUUID.substring(0, Math.min(lUUID.length(), 6));
//            mailService.send(user.getEmail(),"Welcome",String.format("Hi %s , this is your verify code %s ",user.getName(), uuid));
            mailService.sendMail(user);
            user.setVerificationCode(uuid);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public List<User> findByUserType(UserType userType) {
        return userRepository.findByUserType(userType);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        Optional<User> byId = findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User update(User user, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        } else {
            Optional<User> byId = findById(user.getId());
            if (byId.isPresent()) {
                User userById = byId.get();
                user.setPicName(userById.getPicName());
                user.setUserType(UserType.STUDENT);
                return user;
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public void changeUserByLesson(Lesson lesson) {
        List<User> byLesson = userRepository.findByLesson(lesson);
        for (User user : byLesson) {
            user.setLesson(null);
        }
    }

    @Override
    public List<User> findAllByUserTypeAndLesson(UserType userType, Lesson lesson) {
        return userRepository.findAllByUserTypeAndLesson(userType, lesson);
    }
}

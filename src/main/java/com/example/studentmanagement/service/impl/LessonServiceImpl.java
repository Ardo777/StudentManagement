package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.service.LessonService;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final UserService userService;

    @Override
    public Lesson save(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Optional<Lesson> findById(int id) {
        return lessonRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        Optional<Lesson> byId = findById(id);
        if (byId.isPresent()) {
            Lesson lesson = byId.get();
            userService.changeUserByLesson(lesson);
        }
        lessonRepository.deleteById(id);
    }

}
package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.service.LessonService;
import com.example.studentmanagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LessonController {
    private final UserService userService;
    private final LessonService lessonService;

    @GetMapping("/lesson")
    public String lessonPage(ModelMap modelMap) {
        List<Lesson> lessons = lessonService.findAll();
        modelMap.put("lessons", lessons);
        return "lesson";
    }

    @GetMapping("/lesson/add")
    public String addLessonPage(ModelMap modelMap) {
        List<User> teachers = userService.findByUserType(UserType.TEACHER);
        if (teachers != null && !teachers.isEmpty()) {
            modelMap.addAttribute("teachers", teachers);
            return "addLesson";
        }
        return "addLesson";
    }

    @PostMapping("/lesson/add")
    public String addLesson(@ModelAttribute Lesson lesson, @RequestParam("teacherId") int teacherId){
        Optional<User> byId = userService.findById(teacherId);
        byId.ifPresent(lesson::setUser);
        lessonService.save(lesson);
        return "redirect:/lesson";
    }


    @Transactional
    @GetMapping("/lesson/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {

        lessonService.deleteById(id);
        return "redirect:/lesson";
    }

    @GetMapping("/lesson/update/{id}")
    public String updateLessonPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Lesson> byId = lessonService.findById(id);
        List<User> teachers = userService.findByUserType(UserType.TEACHER);
        if (byId.isPresent()) {
            Lesson lesson = byId.get();
            modelMap.addAttribute("lesson", lesson);
            modelMap.addAttribute("teachers", teachers);
            return "updateLesson";
        }
        return "lesson";
    }

    @PostMapping("/lesson/update")
    public String updateUser(@ModelAttribute Lesson lesson,@RequestParam("teacher.id") int id){
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()){
            lesson.setUser(byId.get());
        }
        lessonService.save(lesson);
        return "redirect:/lesson";
    }
}
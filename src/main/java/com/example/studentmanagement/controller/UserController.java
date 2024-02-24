package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.LessonService;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.service.impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LessonService lessonService;
    private final MailServiceImpl mailService;

    @GetMapping("/user/add")
    public String addUserPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonService.findAll());
        return "addUser";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        User save = userService.save(user, multipartFile);
        if (save == null) {
            if (user.getUserType() == UserType.TEACHER) {
                return "redirect:/lesson?msg=User Added";
            }
            return "redirect:/user/home?msg=User Added";
        } else {
            return "redirect:/user/register?msg=Email already in used";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, @AuthenticationPrincipal SpringUser springUser) {
        userService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/user/update/{id}")
    public String updateUserPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        List<Lesson> lessons = lessonService.findAll();
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("lessons", lessons);
            return "updateUser";
        } else
            return "user";
    }


    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        userService.update(user, multipartFile);
        return "redirect:/user/home";
    }

    @GetMapping("/user/register")
    public String userRegisterPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        return "register";
    }

    @PostMapping("/user/register")
    public String userRegister(@ModelAttribute User user, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        User saveUser = userService.save(user, multipartFile);

            return "redirect:/user/register/verification/" + saveUser.getEmail();

    }


    @GetMapping("/user/home")
    public String homePage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if (springUser != null) {
            User user = springUser.getUser();
            List<Lesson> lessons = lessonService.findAll();
            modelMap.addAttribute("lessons", lessons);
            if (user.getUserType() == UserType.TEACHER) {
                List<User> teachers = userService.findByUserType(UserType.TEACHER);
                modelMap.addAttribute("teachers", teachers);
                return "lesson";
            } else {
                List<User> students = userService.findAllByUserTypeAndLesson(UserType.STUDENT, user.getLesson());
                modelMap.addAttribute("students", students);
                return "user";
            }
        }
        return "loginPage";
    }

    @GetMapping("/user/register/verification/{mail}")
    public String verificationPage(@PathVariable("mail") String mail, ModelMap modelMap) {
        Optional<User> userOptional = userService.findByEmail(mail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            modelMap.addAttribute("user", user);
            mailService.send(mail, "Welcome", String.format("Hi %s  this is your verify code %s ", user.getName(), user.getVerificationCode()));
        }

        return "mailVerification";
    }

    @PostMapping("/user/register/verification")
    public String verification(@RequestParam("id")int id,@RequestParam("verificationCode") String verificationCode){

        Optional<User> byId = userService.findById(id);
        if (byId.isEmpty()){
            return "redirect:/404";
        }
        User user = byId.get();
        if (user.getVerificationCode().equals(verificationCode)){
            return "redirect:/loginPage";
        }else {
            userService.deleteById(id);
            return "redirect:/register";
        }

    }

    @GetMapping("/loginPage")
    public String loginPage(){
        return "loginPage";
    }
}

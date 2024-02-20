package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.MessageService;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/message/send")
    public String sendMessage(@RequestParam("message") String message,
                              @RequestParam("toUserId") int toId,
                              @AuthenticationPrincipal SpringUser springUser) {
        User fromUser = springUser.getUser();
        messageService.save(message, toId, fromUser);
       return "redirect:/user/home";
    }

    @GetMapping("/message/send/{studentId}")
    public String send(@PathVariable("studentId") int id, ModelMap modelMap) {
        Optional<User> userById = userService.findById(id);
        if (userById.isPresent()) {
            User toUser = userById.get();
            modelMap.addAttribute("toId", toUser);
            return "message";
        }
        return "user";
    }

    @GetMapping("messages/list")
    public String myMessages(@AuthenticationPrincipal SpringUser springUser,ModelMap modelMap){
        User user = springUser.getUser();
        List<Message> messages = messageService.allMessages(user.getId());
        modelMap.addAttribute("messages",messages);
        return "messageList";
    }
}

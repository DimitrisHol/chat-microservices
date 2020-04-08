package hello.controller;


import hello.model.Message;
import hello.model.User;
import hello.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
public class ChatDBContoller {


    private final ChatService chatService;

    public ChatDBContoller(ChatService chatService) {
        this.chatService = chatService;
    }


    // At the start of the session, client makes a request to see the past 10 messages.
    @GetMapping("/chatlog")
    public List<Message> chatLog() throws Exception {

        List<Message> recentMessages = chatService.returnRecentMessages();
        System.out.println(recentMessages.toString());
        return recentMessages;
    }

    // At the start of the session, client makes a request to see who is online
    @GetMapping("/activeUsers")
    public List<User> activeUsers() throws Exception {

        List<User> activeUsers = chatService.returnActiveUseres();
        System.out.println(activeUsers.toString());
        return activeUsers;
    }

    // A user sends a message
    @PostMapping("/message")
    public void newMessage(@RequestBody Message message){

        System.out.println("I've received a message !" + message.toString());
        chatService.newMessage(message);

    }

    // A user logins to the chat server
    @PostMapping("/user")
    public void userJoined(@RequestBody User user){

        System.out.println("New user has joined! : " + user.toString());
        user.setIs_online(true);
        chatService.userJoined(user);
    }

    // A user logs out to the chat server
    @PostMapping("/user-offline")
    public void updateUserStatus(@RequestBody User user){

        System.out.println("Logging user out " + user.toString());
        user.setIs_online(false);
        chatService.userLeft(user);
    }
}

package hello.controllerAPI;


import hello.model.Message;
import hello.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;


@CrossOrigin(origins = "*" , allowedHeaders = "*")
@Controller
public class WebSocketController {

    @Autowired
    private RestTemplate restTemplate;

    // A user sends a message at topic "/hello"
    // We reply to topic  "/hello"

    @MessageMapping("/chat")
    @SendTo("/topic/chat-stream")
    public Message messageLog (Message message) throws Exception{

        // POST message to database.
        restTemplate.postForLocation("http://thesis-database/message", message);

        // Return the the same message with the a current timestamp

        // Create the current timestamp
        Date date= new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        String content = message.getMessage();

        return new Message(message.getAuthorName() , content , timestamp);

    }

    // A user enters the chat, sends a message to topic /login
    // We temporarily log them into the userList data structure.
    @MessageMapping("/login")
    @SendTo("/topic/chat-stream")
    public Message userLogin (User user , SimpMessageHeaderAccessor headAccessor) throws Exception{

        // POST user to the database.
        // Update that user is now online to the database.
        restTemplate.postForLocation("http://thesis-database/user", user);

        // Set the name of the user to notify chat when he leaves.
        headAccessor.getSessionAttributes().put("author" , user.getUsername());

        String authorUsername = user.getUsername();
        String content = "has joined the chat";

        Date date= new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        // Send a notification to the chat that the user has joined the chat.
        Message message = new Message(authorUsername, content, timestamp);
        message.setType("JOIN");

        return message;
    }



}

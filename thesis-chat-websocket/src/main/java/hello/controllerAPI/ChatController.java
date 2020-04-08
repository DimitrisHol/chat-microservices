package hello.controllerAPI;


import hello.model.Message;
import hello.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/")
public class ChatController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/chatlog")
    public List<Message> chatLog() throws Exception {

        List<Message> recentMessages = restTemplate.getForObject("http://thesis-database/chatlog", List.class);

        System.out.println(recentMessages.toString());
        return recentMessages;
    }

    @GetMapping("/activeUsers")
    public List<User> activeUsers() throws Exception {

        List<User> activeUsers = restTemplate.getForObject("http://thesis-database/activeUsers", List.class);
        System.out.println(activeUsers.toString());
        return activeUsers;
    }


}

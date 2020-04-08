package hello.service;

import hello.dao.chatDao;
import hello.model.Message;
import hello.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatService {

    private final chatDao chatDao;


    @Autowired
    public ChatService(@Qualifier("postgres") chatDao chatDao){
        this.chatDao = chatDao;
    }

    // Methods related to Messages.

    public Message newMessage(Message message) {

        return chatDao.newMessage(message);
    }

    public List<Message> returnRecentMessages() {

        return chatDao.selectRecentMessages();

    }

    public List<User> returnActiveUseres(){

        return chatDao.selectActiveUsers();
    }

    // Methods related to User activity.

    public void userJoined(User user){

        chatDao.userLoggedIn(user);
    }

    public void userLeft(User user) {

        chatDao.userLoggedOut(user);


    }

}

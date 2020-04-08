package hello.dao;

import hello.model.Message;
import hello.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository("postgres")
public class ChatDatabaseAccessRepository implements chatDao {

    private final JdbcTemplate jdbcTemplate;

    public ChatDatabaseAccessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // New message arrived, store it in the database.
    @Override
    public Message newMessage(Message message) {

        //Message has the author name, we need to get the author_id to insert into the database.
        User author = userNameLookUp(message.getAuthorName());
        int author_id = author.getId();

        String content = message.getMessage();

        Date date= new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        final String sql  = "INSERT INTO messages(author_id , content, submitted_at) VALUES (? , ? , ?)";
        jdbcTemplate.update(sql , author_id, content, timestamp);

        return new Message(message.getAuthorName() , content , timestamp);
    }

    // When the a users joins the chat, return the 10 most recent messages back.
    @Override
    public List<Message> selectRecentMessages() {

        final String sql = "select username , content, submitted_at from users inner join messages on users.user_id = author_id order by submitted_at desc limit 10;";

        List<Message> messages = jdbcTemplate.query(sql, (resultSet, i) -> {

            String author = resultSet.getString("username");
            String content = resultSet.getString("content");
            Timestamp timeStamp  = resultSet.getTimestamp("submitted_at");

            // Convert
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            return new Message(author , content, timeStamp);
        });

        return messages;
    }

    @Override
    public List<User> selectActiveUsers() {
        final String sql = "SELECT username from users where is_online = true";

        List<User> activeUsers = jdbcTemplate.query(sql , (resultSet , i) -> {

            String author = resultSet.getString("username");
            return new User(0, author);
        });
        return activeUsers;
    }

    // User logged in , add him to the database if he's not there already, and flag him as active.
    @Override
    public void userLoggedIn(User user) {

        String username = user.getUsername();

        // Test if user is already in the database
        User checkUser = userNameLookUp(username);

        if (checkUser == null){
            final String sql  = "INSERT INTO users(username) VALUES (?)";
            jdbcTemplate.update(sql , user.getUsername());

        }

        // Flag him as active

        final String sqlUpdate = "UPDATE USERS SET is_online = TRUE WHERE username = ?";
        jdbcTemplate.update(sqlUpdate , user.getUsername());


    }

    public User userNameLookUp(String username){


        final String sql = "SELECT * FROM users where username = ?";

        try {
            User user =  jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{username},
                    (resultSet, i) ->
                            new User(
                                    resultSet.getInt("user_id") ,
                                    resultSet.getString("username")
                            ));
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;

        }

    }

    // User logged out, flag him as no longer active.
    @Override
    public void userLoggedOut(User user) {

        // Get his name
        String username = user.getUsername();

        // Get the database reference to the user
        User checkUser = userNameLookUp(username);

        // Flag him as inactive
        if (checkUser != null) {
            final String sqlUpdate = "UPDATE USERS SET is_online = FALSE WHERE username = ?";
            jdbcTemplate.update(sqlUpdate, user.getUsername());
        }
    }
}

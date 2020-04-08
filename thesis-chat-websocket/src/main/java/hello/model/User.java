package hello.model;

public class User {

    private final int id;
    private final String username;
    private boolean is_online;


    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean Is_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", is_online=" + is_online +
                '}';
    }
}

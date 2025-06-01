package model;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = username.equals("admin") && password.equals("123");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return (username.equals("admin") && password.equals("123")) || password.equals("1234");
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

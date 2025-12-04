package modelo;

import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    public String username;
    public String fullname;
    public String password;
    public Directory home;
   

    public User(){}
    
    public User(String username, String fullname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

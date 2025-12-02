
package modelo;

import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    public String username;
    public String fullname;
    public String password;
    public Integer root;

    public User(){}
    
    public User(String username, String fullname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }
}

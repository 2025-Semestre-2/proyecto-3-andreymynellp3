
package modelo;

public class User {
    private String username;
    private String fullname;
    private String password;
    private int userId;
    private int groupId;

    public User(String username, String fullname, String password, int userId, int groupId) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.userId = userId;
        this.groupId = groupId;
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

    public int getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    
    
}

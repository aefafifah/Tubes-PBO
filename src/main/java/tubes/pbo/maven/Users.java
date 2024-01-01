package tubes.pbo.maven;

import java.io.Serializable;

public class Users implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String religion;
    private String password;
    private int id;

    public Users(String name, String email, String phone, String address, String religion, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.religion = religion;
        this.password = password;
    }

    public Users() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId () {
        return id;
    }

    public void setId (int id ) {
        this.id = id ;
    }

}

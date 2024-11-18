package com.example.myapplication;

public class UserResponse {
    private int id;
    private String user_id;
    private String name;
    private int age;
    private String email;
    private String password;
    private String user_name;
    private int height;
    private int weight;

    // Add getters and setters

    public int getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }
//    ------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_name='" + user_name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}

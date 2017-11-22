package org.vaadin.artur.firebase.db.data;

import org.vaadin.artur.firebase.HasKey;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements HasKey {

    private String key;
    private String name;
    private int age;

    public User() {

    }

    public User(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + "]";
    }

}

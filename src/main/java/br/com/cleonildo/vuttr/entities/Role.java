package br.com.cleonildo.vuttr.entities;

public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String nameRole;

    Role(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getNameRole(){
        return this.nameRole;
    }
}

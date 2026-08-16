package com.example.fantasta.auction_service.dto;

// dto/PlayerDTO.java
public class PlayerDTO {
    
    private Integer id;
    private String name;
    private String surname;
    private String role;
    private String team;
    

    public PlayerDTO() {}
    public PlayerDTO(Integer id, String name, String surname, String role, String team) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.team = team;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }


}
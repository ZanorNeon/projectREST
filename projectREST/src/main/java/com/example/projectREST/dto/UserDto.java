package com.example.projectREST.dto;

public class UserDto {

    public UserDto(Long id, String email, String password, String fullName, Boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.enabled = enabled;
    }

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private Boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

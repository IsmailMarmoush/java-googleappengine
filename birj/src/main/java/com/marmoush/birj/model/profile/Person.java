package com.marmoush.birj.model.profile;

import java.util.Date;
import java.util.Map;

import javax.annotation.ManagedBean;

import com.marmoush.birj.model.AbstractEntity;

//@XmlRootElement
public class Person extends AbstractEntity {

    private static final long serialVersionUID = -5665972504889566259L;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date lastLogin;

    private Map<String, Object> personMap;
    
    public Person() {
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
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

    public Date getLastLogin() {
	return lastLogin;
    }

    public Map<String, Object> getPersonMap() {
	return personMap;
    }

    public void setPersonMap(Map<String, Object> personMap) {
	this.personMap = personMap;
    }

}

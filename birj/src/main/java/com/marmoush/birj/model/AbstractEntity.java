package com.marmoush.birj.model;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = -3861082540539277027L;

    private String id;
    private Integer power;
    private Date created;
    private Date lastUpdated;

    public Date getCreated() {
	return created;
    }

    public String getId() {
	return id;
    }

    public Date getLastUpdated() {
	return lastUpdated;
    }

    public Integer getPower() {
	return power;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
    }

    public void setPower(Integer power) {
	this.power = power;
    }

}
package com.parra.event.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Event implements Serializable {

	private static final long serialVersionUID = 224117223595682766L;

	@Id
	@Column(name = "event_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "Type")
	private String type;
	
	@Column(name = "Created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Dublin/London")
	private Date created_at;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actor_id", referencedColumnName = "actor_id")
    private Actor actor;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "repo_id", referencedColumnName = "repo_id")
    private Repo repo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public Repo getRepo() {
		return repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", type=" + type + " date=" + created_at + " Actor=" + actor + "]";
	}

}

package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CONTACT")
public class contact 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	@NotBlank(message = "Name cannot be blank!")
	@Size(min = 4, max = 20)
	private String name;
	
	private String secondName;
	
	@NotBlank(message = "Work cannot be blank!")
	@Size(min = 4, max = 45)
	private String work;
	
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid Email !!")
	private String email;
	
	@Pattern(regexp="^[0-9]{10}$", message="Phone number must be 10 digits")
	private String phone;
	private String image;
	@Column(length = 5000)
	private String description;
	
	@ManyToOne
	@JsonIgnore 
	//JsonIgnore does not serialize User, meaning when we send json response, user will not be included in the body.
	private User user;
	
	public contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	return this.cId==((contact)obj).getcId();
	}
/*
	@Override
	public String toString() {
		return "contact [cId=" + cId + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", User=" + User
				+ "]";
	}
	*/
	
	
}

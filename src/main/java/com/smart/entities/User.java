package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "USER")
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "Name should not be blank!")
	@Size(min = 2, max = 20, message = "Minimum 2 and Maximum 20 charachters allowed")
	private String name;
	
	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid Email !!")
	private String email;
	
	@NotBlank(message = "Password should not be blank!")
	private String password;
	private String role;
	private boolean enabled;
	private String imageUrl;
	
	/*
	🔍 How It Works:
	@Column(name = "user_name") → Maps the userName field to the user_name column
	in the database.
	
	length = 50 → Sets the max length of the column to 50 characters.
	
	nullable = false → The column cannot be NULL.
	
	unique = true → Ensures no two users have the same user_name.
	columnDefinition = "VARCHAR(100) DEFAULT 'unknown'" → Sets a custom SQL
	column type and default value.
	
	🛠️ When to Use @Column?
	If you want custom column names different from Java field names.
	If you need constraints like nullable, length, unique, etc.
	If you want custom SQL column definitions.
	It is schema-level annotations. These influence the database structure (schema) generated or expected by JPA/Hibernate.
		 */
	@Column(length = 500)
	private String about;
	
	// CascadeType.ALL ensures that any operation (save, delete, update) 
	// performed on the User entity will also be applied to its associated 
	// contacts. For example, saving a User will also save its contacts, 
	// and deleting a User will remove its contacts from the database.
	
	//orphanRemoval = true means when we unlink a child entity(contact) with its parent(user), the child entity will be removed.
	//so by doing Contact.setUser(Null), that contact will be unlinked and removed. Check userController delete URL
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<contact> contacts = new ArrayList<>(); 
	/*
	Sure! Here's an even simpler explanation:

### 🔑 What `mappedBy` does:

- It tells Hibernate **where the foreign key is stored**.  
- In your case, the foreign key is in the **`CONTACT`** table.

- **`mappedBy = "User"`** → This points to this field in the `contact` class:


This means:  
➡️ **"The `CONTACT` table has a column that links it to the `USER` table."**

### ⚖️ Why is it needed?

- Without `mappedBy` → Hibernate creates an extra table to manage the link.  
- With `mappedBy` → Hibernate directly adds a **user_id** in the `CONTACT` table.

**Think of it like:**  
📖 *"`USER` has many `CONTACTS`, but the link is stored in the `CONTACT` table."*
	 */
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "user [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	
	
	
}

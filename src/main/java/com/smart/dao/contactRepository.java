package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.contact;
import com.smart.entities.User;

public interface contactRepository extends JpaRepository<contact, Integer>
{
	//method for pagination...
	@Query("from contact as c where c.user.id =:userId")
	public Page<contact> findContactsByUser(@Param("userId") int userId, Pageable p);
	/*Pageable object has 2 stuffs:-
		1) current page	(eg 1st page)
		2) contact per page (eg 5 contacts)
	*/
	
	//search
	public List<contact> findByNameContainingAndUser(String name, User user);
}	

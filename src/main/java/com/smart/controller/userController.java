package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.contactRepository;
import com.smart.dao.userRepository;
import com.smart.entities.contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/user")
public class userController {
	@Autowired
	userRepository Ur;

	@Autowired
	contactRepository Cr;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	// This method runs before every handler method in this controller.
	// Spring creates the Model internally and adds attributes to it here,
	// so these attributes are automatically available in all view pages.
	// You don't have to explicitly declare 'Model model' and add these attributes
	// again in each handler method.
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);
		// get the user using username(Email)
		User User = this.Ur.getUserByUserName(userName);
		System.out.println("USER " + User);

		model.addAttribute("user", User);
		model.addAttribute("contact", new contact());

	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("Title", "User Dashboard");
		return "Normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("Title", "Add Contact");
		model.addAttribute("contact", new contact());
		return "Normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String addContactHandler(@Valid @ModelAttribute contact contact, BindingResult bindingResult,
			@RequestParam("Profileimage") MultipartFile file, Principal principal, Model model, HttpSession Session) {

		try {

			if (bindingResult.hasErrors()) {
				model.addAttribute("contact", contact);
				System.out.println("ERROR " + bindingResult.toString());
				return "Normal/add_contact_form";
			}
			String name = principal.getName();
			User User = Ur.getUserByUserName(name);

			// processing and uploading file...
			if (file.isEmpty()) {
				System.out.println("File is empty");
				contact.setImage("default.jpg");
				// If the file is empty then try our message
			} else {
				File saveFile = new File("uploads");

			if (!saveFile.exists()) {
    				saveFile.mkdirs(); // Create the folder if it doesn't exist
				}

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// upload the file to folder and update the name to contact
				//contact.setImage(file.getOriginalFilename());
				//File saveFile = new ClassPathResource("static/img").getFile();
				// Consists of a path
				// Eg- C:\Users\YourName\your-spring-project\target\classes\static\img

				/*
				 * getFile() converts this into File object/
				 * 
				 * ClassPathReource : It points to the actual file system location of the
				 * static/img folder in the compiled target/classes directory, not the source
				 * src/main/resources. matlab compile hoke jaha bhi code save hoga at the end of
				 * the day, classpathresource uss location sei static/img dhundega.
				 * 
				 * src/main/resources/static/img folder is for development, later on the content
				 * of this gets copied to different location 'target/resources/static/img', so
				 * classPathResource yha sei read karega current folders.
				 * 
				 * ✅ From where does ClassPathResource load files? ➤ It loads files from the
				 * classpath, which typically means: target/classes/
				 * 
				 * or if packaged: yourapp.jar!/ (inside the JAR)
				 * 
				 * 🔍 What is the "classpath"? The classpath is the place where the Java
				 * application looks for classes and resources at runtime.
				 * 
				 * In a Spring Boot project, everything inside:
				 * 
				 * src/main/resources/ is automatically copied to:
				 * 
				 * target/classes/ when the project is compiled or built.
				 * 
				 * 🧪 Example If your file is here in source:
				 * 
				 * src/main/resources/static/img/pic.jpg Then after building (Maven/Gradle or
				 * via IDE), it becomes: target/classes/static/img/pic.jpg
				 * 
				 * So when you write:
				 * 
				 * new ClassPathResource("static/img/pic.jpg");
				 * 
				 * Spring internally looks at: /static/img/pic.jpg in the classpath (i.e.,
				 * target/classes)
				 * 
				 * 📦 What if it’s in a JAR? If the application is packaged like this: java -jar
				 * myapp.jar
				 * 
				 * Then the file is inside the JAR file, like:
				 * 
				 * myapp.jar!/static/img/pic.jpg
				 * 
				 * Spring still finds it using ClassPathResource, because it knows how to look
				 * inside JARs too (using URLClassLoader or similar).
				 * 
				 * However:
				 * 
				 * 🚫 You can’t write to files inside the JAR, which is why .getFile() may fail
				 * in production (write logic).
				 */
				// getting the path
				//Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				System.out.println(path);
				/*
				 * This line is building a complete file path where your uploaded image will be
				 * saved, and it's storing it in a Path object.
				 * 
				 * 🔹 Goal of This Line To build a full file path like this:
				 * /absolute/path/to/static/img/filename.jpg
				 * 
				 * So you can use it to copy the uploaded file to that location.
				 * 
				 * 🔹 Let's Break It Down ✅ Paths.get(...) From java.nio.file.Paths
				 * 
				 * It creates a Path object — a modern alternative to the old File class.
				 * 
				 * Path is used with Files.copy() to perform file operations like writing to
				 * disk.
				 * 
				 * ✅ saveFile.getAbsolutePath() saveFile is a File object that points to the
				 * static/img folder.
				 * 
				 * .getAbsolutePath() returns the full path to that folder.
				 * 
				 * Example:
				 * 
				 * saveFile.getAbsolutePath() // Might return something like:
				 * /Users/you/yourproject/target/classes/static/img ✅ File.separator A
				 * platform-independent separator used in file paths.
				 * 
				 * It’s / on Linux/macOS and \\ on Windows.
				 * 
				 * So instead of hardcoding /, you write:
				 * 
				 * File.separator ✅ Cross-platform safe code!
				 * 
				 * ✅ file.getOriginalFilename() file is the uploaded MultipartFile from the HTML
				 * form.
				 * 
				 * .getOriginalFilename() returns the original name of the uploaded file.
				 * 
				 * Example:
				 * 
				 * file.getOriginalFilename() // "profilepic.jpg" 🔹 Putting It All Together
				 * 
				 * Path path = Paths.get( saveFile.getAbsolutePath() + File.separator +
				 * file.getOriginalFilename() ); This constructs a path like:
				 * 
				 * /Users/you/yourproject/target/classes/static/img/profilepic.jpg So now you
				 * can copy the uploaded file to that location using:
				 */
				//Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded!");
			}
				
			//Since it is a bi-directional mapping, we have to set mappings from both side.
			User.getContacts().add(contact);

			contact.setUser(User);
			Ur.save(User);
			/// We are not calling Cr.save(contact) because CascadeType.ALL is enabled on the User entity.
			// This means when we add the contact to the user's contact list and save the user using Ur.save(user),
			// Hibernate will automatically save the contact as well (due to cascading behavior).
			// The contact will be saved first, and then the user, maintaining proper relationship consistency.

			System.out.println("Added to DataBase!");
			System.out.println(contact);

			// success msg
			model.addAttribute("message", new Message("Contact Added!", "success"));

		} catch (Exception e) {
			System.out.println("ERRROR" + e.getMessage());
			// error msg
			model.addAttribute("message", new Message("Something went wrong!... Try again!", "error"));

		}

		return "Normal/add_contact_form";
	}

	// show contacts handler
	// per page 5 contacts
	// current page = 0
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal p) {

		String userName = p.getName();
		User User = Ur.getUserByUserName(userName);
		/*
		 * List<contact> contacts = User.getContacts();
		 */
		// per page 5 contacts
		// current page = 0
		Pageable pageable = PageRequest.of(page, 5);
		/*
		 * What is Pageable? It's an interface that defines:
		 * 
		 * Which page to fetch
		 * 
		 * How many items per page
		 * 
		 * PageRequest — The Implementation of Pageable This is a static factory for
		 * creating Pageable instances:
		 */
		Page<contact> contacts = Cr.findContactsByUser(User.getId(), pageable);
		/*
		 * What is Page<T>? It's an interface that extends Slice<T> and Iterable<T>. It
		 * represents:
		 * 
		 * One page of data
		 * 
		 * Metadata about the pagination
		 */
		model.addAttribute("Title", "Show Contacts");
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages() - 1);
		System.out.println(contacts.getTotalPages());
		// send contact list
		return "Normal/show_contacts";
	}

	// showing specific contact detail
	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") int cId, Model model, Principal p) {
		System.out.println("cId : " + cId);
		Optional<contact> contactOptional = Cr.findById(cId);
		contact Contact = contactOptional.get();

		String userName = p.getName();
		User User = Ur.getUserByUserName(userName);

		if (User.getId() == Contact.getUser().getId()) {
			model.addAttribute("contact", Contact);
		}

		return "Normal/contact_detail";
	}

	@GetMapping("/delete/{cId}")
	public String deleteUser(@PathVariable("cId") int cId, Model model, Principal p, HttpSession session) {
		Optional<contact> contactOptional = Cr.findById(cId);
		contact Contact = contactOptional.get();

		String userName = p.getName();
		User User = Ur.getUserByUserName(userName);

		if (User.getId() == Contact.getUser().getId()) {
			User.getContacts().remove(Contact);
			
			Ur.save(User);
			
			session.setAttribute("message", new Message("Contact deleted Successfully!", "success"));
			return "redirect:/user/show-contacts/0";
		}

		return "Normal/show_contacts";

	}

	// open update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") int cId, Model model)
	{
		Optional <contact> contactOptional = Cr.findById(cId);
		contact Contact = contactOptional.get();
		
		model.addAttribute("Title","Update Contact");
		model.addAttribute("contact",Contact);
		return "Normal/update_form";
	}
	
	//update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute contact Contact, @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session, Principal p)
	{
		System.out.println("Contact Id : " + Contact.getcId());
		System.out.println("Contact Name : " + Contact.getName());
		
		try
		{
			//fetching old contact detail
			contact oldContact = Cr.findById(Contact.getcId()).get();
			
			//image...
			if(!file.isEmpty())
			{
				//file re write
				
				//delete old photo
				//File can point to both a Folder and a File.
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContact.getImage());
				file1.delete();
				//update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); 
				Contact.setImage(file.getOriginalFilename());
				
			}
			else
			{
				Contact.setImage(oldContact.getImage());
				
			}
			User User = Ur.getUserByUserName(p.getName());
			Contact.setUser(User);
			Cr.save(Contact);
		}
		catch(Exception e)
		{
			
		}
		return "redirect:/user/" + Contact.getcId() + "/contact";
	}
	
	//Your Profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("Title", "Profile Page");
		return "Normal/profile";
	}
	
	//Open Settings handler
	@GetMapping("/settings")
	public String openSettings()
	{
		return "Normal/settings";
	}
	
	//change password handler...
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			Principal p, HttpSession session, Model model)
	{
		System.out.println("OLD PASSWORD : "+ oldPassword);
		System.out.println("NEW PASSWORD : "+ newPassword);
		
		User currentUser = Ur.getUserByUserName(p.getName());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			//change password...
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.Ur.save(currentUser);
			model.addAttribute("message", new Message("Your Password is successfully Changed...", "alert-success"));
			return "Normal/user_dashboard";
		}
		else
		{
			//error...
			model.addAttribute("message", new Message("Please enter correct old password!", "alert-danger")); 
			return "Normal/settings";
		}
			
	}
	
	@PostMapping("/change-profile")
	public String changeProfilePic(@RequestParam("profilePic") MultipartFile profilePic, Model model, Principal p)
	{
		String contentType = profilePic.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) 
		{
		    model.addAttribute("message", new Message("Only images are allowed!", "alert-danger"));
		    return "settings";
		}
		
		User user = Ur.getUserByUserName(p.getName());
		
		try
		{
			user.setImageUrl(profilePic.getOriginalFilename());
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + profilePic.getOriginalFilename());
			Files.copy(profilePic.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			Ur.save(user);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		model.addAttribute("message", new Message("Settings Updated Successfully!", "alert-success"));
		return "Normal/settings";
	}
}

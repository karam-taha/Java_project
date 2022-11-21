package com.java.project.Controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.project.Models.Cars;
import com.java.project.Models.Parts;
import com.java.project.Models.User;
import com.java.project.Serveses.Serveses;
import com.java.project.validator.UserValidator;

@Controller
public class Controllers {
	private Serveses userService;
	private UserValidator userValidator;
	
	
	
	public Controllers(Serveses userService, UserValidator userValidator) {
		super();
		this.userService = userService;
		this.userValidator = userValidator;
	}

	@RequestMapping("/register")
	public String registration(
			@Valid @ModelAttribute("user") User user, 
			BindingResult result, 
			Model model, 
			HttpSession session,
			HttpServletRequest request) {
		userValidator.validate(user, result);
		// Store the password before it is encrypted
		String password = user.getPassword();
		if(result.hasErrors()) {
			return "loginPage.jsp";
		}
		// Make first user SUPER ADMIN
		if(userService.allUsers().size()==0) {
			userService.newUser(user, "ROLE_ADMIN");
		}else {
			userService.newUser(user, "ROLE_USER");
//			session.setAttribute("user_id", newUser.getId());
		}
		
		// Log in new user with the password we stored before encrypting it
		authWithHttpServletRequest(request, user.getEmail(), password);
		return "redirect:/";
	}
	
	// We will call this method to automatically log in newly registered users
	public void authWithHttpServletRequest(HttpServletRequest request, String email, String password) {
	    try {
	        request.login(email, password);
	    } catch (ServletException e) {
	    	System.out.println("Error while login: " + e);
	    }
	}
	
	@RequestMapping("/admin")
	public String adminPage(@Valid @ModelAttribute("addpart") Parts part ,BindingResult result ,Principal principal, Model model) {
		String email = principal.getName();
		model.addAttribute("currentUser", userService.findByEmail(email));
		model.addAttribute("users", userService.allUsers());
		return "adminPage.jsp";
	}
	
	@RequestMapping("/admin/{id}")
	public String makeAdmin(@PathVariable("id") Long id, Model model) {
		
		User user = userService.findById(id);
		userService.upgradeUser(user);
		
		model.addAttribute("users", userService.allUsers());
		 
		return "redirect:/admin";
	}
	
	@RequestMapping("/login")
	public String login(
			@ModelAttribute("user") User user,
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="logout", required=false) String logout, 
			Model model) {
		
		
		if(error!=null) {
			model.addAttribute("errorMessage","Invalid Credentials, Please try again.");
		}
		if(logout!=null) {
			model.addAttribute("logoutMessage","Logout Successful!");
		}
		
		return "loginPage.jsp";
	}
	
	@RequestMapping(value={"/", "/home"})
	public String home(Principal principal, Model model,HttpSession session) {
		String email = principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		session.setAttribute("user", user);
		
		if(user!=null) {
			user.setLastLogin(new Date());
			userService.updateUser(user);
			// If the user is an ADMIN or SUPER_ADMIN they will be redirected to the admin page
			if(user.getRoles().get(0).getName().contains("ROLE_ADMIN")||user.getRoles().get(0).getName().contains("ROLE_ADMIN")) {
				model.addAttribute("currentUser", userService.findByEmail(email));
				model.addAttribute("users", userService.allUsers());
				return "adminPage.jsp";
			}
			// All other users are redirected to the home page
		}
		
		return "home.jsp";
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, HttpSession session, Model model) {	
		User user = userService.findById(id);
		userService.deleteUser(user);
		
		model.addAttribute("users", userService.allUsers());
		 
		return "redirect:/admin";
	}
	
//----------------------------------------------------------------------------------	
//	 @Autowired
//	public final Serveses userServ;
//
//	 public Controllers(Serveses userServes) {
//		
//		this.userServ = userServes;
//	}
//	 @GetMapping("/login")
//	    public String index(Model model) {
//	        model.addAttribute("newUser", new User());
//	        model.addAttribute("newLogin", new Login());
//	        return "login.jsp";
//	    }
//	    @PostMapping("/register")
//	    public String register(@Valid @ModelAttribute("newUser") User newUser, 
//	            BindingResult result, Model model, HttpSession session) {
//	    	userServ.register(newUser, result);
//	        if(result.hasErrors()) {
//	            // Be sure to send in the empty LoginUser before 
//	            // re-rendering the page.
//	            model.addAttribute("newLogin", new Login());
//	            return "login.jsp";
//	        }
//	        session.setAttribute("user_id", newUser.getId());
//	        return "redirect:/home";
//	    }
//
//	    @PostMapping("/login")
//	    public String login(@Valid @ModelAttribute("newLogin") Login newLogin, 
//	            BindingResult result, Model model, HttpSession session) {
//
//	        // Add once service is implemented:
//	         User user = userServ.login(newLogin, result);
//	        if(result.hasErrors()) {
//	            model.addAttribute("newUser", new User());
//	            return "login.jsp";
//	        }
//	        session.setAttribute("user_id", user.getId());
//	        return "redirect:/home";
//	    }
//	    @GetMapping("/logout")
//	    public String logout(HttpSession session) {
//	    	session.invalidate();
//	    	return "redirect:/welcome";
//
//	    }
//-------------------------------------------------------------------------------------------	    
	    @GetMapping("/")
	    public String home(Model model,HttpSession session) {
//	    	List<Team> projects = userServ.findAll();
//	    	model.addAttribute("team", projects);
//	    	if(session.getAttribute("user_id")!=null){
//	    	User user = userService.findUser((Long) session.getAttribute("user_id"));
	    	
//	    	List<Projects> projects2 = userServ.allos(user);
//	    	model.addAttribute("projects2", projects2);
//	    	model.addAttribute("user", user);
//	    	}
	    	
	    	return "redirect:/home";
	    }
	   
	    
	    @GetMapping("/home")
	    public String home1(@ModelAttribute("car") Cars car,Model model,HttpSession session,Principal principal) {

	    	
//	    		User user = userService.findUser((Long) session.getAttribute("user_id"));
//	    		Cars car=userServ.findCarById(id);
	    		String email = principal.getName();
				User user = userService.findByEmail(email);
				model.addAttribute("user", user);
	    		List<Parts> allparts=userService.allpart();
	    		model.addAttribute("allparts", allparts);
	    		
	    		
	    	
	    	return "ShowAll.jsp";
	    }
	    
	    @GetMapping("/about")
	    public String about() {
	    	return "about.jsp";
	    }
	    
	    
//------------------------------------------------------------------------------------------------	    
	    @GetMapping("/addcar")
	    public String addcar(@ModelAttribute("car") Cars car,Principal principal,Model model) {
	    	String email = principal.getName();
	    	User user = userService.findByEmail(email);
	    	model.addAttribute("user", user);
	    	return "addcarpage.jsp";
	    }
	   
	    @PostMapping("/addcare")
	    public String createCar(@Valid @ModelAttribute("car") Cars car ,BindingResult result ,Model model,HttpSession session) {
	        if (result.hasErrors()) {
	            return "addcarpage.jsp";
	        } else {
	        	
	        	userService.createcar(car);
	    		
//	         	user.getTeams().add(car);
//	    		userServ.createTeam(car);
	            return "redirect:/home";
	        }
	    }
	    @GetMapping("/showcar")
	    public String ShowCar(Principal principal,Model model,HttpSession session) {
	    	
	    	String email = principal.getName();
			User user = userService.findByEmail(email);	    	
	    	model.addAttribute("user", user);
	    	List<Cars> Allcars= userService.findAllCars();
	    	model.addAttribute("allcars", Allcars);
	    	return "ShowCars.jsp";
	    	
	    }
//--------------------------------------------------------------------------------------
	    @GetMapping("/showorders")
	    public String Showorders(Principal principal,Model model,HttpSession session) {
	    	
	    	String email = principal.getName();
			User user = userService.findByEmail(email);	    	
	    	model.addAttribute("user", user);
//	    	List<Cars> Allcars= userServ.findAllCars();
//	    	model.addAttribute("allcars", Allcars);
	    	return "Showorders.jsp";
//-------------------------------------------------------------------------------------------   	
	    }
	    @GetMapping("/addpart")
	    
	    public String Addpart(@ModelAttribute("addpart") Parts part  ,Model model,HttpSession session) {
	    	
	    	User user = userService.findUser((Long) session.getAttribute("user_id"));
	    	
	    	model.addAttribute("user", user);
	    	
	    	return "addparts.jsp";
	    	
	    }
	    @PostMapping("/newpart")
	    public String createpart(@Valid @ModelAttribute("addpart") Parts part ,BindingResult result ,Model model,HttpSession session) {
	        if (result.hasErrors()) {
	            return "addparts.jsp";
	        } else {
	        	
	        	userService.createpart(part);
//	    		User user = userServ.findUser((Long)session.getAttribute("user_id"));
//	         	user.getTeams().add(car);
//	    		userServ.createTeam(car);
	            return "redirect:/home";
	        }
	    }
	    
//-----------------------------------------------------------------------------------------------------	    
	    
	    
//	    @PostMapping("/newteam")
//	    public String createProject(@Valid @ModelAttribute("team") Team team ,BindingResult result ,Model model,HttpSession session) {
//	        if (result.hasErrors()) {
//	            return "create.jsp";
//	        } else {
//
//	         	userServ.createTeam(team);
//	    		User user = userServ.findUser((Long)session.getAttribute("user_id"));
//	         	user.getTeams().add(team);
//	    		userServ.createTeam(team);
//	            return "redirect:/dashboard";
//	        }
//	    }
//	    @RequestMapping("/create")
//	    public String addProject(HttpSession session,@ModelAttribute("team") Team team,Model model) {
//	    	model.addAttribute("newLogin", new Team());
//	    	model.addAttribute("user",userServ.findUser((Long) session.getAttribute("user_id")));
//	    	return "create.jsp";
//	    	
//	    }
//
//	     @GetMapping("/showteam/{id}")
//	   	 public String showP(@PathVariable("id")Long id,Model model,HttpSession session) {
//	    	 User user=userServ.findUser((Long) session.getAttribute("user_id"));
//	    	 model.addAttribute("user",user);
//	   		 Team team= userServ.findTeam(id);
//	          List<User> users = userServ.findAllUser();
////	          team.getPlayers()
//	   		model.addAttribute("AllPlayers", users);
//	          
//	   		 model.addAttribute("team", team);
//	   		 return "Showteam.jsp";
//	   	 }
//	     
//	     
//	     @GetMapping("edit/{id}")
//	  	 public String edit(@PathVariable("id")Long id,Model model) {
//	  		 Team project = userServ.findTeam(id);
//	  		 model.addAttribute("editteam", project);
//	  		 return "Edit.jsp";
//	  	 }
//	      @PutMapping("/editteams/{id}")
//	  	 public String update(@Valid @ModelAttribute("editteam") Team team,BindingResult result,HttpSession session) {
//	  		   if (result.hasErrors()) {
//	  	            return "Edit.jsp";
//	  	            }
//	  		   else {
//	  			 userServ.createTeam(team);
//	  			 User user = userServ.findUser((Long)session.getAttribute("user_id"));
//	  			user.getTeams().add(team);
//	     		userServ.createTeam(team);
//
//	  			   return "redirect:/dashboard";
//	  		   }
//	  	 }
//	      @DeleteMapping("/delete/{id}")
//	      public String delete(@PathVariable("id")Long id) {
////	    	  Team project = userServ.findTeam(id);
//	    	  userServ.deleteTeam(id);
//	    	  return "redirect:/dashboard";
//	      }
//	      @PostMapping("/AddPlayer/{id}")
//	        public String AddPlayer(@Valid @ModelAttribute("player") User player, BindingResult result ,Model model ,@PathVariable("id") Long id, @RequestParam(name = "SelectPlayer") Long PlayerID) {
//	            Team thisTeam = userServ.findTeam(id);
////	            User playerr = userServ.findUser(PlayerID);
//	            
////	            List<User> users = userServ.findAllUser();
////	            model.addAttribute("AllPlayers", users);
////	            thisTeam.getUsers().add(Player);
//	            thisTeam.getPlayers().add(player);
//	            
//	            userServ.createTeam(thisTeam);
//	            userServ.updateUser1(player);
//	            return "redirect:/home";
//	        }
	     
	      
	      
//	      
//	      @PostMapping("/AddPlayer/{id}")
//	        public String AddPlayer(@Valid @ModelAttribute("player") User player, BindingResult result ,Model model ,@PathVariable("id") Long id, @RequestParam(name = "SelectPlayer") Long PlayerID) {
//	            Team thisTeam = userServ.findTeam(id);
//	            User newPlayer = userServ.findUser(PlayerID);
//	            thisTeam.getPlayers().add(newPlayer);
//	            
//	            userServ.updateTeam(thisTeam);
//	            userServ.updateUser1(newPlayer);
//	            return "redirect:/showteam/{id}";
//	        }
//	      
//	      
	     
	   
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
}

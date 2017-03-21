package com.privilance.controller;

		import com.privilance.model.User;
		import com.privilance.service.UserService;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.security.authentication.AnonymousAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.context.SecurityContextHolder;
		import org.springframework.stereotype.Controller;
		import org.springframework.validation.BindingResult;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.servlet.ModelAndView;

		import javax.validation.Valid;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)//redirect to login page
	public ModelAndView login(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();//get logged user

		if (!(auth instanceof AnonymousAuthenticationToken)) {
    		/* if the user is logged in  */
			return new ModelAndView("redirect:/index");
		}
		else {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login");
			return modelAndView;
		}
	}


	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"User with the same email already exists");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}

	@RequestMapping(value="/index", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName());
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("/index");
		return modelAndView;
	}
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(){
		SecurityContextHolder.getContext().setAuthentication(null);
		return "login";
	}

	@RequestMapping(value="/new",method=RequestMethod.GET)
	public String indexx(){
		return "new";
	}
}

package pl.stock.web.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.piomin.core.data.model.User;
import pl.piomin.core.data.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	void register(User user) {
		
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	void login(User user) {

	}
	
	
	
	
}

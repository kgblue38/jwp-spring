package next.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import core.valid.annotation.DeleteUser;
import core.valid.annotation.LoginUser;
import core.valid.annotation.SaveUser;
import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class UsersController {
	@Autowired
	private UserDao userDao;
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(User user) throws Exception {
        log.debug("User : {}", user);
        userDao.insert(user);
		return "redirect:/";
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(@LoginUser User loginUser, Model model) throws Exception {
    	if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
        model.addAttribute("users", userDao.findAll()); 
        return "/user/list";
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute("user", new User());
		return "/user/form";
	}
	
	@RequestMapping(value = "/{userId}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String userId, Model model, @LoginUser User loginUser) throws Exception {
		User user = userDao.findByUserId(userId);
		
		if (loginUser.isGuestUser()) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@RequestMapping(value = "/loginForm",  method = RequestMethod.GET)
	public String loginForm(Model model) {
		model.addAttribute("userId", "");
		model.addAttribute("password", "");
		return "user/login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@SaveUser
	public String login(@RequestParam String userId, @RequestParam String password, Model model) throws Exception {
        User user = userDao.findByUserId(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        
        if (user.matchPassword(password)) {
        	model.addAttribute("user", user);
            return "redirect:/";
        } else {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
    }
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	@DeleteUser
	public String logout() throws Exception {
        return "redirect:/";
    }
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showProfile(@RequestParam String userId, Model model) throws Exception {
		model.addAttribute("user", userDao.findByUserId(userId));
        return "/user/profile";
    }
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public String update(User updateUser, @LoginUser User loginUser, @PathVariable String userId) throws Exception {
		User user = userDao.findByUserId(userId);
		
        if (!user.isSameUser(loginUser)) {
        	throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        userDao.update(user);
        return "redirect:/";
	}
}








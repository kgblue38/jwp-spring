package core.intercepter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import core.valid.annotation.SaveUser;
import next.dao.UserDao;
import next.model.User;

public class SaveSessionUserIntercepter extends WebContentInterceptor{
	@Autowired
	private UserDao userDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerMethod handlerMethod = ((HandlerMethod)handler); 
		SaveUser saveUser = handlerMethod.getMethodAnnotation(SaveUser.class);
		if (saveUser != null) {
			request.getSession().setAttribute("user", modelAndView.getModel().get("user"));
		}
		return;
	}
}

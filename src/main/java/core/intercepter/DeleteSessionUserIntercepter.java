package core.intercepter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import core.valid.annotation.DeleteUser;

public class DeleteSessionUserIntercepter extends WebContentInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		DeleteUser deleteUser = ((HandlerMethod)handler).getMethodAnnotation(DeleteUser.class);
		if (deleteUser != null) {
			request.getSession().removeAttribute("user");
		}
		return true;
	}
}

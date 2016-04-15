package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import core.utils.UserSessionUtils;
import core.valid.annotation.LoginUser;
import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.service.QnaService;

@Controller
@RequestMapping("/questions")
public class QuestionsController {
	@Autowired
	private QuestionDao questionDao;
	@Autowired 
	private AnswerDao answerDao;
	@Autowired
	private QnaService qnaService;
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String form(@LoginUser User loginUser) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		return "/qna/form";
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String create(@RequestParam String title, @RequestParam String contents, @LoginUser User loginUser) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
    	Question question = new Question(loginUser.getUserId(), title, contents);
    	questionDao.insert(question);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/{questionId}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable long questionId, Model model, @LoginUser User loginUser) throws Exception {
    	if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		Question question = questionDao.findById(questionId);
		if (!question.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		model.addAttribute("question", question);
		return "/qna/update";
	}
	
	@RequestMapping(value = "/{questionId}", method = RequestMethod.PUT)
	public String update(@PathVariable long questionId, @RequestParam String title, @RequestParam String contents, @LoginUser User loginUser) throws Exception {
    	if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		
		Question question = questionDao.findById(questionId);
		if (!question.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		
		Question newQuestion = new Question(question.getWriter(), title, contents);
		question.update(newQuestion);
		questionDao.update(question);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
	public String show(@PathVariable long questionId, Model model) throws Exception {
		
        Question question = questionDao.findById(questionId);
        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        
        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        return "/qna/show";
	}
	
	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	public String destroy(@PathVariable long questionId, Model model, @LoginUser User loginUser) throws Exception {
    	if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		
		try {
			qnaService.deleteQuestion(questionId, loginUser);
			return "redirect:/";
		} catch (CannotDeleteException e) {
			model.addAttribute("question", qnaService.findById(questionId));
			model.addAttribute("answers", qnaService.findAllByQuestionId(questionId));
			model.addAttribute("errorMessage", e.getMessage());
			return "/qna/show";
		}
	}
}








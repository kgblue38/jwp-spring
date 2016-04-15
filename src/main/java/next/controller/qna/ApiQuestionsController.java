package next.controller.qna;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.jdbc.DataAccessException;
import core.utils.UserSessionUtils;
import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Result;
import next.model.User;
import next.service.QnaService;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionsController {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionsController.class);
	
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private QnaService qnaService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Map<String, Object> index(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> jsonResult = new HashMap<String, Object>();
		jsonResult.put("questions", questionDao.findAll());
		return jsonResult;
	}
	
	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	public Map<String, Object> execute(@PathVariable long questionId, HttpSession session) throws Exception {
    	Map<String, Object> jsonResult = new HashMap<>();
		if (!UserSessionUtils.isLogined(session)) {
			jsonResult.put("result", Result.fail("Login is required"));
			return jsonResult;
		}
		try {
			qnaService.deleteQuestion(questionId, UserSessionUtils.getUserFromSession(session));
			jsonResult.put("result", Result.ok());
			return jsonResult; 
		} catch (CannotDeleteException e) {
			jsonResult.put("result", Result.fail(e.getMessage()));
			return jsonResult; 
		}
	}
	
	@RequestMapping(value = "/{questionId}/answers", method = RequestMethod.POST)
	public Map<String, Object> answerCreate(@PathVariable long questionId, @RequestParam String contents, HttpSession session) throws Exception {
    	Map<String, Object> jsonResult = new HashMap<>();
		if (!UserSessionUtils.isLogined(session)) {
			jsonResult.put("result", Result.fail("Login is required"));
			return jsonResult;
		}
    	
    	User user = UserSessionUtils.getUserFromSession(session);
		Answer answer = new Answer(user.getUserId(), contents, questionId);
		log.debug("answer : {}", answer);
		
		Answer savedAnswer = answerDao.insert(answer);
		questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
		jsonResult.put("answer", savedAnswer);
		jsonResult.put("result", Result.ok());
		return jsonResult;
	}
	
	@RequestMapping(value = "/{questionId}/answers/{answerId}", method = RequestMethod.DELETE)
	public Map<String, Object> destroy(@PathVariable long answerId) throws Exception {
		Map<String, Object> jsonResult = new HashMap<>();
		try {
			answerDao.delete(answerId);
			jsonResult.put("result", Result.ok());
		} catch (DataAccessException e) {
			jsonResult.put("result", Result.fail(e.getMessage()));
		}
		return jsonResult;
	}
}


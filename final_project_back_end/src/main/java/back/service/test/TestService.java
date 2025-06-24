package back.service.test;

import java.util.List;
import java.util.Map;

import back.model.test.Test;

public interface TestService {
//	public List<Test> getQuestion(Test test);
//	public List<Test> getOption(Test test);
	public List<Map<String, Object>> getQuestionOption(Test test);
	public Test getResultTopCategory(Test test);
}

package back.mapper.test;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import back.model.test.Test;

@Mapper
public interface TestMapper {
	public List<Map<String, Object>> getType();
	public List<Test> getQuestion(Test test);
	public List<Test> getOption(Test test);
	public Test getResultTopCategory(Test test); 
}

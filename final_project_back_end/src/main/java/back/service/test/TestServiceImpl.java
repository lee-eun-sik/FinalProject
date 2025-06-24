package back.service.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.mapper.test.TestMapper;
import back.model.common.PostFile;
import back.model.test.Test;
import back.service.diary.DiaryServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
	@Autowired
	private TestMapper testMapper;
	@Autowired
	private FileMapper fileMapper;
	
//	@Override
//	public List<Test> getQuestion(Test test) {
//			try {
//				List<Test> questionList=testMapper.getQuestion(test);
//			return questionList;
//			}catch(Exception e) {
//				log.error("질문 목록 조회 실패", e);
//				throw new HException("질문 목록 조회 실패",e);
//			}	
//	}
//
//	@Override
//	public List<Test> getOption(Test test) {
//		try {
//			List<Test> optionQuestion=testMapper.getOption(test);
//		return optionQuestion;
//		}catch(Exception e) {
//			log.error("답변 목록 조회 실패", e);
//			throw new HException("답변 목록 조회 실패",e);
//		}	
//	}

	
	
	@Override
	public List<Map<String, Object>> getQuestionOption(Test test) {
		List<Test> questionList = testMapper.getQuestion(test);
		List<Integer> questionIds = new ArrayList<>();
		for(Test question : questionList) {
			questionIds.add(question .getTestQuestionId());
		}
		test.setQuestionIds(questionIds);
		
		List<Test> optionList = testMapper.getOption(test);
		
		Map<Integer, List<Test>> optionMap=new HashMap<>();
		for (Test option : optionList) {
		    Integer questionId = option.getTestQuestionId();
		    if (!optionMap.containsKey(questionId)) {
		        optionMap.put(questionId, new ArrayList<>());
		    }
		    optionMap.get(questionId).add(option);
		}
		    List<Map<String, Object>> resultList = new ArrayList<>();
		    for (Test question : questionList) {
		        Map<String, Object> qMap = new HashMap<>();
		        qMap.put("question", question );
		        List<Test> options = optionMap.get(question.getTestQuestionId());
		        qMap.put("options", options);
		        resultList.add(qMap);
		    }
		return resultList;
	}
	
	
	@Override
	public Test getResultTopCategory(Test test) {
		try {
			Test topCategory = testMapper.getResultTopCategory(test);
			if (topCategory != null && topCategory.getFileId() != null) {
			    PostFile file = fileMapper.getFileByFileId(topCategory.getFileId());
			    topCategory.setPostFile(file); // ← Test 클래스에 PostFile file 필드가 있어야 함
			}
		return topCategory;
		}catch(Exception e) {
			log.error("결과 조회 실패", e);
			throw new HException("결과 조회 실패",e);
		}	
	}

	
}

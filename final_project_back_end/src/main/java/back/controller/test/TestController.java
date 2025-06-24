package back.controller.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.model.common.PostFile;
import back.model.test.Test;
import back.service.test.TestService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {
	@Autowired
	private TestService testService;
	private PostFile postfile;
	
	@PostMapping("/questionOption.do")
	public ResponseEntity<?> getQuestionOption(@RequestBody Test test){
		List<Map<String, Object>> questionOptionList  = testService.getQuestionOption(test);
		return ResponseEntity.ok(new ApiResponse<>(true,"조회성공", questionOptionList ));
	}
	
	@PostMapping("/result.do")
	public ResponseEntity<?> getResultTopCategory(@RequestBody Test test){
		Test topCategory = testService.getResultTopCategory(test);
		return ResponseEntity.ok(new ApiResponse<>(true,"조회성공", topCategory));
	}
}

package back.controller.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // AuthenticationPrincipal 임포트
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.model.common.CustomUserDetails; // CustomUserDetails 임포트
import back.model.home.Home;
import back.service.home.HomeService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/home")
@Slf4j
public class HomeController {

	@Autowired
	private HomeService homeService;

	@PostMapping("/animal.do")
	public ResponseEntity<?> getAnimalList(
			@RequestBody Home home,
			@AuthenticationPrincipal CustomUserDetails userDetails // 현재 로그인된 사용자 정보 주입
	) {
		String currentUserId = null;
		if (userDetails != null) {
			currentUserId = userDetails.getUsername(); // CustomUserDetails의 getUsername()이 usersId를 반환
		} else {
			log.warn("getAnimalList: 인증된 사용자 정보(userDetails)가 null입니다. 익명 요청이거나 Spring Security 설정 문제일 수 있습니다.");
		}
		home.setUsersId(currentUserId); // Home 객체에 현재 사용자 ID를 설정

		log.info("getAnimalList 요청: " + home.toString());
		log.info("설정된 usersId: " + home.getUsersId()); // 설정된 usersId 값 다시 확인
		List<Home> animalList = homeService.getAnimalList(home);
		return ResponseEntity.ok(new ApiResponse<>(true, "동물 목록 조회 성공", animalList));
	}
	
	@PostMapping("/plant.do")
	public ResponseEntity<?> getPlantList(
			@RequestBody Home home,
			@AuthenticationPrincipal CustomUserDetails userDetails // 현재 로그인된 사용자 정보 주입
	) {
		String currentUserId = null;
		if (userDetails != null) {
			currentUserId = userDetails.getUsername(); // CustomUserDetails의 getUsername()이 usersId를 반환
		} else {
			log.warn("getPlantList: 인증된 사용자 정보(userDetails)가 null입니다. 익명 요청이거나 Spring Security 설정 문제일 수 있습니다.");
		}
		home.setUsersId(currentUserId); // Home 객체에 현재 사용자 ID를 설정

		log.info("getPlantList 요청: " + home.toString());
		log.info("설정된 usersId: " + home.getUsersId()); // 설정된 usersId 값 다시 확인
		List<Home> plantList = homeService.getPlantList(home);
		return ResponseEntity.ok(new ApiResponse<>(true, "식물 목록 조회 성공", plantList));
	}

}

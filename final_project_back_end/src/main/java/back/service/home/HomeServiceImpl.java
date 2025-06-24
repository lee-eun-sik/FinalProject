package back.service.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.mapper.home.HomeMapper;
import back.model.common.PostFile;
import back.model.home.Home;
import back.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeServiceImpl implements HomeService {
	@Autowired
	private final HomeMapper homeMapper;
	@Autowired
	private final FileService fileService; // 현재 이 서비스에서는 직접 사용되지 않지만, 기존 코드 유지
	@Autowired
	private FileMapper fileMapper;

	@Override
	public List<Home> getAnimalList(Home home) { // 파라미터 이름을 homeCriteria로 변경하여 혼동 방지
		try {
			List<Home> animalList = homeMapper.getAnimalList(home);

			// 조회된 동물 목록이 비어있지 않다면 각 동물 객체에 파일 정보 설정
			if (animalList != null && !animalList.isEmpty()) {
				for (Home animalItem : animalList) { // 리스트의 각 동물 객체 순회
					// 각 animalItem의 fileId를 가져옴
					// Home 모델의 getFileId()가 int를 반환한다고 가정
					int animalFileId = animalItem.getFileId();

					if (animalFileId != 0) { // fileId가 유효한 값(0이 아님)일 경우에만 파일 조회 시도
						PostFile profileFile = fileMapper.getFileByFileId(animalFileId);
//&& "ANI".equals(profileFile.getPostFileCategory())
						// 파일을 성공적으로 가져왔고, 해당 파일의 카테고리가 'ANI'인 경우에만 설정
						if (profileFile != null ) {
							animalItem.setPostFiles(List.of(profileFile)); // 현재 animalItem에 PostFile 리스트 설정
						} else {
							// 파일이 없거나 카테고리가 'ANI'가 아닌 경우
							animalItem.setPostFiles(null); // null 또는 빈 리스트로 설정하여 이미지 없음 처리
						}
					} else {
						// animalFileId가 0인 경우 (파일이 연결되지 않은 경우)
						animalItem.setPostFiles(null); // null 또는 빈 리스트로 설정
					}
				}
			}
			return animalList; // 파일 정보가 추가된 동물 목록 반환
		} catch (Exception e) {
			log.error("동물 목록 조회 실패", e);
			throw new HException("동물 목록 조회 실패", e);
		}
	}

	@Override
	public List<Home> getPlantList(Home home) { // 파라미터 이름을 homeCriteria로 변경하여 혼동 방지
		try {
			List<Home> plantList = homeMapper.getPlantList(home);

			// 조회된 식물 목록이 비어있지 않다면 각 식물 객체에 파일 정보 설정
			if (plantList != null && !plantList.isEmpty()) {
				for (Home plantItem : plantList) { // 리스트의 각 식물 객체 순회
					// 각 plantItem의 fileId를 가져옴
					int plantFileId = plantItem.getFileId();

					if (plantFileId != 0) { // fileId가 유효한 값(0이 아님)일 경우에만 파일 조회 시도
						PostFile profileFile = fileMapper.getFileByFileId(plantFileId);
// && "PLA".equals(profileFile.getPostFileCategory())
						// 파일을 성공적으로 가져왔고, 해당 파일의 카테고리가 'P'인 경우에만 설정
						if (profileFile != null) { // 식물 카테고리 'P'
							plantItem.setPostFiles(List.of(profileFile)); // 현재 plantItem에 PostFile 리스트 설정
						} else {
							// 파일이 없거나 카테고리가 'P'가 아닌 경우
							plantItem.setPostFiles(null); // null 또는 빈 리스트로 설정
						}
					} else {
						// plantFileId가 0인 경우
						plantItem.setPostFiles(null); // null 또는 빈 리스트로 설정
					}
				}
			}
			return plantList; // 파일 정보가 추가된 식물 목록 반환
		} catch (Exception e) {
			log.error("식물 목록 조회 실패", e);
			throw new HException("식물 목록 조회 실패");
		}
	}
}
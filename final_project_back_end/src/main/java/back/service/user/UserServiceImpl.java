package back.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.mapper.user.UserMapper;
import back.model.common.PostFile;
import back.model.user.User;
import back.service.file.FileService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
    private UserMapper userMapper;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private FileMapper fileMapper;
	@Autowired
	private FileService fileService;


    
    @Override
	@Transactional
	public boolean validateUser(User user) {
    	
    	try {
            User dbUser = userMapper.getUserById(user.getUsersId());
            if (dbUser == null) return false;
            
            String encryptedPassword = passwordEncoder.encode(user.getUsersPassword());
            return passwordEncoder.matches(dbUser.getUsersPassword(), encryptedPassword);
        } catch (Exception e) {
        	log.error("로그인 실패", e);
			throw new HException("로그인 실패", e);
        }
	}

    @Override
	@Transactional
    public boolean registerUser(User user) {

        try {
            String password = user.getUsersPassword();
            user.setUsersPassword(password != null ? passwordEncoder.encode(password) : null);
            return userMapper.registerUser(user) > 0;
        } catch (Exception e) {
        	log.error("회원가입 실패", e);
        	e.printStackTrace();
			throw new HException("회원가입 실패", e);
        }
    }
 // 사용자 ID로 사용자 정보 조회
 	@Override
 	@Transactional
 	public User getUserById(String usersId) {
 		try {
 			User user = userMapper.getUserById(usersId);
 			if (user != null) {
 				int usersFileId = user.getUsersFileId(); // User 객체에 있는 usersFileId 가져옴
 				if (usersFileId != 0) {
 					PostFile profileFile = fileMapper.getFileByFileId(usersFileId);
 					if (profileFile != null) {
 						user.setPostFiles(List.of(profileFile));
 					} else {
 						log.warn("사용자 ID: {} 의 프로필 파일 ID: {} 에 해당하는 파일을 찾을 수 없습니다.", usersId, usersFileId);
 						user.setPostFiles(null); // 찾지 못했으면 리스트를 null 또는 비어있는 리스트로 설정
 					}
 				} else {
 					log.info("사용자 ID: {} 에게 연결된 프로필 파일이 없습니다 (usersFileId = 0).", usersId);
 					user.setPostFiles(null); // 프로필 파일 ID가 0이면 리스트를 null 또는 비어있는 리스트로 설정
 				}
 			}
 			return user;
 		} catch (Exception e) {
 			log.error("사용자 ID로 사용자 정보 조회 실패: {}", usersId, e); // 로그 메시지 수정
 			throw new HException("사용자 정보 조회 실패", e); // 예외 메시지 수정
 		}
 	}
 	
 	//회원정보 수정
	@Override
	@Transactional
	public boolean updateUser(User user) {
		try {
			User existingUser = userMapper.getUserById(user.getUsersId());
			if (existingUser == null) {
				throw new HException("사용자 정보를 찾을 수 없습니다.");
			}

			// 비밀번호 업데이트 로직
			String inputPassword = user.getUsersPassword();
			if (inputPassword == null || inputPassword.isEmpty()) {
				throw new HException("비밀번호는 필수로 입력해야 합니다.");
			}
			if (!passwordEncoder.matches(inputPassword, existingUser.getUsersPassword())) {
				user.setUsersPassword(passwordEncoder.encode(inputPassword));
			} else {
				user.setUsersPassword(null);
			}

			// 프로필 이미지 ID 결정 로직
			// DB에 저장된 현재 프로필 파일 ID를 가져옴 (이것이 삭제되거나 교체될 대상)
			int originalProfileFileId = existingUser.getUsersFileId();
			// 최종적으로 User 객체에 설정될 파일 ID (기존 ID로 초기화)
			int newProfileFileIdToSet = originalProfileFileId;

			// 명시적 프로필 삭제 요청 (remainingFileIds가 "0"일 때)
			if ("0".equals(user.getRemainingFileIds())) {
				newProfileFileIdToSet = 0; // 연결 끊기
			}
			// 새로운 프로필 파일이 업로드된 경우 (user.getFiles()가 존재하고 비어있지 않을 때)
			else if (user.getFiles() != null && !user.getFiles().isEmpty()) {
				PostFile newProfileFile = new PostFile();
				newProfileFile.setFiles(user.getFiles());
				newProfileFile.setCreateId(user.getUsersId());
				newProfileFile.setPostFileCategory("PEO");

				Map<String, Object> uploadResult = fileService.insertFiles(newProfileFile);
				if (!(boolean) uploadResult.get("result")) {
					log.error("새 프로필 파일 업로드 및 DB 추가 실패: {}", uploadResult.get("message"));
					throw new HException("새 프로필 파일 업로드 실패: " + uploadResult.get("message"));
				}

				if (uploadResult.containsKey("fileId") && uploadResult.get("fileId") instanceof Integer) {
					Integer uploadedFileId = (Integer) uploadResult.get("fileId");
					if (uploadedFileId != null && uploadedFileId != 0) {
						newProfileFileIdToSet = uploadedFileId; // 새 파일 ID로 설정
					} else {
						throw new HException("새 프로필 파일 업로드 성공했으나 ID 반환 실패.");
					}
				} else {
					throw new HException("새 프로필 파일 업로드 성공했으나 파일 ID를 Map에서 찾을 수 없습니다.");
				}
			}

			// POST_FILE 테이블에서 이전 프로필 사진 논리적 삭제 처리
			if (originalProfileFileId != 0 && newProfileFileIdToSet != originalProfileFileId) {
				PostFile fileToDelete = new PostFile();
				fileToDelete.setPostFileId(originalProfileFileId); // 삭제 대상은 기존 파일 ID
				fileToDelete.setUpdateId(user.getUpdateId() != null ? user.getUpdateId() : user.getUsersId());
				fileToDelete.setPostFileCategory("PEO"); // 카테고리도 삭제 조건에 사용 (필요시)

				boolean deleteOldFileResult = fileMapper.deleteFile(fileToDelete) > 0;
				if (!deleteOldFileResult) {
					log.warn("기존 프로필 파일 DB 논리적 삭제 실패 : {}", originalProfileFileId);
				} else {
					log.info("기존 프로필 파일 DB 논리적 삭제 성공 : {}", originalProfileFileId);
				}
			}

			// 최종 사용자 정보 업데이트 (USERS 테이블)
			user.setUsersFileId(newProfileFileIdToSet); // 최종 결정된 프로필 파일 ID 설정

			boolean result = userMapper.updateUser(user) > 0;
			if (!result) {
				throw new HException("사용자 정보 최종 업데이트 실패");
			}
			return result;

		} catch (HException e) {
			log.error("사용자 수정 중 비즈니스 로직 오류: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error("사용자 수정 실패: {}", e.getMessage(), e);
			throw new HException("사용자 수정 실패", e);
		}
	}
	
    
    @Override
	public boolean deleteUser(User user) {
    	try {
            String password = user.getUsersPassword();
            user.setUsersPassword(password != null ? passwordEncoder.encode(password) : null);
            return userMapper.deleteUser(user) > 0;
        } catch (Exception e) {
            log.error("사용자 탈퇴 중 오류", e);
            throw new HException("사용자 탈퇴 실패", e);
        }
	}

	@Override
	public List<User> getUserList(User user) {
		try {
			int page = user.getPage();
			int size = user.getSize();
			
			int totalCount = userMapper.getTotalUserCount(user);
			int totalPages = (int) Math.ceil((double) totalCount / size);
			
			int startRow = (page - 1) * size + 1;
			int endRow = page *size;
			
			user.setTotalCount(totalCount);
			user.setTotalPages(totalPages);
			user.setStartRow(startRow);
			user.setEndRow(endRow);
			
			List list = userMapper.getUserList(user);
			
			return list;
		} catch (Exception e) {
			log.error("유저 목록 조회 실패", e);
			throw new HException("유저 목록 조회 실패", e);
		}
	}

	@Override
	public boolean userM(User user) {
		try {
            String password = user.getUsersPassword();
            user.setUsersPassword(password != null ? passwordEncoder.encode(password) : null);
            return userMapper.userM(user) > 0;
        } catch (Exception e) {
            log.error("회원관리 중 오류", e);
            throw new HException("회원관리 실패", e);
        }
	}

	@Override
	public boolean usersIdCheck(User user) {
		try {
            int count = userMapper.usersIdCheck(user);
            return count > 0;
        } catch (Exception e) {
            log.error("아이디 중복 체크 중 오류 발생!", e);
            throw new HException("아이디 중복 체크 실패", e);
        }
	}

	@Override
	public boolean isUserIdDuplicate(String usersId) {
		try {
			int count = userMapper.checkUserIdDuplicate(usersId);
			return count > 0; // 이미 DB에 존재하면 true, 존재하지 않으면 false
		} catch (Exception e) {
			log.error("아이디 중복 체크 중 오류", e);
			throw new HException("아이디 중복 체크 실패", e);
		}
	}
	
	 @Override
	 public List<User> findUsersByInfo(String email) {
	     try {
	         return userMapper.selectUsersByEmail(email);
	     } catch (Exception e) {
	         log.error("이메일로 사용자 목록 조회 중 오류", e);
	         throw new HException("사용자 조회 실패", e);
	     }
	 }

	 @Override
	 public User findUserByUserIdAndEmail(String usersId, String usersEmail) {
	     try {
	         Map<String, Object> params = new HashMap<>();
	         params.put("usersId", usersId);
	         params.put("usersEmail", usersEmail);
	         return userMapper.findUserByUserIdAndEmail(params);
	     } catch (Exception e) {
	         log.error("아이디와 이메일로 사용자 조회 중 오류", e);
	         throw new HException("사용자 조회 실패", e);
	     }
	 }
	
	 @Override
	 public boolean updatePassword(String usersId, String encodedPassword) {
	     try {
	    	 Map<String, Object> params = new HashMap<>();
	         params.put("usersId", usersId);
	         params.put("encodedPassword", encodedPassword);
	         return userMapper.updatePassword(params) > 0;
	     } catch (Exception e) {
	         log.error("비밀번호 업데이트 중 오류", e);
	         throw new HException("비밀번호 업데이트 실패", e);
	     }
	 }
	
	 @Override
	 public boolean resetPassword(User user) {
	     try {
	         User userCheck = userMapper.findByUserId(user.getUsersId());
	         if (userCheck == null) return false;
	         
	         log.info(">>>> userCheck체크 결과", userCheck);
	         
	         String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
	         log.info(">>>> encodedPassword", encodedPassword);
	         
	         Map<String, Object> params = new HashMap<>();
	         params.put("usersId", user.getUsersId());
	         params.put("updateId", user.getUsersId());
	         params.put("encodedPassword", encodedPassword);
	         


	         return userMapper.updatePassword(params) > 0;
	     } catch (Exception e) {
	         log.error("비밀번호 재설정 중 오류", e);
	         throw new HException("비밀번호 재설정 실패", e);
	     }
	 }
	
	
	@Override
	public boolean isEmailRegistered(String email) {
		try {
		    int count = userMapper.isEmailRegistered(email);
		    return count > 0;
		} catch (Exception e) {
	    	log.error("이메일 등록 여부 중 확인 실패했습니다.");
	    	throw new HException("이메일 등록 여부 확인 실패");
	    }
	}
	@Override
	public User findByEmail(String email) {
	    try {
	        List<User> users = userMapper.selectUsersByEmail(email);
	        return users != null && !users.isEmpty() ? users.get(0) : null;
	    } catch (Exception e) {
	        log.error("이메일로 사용자 조회 중 오류", e);
	        throw new HException("이메일 조회 실패", e);
	    }
	}
}
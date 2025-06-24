package back.mapper.combo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.combo.Combo;
import back.model.combo.CommonCode;
import back.model.combo.GroupCode;

@Mapper
public interface ComboMapper {
	
	public int create(Combo combo);
	
	public int update(Combo combo);
	
	public int delete(Combo combo);
	
	public List<Combo> list();
	
	// 활성화된 그룹코드 조회
    List<GroupCode> selectActiveGroups();

    // 특정 그룹코드에 속하는 공통코드 조회
    List<CommonCode> selectCodesByGroupId(String groupId);
	
	
}

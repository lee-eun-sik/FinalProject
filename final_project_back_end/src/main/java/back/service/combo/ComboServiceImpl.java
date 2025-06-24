package back.service.combo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.combo.ComboMapper;
import back.mapper.file.FileMapper;
import back.model.combo.Combo;
import back.model.combo.CommonCode;
import back.model.combo.GroupCode;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ComboServiceImpl implements ComboService {
	
    @Autowired
    private ComboMapper comboMapper;
    
	
    @Override
	@Transactional
	public boolean create(Combo combo)  {
       
    	boolean result = comboMapper.create(combo) > 0;
		
		return result;
    
    }
	
	@Override
	@Transactional
	public boolean update(Combo combo) {
        try {
            boolean result = comboMapper.update(combo) > 0;
                     
            return result;
        } catch (Exception e) {
            log.error("게시글 수정 실패", 0);
            throw new HException("게시글 수정 실패", e);
        }
    }
	
	@Override
	@Transactional
	public boolean delete(Combo combo) {
		try {
			return comboMapper.delete(combo) > 0;
		} catch (Exception e) {
			log.error("게시글 삭제 실패", e);
			throw new HException("게시글 삭제 실패", e);
		}
    }

	@Override
	public List<Combo> getList(Combo combo) {
		return comboMapper.list();
	}

	@Override
	public List<GroupCode> getActiveGroupsWithCodes() {
		List<GroupCode> groups = comboMapper.selectActiveGroups();

        for (GroupCode group : groups) {
            List<CommonCode> codes = comboMapper.selectCodesByGroupId(group.getGroupId());
            group.setCommonCodes(codes);
        }

        return groups;
	}

	@Override
	public List<CommonCode> getListByGroupId(String groupId) {
		
            List<CommonCode> codes = comboMapper.selectCodesByGroupId(groupId);
		return codes;
	}


}
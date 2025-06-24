package back.service.combo;

import java.io.IOException;
import java.util.List;

import back.model.combo.Combo;
import back.model.combo.CommonCode;
import back.model.combo.GroupCode;

public interface ComboService {
    
    public boolean create(Combo combo);
    
    public boolean update(Combo combo);
    
    public boolean delete(Combo combo);
    
    public List getList(Combo combo);
    
    public List<GroupCode> getActiveGroupsWithCodes();
    
    public List<CommonCode> getListByGroupId(String groupId);


}
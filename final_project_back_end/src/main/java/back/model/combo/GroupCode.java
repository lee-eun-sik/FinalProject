package back.model.combo;

import java.util.List;

public class GroupCode {
    private String groupId;
    private String groupName;
    private List<CommonCode> commonCodes;  // 자식코드 리스트

    // getter / setter
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public List<CommonCode> getCommonCodes() { return commonCodes; }
    public void setCommonCodes(List<CommonCode> commonCodes) { this.commonCodes = commonCodes; }
}

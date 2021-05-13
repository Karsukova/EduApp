package space.karsukova.educateapp.utils;

public class Groups {
    private String gropTitle;
    private String groupDescription;
    private String createdBy;
    private String groupIcon;
    private String g_timestamp;
    private String groupId;

    public Groups(){

    }

    public Groups(String groupId, String groupTitle, String description,  String groupIcon,  String g_timestamp, String createdBy) {
        this.gropTitle = groupTitle;
        this.groupDescription = description;
        this.createdBy = createdBy;
        this.groupIcon = groupIcon;
        this.g_timestamp = g_timestamp;
        this.groupId = groupId;
    }

    public void setGropTitle(String gropTitle) {
        this.gropTitle = gropTitle;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public void setG_timestamp(String g_timestamp) {
        this.g_timestamp = g_timestamp;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGropTitle() {
        return gropTitle;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public String getG_timestamp() {
        return g_timestamp;
    }

    public String getGroupId() {
        return groupId;
    }
}

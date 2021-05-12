package space.karsukova.educateapp.utils;

public class Groups {
    private String groupName, description;

    public Groups(){

    }

    public Groups(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package bj.albon.arith.config.parser.api.model;

import java.util.Date;
import java.util.List;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午5:24
 */
public class AppData {
    private int id;
    private String code;
    private String name;
    private List<String> developer;
    private List<String> owner;
    private List<String> mailGroup;
    private String groupCode;
    private String appKey;
    private String creator;
    private String status;
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getDeveloper() {
        return developer;
    }

    public void setDeveloper(List<String> developer) {
        this.developer = developer;
    }

    public List<String> getOwner() {
        return owner;
    }

    public void setOwner(List<String> owner) {
        this.owner = owner;
    }

    public List<String> getMailGroup() {
        return mailGroup;
    }

    public void setMailGroup(List<String> mailGroup) {
        this.mailGroup = mailGroup;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}

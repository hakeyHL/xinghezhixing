package hasoffer.core.persistence.po.urm;

/**
 * Created by hs on 2016/6/17.
 */

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UrmUser implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String thirdId;
    private String userToken;
    private String userName;
    private String thirdToken;
    private String thirdPlatform;
    private String avatarPath;
    private Date createTime;
    private String telephone;


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userToken != null ? userToken.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (thirdToken != null ? thirdToken.hashCode() : 0);
        result = 31 * result + (thirdPlatform != null ? thirdPlatform.hashCode() : 0);
        result = 31 * result + (avatarPath != null ? avatarPath.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (thirdId != null ? thirdId.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmUser user = (UrmUser) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (userToken != null ? !userToken.equals(user.userToken) : user.userToken != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (createTime != null ? !createTime.equals(user.createTime) : user.createTime != null) return false;
        if (thirdToken != null ? !thirdToken.equals(user.thirdToken) : user.thirdToken != null) return false;
        if (avatarPath != null ? !avatarPath.equals(user.avatarPath) : user.avatarPath != null) return false;
        if (createTime != null ? !createTime.equals(user.createTime) : user.createTime != null) return false;
        if (thirdId != null ? !thirdId.equals(user.thirdId) : user.thirdId != null) return false;
        return (telephone != null ? !telephone.equals(user.telephone) : user.telephone != null);
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThirdToken() {
        return thirdToken;
    }

    public void setThirdToken(String thirdToken) {
        this.thirdToken = thirdToken;
    }

    public String getThirdPlatform() {
        return thirdPlatform;
    }

    public void setThirdPlatform(String thirdPlatform) {
        this.thirdPlatform = thirdPlatform;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thridId) {
        this.thirdId = thridId;
    }
}

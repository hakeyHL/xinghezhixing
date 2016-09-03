package hasoffer.api.controller.vo;

import java.math.BigDecimal;

/**
 * Created by hs on 2016/6/20.
 */
public class UserVo {
    private  String userName;
    private String thirdId;
    private  String userIcon;
    private String token;
    private String platform;
    private String telephone;
    private  String name;
    private BigDecimal conis;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getConis() {
        return conis;
    }

    public void setConis(BigDecimal conis) {
        this.conis = conis;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

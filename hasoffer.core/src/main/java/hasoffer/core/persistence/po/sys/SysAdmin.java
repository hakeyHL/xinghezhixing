package hasoffer.core.persistence.po.sys;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.persistence.enums.AdminType;

import javax.persistence.*;
import java.util.Date;

/**
 * Date : 2015/12/27
 * Function :
 */
@Entity
public class SysAdmin implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createTime = TimeUtils.nowDate();

    private boolean valid;

    private String uname;

    private String password;

    private String email;

    private Date lastLoginTime = TimeUtils.nowDate();

    private String ukey;

    @Enumerated(EnumType.STRING)
    private AdminType type;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AdminType getType() {
        return type;
    }

    public void setType(AdminType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysAdmin sysAdmin = (SysAdmin) o;

        if (valid != sysAdmin.valid) return false;
        if (id != null ? !id.equals(sysAdmin.id) : sysAdmin.id != null) return false;
        if (createTime != null ? !createTime.equals(sysAdmin.createTime) : sysAdmin.createTime != null) return false;
        if (uname != null ? !uname.equals(sysAdmin.uname) : sysAdmin.uname != null) return false;
        if (password != null ? !password.equals(sysAdmin.password) : sysAdmin.password != null) return false;
        if (email != null ? !email.equals(sysAdmin.email) : sysAdmin.email != null) return false;
        if (lastLoginTime != null ? !lastLoginTime.equals(sysAdmin.lastLoginTime) : sysAdmin.lastLoginTime != null)
            return false;
        if (ukey != null ? !ukey.equals(sysAdmin.ukey) : sysAdmin.ukey != null) return false;
        return type == sysAdmin.type;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (valid ? 1 : 0);
        result = 31 * result + (uname != null ? uname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (lastLoginTime != null ? lastLoginTime.hashCode() : 0);
        result = 31 * result + (ukey != null ? ukey.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

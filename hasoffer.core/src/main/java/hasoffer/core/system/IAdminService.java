package hasoffer.core.system;

import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.persistence.po.sys.SysCheckTask;

import javax.security.auth.login.LoginException;

/**
 * Date : 2015/12/27
 * Function :
 */
public interface IAdminService {

    SysAdmin login(String uname, String password) throws LoginException;

    SysAdmin findAdminByKey(String ukey);

    void createWebsites();

    SysCheckTask findLatestCheckTask(long adminId);

    void logout(SysAdmin admin);
}

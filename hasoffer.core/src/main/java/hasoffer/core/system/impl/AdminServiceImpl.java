package hasoffer.core.system.impl;

import hasoffer.base.model.Website;
import hasoffer.base.utils.*;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.app.AppWebsite;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.persistence.po.sys.SysCheckTask;
import hasoffer.core.persistence.po.sys.updater.SysAdminUpdater;
import hasoffer.core.system.IAdminService;
import hasoffer.fetch.helper.WebsiteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2015/12/27
 * Function :
 */
@Service
public class AdminServiceImpl implements IAdminService {

    private static final String Q_ADMIN_BY_NAME =
            "SELECT t FROM SysAdmin t " +
                    " WHERE t.uname = ?0 ";

    private static final String Q_ADMIN_BY_KEY =
            "SELECT t FROM SysAdmin t " +
                    " WHERE t.ukey = ?0 ";

    private static final String Q_CHECKTASK =
            "SELECT t FROM SysCheckTask t " +
                    " WHERE t.adminId = ?0 " +
                    " ORDER BY t.createTime DESC ";

    @Resource
    IDataBaseManager dbm;
    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public void logout(SysAdmin admin) {
        // 设置当前时间md5加密值，防止登出后...
        String _ukey = HexDigestUtil.md5(String.valueOf(TimeUtils.now()));

        SysAdminUpdater sysAdminUpdater = new SysAdminUpdater(admin.getId());
        sysAdminUpdater.getPo().setLastLoginTime(TimeUtils.nowDate());
        sysAdminUpdater.getPo().setUkey(_ukey);

        dbm.update(sysAdminUpdater);
    }

    @Override
    public SysCheckTask findLatestCheckTask(long adminId) {
        List<SysCheckTask> tasks = dbm.query(Q_CHECKTASK, Arrays.asList(adminId));

        if (ArrayUtils.hasObjs(tasks)) {
            return tasks.get(0);
        }

        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createWebsites() {
        List<AppWebsite> appWebsites = dbm.query("SELECT t FROM AppWebsite t");

        Map<Website, AppWebsite> siteMap = new HashMap<Website, AppWebsite>();
        for (AppWebsite appWebsite : appWebsites) {
            siteMap.put(appWebsite.getWebsite(), appWebsite);
        }

        for (Website website : Website.values()) {
            if (siteMap.get(website) != null) {
                continue;
            }
            AppWebsite appWebsite = new AppWebsite(website, WebsiteHelper.getPackage(website));
            dbm.create(appWebsite);
        }
    }

    @Override
    public SysAdmin findAdminByKey(String ukey) {

        if (StringUtils.isEmpty(ukey)) {
            return null;
        }

        return dbm.querySingle(Q_ADMIN_BY_KEY, Arrays.asList(ukey));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public SysAdmin login(String uname, String password) throws LoginException {
        logger.debug(uname + " try login.");

        SysAdmin sysAdmin = dbm.querySingle(Q_ADMIN_BY_NAME, Arrays.asList(uname));

        if (sysAdmin == null) {
            throw new LoginException("用户不存在");
        }

        if (!sysAdmin.getPassword().equals(password)) {
            throw new LoginException("密码不正确");
        }

        if (!sysAdmin.isValid()) {
            throw new LoginException("用户已失效");
        }

        String ukey = IDUtil.uuid();

        sysAdmin.setUkey(ukey);

        SysAdminUpdater sysAdminUpdater = new SysAdminUpdater(sysAdmin.getId());
        sysAdminUpdater.getPo().setLastLoginTime(TimeUtils.nowDate());
        sysAdminUpdater.getPo().setUkey(ukey);
        dbm.update(sysAdminUpdater);

        return sysAdmin;
    }
}

package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.SiteTaskDao;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.SiteTask;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.service.SiteTaskService;
import ru.alex.webapp.service.UserSiteTaskService;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserSiteTaskServiceImpl extends GenericServiceImpl<UserSiteTask, Long> implements UserSiteTaskService {
    private static final Logger logger = LoggerFactory.getLogger(UserSiteTaskServiceImpl.class);
    @Autowired
    UserSiteTaskDao userSiteTaskDao;
    @Autowired
    UserTaskTimeDao userTaskTimeDao;
    @Autowired
    SiteTaskDao siteTaskDao;

    @Autowired
    SiteTaskService siteTaskService;

    @Override
    protected GenericDao<UserSiteTask, Long> getDao() {
        return userSiteTaskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(UserSiteTask entity) throws Exception {
        logger.debug("update userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        userSiteTaskDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(UserSiteTask entity) throws Exception {
        logger.debug("add userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getSiteTask() == null || entity.getUserByUsername() == null
                || entity.getStatus() == null || entity.getUpdateTime() == null
                || entity.getCreateTime() == null || entity.getDeleted() == null)
            throw new Exception("Wrong params");
        entity.setDeleted(false);
        userSiteTaskDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserSiteTask entity) throws Exception {
        logger.debug("remove userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());

        Map<String, Object> params = new HashMap<>(1);
        params.put("userSiteTaskId", entity.getId());
        Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.BY_USER_SITE_TASK, params);
        logger.debug("remove BY_USER_SITE_TASK userTaskTimeList={}", userTaskTimeList);
        if (userTaskTimeList == null || userTaskTimeList.size() == 0) {
            entity = userSiteTaskDao.merge(entity);
            entity.getSiteTask().getUserSiteTaskList().remove(entity);
            entity.getUserByUsername().getUserSiteTasksByUsername().remove(entity);
            userSiteTaskDao.remove(entity);
        } else {
            entity.setDeleted(true);
            userSiteTaskDao.merge(entity);
        }
    }

    @Override
    public List<UserSiteTask> getAllCurrentTime() throws Exception {
        logger.debug("remove getAllCurrentTime");
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_CURRENT_TIME);
        logger.debug("remove getAllCurrentTime userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, Task task) throws Exception {
        logger.debug("getNotDeletedUserSiteTasks site={}, task={}", site, task);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (task == null || task.getId() == null)
            throw new IllegalArgumentException("Wrong task");
        Map<String, Object> params = new HashMap<>(2);
        params.put("siteId", site.getId());
        params.put("taskId", task.getId());
        List<SiteTask> siteTaskList = siteTaskDao.findWithNamedQuery(SiteTask.BY_SITE_TASK_NOT_DELETED, params);
        logger.debug("getNotDeletedUserSiteTasks siteTaskList={}", siteTaskList);
        if (siteTaskList == null || siteTaskList.size() == 0)
            throw new Exception("Can't find SiteTask with siteId=" + site.getId() + ", taskId=" + task.getId());
        SiteTask siteTask = siteTaskList.get(0);
        params = new HashMap<>(1);
        params.put("siteTaskId", siteTask.getId());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_NOT_DELETED_BY_SITE_TASK, params);
        logger.debug("getNotDeletedUserSiteTasks userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public UserSiteTask getByUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("getByUserSiteTask user={}, site={}, task={}", user, site, task);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (task == null || task.getId() == null)
            throw new IllegalArgumentException("Wrong task");
        Map<String, Object> params = new HashMap<>(3);
        params.put("username", user.getUsername());
        params.put("siteId", site.getId());
        params.put("taskId", task.getId());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.BY_USER_SITE_TASK, params);
        logger.debug("getByUserSiteTask userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList == null || userSiteTaskList.size() == 0 ? null : userSiteTaskList.get(0);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("addUserSiteTask user={}, site={}, task={}", user, site, task);
        SiteTask siteTask = siteTaskService.getBySiteTask(site, task);
        logger.debug("addUserSiteTask siteTask={}", siteTask);
        if (siteTask == null)
            throw new Exception("Site task doesn't exist with siteId=" + site.getId() + " taskId=" + task.getId());
        UserSiteTask userSiteTask = getByUserSiteTask(user, site, task);
        Date now = new Date();
        if (userSiteTask == null) {
            userSiteTask = new UserSiteTask();
            userSiteTask.setSiteTask(siteTask);
            userSiteTask.setUserByUsername(user);
            userSiteTask.setUpdateTime(now);
            userSiteTask.setCreateTime(now);
            userSiteTask.setStatus(TaskStatus.UNKNOWN.getStatusStr());
            userSiteTask.setDeleted(false);
            add(userSiteTask);
        } else {
            if (userSiteTask.getDeleted()) {
                userSiteTask.setUpdateTime(now);
                userSiteTask.setDeleted(true);
                userSiteTaskDao.merge(userSiteTask);
            } else {
                throw new Exception("Can't add, userSiteTask exists already with siteId=" + site.getId() + ", taskId=" + task.getId() + ", username" + user.getUsername());
            }
        }
    }

    @Override
    public void removeUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("removeUserSiteTask user={}, site={}, task={}", user, site, task);
        UserSiteTask userSiteTask = getByUserSiteTask(user, site, task);
        if (userSiteTask == null || userSiteTask.getDeleted()) {
            throw new Exception("Can't remove, userSiteTask is removed, siteId=" + site.getId() + ", taskId=" + task.getId() + ", username" + user.getUsername());
        }
        remove(userSiteTask);
    }
}

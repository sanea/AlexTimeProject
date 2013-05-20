package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.dao.TaskTimeDao;
import ru.alex.webapp.dao.UserActionDao;
import ru.alex.webapp.dao.TaskTimeSeqDao;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskTime;
import ru.alex.webapp.service.TaskService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskServiceImpl extends GenericServiceImpl<Task, Long> implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskTimeDao taskTimeDao;
    @Autowired
    private TaskTimeSeqDao taskTimeSeqDao;
    @Autowired
    private UserActionDao userActionDao;

    @Override
    protected GenericDao<Task, Long> getDao() {
        return taskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(Task task) throws Exception {
        logger.debug("addTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        if (task.getName() == null || task.getName().equals(""))
            throw new Exception("Name can't be empty");
        if (task.getType() == null || task.getType().equals(""))
            throw new Exception("Type can't be empty");
        if (task.getPriceHour() == null)
            throw new Exception("Price can't be empty");
        if (task.getEnabled() == null || task.getIncome() == null)
            throw new Exception("enabled and income can't be empty");
        task.setDeleted(false);
        taskDao.persist(task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(Task task) throws Exception {
        logger.debug("removeTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        throwExceptionIfNotExists(task.getId());
        if (!isTaskEditable(task)) {
            throw new Exception("Task is already stated, please disable task, can't be delete");
        }

        Map<String, Object> params = new HashMap<>(1);
        params.put("taskId", task.getId());
        Collection<TaskTime> taskTimeList = taskTimeDao.findWithNamedQuery(TaskTime.BY_TASK_ID, params);
        logger.debug("remove BY_TASK_ID taskTimeList={}", taskTimeList);
        if (taskTimeList == null || taskTimeList.size() == 0) {
            task = taskDao.merge(task);
            taskDao.remove(task);
        } else {
            task.setName(task.getName() + REMOVED_NAME_APPEND);
            task.setDeleted(true);
            taskDao.merge(task);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(Task task) throws Exception {
        logger.debug("editTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        throwExceptionIfNotExists(task.getId());
        Task taskEntity = taskDao.findById(task.getId());
        if (!isTaskEditable(task)) {
            if (!taskEntity.getType().equals(task.getType()))
                throw new Exception("Can't change type of not editable task");
            if (!taskEntity.getPriceHour().equals(task.getPriceHour()))
                throw new Exception("Can't change price of not editable task");
            if (!taskEntity.getDeleted().equals(task.getDeleted()))
                throw new Exception("Can't delete task task");
            if (!taskEntity.getIncome().equals(task.getIncome()))
                throw new Exception("Can't change task income");
        }
        task = taskDao.merge(task);
        logger.debug("editTask merged_task={}", task);
    }

    /**
     * Returns true if task is not deleted and
     * task has no current time (user_site_task is not in progress)
     *
     * @param task
     * @return
     * @throws Exception
     */
    @Override
    public boolean isTaskEditable(Task task) throws Exception {
        logger.debug("isTaskEditable task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        throwExceptionIfNotExists(task.getId());
        boolean result;
        if (task.getDeleted()) {
            result = false;
        } else {
            Map<String, Object> params = new HashMap<>(1);
            params.put("taskId", task.getId());
            Collection<TaskTime> taskTimeList = taskTimeDao.findWithNamedQuery(TaskTime.CURRENT_BY_TASK_ID, params);
            logger.debug("isTaskEditable CURRENT_BY_TASK_ID taskTimeList={}", taskTimeList);
            result = taskTimeList == null || taskTimeList.size() == 0;
        }
        logger.debug("isTaskEditable {}", result);
        return result;
    }

    @Override
    public List<Task> getEnabledNotDeletedTasks() throws Exception {
        List<Task> taskList = taskDao.findWithNamedQuery(Task.ALL_ENABLED_NOT_DELETED);
        logger.debug("getEnabledNotDeletedTasks taskList={}", taskList);
        return taskList;
    }

}

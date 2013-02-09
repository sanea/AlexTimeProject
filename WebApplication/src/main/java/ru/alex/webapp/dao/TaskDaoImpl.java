package ru.alex.webapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alex.webapp.model.TaskEntity;
import ru.alex.webapp.model.UserTaskEntity;
import ru.alex.webapp.model.UserTaskStatusEntity;

import java.util.List;


@Repository
public class TaskDaoImpl implements TaskDao {
    private SessionFactory sessionFactory;

    @Autowired
    public TaskDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void addTak(TaskEntity task) {
        currentSession().save(task);
    }

    @Override
    public void updateTask(TaskEntity task) {
        currentSession().update(task);
    }

    @Override
    public void deleteTask(TaskEntity task) {
        currentSession().delete(task);
    }

    @Override
    public TaskEntity getTask(long id) {
        return (TaskEntity) currentSession().get(TaskEntity.class, new Long(id));
    }

    @Override
    public List<UserTaskEntity> getTasksForUser(String username) {
        return currentSession().createQuery("SELECT ut FROM UserTaskEntity ut where ut.usersByUsername.username = :username").setParameter("username", username).list();
    }

    @Override
    public UserTaskEntity getTaskForUser(String username, Long taskId) {
        return (UserTaskEntity) currentSession().createQuery("SELECT ut FROM UserTaskEntity ut where ut.usersByUsername.username = :username AND ut.taskByTaskId.id = :taskId").setParameter("username", username).setParameter("taskId", taskId).uniqueResult();
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        return currentSession().createQuery("SELECT t FROM TaskEntity t").list();
    }


    @Override
    public void addTaskStatus(UserTaskStatusEntity entity) {
        currentSession().save(entity);
    }

    @Override
    public void updateTaskStatus(UserTaskStatusEntity entity) {
        currentSession().update(entity);
    }

    @Override
    public List<UserTaskStatusEntity> getActiveTasks(String username) {
        return currentSession().createQuery("SELECT st FROM UserTaskStatusEntity st where st.usersByUsername.username = :username AND st.status = :status").setParameter("username", username).setParameter("status", UserTaskStatusEntity.TaskStatus.IN_PROGRESS.getStatus()).list();
    }

    @Override
    public List<UserTaskStatusEntity> getActiveTasks(String username, Long taskId) {
        return currentSession().createQuery("SELECT st FROM UserTaskStatusEntity st where st.usersByUsername.username = :username AND st.taskByTaskId.id = :taskId AND st.status = :status").setParameter("username", username).setParameter("taskId", taskId).setParameter("status", UserTaskStatusEntity.TaskStatus.IN_PROGRESS.getStatus()).list();
    }

    @Override
    public List<UserTaskStatusEntity> getAllTaskStatus() {
        return currentSession().createQuery("SELECT st FROM UserTaskStatusEntity st ORDER BY st.endTimestamp").list();
    }
}

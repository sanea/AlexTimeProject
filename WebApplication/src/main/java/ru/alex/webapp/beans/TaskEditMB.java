package ru.alex.webapp.beans;

import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class TaskEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TaskEditMB.class);
    private List<Task> taskList;
    private Map<Long, Boolean> taskEditable;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SessionMB sessionMB;
    private Task newTask = new Task();
    private Task selectedTask;

    @PostConstruct
    private void init() {
        initTasks();
    }

    private void initTasks() {
        try {
            taskList = taskService.findAll();
            logger.debug("initTasks taskList={}", taskList);
            taskEditable = new HashMap<>(taskList.size());
            for (Task task : taskList) {
                boolean editable = taskService.isTaskEditable(task);
                taskEditable.put(task.getId(), editable);
            }
            logger.debug("onEditInit taskEditable={}", taskEditable);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public Map<Long, Boolean> getTaskEditable() {
        return taskEditable;
    }

    public TaskType[] getTaskTypes() {
        return TaskType.values();
    }

    public Task getNewTask() {
        return newTask;
    }

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    public String getTaskTypeFormatted(String taskType) {
        return TaskType.getTypeFormatted(taskType, sessionMB.getResourceBundle());
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public void onEdit(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onEdit task={}", task);
        try {
            taskService.update(task);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Edited", task.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing task", e.toString()));
        }
        initTasks();
    }

    public void onCancel(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onCancel task={}", task);
    }

    public void addNewTaskListener(ActionEvent event) {
        logger.debug("addNewTaskListener");
        newTask = new Task();
        newTask.setDeleted(false);
        newTask.setIncome(true);
        newTask.setEnabled(true);
    }

    public void addNewTask() {
        logger.debug("addNewTask newTask={}", newTask);
        try {
            taskService.add(newTask);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Added", newTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding task", e.toString()));
        }
        initTasks();
    }

    public void removeTask() {
        logger.debug("removeTask selectedTask={}", selectedTask);
        try {
            taskService.remove(selectedTask);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Removed", selectedTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
        }
        initTasks();
    }


}


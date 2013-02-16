package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.model.Task;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class TaskDaoImpl extends GenericDaoImpl<Task, Long> implements TaskDao {


}

package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.TaskTimeSeqDao;
import ru.alex.webapp.model.TaskTimeSeq;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class TaskTimeSeqDaoImpl extends GenericDaoImpl<TaskTimeSeq, Long> implements TaskTimeSeqDao {
}

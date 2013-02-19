package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeSeqDao;
import ru.alex.webapp.model.UserTaskTimeSeq;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskTimeSeqDaoImpl extends GenericDaoImpl<UserTaskTimeSeq, Long> implements UserTaskTimeSeqDao {
}

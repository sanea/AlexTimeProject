package ru.alex.webapp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/root-context.xml", "classpath:spring/service-context.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
public class ServiceTestSuite {
    private static final Logger logger = LoggerFactory.getLogger(ServiceTestSuite.class);
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @BeforeTransaction
    public void verifyInitialDatabaseState() {
        logger.info("BeforeTransaction");
    }

    @Before
    public void setUpTestDataWithinTransaction() {
        logger.info("Before");
    }

    @After
    public void tearDownWithinTransaction() {
        logger.info("After");
    }

    @AfterTransaction
    public void verifyFinalDatabaseState() {
        logger.info("AfterTransaction");
    }

    @Test
    @Rollback(true)
    public void test() {
        Assert.assertNotNull(userService);
        Assert.assertNotNull(taskService);
    }

}

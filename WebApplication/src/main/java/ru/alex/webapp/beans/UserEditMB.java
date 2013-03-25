package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Group;
import ru.alex.webapp.model.GroupMember;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.GroupMemberService;
import ru.alex.webapp.service.GroupService;
import ru.alex.webapp.service.UserService;
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
public class UserEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserEditMB.class);
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMemberService groupMemberService;
    private List<User> userList;
    private Map<String, Boolean> userDeletable;
    private User newUser = new User();
    private User selectedUser;
    private List<Group> groupList;
    private Group selectedGroup;

    @PostConstruct
    private void init() {
        initUsers();
        initGroups();
    }

    private void initUsers() {
        try {
            userList = userService.findAll();
            logger.debug("initUsers userList={}", userList);
            userDeletable = new HashMap(userList.size());
            for (User u : userList) {
                boolean deletable = userService.isUserDeletable(u);
                userDeletable.put(u.getUsername(), deletable);
            }
            logger.debug("initUsers userEditable={}", userDeletable);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of users", e.toString()));
        }
    }

    private void initGroups() {
        try {
            groupList = groupService.findAll();
            logger.debug("initGroups groupList={}", groupList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of users", e.toString()));
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    public Map<String, Boolean> getUserDeletable() {
        return userDeletable;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void onEdit(RowEditEvent event) {
        User user = (User) event.getObject();
        logger.debug("onEdit user={}", user);
        try {
            userService.edit(user);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("User Edited", user.getUsername()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing user", e.toString()));
        }
        initUsers();
    }

    public void onCancel(RowEditEvent event) {
        User user = (User) event.getObject();
        logger.debug("onCancel user={}", user);
    }

    public void addNewUserListener(ActionEvent event) {
        logger.debug("addNewUserListener");
        newUser = new User();
        newUser.setDeleted(false);
        newUser.setEnabled(true);
    }

    public void addNewUser() {
        logger.debug("addNewUser newUser={}", newUser);
        try {
            userService.add(newUser);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("User Added", newUser.getUsername()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding user", e.toString()));
        }
        initUsers();
    }

    public void removeUser() {
        logger.debug("removeUser selectedUser={}", selectedUser);
        try {
            userService.remove(selectedUser);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("User Removed", selectedUser.getUsername()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing user", e.toString()));
        }
        initUsers();
    }

    public void assignListener(ActionEvent event) {
        try {
            selectedUser = (User) event.getComponent().getAttributes().get("user");
            logger.debug("assignListener selectedUser={}", selectedUser);
            GroupMember groupMember = selectedUser.getGroupMemberByUsername();
            logger.debug("assignListener groupMember={}", groupMember);
            if (groupMember != null)
                selectedGroup = groupMember.getGroupByGroupId();
            else
                selectedGroup = null;
            logger.debug("assignListener selectedGroup={}", selectedGroup);
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", false);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void assignUser() {
        logger.debug("assignUser selectedUser={}, selectedGroup={}", selectedUser, selectedGroup);
        try {
            GroupMember groupMemberOld = selectedUser.getGroupMemberByUsername();
            logger.debug("assignUser groupMemberOld={}", groupMemberOld);
            if (groupMemberOld != null && groupMemberOld.getGroupByGroupId() != null) {
                groupMemberOld.setGroupByGroupId(selectedGroup);
                groupMemberService.edit(groupMemberOld);
            } else {
                GroupMember groupMember = new GroupMember();
                groupMember.setGroupByGroupId(selectedGroup);
                groupMember.setUserByUsername(selectedUser);
                groupMemberService.add(groupMember);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
        initUsers();
    }

}


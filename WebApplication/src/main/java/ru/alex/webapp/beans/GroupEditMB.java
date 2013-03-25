package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Group;
import ru.alex.webapp.model.GroupAuthority;
import ru.alex.webapp.model.enums.Authority;
import ru.alex.webapp.service.GroupAuthorityService;
import ru.alex.webapp.service.GroupService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class GroupEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(GroupEditMB.class);
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupAuthorityService groupAuthorityService;
    private List<Group> groupList;
    private Group newGroup = new Group();
    private Group selectedGroup;
    private List<Authority> authorityList;
    private List<Authority> selectedAuthorities;

    @PostConstruct
    private void init() {
        initGroups();
        authorityList = Arrays.asList(Authority.values());
        logger.debug("init authorityList={}", authorityList);
    }

    private void initGroups() {
        try {
            groupList = groupService.findAll();
            logger.debug("initGroups groupList={}", groupList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of groups", e.toString()));
        }
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public Group getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(Group newGroup) {
        this.newGroup = newGroup;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    public List<Authority> getSelectedAuthorities() {
        return selectedAuthorities;
    }

    public void setSelectedAuthorities(List<Authority> selectedAuthorities) {
        this.selectedAuthorities = selectedAuthorities;
    }

    public void onEdit(RowEditEvent event) {
        Group group = (Group) event.getObject();
        logger.debug("onEdit group={}", group);
        try {
            groupService.update(group);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Group Edited", group.getGroupName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing group", e.toString()));
        }
        initGroups();
    }

    public void onCancel(RowEditEvent event) {
        Group group = (Group) event.getObject();
        logger.debug("onCancel group={}", group);
    }

    public void addNewGroupListener(ActionEvent event) {
        logger.debug("addNewGroupListener");
        newGroup = new Group();
    }

    public void addNewGroup() {
        logger.debug("addNewGroup newGroup={}", newGroup);
        try {
            groupService.add(newGroup);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Group Added", newGroup.getGroupName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding group", e.toString()));
        }
        initGroups();
    }

    public void removeGroup() {
        logger.debug("removeGroup selectedGroup={}", selectedGroup);
        try {
            groupService.remove(selectedGroup);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Group Removed", selectedGroup.getGroupName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing group", e.toString()));
        }
        initGroups();
    }

    public void assignListener(ActionEvent event) {
        try {
            selectedGroup = (Group) event.getComponent().getAttributes().get("group");
            logger.debug("assignListener selectedGroup={}", selectedGroup);
            selectedAuthorities = new ArrayList<>();
            Collection<GroupAuthority> groupAuthorities = selectedGroup.getGroupAuthorityById();
            logger.debug("assignListener groupAuthorities={}", groupAuthorities);
            if (groupAuthorities != null && groupAuthorities.size() > 0) {
                for (GroupAuthority ga : groupAuthorities) {
                    selectedAuthorities.add(Authority.valueOf(ga.getAuthority()));
                }
            }
            logger.debug("assignListener selectedAuthorities={}", selectedAuthorities);
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", false);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void assignGroup() {
        logger.debug("assignGroup selectedGroup={}, selectedAuthorities={}", selectedGroup, selectedAuthorities);
        try {
            Collection<GroupAuthority> selectedGA = selectedGroup.getGroupAuthorityById();
            logger.debug("assignGroup before groupAuthorities={}", selectedGA);
            for (Authority authority : authorityList) {
                GroupAuthority ga = groupAuthorityByAuthority(selectedGA, authority);
                if (selectedAuthorities.contains(authority)) {
                    if (ga == null) {
                        GroupAuthority newGA = new GroupAuthority();
                        newGA.setAuthority(authority.toString());
                        selectedGroup.addAuthority(newGA);
                        groupAuthorityService.add(newGA);
                    }
                } else {
                    if (ga != null) {
                        selectedGroup.removeAuthority(ga);
                        groupAuthorityService.remove(ga);
                    }
                }
            }
            logger.debug("assignGroup after groupAuthorities={}", selectedGA);
//            groupService.update(selectedGroup);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
        initGroups();
    }

    private GroupAuthority groupAuthorityByAuthority(Collection<GroupAuthority> groupAuthorities, Authority authority) {
        if (groupAuthorities == null)
            return null;
        for (GroupAuthority ga : groupAuthorities)
            if (ga.getAuthority().equals(authority.toString()))
                return ga;
        return null;
    }

}


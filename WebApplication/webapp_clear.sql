delete from group_authorities where group_id <> 1;
delete from group_members where username <> 'admin';
delete from groups where id <> 1;

delete from user_action;
delete from user_task_time;
delete from user_task_time_seq;
delete from user_site_task;
delete from site_task;

update users set current_change = null;
delete from user_change;

delete from site;
delete from task;
delete from user_admin;
delete from users where username <> 'admin';
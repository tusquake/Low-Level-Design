package org.example.Controllers;

import org.example.Group.Group;
import org.example.User.User;

import java.util.ArrayList;
import java.util.List;

public class GroupController {

    List<Group> groupList;

    public GroupController(){
        groupList = new ArrayList<>();
    }

    //create group
    public Group createNewGroup(String groupId, String groupName, User createdByUser) {

        //create a new group
        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName(groupName);

        //add the user into the group, as it is created by the USER
        group.addMember(createdByUser);

        //add the group in the list of overall groups
        groupList.add(group);
        return group;
    }

    public Group getGroup(String groupId){

        for(Group group: groupList) {

            if(group.getGroupId().equals(groupId)){
                return group;
            }
        }
        System.out.println("No such group exist!");
        return null;
    }

    public List<Group> getAllGroups(){
        return groupList;
    }

}

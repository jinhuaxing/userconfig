package com.jeex.userconfig.impl;

public class Group {
		
	private String groupId;
	private String name;
	private String description;
	
	public Group() {}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + name
				+ ", description=" + description + "]";
	}

}

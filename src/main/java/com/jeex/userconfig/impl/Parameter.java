package com.jeex.userconfig.impl;

public class Parameter {
	private String groupId;
	private String name;
	private String descriptiveName;
	private String description;
	private String defaultValue;
	private String value;
	private Integer readonly;
	
	private ParamType type;
	private Direction direction;
	private String lastValue;

	public Parameter() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescriptiveName() {
		return descriptiveName;
	}
	public void setDescriptiveName(String chineseName) {
		this.descriptiveName = chineseName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getReadonly() {
		return readonly;
	}

	public void setReadonly(Integer readonly) {
		this.readonly = readonly;
	}
	
	public void setReadonlyBool(boolean readonly) {
		if (readonly) {
			this.readonly = Integer.valueOf(1);
		} else {
			this.readonly = Integer.valueOf(0);
		}
	}

	public ParamType getType() {
		return type;
	}

	public void setType(ParamType type) {
		this.type = type;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}
	
	public String getFullName() {
		return groupId + "." + name;
	}

	@Override
	public String toString() {
		return "Parameter [groupId=" + groupId + ", name="
				+ name + ", chineseName=" + descriptiveName + ", description="
				+ description + ", defaultValue=" + defaultValue + ", value="
				+ value + ", lastValue=" + lastValue + ", internalUse="
				+ readonly + ", type=" + type + ", direction=" + direction
				+ "]";
	}
}

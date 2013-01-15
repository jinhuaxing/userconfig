package com.jeex.userconfig.impl;

import org.springframework.stereotype.Component;

import com.jeex.userconfig.ConfigGroup;
import com.jeex.userconfig.ParamFromDb;
import com.jeex.userconfig.ParamToDb;

@Component
@ConfigGroup(id = "rule1", name = "规则名字", description = "规则描述")
public class TestRule1 {

	private volatile int count;
	private volatile String feedback;
	private volatile boolean active = false;
	private volatile long longCount;

	private static volatile int staticCount;

	@ParamToDb
	public int getCount() {
		return count;
	}

	@ParamFromDb(name = "次数", defaultValue = "5", readonly = false)
	public void setCount(int count) {
		this.count = count;
	}

	@ParamFromDb(name = "反馈信息", defaultValue = "达到最大次数")
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	@ParamToDb(name = "name")
	public String getFeedback() {
		return feedback;
	}

	@ParamFromDb(name = "是否激活", defaultValue = "true")
	public void setActive(boolean active) {
		this.active = active;
	}

	@ParamToDb
	public boolean getActive() {
		return active;
	}

	@ParamToDb
	public long getLongCount() {
		return longCount;
	}

	@ParamFromDb(name = "长整数", defaultValue = "12345")
	public void setLongCount(long longCount) {
		this.longCount = longCount;
	}

	@ParamToDb
	public static int getStaticCount() {
		return staticCount;
	}

	@ParamFromDb(name = "静态整数", defaultValue = "10")
	public static void setStaticCount(int staticCount) {
		TestRule1.staticCount = staticCount;
	}

}

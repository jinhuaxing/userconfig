package com.jeex.userconfig.impl;

import org.springframework.stereotype.Component;

import com.jeex.userconfig.ParamFromDb;
import com.jeex.userconfig.ParamToDb;

@Component
public class TestRule2 {

	private volatile long count;
	
	@ParamToDb
	public long getCount() {
		return count;
	}

	@ParamFromDb(name = "上限值", defaultValue = "10")
	public void setCount(long count) {
		this.count = count;
	}
}

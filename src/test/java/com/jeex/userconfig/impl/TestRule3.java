package com.jeex.userconfig.impl;

import com.jeex.userconfig.ConfigGroup;
import com.jeex.userconfig.ParamFromDb;
import com.jeex.userconfig.ParamToDb;

@ConfigGroup(id="TestGroup", name="name1", description="desc1")
public class TestRule3 {
	
	private static volatile int staticCount;

	@ParamToDb
	public static int getStaticCount() {
		return staticCount;
	}

	@ParamFromDb(name="static3", defaultValue="13")
	public static void setStaticCount(int staticCount) {
		TestRule3.staticCount = staticCount;
	}

}

package com.jeex.userconfig.impl;

import com.jeex.userconfig.ConfigGroup;
import com.jeex.userconfig.ParamFromDb;
import com.jeex.userconfig.ParamToDb;

@ConfigGroup(id="TestGroup", name="name2", description="desc2")
public class TestRule4 {
	private static volatile int staticCount;

	@ParamToDb
	public static int getStaticCount() {
		return staticCount;
	}

	@ParamFromDb(name="static4", defaultValue="14")
	public static void setStaticCount(int staticCount0) {
		staticCount = staticCount0;
	}
}

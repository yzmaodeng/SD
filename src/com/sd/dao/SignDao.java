package com.sd.dao;

import com.sd.vo.Admin;
import com.sd.vo.Sign;

public interface SignDao extends BaseDao<Sign, String> {
	int todayNum(String s);
}

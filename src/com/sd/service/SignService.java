package com.sd.service;

import com.sd.vo.Admin;
import com.sd.vo.Sign;

public interface SignService extends BaseService<Sign, String> {
	public int countNumBySql(String s);
}

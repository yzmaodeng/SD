package com.sd.service;

import com.sd.vo.Admin;
import com.sd.vo.Score;
import com.sd.vo.Sign;

public interface ScoreService extends BaseService<Score, String> {
	public int countNumBySql(String s);
	public int totleNumBySql(String s);
}

package com.sd.dao;

import com.sd.vo.Admin;
import com.sd.vo.Score;
import com.sd.vo.Sign;

public interface ScoreDao extends BaseDao<Score, String> {

	int todayNum(String s);
	int totleNum(String s);

}

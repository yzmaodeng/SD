package com.sd.dao;

import java.util.List;
import java.util.Map;

import com.sd.vo.KfData;

public interface KfDataDao extends BaseDao<KfData, String> {
	public List<KfData> getDateBySql(Map<String, String> condition);
	public List<KfData> getDate(String teamGid,String time);
}

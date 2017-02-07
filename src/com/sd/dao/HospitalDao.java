package com.sd.dao;

import java.util.List;

import com.sd.vo.Thospital;

public interface HospitalDao extends BaseDao<Thospital,String>{
	public List<Thospital> query(Thospital thospital);
}

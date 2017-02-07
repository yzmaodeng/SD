package com.sd.service;

import java.util.List;

import com.sd.vo.Thospital;

public interface HospitalService extends BaseService< Thospital, String> {
	public List<Thospital> query(Thospital thospital);
}

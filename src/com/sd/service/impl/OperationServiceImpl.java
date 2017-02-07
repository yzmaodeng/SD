package com.sd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.AdminDao;
import com.sd.dao.OperationDao;
import com.sd.service.AdminService;
import com.sd.service.OperationService;
import com.sd.vo.Admin;
import com.sd.vo.Operation;

@Service
public class OperationServiceImpl extends BaseServiceImpl<Operation, String> implements OperationService{
	@Resource
	public void setBaseDao(OperationDao operationDao) {
		super.setBaseDao(operationDao);
	}
	@Resource private OperationDao operationDao;
	
}

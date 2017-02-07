package com.sd.dao.impl;

import org.springframework.stereotype.Repository;

import com.sd.dao.PatientDao;
import com.sd.vo.Record;
@Repository
public class PatientDaoImpl extends BaseDaoImpl<Record,String> implements PatientDao {
}

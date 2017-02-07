package com.sd.dao;

import com.sd.vo.Scheme;

public interface SchemeDao extends BaseDao<Scheme, String> {

	int countMainNumBySql(String gid);

}

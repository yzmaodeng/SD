package com.sd.service;
import java.util.List;

import com.sd.vo.Columns;
public interface ColumnsService extends BaseService<Columns, String> {

	List<Columns> getHomeColumn();

	Columns getById(String gid);
	
	
}

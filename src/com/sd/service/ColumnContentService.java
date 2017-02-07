package com.sd.service;
import java.util.List;

import com.sd.vo.ColumnContent;
public interface ColumnContentService extends BaseService<ColumnContent, String> {

	List<ColumnContent> getByParentID(String gid);
	
}

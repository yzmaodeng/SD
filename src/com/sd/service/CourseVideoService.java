package com.sd.service;

import java.util.List;

import com.sd.vo.CourseVideo;

public interface CourseVideoService extends BaseService<CourseVideo, String> {

	List<CourseVideo> executeSql(String sql, String string, String type);

	List<CourseVideo> getVoListBySql(String hql2);
	
	// 更新视频浏览量
	void updateWatchNumber(String videoGid);
	
}

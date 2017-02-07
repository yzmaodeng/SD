package com.sd.service;

import java.util.List;

import com.sd.vo.Admin;
import com.sd.vo.CourseExpert;

public interface CourseExpertService extends BaseService<CourseExpert, String> {

	List<CourseExpert> getfourExperts();
}

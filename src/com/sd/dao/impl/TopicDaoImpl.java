package com.sd.dao.impl;

import org.springframework.stereotype.Repository;

import com.sd.dao.TopicDao;
import com.sd.vo.Topic;
@Repository
public class TopicDaoImpl extends BaseDaoImpl<Topic, String> implements
		TopicDao {

}

package com.sd.dao;

import java.util.List;

import com.sd.vo.Textbook;

public interface TextBookDao extends BaseDao<Textbook,String>{
	public List<Textbook> query(int selected,String currId,String count,String keyword,String category,String isFav,String userId);
}

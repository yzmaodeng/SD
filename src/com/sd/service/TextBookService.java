package com.sd.service;

import java.util.List;

import com.sd.vo.Textbook;

public interface TextBookService extends BaseService<Textbook,String>{
	public List<Textbook> query(int selected,String currId,String count,String keyword,String category,String isFav,String userId);

	public List<Object[]>  getClassificationSelectionAll(int count, String string, String orderString, String category);

	public List<Object[]> classificationSingleSelection(String type, int parseInt,
			String string, String orderString, String category);

	public List<Object[]> queryCategory();

	public List<Textbook> queryHomeTexts();
	
}

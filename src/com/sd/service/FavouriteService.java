package com.sd.service;

import java.util.List;

import com.sd.vo.Favourite;

public interface FavouriteService extends BaseService<Favourite, String> {
	/**
	 * 查询是否被收藏
	 * @param gid
	 * @param userId
	 * @return
	 */
	String isFav(String gid, String userId);
	public void updateFav(String gid, String userId);
	List<Object[]> getListBySql(String sql);
	

	
	
	
}

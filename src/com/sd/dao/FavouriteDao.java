package com.sd.dao;

import java.util.List;

import com.sd.vo.Favourite;

public interface FavouriteDao extends BaseDao<Favourite, String> {
	/**
	 * 查询是否被收藏
	 * @param gid
	 * @param userId
	 * @return
	 */
	String isFav(String gid, String userId);
	public void updateFav(String gid, String userId);
	
}

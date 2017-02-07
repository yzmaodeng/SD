package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sd.dao.FavouriteDao;
import com.sd.service.FavouriteService;
import com.sd.vo.Favourite;

@Service
public class FavouriteServiceImpl extends BaseServiceImpl<Favourite, String> implements FavouriteService{
	@Resource
	public void setBaseDao(FavouriteDao favouriteDao) {
		super.setBaseDao(favouriteDao);
	}
	@Resource private FavouriteDao favouriteDao;
	
	/**
	 * 查询是否被收藏
	 * @param gid
	 * @param userId
	 * @return
	 */
	public String isFav(String gid, String userId) {
		return favouriteDao.isFav(gid, userId);
	}
	@Transactional
	public void updateFav(String gid, String userId){
		favouriteDao.updateFav(gid, userId);
	}
	@Override
	public List<Object[]> getListBySql(String sql) {
		// TODO Auto-generated method stub
		return favouriteDao.getListBySql(sql);
	}

}

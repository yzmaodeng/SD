package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sd.dao.CouponDao;
import com.sd.service.CouponService;
import com.sd.service.UserService;
import com.sd.util.CustomException;
import com.sd.vo.Coupon;
import com.sd.vo.User;

@Service
public class CouponServiceImpl extends BaseServiceImpl<Coupon, String>
		implements CouponService {
	@Resource
	public void setBaseDao(CouponDao couponDao) {
		super.setBaseDao(couponDao);
	}
	@Resource private CouponDao couponDao;
	@Resource
	protected UserService userService;

	@SuppressWarnings("unused")
	@Override
	public List<Coupon> getByUserId(String userId) throws CustomException {
		User user = userService.get(userId);
		String phone = user.getPhone();
		String sqlString="select  * from  coupon  where inuse='-1'  and now() > beginTime and now()<endTime and   telephone='"+phone+"'";
		if (user==null) {
			throw new CustomException("gid为"+userId+"用户不存在！！！");
		}
		return couponDao.getVoListBySql(sqlString);
	}


	@Override
	public List<Coupon> getByGid(String gid) {
		String sqlString="select  * from  coupon  where gid='"+gid+"'";
		return couponDao.getVoListBySql(sqlString);
	}
	
	public void updateCouponStatus(String gid){
		String sqlString="update coupon c set c.inuse ="+0+" where c.gid='"+gid+"'";
		couponDao.executeUpdateSql(sqlString);
	}


	@Override
	public  List<Coupon> queryOne() {
		String s="select  * from  coupon  where inuse='1' and now() > beginTime and now()<endTime  order by gid asc limit 1";
		return  couponDao.getVoListBySql(s);
	}


	@Override
	public List<Coupon> getByPhoto(String telephoneNum,String gid) {
		String sqlString="SELECT  * FROM  coupon  WHERE telephone='"+telephoneNum+"' AND coupongid='"+gid+"'";
		return couponDao.getVoListBySql(sqlString);
	}


	@Override
	public List<Coupon> getCoupon() {
		String sqlString="SELECT * FROM coupon WHERE coupongid='0' AND inuse='-1' ORDER BY createTime DESC LIMIT 1";
		return couponDao.getVoListBySql(sqlString);
	}


	@Override
	public List<Coupon> getByIdentification(String identification) {
		String sqlString="SELECT  * FROM  coupon  WHERE `code`='"+identification+"'AND inuse='1'";
		return couponDao.getVoListBySql(sqlString);
	}

}

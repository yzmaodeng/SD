package com.sd.service;

import java.util.List;

import com.sd.util.CustomException;
import com.sd.vo.Coupon;

public interface CouponService extends BaseService<Coupon, String> {

	List<Coupon> getByUserId(String userId) throws CustomException;
	List<Coupon> getByGid(String gid);
	void updateCouponStatus(String gid);
	 List<Coupon> queryOne();
	List<Coupon> getByPhoto(String telephoneNum,String gid);
	List<Coupon> getCoupon();
	List<Coupon> getByIdentification(String identification);

}

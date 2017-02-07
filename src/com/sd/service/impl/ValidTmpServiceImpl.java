package com.sd.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.ValidTmpDao;
import com.sd.service.ValidTmpService;
import com.sd.vo.ValidTmp;

@Service
public class ValidTmpServiceImpl extends BaseServiceImpl<ValidTmp, String> implements ValidTmpService{
	@Resource
	public void setBaseDao(ValidTmpDao validTmpDao) {
		super.setBaseDao(validTmpDao);
	}
	@Resource private ValidTmpDao validTmpDao;
	
	/**
	 * 校验验证码
	 * @param mobile
	 * @param identityCode
	 */
	public boolean checkValidTmp(String mobile, String identityCode) {
		return validTmpDao.checkValidTmp(mobile, identityCode);
	}
	
	/**
	 * 获取本手机号最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	public Date getNewestDate(String mobile) {
		return validTmpDao.getNewestDate(mobile);
	}
	/**
	 * 获取本IP最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	public Date getNewestIPDate(String ip) {
		return validTmpDao.getNewestIPDate(ip);
	}
	/**
	 * 获取本手机号今天发送的次数
	 * @param mobile
	 * @return
	 */
	public Integer getTodayCount(String mobile) {
		return validTmpDao.getTodayCount(mobile);
	}
	/**
	 * 获取ip今天发送的次数
	 * @param mobile
	 * @return
	 */
	public Integer getTodayIPCount(String ip) {
		return validTmpDao.getTodayIPCount(ip);
	}
	
}

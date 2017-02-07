package com.sd.service;

import java.util.Date;

import com.sd.vo.ValidTmp;

public interface ValidTmpService extends BaseService<ValidTmp, String> {

	/**
	 * 校验验证码
	 * @param mobile
	 * @param identityCode
	 */
	boolean checkValidTmp(String mobile, String identityCode);
	/**
	 * 获取本IP最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	Date getNewestIPDate(String mobile);
	/**
	 * 获取本手机号最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	Date getNewestDate(String mobile);
	/**
	 * 获取本手机号今天发送的次数
	 * @param mobile
	 * @return
	 */
	Integer getTodayCount(String mobile);
	/**
	 * 获取ip今天发送的次数
	 * @param mobile
	 * @return
	 */
	Integer getTodayIPCount(String ip);
	
}

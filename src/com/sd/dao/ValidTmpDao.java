package com.sd.dao;

import java.util.Date;

import com.sd.vo.ValidTmp;

public interface ValidTmpDao extends BaseDao<ValidTmp, String> {

	/**
	 * 校验验证码
	 * @param mobile
	 * @param identityCode
	 */
	boolean checkValidTmp(String mobile, String identityCode);
	/**
	 * 获取本手机号最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	Date getNewestDate(String mobile);
	/**
	 * 获取IP最近一次发送的时间
	 * @param mobile
	 * @return
	 */
	Date getNewestIPDate(String ip);
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

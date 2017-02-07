package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AcademyFileService;
import com.sd.service.AcademyService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.Academy;
import com.sd.vo.AcademyFile;
import com.sd.vo.AcademyUser;
import com.sd.vo.AcademyVip;
import com.sd.vo.Announcement;
import com.sd.vo.Columns;
import com.sd.vo.Coupon;
import com.sd.vo.Course;
import com.sd.vo.CourseExpert;
import com.sd.vo.CourseVideo;
import com.sd.vo.Invoice;
import com.sd.vo.NeedsChange;
import com.sd.vo.OrderInfo;

public class AcademyAction extends BaseAction {
	private Logger logger = Logger.getLogger(this.getClass());
	private String userId;
	@Resource
	private AcademyFileService academyFileService;
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Resource
	private AcademyService academyService;
	private String gid;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void needsChange() {

		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
//			if (!logined)
//				throw new CustomException(NO_LOGIN);
			List<NeedsChange> homeAcademy = academyService.needsChange();
			
			if (homeAcademy.size()==0) {
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("vipEndTime", "0");
				
			} else {
				NeedsChange needsChange = homeAcademy.get(0);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("vipEndTime", "1");

			}
			
			
		

//		} catch (CustomException e) {
//			ret.put("code", "0");
//			ret.put("message", e.getMessage());
//			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}
	
	
	// 获取幕课主界面数据
		public void queryAcademyHomePage() {
			JSONObject ret = new JSONObject();
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				boolean logined = userTokenService.checkToken(request);
				String userId = request.getHeader("userId");
				// 轮转列表
				Map<String, String> condition1 = new HashMap<String, String>();
				condition1.put("category", "慕课");
				List<Announcement> list = announcementService.getConditonList(
						condition1, null, false, null);
				JSONArray aja = new JSONArray();
				for (Announcement announcement : list) {
					JSONObject jo = new JSONObject();
					jo.put("gid", announcement.getGid());
					jo.put("pic", announcement.getPic());
					jo.put("url", announcement.getUrl());
					jo.put("typestr", announcement.getTypestr());
					jo.put("title", announcement.getTitle());
					// 返回跳转首页的类型和gid
					jo.put("type", announcement.getType());
					String fav = favouriteService.isFav(gid, userId);
					if (fav!=null&&"1".equals(fav)) {
						jo.put("isFav", "1");
					}else{
						jo.put("isFav", "2");
					}
					aja.add(jo);
				}

				Map<String, String> condition = new HashMap<String, String>();
				// 建议的
				condition.put("recommendation", "1");
				condition.put("parentGid", "0");
				List<Course> clist = courseService.getConditonList(condition,
						"updateTime", true, null);
				// 课程列表
				JSONArray cja = new JSONArray();
				for (Course course : clist) {
					float price2 = course.getPrice();
					if (price2==0) {
						continue;
					}
					JSONObject jo = new JSONObject();
					jo.put("gid", course.getGid());
					jo.put("avatar", course.getAvatar());
					JSONArray eja = new JSONArray();
					String[] strs = course.getExpertGid().split(",");
					for (String string : strs) {
						CourseExpert courseExpert = courseExpertService.get(string);
						JSONObject ejo = new JSONObject();
						ejo.put("expertGid", courseExpert.getGid());
						ejo.put("expertName", courseExpert.getName());
						ejo.put("expertHospital", courseExpert.getHospital());
						eja.add(ejo);
					}
					jo.put("expert", eja);
					jo.put("title", course.getTitle());
					jo.put("cWatchNum", course.getWatchNumber());
					jo.put("total", course.getTotalNumber());
					jo.put("current", course.getCurrentNumber());
					cja.add(jo);
				}

				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				JSONArray acja = new JSONArray();
				JSONArray columsArr = new JSONArray();// 分类的json
				List<Columns> Columns =columnsService.getHomeColumn();
				for (Columns columns2 : Columns) {
					JSONObject joc = new JSONObject();
					joc.put("purchase", false);
					if (logined) {
						List<OrderInfo> orderInfos=orderInfoService.getByUserIdAcademyId(userId,columns2.getGid());
						if (orderInfos.size()!=0) {
							joc.put("purchase", true);
						}
					}
					joc.put("gid", columns2.getGid());
					joc.put("author", columns2.getAuthor());
					joc.put("price", "￥"+columns2.getPrice()+" / 年");
					joc.put("title", columns2.getTitle());
					joc.put("pic", columns2.getPic());
					joc.put("introduction", columns2.getIntruduction());
					joc.put("subscriberNum", columns2.getSubscriberNum());
					columsArr.add(joc);
				}
				List<Academy> homeAcademy = academyService.getHomeAcademy();
			     for (Academy academy : homeAcademy) {
			    	 JSONObject jo = new JSONObject();
					 jo.put("gid", academy.getGid());
					 jo.put("avatar", academy.getPic());
					 jo.put("beginTime",sdf.format(academy.getBeginDate()) );
					 jo.put("endTime", sdf.format(academy.getEndDate()));
					 jo.put("local", academy.getLocal());
					 Date date=new Date();
						String price="";
						Date stageTime = academy.getStageTime();
						Date stageTimeI = academy.getStageTimeI();
						Date stageTimeII = academy.getStageTimeII();
						if(stageTimeI == null && stageTimeII != null)
							 throw new CustomException("gid为" + gid + "的售票时间设置错误！");
						Date endDate = academy.getEndDate();
						if (stageTimeI != null && stageTimeII != null) {
							 String priceII = academy.getPriceII();
							String priceIII = academy.getPriceIII();
							if (!StringUtils.isNotEmpty(priceII)||!StringUtils.isNotEmpty(priceIII)) {
								throw new CustomException("gid为" + gid + "的票价设置错误！");
							}
							
							if (date.after(stageTimeI) && date.before(stageTimeII)) {
								price=priceII;
							} else if (date.after(stageTimeII) && date.before(endDate)) {
								price=priceIII;
							} else{
								price = academy.getPrice();
							}

						} else if (stageTimeI != null && stageTimeII == null) {
							 String priceII = academy.getPriceII();
							if (!StringUtils.isNotEmpty(priceII)) {
								throw new CustomException("gid为" + gid + "的priceII票价设置错误！");
							}

							if (date.after(stageTimeI) && date.before(endDate)) {
								price=priceII;
							} else{
								price = academy.getPrice();
							}

						} else{
							price = academy.getPrice();
						}
					 jo.put("price", price);
					 jo.put("title", academy.getTitle());
					 jo.put("preprice", academy.getPreprice());
					 acja.add(jo);
					
				}

				JSONObject res = new JSONObject();
				res.put("imageList", aja);
				res.put("courseList", cja);
				res.put("academyList", acja);
				res.put("columsList", columsArr);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", res);
			} catch (Exception e) {
				logger.error("", e);
				ret.put("code", "0");
				ret.put("message", ERROR_INFO);
				ret.put("result_data", new JSONObject());
			}
			putDataOut(ret.toString());
		}

	public void academyDetail() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			Academy academy = academyService.get(gid);
			JSONObject jo = JSONObject.fromObject(academy);
			AcademyVip academyVip = academyService.getVipInfo(gid);
			if (academyVip != null && academyVip.getResNum() != 0) {
				jo.put("vipPrice", academyVip.getPrice());
				jo.put("vipDescri", academyVip.getDescription());
				jo.put("vipName", academyVip.getName());
				jo.put("vipGid", academyVip.getGid());
				jo.put("vipResNum", academyVip.getResNum());
			} else {
				jo.put("vipPrice", "");
				jo.put("vipDescri", "");
				jo.put("vipName", "");
				jo.put("vipGid", "");
				jo.put("vipResNum", "");
			}
			jo.put("beginDate", sdf.format(academy.getBeginDate()));
			jo.put("endDate", sdf.format(academy.getEndDate()));
			jo.put("createTime", sdf.format(academy.getCreateTime()));
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("aid", gid);
			List<AcademyFile> list = academyFileService.getConditonList(
					condition, "", false, null);
			JSONArray ja = new JSONArray();
			for (AcademyFile academyFile : list) {
				JSONObject afjo = JSONObject.fromObject(academyFile);
				afjo.put("createTime", sdf.format(academyFile.getCreateTime()));
				ja.add(afjo);
			}
			Date beginDate = academy.getBeginDate();
			Date endDate = academy.getEndDate();
			Date date = new Date();
			String isOver = "true";
			if (date.after(endDate)) {
				isOver = "false";
			}
			// 票价的判断
			JSONObject jsonPrice = new JSONObject();
			Date stageTime = academy.getStageTime();
			// jo.put("stageTime", sdf.format(stageTime));
			Date stageTimeI = academy.getStageTimeI();
			Date stageTimeII = academy.getStageTimeII();

			if (stageTimeI == null && stageTimeII != null)
				throw new CustomException("gid为" + gid + "的售票时间设置错误！");
			String priceI = academy.getPriceI();
			if (priceI == null) {
				throw new CustomException("gid为" + gid + "的priceI票价不能为空！");
			}
			jo.put("priceI", priceI);
			String type = "";
			String saleMessage = "";
			if (stageTimeI != null && stageTimeII != null) {
				String priceII = academy.getPriceII();
				String priceIII = academy.getPriceIII();
				if (!StringUtils.isNotEmpty(priceII)
						|| !StringUtils.isNotEmpty(priceIII)) {
					throw new CustomException("gid为" + gid + "的票价设置错误！");
				}
				jo.put("priceII", priceII);
				jo.put("priceIII", priceIII);
				jo.put("stageTimef", sdf.format(stageTime));
				jo.put("stageTimeII", sdf.format(stageTimeI));
				jo.put("stageTimeIII", sdf.format(stageTimeII));
				if (stageTime.after(stageTimeI)
						|| stageTimeI.after(stageTimeII)
						|| stageTimeII.after(endDate)) {
					throw new CustomException("gid为" + gid + "的会议售票时间设置错误！！！");
				}
				if (date.after(stageTime) && date.before(stageTimeI)) {
					saleMessage = "3人拼团：3人以上限时优惠" + priceI + "元";
					type = "1";
				} else if (date.after(stageTimeI) && date.before(stageTimeII)) {
					saleMessage = "折扣规则：3人以上9折优惠";
					type = "2";
				} else {
					saleMessage = "折扣规则：3人以上9折优惠";
					type = "3";
				}

			} else if (stageTimeI != null && stageTimeII == null) {
				String priceII = academy.getPriceII();
				jo.put("priceII", priceII);
				if (!StringUtils.isNotEmpty(priceII)) {
					throw new CustomException("gid为" + gid + "的priceII票价设置错误！");
				}
				jo.put("stageTimeII", stageTimeI);

				if (date.after(stageTime) && date.before(stageTimeI)) {
					saleMessage = "三人以上，票价减为" + priceI + "元";
					type = "1";
				} else {
					saleMessage = "三人以上打九折！！！";
					type = "2";
				}

			} else {
				type = "1";

			}

			jo.put("saleMessage", saleMessage);
			jo.put("type", type);
			jo.put("academyFile", ja);
			jo.put("attendNum", academy.getTotleNum());
			jo.put("isOver", isOver);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jo);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 臧亮 返回课程的列表的信息
	 */
	public void queryAcademy() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();

			int firstNUm = (Integer.parseInt(pageIndex) - 1)
					* Integer.parseInt(pageCount);
			List<Academy> homeAcademy = academyService.getListBySql(firstNUm,
					pageCount);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			JSONArray eja = new JSONArray();
			for (Academy academy : homeAcademy) {
				JSONObject jo = new JSONObject();
				jo.put("gid", academy.getGid());
				jo.put("avatar", academy.getPic());
				jo.put("beginTime", sdf.format(academy.getBeginDate()));
				jo.put("endTime", sdf.format(academy.getEndDate()));
				jo.put("local", academy.getLocal());

				Date date = new Date();
				String price = "";
				Date stageTime = academy.getStageTime();
				Date stageTimeI = academy.getStageTimeI();
				Date stageTimeII = academy.getStageTimeII();
				if (stageTimeI == null && stageTimeII != null)
					throw new CustomException("gid为" + gid + "的售票时间设置错误！");
				Date endDate = academy.getEndDate();
				if (stageTimeI != null && stageTimeII != null) {
					String priceII = academy.getPriceII();
					String priceIII = academy.getPriceIII();
					if (!StringUtils.isNotEmpty(priceII)
							|| !StringUtils.isNotEmpty(priceIII)) {
						throw new CustomException("gid为" + gid + "的票价设置错误！");
					}

					if (date.after(stageTimeI) && date.before(stageTimeII)) {
						price = priceII;
					} else if (date.after(stageTimeII) && date.before(endDate)) {
						price = priceIII;
					} else {
						price = academy.getPrice();
					}

				} else if (stageTimeI != null && stageTimeII == null) {
					String priceII = academy.getPriceII();
					if (!StringUtils.isNotEmpty(priceII)) {
						throw new CustomException("gid为" + gid
								+ "的priceII票价设置错误！");
					}

					if (date.after(stageTimeI) && date.before(endDate)) {
						price = priceII;
					} else {
						price = academy.getPrice();
					}

				} else {
					price = academy.getPrice();
				}

				jo.put("price", price);
				jo.put("title", academy.getTitle());
				jo.put("preprice", academy.getPreprice());
				eja.add(jo);

			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", eja);

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 臧亮 新建联系人
	 * 
	 * @param userId
	 * @param token
	 * @param name
	 *            *
	 * @param location
	 * @param telephoneNum
	 * @param mail
	 * @param title
	 * @param company
	 * @param companyNature
	 */

	public void newOrderPerson() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			AcademyUser academyUser = new AcademyUser();
			academyUser.setGid(ComUtil.getuuid());
			academyUser.setLocation(location);
			academyUser.setMail(mail);
			academyUser.setName(name);
			academyUser.setOrganiserId(userId);
			academyUser.setTelephoneNum(telephoneNum);
			academyUser.setCompany(company);
			academyUser.setCompanyNature(companyNature);
			academyUser.setTitle(title);
			academyUserService.save(academyUser);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	/**
	 * 臧亮 查询联系人
	 */
	public void queryOrderPersonList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			JSONArray uja = new JSONArray();
			List<AcademyUser> allList = academyUserService
					.getByOrganiser(userId);
			for (AcademyUser academyUser2 : allList) {
				JSONObject jo = new JSONObject();
				jo.put("gid", academyUser2.getGid());
				jo.put("local", academyUser2.getLocation());
				jo.put("price", academyUser2.getMail());
				jo.put("name", academyUser2.getName());
				jo.put("Organiser", academyUser2.getOrganiserId());
				jo.put("telephoneNum", academyUser2.getTelephoneNum());
				uja.add(jo);

			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", uja);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	/**
	 * 臧亮 
	 */
	public void queryCoupon() {
		
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			JSONObject jo = new JSONObject();
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isNotBlank(identification)) {
				List<Coupon> byPhoto=couponService.getByIdentification(identification);
				if (byPhoto.size()!=1) {
					throw new CustomException("该验证码优惠券过期");
				}
				Coupon coupon = byPhoto.get(0);
				coupon.setInuse("-1");
				coupon.setTelephone(telephoneNum);
				couponService.update(coupon);
				jo.put("beginTime", sdf.format(coupon.getBeginTime()));
				jo.put("accomplish", coupon.getAccomplish());
				jo.put("discount", coupon.getDiscount());
				jo.put("endTime", sdf.format(coupon.getEndTime()));
				jo.put("type", coupon.getType());
				
			}else{
				 List<Coupon> coupons = couponService.getCoupon();
			     if (coupons.size()==0) {
			    	 throw new CustomException("没有优惠券活动");
				}
			     Coupon coupon=coupons.get(0);
				jo.put("beginTime", sdf.format(coupon.getBeginTime()));
				jo.put("accomplish", coupon.getAccomplish());
				jo.put("discount", coupon.getDiscount());
				jo.put("endTime", sdf.format(coupon.getEndTime()));
				jo.put("type", coupon.getType());
				jo.put("status", "1");
				if (!StringUtils.isNotBlank(telephoneNum))
					throw new CustomException("手机号为空");
				List<Coupon> byPhoto = couponService.getByPhoto(telephoneNum,coupon.getGid());
				if (byPhoto.size()==0) {
				      Calendar c = Calendar.getInstance();
				       c.add(Calendar.DAY_OF_MONTH, 30);
					Date date = new Date();
					Coupon coupon2 = new Coupon();
					coupon2.setGid(ComUtil.getuuid());
					coupon2.setAccomplish(coupon.getAccomplish());
					coupon2.setDiscount(coupon.getDiscount());
					coupon2.setBeginTime(date);
					coupon2.setEndTime(c.getTime());
					coupon2.setInuse("-1");
					coupon2.setCreateTime(date);
					coupon2.setTelephone(telephoneNum);
					coupon2.setType(coupon.getType());
					coupon2.setUserId(userId);
					coupon2.setCoupongid(coupon.getGid());
					couponService.save(coupon2);
					jo.put("status", "0");
				}
				
				
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jo);

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}
	public void queryCouponList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			JSONArray uja = new JSONArray();
			List<Coupon> allList = couponService.getByUserId(userId);
			if (allList.size() == 0) {
				ret.put("code", "你没有优惠券！！");
			}
			for (Coupon coupon : allList) {
				JSONObject jo = new JSONObject();
				jo.put("gid", coupon.getGid());
				jo.put("beginTime", sdf.format(coupon.getBeginTime()));
				jo.put("accomplish", coupon.getAccomplish());
				jo.put("discount", coupon.getDiscount());
				jo.put("endTime", sdf.format(coupon.getEndTime()));
				jo.put("type", coupon.getType());
				jo.put("userId", coupon.getUserId());
				uja.add(jo);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", uja);

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	/**
	 * 臧亮 gid排序获得第一张优惠券
	 */
	public void doleCoupon() {
		JSONObject ret = new JSONObject();
		try {
			List<Coupon> queryOne = couponService.queryOne();
			if (queryOne.size() == 0) {
				throw new CustomException("优惠券库存不足！！！！！");
			}
			Coupon coupon = queryOne.get(0);
			JSONObject jo = new JSONObject();
			jo.put("gid", coupon.getGid());
			jo.put("beginTime", sdf.format(coupon.getBeginTime()));
			jo.put("accomplish", coupon.getAccomplish());
			jo.put("discount", coupon.getDiscount());
			jo.put("endTime", sdf.format(coupon.getEndTime()));
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jo);

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	/**
	 * 臧亮 将优惠券改为领取状态
	 */
//	public void chanCouponStatus() {
//		JSONObject ret = new JSONObject();
//		try {
//
//			List<Coupon> list = couponService.getByPhoto(telephoneNum);
//
//			if (list.size() != 0) {
//				throw new CustomException("已经领取过优惠券！！！");
//			}
//
//			Coupon coupon = couponService.get(gid);
//			if (coupon == null) {
//				throw new CustomException("优惠券不存在");
//			}
//			coupon.setTelephone(telephoneNum);
//			coupon.setInuse("-1");
//			couponService.update(coupon);
//
//			JSONObject jo = new JSONObject();
//			jo.put("gid", coupon.getGid());
//			jo.put("beginTime", sdf.format(coupon.getBeginTime()));
//			jo.put("accomplish", coupon.getAccomplish());
//			jo.put("discount", coupon.getDiscount());
//			jo.put("endTime", sdf.format(coupon.getEndTime()));
//			jo.put("telephoneNum", telephoneNum);
//
//			ret.put("result_data", jo);
//			ret.put("code", "1");
//			ret.put("message", SUCCESS_INFO);
//		} catch (CustomException e) {
//			ret.put("code", "0");
//			ret.put("message", e.getMessage());
//			ret.put("result_data", new JSONObject());
//		} catch (Exception e) {
//			logger.error("", e);
//			ret.put("code", "0");
//			ret.put("message", ERROR_INFO);
//			ret.put("result_data", new JSONObject());
//		}
//		putDataOut(ret.toString());
//
//	}

	// creatInvoice
	/**
	 * 臧亮 保存发票
	 * 
	 * @param userId
	 * @param token
	 * @param name
	 * @param location
	 * @param telephoneNum
	 * @param type
	 *            "1"会务"2"会议
	 * @param detail
	 *            詳情
	 */

	public void creatInvoice() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			Invoice invoice = new Invoice();
			String gidString = ComUtil.getuuid();
			invoice.setGid(gidString);
			invoice.setLocation(location);
			invoice.setName(name);
			invoice.setTelephoneNum(telephoneNum);
			invoice.setType(type);
			invoice.setDetail(detail);
			invoice.setPrice(price);
			invoiceService.save(invoice);
			JSONObject jsonGid = new JSONObject();
			jsonGid.put("gid", gidString);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jsonGid);

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	/**
	 * 臧亮 保存发票
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 */

	public void queryDowloadContent() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			List<Coupon> allList = couponService.getByUserId(userId);
			JSONArray uja = new JSONArray();
			for (Coupon coupon : allList) {
				JSONObject jo = new JSONObject();
				jo.put("gid", coupon.getGid());
				jo.put("beginTime", sdf.format(coupon.getBeginTime()));
				jo.put("accomplish", coupon.getAccomplish());
				jo.put("discount", coupon.getDiscount());
				jo.put("endTime", sdf.format(coupon.getEndTime()));
				jo.put("type", coupon.getType());
				jo.put("userId", coupon.getUserId());
				uja.add(jo);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");

		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTelephoneNum() {
		return telephoneNum;
	}

	public void setTelephoneNum(String telephoneNum) {
		this.telephoneNum = telephoneNum;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	private String location;
	private String telephoneNum;
	private String mail;
	private String pageCount;
	private String pageIndex;
	private String type;
	private String detail;
	private String price;
	private String title;
	private String company;
	private String companyNature;
	private String identification;
	

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyNature() {
		return companyNature;
	}

	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}

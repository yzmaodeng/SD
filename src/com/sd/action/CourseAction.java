package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.util.ComUtil;
import com.sd.util.Conditions;
import com.sd.util.CustomException;
import com.sd.util.MyPage;
import com.sd.util.SmsUtil;
import com.sd.vo.Academy;
import com.sd.vo.Announcement;
import com.sd.vo.Columns;
import com.sd.vo.Course;
import com.sd.vo.CourseAnnocement;
import com.sd.vo.CourseExpert;
import com.sd.vo.CourseReply;
import com.sd.vo.CourseVideo;
import com.sd.vo.Favourite;
import com.sd.vo.Notice;
import com.sd.vo.OrderInfo;
import com.sd.vo.User;

public class CourseAction extends BaseAction {
	private Logger logger = Logger.getLogger(this.getClass());
	private String userId;
	private String gid;
	private String type;
	private String charge;
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	private Integer pageNum;
	//苹果审核隐藏收费课程
	public String getIos_version() {
		return ios_version;
	}

	public void setIos_version(String ios_version) {
		this.ios_version = ios_version;
	}

	private String ios_version;
	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	private String rank;
	private String category;
	private String keyword;
	private String location;
	private String beReplyGid;
	private String content;
	private String replyGid;
	private String pstGid;
	private String userGid;
	private String isRaed;
	private String createTime;
	//专栏
	
	// 获取幕课主界面数据
	public void queryCourseHomePage() {
		JSONObject ret = new JSONObject();
		try {
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
			List<CourseVideo> tlist = courseVideoService.getConditonList(
					condition, "updateTime", true, null);
			// 视频列表
			JSONArray tja = new JSONArray();// 分类的json
			Set<String> set = new HashSet<String>();
			for (CourseVideo courseVideo : tlist) {
				set.add(courseVideo.getType());
			}
			for (String type : set) {
				JSONObject jo = new JSONObject();
				jo.put("type", type);
				Map<String, String> condition2 = new HashMap<String, String>();
				condition2.put("recommendation", "1");
				condition2.put("type", type);
				List<CourseVideo> vlist = courseVideoService.getConditonList(
						condition2, "updateTime", true, null);
				JSONArray vja = new JSONArray();// 视频的json
				for (CourseVideo courseVideo : vlist) {
					JSONObject vjo = new JSONObject();
					vjo.put("gid", courseVideo.getGid());
					vjo.put("avatar", courseVideo.getAvatar());
					JSONArray eja = new JSONArray();
					String[] strs = courseVideo.getExpertGid().split(",");
					for (String string : strs) {
						CourseExpert courseExpert = courseExpertService
								.get(string);
						JSONObject ejo = new JSONObject();
						ejo.put("expertGid", courseExpert.getGid());
						ejo.put("expertName", courseExpert.getName());
						ejo.put("expertHospital", courseExpert.getHospital());
						eja.add(ejo);
					}
					vjo.put("expert", eja);
					vjo.put("title", courseVideo.getTitle());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vja.add(vjo);
				}
				jo.put("classList", vja);
				tja.add(jo);
			}

			List<CourseExpert> elist = courseExpertService.getfourExperts();
			// 专家列表
			JSONArray eja = new JSONArray();
			for (CourseExpert courseExpert : elist) {
				JSONObject jo = new JSONObject();
				jo.put("expertGid", courseExpert.getGid());
				jo.put("expertAvatar", courseExpert.getAvatar());
				jo.put("expertName", courseExpert.getName());
				eja.add(jo);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			JSONArray acja = new JSONArray();
			
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
			res.put("videoList", tja);
			res.put("expertList", eja);
			res.put("academyList", acja);
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

	// 查询慕课详情
	// 慕课gid gid
	public void queryCourseDetail() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			Course course = courseService.get(gid);
			List resultList = new ArrayList();
			// 如果登陆将该用户的收藏全部传回来
			if (logined) {
				String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
				List paraList = new ArrayList();
				paraList.add(course.getGid());
				paraList.add(request.getHeader("userid"));
				paraList.add("1");
				resultList = favouriteService
						.HQLSqlBindParameter(hql, paraList);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONObject jo = new JSONObject();
			
			jo.put("isPerchase", course.getPrice()==0?true:false);
			jo.put("introduction", course.getIntroduction());
//			jo.put("price", course.getPricee());
			JSONArray eja = new JSONArray();
			String[] strs = course.getExpertGid().split(",");
			for (String string : strs) {
				CourseExpert courseExpert = courseExpertService.get(string);
				JSONObject ejo = new JSONObject();
				ejo.put("expertGid", courseExpert.getGid());
				ejo.put("expertName", courseExpert.getName());
				ejo.put("expertHospital", courseExpert.getHospital());
				ejo.put("expertAvatar", courseExpert.getAvatar());
				eja.add(ejo);
			}
			jo.put("expert", eja);
			Map<String, String> map = new HashMap<String, String>();
			map.put("cvgid", course.getGid());
			JSONArray aja = new JSONArray();
			List<CourseAnnocement> alist = courseAnnouncementService
					.getConditonList(map, "", false, null);
			for (CourseAnnocement courseAnnocement : alist) {
				JSONObject ajo = new JSONObject();
				ajo.put("gid", courseAnnocement.getGid());
				ajo.put("name", courseAnnocement.getContent());
				ajo.put("url", courseAnnocement.getUrl());
				ajo.put("time", sdf.format(courseAnnocement.getUpdateTime()));
				aja.add(ajo);
			}

			jo.put("announcement", aja);
			jo.put("title", course.getTitle());
			// 添加分享地址
			if(course.getPrice()>0){
				jo.put("shareAddress",getProperByName("coursePayUrl")+gid);
			}else{
				jo.put("shareAddress",getProperByName("courseUrl")+gid);
			}
			
			// 只要调用一次接口watchNumber加一
			Integer watchNumber = course.getWatchNumber();
			watchNumber += new java.util.Random().nextInt(5)+1;
			course.setWatchNumber(watchNumber);
			courseService.update(course);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			// 根据登陆的用和判断是否收藏“1”收藏“0”没有收藏
			if (logined && resultList.size() > 0) {
				jo.put("favourite", "1");

			} else {
				jo.put("favourite", "0");
			}

			ret.put("result_data", jo);

		} catch (Exception e) {
			logger.error("", e);

			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 查询视频详情
	// 视频gid
	public void queryVideoDetail() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONObject jo = new JSONObject();
			CourseVideo courseVideo = courseVideoService.get(gid);
			List resultList = new ArrayList();
			if (logined) {
				String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
				List paraList = new ArrayList();
				paraList.add(courseVideo.getGid());
				paraList.add(request.getHeader("userid"));
				paraList.add("1");
				resultList = favouriteService
						.HQLSqlBindParameter(hql, paraList);
			}

			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("gid", courseVideo.getGid());
			List<Favourite> conditonList = favouriteService.getConditonList(
					map1, "", false, null);

			jo.put("introduction", courseVideo.getIntroduction());
			JSONArray eja = new JSONArray();
			String[] strs = courseVideo.getExpertGid().split(",");
			for (String string : strs) {
				CourseExpert courseExpert = courseExpertService.get(string);
				JSONObject ejo = new JSONObject();
				ejo.put("expertGid", courseExpert.getGid());
				ejo.put("expertName", courseExpert.getName());
				ejo.put("expertHospital", courseExpert.getHospital());
				ejo.put("expertAvatar", courseExpert.getAvatar());
				eja.add(ejo);
			}
			jo.put("expert", eja);
			Map<String, String> map = new HashMap<String, String>();

			map.put("cvgid", courseVideo.getGid());
			JSONArray aja = new JSONArray();
			List<CourseAnnocement> alist = courseAnnouncementService
					.getConditonList(map, "", false, null);
			for (CourseAnnocement courseAnnocement : alist) {
				JSONObject ajo = new JSONObject();
				ajo.put("gid", courseAnnocement.getGid());
				ajo.put("name", courseAnnocement.getContent());
				ajo.put("url", courseAnnocement.getUrl());
				ajo.put("time", sdf.format(courseAnnocement.getUpdateTime()));
				aja.add(ajo);
			}

			jo.put("videoUrl", courseVideo.getVideoUrl());
			jo.put("avatar", courseVideo.getAvatar());
			jo.put("videoTraffic", courseVideo.getSize());
			jo.put("timerLength", courseVideo.getDuration());
			jo.put("announcement", aja);
			jo.put("title", courseVideo.getTitle());
			jo.put("shareAddress",getProperByName("videoUrl")+gid);
			// 只要调用一次接口watchNumber加一
			Integer watchNumber = courseVideo.getWatchNumber();
			watchNumber++;
			courseVideo.setWatchNumber(watchNumber);
			courseVideoService.update(courseVideo);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			// 根据登陆的用和判断是否收藏“1”收藏“0”没有收藏
			if (logined && resultList.size() > 0) {
				jo.put("favourite", "1");

			} else {
				jo.put("favourite", "0");
			}
			ret.put("result_data", jo);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 查询慕课视频
	// 慕课gid gid
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void queryCourseVideo() {
		JSONObject ret = new JSONObject();
		try {
			// 判斷當前慕課是否被收藏
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			List resultList = new ArrayList();
			JSONArray ja = new JSONArray();
			Course course = courseService.get(gid);
			if (logined) {
				String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
				List paraList = new ArrayList();
				paraList.add(course.getGid());
				paraList.add(request.getHeader("userid"));
				paraList.add("1");
				resultList = favouriteService
						.HQLSqlBindParameter(hql, paraList);
			}
			// 獲得該慕課的專家
			JSONArray expertJa = new JSONArray();
			// 后来给周冉添加的专家的信息
			String[] strs = course.getExpertGid().split(",");
			if (strs.length == 0) {
				throw new CustomException("数据库专家为空");
			}
			for (String string : strs) {
				CourseExpert courseExpert = courseExpertService.get(string);
				JSONObject ejo = new JSONObject();
				ejo.put("expertGid", courseExpert.getGid());
				ejo.put("expertName", courseExpert.getName());
				ejo.put("expertHospital", courseExpert.getHospital());
				ejo.put("expertAvatar", courseExpert.getAvatar());
				expertJa.add(ejo);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("parentGid", course.getGid());
			List<Course> clist = courseService.getConditonList(map, "sequence",
					false, null);
			for (Course course2 : clist) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				JSONObject jo = new JSONObject();
				jo.put("title", course2.getTitle());
				// 分享的地址
				jo.put("shareAddress",
						"http://a.app.qq.com/o/simple.jsp?pkgname=com.naton.bonedict");
				// 如果登陸了並且有收藏則返回收藏
				if (logined && resultList.size() > 0) {
					// 收藏返回“1”
					jo.put("favourite", "1");
				} else {
					jo.put("favourite", "0");
				}
				Set<CourseVideo> cv = course2.getCourseVideo();
				// 存放视频和专家的list
				JSONArray vja = new JSONArray();

				// 获得慕课下面的视频的列表
				for (CourseVideo courseVideo : cv) {
					JSONObject vjo = new JSONObject();
					vjo.put("gid", courseVideo.getGid());
					vjo.put("avatar", courseVideo.getAvatar());
					vjo.put("title", courseVideo.getTitle());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("updateTime",
							sdf.format(courseVideo.getUpdateTime()));
					vjo.put("videoTraffic", courseVideo.getSize());
					vjo.put("timerLength", courseVideo.getDuration());
					vja.add(vjo);
				}
				// 将专家和视频的list添加到结果里
				jo.put("expert", expertJa);
				jo.put("list", vja);

				ja.add(jo);
			}

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", ja);
		} catch (Exception e) {
			System.out.println(e.toString());
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	// 更新慕课下视频观看数量 （收费课程使用）
	public void updateWatchNumber(){
		JSONObject ret = new JSONObject();
		try {
			courseVideoService.updateWatchNumber(gid);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 收藏课程或者视频
	// gid 慕课或者视频gid
	// type 类型
	public void collectCourse() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			// 1是慕课
			// 2是视频
			if ("1".equals(type))
				type = "4";
			if ("2".equals(type))
				type = "5";
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("gid", gid);
			condition.put("userGid", userId);
			condition.put("type", type);
			List<Favourite> retList = favouriteService.getConditonList(
					condition, "", false, null);
			if (retList.size() > 0) {
				Favourite fav = retList.get(0);
				int isdel = 0;
				isdel = Integer.parseInt(fav.getIsdel());
				isdel = (isdel + 1) % 2;
				fav.setIsdel(isdel + "");
				favouriteService.update(fav);
			} else {
				Date now = new Date();
				Favourite fav = new Favourite();
				fav.setGid(gid);
				fav.setCreateTime(now);
				fav.setUpdateTime(now);
				fav.setUserGid(userId);
				fav.setType(type);
				fav.setIsdel("1");
				favouriteService.save(fav);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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

	// 专家详情
	// 专家gid gid
	public void queryExpertDetailInfo() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			String phone = "";
			if(!TextUtils.isEmpty(userId)){
				User user = userService.get(userId);
				phone = user.getPhone();
			}
			boolean logined = userTokenService.checkToken(request);
			HashSet<String> courseSet = new HashSet<String>();
			courseSet.add("c99dd05b-3da1-ad78-a8c7-28e5b6061b10");
			HashSet<String> VideoSet = new HashSet<String>();
			
			
			
			
			CourseExpert courseExpert = courseExpertService.get(gid);
			JSONObject jo = new JSONObject();
			jo.put("gid", courseExpert.getGid());
			jo.put("name", courseExpert.getName());
			jo.put("avatar", courseExpert.getAvatar());
			jo.put("introduction", courseExpert.getIntroduction());

			JSONArray cja = new JSONArray();
			String hql = "select * from course where expertGid like '%"+gid+"%'";
			if (charge==null) {
				 hql = "select * from course where expertGid like '%"+gid+"%' and price=0";
			}
			List<Course> courseList = courseService.getVoListBySql(hql);
			for (Course course : courseList) {
				
				
				if (!logined||!SmsUtil.checkPhone(phone)){
					if (courseSet.contains(course.getGid())) {
						continue;
					}
					
				}
				
				
				JSONObject cjo = new JSONObject();
				cjo.put("gid", course.getGid());
				cjo.put("avatar", course.getAvatar());
				cjo.put("title", course.getTitle());
				cjo.put("total", course.getTotalNumber());
				cjo.put("current", course.getCurrentNumber());
				cjo.put("watchNum", course.getWatchNumber());
				cjo.put("type", "1");
				cja.add(cjo);
			}
			JSONArray vja = new JSONArray();
			String hql2 = "select  * from courseVideo where expertGid like '%"+gid+"%' ";
			if (charge==null) {
				 hql2 = "select * from courseVideo where expertGid like '%"+gid+"%' and price=0";
			}
			
			List<CourseVideo> courseVideoList = courseVideoService.getVoListBySql(hql2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (CourseVideo courseVideo : courseVideoList) {
				
				if (!logined||!SmsUtil.checkPhone(phone)){
					if (VideoSet.contains(courseVideo.getGid())) {
						continue;
					}
					
				}
				
				
				
				JSONObject vjo = new JSONObject();
				vjo.put("gid", courseVideo.getGid());
				vjo.put("avatar", courseVideo.getAvatar());
				vjo.put("title", courseVideo.getTitle());
				vjo.put("watchNum", courseVideo.getWatchNumber());
				vjo.put("videoUrl", courseVideo.getVideoUrl());
				vjo.put("downloadUrl", courseVideo.getDownloadUrl());
				vjo.put("updateTime", sdf.format(courseVideo.getUpdateTime()));
				vjo.put("type", "2");
				vja.add(vjo);
			}
			jo.put("courseList", cja);
			jo.put("classList", vja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jo);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 上线之后自己修改的（自己测试已经完成）
	// 课程列表
	// keyword(关键字默认是“”)
	// 类型 String type（默认是空串“”）;
	// 视频or慕课 String category（0-“全部”1-“慕课”2-“视频”）;
	// 排序 String rank（默认是updatetime）;
	// 新增commentNumber排序的方法
	public void queryCourseList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			String phone = "";
			if(!TextUtils.isEmpty(userId)){
				User user = userService.get(userId);
				phone = user.getPhone();
			}
			
			MyPage myPage=null;
			String limitString="";
			if (pageNum!=null) {
				int first=(pageNum-1)*20;
				limitString=" limit  "+first+" ,20";
				myPage = new MyPage();
				List<Object> max=new ArrayList<Object>();
				max.add(20);
				myPage.setPage(pageNum);
				myPage.setRows(max);
			}
			boolean logined = userTokenService.checkToken(request);
			HashSet<String> courseSet = new HashSet<String>();
			courseSet.add("c99dd05b-3da1-ad78-a8c7-28e5b6061b10");
			HashSet<String> videoSet = new HashSet<String>();
			List<Conditions> list = new ArrayList<Conditions>();
			// like模糊查询 title的名称
			Conditions conditions = new Conditions();
			conditions.setType("like");
			conditions.setKey("title");
			// 增加条件如果keyword为空格的话自动将去转为“”，提高代码的健壮性，'%%'不同于‘%
			// %’只查询包含空格的title，而是全部查询
			keyword = keyword.trim();
			conditions.setValue(keyword);
			list.add(conditions);
			// 按照类型只查询顶级的幕课
			Conditions conditions2 = new Conditions();
			conditions2.setType("map");
			Map<String, String> map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(type))
				map.put("type", type);
			map.put("parentGid", "0");
			if (charge==null||"1.6.0".equals(ios_version)) {
				map.put("price", "0");
			}
			conditions2.setMap(map);
			list.add(conditions2);
			List<Course> courseList = new ArrayList<Course>();
			List<CourseVideo> courseVideoList = new ArrayList<CourseVideo>();
			// 如果rank的值为ommentNumber时使用sql语句查询
			if (rank != null && "commentNumber".equals(rank)) {
				// 当category值为'0'and'1'时，course一定查询
				if (!"2".equals(category)) {
					String sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from course c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and  a.parentGid='0' order by commentNnum DESC";
					
					if (charge==null) {
						 sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from course c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and  a.parentGid='0' and a.price=0 order by commentNnum DESC";

					}
					sql+=limitString;
					courseList = courseService.executeSql(sql,
							'%' + keyword + '%', '%' + type + '%');
				}
				// 当category值为'0'and'2'时，courseVideo一定查询
				if (!"1".equals(category)) {
					String sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from courseVideo c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and a.parentGid='0'  order by commentNnum DESC";
					
					if (charge==null) {
						sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from courseVideo c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and a.parentGid='0' and a.price=0  order by commentNnum DESC";

					}
					
					sql+=limitString;
					courseVideoList = courseVideoService.executeSql(sql,
							'%' + keyword + '%', '%' + type + '%');
				}

			} else {
				// 当category值为'0'and'1'时，course一定查询
				if (!"2".equals(category))
					courseList = courseService.getConditonsList(list, rank,
							true, myPage);
				// 当category值为'0'and'2'时，courseVideo一定查询
				if (!"1".equals(category))
					courseVideoList = courseVideoService.getConditonsList(list,
							rank, true, myPage);
			}

			JSONArray cja = new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (!"2".equals(category)) {
				for (Course course : courseList) {
					JSONObject cjo = new JSONObject();
					if (!logined||!SmsUtil.checkPhone(phone)){
						if (courseSet.contains(course.getGid())) {
							continue;
						}
						
					}
					cjo.put("gid", course.getGid());
					cjo.put("price", course.getPrice());
					cjo.put("avatar", course.getAvatar());
					cjo.put("title", course.getTitle());
					cjo.put("total", course.getTotalNumber());
					cjo.put("current", course.getCurrentNumber());
					String expertGids = course.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					String[] split = expertGids.split(",");
					try {
						for (String gid : expertGids.split(",")) {
							stringBuffer.append(
									courseExpertService.get(gid).getName())
									.append(",");
						}
						stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					} catch (Exception e) {
						throw new CustomException("该专家不存在");
					}
					cjo.put("expertName", stringBuffer.toString());
					cjo.put("watchNum", course.getWatchNumber());
					cjo.put("type", "1");
					cjo.put("updateTime", sdf.format(course.getUpdateTime()));
					cja.add(cjo);
				}
			}
			// 遍历之后保存到数列里面
			JSONArray vja = new JSONArray();
			if (!"1".equals(category)) {
				for (CourseVideo courseVideo : courseVideoList) {
					
					if (!logined||!SmsUtil.checkPhone(phone)){
						if (videoSet.contains(courseVideo.getGid())) {
							continue;
						}
					}
			
						
					
					JSONObject vjo = new JSONObject();
					vjo.put("gid", courseVideo.getGid());
					vjo.put("avatar", courseVideo.getAvatar());
					vjo.put("price", courseVideo.getPrice());
					vjo.put("title", courseVideo.getTitle());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vjo.put("type", "2");

					String expertGids = courseVideo.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					try {
						for (String gid : expertGids.split(",")) {
							stringBuffer.append(
									courseExpertService.get(gid).getName())
									.append(",");
						}
						stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					} catch (Exception e) {
						throw new CustomException("该专家不存在");
					}
					vjo.put("expertName", stringBuffer.toString());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("updateTime",
							sdf.format(courseVideo.getUpdateTime()));
					vja.add(vjo);
				}
			}
			JSONObject res = new JSONObject();
			res.put("courseList", cja);
			res.put("videoList", vja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", res);
		} catch (CustomException e) {
			ret.put("code", "0");
			System.out.println(e.toString());
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
	
	
	public void queryContentList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			String phone = "";
			if(!TextUtils.isEmpty(userId)){
				User user = userService.get(userId);
				phone = user.getPhone();
			}
			boolean logined = userTokenService.checkToken(request);
			HashSet<String> courseSet = new HashSet<String>();
			courseSet.add("c99dd05b-3da1-ad78-a8c7-28e5b6061b10");
			HashSet<String> videoSet = new HashSet<String>();
			List<Conditions> list = new ArrayList<Conditions>();
			// like模糊查询 title的名称
			Conditions conditions = new Conditions();
			conditions.setType("like");
			conditions.setKey("title");
			// 增加条件如果keyword为空格的话自动将去转为“”，提高代码的健壮性，'%%'不同于‘%
			// %’只查询包含空格的title，而是全部查询
			keyword = keyword.trim();
			conditions.setValue(keyword);
			list.add(conditions);
			// 按照类型只查询顶级的幕课
			Conditions conditions2 = new Conditions();
			conditions2.setType("map");
			Map<String, String> map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(type))
				map.put("type", type);
			map.put("parentGid", "0");
			if (charge==null||"1.6.0".equals(ios_version)) {
				map.put("price", "0");
			}
			conditions2.setMap(map);
			list.add(conditions2);
			List<Course> courseList = new ArrayList<Course>();
			List<CourseVideo> courseVideoList = new ArrayList<CourseVideo>();
			// 如果rank的值为ommentNumber时使用sql语句查询
			if (rank != null && "commentNumber".equals(rank)) {
				// 当category值为'0'and'1'时，course一定查询
				if (!"2".equals(category)) {
					String sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from course c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and  a.parentGid='0' order by commentNnum DESC";
					
					if (charge==null) {
						 sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from course c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and  a.parentGid='0' and a.price=0 order by commentNnum DESC";

					}
					courseList = courseService.executeSql(sql,
							'%' + keyword + '%', '%' + type + '%');
				}
				// 当category值为'0'and'2'时，courseVideo一定查询
				if (!"1".equals(category)) {
					String sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from courseVideo c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and a.parentGid='0'  order by commentNnum DESC";
					
					if (charge==null) {
						sql = "select * from(select distinct c.*,COUNT(c.gid) as commentNnum from courseVideo c left join courseReply  r on c.gid=r.courseGid GROUP BY c.gid ) as a where a.title like ? and a.type like ? and a.parentGid='0' and a.price=0  order by commentNnum DESC";

					}
					
					
					courseVideoList = courseVideoService.executeSql(sql,
							'%' + keyword + '%', '%' + type + '%');
				}

			} else {
				// 当category值为'0'and'1'时，course一定查询
				if (!"2".equals(category))
					courseList = courseService.getConditonsList(list, rank,
							true, null);
				// 当category值为'0'and'2'时，courseVideo一定查询
				if (!"1".equals(category))
					courseVideoList = courseVideoService.getConditonsList(list,
							rank, true, null);
			}

			JSONArray cja = new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (!"2".equals(category)) {
				for (Course course : courseList) {
					JSONObject cjo = new JSONObject();
					if (!logined||!SmsUtil.checkPhone(phone)){
						if (courseSet.contains(course.getGid())) {
							continue;
						}
						
					}
					cjo.put("gid", course.getGid());
					cjo.put("price", course.getPrice());
					cjo.put("avatar", course.getAvatar());
					cjo.put("title", course.getTitle());
					cjo.put("total", course.getTotalNumber());
					cjo.put("current", course.getCurrentNumber());
					String expertGids = course.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					String[] split = expertGids.split(",");
					try {
						for (String gid : expertGids.split(",")) {
							stringBuffer.append(
									courseExpertService.get(gid).getName())
									.append(",");
						}
						stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					} catch (Exception e) {
						throw new CustomException("该专家不存在");
					}
					cjo.put("expertName", stringBuffer.toString());
					cjo.put("watchNum", course.getWatchNumber());
					cjo.put("type", "1");
					cjo.put("updateTime", sdf.format(course.getUpdateTime()));
					cja.add(cjo);
				}
			}
			// 遍历之后保存到数列里面
			JSONArray vja = new JSONArray();
			if (!"1".equals(category)) {
				for (CourseVideo courseVideo : courseVideoList) {
					
					if (!logined||!SmsUtil.checkPhone(phone)){
						if (videoSet.contains(courseVideo.getGid())) {
							continue;
						}
					}
			
						
					
					JSONObject vjo = new JSONObject();
					vjo.put("gid", courseVideo.getGid());
					vjo.put("avatar", courseVideo.getAvatar());
					vjo.put("price", courseVideo.getPrice());
					vjo.put("title", courseVideo.getTitle());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vjo.put("type", "2");

					String expertGids = courseVideo.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					try {
						for (String gid : expertGids.split(",")) {
							stringBuffer.append(
									courseExpertService.get(gid).getName())
									.append(",");
						}
						stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					} catch (Exception e) {
						throw new CustomException("该专家不存在");
					}
					vjo.put("expertName", stringBuffer.toString());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("updateTime",
							sdf.format(courseVideo.getUpdateTime()));
					vja.add(vjo);
				}
			}
			JSONObject res = new JSONObject();
			res.put("courseList", cja);
			res.put("videoList", vja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", res);
		} catch (CustomException e) {
			ret.put("code", "0");
			System.out.println(e.toString());
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

	// 新增回复
	// userId:"用户GID",
	// token:token",
	// beReplyGid:"被回复的评论GID",
	// content:"回复内容",
	// gid:"幕课GID",
	// type:"1"
	public void addComment() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}
			// 保存评论
			// String removeFourCharContent= removeFourChar(content);
			String filterContent = emojiFilter(content);
			CourseReply courseReply = new CourseReply();
			courseReply.setBeReplyGid(beReplyGid);
			courseReply.setContent(filterContent);
			courseReply.setCourseGid(gid);
			courseReply.setCreateTime(new Date());
			courseReply.setLocation(location);
			courseReply.setUserGid(userId);
			courseReply.setType(type);
			String courseReplyGid = ComUtil.getuuid();
			courseReply.setGid(courseReplyGid);
			courseReplyService.save(courseReply);
			// 即可获得评论的内容返回给前端
			CourseReply immediateCourseReply = courseReplyService
					.get(courseReplyGid);
			JSONObject jo = new JSONObject();
			if (immediateCourseReply != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String createTime = sdf.format(immediateCourseReply
						.getCreateTime());
				String userGid = immediateCourseReply.getUserGid();
				User user = userService.get(userGid);
				jo.put("gid", immediateCourseReply.getGid());
				jo.put("userId", immediateCourseReply.getUserGid());
				jo.put("userName", user.getName());
				jo.put("avatar", user.getAvatars());
				// 表情的转换
				String getFilterContent = immediateCourseReply.getContent();
				String emojiRecovery = emojiRecovery(getFilterContent);
				jo.put("content", emojiRecovery);
				jo.put("createTime",
						sdf.format(immediateCourseReply.getCreateTime()));
				jo.put("location", immediateCourseReply.getLocation());
				jo.put("beReplyUserId", immediateCourseReply.getBeReplyGid());
				if (immediateCourseReply.getBeReplyGid() != null
						&& !immediateCourseReply.getBeReplyGid().trim()
								.equals("")) {
					String BeReplyUserGid = (courseReplyService
							.get(immediateCourseReply.getBeReplyGid()))
							.getUserGid();
					User beReplyUser = userService.get(BeReplyUserGid);
					jo.put("beReplyUserName", beReplyUser.getName());
					jo.put("beReplyAvatar", beReplyUser.getAvatars());
					// 表情的转换
					String beReplyContent = courseReplyService.get(
							immediateCourseReply.getBeReplyGid()).getContent();
					String beReplyContentEmojiRecovery = emojiRecovery(beReplyContent);
					jo.put("beReplyContent", beReplyContentEmojiRecovery);
				}

			}
			ret.put("code", "1");
			ret.put("message", "新增成功");
			ret.put("result_data", JSONObject.fromObject(jo));
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	// 查询课程或视频下的讨论列表
	// userId:"用户GID",
	// token:"token",
	// gid:"1",
	// type:"1"
	public void queryCommentList() {
		JSONObject ret = new JSONObject();
		try {
			JSONArray ja = new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("courseGid", gid);
			List<CourseReply> list = courseReplyService.getConditonList(
					condition, "createTime", false, null);
			if (!list.isEmpty() && list.size() > 0) {
				for (CourseReply courseReply : list) {
					JSONObject jo = new JSONObject();
					String createTime = sdf.format(courseReply.getCreateTime());
					String userId = courseReply.getUserGid();
					User user = userService.get(userId);
					if(user==null)throw new CustomException("评论的用户被删除");
					jo.put("gid", courseReply.getGid());
					jo.put("userId", courseReply.getUserGid());
					jo.put("userName", user.getName());
					jo.put("avatar", user.getAvatars());
					jo.put("content", emojiRecovery(courseReply.getContent()));
					jo.put("createTime",
							sdf.format(courseReply.getCreateTime()));
					jo.put("location", courseReply.getLocation());
					jo.put("beReplyUserId", courseReply.getBeReplyGid());
					if (courseReply.getBeReplyGid() != null
							&& !courseReply.getBeReplyGid().trim().equals("")) {
						CourseReply courseReply2 = courseReplyService
								.get(courseReply.getBeReplyGid());
						if (courseReply2 == null)
							throw new CustomException("被恢复的评论被删除");
						String userGid = courseReply2.getUserGid();
						User beReplyUser = userService.get(userGid);
						jo.put("beReplyUserName", beReplyUser.getName());
						jo.put("beReplyAvatar", beReplyUser.getAvatars());
						jo.put("beReplyContent",
								emojiRecovery(courseReplyService.get(
										courseReply.getBeReplyGid())
										.getContent()));
					}
					ja.add(jo);
				}
			}
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", ja);
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

	// 通知的添加
	// replyGid:评论的gid
	// pstGid:幕课的gid
	// userGid:被回复的人的gid
	// isRaed:判断是否阅读“1”已读/0未读
	// CreateTime:创建的时间
	// type：“2”幕课“1”评论
	public void addNotice() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}

			Notice notice = new Notice();
			Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(createTime);
			notice.setCreateTime(time);
			notice.setIsRead("0");
			notice.setPostId(pstGid);
			notice.setType(type);
			notice.setUserId(userGid);
			notice.setReplyId(replyGid);
			notice.setUpdateTime(time);
			notice.setIsdel("");
			noticeService.save(notice);
			ret.put("code", "1");
			ret.put("message", "新增成功");
			ret.put("result_data", "");
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());

	}

	// 查询通知信息
	@SuppressWarnings("unchecked")
	public void queryNoticeList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}
			String hql = "from Notice n,CourseReply c where n.replyId =c.gid and n.isRead=? and n.userId=?";
			@SuppressWarnings("rawtypes")
			List paraList = new ArrayList();
			paraList.add("0");
			paraList.add(userId);
			@SuppressWarnings("rawtypes")
			List hqlSqlBindParameter = noticeService.HQLSqlBindParameter(hql,
					paraList);
			JSONArray ja = new JSONArray();
			for (Object object : hqlSqlBindParameter) {
				JSONObject jsonObject = new JSONObject();
				Object[] objectArray = (Object[]) object;
				Notice notice = (Notice) objectArray[0];
				CourseReply courseReply = (CourseReply) objectArray[1];
				notice.setIsRead("1");
				noticeService.update(notice);
				jsonObject.put("userId", userId);
				jsonObject.put("replyId", notice.getReplyId());
				jsonObject.put("pstId", notice.getPostId());
				jsonObject.put("replyContent", courseReply.getContent());
				ja.add(jsonObject);

			}
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", ja);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}

		putDataOut(ret.toString());
	}

	public void queryVideoInfo() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CourseVideo courseVideo = courseVideoService.get(gid);
			JSONObject vjo = new JSONObject();
			vjo.put("gid", courseVideo.getGid());
			vjo.put("avatar", courseVideo.getAvatar());
			vjo.put("title", courseVideo.getTitle());
			vjo.put("watchNum", courseVideo.getWatchNumber());
			vjo.put("videoUrl", courseVideo.getVideoUrl());
			vjo.put("downloadUrl", courseVideo.getDownloadUrl());
			vjo.put("updateTime", sdf.format(courseVideo.getUpdateTime()));
			vjo.put("videoTraffic", courseVideo.getSize());
			vjo.put("timerLength", courseVideo.getDuration());
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", vjo);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}

		putDataOut(ret.toString());

	}

	// 臧亮
	public void queryCourseCollection() {
		JSONObject ret = new JSONObject();
		// 确定用户是否登陆
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}
			// 创建json承载对象
			JSONObject result_data = new JSONObject();
			JSONArray courseList = new JSONArray();
			JSONArray videoList = new JSONArray();
			// 创建时间样板
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 查询用户的所有的收藏Search

			String hql = "from Favourite f where f.userGid=? and f.isdel=1 and (f.type=4 or f.type=5 )";
			List paraList = new ArrayList();
			paraList.add(request.getHeader("userid"));
			List<Favourite> resultList = favouriteService.HQLSqlBindParameter(
					hql, paraList);
			// 判断收藏的类别然后遍历保存保存幕课和视频
			for (Favourite favourite : resultList) {
				String type = favourite.getType();
				String courseGid = favourite.getGid();
				if (type.equals("4") && (courseService.get(courseGid)) != null) {
					// 保存幕课信息到cjo
					JSONObject cjo = new JSONObject();
					Course course = courseService.get(favourite.getGid());
					cjo.put("gid", course.getGid());
					cjo.put("avatar", course.getAvatar());
					cjo.put("title", course.getTitle());
					String expertGids = course.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					for (String gid : expertGids.split(",")) {
						stringBuffer.append(
								courseExpertService.get(gid).getName()).append(
								",");
					}
					stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					cjo.put("expertName", stringBuffer.toString());
					cjo.put("watchNum", course.getWatchNumber());
					cjo.put("type", "1");
					cjo.put("total", course.getTotalNumber());
					cjo.put("current", course.getCurrentNumber());
					cjo.put("updateTime", sdf.format(course.getUpdateTime()));
					courseList.add(cjo);

				} else if (type.equals("5")
						&& (courseVideoService.get(courseGid)) != null) {
					// 保存视频信息到json
					JSONObject vjo = new JSONObject();
					CourseVideo courseVideo = courseVideoService.get(favourite
							.getGid());
					vjo.put("gid", courseVideo.getGid());
					vjo.put("avatar", courseVideo.getAvatar());
					vjo.put("title", courseVideo.getTitle());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vjo.put("type", "2");

					String expertGids = courseVideo.getExpertGid();
					StringBuffer stringBuffer = new StringBuffer();
					for (String gid : expertGids.split(",")) {
						stringBuffer.append(
								courseExpertService.get(gid).getName()).append(
								",");
					}
					stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					vjo.put("expertName", stringBuffer.toString());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("updateTime",
							sdf.format(courseVideo.getUpdateTime()));
					videoList.add(vjo);

				}
			}
			// 返回结果信息
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			result_data.put("courseList", courseList);
			result_data.put("videoList", videoList);
			ret.put("result_data", result_data);

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

	// 查询视频详情
	// 视频gid
	public void querySingleVideoDetail() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONObject jo = new JSONObject();
			CourseVideo courseVideo = courseVideoService.get(gid);
			List resultList = new ArrayList();
			if (logined) {
				String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
				List paraList = new ArrayList();
				paraList.add(courseVideo.getGid());
				paraList.add(request.getHeader("userid"));
				paraList.add("1");
				resultList = favouriteService
						.HQLSqlBindParameter(hql, paraList);
			}

			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("gid", courseVideo.getGid());
			List<Favourite> conditonList = favouriteService.getConditonList(
					map1, "", false, null);

			jo.put("introduction", courseVideo.getIntroduction());
			JSONArray eja = new JSONArray();
			String[] strs = courseVideo.getExpertGid().split(",");
			for (String string : strs) {
				CourseExpert courseExpert = courseExpertService.get(string);
				JSONObject ejo = new JSONObject();
				ejo.put("expertGid", courseExpert.getGid());
				ejo.put("expertName", courseExpert.getName());
				ejo.put("expertHospital", courseExpert.getHospital());
				ejo.put("expertAvatar", courseExpert.getAvatar());
				eja.add(ejo);
				
			}
			jo.put("expert", eja);
			Map<String, String> map = new HashMap<String, String>();

			map.put("cvgid", courseVideo.getGid());
			JSONArray aja = new JSONArray();
			List<CourseAnnocement> alist = courseAnnouncementService
					.getConditonList(map, "", false, null);
			for (CourseAnnocement courseAnnocement : alist) {
				JSONObject ajo = new JSONObject();
				ajo.put("gid", courseAnnocement.getGid());
				ajo.put("name", courseAnnocement.getContent());
				ajo.put("url", courseAnnocement.getUrl());
				ajo.put("time", sdf.format(courseAnnocement.getUpdateTime()));
				aja.add(ajo);
			}

			jo.put("videoUrl", courseVideo.getVideoUrl());
			jo.put("avatar", courseVideo.getAvatar());
			jo.put("videoTraffic", courseVideo.getSize());
			jo.put("timerLength", courseVideo.getDuration());
			jo.put("announcement", aja);
			jo.put("title", courseVideo.getTitle());
			jo.put("shareAddress",getProperByName("videoUrl")+gid);
			
			// 只要调用一次接口watchNumber加一
			Integer watchNumber = courseVideo.getWatchNumber();
			watchNumber++;
			courseVideo.setWatchNumber(watchNumber);
			courseVideoService.update(courseVideo);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			// 根据登陆的用和判断是否收藏“1”收藏“0”没有收藏
			if (logined && resultList.size() > 0) {
				jo.put("favourite", "1");

			} else {
				jo.put("favourite", "0");
			}
			ret.put("result_data", jo);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}

	//慕课的视频列表
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void queryCourseVideoNewList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			// 判斷當前慕課是否被收藏
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (userId!=null&&!logined) {
				throw new CustomException(NO_LOGIN);
			}
			List resultList = new ArrayList(); 
			JSONArray ja = new JSONArray();
			Course course = courseService.get(gid);
			boolean courseOrder=false;
			boolean i=false;
			if(!logined){
				//没有购买用i=true
				i=true;
			}else{
				User user = userService.get(userId);
				Date annualmembership = user.getAnnualmembership();
				Date date = new Date();
				 long daysBetweenDate=date.getTime()-annualmembership.getTime();
				if (daysBetweenDate<0) {
					courseOrder=true;
				} else {

					List<OrderInfo> orderCourse = orderInfoService.getOrderCourse(userId,4,"1",gid);
					if(orderCourse.size()==0){
						i=true;
						List<OrderInfo> orderCourse1=orderInfoService.getOrderCourseOrNum(userId,4,"1",gid);
						courseOrder=(orderCourse1.size()==0?false:true);
					}else{
						courseOrder=true;
					}
				}
				
				
				
				
			}
			 HashSet<String> hashSet = new HashSet<String>();
			 HashSet<String> hashSetOrder = new HashSet<String>();
				if(logined&&i){ 
					List<OrderInfo> orderVideo2 = orderInfoService.getOrderCourse(userId,4,"2",gid);
					if(orderVideo2.size()!=0){
						for (OrderInfo orderInfo : orderVideo2) {
							String contactGids = orderInfo.getContactGids();
							String[] split = contactGids.split(",");
							for (String string : split) {
								hashSet.add(string);
								
							}
						}
						
					}
					List<OrderInfo> orderVideo3 = orderInfoService.getOrderCourseOrNum(userId,4,"2",gid);
					if(orderVideo3.size()!=0){
						for (OrderInfo orderInfo : orderVideo3) {
							String contactGids = orderInfo.getContactGids();
							String[] split = contactGids.split(",");
							for (String string : split) {
								hashSetOrder.add(string);
								
							}
						}
						
					}
				}
			
			if (logined) {
				String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
				List paraList = new ArrayList();
				paraList.add(course.getGid());
				paraList.add(request.getHeader("userid"));
				paraList.add("1");
				resultList = favouriteService
						.HQLSqlBindParameter(hql, paraList);
			}
			// 獲得該慕課的專家
			JSONArray expertJa = new JSONArray();
			// 后来给周冉添加的专家的信息
			String[] strs = course.getExpertGid().split(",");
			if (strs.length == 0) {
				throw new CustomException("数据库专家为空");
			}
			for (String string : strs) {
				CourseExpert courseExpert = courseExpertService.get(string);
				JSONObject ejo = new JSONObject();
				ejo.put("expertGid", courseExpert.getGid());
				ejo.put("expertName", courseExpert.getName());
				ejo.put("expertHospital", courseExpert.getHospital());
				ejo.put("expertAvatar", courseExpert.getAvatar());
				expertJa.add(ejo);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("parentGid", course.getGid());
			List<Course> clist = courseService.getConditonList(map, "sequence",
					false, null);
			for (Course course2 : clist) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				JSONObject jo = new JSONObject();
			
				jo.put("courseGid", course2.getGid());
				jo.put("title", course2.getTitle());
				jo.put("coureAvatar", course.getAvatar());
				jo.put("coureTitle", course.getTitle());
				jo.put("price", course.getPrice());
				jo.put("preprice", course.getPreprice());
				// 分享的地址
				if(course.getPrice()>0){ //付费课程
					jo.put("shareAddress",getProperByName("coursePayUrl")+gid);
				}else{
					jo.put("shareAddress",getProperByName("courseUrl")+gid);
				}
				
				// 如果登陸了並且有收藏則返回收藏
				
				
				
				if (logined && resultList.size() > 0) {
					// 收藏返回“1”
					jo.put("favourite", "1");
				} else {
					jo.put("favourite", "0");
				}
				
				
				if (i) {
					jo.put("isCoursePerchase", false);
					
				} else {
					jo.put("isCoursePerchase", true);
				}
				jo.put("courseOrder", courseOrder);
				jo.put("isPrice", course.getPrice()==0?false:true);
				
				
				Set<CourseVideo> cv = course2.getCourseVideo();
				// 存放视频和专家的list
				JSONArray vja = new JSONArray();
				

				// 获得慕课下面的视频的列表
				for (CourseVideo courseVideo : cv) {
					JSONObject vjo = new JSONObject();
					String gid2 = courseVideo.getGid();
					//只有在没有购买整套并且该视频订单里面没有，并且在登陆的状态下
					if (logined&&(!i||hashSet.contains(gid2))) {
						vjo.put("isPerchase",true);
						vjo.put("videoOrder",true);
					}else if(logined&&(!i||hashSetOrder.contains(gid2))){
						vjo.put("isPerchase",false);
						vjo.put("videoOrder",true);
					}
					
					
					else{
						vjo.put("isPerchase",false);
						vjo.put("videoOrder",false);
					}
					vjo.put("gid",gid2 );
					vjo.put("price", courseVideo.getPrice());
					vjo.put("avatar", courseVideo.getAvatar());
					vjo.put("title", courseVideo.getTitle());
					vjo.put("watchNum", courseVideo.getWatchNumber());
					vjo.put("videoUrl", courseVideo.getVideoUrl());
					vjo.put("downloadUrl", courseVideo.getDownloadUrl());
					vjo.put("updateTime",
							sdf.format(courseVideo.getUpdateTime()));
					vjo.put("videoTraffic", courseVideo.getSize());
					vjo.put("timerLength", courseVideo.getDuration());
					// 添加分享地址
					if(course.getPrice()>0){
						vjo.put("shareAddress",getProperByName("coursePayUrl")+gid);
					}else{
						vjo.put("shareAddress",getProperByName("courseUrl")+gid);
					}
					vja.add(vjo);
				}
				// 将专家和视频的list添加到结果里
				jo.put("expert", expertJa);
				jo.put("list", vja);
				ja.add(jo);
			}

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", ja);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONArray());
		}catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}
	
	
	
	
	
	// 1.5.6之前版本使用的接口
	// 慕课gid gid
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void queryCourseVideoList() {
		JSONObject ret = new JSONObject();
		try {
			// 判斷當前慕課是否被收藏
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			List resultList = new ArrayList();
			JSONArray ja = new JSONArray();
			Course course = courseService.get(gid);
			if(course.getPrice()>0){ // 收费课程须使用1.5.6及以后版本观看
				ret.put("code", "0");
				ret.put("message", "您的版本过低请及时更新");
				ret.put("result_data", new JSONArray());
			}else{
				if (logined) {
					String hql = "from Favourite f where f.gid =? and f.userGid=? and f.isdel=?";
					List paraList = new ArrayList();
					paraList.add(course.getGid());
					paraList.add(request.getHeader("userid"));
					paraList.add("1");
					resultList = favouriteService
							.HQLSqlBindParameter(hql, paraList);
				}
				// 獲得該慕課的專家
				JSONArray expertJa = new JSONArray();
				// 后来给周冉添加的专家的信息
				String[] strs = course.getExpertGid().split(",");
				if (strs.length == 0) {
					throw new CustomException("数据库专家为空");
				}
				for (String string : strs) {
					CourseExpert courseExpert = courseExpertService.get(string);
					JSONObject ejo = new JSONObject();
					ejo.put("expertGid", courseExpert.getGid());
					ejo.put("expertName", courseExpert.getName());
					ejo.put("expertHospital", courseExpert.getHospital());
					ejo.put("expertAvatar", courseExpert.getAvatar());
					expertJa.add(ejo);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("parentGid", course.getGid());
				List<Course> clist = courseService.getConditonList(map, "sequence",
						false, null);
				for (Course course2 : clist) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					JSONObject jo = new JSONObject();
					boolean i=false;
					if(!logined){
						jo.put("isPerchase", false);
					}else{
						
						List<OrderInfo> orderCourse = orderInfoService.getOrderCourse(userId,4,"1",course2.getGid());
						if(orderCourse.size()==0){
							jo.put("isPerchase", false);
							i=true;
						}else{
							jo.put("isPerchase", true);
						}
						
						
					}
					jo.put("title", course2.getTitle());
					jo.put("price", course2.getPrice());
					jo.put("price", course2.getPreprice());
					// 分享的地址
//					jo.put("shareAddress","http://123.59.57.67/muke.html?gid="+gid+"&type=1");
					if(course.getPrice()>0){
						jo.put("shareAddress",getProperByName("coursePayUrl")+gid);
					}else{
						jo.put("shareAddress",getProperByName("courseUrl")+gid);
					}
					// 如果登陸了並且有收藏則返回收藏
					if (logined && resultList.size() > 0) {
						// 收藏返回“1”
						jo.put("favourite", "1");
					} else {
						jo.put("favourite", "0");
					}
					Set<CourseVideo> cv = course2.getCourseVideo();
					// 存放视频和专家的list
					JSONArray vja = new JSONArray();
					ArrayList<String> arrayList= new ArrayList<String>();
					if(logined&&i){ 
						
						List<OrderInfo> orderVideo2 = orderInfoService.getOrderCourse(userId,4,"2",course2.getGid());
						if(orderVideo2.size()!=0){
							for (OrderInfo orderInfo : orderVideo2) {
								String contactGids = orderInfo.getContactGids();
								String[] split = contactGids.split(",");
								for (String string : split) {
									arrayList.add(string);
									
								}
							}
							
							
							HashSet h = new HashSet(arrayList);  
							arrayList.clear();  
							arrayList.addAll(h);  
						}
					}
					

					// 获得慕课下面的视频的列表
					for (CourseVideo courseVideo : cv) {
						JSONObject vjo = new JSONObject();
						String gid2 = courseVideo.getGid();
						//只有在没有购买整套并且该视频订单里面没有，并且在登陆的状态下
						if (logined&&(!i||arrayList.contains(gid2))) {
							vjo.put("isPerchase",true);
						}else{
							vjo.put("isPerchase",false);
						}
						vjo.put("gid",gid2 );
						vjo.put("price", courseVideo.getPrice());
						vjo.put("avatar", courseVideo.getAvatar());
						vjo.put("title", courseVideo.getTitle());
						vjo.put("watchNum", courseVideo.getWatchNumber());
						vjo.put("videoUrl", courseVideo.getVideoUrl());
						vjo.put("downloadUrl", courseVideo.getDownloadUrl());
						vjo.put("updateTime",
								sdf.format(courseVideo.getUpdateTime()));
						vjo.put("videoTraffic", courseVideo.getSize());
						vjo.put("timerLength", courseVideo.getDuration());
						if(course.getPrice()>0){
							vjo.put("shareAddress",getProperByName("coursePayUrl")+gid);
						}else{
							vjo.put("shareAddress",getProperByName("courseUrl")+gid);
						}
						vja.add(vjo);
					}
					// 将专家和视频的list添加到结果里
					jo.put("expert", expertJa);
					jo.put("list", vja);
					ja.add(jo);
				}

				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", ja);
			}
			
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}

	public String getUserId() {
		return userId;
	}

	public String getLocation() {
		return location;
	}

	public String getBeReplyGid() {
		return beReplyGid;
	}

	public String getContent() {
		return content;
	}

	public String getReplyGid() {
		return replyGid;
	}

	public String getPstGid() {
		return pstGid;
	}

	public String getUserGid() {
		return userGid;
	}

	public String getIsRaed() {
		return isRaed;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setBeReplyGid(String beReplyGid) {
		this.beReplyGid = beReplyGid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setReplyGid(String replyGid) {
		this.replyGid = replyGid;
	}

	public void setPstGid(String pstGid) {
		this.pstGid = pstGid;
	}

	public void setUserGid(String userGid) {
		this.userGid = userGid;
	}

	public void setIsRaed(String isRaed) {
		this.isRaed = isRaed;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.ConfService;
import com.sd.service.FavouriteService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.Conf;
import com.sd.vo.Favourite;
import com.sd.vo.Topic;
/**
 * 学术会议接口
 * @author baixf
 *
 */

public class ConsAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource private ConfService confService;
	@Resource private UserTokenService userTokenService;
	@Resource private FavouriteService favouriteService;
	
	private String dateBegin;
	private String selectType;
	private String count;
	private String isFav;
	private String gid;
	private String city;
	private String status;
	private String date;
	

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * 学术会议概要信息查询
	 * @param userId		用户GID
	 * @param token
	 * @param dateBegin		查询日期
	 * @param selectType	查询类型(1-大于当前日期/2-小于当前日期)
	 * @param count			显示条数
	 * @param isFav			是否收藏（0-所有/1-收藏的/2-已举办）
	 */
	public void confSummary(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined && "1".equals(isFav))
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			List<Conf> confList = confService.getConfList(dateBegin, selectType, count, isFav, userId);
			JSONArray data = new JSONArray();
			for (Conf conf : confList){
				JSONObject met = new JSONObject();
				met.put("gid", conf.getGid());
				met.put("local", conf.getLocal());
				met.put("date", ComUtil.dateTime2Str(conf.getDateBegin()));
				met.put("title", conf.getName());
				met.put("sponsor", conf.getSponsor());
				if (logined)
					met.put("isFav", favouriteService.isFav(conf.getGid(), userId));
				else
					met.put("isFav", "2");
				data.add(met);
			}
			if(confList.size()!=0){
				List<Conf> confList1 = confService.getConfList(sdf.format(confList.get(confList.size()-1).getDateBegin()), "0", "-1", isFav, userId);
				for (Conf conf : confList1){
					if(conf.getCreateTime().getTime()>confList.get(confList.size()-1).getDateBegin().getTime()){
						JSONObject met = new JSONObject();
						met.put("gid", conf.getGid());
						met.put("local", conf.getLocal());
						met.put("date", ComUtil.dateTime2Str(conf.getDateBegin()));
						met.put("title", conf.getName());
						met.put("sponsor", conf.getSponsor());
						if (logined)
							met.put("isFav", favouriteService.isFav(conf.getGid(), userId));
						else
							met.put("isFav", "2");
						data.add(met);
					}
				}
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
		}catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		}  catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 学术会议的筛选查询
	 * @param userId		用户GID
	 * @param token
	 * @param dateBegin		查询日期
	 * @param selectType	查询类型(1-大于当前日期/2-小于当前日期)
	 * @param count			显示条数
	 * @param isFav			是否收藏（0-所有/1-收藏的/2-已举办）
	 */
	public void queryConferenceList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if ((!logined && "1".equals(isFav))||(!logined && "2".equals(isFav)))
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			List<Conf> confList =confService.getConfListFactor(userId,count,isFav,status,city,date);
			JSONArray data = new JSONArray();
			for (Conf conf : confList){
				JSONObject met = new JSONObject();
				met.put("gid", conf.getGid());
				met.put("local", conf.getLocal());
				met.put("date", ComUtil.dateTime2Str(conf.getDateBegin()));
				met.put("title", conf.getName());
				met.put("sponsor", conf.getSponsor());
				if (logined)
					met.put("isFav", favouriteService.isFav(conf.getGid(), userId));
				else
					met.put("isFav", "2");
				data.add(met);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
		}catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONArray());
		}  catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 会议详细信息查询
	 * @param userId		用户GID
	 * @param token
	 * @param gid			会议GID
	 */
	public void confDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			Conf conf = confService.get(gid);
			if (conf == null)
				throw new CustomException(NO_CONF);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			
			JSONObject data = new JSONObject();
			data.put("name", conf.getName());
			data.put("date", ComUtil.dateTime2Str(conf.getDateBegin()));
			data.put("dateEnd", ComUtil.dateTime2Str(conf.getDateEnd()));
			data.put("local", conf.getLocal());
			data.put("localDetail", conf.getLocalDetail());
			data.put("sponsor", conf.getSponsor());
			data.put("detail", conf.getDetail());
			data.put("localGps", conf.getLocalGps());
			if (logined)
				data.put("isFav", favouriteService.isFav(conf.getGid(), userId));
			else
				data.put("isFav", "2");
			
			ret.put("result_data", data);
		} catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	/**
	 * 会议日程查询
	 * @param userId		用户GID
	 * @param token
	 * @param gid			会议GID
	 */
	public void confTopicSelect(){
		JSONObject ret = new JSONObject();
		try {
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			List<Topic> topicList = confService.getTopicList(gid, "1");
			JSONArray data = new JSONArray();
			for (Topic top : topicList){
				JSONObject met = new JSONObject();
				met.put("gid", top.getGid());
				met.put("moderator", top.getModerator());
				met.put("reviewer", top.getReviewer());
				met.put("sponsor", top.getSponsor());
				met.put("sponsorDesc", top.getSponsorDesc());
				met.put("title", top.getTitle());
				met.put("titleE", top.getTitlee());
				met.put("type", top.getType());
				met.put("datetimeBegin", ComUtil.dateTime2Str(top.getDatetimeBegin()));
				met.put("datetimeEnd", ComUtil.dateTime2Str(top.getDatetimeEnd()));
				
				List<Topic> topic2List = confService.getTopicList(top.getGid(), "2");
				JSONArray topics = new JSONArray();
				for (Topic top2 : topic2List){
					JSONObject topic = new JSONObject();
					topic.put("topicGid", top2.getGid());
					topic.put("title", top2.getTitle());
					topic.put("titleE", top2.getTitlee());
					topic.put("speaker", top2.getSpeaker());
					topic.put("datetimeBegin", ComUtil.dateTime2Str(top2.getDatetimeBegin()));
					topic.put("datetimeEnd", ComUtil.dateTime2Str(top2.getDatetimeEnd()));
					topics.add(topic);
				}
				met.put("topic", topics);
				data.add(met);
			}
			
			ret.put("result_data", data);
		} catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 学术会议收藏
	 * @param userId		用户GID
	 * @param token
	 * @param gid			会议GID
	 */
	public void confFav(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("gid", gid);
			condition.put("userGid", userId);
			condition.put("type", "2");
			List<Favourite> retList = favouriteService.getConditonList(condition, "", false, null);
			if (retList.size() > 0){
				Favourite fav = retList.get(0);
				int isdel = 0;
				isdel = Integer.parseInt(fav.getIsdel());
				isdel = (isdel+1)%2;
				fav.setIsdel(isdel+"");
				favouriteService.update(fav);
			}else{
				Date now = new Date();
				Favourite fav = new Favourite();
				fav.setGid(gid);
				fav.setCreateTime(now);
				fav.setUpdateTime(now);
				fav.setUserGid(userId);
				fav.setType("2");
				fav.setIsdel("1");
				favouriteService.save(fav);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
		} catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public UserTokenService getUserTokenService() {
		return userTokenService;
	}
	public void setUserTokenService(UserTokenService userTokenService) {
		this.userTokenService = userTokenService;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getIsFav() {
		return isFav;
	}
	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

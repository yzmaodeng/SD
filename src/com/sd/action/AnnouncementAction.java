package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AnnouncementService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.ActivityData;
import com.sd.vo.Announcement;
import com.sd.vo.Channel;
import com.sd.vo.Favourite;
import com.sd.vo.Posts;
import com.sd.vo.User;

public class AnnouncementAction extends BaseAction{
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource private AnnouncementService announcementService;
	private String type;
	private String gid;
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


	//暂时没有用
	public void searchAnnouncement(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			List<Announcement>list=announcementService.getAllList();
			JSONArray ja=new JSONArray();
			for (Announcement announcement : list) {
				JSONObject jo = new JSONObject();
				jo.put("gid", announcement.getGid());
				jo.put("content", announcement.getContent());
				jo.put("type", announcement.getType());
				ja.add(jo);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", ja);	
		}  catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	
	/**
	 * 公告的收藏和取消收藏
	 * @param userId
	 * @param token
	 * @param gid
	 * @param type
	 */
	public void annouFav(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if(type!=null&&type.equals("6")){
				Date now = new Date();
				Favourite fav = new Favourite();
				fav.setGid(gid);
				fav.setCreateTime(now);
				fav.setUpdateTime(now);
				fav.setUserGid(userId);
				fav.setType("6");
				fav.setIsdel("1");
				favouriteService.save(fav);
			}else{
				favouriteService.updateFav(gid, userId);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");
		}catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	
	
	
	
	
}

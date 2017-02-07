package com.sd.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.FavouriteService;
import com.sd.service.SubjectService;
import com.sd.service.TextBookService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.util.decoratedJSON;
import com.sd.vo.Course;
import com.sd.vo.CourseVideo;
import com.sd.vo.Favourite;
import com.sd.vo.Subject;
import com.sd.vo.Textbook;
/**
 * 骨科指南接口
 * @author bxf
 *
 */

public class TextBookAction extends BaseAction{
	private static final long serialVersionUID = 1820849025858684909L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource private TextBookService textBookService;
	@Resource private UserTokenService userTokenService;
	@Resource private FavouriteService favouriteService;
	@Resource private SubjectService subjectService;
	
	private String currId;	// 当前自增ID
	private String count;		// 查询个数
	private String keyword;		// 关键字
	private String category;	// 所属类型
	private String gid;			// 文章gid
	private String type;
	private String isFav;
	private String orderString;
	

	public String getOrderString() {
		return orderString;
	}
	public void setOrderString(String orderString) {
		this.orderString = orderString;
	}
	
	public void getCouponInfo(){
		JSONObject ret=new JSONObject();
		JSONObject data=new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}
			data.put("month", "12");
			data.put("money", "3998");
//			data.put("url", "http://academy.ufile.ucloud.com.cn/bigNian.jpg");
			data.put("url", "http://upload.orthoday.com/SDpic/common/picSelect?gid=302aebfd-7dda-486a-9e84-4175337ed899");
			data.put("desc", "开通骨今学院超级年会员，尽享超值回馈！");
			data.put("link", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc8fb10081c4a9f01&redirect_uri=http://bonetoday.com/vipActivity.html&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
			data.put("imgUrl", "http://academy.ufile.ucloud.com.cn/vip_share_180.png");
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (CustomException e) {
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
	public void getSubjectForWebPage(){
		JSONObject ret=new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			Subject pSubject =subjectService.get(gid);
			JSONObject data = new JSONObject();
			data.put("gid", pSubject.getGid());
			data.put("name", pSubject.getTitle());
			data.put("description",pSubject.getDescription());
			data.put("pic", pSubject.getPic());
			JSONArray ja=new JSONArray();
			Map<String, String> condition2=new HashMap<String, String>();
			condition2.put("parentGid", pSubject.getGid());
			List<Subject> list2=subjectService.getConditonList(condition2, "", false, null);
			
			for (Subject cSubject : list2) {
				JSONObject jo = new JSONObject();
				jo.put("mark", cSubject.getTitle());
				Map<String, String> condition3=new HashMap<String, String>();
				condition3.put("subjectGid", cSubject.getGid());
				List<Textbook> list3=textBookService.getConditonList(condition3, "", false, null);
				JSONArray jaTextbook=new JSONArray();
				for (Textbook textbook : list3) {
					JSONObject art = new JSONObject();
					art.put("tbId",textbook.getTbId());
					art.put("textbookGid", textbook.getGid());
					art.put("pic", textbook.getTbAvatars());
					art.put("publish", ComUtil.dateTime2Str(textbook.getTbCreatetime()));
					art.put("author", textbook.getTbAuthor());
					art.put("title",textbook.getTbTitle());
					art.put("readNumber",textbook.getReadNumber());
					art.put("subjectGid", textbook.getSubjectGid());
					art.put("selected", "1");
					art.put("url",textbook.getTbDetail());
					jaTextbook.add(art);
				}
				jo.put("textbookList", jaTextbook);
				ja.add(jo);
			}
			
			data.put("markList",ja);
			
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 获得专题
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void getSubject(){
		JSONObject ret=new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			Subject subject = subjectService.get(gid);
			Map<String, String> condition=new HashMap<String, String>();
			condition.put("gid", subject.getParentGid());
			List<Subject> list=subjectService.getConditonList(condition, "", false, null);
			Subject pSubject =list.get(0);
			JSONObject data = new JSONObject();
			data.put("gid", pSubject.getGid());
			data.put("name", pSubject.getTitle());
			data.put("description",pSubject.getDescription());
			data.put("pic", pSubject.getPic());
			JSONArray ja=new JSONArray();
			Map<String, String> condition2=new HashMap<String, String>();
			condition2.put("parentGid", pSubject.getGid());
			List<Subject> list2=subjectService.getConditonList(condition2, "", false, null);
			
			for (Subject cSubject : list2) {
				JSONObject jo = new JSONObject();
				jo.put("mark", cSubject.getTitle());
				Map<String, String> condition3=new HashMap<String, String>();
				condition3.put("subjectGid", cSubject.getGid());
				List<Textbook> list3=textBookService.getConditonList(condition3, "", false, null);
				JSONArray jaTextbook=new JSONArray();
				for (Textbook textbook : list3) {
					JSONObject art = new JSONObject();
					art.put("tbId",textbook.getTbId());
					art.put("textbookGid", textbook.getGid());
					art.put("pic", textbook.getTbAvatars());
					art.put("publish", ComUtil.dateTime2Str(textbook.getTbCreatetime()));
					art.put("author", textbook.getTbAuthor());
					art.put("title",textbook.getTbTitle());
					art.put("readNumber",textbook.getReadNumber());
					art.put("subjectGid", textbook.getSubjectGid());
					art.put("selected", "1");
					art.put("url",textbook.getTbDetail());
					jaTextbook.add(art);
				}
				jo.put("textbookList", jaTextbook);
				ja.add(jo);
			}
			
			data.put("markList",ja);
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 骨科指南查询
	 * @param userId
	 * @param token
	 * @param currId 当前自增ID
	 * @param count
	 * @param keyword
	 * @param category
	 */
	public void tbSummary(){
		JSONObject ret = new JSONObject();
		try {
			JSONArray data = new JSONArray();
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			if(isFav!=null){
				boolean logined = userTokenService.checkToken(request);
				if (!logined){
					throw new CustomException(NO_LOGIN);
				}
			}
			if(currId==null&&keyword==null&&isFav==null){
				//textBookService.query 轮转，当前ID，关键字，分类，是否收藏，用户ID
				List<Textbook> list=textBookService.query(1,currId,count,keyword,category,isFav,userId);
				String ss = new String("骨今中外");	
				for (int i=0; i<list.size(); i++){
					JSONObject art = new JSONObject();
					art.put("tbId",list.get(i).getTbId());
					art.put("textbookGid", list.get(i).getGid());
					art.put("pic", list.get(i).getTbAvatars());
					art.put("publish", ComUtil.dateTime2Str(list.get(i).getTbCreatetime()));
					if(list.get(i).getTbAuthor()==null||"".equals((list.get(i).getTbAuthor().trim()))||"".equals((list.get(i).getTbAuthor()))){
						art.put("author", ss);
					}else{
						art.put("author", list.get(i).getTbAuthor());
					}
					art.put("title", list.get(i).getTbTitle());
					art.put("readNumber", list.get(i).getReadNumber());
					art.put("subjectGid", list.get(i).getSubjectGid());
					if(list.get(i).getSubjectGid()==null||"0".equals(list.get(i).getSubjectGid()))art.put("subjectGid","");
					art.put("selected", "1");
					art.put("url", list.get(i).getTbDetail());
					if (userId!=null)
						art.put("isFav", favouriteService.isFav(list.get(i).getGid(),userId));
					else
						art.put("isFav", "2");
					data.add(art);
				}
			}
			List<Textbook> list=textBookService.query(0,currId,count,keyword,category,isFav,userId);
			String ss = new String("骨今中外");
			for (int i=0; i<list.size(); i++){
				JSONObject art = new JSONObject();
				art.put("tbId",list.get(i).getTbId());
				art.put("textbookGid", list.get(i).getGid());
				art.put("pic", list.get(i).getTbAvatars());
				art.put("publish", sdf.format(list.get(i).getTbCreatetime()));
				if(list.get(i).getTbAuthor()==null||"".equals((list.get(i).getTbAuthor().trim()))||"".equals((list.get(i).getTbAuthor()))){
					art.put("author", ss);
				}else{
					art.put("author", list.get(i).getTbAuthor());
				}
				art.put("title", list.get(i).getTbTitle());
				art.put("readNumber", list.get(i).getReadNumber());
				art.put("subjectGid", list.get(i).getSubjectGid());
				if(list.get(i).getSubjectGid()==null||"0".equals(list.get(i).getSubjectGid()))art.put("subjectGid","");
				art.put("selected", "0");
				art.put("url", list.get(i).getTbDetail());
				if (userId!=null)
					art.put("isFav", favouriteService.isFav(list.get(i).getGid(),userId));
				else
					art.put("isFav", "2");
				data.add(art);
			}
			ret.put("result_data", data);
			ret.put("code", "1");
			ret.put("message", "查询成功");
		} catch(CustomException e){
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
	
	public List<Object[]> classificationSelection(String count,String keyword,String orderString,String category  ) {
		List<Object[]>  list=textBookService.getClassificationSelectionAll(Integer.parseInt(count),keyword = keyword.trim(),orderString,category);
		return list;
	}
	
	
	public List<Object[]> classificationSingleSelection(String type,String count,String keyword,String orderString,String category  ) {
		List<Object[]>  list=textBookService.classificationSingleSelection(type,Integer.parseInt(count),keyword = keyword.trim(),orderString,category);
		return list;
	}
	public void queryContentList() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getHeader("userId");
		JSONObject ret = new JSONObject();
		JSONArray result = new JSONArray();
		List<Object[]> classificationSelection=null;
		try {
			if ("0".equals(type)) {
				 classificationSelection = classificationSelection(count, keyword, orderString, category);
			}else{
				classificationSelection = classificationSingleSelection(type,count, keyword, orderString, category);
			}
		for (Object[] objects : classificationSelection) {
			decoratedJSON decoratedJSON = new decoratedJSON(new JSONObject());
			String string = objects[2].toString();
			 if ("1".equals(string)) {
				 Course course = courseService.get((String)objects[0]);
				 decoratedJSON.put("type", "1");
				 decoratedJSON.put("gid", course.getGid());
				 decoratedJSON.put("avatar", course.getAvatar());
				 decoratedJSON.put("title", course.getTitle());
				 decoratedJSON.put("total", course.getTotalNumber());
				 decoratedJSON.put("current", course.getCurrentNumber());
				 decoratedJSON.put("watchNum", course.getWatchNumber());
				 decoratedJSON.put("category", course.getCategory());
				 decoratedJSON.put("price", course.getPrice());
				 decoratedJSON.put("preprice", course.getPreprice());
				 decoratedJSON.put("createTime", course.getCreateTime());
			} else if("2".equals(string)) {
				 CourseVideo courseVideo = courseVideoService.get((String)objects[0]);
				 decoratedJSON.put("type", "2");
				 decoratedJSON.put("videoUrl", courseVideo.getVideoUrl());
				 decoratedJSON.put("avatar", courseVideo.getAvatar());
				 decoratedJSON.put("videoTraffic", courseVideo.getSize());
				 decoratedJSON.put("timerLength", courseVideo.getDuration());
				 decoratedJSON.put("title", courseVideo.getTitle());
				 decoratedJSON.put("shareAddress",getProperByName("videoUrl")+gid);
				 decoratedJSON.put("duration",courseVideo.getDuration());
				 decoratedJSON.put("category",courseVideo.getCategory());
				 decoratedJSON.put("createTime",courseVideo.getCreateTime());
				 decoratedJSON.put("downloadUrl",courseVideo.getDownloadUrl());

			}else if("3".equals(string)) {
				Textbook textbook = textBookService.get((String)objects[0]);
				decoratedJSON.put("type", "3");
				decoratedJSON.put("tbId",textbook.getTbId());
				decoratedJSON.put("textbookGid", textbook.getGid());
				decoratedJSON.put("pic", textbook.getTbAvatars());
				decoratedJSON.put("publish", ComUtil.dateTime2Str(textbook.getTbCreatetime()));
				decoratedJSON.put("author", textbook.getTbAuthor());
				decoratedJSON.put("title",textbook.getTbTitle());
				decoratedJSON.put("readNumber",textbook.getReadNumber());
				decoratedJSON.put("subjectGid", textbook.getSubjectGid());
				decoratedJSON.put("category", textbook.getCategory());
				decoratedJSON.put("url",textbook.getTbDetail());
			}else if("4".equals(string)) {
				Subject pSubject = subjectService.get((String)objects[0]);
				decoratedJSON.put("type", "4");
				decoratedJSON.put("gid", pSubject.getGid());
				decoratedJSON.put("name", pSubject.getTitle());
				decoratedJSON.put("description",pSubject.getDescription());
				decoratedJSON.put("pic", pSubject.getPic());
				decoratedJSON.put("category", pSubject.getCategory());
			}
			 result.add(decoratedJSON.getReJsonOBJ());
			 
		}
	
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", result);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}
	
	
	public void queryCategory() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getHeader("userId");
		JSONObject ret = new JSONObject();
		JSONArray result = new JSONArray();
	
		try {
			List<Object[]> Categorys=textBookService.queryCategory();
			decoratedJSON csJSON = new decoratedJSON(new JSONObject());
			csJSON.put("name", "创伤");
			csJSON.put("type", "0");
			JSONArray cs = new JSONArray();
			
			
			
			decoratedJSON gjJSON = new decoratedJSON(new JSONObject());
			gjJSON.put("name", "关节");
			gjJSON.put("type", "1");
			JSONArray gj = new JSONArray();
			
			
			decoratedJSON jzJSON = new decoratedJSON(new JSONObject());
			jzJSON.put("name", "脊柱");
			jzJSON.put("type", "2");
			JSONArray jz = new JSONArray();
			
			
			
			decoratedJSON yyJSON = new decoratedJSON(new JSONObject());
			yyJSON.put("name", "运医");
			yyJSON.put("type", "3");
			JSONArray yy = new JSONArray();
			
			
			
            for (Object[] objects : Categorys) {
            	String gidString=objects[1].toString();
            	String type=objects[2].toString();
            	String minitype=objects[3].toString();
            	if ("创伤".equals(type)) {
            		decoratedJSON cJSON = new decoratedJSON(new JSONObject());
            		cJSON.put("gid", gidString);
            		cJSON.put("type", type);
            		cJSON.put("minitype", minitype);
            		cs.add(cJSON.getReJsonOBJ());
				} else if("关节".equals(type)) {
					decoratedJSON gJSON = new decoratedJSON(new JSONObject());
					gJSON.put("gid", gidString);
					gJSON.put("type", type);
					gJSON.put("minitype", minitype);
					gj.add(gJSON.getReJsonOBJ());

				} else if("脊柱".equals(type)) {
					decoratedJSON jJSON = new decoratedJSON(new JSONObject());
					jJSON.put("gid", gidString);
					jJSON.put("type", type);
					jJSON.put("minitype", minitype);
					jz.add(jJSON.getReJsonOBJ());
				} else {
					decoratedJSON yJSON = new decoratedJSON(new JSONObject());
					yJSON.put("gid", gidString);
					yJSON.put("type", type);
					yJSON.put("minitype", minitype);
					yy.add(yJSON.getReJsonOBJ());
				}
            	csJSON.put("cs", cs);
            	gjJSON.put("cs", gj);
            	jzJSON.put("cs", jz);
            	yyJSON.put("cs", yy);
			}
            result.add(csJSON.getReJsonOBJ());
            result.add(gjJSON.getReJsonOBJ());
            result.add(jzJSON.getReJsonOBJ());
            result.add(yyJSON.getReJsonOBJ());
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", result);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 骨科指南文章详细查询
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void tbDetail(){
		JSONObject ret=new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			
			
			Textbook textbook = textBookService.get(gid);
			Random random=new Random();
			textbook.setReadNumber(textbook.getReadNumber()+random.nextInt(5)+1);
			textbook.setReadNumberR(textbook.getReadNumberR()+1);
			textBookService.update(textbook);
			JSONObject data = new JSONObject();
			data.put("gid", textbook.getGid());
			data.put("publish", ComUtil.dateTime2Str( textbook.getTime()));
			data.put("author", textbook.getTbAuthor());
			data.put("pic", textbook.getTbAvatars());
			data.put("title", textbook.getTbTitle());
			data.put("detail",textbook.getTbDetail());
			data.put("count", textbook.getTbCount());
			if (userId!=null)
				data.put("isFav", favouriteService.isFav(textbook.getGid(),userId));
			else
				data.put("isFav", "2");
			
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 骨科指南收藏
	 * @param userId
	 * @param token
	 * @param gid
	 * @param type
	 */
	public void tbFav(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if(type!=null&&type.equals("1")){
				Date now = new Date();
				Favourite fav = new Favourite();
				fav.setGid(gid);
				fav.setCreateTime(now);
				fav.setUpdateTime(now);
				fav.setUserGid(userId);
				fav.setType("1");
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getCurrId() {
		return currId;
	}
	public void setCurrId(String currId) {
		this.currId = currId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsFav() {
		return isFav;
	}
	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}
	
}

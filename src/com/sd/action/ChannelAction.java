package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AnnouncementService;
import com.sd.service.ChannelService;
import com.sd.service.ChildPostService;
import com.sd.service.ChinaAreaService;
import com.sd.service.FavouriteService;
import com.sd.service.NoticeDetailService;
import com.sd.service.NoticeService;
import com.sd.service.PostFavService;
import com.sd.service.PostsService;
import com.sd.service.ReplyService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.util.decoratedJSON;
import com.sd.util.scoreUtil;
import com.sd.vo.Announcement;
import com.sd.vo.Channel;
import com.sd.vo.ChildPost;
import com.sd.vo.ChinaArea;
import com.sd.vo.Favourite;
import com.sd.vo.Notice;
import com.sd.vo.NoticeDetail;
import com.sd.vo.PostAnswer;
import com.sd.vo.PostFav;
import com.sd.vo.PostSubject;
import com.sd.vo.Posts;
import com.sd.vo.Reply;
import com.sd.vo.User;

/**
 * 频道接口
 * 
 * @author bxf
 * 
 */

public class ChannelAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ChinaAreaService chinaAreaService;
	@Resource
	private AnnouncementService announcementService;
	@Resource
	private UserTokenService userTokenService;
	@Resource
	private UserService userService;
	@Resource
	private PostsService postsService;
	@Resource
	private ChannelService channelService;
	@Resource
	private ReplyService replyService;
	@Resource
	private PostFavService postFavService;
	@Resource
	private FavouriteService favouriteService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private NoticeDetailService noticeDetailService;
	@Resource
	private ChildPostService childPostService;

	
	/**
	 * 返回行政区划
	 * 
	 * @param
	 * @return regionDivisions
	 */
	public void regionDivisions() {
		JSONObject ret = new JSONObject();
		JSONArray AllProvinceJsonArr = new JSONArray();
		try {

			List<ChinaArea> chinaAreaList = chinaAreaService.getAllList();
			int i = 0;
			// 上一條記錄的省份
			String provincej = "0";
			// 初始存省份
			JSONObject provinceJsonObj = new JSONObject();
			// 初始存地区的jsonArray
			JSONArray areaJsonArr = new JSONArray();
			while (i < 422) {
				// 當前查詢的記錄
				ChinaArea chinaAreai = chinaAreaList.get(i++);
				String provincei = chinaAreai.getProvince();
				String areai = chinaAreai.getArea();
				// 当前记录和上一条记录不同
				if (provincei.equals(provincej) || "0".equals(provincej)) {

					areaJsonArr.add(areai);

				} else {
					// 說明省份出現不同，應該保存上一個省份并創建新的存放對象
					provinceJsonObj.put("provinceName", provincej);
					provinceJsonObj.put("cities", areaJsonArr);
					AllProvinceJsonArr.add(provinceJsonObj);
					// 为下一个省份创建存储 对象
					provinceJsonObj = new JSONObject();
					areaJsonArr = new JSONArray();
					// 下一个省份的第一个已经查询出来所以要保存起来
					areaJsonArr.add(areai);

				}
				// 記錄上一條記錄的省份名稱
				provincej = provincei;
			}
			// 将最后的一个省份添加进来
			provinceJsonObj.put("provinceName", provincej);
			provinceJsonObj.put("cities", areaJsonArr);
			AllProvinceJsonArr.add(provinceJsonObj);
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", AllProvinceJsonArr);

		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 个人的帖子
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 */
	public void postSelectByUser() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			// String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			
		
			
			List<Posts> postsList = postsService.getPostsListByPage(userId,
					pageNum, pageSize);
			JSONArray data = new JSONArray();
			for (Posts post : postsList) {
				JSONObject met1 = new JSONObject();
				decoratedJSON met = new decoratedJSON(met1);
				met.put("gid", post.getGid());
				User user = post.getUser();
				if (user != null) {
					met.put("userId", user.getGid());
					met.put("userName", user.getName());
					met.put("userAvatars", user.getAvatars());
					met.put("isApprove", user.getApproveStatus());
					if (!StringUtils.isEmpty(user.getHospital())) {
						met.put("hospital", user.getHospital());
					}

				} else {
					met.put("userId", "");
					met.put("userName", "");
					met.put("userAvatars", "");
					met.put("isApprove", "");
				}
				if(post.getDetail()==null){
					met.put("detail", post.getDetail());
				}else{
					
					met.put("detail", emojiRecovery(post.getDetail()));
				}
				met.put("readNumber", post.getReadNumber());
				met.put("title", post.getTitle());
				met.put("mark", post.getMark());
				met.put("createTime",
						ComUtil.dateTime2Str(post.getCreateTime()));
				met.put("type", post.getType());
				met.put("top", post.getTop());
				met.put("location", post.getLocation());
				met.put("lstPic", post.getPic());
				met.put("lstVid", post.getVid());
				met.put("favCount", post.getFavCount());
				met.put("lstReplyCount", post.getReplyCount());
				data.add(met.getReJsonOBJ());
			}
			JSONObject res = new JSONObject();
			
			String s = "select count(*)*20+sum(pst_favcount*2+pst_replycount*3) +100 from tpost where pst_userGid='"+userId+"'";
			int countNumBySql = scoreService.totleNumBySql(s);
			String s1 ="select count(*) from tpostfav where  pf_userGid='"+userId+"'";
			String s2 ="select count(*) from treply where  rpy_userGid='"+userId+"'";
			int countNumBySql2 = scoreService.totleNumBySql(s1);
			int countNumBySql3 = scoreService.totleNumBySql(s2);
			String honour=String.valueOf(countNumBySql);
			String assistance=String.valueOf(countNumBySql2+countNumBySql3*5); 
			res.put("honour", honour);
			res.put("assistance", assistance);
			
			
			
			res.put("post", data);
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

	// 删除字帖
	public void deleteChildPost() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined)
				throw new CustomException(NO_LOGIN);

			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			childPostService.delete(gid);
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

	// 增加子贴
	public void addChildPost() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined)
				throw new CustomException(NO_LOGIN);

			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			ChildPost childPost = new ChildPost();
			childPost.setGid(ComUtil.getuuid());
			
			if(detail==null){
				childPost.setDetail(detail);
			}else{
				
				childPost.setDetail(emojiFilter(detail));
			}
			
			
			
			
			childPost.setTitle(title);
			childPost.setPic(pic);
			childPost.setParentGid(gid);
			childPost.setCreatetime(new Date());
			childPostService.save(childPost);
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

	/**
	 * 获取点赞人列表
	 * 
	 * @param gid
	 *            帖子GID
	 */
	public void searchPraiseUser() {
		JSONObject ret = new JSONObject();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("postGid", gid);
			List<PostFav> list = postFavService.getConditonList(map, "", false,
					null);
			JSONArray ja = new JSONArray();
			for (PostFav postFav : list) {
				JSONObject jo = new JSONObject();
				User user = userService.get(postFav.getUserGid());
				if (user != null) {

				} else {
					continue;
				}
				jo.put("gid", user.getGid());
				jo.put("name", user.getName());
				jo.put("avatar", user.getAvatars());
				ja.add(jo);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", ja);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 频道列表
	 * 
	 * @param pageNum
	 *            页数
	 * @param count
	 *            每页条数
	 */
	public void channelSelect() {
		JSONObject ret = new JSONObject();
		try {
			List<Channel> channelList = channelService.getAllList(pageNum,
					count);
			JSONArray data = new JSONArray();
			for (Channel channel : channelList) {
				JSONObject met = new JSONObject();
				met.put("gid", channel.getGid());
				met.put("name", channel.getName());
				met.put("avatars", channel.getAvatars());
				met.put("slogan", channel.getSlogan());
				met.put("allCount",
						postsService.getCountNum(channel.getGid(), "all"));
				met.put("newCount",
						postsService.getCountNum(channel.getGid(), "new"));
				data.add(met);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 帖子列表+公告
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param chGid
	 *            频道GID
	 * @param dateBegin
	 *            开始日期
	 * @param count
	 *            显示条数
	 * @param isFav
	 *            是否关注0全部1已关注 orderNum "2" 浏览量
	 */
	public void searchPostAndAnnouncement() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
//			if (!logined && "1".equals(isFav))
//				throw new CustomException(NO_LOGIN);
			List<Posts> postsList = postsService.getPostsList(chGid,mark,dateBegin,count, isFav,userId, orderNum);
			JSONArray data = new JSONArray();
			for (Posts post : postsList) {
				JSONObject met1 = new JSONObject();
				decoratedJSON met = new decoratedJSON(met1);
				met.put("gid", post.getGid());
				User user = post.getUser();
				if (user != null) {
					met.put("userId", user.getGid());
					met.put("userName", user.getName());
					met.put("userAvatars", user.getAvatars());
					met.put("isApprove", user.getApproveStatus());
				} else {
					met.put("userId", "");
					met.put("userName", "");
					met.put("userAvatars", "");
					met.put("isApprove", "");
				}
				
				if (post.getDetail()!=null) {
					
					met.put("detail", emojiRecovery(post.getDetail()));
				} else {
					met.put("detail", post.getDetail());
				}
				met.put("readNumber", post.getReadNumber());
				met.put("title", post.getTitle());
				met.put("mark", post.getMark());
				met.put("createTime",
						ComUtil.dateTime2Str(post.getCreateTime()));
				met.put("type", post.getType());
				met.put("top", post.getTop());
				met.put("location", post.getLocation());
				if ("3".equals(post.getType())&&(post.getPic()==null||post.getPic().isEmpty())) {
					Set<ChildPost> childPost = post.getChildPost();
					String lstpic="";
					for (ChildPost childPost2 : childPost) {

						if(childPost2.getPic()!=null&&!(childPost2.getPic().isEmpty())){
							lstpic=childPost2.getPic();
//							met.put("lstPic", childPost2.getPic());
							break;
						}
					}
					
					if("".equals(lstpic)){
						met.put("lstPic", "");
					}else{
						met.put("lstPic", lstpic);
					}
					
					
				}else {
					
					met.put("lstPic", post.getPic());
				}
				met.put("lstVid", post.getVid());
				met.put("favCount", post.getFavCount());
				met.put("lstReplyCount", post.getReplyCount());
				Channel channel = post.getChannel();
				if (channel != null) {
					met.put("chGid", channel.getGid());
					met.put("chName", channel.getName());
					met.put("chAvatars", channel.getAvatars());
				} else {
					met.put("chGid", "");
					met.put("chName", "");
					met.put("chAvatars", "");
				}
				if (logined) {
					User loginUser = userService.get(userId);
					met.put("isPraised",
							postFavService.isPraised(post.getGid(), userId));
					met.put("isAllowed",
							channelService.isAllowed(channel, loginUser));
					met.put("isFav",
							favouriteService.isFav(post.getGid(), userId));
				} else {
					met.put("isPraised", "0");
					met.put("isAllowed", "0");
					met.put("isFav", "0");
				}
				data.add(met.getReJsonOBJ());
			}
			// ---------------------------------华丽丽的分割线-------------------------------
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("category", "频道");
			List<Announcement> list = announcementService.getConditonList(
					condition, null, false, null);
			JSONArray ja = new JSONArray();
			for (Announcement announcement : list) {
				JSONObject jo1 = new JSONObject();
				decoratedJSON jo = new decoratedJSON(jo1);
				jo.put("gid", announcement.getGid());
				jo.put("content", announcement.getContent());
				jo.put("type", announcement.getType());
				jo.put("url", announcement.getUrl());
				jo.put("title", announcement.getTitle());
				ja.add(jo.getReJsonOBJ());
			}
			JSONObject res = new JSONObject();
			res.put("post", data);
			res.put("announcement", ja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", res);
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

	/**
	 * 帖子列表
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param chGid
	 *            频道GID
	 * @param dateBegin
	 *            开始日期
	 * @param count
	 *            显示条数
	 * @param isFav
	 *            是否关注0全部1已关注
	 */
	public void postSelect() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined && "1".equals(isFav))
				throw new CustomException(NO_LOGIN);

			List<Posts> postsList = postsService.getPostsList(chGid, null,
					dateBegin, count, isFav, userId, null);
			JSONArray data = new JSONArray();
			for (Posts post : postsList) {
				JSONObject met = new JSONObject();
				met.put("gid", post.getGid());
				User user = post.getUser();
				if (user != null) {
					met.put("userId", user.getGid());
					met.put("userName", user.getName());
					met.put("userAvatars", user.getAvatars());
					met.put("isApprove", user.getApproveStatus());
				} else {
					met.put("userId", "");
					met.put("userName", "");
					met.put("userAvatars", "");
					met.put("isApprove", "");
				}
				
				
				
				
				if (post.getDetail()!=null) {
					
					met.put("detail", emojiRecovery(post.getDetail()));
				} else {
					met.put("detail", post.getDetail());

				}
				met.put("readNumber", post.getReadNumber());
				met.put("title", post.getTitle());
				if (StringUtils.isNotBlank(post.getMark())) {
					met.put("mark", post.getMark());
				} else {
					met.put("mark", "其它");
				}
				met.put("createTime",
						ComUtil.dateTime2Str(post.getCreateTime()));
				met.put("type", post.getType());
				met.put("top", post.getTop());
				met.put("location", post.getLocation());
				met.put("lstPic", post.getPic());
				met.put("lstVid", post.getVid());
				met.put("favCount", post.getFavCount());
				met.put("lstReplyCount", post.getReplyCount());
				Channel channel = post.getChannel();
				if (channel != null) {
					met.put("chGid", channel.getGid());
					met.put("chName", channel.getName());
					met.put("chAvatars", channel.getAvatars());
				} else {
					met.put("chGid", "");
					met.put("chName", "");
					met.put("chAvatars", "");
				}
				if (logined) {
					User loginUser = userService.get(userId);
					met.put("isPraised",
							postFavService.isPraised(post.getGid(), userId));
					met.put("isAllowed",
							channelService.isAllowed(channel, loginUser));
					met.put("isFav",
							favouriteService.isFav(post.getGid(), userId));
				} else {
					met.put("isPraised", "0");
					met.put("isAllowed", "0");
					met.put("isFav", "0");
				}
				data.add(met);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	 * 帖子新增
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param chGid
	 *            频道GID
	 * @param detail
	 *            发帖内容
	 * @param lstPic
	 *            图片列表
	 * @param lstVid
	 *            视频列表
	 */
	public void postAdd() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
           

			Date now = new Date();
			Posts posts = new Posts();
			
			 if(chGid!=null){
	            	Channel channel = channelService.get(chGid);
	            	if (channel == null)
	            		throw new CustomException(NO_CHANNEL);
	            	
	            	posts.setChannelGid(chGid);
	            }
			posts.setCreateTime(now);
			posts.setTitle(title);
			
			
			if (detail!=null) {
				posts.setDetail(emojiFilter(detail));
				
			} else {
				posts.setDetail(detail);

			}
			
			
			
			if (location == null) {
				posts.setLocation("");
			} else {

				posts.setLocation(location);
			}
			posts.setReadNumber(1);
			posts.setReadNumberR(1);
			posts.setFavCount("0");
			posts.setGid(ComUtil.getuuid());
			posts.setIsdel("1");
			posts.setPic(lstPic);
			posts.setReplyCount(0);
			posts.setUpdateTime(now);
			posts.setUserGid(userId);
			posts.setVid(lstVid);
			if (StringUtils.isBlank(type)) {
				posts.setType("1");
			} else {
				posts.setType(type);
			}
			posts.setMark(mark);
			posts.setOrder("0");
			posts.setTop("0");
			postsService.save(posts);

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

	/**
	 * 帖子新增
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param chGid
	 *            频道GID
	 * @param detail
	 *            发帖内容
	 * @param lstPic
	 *            图片列表
	 * @param lstVid
	 *            视频列表
	 */
	@SuppressWarnings("rawtypes")
	public void addMedicalRecord() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			String token = request.getHeader("token");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			// resultList帖子json数据
			if (StringUtils.isNotBlank(resultList.trim())) {
				// 加分
				scoreUtil.addMedicalRecordChangeScor(scoreService,
						postsService, userService, userId, 5);
				@SuppressWarnings("static-access")
				JSONArray resultJsonArray = new JSONArray()
						.fromObject(resultList);
				int size = resultJsonArray.size();
				if (size != 4) {
					throw new CustomException("上传的病例帖子个数不够");
				}
				String PostGid = ComUtil.getuuid();
				JSONObject firstJsonObject = resultJsonArray.getJSONObject(0);
				String title = (String) firstJsonObject.get("title");
				String detail = (String) firstJsonObject.get("detail");
				String location = (String) firstJsonObject.get("location");
				String lstPic = (String) firstJsonObject.get("lstPic");
				Date now = new Date();
				Posts posts = new Posts();
				posts.setChannelGid("");
				posts.setCreateTime(now);
				posts.setTitle(title);
			    if (detail!=null) {
			    	posts.setDetail( emojiFilter(detail));
			    	
					
				} else {
					posts.setDetail( detail);

				}
				if (location == null || "".equals(location)) {
					posts.setLocation("");
				} else {

					posts.setLocation(location);
				}
				posts.setReadNumber(1);
				posts.setReadNumberR(1);
				posts.setFavCount("0");

				posts.setGid(PostGid);
				posts.setIsdel("1");
				posts.setPic(lstPic);
				posts.setReplyCount(0);
				posts.setUpdateTime(now);
				posts.setUserGid(userId);
				// lstVid不用传了type不用传了Mark不用传了chGid不用了
				// posts.setVid(lstVid);
				// if (StringUtils.isBlank(type)) {
				// posts.setType("1");
				// } else {
				// posts.setType(type);
				// }
				posts.setVid("");
				posts.setType("3");
				posts.setMark("病例");
				posts.setOrder("0");
				posts.setTop("0");
				postsService.save(posts);

				for (int i = 1; i <= 3; i++) {
					JSONObject jsonObject = resultJsonArray.getJSONObject(i);
					if (!(jsonObject.isEmpty())) {
						// detail、pic、gender、age、type
						String childDetail = (String) jsonObject.get("detail");
						String childPic = (String) jsonObject.get("pic");
						String gender = (String) jsonObject.get("gender");
						String age = (String) jsonObject.get("age");
						String type = (String) jsonObject.get("type");
						ChildPost childPost = new ChildPost();
						childPost.setGid(ComUtil.getuuid());
						if(childDetail==null){
							childPost.setDetail(childDetail);

						}else{
							
							childPost.setDetail(emojiFilter(childDetail));
						}
						// title为空新增加了gender、age、type
						childPost.setTitle("");
						childPost.setPic(childPic);
						childPost.setCreatetime(new Date());
						childPost.setGender(gender);
						childPost.setAge(age);
						childPost.setType(type);
						childPost.setParentGid(PostGid);
						childPostService.save(childPost);
					}
				}

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

	/**
	 * 帖子删除
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param gid
	 *            帖子GID
	 */
	public void postDelete() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Posts posts = postsService.get(gid);
			if (posts == null)
				throw new CustomException(NO_POST);

			User user = posts.getUser();
			User loginUser = userService.get(userId);
			Channel channel = posts.getChannel();
			String isAllowed = channelService.isAllowed(channel, loginUser);
			if ((user == null || userId == null) && "0".equals(isAllowed))
				throw new CustomException("无此操作权限");

			posts.setIsdel("0");
			postsService.update(posts);

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

	/**
	 * 帖子加精
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param gid
	 *            帖子GID
	 */
	public void postRecommend() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Posts posts = postsService.get(gid);
			if (posts == null)
				throw new CustomException(NO_POST);

			User user = userService.get(userId);
			Channel channel = posts.getChannel();
			String isAllowed = channelService.isAllowed(channel, user);
			if ("0".equals(isAllowed))
				throw new CustomException("无此操作权限");

			posts.setIsRec("1");
			postsService.update(posts);

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

	/**
	 * 频道查询
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param gid
	 *            频道GID
	 */
	public void channelSelectByGid() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Channel channel = channelService.get(gid);
			if (channel == null)
				throw new CustomException(NO_CHANNEL);

			JSONObject data = new JSONObject();
			data.put("gid", channel.getGid());
			data.put("name", channel.getName());
			data.put("slogan", channel.getSlogan());
			data.put("avatars", channel.getAvatars());
			List<User> userList = channelService.getUserList(channel.getGid());
			JSONArray lstusers = new JSONArray();
			for (User user : userList) {
				JSONObject lst = new JSONObject();
				lst.put("userId", user.getGid());
				lst.put("userName", user.getName());
				lst.put("userAvatars", user.getAvatars());
				lstusers.add(lst);
			}

			data.put("lstuser", lstusers);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	 * 频道更新
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param gid
	 *            频道GID
	 * @param slogan
	 *            频道slogan
	 */
	public void channelUpdate() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			Channel channel = channelService.get(gid);
			if (channel == null)
				throw new CustomException(NO_CHANNEL);
			channel.setSlogan(slogan);
			channelService.update(channel);
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

	// 点赞评论接口
	// gid评论的GID
	public void praiseReply() {
		JSONObject ret = new JSONObject();
		try {
			boolean logined = userTokenService.checkToken(ServletActionContext
					.getRequest());
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("userGid", userId);
			condition.put("postGid", gid);
			List<PostFav> list = postFavService.getConditonList(condition, "",
					false, null);
			if (list.size() != 0)
				throw new CustomException("您已点过赞");
			PostFav pfav = new PostFav();
			pfav.setPostGid(gid);
			pfav.setUserGid(userId);
			pfav.setCreateTime(new Date());
			pfav.setUpdateTime(new Date());
			pfav.setIsdel("1");
			scoreUtil.postPraiseChangeScor(scoreService, postsService,
					userService, userId, 1);
			postFavService.save(pfav);
			Reply reply = replyService.get(gid);
			reply.setPraiseNumber(reply.getPraiseNumber() + 1);
			replyService.update(reply);
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

	/**
	 * 帖子详情
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param gid
	 *            帖子GID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void postDetailSelect() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Posts post = postsService.get(gid);
			if (post == null)
				throw new CustomException(NO_POST);

			post.setReadNumberR(post.getReadNumberR() + 1);
			Random random = new java.util.Random();
			post.setReadNumber(post.getReadNumber() + random.nextInt(5) + 1);
			postsService.update(post);
			JSONObject data = new JSONObject();
			data.put("gid", post.getGid());
			User user = post.getUser();
			if (user != null) {
				String userGid = user.getGid();
				data.put("gdId", user.getId());
				data.put("userId", user.getGid());
				// UserToken freshToken = userTokenService.freshToken(userGid);
				// data.put("token", freshToken.getContent());
				data.put("userName", user.getName());
				data.put("userAvatars", user.getAvatars());
				data.put("approveStatus", user.getApproveStatus());
				data.put("isFav", favouriteService.isFav(post.getGid(), userId));
			} else {
				data.put("gdId", "");
				data.put("userId", "");
				data.put("userName", "");
				data.put("userAvatars", "");
				data.put("approveStatus", "");
				if ("4".equals(post.getType())){
					data.put("isFav", favouriteService.isFav(post.getGid(), userId));
				}else{
					
					data.put("isFav", "");
				}
			}
			if (post.getDetail()!=null) {
				
				data.put("detail", emojiRecovery(post.getDetail()));
			} else {
				data.put("detail", post.getDetail());

			}
			data.put("createTime", ComUtil.dateTime2Str(post.getCreateTime()));
			data.put("lstPic", post.getPic());
			data.put("title", post.getTitle());
			data.put("lstVid", post.getVid());
			data.put("readNumber", post.getReadNumber());
			data.put("type", post.getType());
			data.put("favCount", post.getFavCount());
			data.put("location", post.getLocation());
			data.put("replyCount", post.getReplyCount());
		 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONArray ja = new JSONArray();
			if ("3".equals(post.getType())) {
				for (ChildPost childPost : post.getChildPost()) {
					JSONObject jo = new JSONObject();
					// jo.put("title", childPost.getTitle());
					if (childPost.getDetail()!=null) {
						
						jo.put("detail", emojiRecovery(childPost.getDetail()));
					} else {
						jo.put("detail", childPost.getDetail());
					}
					jo.put("pic", childPost.getPic());
					// 病例的帖子最外层添加age和gender 子帖子添加type类型少了title
					data.put("age", childPost.getAge());
					data.put("gender", childPost.getGender());
					jo.put("type", childPost.getType());
					jo.put("gid", childPost.getGid());
					jo.put("time", sdf.format(childPost.getCreatetime()));
					ja.add(jo);
				}

			} else if ("4".equals(post.getType())) {
				JSONObject jo = new JSONObject();
				jo.put("htmlText", post.getHtmltext());
				String hql = "from PostSubject  where postGid=?";
				List arrayList = new ArrayList();
				arrayList.add(gid);
				List<PostSubject> postSubjectList = postSubjectService
						.HQLSqlBindParameter(hql, arrayList);
				if (postSubjectList.size() != 0) {
					PostSubject postSubject = postSubjectList.get(0);
					String status = postAnswerService.getStatus(userId,
							postSubject.getGid());
					int parseInt = Integer.parseInt(status);
					if (parseInt != 0) {
						jo.put("status", "0");
					} else {
						jo.put("subjectGid", postSubject.getGid());
						jo.put("title", postSubject.getQuestion());
						jo.put("status", "1");
						ArrayList<String> optList = new ArrayList<String>();
						optList.add(postSubject.getOption1());
						optList.add(postSubject.getOption2());
						optList.add(postSubject.getOption3());
						optList.add(postSubject.getOption4());
						JSONArray optionArray = new JSONArray();
						for (int i = 0; i < optList.size(); i++) {
							if (!optList.get(i).isEmpty()) {
								JSONObject option = new JSONObject();
								option.put("option", optList.get(i));
								optionArray.add(option);
							}
						}
						jo.put("optionList", optionArray);
					}

				}
				data.put("question", jo);
			} else {
				for (ChildPost childPost : post.getChildPost()) {
					JSONObject jo = new JSONObject();
					jo.put("title", childPost.getTitle());
					
					
					if (childPost.getDetail()!=null) {
						
						jo.put("detail", emojiRecovery(childPost.getDetail()));
					} else {
						jo.put("detail", childPost.getDetail());
					}
					
					
					jo.put("pic", childPost.getPic());
					jo.put("gid", childPost.getGid());
					jo.put("time", sdf.format(childPost.getCreatetime()));
					ja.add(jo);
				}

			}
			data.put("childPost", ja);
			Map<String, String> map = new HashMap<String, String>();
			map.put("postGid", gid);
			List<PostFav> list = postFavService.getConditonList(map, "", false,
					null);
			String head = "";
			for (PostFav postFav : list) {
				User u = userService.get(postFav.getUserGid());
				if (u != null)
					head += u.getAvatars() + ",";
			}
			if (head != "")
				data.put("avatars", head.substring(0, head.length() - 1));
			if (logined)
				data.put("isPraised",
						postFavService.isPraised(post.getGid(), userId));
			else
				data.put("isPraised", "");
			Channel channel = post.getChannel();
			if (channel != null) {
				data.put("chGid", channel.getGid());
				data.put("chName", channel.getName());
			} else {
				data.put("chGid", "");
				data.put("chName", "");
			}
			List<Reply> replyList = replyService.getReplyClient(gid);
			JSONArray replys = new JSONArray();
			for (Reply rep : replyList) {
				JSONObject reply = new JSONObject();
				reply.put("rpyGid", rep.getGid());
				User rpyUser = rep.getUser();
				if (rpyUser != null) {
					reply.put("rpyGdid", rpyUser.getId());
					reply.put("rpyUserId", rpyUser.getGid());
					reply.put("rpyUserName", rpyUser.getName());
					reply.put("rpyUserAvatars", rpyUser.getAvatars());
					reply.put("approveStatus", rpyUser.getApproveStatus());
				} else {
					reply.put("rpyGdid", "");
					reply.put("rpyUserId", "");
					reply.put("rpyUserName", "");
					reply.put("rpyUserAvatars", "");
					reply.put("approveStatus", "");
				}
				User rpyToUser = rep.getToUser();
				if (rpyToUser != null) {
					reply.put("rpyToUserId", rpyToUser.getGid());
					reply.put("rpyToUserName", rpyToUser.getName());
					if (StringUtils.isNotBlank(rep.getReplyGid())) {
						Reply beReply = replyService.get(rep.getReplyGid());
						// 返回评论的内容
						reply.put("repliedDetail",emojiRecovery(beReply.getDetail()));
					} else {
						reply.put("repliedDetail", "");
					}
				} else {
					reply.put("rpyToUserId", "");
					reply.put("rpyToUserName", "");
					reply.put("repliedDetail", "");
				}
				// 返回评论的内容
				
				if (rep.getDetail()!=null) {
					reply.put("rpyDetail", emojiRecovery(rep.getDetail()));
				} else {
					reply.put("rpyDetail", rep.getDetail());
				}
				
				
				reply.put("pic", rep.getPic());
				reply.put("location", rep.getLocation());
				reply.put("praiseNumber", rep.getPraiseNumber());
				reply.put("rpyCreateTime",
						ComUtil.dateTime2Str(rep.getCreateTime()));
				if (logined)
					reply.put("praiseReply",
							postFavService.isPraised(rep.getGid(), userId));
				else
					reply.put("praiseReply", "");
				replys.add(reply);
			}
			data.put("lstreply", replys);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	 * 帖子点赞
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 * @param type
	 *            1-点赞/2-收藏
	 */
	public void postFav() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Posts post = postsService.get(gid);
			if (post == null)
				throw new CustomException(NO_POST);

			Date now = new Date();
			if ("1".equals(type)) {
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("userGid", userId);
				condition.put("postGid", gid);
				List<PostFav> list = postFavService.getConditonList(condition,
						"", false, null);
				if (list.size() != 0)
					throw new CustomException("您已点过赞");
				PostFav pfav = new PostFav();
				pfav.setPostGid(gid);
				pfav.setUserGid(userId);
				pfav.setCreateTime(now);
				pfav.setUpdateTime(now);
				pfav.setIsdel("1");
				scoreUtil.postPraiseChangeScor(scoreService, postsService,
						userService, userId, 1);
				postFavService.save(pfav);
				postsService.updateFavCount(gid);
			} else if ("2".equals(type)) {
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("userGid", userId);
				condition.put("gid", gid);
				List<Favourite> list = favouriteService.getConditonList(
						condition, "", false, null);
				if (list.size() != 0)
					throw new CustomException("您已收藏过");
				Favourite fav = new Favourite();
				fav.setCreateTime(now);
				fav.setUpdateTime(now);
				fav.setGid(gid);
				fav.setIsdel("1");
				fav.setType("3");// 帖子
				fav.setUserGid(userId);
				scoreUtil.postFavChangeScor(scoreService, postsService,
						userService, userId, 1);
				favouriteService.save(fav)   ;
			} else {
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("gid", gid);
				condition.put("userGid", userId);
				condition.put("type", "3");
				List<Favourite> retList = favouriteService.getConditonList(
						condition, "", false, null);
				if (retList.size() > 0) {
					Favourite fav = retList.get(0);
					favouriteService.delete(fav);
				}
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

	/**
	 * 帖子回复
	 * 
	 * @param userId
	 * @param token
	 * @param toUserId
	 * @param detail
	 * @param gid
	 */
	public void replyAdd() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			Posts post = postsService.get(gid);
			if (post == null)
				throw new CustomException(NO_POST);

			Date now = new Date();
			Reply reply = new Reply();
			reply.setGid(ComUtil.getuuid());
			reply.setCreateTime(now);
			reply.setUpdateTime(now);
			if (detail!=null) {
				reply.setDetail(emojiFilter(detail));
			} else {
				reply.setDetail(detail);
			}
			reply.setPic(pic);
			reply.setLocation(location);
			reply.setPraiseNumber(0);
			reply.setIsdel("1");
			reply.setReplyGid(replyGid);
			reply.setPostGid(gid);
			reply.setToUserId(toUserId);
			reply.setUserId(userId);
			replyService.save(reply);
			postsService.updateReplyCount(gid);
			scoreUtil.addReplyPostChangeScor(scoreService, postsService,
					userService, userId, 1);
			// 添加提醒通知
			String nUser = post.getUserGid();
			// Notice notice = noticeService.getByUserPost(nUser, gid);
			// if (notice == null){// 未参与，无提醒
			Notice notice = new Notice();
			notice.setCreateTime(now);
			notice.setUpdateTime(now);
			notice.setIsdel("0");
			notice.setIsRead("0");
			notice.setUserId(post.getUserGid());
			notice.setReplyId(reply.getGid());
			String nId = noticeService.save(notice);

			// NoticeDetail nDetail = new NoticeDetail();
			// nDetail.setParticipate(userId);
			// nDetail.setPid(nId);
			// noticeDetailService.save(nDetail);
			// } else {// 有提醒
			// Map<String, String> condition = new HashMap<String, String>();
			// condition.put("pid", notice.getId());
			// condition.put("participate", userId);
			// String num = noticeDetailService.getConditonCount(condition);
			//
			// if ("0".equals(num)){// 未参与
			// NoticeDetail nDetail = new NoticeDetail();
			// nDetail.setParticipate(userId);
			// nDetail.setPid(notice.getId());
			// noticeDetailService.save(nDetail);
			// } else {
			// // 已参与，不重复提醒
			// }
			// }
			if (StringUtils.isNotBlank(toUserId)) {
				// 回复用户
				Notice noticeu = new Notice();
				noticeu.setCreateTime(now);
				noticeu.setUpdateTime(now);
				noticeu.setIsdel("0");
				noticeu.setIsRead("0");
				noticeu.setUserId(toUserId);
				noticeu.setReplyId(reply.getGid());
				noticeService.save(noticeu);
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

	/**
	 * 删除回复
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 * @param postId
	 */
	public void replyDelete() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);

			if (StringUtils.isBlank(postId))
				throw new CustomException(NO_ID);

			Reply reply = replyService.get(gid);
			if (reply == null)
				throw new CustomException(NO_REPLY);

			User user = reply.getUser();
			if (user == null)
				throw new CustomException(ONLY_SELF);
			User loginUser = userService.get(userId);
			if ("1".equals(loginUser.getChAu())) {

			} else {
				if (userId == null || !userId.equals(user.getGid()))
					throw new CustomException(ONLY_SELF);
			}
			reply.setIsdel("0");
			reply.setUpdateTime(new Date());
			replyService.update(reply);
			postsService.updateReplyCount(postId);
			// 删除未读提醒
			if (reply.getToUser() != null) {
				// 未读回复的回复
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("userId", reply.getToUser().getGid());
				condition.put("replyId", reply.getGid());
				condition.put("isRead", "0");

				List<Notice> noticeList = noticeService.getConditonList(
						condition, "", false, null);
				for (Notice notice : noticeList)
					noticeService.delete(notice);
			} else {
				// 未读帖子的回复
				User postUser = reply.getUser();
				if (postUser != null) {
					Map<String, String> condition = new HashMap<String, String>();
					condition.put("userId", postUser.getGid());
					condition.put("replyId", reply.getPostGid());
					condition.put("isRead", "0");

					List<Notice> noticeList = noticeService.getConditonList(
							condition, "", false, null);
					for (Notice notice : noticeList)
						noticeService.refreshOneNotice(notice, userId);
				}
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

	/**
	 * 查询消息列表
	 * 
	 * @param userId
	 * @param token
	 * @param dateBegin
	 * @param count
	 * @param type
	 *            是否已读0未读
	 */
	public void noticeList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			List<Notice> noticeList = noticeService.getNoticeList(userId,
					dateBegin, count, type);

			JSONArray data = new JSONArray();
			for (Notice notice : noticeList) {
				JSONObject met = new JSONObject();
				String replyid = notice.getReplyId();
				if (StringUtils.isNotBlank(notice.getPostId())) {
					// 回复帖子
					Reply reply = replyService.get(replyid);
					User user = userService.get(reply.getUserId());
					if (reply.getDetail()!=null) {
						
						met.put("reply", emojiRecovery(reply.getDetail()));
					} else {
						met.put("reply", reply.getDetail());

					}
					
					met.put("uId", user.getGid());
					met.put("uName", user.getName());
					met.put("uAvatars", user.getAvatars());
					Set<NoticeDetail> noticeDetSet = notice.getDetailSet();
					if (noticeDetSet != null) {
						met.put("type", "1");
					}
					met.put("num", noticeDetSet.size());
					met.put("time",
							ComUtil.dateTime2Str(notice.getCreateTime()));
					Posts post = postsService.get(notice.getPostId());
					if (post != null) {
						
						
						if (post.getDetail()!=null) {
							met.put("pdetail", emojiRecovery(post.getDetail()));
						} else {
							met.put("pdetail", post.getDetail());
						}
						
						
						
						
						met.put("pdetail", emojiRecovery(post.getDetail()));
						met.put("ppic", post.getPic());
						met.put("pgid", post.getGid());
						met.put("ptype", post.getType());
						Channel chn = post.getChannel();
						if (chn != null) {
							met.put("cId", chn.getGid());
							met.put("cName", chn.getName());
						} else {
							met.put("cId", "");
							met.put("cName", "");
						}
					} else {
						met.put("pdetail", "");
						met.put("ppic", "");
						met.put("pgid", "");
						met.put("cId", "");
						met.put("cName", "");
					}
					data.add(met);
				} else {
					// 回复回复
					Reply reply = replyService.get(notice.getReplyId());
					if (reply != null) {
						met.put("type", "0");
						met.put("time",
								ComUtil.dateTime2Str(notice.getCreateTime()));
						met.put("reply", emojiRecovery(reply.getDetail()));
						User user = reply.getUser();
						if (user != null) {
							met.put("uId", user.getGid());
							met.put("uName", user.getName());
							met.put("uAvatars", user.getAvatars());
						} else {
							met.put("uId", "");
							met.put("uName", "");
							met.put("uAvatars", "");
						}
						Posts post = postsService.get(reply.getPostGid());
						if (post != null) {
							
							if (post.getDetail()!=null) {
								
								met.put("pdetail", emojiRecovery(post.getDetail()));
							} else {
								met.put("pdetail", post.getDetail());

							}
							
							
							met.put("ppic", post.getPic());
							met.put("pgid", post.getGid());
							met.put("ptype", post.getType());
							Channel chn = post.getChannel();
							if (chn != null) {
								met.put("cId", chn.getGid());
								met.put("cName", chn.getName());
							} else {
								met.put("cId", "");
								met.put("cName", "");
							}
						} else {
							met.put("pdetail", "");
							met.put("ppic", "");
							met.put("pgid", "");
							met.put("cId", "");
							met.put("cName", "");
						}
						data.add(met);
					}
				}
			}

			for (Notice notice : noticeList) {
				notice.setIsRead("1");
				notice.setUpdateTime(new Date());
				noticeService.update(notice);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	 * 查询消息条数
	 * 
	 * @param userId
	 * @param token
	 */
	public void noticeCount() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			Map<String, String> conditions = new HashMap<String, String>();
			conditions.put("userId", userId);
			conditions.put("isRead", "0");

			String count = noticeService.getConditonCount(conditions);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", count);
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

	@SuppressWarnings("static-access")
	public void getPostAnswer() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			// if (!logined)
			// throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(resultList.trim())) {
				throw new CustomException("参数不正确");
			}
			// System.out.println(resultList+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			JSONObject answer = new JSONObject().fromObject(resultList);
			PostAnswer postAnswer = new PostAnswer();
			postAnswer.setGid(ComUtil.getuuid());
			postAnswer.setOptions(answer.getString("option"));
			postAnswer.setUserId(userId);
			postAnswer.setPostsubjectGid(answer.getString("subjectGid"));
			postAnswerService.save(postAnswer);
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

	// private JSONObject getCountRe(String gid) {
	// JSONObject jsonObject = new JSONObject();
	// HashMap<String, String> condition1 = new HashMap<String, String>();
	// condition1.put("postsubjectGid", gid);
	// condition1.put("option", "1");
	// String option1 = postAnswerService.getConditonCount(condition1);
	// HashMap<String, String> condition2 = new HashMap<String, String>();
	// condition2.put("postsubjectGid", gid);
	// condition2.put("option", "1");
	// String option2 = postAnswerService.getConditonCount(condition2);
	// HashMap<String, String> condition3 = new HashMap<String, String>();
	// condition3.put("postsubjectGid", gid);
	// condition3.put("option", "1");
	// String option3 = postAnswerService.getConditonCount(condition3);
	// HashMap<String, String> condition4 = new HashMap<String, String>();
	// condition4.put("postsubjectGid", gid);
	// condition4.put("option", "1");
	// String option4 = postAnswerService.getConditonCount(condition4);
	// PostSubject postSubject = postSubjectService.get(gid);
	// jsonObject.put("title",postSubject.getQuestion());
	// jsonObject.put("content1", postSubject.getOption1());
	// jsonObject.put("count1",option1 );
	// jsonObject.put("content2", postSubject.getOption2());
	// jsonObject.put("count2",option2);
	// jsonObject.put("content3", postSubject.getOption3());
	// jsonObject.put("count3",option3);
	// jsonObject.put("content4", postSubject.getOption4());
	// jsonObject.put("count4",option4);
	// return jsonObject;
	//
	// }
	//

	public void getCountRe() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			JSONArray data = new JSONArray();
			JSONObject jsonObject1 = new JSONObject();
			JSONObject jsonObject2 = new JSONObject();
			JSONObject jsonObject3 = new JSONObject();
			JSONObject jsonObject4 = new JSONObject();
			HashMap<String, String> condition1 = new HashMap<String, String>();
			condition1.put("postsubjectGid", gid);
			condition1.put("options", "1");
			String option1 = postAnswerService.getConditonCount(condition1);
			HashMap<String, String> condition2 = new HashMap<String, String>();
			condition2.put("postsubjectGid", gid);
			condition2.put("options", "2");
			String option2 = postAnswerService.getConditonCount(condition2);
			HashMap<String, String> condition3 = new HashMap<String, String>();
			condition3.put("postsubjectGid", gid);
			condition3.put("options", "3");
			String option3 = postAnswerService.getConditonCount(condition3);
			HashMap<String, String> condition4 = new HashMap<String, String>();
			condition4.put("postsubjectGid", gid);
			condition4.put("options", "4");
			String option4 = postAnswerService.getConditonCount(condition4);
			PostSubject postSubject = postSubjectService.get(gid);
			int totle = Integer.parseInt(option1) + Integer.parseInt(option2)
					+ Integer.parseInt(option3) + Integer.parseInt(option4);
			ret.put("totle", totle);
			ret.put("title", postSubject.getQuestion());
			jsonObject1.put("content", postSubject.getOption1());
			jsonObject1.put("count", option1);
			jsonObject2.put("content", postSubject.getOption2());
			jsonObject2.put("count", option2);
			jsonObject3.put("content", postSubject.getOption3());
			jsonObject3.put("count", option3);
			jsonObject4.put("content", postSubject.getOption4());
			jsonObject4.put("count", option4);
			data.add(jsonObject1);
			data.add(jsonObject2);
			data.add(jsonObject3);
			data.add(jsonObject4);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
			// } catch (CustomException e) {
			// ret.put("code", "0");
			// ret.put("message", e.getMessage());
			// ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	

	

	private String resultList;

	public String getResultList() {
		return resultList;
	}

	public void setResultList(String resultList) {
		this.resultList = resultList;
	}

	private String pic;
	private String chGid;
	private String dateBegin;
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}




	private String count;
	private String isFav;
	private String detail;
	private String lstPic;
	private String lstVid;
	private String gid;
	private String toUserId;
	private String slogan;
	private String type;
	private String pageNum;
	private String pageSize;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	private String postId;
	private String mark;
	private String top;
	private String title;
	private String location;
	private String replyGid;
	private String orderNum;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getIsFav() {
		return isFav;
	}

	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getChGid() {
		return chGid;
	}

	public void setChGid(String chGid) {
		this.chGid = chGid;
	}

	
	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getLstPic() {
		return lstPic;
	}

	public void setLstPic(String lstPic) {
		this.lstPic = lstPic;
	}

	public String getLstVid() {
		return lstVid;
	}

	public void setLstVid(String lstVid) {
		this.lstVid = lstVid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getReplyGid() {
		return replyGid;
	}

	public void setReplyGid(String replyGid) {
		this.replyGid = replyGid;
	}

}

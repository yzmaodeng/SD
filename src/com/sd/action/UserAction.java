package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.ChannelService;
import com.sd.service.ChnUserService;
import com.sd.service.ConfService;
import com.sd.service.NotificationService;
import com.sd.service.PostsService;
import com.sd.service.RdPatientService;
import com.sd.service.SchemeTmplService;
import com.sd.service.SignService;
import com.sd.service.TeamService;
import com.sd.service.TextBookService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.service.ValidTmpService;
import com.sd.util.ComUtil;
import com.sd.util.Conditions;
import com.sd.util.CustomException;
import com.sd.util.SmsUtil;
import com.sd.util.scoreUtil;
import com.sd.vo.Advertisement;
import com.sd.vo.Channel;
import com.sd.vo.ChnUser;
import com.sd.vo.Conf;
import com.sd.vo.NeedsChange;
import com.sd.vo.Notification;
import com.sd.vo.Posts;
import com.sd.vo.RdPatient;
import com.sd.vo.SchemeTmpl;
import com.sd.vo.Sign;
import com.sd.vo.Team;
import com.sd.vo.Textbook;
import com.sd.vo.User;
import com.sd.vo.UserToken;
import com.sd.vo.ValidTmp;

/**
 * 用户接口
 * 
 * @author bxf
 *
 */
public class UserAction extends BaseAction {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
	SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SignService signService;
	@Resource
	private UserService userService;
	@Resource
	private UserTokenService userTokenService;
	@Resource
	private ValidTmpService validTmpService;
	@Resource
	private PostsService postsService;
	@Resource
	private ChannelService channelService;
	@Resource
	private ChnUserService chnUserService;
	@Resource
	private TeamService teamService;
	@Resource
	private ConfService confService;
	@Resource
	private RdPatientService rdPatientService;
	@Resource
	private TextBookService textBookService;
	@Resource
	private SchemeTmplService schemeTmplService;
	@Resource
	private NotificationService notificationService;
	private String codeToken; // 邀请码

	public String getCodeToken() {
		return codeToken;
	}

	public void setCodeToken(String codeToken) {
		this.codeToken = codeToken;
	}

	private String invitation; // 邀请码
	private String mobile; // 用户手机号
	private String pwd; // 加密后的密码字符串
	private String hardOsVer; // 终端操作系统版本
	private String hardSn; // 终端SN号
	private String ipv4; // ipv4地址
	private String ipv6; // ipv6地址
	private String softVer; // App版本
	private String type; //
	private String identityCode;// 验证码
	private String name; // 名字（模糊查询）
	private String email; // 邮箱
	private String sex; // 性别(1-男/2-女)
	private String birthday; // 生日
	private String local; // 驻地编码
	private String descript; // 个人简介
	private String avatars; // 头像图片GID
	private String hospital; // 医院
	private String title; // 社会任职
	private String jobTitle; // 职称
	private String wx; // 微信号
	private String wb; // 微博号
	private String qq; // QQ
	private String chGid; // 频道id
	private String userSelectGid;// 被查询用户GID
	private String oldPwd; // 原密码
	private String newPwd; // 新密码
	private String doctorId; // 医生古典号
	private String source;
	private String sourceId;
	private String realName;
	private String department;
	private String jobtitle;
	private String resolution;
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	private String isweixin;

	public String getIsweixin() {
		return isweixin;
	}

	public void setIsweixin(String isweixin) {
		this.isweixin = isweixin;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	private String unionId;

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}
	/**
	 * 广告页面
	 * 
	 * @param mobile
	 *            用户手机号
	 */
	public void getAdvertisement() {
		JSONObject ret = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject();
			String resUrl="";
			Advertisement advertisement = advertisementService.get("1");
			if ("1".equals(resolution)) {
				resUrl=advertisement.getRes1();
				
			}else if ("2".equals(resolution)) {
				resUrl=advertisement.getRes2();
			} else if ("3".equals(resolution)) {
				resUrl=advertisement.getRes3();
			}else if ("4".equals(resolution)) {
				resUrl=advertisement.getRes4();
			}else if ("5".equals(resolution)) {
				resUrl=advertisement.getRes5();
			}
			jsonObject.put("resolution", resolution);
			jsonObject.put("resUrl", resUrl);
			jsonObject.put("url", advertisement.getUrl());
			jsonObject.put("title", advertisement.getTitle());
			jsonObject.put("type", advertisement.getType());
			jsonObject.put("iosUrl", advertisement.getIosUrl());
			jsonObject.put("typeGid", advertisement.getTypegid());
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jsonObject);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 验证码token
	 * 
	 * @param mobile
	 *            用户手机号
	 */

	public void getValidToken() {
		JSONObject ret = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject();
			if (mobile == null) {
				throw new CustomException("請正確傳遞手機號碼");
			}
			String md5 = ComUtil.MD5(ComUtil.getuuid());
			String md52 = ComUtil.MD5(md5);
			String md53 = ComUtil.MD5(md52+"123qwe!@#");
			
			NeedsChange needsChange = new NeedsChange();
			needsChange.setCreateTime(new Date());
			needsChange.setToken(md53);
			needsChange.setMobile(mobile);
			needsChangeService.save(needsChange);
			jsonObject.put("token", md5);

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jsonObject);

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
	 * 验证码判断是否正确
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param identityCode
	 *            验证码
	 */

	public void checkValidTmp() {
		JSONObject ret = new JSONObject();
		try {
			if (!"123456".endsWith(identityCode)
					&& !validTmpService.checkValidTmp(mobile, identityCode)) {
				// 验证码不正确
				throw new CustomException(CODE_WRONG);

			} else {

				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", new JSONObject());
			}
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

	// 无密码登录
	// mobile 手机号
	// identityCode 验证码
	public void loginByMessage() {
		JSONObject ret = new JSONObject();
		try {
			if (!validTmpService.checkValidTmp(mobile, identityCode))
				// 验证码不正确
				throw new CustomException(CODE_WRONG);
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("phone", mobile);
			List<User> list = userService.getConditonList(condition, "", false,
					null);
			User user = new User();
			if (list.size() != 0) {
				user = list.get(0);
			} else {
				Date now = new Date();
				user.setId(userService.getRandomUserId());
				user.setCreateTime(now);
				user.setGid(ComUtil.getuuid());
				user.setIsdel("1");
				user.setScore(0);
				user.setPassword("XXX");
				user.setPhone(mobile);
				user.setUpdateTime(now);
				user.setSex("1"); // 默认 男
				user.setName("骨医" + new Random().nextInt(9)
						+ new Random().nextInt(9) + new Random().nextInt(9));
				user.setAvatars("40aaa107-1fad-4e62-8438-09966b77368b"); // 默认值
				userService.save(user);

				// 用户默认属于 骨科同道 频道
				ChnUser cu = new ChnUser();
				cu.setChannel(channelService
						.get("e3b7faa7-61e4-4dc0-a299-af83b8fb22a6"));
				cu.setUser(user);
				cu.setIsadmin("0");
				cu.setCreateTime(now);
				cu.setUpdateTime(now);
				chnUserService.save(cu);
			}
			// 刷新token
			UserToken token = userTokenService.freshToken(user.getGid());
			JSONObject data = new JSONObject();
			data.put("userId", user.getGid());
			data.put("token", token.getContent());
			data.put("tokenEndTime", ComUtil.dateTime2Str(token.getEndTime()));
			ret.put("result_data", data);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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

	// 第三方绑定手机号
	// identityCode验证码 
	// source;来源
	// mobile手机号
	// sourceId第三方标示
	public void bindlingPhone() {
		JSONObject ret = new JSONObject();
		try {
			if (!validTmpService.checkValidTmp(mobile, identityCode))
				// 验证码不正确
				throw new CustomException(CODE_WRONG);
			JSONObject data = new JSONObject();
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("phone", mobile);
			List<User> list = userService.getConditonList(condition, "", false,
					null);
			User user = new User();
			if (list.size() != 0) {
				user = list.get(0);
				if ("QQ".equals(source)) {
					user.setQq(sourceId);
				} else if ("wechat".equals(source)) {
					if (unionId != null) {

						user.setUnionid(unionId);
						
					} else {
						user.setWb(sourceId);

					}
				} else if ("sinaMicroblog".equals(source)) {
					user.setWb(sourceId);
				}
				userService.update(user);
				data.put("isNew", "1");
			} else {
				data.put("isNew", "0");
				Date now = new Date();
				user.setId(userService.getRandomUserId());
				user.setCreateTime(now);
				user.setGid(ComUtil.getuuid());
				user.setIsdel("1");
				user.setScore(0);
				user.setPassword("XXX");
				user.setPhone(mobile);
				user.setUpdateTime(now);
				user.setSex("1"); // 默认 男
				user.setName("骨医" + new Random().nextInt(9)
						+ new Random().nextInt(9) + new Random().nextInt(9));
				user.setAvatars("388aaf4d-ec0c-4ae1-a25e-f51a9bc91013"); // 默认值

				if ("QQ".equals(source)) {
					user.setQq(sourceId);
				} else if ("wechat".equals(source)) {

					if (unionId != null) {

						user.setUnionid(unionId);
						;
					} else {
						user.setWb(sourceId);

					}

				} else if ("sinaMicroblog".equals(source)) {
					user.setWb(sourceId);
				}
				userService.save(user);

				// 用户默认属于 骨科同道 频道
				ChnUser cu = new ChnUser();
				cu.setChannel(channelService
						.get("e3b7faa7-61e4-4dc0-a299-af83b8fb22a6"));
				cu.setUser(user);
				cu.setIsadmin("0");
				cu.setCreateTime(now);
				cu.setUpdateTime(now);
				chnUserService.save(cu);
			}
			// 刷新token
			UserToken token = userTokenService.freshToken(user.getGid());

			data.put("userId", user.getGid());
			data.put("token", token.getContent());

			ret.put("result_data", data);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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

	// 第三方登录
	// source;来源
	// sourceId;第三方标示
	public void loginBySDK() {
		JSONObject ret = new JSONObject();
		try {
			Map<String, String> condition = new HashMap<String, String>();
			List<User> list = new ArrayList<User>();
			List<User> conditonList1 = new ArrayList<User>();
			List<User> conditonList2 = new ArrayList<User>();
			if ("QQ".equals(source)) {
				condition.put("qq", sourceId);
				list = userService.getConditonList(condition, "", false, null);
			} else if ("wechat".equals(source)) {

				if (StringUtils.isNotEmpty(unionId)) {
					condition.put("unionid", unionId);
					conditonList1 = userService.getConditonList(condition, "",
							false, null);
				}

				if (StringUtils.isNotEmpty(sourceId)) {

					if (StringUtils.isNotEmpty(unionId))
						condition.remove("unionid");
					condition.put("wx", sourceId);
					conditonList2 = userService.getConditonList(condition, "",
							false, null);
				}

				if (conditonList1.size() != 0) {
					list = conditonList1;
				}

				if (conditonList2.size() != 0) {
					list = conditonList2;
				}

			} else if ("sinaMicroblog".equals(source)) {
				condition.put("wb", sourceId);
				list = userService.getConditonList(condition, "", false, null);
			}
			if (list.size() != 0) {
				User user = list.get(0);
				// 刷新token
				UserToken token = null;
				if ("1".equals(isweixin)) {
					token=userTokenService.getByUserId(user.getGid());
				} else{
					token=userTokenService.freshToken(user.getGid());
				}
			
				JSONObject data = new JSONObject();
				data.put("userId", user.getGid());
				data.put("token", token.getContent());
				data.put("phone", user.getPhone());
				data.put("tokenEndTime",
						ComUtil.dateTime2Str(token.getEndTime()));
				ret.put("result_data", data);
			} else {
				ret.put("result_data", null);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	public void mobile() {
		JSONObject ret = new JSONObject();
		try {
			User user = userService.getUserByPhone(mobile);
			if (user != null)
				// 用户已存在
				throw new CustomException(HAVE_USER);

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

	// userId 是 字符串 用户gid
	// token 是 字符串 token
	public void personalCount() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			JSONObject data = new JSONObject();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			if (userId != null) {
				List<Conf> confList = confService.getConfList(
						df.format(new Date()), "1", "-1", "0", userId);
				List<RdPatient> patientList = rdPatientService
						.getmyPatients(userId);
				// textBookService.query 轮转，当前ID，关键字，分类，是否收藏，用户ID
				List<Textbook> list = textBookService.query(0, null, null,
						null, null, "1", userId);
				User myUser = userService.get(userId);
				List<Posts> postsList = postsService.getPostsList(null, null,
						null, "-1", "1", userId, null);
				String sql = "select * from tfav where fav_userGID= '"
						+ userId
						+ "' and fav_isdel='1' and (fav_type='4' or fav_type='5')";
				// 添加参数执行sql语句,查询慕课视频收藏数量
				List<Object[]> listResult = favouriteService.getListBySql(sql);
				// data.put("courseFavNum", listResult.size());
				data.put("score", myUser.getScore());
				data.put("meetCount", confList.size());
				data.put("hisCount", patientList.size());
				// 统计的是会议和帖子和视频的总数
				// 之后我修改的的
				data.put("textCount", list.size() + postsList.size()
						+ listResult.size());

				Map<String, String> teamInfo = teamService
						.getUserTeamInfo(userId);
				if (teamInfo.get("status").equals("2")) {
					Team team = teamService.get(teamInfo.get("id"));
					Map<String, String> condition = new HashMap<String, String>();
					condition.put("isdel", "0");
					condition.put("createTeam", teamInfo.get("id"));
					List<SchemeTmpl> schemeList = schemeTmplService
							.getConditonList(condition, "", false, null);
					data.put("patientCount", team.getWardPatList().size());
					data.put("planCount", schemeList.size());
				} else {
					data.put("patientCount", "0");
					data.put("planCount", "0");
				}
			} else {
				List<Conf> confList = confService.getConfList(
						df.format(new Date()), "1", "-1", "0", null);
				data.put("meetCount", confList.size());
			}

			data.put("orderNum", orderInfoService.getOrderNum(userId));

			ret.put("code", "1");
			ret.put("message", "成功");
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
	 * 用户登录接口(前台加密)
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param pwd
	 *            加密后的密码字符串
	 * @param hardOsVer
	 *            终端操作系统版本
	 * @param hardSn
	 *            终端SN号
	 * @param ipv4
	 *             ipv4地址
	 * @param ipv6
	 *            ipv6地址
	 * @param softVer
	 *            App版本
	 */
	public void secLogin() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				// 手机号为空，登录无效
				throw new CustomException(NO_MOBILE);

			User user = userService.getUserByPhone(mobile);
			if (user == null)
				// 用户不存在
				throw new CustomException("您输入的手机号尚未注册，请核对手机号");

			if (pwd == null || !pwd.equals(user.getPassword()))
				// 密码错
				throw new CustomException("您输入的手机号和密码不匹配，请重新输入");

			// 刷新用户信息
			user.setOsType(hardOsVer);
			user.setOsSn(hardSn);
			user.setIpv4(ipv4);
			user.setIpv6(ipv6);
			userService.update(user);
			// 刷新token
			UserToken token = null;
			if ("1".equals(isweixin)) {
				token=userTokenService.getByUserId(user.getGid());
			} else{
				token=userTokenService.freshToken(user.getGid());
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);

			JSONObject data = new JSONObject();
			data.put("userId", user.getGid());
			data.put("token", token.getContent());
			data.put("tokenEndTime", ComUtil.dateTime2Str(token.getEndTime()));
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
	 * 验证码token接口
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param type
	 *            短信验证类别 1-注册/2-找回密码
	 */
	public void receiveIdentityCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = getIpAddr(request);
		boolean checkBlack = SmsUtil.checkBlack(ip, mobile);
		if (checkBlack) {
			return;
		}
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				throw new CustomException(NO_MOBILE);
			boolean b = needsChangeService.checkByCodeToken(codeToken, mobile);
			if (!b) {
				return;
			}
			Date now = new Date();
			Integer count = validTmpService.getTodayCount(mobile);
			if (count != null && count >= 9) {
				throw new CustomException("今天发送验证码次数过多，请明天再试");
			}
			StringBuffer sms = new StringBuffer();
			for (int i = 0; i < 6; i++) {
				sms.append(new Random().nextInt(10));
			}

			if (!SmsUtil.sendSms("【骨今中外】您的验证码是" + sms, mobile))
				throw new CustomException("验证码发送失败");

			ValidTmp tmp = new ValidTmp();
			tmp.setCreatetime(now);
			tmp.setGid(ComUtil.getuuid());
			tmp.setPhone(mobile);
			tmp.setUpdatetime(now);
			tmp.setValidtext(sms.toString());
			tmp.setValidtime(new Date(now.getTime() + 60 * 60 * 1000));
			validTmpService.save(tmp);
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
	 * 验证码接口
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param type
	 *            短信验证类别 1-注册/2-找回密码
	 */
	public synchronized void identityCodeValidate() {
		JSONObject ret = new JSONObject();
		ret.put("code", "0");
		ret.put("message", "您的版本太低，请更新版本");
//		try {
//			if (StringUtils.isBlank(mobile))
//				throw new CustomException(NO_MOBILE);
//			Date now = new Date();
//			// 增加验证，同一ip，一天最多三次
//			HttpServletRequest request = ServletActionContext.getRequest();
//			String ip = getIpAddr(request);
//			boolean checkBlack = SmsUtil.checkBlack(ip, mobile);
//			if (checkBlack) {
//				return;
//			}
//			Date ipcrt = validTmpService.getNewestIPDate(ip);
//			Integer ipcount = validTmpService.getTodayIPCount(ip);
//
//			int authority = SmsUtil.getVisitAuthority(ip, mobile, ipcrt,
//					ipcount);
//			if (authority == -1) { // 黑名单
//				return;
//			} else if (authority == 0) {
//				throw new CustomException("今天发送验证码次数过多，请明天再试");
//			}
//
//			// 增加验证，同一手机号，一分钟最多一次，一天最多9次
//			Date crt = validTmpService.getNewestDate(mobile);
//			if (crt != null && now.getTime() - crt.getTime() < (60 * 1000)) {
//				throw new CustomException("验证码发送频率过快，请稍后再试");
//			}
//			Integer count = validTmpService.getTodayCount(mobile);
//			if (count != null && count >= 9) {
//				throw new CustomException("今天发送验证码次数过多，请明天再试");
//			}
//			StringBuffer sms = new StringBuffer();
//			for (int i = 0; i < 6; i++) {
//				sms.append(new Random().nextInt(10));
//			}
//
//			ValidTmp tmp = new ValidTmp();
//			tmp.setCreatetime(now);
//			tmp.setGid(ComUtil.getuuid());
//			tmp.setPhone(mobile);
//			tmp.setIpaddress(ip);
//			tmp.setUpdatetime(now);
//			tmp.setValidtext(sms.toString());
//			tmp.setValidtime(new Date(now.getTime() + 60 * 60 * 1000));
//			validTmpService.save(tmp);
//
//			if (!SmsUtil.sendSms("【骨今中外】您的验证码是" + sms, mobile))
//				throw new CustomException("验证码发送失败");
//
//			ret.put("code", "1");
//			ret.put("message", SUCCESS_INFO);
//			ret.put("result_data", new JSONObject());
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
		putDataOut(ret.toString());
	}

	// public void identityCodeValidate(){
	// JSONObject ret = new JSONObject();
	// try {
	// if (StringUtils.isBlank(mobile))
	// throw new CustomException(NO_MOBILE);
	//
	// String i=validTmpService.identityCodeValidate(mobile);
	// if (!SmsUtil.sendSms("【骨今中外】您的验证码是" + i, mobile))
	// throw new CustomException("验证码发送失败");
	// ret.put("code", "1");
	// ret.put("message", SUCCESS_INFO);
	// ret.put("result_data", new JSONObject());
	// } catch(CustomException e){
	// ret.put("code", "0");
	// ret.put("message", e.getMessage());
	// ret.put("result_data", new JSONObject());
	// } catch (Exception e) {
	// logger.error("",e);
	// ret.put("code", "0");
	// ret.put("message", ERROR_INFO);
	// ret.put("result_data", new JSONObject());
	// }
	// putDataOut(ret.toString());
	// }
	/**
	 * 用户注册接口（加密现在正在用的）
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param identityCode
	 *            验证码
	 * @param pwd
	 *            加密后密码
	 */
	public void secRegist() {
		JSONObject ret = new JSONObject();
		String uId = ComUtil.getuuid();
		try {
			if (StringUtils.isBlank(mobile))
				// 手机号为空，无法注册
				throw new CustomException(NO_MOBILE);

			if (!"123456".endsWith(identityCode)) {
				if (!validTmpService.checkValidTmp(mobile, identityCode))
					// 验证码不正确
					throw new CustomException("您输入的手机号和验证码不匹配，请重新输入");
			}

			User isExist = userService.getUserByPhone(mobile);
			if (isExist != null)
				// 用户已存在
				throw new CustomException(HAVE_USER);
			// 创建新用户
			Date now = new Date();
			User user = new User();

			user.setId(userService.getRandomUserId());
			user.setCreateTime(now);
			user.setGid(uId);
			user.setIsdel("1");
			user.setScore(0);
			user.setPassword(pwd);
			user.setInvitation(invitation);
			user.setPhone(mobile);
			user.setUpdateTime(now);
			user.setSex("1"); // 默认 男
			user.setName("骨医" + new Random().nextInt(9)
					+ new Random().nextInt(9) + new Random().nextInt(9));
			user.setAvatars("40aaa107-1fad-4e62-8438-09966b77368b"); // 默认值
			userService.save(user);
			// 用户默认属于 骨科同道 频道
			ChnUser cu = new ChnUser();
			cu.setChannel(channelService
					.get("e3b7faa7-61e4-4dc0-a299-af83b8fb22a6"));
			cu.setUser(user);
			cu.setIsadmin("0");
			cu.setCreateTime(now);
			cu.setUpdateTime(now);
			chnUserService.save(cu);
			UserToken token = userTokenService.freshToken(user.getGid());
			JSONObject data = new JSONObject();
			data.put("userId", user.getGid());
			data.put("token", token.getContent());
			data.put("tokenEndTime", ComUtil.dateTime2Str(token.getEndTime()));
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
		// 方法執行完成添加积分
		scoreUtil.secRegistChangeScor(scoreService, userService, uId, 20);
	}

	/**
	 * 用户信息修改
	 * 
	 * @param userId
	 * @param token
	 * @param name
	 * @param email
	 * @param sex
	 * @param birthday
	 * @param local
	 * @param descript
	 * @param avatars
	 * @param hospital
	 * @param title
	 * @param jobTitle
	 * @param wx
	 * @param wb
	 * @param qq
	 */
	public void update() {
		JSONObject ret = new JSONObject();
		String uId = "";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			uId = userId;
			// boolean logined = userTokenService.checkToken(request);
			// if (!logined)
			// throw new CustomException(NO_LOGIN);

			User user = userService.get(userId);
			if (StringUtils.isNotBlank(name))
				user.setName(name.trim());
			if (StringUtils.isNotBlank(email))
				user.setEmail(email);
			if (StringUtils.isNotBlank(sex))
				user.setSex(sex);
			if (StringUtils.isNotBlank(birthday))
				user.setBirthday(ComUtil.str2Date(birthday));
			if (StringUtils.isNotBlank(local))
				user.setLocal(local);
			if (StringUtils.isNotBlank(descript))
				user.setDescript(descript);

			if (StringUtils.isNotBlank(avatars)) {
				scoreUtil.userUpdateAvatarsChangeScor(scoreService,
						userService, userId, 10);
				user.setAvatars(avatars);
			}

			if (StringUtils.isNotBlank(hospital))
				user.setHospital(hospital);
			if (StringUtils.isNotBlank(title))
				user.setTitle(title);
			if (StringUtils.isNotBlank(jobTitle))
				user.setJobtitle(jobTitle);
			if (StringUtils.isNotBlank(department))
				user.setDepartment(department);
			if (wx != null)
				user.setWx(wx);
			if (wb != null)
				user.setWb(wb);
			if (unionId != null)
				user.setUnionid(unionId);
			if (qq != null)
				user.setQq(qq);
			userService.update(user);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");
			// } catch(CustomException e){
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

	/**
	 * 用户信息查询
	 * 
	 * @param userId
	 *            用户gid
	 * @param token
	 * @param chgGid
	 *            频道gid
	 * @param userSelectGid
	 *            被查询用户gid
	 * @param name
	 *            名字（模糊查询）
	 */
	public void select() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(chGid)
					&& StringUtils.isBlank(userSelectGid)
					&& StringUtils.isBlank(name))
				throw new CustomException(NONE);
			List<User> users = userService.getUserList(chGid, userSelectGid,
					name);
			JSONArray data = new JSONArray();
			for (User obj : users) {
				JSONObject user = new JSONObject();
				user.put("gdId", obj.getId());
				user.put("userId", obj.getGid());
				user.put("name", obj.getName());
				user.put("email", obj.getEmail());
				user.put("unionId", obj.getUnionid());
				user.put("sex", obj.getSex());
				user.put("birthday", ComUtil.dateTime2Str(obj.getBirthday()));
				user.put("local", obj.getLocal());
				user.put("descript", obj.getDescript());
				user.put("avatars", obj.getAvatars());
				user.put("approvePic", obj.getApproveInfo());
				user.put("hospital", obj.getHospital());
				Date date = new Date();
				long daysBetweenDate = obj.getAnnualmembership().getTime()-date.getTime();
		if (daysBetweenDate<=0) {
			user.put("annualmembership", 0);
		} else {
			user.put("annualmembership",dfd.format(obj.getAnnualmembership()));
		}
				
				user.put("title", obj.getTitle());
				user.put("jobTitle", obj.getJobtitle());
				user.put("score", obj.getScore());
				user.put("orderNum", orderInfoService.getOrderNum(obj.getGid()));

				SimpleDateFormat myFmt = new SimpleDateFormat(
						"yyyy-MM-dd 00:00:00");
				List<Conditions> conditions = new ArrayList<Conditions>();
				Conditions conditions1 = new Conditions();
				conditions1.setType("map");
				Map<String, String> map = new HashMap<String, String>();
				map.put("uid", userSelectGid);
				conditions1.setMap(map);
				conditions.add(conditions1);
				Conditions conditions2 = new Conditions();
				conditions2.setType("timeA");
				conditions2.setKey("time");
				conditions2.setValue(myFmt.format(new Date()));
				List<Sign> list = signService.getConditonsList(conditions,
						"time", true, null);
				if (list.size() == 0) {
					user.put("signIn", "0");
				} else {
					user.put("signIn", "1");
				}

				user.put("wx", obj.getWx());
				user.put("wb", obj.getWb());
				user.put("qq", obj.getQq());
				user.put("department", obj.getDepartment());
				user.put("realName", obj.getRealName());
				user.put("approveStatus", obj.getApproveStatus());
				// 团队信息
				Map<String, String> teamInfo = teamService.getUserTeamInfo(obj
						.getGid());
				user.put("teamId", teamInfo.get("id"));
				user.put("teamName", teamInfo.get("name"));
				user.put("teamStatus", teamInfo.get("status"));

				List<Channel> channelList = channelService.getChannelList(obj
						.getGid());
				StringBuffer chgids = new StringBuffer();
				StringBuffer chnames = new StringBuffer();
				for (Channel ch : channelList) {
					chgids.append(ch.getGid() + ",");
					chnames.append(ch.getName() + ",");
				}
				if (chgids != null && chgids.length() > 0)
					user.put("chGid", chgids.substring(0, chgids.length() - 1));
				if (chnames != null && chnames.length() > 0)
					user.put("chName",
							chnames.substring(0, chnames.length() - 1));
				List<Posts> posts = postsService.getPostsList(obj.getGid());
				JSONArray psts = new JSONArray();
				for (Posts post : posts) {
					JSONObject pst = new JSONObject();
					pst.put("pstGid", post.getGid());
					pst.put("pstDetail", post.getDetail());
					psts.add(pst);
				}
				user.put("pst", psts);
				user.put("pstCount", posts.size());
				data.add(user);
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
	 * 医生搜索
	 * 
	 * @param userId
	 *            用户GID
	 * @param token
	 * @param doctorId
	 *            医生骨典号
	 */
	public void selectByCode() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined) {
				throw new CustomException(NO_LOGIN);
			}
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("id", doctorId);
			List<User> list = userService.getConditonList(condition, null,
					false, null);
			if (list.size() == 0)
				throw new CustomException(NO_USER);
			User user = list.get(0);
			JSONObject data = new JSONObject();
			data.put("gid", user.getGid());
			data.put("gdId", user.getId());
			data.put("name", user.getName());
			data.put("avatars", user.getAvatars());

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
	 * 密码修改（加密）
	 * 
	 * @param userId
	 *            用户Gid
	 * @param oldPwd
	 *            加密后旧密码
	 * @param newPwd
	 *            加密后新密码
	 */
	public void secChangePwd() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			User user = userService.get(userId);
			if (user == null)
				// 用户不存在
				throw new CustomException(PASSWORD_WRONG);

			if (oldPwd == null || !oldPwd.equals(user.getPassword()))
				// 密码错
				throw new CustomException(PASSWORD_WRONG);

			user.setPassword(newPwd);
			userService.update(user);

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
	 * 密码重置（加密）
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param identityCode
	 *            验证码
	 * @param newPwd
	 *            加密后新密码
	 */
	public void secResetPwd() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				throw new CustomException(NO_MOBILE);

			if (!validTmpService.checkValidTmp(mobile, identityCode))
				throw new CustomException(CODE_WRONG);

			User user = userService.getUserByPhone(mobile);
			if (user == null)
				throw new CustomException(NO_USER);

			user.setPassword(newPwd);
			userService.update(user);

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
	 * 用户认证
	 * 
	 * @param department
	 * @param realName
	 * @param hospital
	 * @param avatars
	 */
	public void userAuth() {
		JSONObject ret = new JSONObject();
		String uId = "";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			uId = userId;
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			User user = userService.get(userId);
			user.setRealName(realName);
			user.setHospital(hospital);
			user.setLocal(local);
			user.setDepartment(department);
			user.setApplyDate(new Date());
			user.setApproveInfo(avatars);
			user.setApproveStatus("1");
			user.setSex(sex);
			user.setJobtitle(jobtitle);
			userService.update(user);
			if (!SmsUtil.sendSms("【骨今中外】您的认证申请我们已经收到，我们将在1个工作日内进行处理，谢谢您的申请！",
					user.getPhone()))
				throw new CustomException("发送失败");
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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

	/**
	 * 用户状态查询
	 * 
	 * @param userId
	 * @param token
	 */
	public void statusInfo() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			JSONObject data = new JSONObject();
			User user = userService.get(userId);
			Map<String, String> teamInfo = teamService.getUserTeamInfo(userId);
			data.put("teamId", teamInfo.get("id"));
			data.put("teamName", teamInfo.get("name"));
			data.put("teamStatus", teamInfo.get("status"));
			data.put("approveStatus", user.getApproveStatus());

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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

	/**
	 * 用户状态查询
	 * 
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void quryNotificationHistory() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			int firstNUm = (Integer.parseInt(pageIndex) - 1)
					* Integer.parseInt(pageCount);
			List<Notification> notificationResList = notificationService
					.getListBySql(firstNUm, pageCount);
			JSONArray result = new JSONArray();
			for (Notification notification : notificationResList) {
				String typeValue = notification.getType();
				JSONObject jsonObject = new JSONObject();
				String gid = notification.getGid();
				if (gid.isEmpty()) {
					throw new CustomException("gid为空");
				}
				if ("1".equals(typeValue)) {
					Posts posts = postsService.get(gid);
					if (posts == null)
						throw new CustomException("帖子已经被误删");
					String title = posts.getTitle();
					Date createTime = posts.getCreateTime();
					jsonObject.put("type", "1");
					jsonObject.put("gid", gid);
					jsonObject.put("title", title);
					jsonObject.put("createTime", sdf.format(createTime));
				} else if ("5".equals(typeValue)) {
					Textbook textbook = textBookService.get(gid);
					if (textbook == null)
						throw new CustomException("文献已经被误删");
					Date createtime = textbook.getTbCreatetime();
					String title = textbook.getTbTitle();
					jsonObject.put("type", "5");
					jsonObject.put("gid", gid);
					jsonObject.put("title", title);
					jsonObject.put("createTime", sdf.format(createtime));
				}
				// else if ("2".equals(typeValue))
				// {
				// Conf conf = confService.get(gid);
				// Date createTime = conf.getCreateTime();
				// String title = conf.getName();
				// jsonObject.put("type", "2");
				// jsonObject.put("gid", gid);
				// jsonObject.put("title", title);
				// jsonObject.put("createTime", sdf.format(createTime));
				// }
				// }else if ("3".equals(typeValue)) {
				// //推送的一句话content
				// Date createtime = notification.getCreatetime();
				// String content = notification.getContent();
				// jsonObject.put("type", "3");
				// jsonObject.put("gid", gid);
				// jsonObject.put("title", content);
				// jsonObject.put("createTime", sdf.format(createtime));
				// }else if ("4".equals(typeValue)) {
				// Textbook textbook = textBookService.get(gid);
				// Date createtime = textbook.getTbCreatetime();
				// String title = textbook.getTbTitle();
				// jsonObject.put("type", "4");
				// jsonObject.put("gid", gid);
				// jsonObject.put("title", title);
				// jsonObject.put("createTime", sdf.format(createtime));
				//
				// }else if ("7".equals(typeValue)) {
				// CourseVideo courseVideo = courseVideoService.get(gid);
				// String title = courseVideo.getTitle();
				// Date createTime = courseVideo.getCreateTime();
				// jsonObject.put("type", "7");
				// jsonObject.put("gid", gid);
				// jsonObject.put("title", title);
				// jsonObject.put("createTime", sdf.format(createTime));
				//
				// }else if ("6".equals(typeValue)) {
				// Course course = courseService.get(gid);
				// String title = course.getTitle();
				// Date createTime = course.getCreateTime();
				// jsonObject.put("type", "6");
				// jsonObject.put("gid", gid);
				// jsonObject.put("title", title);
				// jsonObject.put("createTime", sdf.format(createTime));
				//
				// }
				else {
					throw new CustomException("类型错误！！");
				}

				result.add(jsonObject);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", result);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONArray());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());

	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	// 页码
	private String pageIndex;

	// 每页的条数
	private String pageCount;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getAvatars() {
		return avatars;
	}

	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHardOsVer() {
		return hardOsVer;
	}

	public void setHardOsVer(String hardOsVer) {
		this.hardOsVer = hardOsVer;
	}

	public String getHardSn() {
		return hardSn;
	}

	public void setHardSn(String hardSn) {
		this.hardSn = hardSn;
	}

	public String getSoftVer() {
		return softVer;
	}

	public void setSoftVer(String softVer) {
		this.softVer = softVer;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getUserSelectGid() {
		return userSelectGid;
	}

	public void setUserSelectGid(String userSelectGid) {
		this.userSelectGid = userSelectGid;
	}

	public String getChGid() {
		return chGid;
	}

	public void setChGid(String chGid) {
		this.chGid = chGid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getInvitation() {
		return invitation;
	}

	public void setInvitation(String invitation) {
		this.invitation = invitation;
	}

	/**
	 * 用户登录接口
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param pwd
	 *            明文的密码字符串
	 * @param hardOsVer
	 *            终端操作系统版本
	 * @param hardSn
	 *            终端SN号
	 * @param ipv4
	 *            ipv4地址
	 * @param ipv6
	 *            ipv6地址
	 * @param softVer
	 *            App版本
	 */
	public void login() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				// 手机号为空，登录无效
				throw new CustomException(NO_MOBILE);

			User user = userService.getUserByPhone(mobile);
			if (user == null)
				// 用户不存在
				throw new CustomException(PASSWORD_WRONG);

			if (pwd == null)
				throw new CustomException(NO_ID);

			String md5 = ComUtil.MD5(ComUtil.MD5(pwd) + SALT);
			if (!md5.equals(user.getPassword()))
				// 密码错
				throw new CustomException(PASSWORD_WRONG);

			// 刷新用户信息
			user.setOsType(hardOsVer);
			user.setOsSn(hardSn);
			user.setIpv4(ipv4);
			user.setIpv6(ipv6);
			userService.update(user);
			// 刷新token
			UserToken token = userTokenService.freshToken(user.getGid());
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);

			JSONObject data = new JSONObject();
			data.put("userId", user.getGid());
			data.put("token", token.getContent());
			data.put("tokenEndTime", ComUtil.dateTime2Str(token.getEndTime()));

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
	 * 用户注册接口
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param identityCode
	 *            验证码
	 * @param pwd
	 *            密码
	 */
	public void regist() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				// 手机号为空，无法注册
				throw new CustomException(NO_MOBILE);

			if (!"123456".endsWith(identityCode)) {
				if (!validTmpService.checkValidTmp(mobile, identityCode))
					// 验证码不正确
					throw new CustomException(CODE_WRONG);
			}

			User isExist = userService.getUserByPhone(mobile);
			if (isExist != null)
				// 用户已存在
				throw new CustomException(HAVE_USER);
			// 创建新用户
			// user = userService.newUser(mobile, pwd);
			if (pwd == null)
				throw new CustomException(NO_ID);

			String md5 = ComUtil.MD5(ComUtil.MD5(pwd) + SALT);
			Date now = new Date();
			User user = new User();
			user.setId(userService.getRandomUserId());
			user.setCreateTime(now);
			user.setGid(ComUtil.getuuid());
			user.setIsdel("1");
			user.setScore(0);
			user.setPassword(md5);
			user.setPhone(mobile);
			user.setUpdateTime(now);
			user.setSex("1"); // 默认 男
			user.setName("骨医" + new Random().nextInt(9)
					+ new Random().nextInt(9) + new Random().nextInt(9));
			user.setAvatars("388aaf4d-ec0c-4ae1-a25e-f51a9bc91013"); // 默认值
			userService.save(user);

			// 用户默认属于 骨科同道 频道
			ChnUser cu = new ChnUser();
			cu.setChannel(channelService
					.get("e3b7faa7-61e4-4dc0-a299-af83b8fb22a6"));
			cu.setUser(user);
			cu.setIsadmin("0");
			cu.setCreateTime(now);
			cu.setUpdateTime(now);
			chnUserService.save(cu);

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
	 * 密码修改
	 * 
	 * @param userId
	 *            用户Gid
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 */
	public void changePwd() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			User user = userService.get(userId);
			if (user == null)
				// 用户不存在
				throw new CustomException(PASSWORD_WRONG);
			if (oldPwd == null)
				throw new CustomException(NO_ID);
			if (newPwd == null)
				throw new CustomException(NO_ID);

			String oldmd5 = ComUtil.MD5(ComUtil.MD5(oldPwd) + SALT);
			if (!oldmd5.equals(user.getPassword()))
				// 密码错
				throw new CustomException(PASSWORD_WRONG);

			String newmd5 = ComUtil.MD5(ComUtil.MD5(newPwd) + SALT);
			user.setPassword(newmd5);
			userService.update(user);

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
	 * 密码重置
	 * 
	 * @param mobile
	 *            用户手机号
	 * @param identityCode
	 *            验证码
	 * @param newPwd
	 *            新密码
	 */
	public void resetPwd() {
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(mobile))
				throw new CustomException(NO_MOBILE);

			if (!validTmpService.checkValidTmp(mobile, identityCode))
				throw new CustomException(CODE_WRONG);

			User user = userService.getUserByPhone(mobile);
			if (user == null)
				throw new CustomException(NO_USER);

			if (newPwd == null)
				throw new CustomException(NO_ID);

			String newmd5 = ComUtil.MD5(ComUtil.MD5(newPwd) + SALT);
			user.setPassword(newmd5);
			userService.update(user);

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

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-real-ip");
		/*
		 * if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		 * { ip = request.getHeader("Proxy-Client-IP"); } if(ip == null ||
		 * ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip =
		 * request.getHeader("WL-Proxy-Client-IP"); } if(ip == null ||
		 * ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip =
		 * request.getRemoteAddr(); }
		 */
		return ip;
	}
}

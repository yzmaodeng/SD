package com.sd.action;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.sd.service.AcademyService;
import com.sd.service.AcademyUserService;
import com.sd.service.AdvertisementService;
import com.sd.service.AnnouncementService;
import com.sd.service.ColumnContentService;
import com.sd.service.ColumnsService;
import com.sd.service.CouponService;
import com.sd.service.CourseAnnouncementService;
import com.sd.service.CourseExpertService;
import com.sd.service.CourseReplyService;
import com.sd.service.CourseService;
import com.sd.service.CourseVideoService;
import com.sd.service.FavouriteService;
import com.sd.service.InvoiceService;
import com.sd.service.NeedsChangeService;
import com.sd.service.NoticeService;
import com.sd.service.OrderInfoService;
import com.sd.service.PostAnswerService;
import com.sd.service.PostSubjectService;
import com.sd.service.PostsService;
import com.sd.service.ScoreService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.CustomException;

public class BaseAction extends ActionSupport {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat stf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	private static final long serialVersionUID = 1L;
	protected static String token = "";
	protected static Date tokenDate;
	protected static String jsapi_ticket = "";
	protected static Date ticketDate;
	private static final String HEADER_ENCODING = "UTF-8";
	private static final boolean HEADER_NO_CACHE = true;
	private static final String HEADER_TEXT_CONTENT_TYPE = "text/plain";
	protected static final String NO_LOGIN = "登录无效";
	protected static final String SUCCESS_INFO = "操作成功";
	protected static final String ERROR_INFO = "抱歉，服务器异常";
	protected static final String NO_CONF = "会议不存在";
	protected static final String NO_USER = "用户不存在";
	protected static final String NO_CHANNEL = "频道不存在";
	protected static final String NO_POST = "帖子不存在";
	protected static final String NO_REPLY = "回复不存在";
	protected static final String NO_PATIENT = "病例不存在";
	protected static final String NO_TEMPLATE = "模版不存在";
	protected static final String NO_SCHEME = "方案不存在";
	protected static final String NO_WARDPATIENT = "患者不存在";
	protected static final String NO_REMIND = "提醒不存在";
	protected static final String NO_TEAM = "团队不存在";
	protected static final String NO_MOTION = "动作不存在";
	protected static final String NO_RESULT = "ID不存在";
	protected static final String NO_ID = "参数ID为空";
	protected static final String ONLY_SELF = "只能操作本人的内容";
	protected static final String PARAM_ERROR = "参数异常";
	protected static final String NO_MOBILE = "手机号为空";
	protected static final String PASSWORD_WRONG = "用户不存在或密码错误";
	protected static final String CODE_WRONG = "验证码错误";
	protected static final String HAVE_USER = "您输入的手机号已注册，请核对手机号或者直接登录。";
	protected static final String NO_LEGAL_DATE = "日期不合法";
	protected static final String UNKNOWN_SCHEME = "未制定康复方案";
	protected static final String SALT = "123qwe!@#";

	@Resource
	protected CourseService courseService;
	@Resource
	protected CourseVideoService courseVideoService;
	@Resource
	protected CourseExpertService courseExpertService;
	@Resource
	protected AnnouncementService announcementService;
	@Resource
	protected UserTokenService userTokenService;
	@Resource
	protected FavouriteService favouriteService;
	@Resource
	protected CourseReplyService courseReplyService;
	@Resource
	protected UserService userService;
	@Resource
	protected NoticeService noticeService;
	@Resource
	protected PostsService postsService;
	@Resource
	protected AcademyService academyService;
	@Resource
	protected AcademyUserService academyUserService;
	@Resource
	protected CourseAnnouncementService courseAnnouncementService;
	@Resource
	protected AdvertisementService advertisementService;
	@Resource
	protected OrderInfoService orderInfoService;
	@Resource protected ScoreService scoreService;
	@Resource protected PostSubjectService postSubjectService;
	@Resource protected PostAnswerService postAnswerService;
	@Resource protected CouponService couponService;
	@Resource protected InvoiceService invoiceService;
	@Resource protected NeedsChangeService needsChangeService;
	@Resource protected ColumnsService columnsService;
	@Resource protected ColumnContentService columnContentService;
	
	/**
	 * * 替换四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）的解决方案 😁
	 * 
	 * @author 臧亮
	 * @data 2016/5/16
	 * @param content
	 * @return
	 */
	public String removeFourChar(String content) {
		byte[] conbyte = content.getBytes();
		for (int i = 0; i < conbyte.length; i++) {
			if ((conbyte[i] & 0xF8) == 0xF0) {
				for (int j = 0; j < 4; j++) {
					conbyte[i + j] = 0x30;
				}
				i += 3;
			}
		}
		content = new String(conbyte);
		return content.replaceAll("0000", "");
	}

	public void putDataOut(String obj) {
		try {
			HttpServletResponse resp = ServletActionContext.getResponse();
			resp.setCharacterEncoding(HEADER_ENCODING);
			resp.setContentType(HEADER_TEXT_CONTENT_TYPE);
			resp.addHeader("Access-Control-Allow-Origin", "*");
			resp.addHeader("Access-Control-Allow-Headers","x-requested-with,content-type,requesttype");
			if (HEADER_NO_CACHE)
				resp.addHeader("Pragma", "no-cache");
			resp.getWriter().write(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected  String emojiFilter(String str) {
		 if(str.isEmpty()||str==null){return "";}else{
			 String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(str);
				StringBuffer sb = new StringBuffer();
				while (matcher.find()) {
					try {
						 matcher.appendReplacement(sb, "[[EMOJI:" + URLEncoder.encode(matcher.group(1),"UTF-8") + "]]");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				matcher.appendTail(sb);

				return sb.toString();
			 
		 }
		
	}

	protected  String emojiRecovery(String str){
		      if(str.isEmpty()||str==null){
		    	  return "";
		      }else{
		    	  String patternString = "\\[\\[EMOJI:(.*?)\\]\\]";
			       Pattern pattern = Pattern.compile(patternString);
			       Matcher matcher = pattern.matcher(str);
			       StringBuffer sb = new StringBuffer();
			       while(matcher.find()) {
			           try {
			               matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1),"UTF-8"));
			           } catch (UnsupportedEncodingException e) {
			               e.printStackTrace();
			           }
			       }
			       matcher.appendTail(sb);
			       return sb.toString();
		    	  
		      }
		       
		   }
	
	protected void objNullException(Object object,String objnum) throws CustomException {
		if(object==null){
			throw new CustomException(objnum+"对象为空");
		  }
	}
	
	public String getProperByName(String name ) throws IOException{
		
		ClassLoader loader = BaseAction.class.getClassLoader();
		URL url = loader.getResource("db.properties");
		String filepath = url.getPath();
		FileInputStream in = new FileInputStream(filepath);
		Properties prop = new Properties();  //map
		prop.load(in);
		String dburl = prop.getProperty(name);
		in.close();
		return dburl;
	
	}
	
	

}

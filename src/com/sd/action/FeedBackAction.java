package com.sd.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.FeedbackService;
import com.sd.service.UserTokenService;
import com.sd.util.CustomException;
import com.sd.vo.Feedback;
/**
 * 意见反馈接口
 * @author bxf
 *
 */
public class FeedBackAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource private UserTokenService userTokenService;
	@Resource private FeedbackService feedbackService;
	
	private String detail;		// 反馈内容
	private String status;		// 是否接受随访0-否/1-是
	private String lstPic;		// 图片GID
	private String title;
	private String telephone;
	public String getPhoneInfo() {
		return phoneInfo;
	}

	public void setPhoneInfo(String phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	private String phoneInfo;
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 意见反馈新增
	 * @param userId
	 * @param token
	 * @param detail
	 * @param datetime
	 * @param status
	 * @param lstPic
	 */
	public void feedbackAdd(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			Feedback feedback=new Feedback();
			feedback.setFbUid(userId);
			feedback.setFbDetail(detail);
			feedback.setTitle(title);
			feedback.setFbDatetime(new Date());
			feedback.setTelePhone(telephone);
			feedback.setPhoneInfo(phoneInfo);
			if(status!=null){
				feedback.setFbStatus(Integer.parseInt(status));
			}
			feedback.setFbPic(lstPic);
			feedbackService.save(feedback);
			ret.put("code", "1");
			ret.put("message",SUCCESS_INFO);
			ret.put("result_data", "");
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLstPic() {
		return lstPic;
	}

	public void setLstPic(String lstPic) {
		this.lstPic = lstPic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
}

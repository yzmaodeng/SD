package com.sd.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.sd.service.RemindService;
import com.sd.service.TeamService;
import com.sd.service.TemplateDetailService;
import com.sd.service.TemplateService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.service.WardPatientService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.Remind;
import com.sd.vo.Team;
import com.sd.vo.Template;
import com.sd.vo.TemplateDetail;
import com.sd.vo.User;
import com.sd.vo.WardPatient;

/**
 * 查房助手接口
 * @author bxf
 *
 */

public class WardAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	@Resource private UserTokenService userTokenService;
	@Resource private UserService userService;
	@Resource private TeamService teamService;
	@Resource private TemplateDetailService templateDetailService;
	@Resource private TemplateService templateService;
	@Resource private RemindService remindService;
	@Resource private WardPatientService wardPatientService;
	
	private String curDate;
	private String teamId;
	private String patientName;
	private String patientBed;
	private String patientSex;
	private String remindList;
	private String remindDate;
	private String remindContent;
	private String gid;
	private String templeteId;
	private String startDate;
	private String templeteName;
	private String templeteNote;
	private String eventList;
	private String tmplId;
	
	/**
	 * 查房助手-首页
	 * @param userid
	 * @param token
	 * @param teamId
	 * @param curDate	yyyy-mm-dd
	 */
	public void getWardList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(teamId))
				throw new CustomException(NO_ID);
			
			Team team = teamService.get(teamId);
			if (team == null)
				throw new CustomException(NO_TEAM);
			
			Date curdate = ComUtil.str2Date(curDate);
			if (curdate == null)
				throw new CustomException(NO_LEGAL_DATE);
				
			JSONArray data = new JSONArray();
			for (WardPatient wp : team.getWardPatList()){
				JSONObject ward = new JSONObject();
				ward.put("pId", wp.getGid());
				ward.put("pName", wp.getName());
				ward.put("pSex", wp.getGender());
				ward.put("pNo", wp.getNo());
				ward.put("pBeginDate", ComUtil.dateTime2Str(wp.getBeginDate()));
				Template tmp = wp.getTemplate();
				if (tmp != null){
					ward.put("tId", tmp.getGid());
					ward.put("tName", tmp.getName());
					long bet = ComUtil.daysBetweenDate(wp.getBeginDate(), curdate) + 1;
					TemplateDetail detail = templateDetailService.getOne(tmp.getGid(), bet);
					if (detail != null){
						ward.put("tDay", bet);
						ward.put("tInfo", detail.getContent());
					} else {
						ward.put("tDay", "");
						ward.put("tInfo", "");
					}
				} else {
					ward.put("tId", "");
					ward.put("tName", "");
					ward.put("tDay", "");
					ward.put("tInfo", "");
				}
				
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("pgid", wp.getGid());
				condition.put("day", curDate);
				condition.put("isdel", "0");
				List<Remind> remindList = remindService.getConditonList(condition, "", true, null);
				
				JSONArray remind = new JSONArray();
				for (Remind re : remindList)
					remind.add(re.getContent());
				
				ward.put("remind", remind);
				data.add(ward);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	
	/**
	 * 查房助手-添加患者
	 * @param userid
	 * @param token
	 * @param teamId
	 * @param patientName
	 * @param patientBed
	 * @param patientSex
	 * @param remindList
	 * @param tmplId
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void addWardPatient(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(teamId))
				throw new CustomException(NO_ID);
			
			Team team = teamService.get(teamId);
			if (team == null)
				throw new CustomException(NO_TEAM);
			
			Date now = new Date();
			WardPatient wp = new WardPatient();
			String pgid = ComUtil.getuuid();
			wp.setCreateTime(now);
			wp.setUpdateTime(now);
			wp.setGender(patientSex);
			wp.setGid(pgid);
			wp.setIsdel("0");
			wp.setName(patientName);
			wp.setNo(patientBed);
			wp.setTeamId(teamId);
			wp.setTmplId(tmplId);
			wardPatientService.save(wp);
			
			if (StringUtils.isNotBlank(remindList)){
				JSONArray json = new JSONArray().fromObject(remindList);
				Iterator iter = json.iterator();
				while (iter.hasNext()) {
					JSONObject obj = new JSONObject().fromObject(iter.next());
					
					Remind remind = new Remind();
					remind.setCreateTime(now);
					remind.setUpdateTime(now);
					remind.setPgid(pgid);
					remind.setDay(obj.get("remindDate")+"");
					remind.setContent(obj.get("remindContent")+"");
					remind.setIsdel("0");
					remindService.save(remind);
				}
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	
	/**
	 * 查房助手-删除患者
	 * @param userid
	 * @param token
	 * @param gid		患者id
	 */
	public void deleteWardPatient(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			WardPatient patient = wardPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			patient.setIsdel("1");
			wardPatientService.update(patient);
				
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	
	/**
	 * 查房助手-患者详情
	 * @param userid
	 * @param token
	 * @param gid		患者id
	 */
	public void getPatientDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			WardPatient patient = wardPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			JSONObject data = new JSONObject();
			data.put("patientId", patient.getGid());
			data.put("name", patient.getName());
			data.put("bedNumber", patient.getNo());
			data.put("sex", patient.getGender());
			Template temp = patient.getTemplate();
			if (temp != null){
				data.put("tId", temp.getGid());
				data.put("tName", temp.getName());
			} else {
				data.put("tId", "");
				data.put("tName", "");
			}
			
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("pgid", patient.getGid());
			condition.put("isdel", "0");
			List<Remind> remindList = remindService.getConditonList(condition, "", true, null);
			
			JSONArray list = new JSONArray();
			for (Remind remind : remindList){
				JSONObject ward = new JSONObject();
				ward.put("remindId", remind.getId());
				ward.put("remindContent", remind.getContent());
				ward.put("remindDate", remind.getDay());
				list.add(ward);
			}
			data.put("eventList", list);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	
	/**
	 * 查房助手-添加提醒
	 * @param userid
	 * @param token
	 * @param gid			患者id
	 * @param remindDate	提醒日期
	 * @param remindContent	提醒内容
	 */
	public void addWard(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			WardPatient patient = wardPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			Date reminddate = ComUtil.str2Date(remindDate);
			if (reminddate == null)
				throw new CustomException(NO_LEGAL_DATE);
			
			Date now = new Date();
			Remind remind = new Remind();
			remind.setCreateTime(now);
			remind.setUpdateTime(now);
			remind.setPgid(gid);
			remind.setDay(remindDate);
			remind.setContent(remindContent);
			remind.setIsdel("0");
			remindService.save(remind);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	
	/**
	 * 查房助手-删除提醒
	 * @param userid
	 * @param token
	 * @param gid		患者id
	 */
	public void deleteWard(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Remind remind = remindService.get(gid);
			if (remind == null)
				throw new CustomException(NO_REMIND);
			
			remind.setIsdel("1");
			remind.setUpdateTime(new Date());
			remindService.update(remind);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	/**
	 * 查房助手-绑定模版提醒
	 * @param userid
	 * @param token
	 * @param gid
	 * @param templeteId
	 * @param startDate
	 */
	public void bindTemplate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			WardPatient patient = wardPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			Date startdate = ComUtil.str2Date(startDate);
			if (startdate == null)
				throw new CustomException(NO_LEGAL_DATE);
			
			patient.setBeginDate(startdate);
			patient.setTmplId(templeteId);
			patient.setUpdateTime(new Date());
			wardPatientService.update(patient);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	/**
	 * 查房助手-获取模版列表
	 * @param userid
	 * @param token
	 * @param teamId
	 */
	public void getTemplateList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(teamId))
				throw new CustomException(NO_ID);
			
			Team team = teamService.get(teamId);
			if (team == null)
				throw new CustomException(NO_TEAM);
			
			List<Template> templateList = templateService.getTmplByTeam(teamId);
			JSONArray data = new JSONArray();
			for (Template tmpl : templateList){
				JSONObject ward = new JSONObject();
				ward.put("templeteId", tmpl.getGid());
				ward.put("templeteName", tmpl.getName());
				User user = userService.get(tmpl.getUserId());
				if (user != null)
					ward.put("userName", user.getName());
				else
					ward.put("userName", "");
				data.add(ward);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	/**
	 * 查房助手-获取模版详情
	 * @param userid
	 * @param token
	 * @param gid
	 */
	public void getTemplateDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Template template = templateService.get(gid);
			if (template == null)
				throw new CustomException(NO_TEMPLATE);
			
			JSONObject data = new JSONObject();
			data.put("templeteId", template.getGid());
			data.put("templeteName", template.getName());
			data.put("templeteNote", template.getSlogan());
			
			JSONArray list = new JSONArray();
			for (TemplateDetail detail : template.getDetailList()){
				JSONObject ward = new JSONObject();
				ward.put("day", detail.getOrdering());
				ward.put("note", detail.getContent());
				list.add(ward);
			}
			data.put("eventList", list);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
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
	/**
	 * 查房助手-创建提醒模版
	 * @param userid
	 * @param token
	 * @param templeteName
	 * @param templeteNote
	 * @param eventList
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void addTemplate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			Date now = new Date();
			Template tmpl = new Template();
			String pid = ComUtil.getuuid();
			tmpl.setCreateTime(now);
			tmpl.setUpdateTime(now);
			tmpl.setGid(pid);
			tmpl.setIsdel("0");
			tmpl.setName(templeteName);
			tmpl.setSlogan(templeteNote);
			tmpl.setUserId(userId);
			templateService.save(tmpl);
			
			if (StringUtils.isNotBlank(eventList)){
				JSONArray json = new JSONArray().fromObject(eventList);
				Iterator iter = json.iterator();
				while (iter.hasNext()) {
					JSONObject obj = new JSONObject().fromObject(iter.next());
					
					TemplateDetail detail = new TemplateDetail();
					detail.setCreateTime(now);
					detail.setUpdateTime(now);
					detail.setIsdel("0");
					detail.setPid(pid);
					detail.setContent(obj.get("note")+"");
					detail.setOrdering(obj.get("day")+"");
					templateDetailService.save(detail);
				}
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	/**
	 * 查房助手-编辑提醒模版
	 * @param userid
	 * @param token
	 * @param gid
	 * @param templeteName
	 * @param templeteNote
	 * @param eventList
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void saveTemplate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Template template = templateService.get(gid);
			if (template == null)
				throw new CustomException(NO_TEMPLATE);
			
			for (TemplateDetail det : template.getDetailList())
				templateDetailService.delete(det);
			
			template.setUpdateTime(new Date());
			template.setName(templeteName);
			template.setSlogan(templeteNote);
			templateService.update(template);
			
			if (StringUtils.isNotBlank(eventList)){
				Date now = new Date();
				JSONArray json = new JSONArray().fromObject(eventList);
				Iterator iter = json.iterator();
				while (iter.hasNext()) {
					JSONObject obj = new JSONObject().fromObject(iter.next());
					
					TemplateDetail detail = new TemplateDetail();
					detail.setCreateTime(now);
					detail.setUpdateTime(now);
					detail.setIsdel("0");
					detail.setPid(gid);
					detail.setContent(obj.get("note")+"");
					detail.setOrdering(obj.get("day")+"");
					templateDetailService.save(detail);
				}
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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
	/**
	 * 查房助手-删除提醒模版
	 * @param userid
	 * @param token
	 * @param gid
	 */
	public void deleteTemplate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Template template = templateService.get(gid);
			if (template == null)
				throw new CustomException(NO_TEMPLATE);
			
			template.setIsdel("1");
			templateService.update(template);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", new JSONObject());
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

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientBed() {
		return patientBed;
	}

	public void setPatientBed(String patientBed) {
		this.patientBed = patientBed;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(String remindDate) {
		this.remindDate = remindDate;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getTempleteId() {
		return templeteId;
	}

	public void setTempleteId(String templeteId) {
		this.templeteId = templeteId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getTempleteName() {
		return templeteName;
	}

	public void setTempleteName(String templeteName) {
		this.templeteName = templeteName;
	}

	public String getTempleteNote() {
		return templeteNote;
	}

	public void setTempleteNote(String templeteNote) {
		this.templeteNote = templeteNote;
	}

	public String getEventList() {
		return eventList;
	}

	public void setEventList(String eventList) {
		this.eventList = eventList;
	}

	public String getTmplId() {
		return tmplId;
	}

	public void setTmplId(String tmplId) {
		this.tmplId = tmplId;
	}

	public String getRemindList() {
		return remindList;
	}

	public void setRemindList(String remindList) {
		this.remindList = remindList;
	}

}

package com.sd.action;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.KfPatientService;
import com.sd.service.PatientService;
import com.sd.service.RdPatientService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.KfPatient;
import com.sd.vo.RdPatient;
import com.sd.vo.Record;
import com.sd.vo.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 病例夹接口
 * @author bxf
 *
 */
public class RdPatientAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource private RdPatientService rdPatientService;
	@Resource private PatientService patientService;
	@Resource private UserTokenService userTokenService;
	@Resource private KfPatientService kfpatientService;
	@Resource private UserService userService;
	
	private String keyword;		// 关键字
	private String gid;			// 文章gid
	private String name;		// 姓名
	private String sex;			// 性别
	private String age;			// 年龄
	private String sick;		// 疾病
	private String repaGid;		// 病人GID
	private String title;		// 标题
	private String recDate;		// 记录日期
	private String visitDate;	// 就诊日期
	private String detail;		// 病程描述
	private String lstPic;		// 图片GID,图片GID
	private String lstVid;		// 视频GID,视频GID
	private String pid;
	private String phone;
	private String relativesPhone;
	private String address;
	private String postcode;
	private String other;
	private String caseNo;
	private String bedNo;
	private String patientNo;
	private String IDCardNo;
	private String operationTime;
	private String audio;
	private String json;
	private String mark;
	private String state;
	private String head;
	private String del;
	private String type;
	private String videoPic;
	private String teamGid;
	private String operationName;
	private String sickName;
	
	//删除病程
		public void deleteCourse(){
			JSONObject ret = new JSONObject();
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				boolean logined = userTokenService.checkToken(request);
				if (!logined){
					throw new CustomException(NO_LOGIN);
				}
				patientService.delete(gid);
				
				ret.put("code", "1");
				ret.put("message", "删除成功");
				ret.put("result_data", "");
			}catch(CustomException e){
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
	//查询病程列表
	public void queryCourse(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("pgid", gid);
			List<Record> list=patientService.getConditonList(condition, "updateTime", true, null);
			JSONArray ja=new JSONArray();
			for (Record record : list) {
				JSONObject user = new JSONObject();
				user.put("gid", gid);
				user.put("courseId", record.getGid());
				user.put("detail", record.getDetail());
				user.put("date",sdf.format(record.getDate()));
				user.put("type", record.getType());
				user.put("audio", record.getAudio());
				user.put("pic", record.getPic());
				user.put("uid", record.getUid());
				if(StringUtils.isNotBlank(record.getUid())){
					User u=userService.get(record.getUid());
					user.put("userName", u.getName());
				}
				user.put("vid", record.getVid());
				user.put("videoPic", record.getVideoPic());
				ja.add(user);
			}
			
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", ja);
		}catch(CustomException e){
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
	//更改单个病程
	public void changeCourse(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Record record=patientService.get(gid);
			if(type!=null)record.setType(type);
			if(recDate!=null)record.setDate(sdf.parse(recDate));
			if(detail!=null)record.setDetail(detail);
			if(audio!=null)record.setAudio(audio);
			if(lstPic!=null)record.setPic(lstPic);
			if(lstVid!=null)record.setVid(lstVid);
			if(videoPic!=null)record.setVideoPic(videoPic);
			record.setUpdateTime(new Date());
			patientService.update(record);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data","");
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
	//更改单个病例
	public void changeRecord(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(StringUtils.isNotBlank(del)){
				rdPatientService.delete(gid);
			}else{
				RdPatient record=rdPatientService.get(gid);
				if(StringUtils.isNotBlank(pid)){
					if("0".equals(pid)){
						record.setPid("");
					}else{
						record.setPid(pid);
					}
				}
				if(address!=null)record.setAddress(address);
				if(age!=null)record.setAge(age);
				if(head!=null)record.setHead(head);
				if(bedNo!=null)record.setBedNo(bedNo);
				if(caseNo!=null)record.setCaseNo(caseNo);
				if(sex!=null)record.setGender(sex);
				if(IDCardNo!=null)record.setIDCardNo(IDCardNo);
				if(name!=null)record.setName(name);
				if(operationName!=null)record.setOperationName(operationName);
				if(StringUtils.isNotBlank(operationTime))record.setOperationTime(sdf.parse(operationTime));
				if(other!=null)record.setOther(other);
				if(patientNo!=null)record.setPatientNo(patientNo);
				if(phone!=null)record.setPhone(phone);
				if(state!=null)record.setState(state);
				if(mark!=null)record.setMark(mark);
				if(postcode!=null)record.setPostcode(postcode);
				if(relativesPhone!=null)record.setRelativesPhone(relativesPhone);
				if(sick!=null)record.setSick(sick);
				record.setUpdateTime(new Date());
				if(StringUtils.isNotBlank(visitDate))record.setVisitDate(sdf.parse(visitDate));
				rdPatientService.update(record);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data","");
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
	//请求单个病例
	public void queryRecord(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("gid", gid);
			List<RdPatient> list=rdPatientService.getConditonList(condition, "updateTime", true, null);
			JSONObject joIn=new JSONObject();
			for (RdPatient rdPatient : list) {
				joIn.put("gid", rdPatient.getGid());
				joIn.put("pid", rdPatient.getPid());
				if(StringUtils.isNotBlank(rdPatient.getPid())){
					KfPatient p = kfpatientService.get(rdPatient.getPid());
					joIn.put("pCode",p.getId());
					joIn.put("head",p.getAvatars());
				}else{
					joIn.put("pCode","");
					joIn.put("head",rdPatient.getHead());
				}
				joIn.put("address", rdPatient.getAddress());
				joIn.put("age",rdPatient.getAge());
				joIn.put("uid",rdPatient.getUserGid());
				joIn.put("userName",rdPatient.getAge());
				joIn.put("bedNo",rdPatient.getBedNo());
				joIn.put("caseNo",rdPatient.getCaseNo());
				joIn.put("sex",rdPatient.getGender());
				joIn.put("IDCardNo",rdPatient.getIDCardNo());
				joIn.put("name",rdPatient.getName());
				if(rdPatient.getOperationTime()!=null){
					joIn.put("operationTime",sdf2.format(rdPatient.getOperationTime()));
				}else{
					joIn.put("operationTime","");
				}
				joIn.put("other",rdPatient.getOther());
				joIn.put("patientNo",rdPatient.getPatientNo());
				joIn.put("phone",rdPatient.getPhone());
				joIn.put("state",rdPatient.getState());
				joIn.put("operationName",rdPatient.getOperationName());
				joIn.put("sickName",rdPatient.getSickName());
				joIn.put("mark",rdPatient.getMark());
				joIn.put("postcode",rdPatient.getPostcode());
				joIn.put("relativesPhone",rdPatient.getRelativesPhone());
				joIn.put("sick",rdPatient.getSick());
				joIn.put("updateTime",sdf2.format(rdPatient.getUpdateTime()));
				joIn.put("userId",rdPatient.getUserGid());
				if(rdPatient.getVisitDate()!=null){
					joIn.put("visitDate",sdf2.format(rdPatient.getVisitDate()));
				}else{
					joIn.put("visitDate","");
				}
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data",joIn);
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
	//改版查询病例列表
		public void queryRecords(){
			JSONObject ret = new JSONObject();
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				String userId = request.getHeader("userid");
				boolean logined = userTokenService.checkToken(request);
				if (!logined)
					throw new CustomException(NO_LOGIN);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("userGid", userId);
				condition.put("isdel", "1");
				List<RdPatient> list=rdPatientService.getConditonList(condition, "updateTime", true, null);
				String date="";
				JSONArray ja=new JSONArray();
				JSONObject jo=new JSONObject();
				JSONArray jaIn=new JSONArray();
				JSONObject joIn=new JSONObject();
				for (RdPatient rdPatient : list) {
					if(StringUtils.isNotBlank(rdPatient.getTeamGid())){
						continue;
					}
					if("1".equals(type)){
						if(StringUtils.isBlank(rdPatient.getPid())){
							continue;
						}
					}else if("0".equals(type)){
						if(StringUtils.isNotBlank(rdPatient.getPid())){
							continue;
						}
					}
					if(!date.equals(sdf.format(rdPatient.getUpdateTime()))){
						if(!date.equals("")){
							jo.put("value", jaIn);
							ja.add(jo);
						}
						date=sdf.format(rdPatient.getUpdateTime());
						jo.clear();
						jaIn.clear();
						jo.put("time", sdf.format(rdPatient.getUpdateTime()));
					}
					joIn.clear();
					joIn.put("gid", rdPatient.getGid());
					joIn.put("pid", rdPatient.getPid());
					if(StringUtils.isNotBlank(rdPatient.getPid())){
						KfPatient p = kfpatientService.get(rdPatient.getPid());
						joIn.put("pCode",p.getId());
						joIn.put("head",p.getAvatars());
					}else{
						joIn.put("pCode","");
						joIn.put("head",rdPatient.getHead());
					}
					joIn.put("address", rdPatient.getAddress());
					joIn.put("age",rdPatient.getAge());
					
					joIn.put("bedNo",rdPatient.getBedNo());
					joIn.put("caseNo",rdPatient.getCaseNo());
					joIn.put("sex",rdPatient.getGender());
					joIn.put("IDCardNo",rdPatient.getIDCardNo());
					joIn.put("name",rdPatient.getName());
					if(rdPatient.getOperationTime()!=null){
						joIn.put("operationTime",sdf2.format(rdPatient.getOperationTime()));
					}else{
						joIn.put("operationTime","");
					}
					joIn.put("other",rdPatient.getOther());
					joIn.put("patientNo",rdPatient.getPatientNo());
					joIn.put("phone",rdPatient.getPhone());
					joIn.put("state",rdPatient.getState());
					joIn.put("mark",rdPatient.getMark());
					joIn.put("postcode",rdPatient.getPostcode());
					joIn.put("relativesPhone",rdPatient.getRelativesPhone());
					joIn.put("sick",rdPatient.getSick());
					joIn.put("updateTime",sdf2.format(rdPatient.getUpdateTime()));
					joIn.put("userId",rdPatient.getUserGid());
					if(rdPatient.getVisitDate()!=null){
						joIn.put("visitDate",sdf2.format(rdPatient.getVisitDate()));
					}else{
						joIn.put("visitDate","");
					}
					jaIn.add(joIn);
				}
				jo.put("value", jaIn);
				ja.add(jo);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				if("".equals(date)){
					ret.put("result_data",new JSONArray());
				}else{
					ret.put("result_data",ja);
				}
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
	//改版增加病程
	public void addCourse(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			String userId = request.getHeader("userid");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			json=json.replace("\n", "\\n");
			JSONArray ja=JSONArray.fromObject(json);
			String res="";
			for (Object object : ja) {
				JSONObject jo=JSONObject.fromObject(object);
				Record record=new Record();
				res=UUID.randomUUID().toString();
				record.setGid(res);
				record.setPgid(jo.getString("gid"));
				record.setType(jo.getString("type"));
				record.setDate(sdf.parse(jo.getString("recDate")));
				record.setDetail(jo.getString("detail"));
				record.setAudio(jo.getString("audio"));
				record.setPic(jo.getString("lstPic"));
				record.setVid(jo.getString("lstVid"));
				record.setVideoPic(jo.getString("videoPic"));
				record.setUid(userId);
				record.setCreateTime(new Date());
				patientService.save(record);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data",res);
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
	//改版增加病例
	public void addRecord(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			int i=0;
			if(StringUtils.isNotBlank(teamGid)){
				Map<String, String>condition=new HashMap<String, String>();
				condition.put("teamGid", teamGid);
				condition.put("pid", pid);
				List<RdPatient>list=rdPatientService.getConditonList(condition, "", false, null);
				if(list.size()!=0){
					i=1;
				}
			}
			RdPatient record=new RdPatient();
			if(i==0){
				record.setGid(ComUtil.getuuid());
				record.setPid(pid);
				record.setTeamGid(teamGid);
				record.setAddress(address);
				if(StringUtils.isNotBlank(age))record.setAge(age);
				record.setHead(head);
				record.setBedNo(bedNo);
				record.setCaseNo(caseNo);
				record.setOperationName(operationName);
				record.setSickName(sickName);
				record.setCreateTime(new Date());
				record.setGender(sex);
				record.setIDCardNo(IDCardNo);
				record.setIsdel("1");
				record.setName(name);
				if(StringUtils.isNotBlank(operationTime))record.setOperationTime(sdf.parse(operationTime));
				record.setOther(other);
				record.setPatientNo(patientNo);
				record.setPhone(phone);
				record.setState(state);
				record.setMark(mark);
				record.setPostcode(postcode);
				record.setRelativesPhone(relativesPhone);
				record.setSick(sick);
				record.setUpdateTime(new Date());
				record.setUserGid(userId);
				if(StringUtils.isNotBlank(visitDate))record.setVisitDate(sdf.parse(visitDate));
				rdPatientService.save(record);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data",record.getGid());
		} 
		catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} 
		catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 病例夹查询
	 * @param userId
	 * @param token
	 * @param keyword
	 */
	public void rdSelect(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			List<RdPatient> patientList = rdPatientService.getmyPatients(userId);
			JSONArray data = new JSONArray();
			for (RdPatient patient : patientList){
				JSONObject art = new JSONObject();
				art.put("gid", patient.getGid());
				art.put("name", patient.getName());
				art.put("age", patient.getAge());
				art.put("sex", patient.getGender());
				art.put("sick", patient.getSick());
				art.put("visit", ComUtil.dateTime2Str(patient.getVisitDate()));
				data.add(art);
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	 * 病历夹详细信息
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void rdSelectByPGid(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			RdPatient patient = rdPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_PATIENT);
			
			JSONObject data = new JSONObject();
			data.put("gid", patient.getGid());
			data.put("name", patient.getName());
			data.put("age", patient.getAge());
			data.put("sex", patient.getGender());
			data.put("sick", patient.getSick());
			data.put("visit", ComUtil.dateTime2Str(patient.getVisitDate()));
			
			JSONArray lstrecords = new JSONArray();
			for (Record record : patient.getRecordSet()){
				JSONObject lstrecord = new JSONObject();
				lstrecord.put("gid", record.getGid());
				lstrecord.put("title", record.getTitle());
				lstrecord.put("detail", record.getDetail());
				lstrecord.put("date", ComUtil.dateTime2Str(record.getDate()));
				lstrecord.put("pic", record.getPic());
				lstrecord.put("vid", record.getVid());
				lstrecords.add(lstrecord);
			}
			data.put("lstRecord", lstrecords);

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	 * 新增病例夹
	 * @param userId
	 * @param token
	 * @param name
	 * @param sex
	 * @param age
	 * @param sick
	 * @param visitDate
	 */
	public void rdAdd(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			Integer.parseInt(age);// 验证年龄是否数字

			Date now = new Date();
			RdPatient patient = new RdPatient();
			patient.setName(name);
			patient.setAge(age);
			patient.setGender(sex);
			patient.setGid(ComUtil.getuuid());
			patient.setIsdel("1");
			patient.setSick(sick);
			patient.setCreateTime(now);
			patient.setUpdateTime(now);
			patient.setUserGid(userId);
			patient.setVisitDate(ComUtil.str2Date(visitDate));
			rdPatientService.save(patient);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");
		} catch (NumberFormatException e){
			ret.put("code", "0");
			ret.put("message", "请填写正确的年龄!");
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
	/**
	 * 更新病例夹
	 * @param userId
	 * @param token
	 * @param gid
	 * @param name
	 * @param sex
	 * @param age
	 * @param sick
	 * @param visitDate
	 */
	public void rdUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			RdPatient patient = rdPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_PATIENT);
			
			patient.setName(name);
			patient.setAge(age);
			patient.setGender(sex);
			patient.setSick(sick);
			patient.setVisitDate(ComUtil.str2Date(visitDate));
			patient.setUpdateTime(new Date());
			rdPatientService.update(patient);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	/**
	 * 删除病例夹
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void rdDelete(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			RdPatient patient = rdPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_PATIENT);
			
			patient.setIsdel("0");
			rdPatientService.update(patient);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	/**
	 * 病程查询（单条）
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void rdSelectSingle(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			Record record=patientService.get(gid);
			JSONObject user = new JSONObject();
			user.put("gid", gid);
			user.put("title",record.getTitle());
			user.put("detail", record.getDetail());
			user.put("date", ComUtil.dateTime2Str( record.getDate()));
			user.put("type", record.getType());
			user.put("audio", record.getAudio());
			user.put("pic", record.getPic());
			user.put("vid", record.getVid());
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", user);
		}catch(CustomException e){
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
	 * 病程新增
	 * @param userId
	 * @param token
	 * @param gid
	 * @param title
	 * @param recDate
	 * @param detail
	 * @param lstPic
	 * @param lstVid
	 */
	public void rdRecordAdd(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			Record record=new Record();
			record.setGid(UUID.randomUUID().toString());
			record.setPgid(gid);
			record.setTitle(title);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			record.setDate(sdf.parse(recDate));
			record.setDetail(detail);
			record.setPic(lstPic);
			record.setVid(lstVid);
			record.setCreateTime(new Date());
			patientService.save(record);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	/**
	 * 病程更新
	 * @param userId
	 * @param token
	 * @param gid
	 * @param title
	 * @param detail
	 * @param recDate
	 * @param lstPic
	 * @param lstVid
	 */
	public void rdRecordUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			Record record=patientService.get(gid);
			if(record==null)throw new CustomException(NO_RESULT);
			record.setTitle(title);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			record.setDate(sdf.parse(recDate));
			record.setDetail(detail);
			record.setPic(lstPic);
			record.setVid(lstVid);
			record.setUpdateTime(new Date());
			patientService.update(record);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	/**
	 * 病程删除
	 * @param userId
	 * @param token
	 * @param gid
	 */
	public void rdRecordDelete(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined){
				throw new CustomException(NO_LOGIN);
			}
			patientService.delete(gid);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSick() {
		return sick;
	}
	public void setSick(String sick) {
		this.sick = sick;
	}
	public String getRepaGid() {
		return repaGid;
	}
	public void setRepaGid(String repaGid) {
		this.repaGid = repaGid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
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
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRelativesPhone() {
		return relativesPhone;
	}
	public void setRelativesPhone(String relativesPhone) {
		this.relativesPhone = relativesPhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getIDCardNo() {
		return IDCardNo;
	}
	public void setIDCardNo(String iDCardNo) {
		IDCardNo = iDCardNo;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDel() {
		return del;
	}
	public void setDel(String del) {
		this.del = del;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVideoPic() {
		return videoPic;
	}
	public void setVideoPic(String videoPic) {
		this.videoPic = videoPic;
	}
	public String getTeamGid() {
		return teamGid;
	}
	public void setTeamGid(String teamGid) {
		this.teamGid = teamGid;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getSickName() {
		return sickName;
	}
	public void setSickName(String sickName) {
		this.sickName = sickName;
	}
	
}

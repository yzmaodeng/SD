package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.ActivityDataService;
import com.sd.service.KfDataService;
import com.sd.service.KfPatientService;
import com.sd.service.KfPatientSickService;
import com.sd.service.MotionService;
import com.sd.service.MotionTmplService;
import com.sd.service.PatientApplyService;
import com.sd.service.PatientDoctorService;
import com.sd.service.PatientService;
import com.sd.service.RdPatientService;
import com.sd.service.SchemeService;
import com.sd.service.SchemeTmplService;
import com.sd.service.ScoreService;
import com.sd.service.SickService;
import com.sd.service.StandardMotionService;
import com.sd.service.TargetService;
import com.sd.service.TargetTmplService;
import com.sd.service.TeamKfpatientService;
import com.sd.service.TeamService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.Conditions;
import com.sd.util.CustomException;
import com.sd.util.scoreUtil;
import com.sd.vo.ActivityData;
import com.sd.vo.KfData;
import com.sd.vo.KfPatient;
import com.sd.vo.KfPatientApply;
import com.sd.vo.KfPatientDoctor;
import com.sd.vo.KfPatientSick;
import com.sd.vo.Motion;
import com.sd.vo.MotionTmpl;
import com.sd.vo.Scheme;
import com.sd.vo.SchemeTmpl;
import com.sd.vo.Sick;
import com.sd.vo.StandardMotion;
import com.sd.vo.Target;
import com.sd.vo.TargetTmpl;
import com.sd.vo.Team;
import com.sd.vo.TeamKfpatient;
import com.sd.vo.User;
/**
 * 康复接口
 * @author bxf
 *
 */
public class RecoveredAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource private ScoreService scoreService;
	@Resource private TeamService teamService;
	@Resource private UserService userService;
	@Resource private UserTokenService userTokenService;
	@Resource private KfPatientService kfPatientService;
	@Resource private SickService sickService;
	@Resource private SchemeService schemeService;
	@Resource private SchemeTmplService schemeTmplService;
	@Resource private MotionService motionService;
	@Resource private MotionTmplService motionTmplService;
	@Resource private TargetService targetService;
	@Resource private TargetTmplService targetTmplService;
	@Resource private KfDataService kfDataService;
	@Resource private PatientService patientService;
	@Resource private PatientApplyService patientApplyService;
	@Resource private PatientDoctorService patientDoctorService;
	@Resource private StandardMotionService stMotionService;
	@Resource private KfPatientSickService kfPatientSickService;
	@Resource private TeamKfpatientService teamKfpatientService;
	@Resource private ActivityDataService activityDataService;
	@Resource private RdPatientService rdPatientService;
	
	private String teamGid;
	private String type;
	private String parentGid;
	private String patientGid;
	private String gid;
	private String sick;
	private String planGid;
	private String operDate;
	private String operRDate;
	private String operLDate;
	private String name;
	private String slogan;
	private String oGid;
	private String beginDay;
	private String endDay;
	private String tartype;
	private String targid;
	private String gCount;
	private String eCount;
	private String pGid;
	private String lstOper;
	private String id;
	private String leg;
	private String mid;
	private String date;
	private String target;
	private String operationGid;
	
	/**
	 * 更新患者信息
	 * @param userId		用户GID
	 * @param token			验证码
	 * @param gid			患者GID
	 * @param sick 			疾病GID
	 * @param operRDate		左腿手术时间（yyyy-mm-dd）
	 * @param operLDate		右腿手术时间（yyyy-mm-dd）
	 */
	public void patientInfoUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			User user=userService.get(userId);
			if(user.getTeamId()==null||user.getTeamId()==""){
				throw new CustomException("医生无团队");
			}
			updatePatientInfo(userId, gid, sick, operRDate, operLDate);
			
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
	 * 更新患者信息,非接口
	 */
	private String updatePatientInfo(String userId, String gid, String sick, String operRDate, String operLDate) {
		User user = userService.get(userId);
		int flag = userService.checkRelationship(gid, user); // 团患关系or医患关系
		Date now = new Date();
		if (flag == 1){
			// 团患
			KfPatientSick pSick = kfPatientSickService.get(gid, user.getTeamId(), flag);
			if (pSick == null){
				KfPatientSick newSick = new KfPatientSick();
				newSick.setPid(gid);
				newSick.setSid(sick);
				newSick.setOid(user.getTeamId());
				newSick.setType(flag + "");
				newSick.setOperRDate(ComUtil.str2Date(operRDate));
				newSick.setOperLDate(ComUtil.str2Date(operLDate));
				newSick.setCreatetime(now);
				newSick.setUpdatetime(now);
				newSick.setIsdel("0");
				return kfPatientSickService.save(newSick);
			} else {
				if (StringUtils.isNotBlank(sick))
					pSick.setSid(sick);
				if (StringUtils.isNotBlank(operRDate))
					pSick.setOperRDate(ComUtil.str2Date(operRDate));
				if (StringUtils.isNotBlank(operLDate))
					pSick.setOperLDate(ComUtil.str2Date(operLDate));
				
			    kfPatientSickService.update(pSick);
			    return pSick.getId();
			}
		} else {
			//没有团患关系就给他添加团患关系
			TeamKfpatient tp=new TeamKfpatient();
			tp.setStgid(user.getTeamId());
			tp.setPgid(gid);
			tp.setCreatetime(new Date());
			tp.setUpdateTime(new Date());
			tp.setIsdel("0");
			teamKfpatientService.save(tp);
			// 医患
			KfPatientSick pSick = kfPatientSickService.get(gid, userId, flag);
			if (pSick == null){
				KfPatientSick newSick = new KfPatientSick();
				newSick.setPid(gid);
				newSick.setSid(sick);
				newSick.setOid(user.getTeamId());
				newSick.setType("1");
				newSick.setOperRDate(ComUtil.str2Date(operRDate));
				newSick.setOperLDate(ComUtil.str2Date(operLDate));
				newSick.setCreatetime(now);
				newSick.setUpdatetime(now);
				newSick.setIsdel("0");
				return kfPatientSickService.save(newSick);
			} else {
				if (StringUtils.isNotBlank(sick))
					pSick.setSid(sick);
				if (StringUtils.isNotBlank(operRDate))
					pSick.setOperRDate(ComUtil.str2Date(operRDate));
				if (StringUtils.isNotBlank(operLDate))
					pSick.setOperLDate(ComUtil.str2Date(operLDate));
				//更新疾病表里面的医生ID变成团队ID
				pSick.setType("1");
				pSick.setOid(user.getTeamId());
			    kfPatientSickService.update(pSick);
			    return pSick.getId();
			}
		}
	}
//	userId	是	字符串	用户GID	
//	token	是	字符串	验证码	
//	gid	是	字符串	患者GID	
//	type	是	字符串	操作类型（1-同意/0-忽略）
	public void dealApply(){//是同意患者的申请同意就创建KfPatientDoctor                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			Map<String, String> condition=new HashMap<String, String>();
			condition.put("uid", userId);
			condition.put("pid", gid);
			List<KfPatientApply> patientList = patientApplyService.getConditonList(condition, null, false, null);
			if(patientList.isEmpty())throw new CustomException("患者申请丢失");
			if("1".equals(type)){
				patientApplyService.delete(patientList.get(0).getGid());
				KfPatientDoctor patientdoctor=new KfPatientDoctor();
				patientdoctor.setGid(UUID.randomUUID().toString());
				patientdoctor.setPid(gid);
				patientdoctor.setUid(userId);
				patientdoctor.setCreatetime(new Date());
				patientdoctor.setIsdel("1");
				patientDoctorService.save(patientdoctor);
			}else{
				KfPatientApply pa=patientApplyService.get(patientList.get(0).getGid());
				pa.setIsdel("0");
				patientApplyService.update(pa);
			}
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
	 * 患者列表（团队gid->所有的医生->所有医生的所有方案->所有的pid-》所有的患者）
	 * @param userId		用户GID
	 * @param token
	 * @param teamGid 		团队GID
	 */
	public void patientList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
//			if (!logined)
//				throw new CustomException(NO_LOGIN);
			String userId = request.getHeader("userid");
			User u=userService.get(userId);
			if (StringUtils.isBlank(u.getTeamId()))
				throw new CustomException(NO_ID);
			
			Team team = teamService.get(u.getTeamId());
			if (team == null)	// 不允许无团队，则后续默认走团患关系
				throw new CustomException(NO_TEAM);
			teamGid=u.getTeamId();
			JSONArray data = new JSONArray();
			Date now = new Date();
			Map<String, String>ucondition=new HashMap<String, String>();
			ucondition.put("teamId", teamGid);
			List<User>ulist=userService.getConditonList(ucondition, "", false, null);
			List<KfPatient> plist=new ArrayList<KfPatient>();
			Set<String>set=new HashSet<String>();
			for (User user : ulist) {
				Map<String, String>scondition=new HashMap<String, String>();
				scondition.put("createUser", user.getGid());
				List<Scheme>slist=schemeService.getConditonList(scondition, "", false, null);
				
				for (Scheme scheme : slist) {
					set.add(scheme.getKfPatientId());
				}
			}
			for (String pid : set) {
				KfPatient p=kfPatientService.get(pid);
				plist.add(p);
			}
			
			for (KfPatient patient : plist){
				//添加最近一周无康复数据筛选
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<Conditions> conditions=new ArrayList<Conditions>();
				Conditions c1=new Conditions();
				Map<String, String> condition=new HashMap<String, String>();
				condition.put("pid", patient.getGid());
				c1.setType("map");
				c1.setMap(condition);
				conditions.add(c1);
				Conditions c2=new Conditions();
				c2.setType("timeA");
				c2.setKey("createTime");
				c2.setValue(sdf.format(new Date(new Date().getTime()-7*24*3600*1000)));
				conditions.add(c2);
				List<KfData> userData =kfDataService.getConditonsList(conditions, "", false, null);
				if(userData.size()==0)continue;
				JSONObject kf = new JSONObject();
				kf.put("gid", patient.getGid());
				kf.put("code", patient.getId());
				kf.put("name", patient.getName());
				kf.put("age", patient.getAge());
				kf.put("gender", patient.getGender());
				kf.put("avatars", patient.getAvatars());

				KfPatientSick pSick = kfPatientSickService.get(patient.getGid(), teamGid, 1);//1团队
				if(pSick!=null){
					kf.put("sick", sickService.getFullSickName(pSick.getSid()));
					Date operDate1 = pSick.getOperLDate();
					Date operDate2 = pSick.getOperRDate();
					if (operDate1!= null&&operDate2!= null){
						String days1 = ComUtil.daysBetweenDate(operDate1, now)+"";
						String days2 = ComUtil.daysBetweenDate(operDate2, now)+"";
						if(operDate1.getTime()<operDate2.getTime()){
							kf.put("operDist", days1);
						}else{
							kf.put("operDist", days2);
						}
						
						Scheme scheml1 = schemeService.getByPatientTeam(patient.getGid(), teamGid, "1");
						Scheme scheml2 = schemeService.getByPatientTeam(patient.getGid(), teamGid, "2");
						Map<String,String>map1=this.getScore(scheml1,Integer.parseInt(days1));
						Map<String,String>map2=this.getScore(scheml2,Integer.parseInt(days2));
						kf.put("score", ((Integer.parseInt(map1.get("score"))+Integer.parseInt(map2.get("score")))/2)+"");
						kf.put("angleL", map1.get("angle"));
						kf.put("angleR", map2.get("angle"));
					}else if (operDate1!= null){
						String days1 = ComUtil.daysBetweenDate(operDate1, now)+"";
						kf.put("operDist", days1);
						Scheme scheml = schemeService.getByPatientTeam(patient.getGid(), teamGid, "1");
						Map<String,String>map1=this.getScore(scheml,Integer.parseInt(days1));
						kf.put("score", map1.get("score"));
						kf.put("angleL", map1.get("angle"));
						kf.put("angleR","");
					}else if(operDate2!= null){
						String days2 = ComUtil.daysBetweenDate(operDate2, now)+"";
						kf.put("operDist", days2);
						Scheme scheml = schemeService.getByPatientTeam(patient.getGid(), teamGid, "2");
						Map<String,String>map2=this.getScore(scheml,Integer.parseInt(days2));
						kf.put("score", map2.get("score"));
						kf.put("angleL","");
						kf.put("angleR", map2.get("angle"));
					}
				}else{
					kf.put("sick", "");
					kf.put("operDist", "");
					kf.put("score", "");
					kf.put("angleL","");
					kf.put("angleR", "");
				}
				kf.put("oper", "");
				data.add(kf);
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
	
	public Map<String, String> getScore(Scheme scheml,int days){
		int score = 0, maxAngle = 0, maxSocre = 0, scoreTarget = 0,sumScore=0,resScore=0,motionSize=0;
		if(scheml != null){
			for (Motion myMotion : scheml.getMotionSet()) {
				score=0;
				if("1".equals(myMotion.getStMotion().getDocType())||"2".equals(myMotion.getStMotion().getDocType())){
					motionSize++;
				}
				scoreTarget=0;
				for (Target target : myMotion.getTargetSet()) {
					if(Integer.parseInt(target.getBdays())<=days||days<=Integer.parseInt(target.getEdays())){
						scoreTarget+=Integer.parseInt(target.getGroup())*Integer.parseInt(target.getNum());
						break;
					}
				}
				Iterator<KfData> it = myMotion.getKfDataSet().iterator();
				int scoreAct=0,count=0;
				String type="";
				while(it.hasNext()){
					KfData kfData=it.next();
					type=kfData.getType();
					if("1".equals(kfData.getType())){
						if(kfData.getDate().equals(ComUtil.date2Str(new Date()))){
							if(Integer.parseInt(kfData.getNum())>maxAngle){
								maxAngle=Integer.parseInt(kfData.getNum());
							} 
							if(Integer.parseInt(kfData.getScore())>maxSocre){
								maxSocre=Integer.parseInt(kfData.getScore());
							}
						}
					}else if("2".equals(kfData.getType())||"3".equals(kfData.getType())){
						if(kfData.getDate().equals(ComUtil.date2Str(new Date()))){
							scoreAct+=Integer.parseInt(kfData.getScore());
							count++;
						}
					}
				}
				if(scoreTarget==0)continue;
				if("1".equals(type)){
					score=maxSocre;
				}else if("2".equals(type)){
					if(scoreTarget>count){
						score=scoreAct/scoreTarget;
					}else{
						if(count==0)continue;
						score=scoreAct/count;
					}
				}else if("3".equals(type)){
					score=scoreAct*100/(scoreTarget*60);
				}
				sumScore+=score;
			}
			if(!(motionSize==0)){
				resScore=sumScore/motionSize;
			}
		}
		Map<String, String>map=new HashMap<String, String>();
		if(resScore>100)
			resScore=100;
		map.put("score",resScore+"");
		map.put("angle",maxAngle+"" );
		return map;
	}
	/**
	 * 通讯录列表
	 * @param userId		用户GID
	 * @param token
	 * @param teamGid		类型为团队时，必填
	 * @param type			类型：默认团队，0个人
	 */
	public void contactsList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			List<KfPatient> patientList = null;
			if ("0".equals(type)){
				patientList = kfPatientService.getPensonalPatients(userId);
			} else {
				if (StringUtils.isBlank(teamGid))
					throw new CustomException(NO_ID);
				
				patientList = kfPatientService.getGroupPatients(teamGid);
			}
			JSONArray data = new JSONArray();
			for (KfPatient patient : patientList){
				JSONObject pat = new JSONObject();
				pat.put("gid", patient.getGid());
				pat.put("avatars", patient.getAvatars());
				pat.put("name", patient.getName());
				pat.put("code", patient.getId());
				data.add(pat);
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
	 * 新患者列表
	 * @param userId		用户GID
	 * @param token
	 */
	public void requestUserList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			List<Object[]> patientList = kfPatientService.getNewApplyPatients(userId);
			
			JSONArray data = new JSONArray();
			for (Object[] patient : patientList){
				JSONObject pat = new JSONObject();
				pat.put("gid", patient[0]);
				pat.put("avatars", patient[1]);
				pat.put("name", patient[2]);
				String emojiRecovery = emojiRecovery((String)patient[3]);
				pat.put("msg", emojiRecovery);
				data.add(pat);
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
	 * 疾病列表
	 * @param parentGid		父级ID,查询第一级时为空
	 */
	public void sickList(){
		JSONObject ret = new JSONObject();
		try {
			Map<String, String> condition = new HashMap<String, String>();
			if (StringUtils.isBlank(parentGid))
				condition.put("pid", "0");
			else
				condition.put("pid", parentGid);
			condition.put("isdel", "0");
			
			List<Sick> sickList = sickService.getConditonList(condition, "", false, null);
			
			JSONArray data = new JSONArray();
			for (Sick sick : sickList){
				JSONObject pat = new JSONObject();
				pat.put("gid", sick.getGid());
				pat.put("name", sick.getName());
				pat.put("parentGid", sick.getPid());
				data.add(pat);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
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
	 * 康复方案列表
	 * @param userId 用户GID
	 * @param token
	 * @param teamGid 团队GID
	 * @param patientGid 患者GID
	 */
	public void planList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(teamGid))
				throw new CustomException(NO_ID);
			
			JSONObject lists = new JSONObject();
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("isdel", "0");
			condition.put("createTeam", teamGid);
			List<SchemeTmpl> schemeList = schemeTmplService.getConditonList(condition, "", false, null);
			
			JSONArray listA = new JSONArray();
			for (SchemeTmpl schemeTmpl : schemeList) {
				JSONObject pat = new JSONObject();
				pat.put("gid", schemeTmpl.getGid());
				pat.put("name", schemeTmpl.getName());
				pat.put("slogan", schemeTmpl.getNote());
				pat.put("teamId", schemeTmpl.getCreateTeam());
				pat.put("isChoosen", "0");
				pat.put("pic", ComUtil.randomPic(schemeTmpl.getGid()));
				
				if (StringUtils.isNotBlank(patientGid)) {
					Map<String, String> condition2 = new HashMap<String, String>();
					condition2.put("isdel", "0");
					condition2.put("referTmpl", schemeTmpl.getGid());
					condition2.put("kfPatientId", patientGid);
					
					String sickList = schemeService.getConditonCount(condition2);
					if (!"0".equals(sickList))
						pat.put("isChoosen", "1");
				}
				listA.add(pat);
			}
			lists.put("teamList", listA);
			
			condition.put("createTeam", "default_team");
			schemeList = schemeTmplService.getConditonList(condition, "", false, null);
			
			JSONArray listB = new JSONArray();
			for (SchemeTmpl schemeTmpl : schemeList) {
				JSONObject pat = new JSONObject();
				pat.put("gid", schemeTmpl.getGid());
				pat.put("name", schemeTmpl.getName());
				pat.put("slogan", schemeTmpl.getNote());
				pat.put("teamId", schemeTmpl.getCreateTeam());
				pat.put("isChoosen", "0");
				pat.put("pic", ComUtil.randomPic(schemeTmpl.getGid()));
				
				if (StringUtils.isNotBlank(patientGid)) {
					Map<String, String> condition2 = new HashMap<String, String>();
					condition2.put("isdel", "0");
					condition2.put("referTmpl", schemeTmpl.getGid());
					condition2.put("kfPatientId", patientGid);
					
					String sickList = schemeService.getConditonCount(condition2);
					if (!"0".equals(sickList))
						pat.put("isChoosen", "1");
				}
				listB.add(pat);
			}
			lists.put("defaultList", listB);
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", lists);
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
	 * 患者详细信息
	 * @param userId		用户GID
	 * @param token
	 * @param gid			患者gid
	 */
	public void patientDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			KfPatient patient = kfPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			JSONObject data = new JSONObject();
			data.put("code", patient.getId());
			data.put("gid", patient.getGid());
			data.put("name", patient.getName());
			data.put("age", patient.getAge());
			data.put("local", patient.getLocal());
			data.put("gender", patient.getGender());
			data.put("avatars", patient.getAvatars());
			Map<String, String> myMap=new HashMap<String, String>();
			myMap.put("pid",gid);
			myMap.put("uid",userId);
			List<KfPatientApply> myList=patientApplyService.getConditonList(myMap, null, false, null);
			if(myList.size()==0){
				data.put("record", "");
			}else{
				
				if(myList.get(0).getInfo()==null){
					data.put("record", "");
				}else{
					
					data.put("record", emojiRecovery(myList.get(0).getInfo()));
				}
			}
			User user = userService.get(userId);
			int flag = userService.checkRelationship(gid, user); // 团患关系or医患关系
			KfPatientSick pSick = null;
			JSONArray schemes = new JSONArray();
			if (flag == 1)
				pSick = kfPatientSickService.get(patient.getGid(), user.getTeamId(), 1);
			else
				pSick = kfPatientSickService.get(patient.getGid(), userId, 2);
			
			if (pSick != null){
				String sickName = sickService.getFullSickName(pSick.getSid());
				data.put("sickId", pSick.getSid());
				data.put("sick", sickName);
			}
				
			
			for (int i = 1; i <= 2; i++) {// 1-左，2-右
				JSONObject schemeJson = new JSONObject();
				Date OperDate = null;
				if (pSick != null) {
					OperDate = i == 1 ? pSick.getOperLDate() : pSick.getOperRDate();
					schemeJson.put("operDist", ComUtil.daysBetweenDate(OperDate, new Date()));
					schemeJson.put("operDate", ComUtil.date2Str(OperDate));
				}
				Scheme scheme = schemeService.getByPatientUser(patient.getGid(), userId, i+"");
				if (scheme != null){
					schemeJson.put("schemeId", scheme.getGid());
					schemeJson.put("schemeName", scheme.getName());
					schemeJson.put("isMain", scheme.getIsMain());
					JSONArray kfArray = new JSONArray();
					String days = ComUtil.daysBetweenDate(OperDate, new Date()) + "";
					List<String> tarDataList = targetService.getDataByType(scheme.getKfPatientId(), days);
					for (String obj : tarDataList)
						kfArray.add(obj);
					data.put("lstToday", kfArray);
				}
				schemeJson.put("leg", i);
				
				schemes.add(schemeJson);
			}
			data.put("schemes", schemes);
			
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
	 * 患者信息更新（患者创建康复方案的实际接口）
	 * @param userId		用户GID
	 * @param token
	 * @param gid			患者gid
	 * @param planGid		康复方案gid
	 * @param operDate		右腿手术时间
	 * @param leg			1-左，2-右
	 */
	//医生必须有团队才能创建方案
	public void patientUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			KfPatient patient = kfPatientService.get(gid);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			User user=userService.get(userId);
			if(user.getTeamId()==null||user.getTeamId()==""){
				throw new CustomException("医生无团队");
			}
			String psid = null;
			if ("1".equals(leg))
				psid = updatePatientInfo(userId, gid, null, null, operDate);
			else
				psid = updatePatientInfo(userId, gid, null, operDate, null);
			//如果患者存在康复方案模板
			if (StringUtils.isNotBlank(planGid)){
				SchemeTmpl newScheme = schemeTmplService.get(planGid);
				//如果存在康复的模板
				if (newScheme != null){
					Scheme oldScheme = schemeService.getByPatientUser(gid, userId, leg);
					Date now = new Date();
					if(oldScheme != null){// 存在原有方案，覆盖更新
						oldScheme.setUpdateTime(now);
						oldScheme.setName(newScheme.getName());
						oldScheme.setNote(newScheme.getNote());
						oldScheme.setIsdel("0");
						oldScheme.setReferTmpl(planGid);
						oldScheme.setPatSickId(psid);
						oldScheme.setCreateteam(user.getTeamId());
						schemeService.update(oldScheme);
						for (Motion motion : oldScheme.getMotionSet()) {
							motion.setIsdel("0");
							motionService.update(motion);
						}
						// 复制模版动作，目标成为患者的动作，目标
						motionService.saveMotionByCopy(newScheme, oldScheme.getGid());
						
					}else{// 不存在原有方案，新增
						Scheme scheme = new Scheme();
						scheme.setGid(ComUtil.getuuid());
						scheme.setCreateTime(now);
						scheme.setUpdateTime(now);
						scheme.setCreateUser(userId);
						scheme.setIsdel("0");
						scheme.setKfPatientId(gid);
						scheme.setName(newScheme.getName());
						scheme.setNote(newScheme.getNote());
						scheme.setReferTmpl(planGid);
						scheme.setPatSickId(psid);
						scheme.setLeg(leg);
						if(getMainShameNum(gid)==0){
							scheme.setIsMain("1");
						}
						scheme.setCreateteam(user.getTeamId());
						String sgid = schemeService.save(scheme);
						// 复制模版动作，目标成为患者的动作，目标
						motionService.saveMotionByCopy(newScheme, sgid);
						//加团患关系
						TeamKfpatient tp=new TeamKfpatient();
						tp.setPgid(gid);
						tp.setStgid(user.getTeamId());
						tp.setCreatetime(new Date());
						tp.setIsdel("0");
						teamKfpatientService.save(tp);
						
					}
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
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	/**
	 * 康复方案详情
	 * @param userId 用户GID
	 * @param token
	 * @param gid 方案GID
	 */
	public void planDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			SchemeTmpl schemeTmpl = schemeTmplService.get(gid);
			if (schemeTmpl == null)
				throw new CustomException(NO_TEMPLATE);
			
			JSONObject data = new JSONObject();
			data.put("gid", schemeTmpl.getGid());
			data.put("name", schemeTmpl.getName());
			data.put("slogan", schemeTmpl.getNote());
			
			JSONArray lstOper = new JSONArray();
			for (MotionTmpl motionTmpl : schemeTmpl.getMotionSet()) {
				JSONObject mot = new JSONObject();
				mot.put("gid",motionTmpl.getId());
				StandardMotion sMotion = motionTmpl.getStMotion();
				if (sMotion != null){
					mot.put("mGid",sMotion.getGid());
					mot.put("name",sMotion.getName());
					mot.put("pic",sMotion.getPic());
					mot.put("slogan",sMotion.getNote());
					mot.put("begin",sMotion.getBeginday());
					mot.put("type",sMotion.getDocType());
					mot.put("motionType",sMotion.getType());
					mot.put("targetType",sMotion.getTargetType());
					mot.put("video",sMotion.getVid());
					mot.put("videoDownload",sMotion.getVideoDownload());
				}
				JSONArray tarData = new JSONArray();
				for(TargetTmpl targetTmpl:motionTmpl.getTargetSet()){
					JSONObject tar = new JSONObject();
					tar.put("gid",targetTmpl.getId());	
					tar.put("beginDay",targetTmpl.getBdays());	
					tar.put("endDay",targetTmpl.getEdays());	
					tar.put("gCount",targetTmpl.getGroup());	
					tar.put("eCount",targetTmpl.getNum());	
					tarData.add(tar);
				}
				mot.put("lstTaget",tarData);
				lstOper.add(mot);
			}
			data.put("lstOper", lstOper);
			
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
	 * 康复方案新增
	 * @param userId 用户GID
	 * @param token
	 * @param teamGid 团队GID
	 * @param name 方案名称
	 * @param slogan 方案备注
	 * @param lstOper 
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void planAdd(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			scoreUtil.planAddChangeScor(scoreService, userService, userId, 1);
			Date now = new Date();
			SchemeTmpl schemeTmpl =new SchemeTmpl();
			schemeTmpl.setCreateTime(now);
			schemeTmpl.setUpdateTime(now);
			schemeTmpl.setIsdel("0");
			schemeTmpl.setName(name);
			schemeTmpl.setCreateTeam(teamGid);
			schemeTmpl.setNote(slogan);
			schemeTmpl.setGid(ComUtil.getuuid());
			schemeTmplService.save(schemeTmpl);
			if (StringUtils.isNotBlank(lstOper)){
				JSONArray json = new JSONArray().fromObject(lstOper);
				Iterator iter = json.iterator();
				while (iter.hasNext()) {
					JSONObject obj = new JSONObject().fromObject(iter.next());
					String addgid = obj.get("mGid") + "";
					
					MotionTmpl motionTmpl = new MotionTmpl();
					motionTmpl.setCreateTime(now);
					motionTmpl.setUpdateTime(now);
					motionTmpl.setIsdel("0");
					if(obj.get("order")!=null)motionTmpl.setOrdering(obj.get("order")+"");
					motionTmpl.setStMotionId(addgid);
					motionTmpl.setSgid(schemeTmpl.getGid());
					String mgid = motionTmplService.save(motionTmpl);
					
					targetTmplService.saveJsonTarget(mgid, obj.get("lstTarget"));
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
	 * 康复方案修改
	 * @param userId 用户GID
	 * @param token
	 * @param gid 方案GID
	 * @param name 方案名称
	 * @param slogan 方案备注
	 */
	public void planUpdateName(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			SchemeTmpl schemeTmpl = schemeTmplService.get(gid);
			if (schemeTmpl == null)
				throw new CustomException(NO_TEMPLATE);
			
			if (StringUtils.isNotBlank(name))
				schemeTmpl.setName(name);
			if (StringUtils.isNotBlank(slogan))
				schemeTmpl.setNote(slogan);
			
			schemeTmpl.setUpdateTime(new Date());
			schemeTmplService.update(schemeTmpl);
	
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
	 * 康复方案删除
	 * @param userId 用户GID
	 * @param token
	 * @param gid 方案GID
	 */
	public void planDelete(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);

			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			SchemeTmpl schemeTmpl = schemeTmplService.get(gid);
			if (schemeTmpl == null)
				throw new CustomException(NO_TEMPLATE);
			
			schemeTmpl.setIsdel("1");
			schemeTmpl.setUpdateTime(new Date());
			schemeTmplService.update(schemeTmpl);

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
	 * 康复动作详情
	 * @param userId 用户GID
	 * @param token
	 * @param gid 动作GID
	 */
	public void planOperDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
				
			MotionTmpl motionTmpl = motionTmplService.get(gid);
			if (motionTmpl == null)
				throw new CustomException(NO_TEMPLATE);
			
			JSONObject data = new JSONObject();
			data.put("gid", gid);
			data.put("pGid", motionTmpl.getSgid());
			StandardMotion sMotion = motionTmpl.getStMotion();
			if (sMotion != null) {
				data.put("name", sMotion.getName());
				data.put("pic", sMotion.getPic());
				data.put("vid", sMotion.getVid());
				data.put("slogan", sMotion.getNote());
			}
			
			JSONArray targ = new JSONArray();
			for (TargetTmpl targettmpl : motionTmpl.getTargetSet()) {
				JSONObject pat = new JSONObject();
				pat.put("gid",targettmpl.getGid());
				pat.put("beginDay",targettmpl.getBdays());
				pat.put("endDay",targettmpl.getBdays());
				pat.put("gCount",targettmpl.getGroup());
				pat.put("eCount",targettmpl.getNum());
				
				targ.add(pat);
			}
			data.put("lstTaget", targ);
			
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
	 * 方案动作编辑
	 * @param userId 用户GID
	 * @param token
	 * @param pGid	方案GID
	 * @param lstOper	动作GID,动作GID,动作GID
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void planUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(pGid))
				throw new CustomException(NO_ID);
			
			SchemeTmpl tmpl = schemeTmplService.get(pGid);
			if (tmpl == null)
				throw new CustomException(NO_TEMPLATE);

			for(MotionTmpl motiontmpl : tmpl.getMotionSet()){
				motionTmplService.delete(motiontmpl);
			}
		
			if (StringUtils.isNotBlank(lstOper)){
				Date now = new Date();
				JSONArray json = new JSONArray().fromObject(lstOper);
				Iterator iter = json.iterator();
				while (iter.hasNext()) {
					JSONObject obj = new JSONObject().fromObject(iter.next());
					String addgid = obj.get("mGid") + "";
					
					MotionTmpl motionTmpl = new MotionTmpl();
					motionTmpl.setCreateTime(now);
					motionTmpl.setUpdateTime(now);
					motionTmpl.setIsdel("0");
					if(obj.get("order")!=null)motionTmpl.setOrdering(obj.get("order") + "");
					motionTmpl.setStMotionId(addgid);
					motionTmpl.setSgid(pGid);
					String mgid = motionTmplService.save(motionTmpl);
					
					targetTmplService.saveJsonTarget(mgid, obj.get("lstTarget"));
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
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	/**
	 * 方案动作目标更新
	 * @param userId		用户GID
	 * @param token
	 * @param gid		动作目标GID
	 * @param beginDay		开始天数
	 * @param endDay		截止天数
	 * @param gCount		组数
	 * @param eCount		每组个数
	 */
	public void planOperTargetUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			TargetTmpl targetTmpl = targetTmplService.get(gid);
			if (targetTmpl == null)
				throw new CustomException(NO_TEMPLATE);
			
			targetTmpl.setBdays(beginDay);
			targetTmpl.setEdays(endDay);
			targetTmpl.setGroup(gCount);
			targetTmpl.setNum(eCount);
			targetTmpl.setUpdateTime(new Date());
			targetTmplService.update(targetTmpl);
		
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
	/**
	 * 动作数据接口
	 * @param userId 	用户GID
	 * @param token
	 * @param pid 		患者GID
	 * @param leg		左右腿
	 * @param teamGid	团队ID
	 */
	public void getData(){
		JSONObject ret = new JSONObject();
		try {
			JSONObject jo=new JSONObject();
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (teamGid == null)
				throw new CustomException(NO_TEAM);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			List<Conditions> clist=new ArrayList<Conditions>();
			Map<String,String> condition=new HashMap<String,String>();
			condition.put("pid", gid);
			condition.put("leg", leg);
			condition.put("date", sdf.format(new Date(new Date().getTime()-24*3600*1000)));
			if("".equals(teamGid)){
				Map<String,String> condition2=new HashMap<String,String>();
				condition2.put("pgid", gid);
				List<TeamKfpatient> list=teamKfpatientService.getConditonList(condition2, null, false, null);
				teamGid=list.get(0).getStgid();
			}
			condition.put("teamGid", teamGid);
			Conditions c2=new Conditions();
			c2.setType("map");
			c2.setMap(condition);
			clist.add(c2);
			List<KfData> userData =kfDataService.getConditonsList(clist, "mgid", false, null);
			int a=0,b=0,sum=0,act=0;
			for (KfData kfData : userData) {
				switch(kfData.getMotion().getStMotion().getId()){
				case "8":if(Integer.parseInt(kfData.getNum())>b)b=Integer.parseInt(kfData.getNum());break;
				case "5":if(Integer.parseInt(kfData.getNum())>b)b=Integer.parseInt(kfData.getNum());break;
				case "3":if(Integer.parseInt(kfData.getNum())>b)b=Integer.parseInt(kfData.getNum());break;
				case "4":if(Integer.parseInt(kfData.getNum())>b)b=Integer.parseInt(kfData.getNum());break;
//				case "10":sum++;break;
//				case "9":sum++;break;
//				case "11":sum++;break;
				}
				if(kfData.getType().equals("2")){
					sum++;
				}
			}
			List<Conditions> myList=new ArrayList<Conditions>();
			Map<String,String> map=new HashMap<String,String>();
			map.put("pid", gid);
			Conditions c1=new Conditions();
			c1.setType("map");
			c1.setMap(map);
			myList.add(c1);
			Conditions c3=new Conditions();
			c3.setType("timeA");
			c3.setKey("time");
			c3.setValue(sdf2.format(new Date(new Date().getTime()-24*3600*1000)));
			myList.add(c3);
			Conditions c4=new Conditions();
			c4.setType("timeB");
			c4.setKey("time");
			c4.setValue(sdf2.format(new Date()));
			myList.add(c4);
			List<ActivityData> actData =activityDataService.getConditonsList(myList, "", false, null);
			for (ActivityData activityData : actData) {
				if(Integer.parseInt(activityData.getActivity())>100){
					act++;
				}
			}
			jo.put("rom1", a+"");
			jo.put("rom2", b+"");
			jo.put("num", sum+"");
			jo.put("time", act*5+"");
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
	

	
	
	
	
	/**
	 * 动作数据接口
	 * @param userId 	用户GID
	 * @param token
	 * @param gid 		患者GID
	 * @param leg		左右腿
	 * @param date		时间
	 * @param mid		动作GID
	 * @param teamGid	团队ID
	 */
	public void dataList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (teamGid == null)
				throw new CustomException(NO_TEAM);
			
			List<Conditions> clist=new ArrayList<Conditions>();
			Map<String,String> condition=new HashMap<String,String>();
			condition.put("pid", gid);
			condition.put("leg", leg);
			condition.put("mgid", mid);
			condition.put("date", date);
			if("".equals(teamGid)){
				Map<String,String> condition2=new HashMap<String,String>();
				condition2.put("pgid", gid);
				List<TeamKfpatient> list=teamKfpatientService.getConditonList(condition2, null, false, null);
				teamGid=list.get(0).getStgid();
			}
			condition.put("teamGid", teamGid);
			Conditions c2=new Conditions();
			c2.setType("map");
			c2.setMap(condition);
			clist.add(c2);
			
			
			
			
			
			
			List<KfData> userData =kfDataService.getConditonsList(clist, "mgid", false, null);
			JSONObject jo=new JSONObject();
			JSONArray data = new JSONArray();
			SimpleDateFormat ss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			
			
			
			int i=0,max=0,array=0,excellent=80,good=60,qualified=0;
			for (KfData myData : userData){
				JSONObject kfd = new JSONObject();
				kfd.put("score", myData.getScore());
				kfd.put("num", myData.getNum());
				kfd.put("type", myData.getType());
				kfd.put("date", ss.format(myData.getCreateTime()));
				
				
				
				
				
				if("1".equals(myData.getType())){
					//角度
					int angle=Integer.parseInt(myData.getNum());
					array+=angle;
					if(angle>max){
						max=angle;
					}
					
					if(myData.getMotion().getStMotion().getId().equals("8")){
						if(angle<=8){
							kfd.put("grade", "1");
							qualified++;
						}else if(angle<=15){
							kfd.put("grade", "2");
							qualified++;
						}else{
							kfd.put("grade", "3");
						}
					}else{
						KfPatientSick pSick = kfPatientSickService.get(gid, teamGid, 1);
						int days=0;
						if("1".equals("leg")){
							days =Integer.parseInt(ComUtil.daysBetweenDate(pSick.getOperLDate(), new Date())+"");
						}else{
							days = Integer.parseInt(ComUtil.daysBetweenDate(pSick.getOperRDate(), new Date())+"");
						}
						
						Set<Target> t= myData.getMotion().getTargetSet();
						for (Target target : t) {
							if(Integer.parseInt(target.getBdays())<=days&&days<=Integer.parseInt(target.getEdays())){
								excellent=Integer.parseInt(target.getNum())-10;
								good=Integer.parseInt(target.getNum())-30;
							}
						}
						if(angle>=excellent){
							kfd.put("grade", "1");
							qualified++;
						}else if(angle>=good){
							kfd.put("grade", "2");
							qualified++;
						}else{
							kfd.put("grade", "3");
						}
					}
				}else{
					//肌力
					int score=Integer.parseInt(myData.getScore());
					array+=score;
					if(score>max){
						max=score;
					}
					if(score>=excellent){
						kfd.put("grade", "1");
						qualified++;
					}else if(score>=good){
						kfd.put("grade", "2");
						qualified++;
					}else{
						kfd.put("grade", "3");
					}
					if(max>100)max=100;
				}
				i++;
				data.add(kfd);
			}
			if(i==0){
				i=1;
			}
			jo.put("data", data);
			jo.put("max", max+"");
			jo.put("array", array/i+"");
			jo.put("qualified", (int)((double)qualified/i*100)+"");
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jo);
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
	
	public void completeData(){
		JSONObject ret = new JSONObject();
		 try {
			 HttpServletRequest request = ServletActionContext.getRequest();
			 boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (teamGid == null)
				throw new CustomException(NO_TEAM);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Conditions>list=new ArrayList<Conditions>();
			Conditions c1=new Conditions();
			c1.setType("timeA");
			c1.setKey("createTime");
			if("2".equals(type)){
				c1.setValue(sdf.format(new Date(new Date().getTime()-(new Long(7)*24*60*60*1000))));
			}else if("3".equals(type)){
				c1.setValue(sdf.format(new Date(new Date().getTime()-(new Long(30)*24*60*60*1000))));
			}
			list.add(c1);
			Map<String,String> condition=new HashMap<String,String>();
			condition.put("pid", gid);
			condition.put("leg", leg);
			condition.put("teamGid", teamGid);
			Conditions c2=new Conditions();
			c2.setType("map");
			c2.setMap(condition);
			list.add(c2);
			List<KfData> userData =kfDataService.getConditonsList(list, "updateTime", false, null);
			Set<String>set =new HashSet<String>();
			for (KfData kfData : userData) {
				if("4".equals(kfData.getType())){
					continue;
				}
				set.add(kfData.getMgid());
			}
			Set<String>standard=new HashSet<String>();
			int i=0;
			JSONArray calone = new JSONArray();
			JSONObject calen = new JSONObject();
			JSONArray data = new JSONArray();
			for (String ss : set) {
				Motion motion=motionService.get(ss);
				if(motion==null)continue;
				if(!standard.contains((motion.getStMotionId()))){
					if(i!=0){
						calen.put("completeData",data);
						calone.add(calen);
						data.clear();
					}
					standard.add(motion.getStMotionId());
					StandardMotion motionStandard= stMotionService.get(motion.getStMotionId());
					calen.put("name",motionStandard.getName());
					calen.put("type",motionStandard.getType());
					calen.put("mid",motion.getId());
				}
				List<Object[]> kfDataList = kfDataService.getDataByType(ss,type,leg,teamGid,gid);
				if(kfDataList.size()!=0){
					for (Object[] obj : kfDataList){
						JSONObject kfd = new JSONObject();
						kfd.put("date", obj[0]);
						kfd.put("type", obj[1]);
						kfd.put("score", obj[2]);
						kfd.put("num", obj[3]);
						data.add(kfd);
					} 
				}
				i++;
			}
			if(i!=0){
				calen.put("completeData",data);
				calone.add(calen);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", calone);
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
	 * 标准动作列表接口
	 */
	public void planOperList(){
		JSONObject ret = new JSONObject();
		try {
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("isdel", "0");
			List<StandardMotion> stmotionList = stMotionService.getConditonList(condition, "ordering", false, null);
			JSONArray data = new JSONArray();
			for (StandardMotion motion : stmotionList){
				JSONObject mo = new JSONObject();
				mo.put("gid", motion.getGid());
				mo.put("name", motion.getName());
				mo.put("pic", motion.getPic());
				mo.put("type", motion.getDocType());
				mo.put("motionType", motion.getType());
				mo.put("targetType", motion.getTargetType());
				mo.put("slogan", motion.getNote());
				mo.put("vid", motion.getVid());
				mo.put("videoDownload", motion.getVideoDownload());
				data.add(mo);
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
	 * (患者)康复方案详情
	 * @param userId 	用户GID
	 * @param token
	 * @param gid 		患者GID 
	 */
	public void patientPlanDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			JSONArray Schemes = new JSONArray();
			for (int i = 1; i <= 2; i++){
				//根据患者和医生的id就获得了方案
				Scheme scheme = schemeService.getByPatientUser(gid, userId, i+"");
				if (scheme != null) {
					JSONObject schemeJson = new JSONObject();
					schemeJson.put("gid", scheme.getGid());
					schemeJson.put("name", scheme.getName());
					schemeJson.put("slogan", scheme.getNote());
					schemeJson.put("leg", i);
					KfPatientSick patSick = scheme.getPatSick();
					if (patSick != null && 1 == i){
						schemeJson.put("operDate", ComUtil.date2Str(patSick.getOperLDate()));
						if(patSick.getOperationL()!=null){
							schemeJson.put("operationName", patSick.getOperationL().getName());
							schemeJson.put("operationGid", patSick.getOperationL().getGid());
						}else{
							schemeJson.put("operationName", "");
							schemeJson.put("operationGid", "");
						}
					}
					else if (patSick != null && 2 == i){
						schemeJson.put("operDate", ComUtil.date2Str(patSick.getOperRDate()));
						if(patSick.getOperationR()!=null){
							schemeJson.put("operationName", patSick.getOperationR().getName());
							schemeJson.put("operationGid", patSick.getOperationR().getGid());
						}else{
							schemeJson.put("operationName", "");
							schemeJson.put("operationGid", "");
						}
					}
					
					JSONArray lstOper = new JSONArray();
					for (Motion motion : scheme.getMotionSet()) {
						if(motion.getIsdel().equals("0"))continue;
						JSONObject mot = new JSONObject();
						mot.put("gid",motion.getId());
						StandardMotion sMotion = motion.getStMotion();
						if (sMotion != null){
							mot.put("mGid",sMotion.getGid());
							mot.put("name",sMotion.getName());
							mot.put("pic",sMotion.getPic());
							mot.put("begin",sMotion.getBeginday());
							mot.put("type",sMotion.getDocType());
							mot.put("motionType",sMotion.getType());
							mot.put("targetType",sMotion.getTargetType());
							mot.put("video",sMotion.getVid());
							mot.put("slogan",sMotion.getNote());
							mot.put("videoDownload",sMotion.getVideoDownload());
						}
						JSONArray tarData = new JSONArray();
						for(Target target:motion.getTargetSet()){
							JSONObject tar = new JSONObject();
							tar.put("gid",target.getId());	
							tar.put("beginDay",target.getBdays());	
							tar.put("endDay",target.getEdays());	
							tar.put("gCount",target.getGroup());	
							tar.put("eCount",target.getNum());	
							tarData.add(tar);
						}
						mot.put("lstTaget",tarData);
						lstOper.add(mot);
					}
					schemeJson.put("lstOper", lstOper);
					Schemes.add(schemeJson);
				}
			}
			
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", Schemes);
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
	 * (患者)动作详情
	 * @param userId 用户GID
	 * @param token
	 * @param gid 动作GID
	 */
	public void patientPlanOperDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
				
			Motion motion = motionService.get(gid);
			if (motion == null)
				throw new CustomException(NO_MOTION);
			
			JSONObject data = new JSONObject();
			data.put("gid", gid);
			data.put("pGid", motion.getSgid());
			StandardMotion sMotion = motion.getStMotion();
			if (sMotion != null) {
				data.put("name", sMotion.getName());
				data.put("pic", sMotion.getPic());
				data.put("vid", sMotion.getVid());
				data.put("slogan", sMotion.getNote());
			}
			
			JSONArray targ = new JSONArray();
			for (Target target : motion.getTargetSet()) {
				JSONObject pat = new JSONObject();
				pat.put("gid",target.getId());
				pat.put("beginDay",target.getBdays());
				pat.put("endDay",target.getBdays());
				pat.put("gCount",target.getGroup());
				pat.put("eCount",target.getNum());
				
				targ.add(pat);
			}
			data.put("lstTaget", targ);
			
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
	 * （患者）方案编辑
	 * @param userId 用户GID
	 * @param token
	 * @param gid	方案GID
	 * @param name	方案名称
	 * @param slogan 方案备注
	 * @param lstOper	动作GID,动作GID,动作GID
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public void patientPlanUpdate(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Scheme patientscheml = schemeService.get(gid);
			if (patientscheml == null)
				throw new CustomException(NO_SCHEME);
			KfPatientSick psick=patientscheml.getPatSick();
			if(psick!=null){
				if("1".equals(patientscheml.getLeg())){
					psick.setOperationGidL(operationGid);
				}else{
					psick.setOperationGidR(operationGid);
				}
			}
			Date now = new Date();
			patientscheml.setName(name);
			patientscheml.setNote(slogan);
			patientscheml.setUpdateTime(now);
			schemeService.update(patientscheml);
			
			Set<Motion> oldMotionSet = patientscheml.getMotionSet();
			List<String> undelMotionIdList = new ArrayList<String>();// 无需删除的动作id列表
			if (StringUtils.isNotBlank(lstOper)){
				JSONArray json = new JSONArray().fromObject(lstOper);
				Iterator iter = json.iterator();
				
				while (iter.hasNext()){// 遍历新动作列表
					boolean flag = true;// 判断此新动作是否原来就有，若有，则不处理
					JSONObject obj = new JSONObject().fromObject(iter.next());
					String addgid = obj.get("mGid") + "";
					if (StringUtils.isNotBlank(addgid)){
						for (Motion oldMotion : oldMotionSet){
							if (addgid.equals(oldMotion.getStMotionId())){
								flag = false; // 更新，无需新增
								undelMotionIdList.add(oldMotion.getId()); // 此动作无需删除
								if(obj.get("order")!=null)oldMotion.setOrdering(obj.get("order")+"");
								oldMotion.setIsdel("1");
								oldMotion.setCreateTime(now);
								motionService.update(oldMotion);
								targetService.saveJsonTarget(oldMotion.getId(), obj.get("lstTarget")); // 更新动作目标
								break;
							}
						}
					}
					if (flag){
						//新增
						Motion motion = new Motion();
						motion.setCreateTime(now);
						motion.setUpdateTime(now);
						if(obj.get("order")!=null)motion.setOrdering(obj.get("order")+"");
						motion.setIsdel("1");
						motion.setStMotionId(addgid);
						motion.setSgid(gid);
						String mgid = motionService.save(motion);
						targetService.saveJsonTarget(mgid, obj.get("lstTarget"));
					}
				}
			}
			
			for (Motion oldMotion : oldMotionSet){// 处理旧动作
				if (!undelMotionIdList.contains(oldMotion.getId()))
					// 旧动作不在列表中，则删除，同时会级联删除动作目标
					oldMotion.setIsdel("0");
					oldMotion.setCreateTime(now);
					motionService.update(oldMotion);
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
	/**
	 * 根据患者号查询患者信息
	 * @param userId		用户GID
	 * @param token
	 * @param id			患者id
	 */
	public void selectPatientDetail(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			if (StringUtils.isBlank(id))
				throw new CustomException(NO_ID);
			
			KfPatient patient = kfPatientService.getByPatientId(id);
			if (patient == null)
				throw new CustomException(NO_WARDPATIENT);
			
			JSONObject data = new JSONObject();
			data.put("id", patient.getId());
			data.put("gid", patient.getGid());
			data.put("name", patient.getName());
			data.put("avatars", patient.getAvatars());
			
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
	
	private int getMainShameNum( String gid){
		int i=schemeService.countMainNumBySql(gid);
		return i;
	}
	public String getTeamGid() {
		return teamGid;
	}
	public void setTeamGid(String teamGid) {
		this.teamGid = teamGid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParentGid() {
		return parentGid;
	}
	public void setParentGid(String parentGid) {
		this.parentGid = parentGid;
	}
	public String getPatientGid() {
		return patientGid;
	}
	public void setPatientGid(String patientGid) {
		this.patientGid = patientGid;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getSick() {
		return sick;
	}
	public void setSick(String sick) {
		this.sick = sick;
	}
	public String getPlanGid() {
		return planGid;
	}
	public void setPlanGid(String planGid) {
		this.planGid = planGid;
	}
	public String getOperDate() {
		return operDate;
	}
	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
	public String getOperRDate() {
		return operRDate;
	}
	public void setOperRDate(String operRDate) {
		this.operRDate = operRDate;
	}
	public String getOperLDate() {
		return operLDate;
	}
	public void setOperLDate(String operLDate) {
		this.operLDate = operLDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getOGid() {
		return oGid;
	}
	public void setOGid(String oGid) {
		this.oGid = oGid;
	}
	public String getBeginDay() {
		return beginDay;
	}
	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getTartype() {
		return tartype;
	}
	public void setTartype(String tartype) {
		this.tartype = tartype;
	}
	public String getTargid() {
		return targid;
	}
	public void setTargid(String targid) {
		this.targid = targid;
	}
	public String getGCount() {
		return gCount;
	}
	public void setGCount(String gCount) {
		this.gCount = gCount;
	}
	public String getECount() {
		return eCount;
	}
	public void setECount(String eCount) {
		this.eCount = eCount;
	}
	public String getPGid() {
		return pGid;
	}
	public void setPGid(String pGid) {
		this.pGid = pGid;
	}
	public String getLstOper() {
		return lstOper;
	}
	public void setLstOper(String lstOper) {
		this.lstOper = lstOper;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLeg() {
		return leg;
	}
	public void setLeg(String leg) {
		this.leg = leg;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getOperationGid() {
		return operationGid;
	}
	public void setOperationGid(String operationGid) {
		this.operationGid = operationGid;
	}
	
}

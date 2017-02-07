package com.sd.action;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.sd.service.KfPatientSickService;
import com.sd.service.SchemeService;
import com.sd.service.TeamApplyService;
import com.sd.service.TeamKfpatientService;
import com.sd.service.TeamService;
import com.sd.service.TemplateService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.CustomException;
import com.sd.util.scoreUtil;
import com.sd.vo.KfPatientSick;
import com.sd.vo.Scheme;
import com.sd.vo.Sick;
import com.sd.vo.Team;
import com.sd.vo.TeamApply;
import com.sd.vo.TeamKfpatient;
import com.sd.vo.User;

/**
 * 团队接口
 * @author bxf
 *
 */

public class TeamAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	@Resource private TeamService teamService;
	@Resource private TeamApplyService teamApplyService;
	@Resource private UserTokenService userTokenService;
	@Resource private UserService userService;
	@Resource private TemplateService templateService;
	@Resource private SchemeService schemeService;
	@Resource private KfPatientSickService kfPatientSickService;
	@Resource private TeamKfpatientService teamKfpatientService;
	private String teamGid;
	private String name;
	private String local;
	private String operType;
	private String applyGid;
	private String teamId;
	
	//团队创建
//	userId	是	字符串	用户gid	
//	token	是	字符串	token	
//	name	是	字符串	团队名称	
//	local	是	字符串	所属地区	
	public void addTeamInfo(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			Team team =new Team();
			Map<String, String> map=new HashMap<String, String>();
			List<Team> tt= teamService.getConditonList(map,"id",true,null);
			if(tt.size()!=0){
				team.setId("T"+(Integer.parseInt(tt.get(0).getId().substring(1))+ new Random().nextInt(10)));
			}else{
				team.setId("T10001");
			}
			
			team.setGid(UUID.randomUUID().toString());
			team.setName(name);
			team.setLocal(local);
			team.setCreateTime(new Date());
			team.setIsdel("0");
			teamService.save(team);
			User user=userService.get(request.getHeader("userid"));
			user.setTeamId(team.getGid());
			userService.update(user);
			scoreUtil.addTeamInfoChangeScor(scoreService,userService,userId,3);
			//更改他的方案对应的疾病里面的团队ID
			Map<String, String> condition1=new HashMap<String, String>();
			condition1.put("createUser", user.getGid());
			List<Scheme> list=schemeService.getConditonList(condition1, "", false, null);
			Set<String>set=new HashSet<String>();
			for (Scheme scheme : list) {
				KfPatientSick sick= scheme.getPatSick();
				sick.setOid(team.getGid());
				kfPatientSickService.update(sick);
				set.add(scheme.getKfPatientId());
			}
			
			for (String pat : set) {
				TeamKfpatient tp=new TeamKfpatient();
				tp.setPgid(pat);
				tp.setStgid(team.getGid());
				tp.setIsdel("0");
				tp.setCreatetime(new Date());
				tp.setUpdateTime(new Date());
				teamKfpatientService.save(tp);
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
	//新申请成员处理
//	userId	是	字符串	用户gid	
//	token	是	字符串	token	
//	teamGid	是	字符串	团队gid	
//	applyGid	是	字符串	申请人GID	
//	operType	是	字符串	操作类型（1-同意/0-忽略）
	public void applyOper(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			Map<String, String> map=new HashMap<String, String>();
			map.put("teamId", teamGid);
			map.put("userId", applyGid);
			map.put("isdel", "0");
			List<TeamApply> list= teamApplyService.getConditonList(map, null, false, null);
			TeamApply teamApply=teamApplyService.get(list.get(0).getGid());
			if(operType.equals("1")){
				teamApply.setIsdel("1");
				teamApply.setFlag("2");
				User user=userService.get(applyGid);
				scoreUtil.applyOperChangeScor(scoreService,userService, applyGid, 2);
				user.setTeamId(teamGid);
				userService.update(user);
				//更改他的方案对应的疾病里面的团队ID
				Map<String, String> condition1=new HashMap<String, String>();
				condition1.put("createUser", user.getGid());
				List<Scheme> slist=schemeService.getConditonList(condition1, "", false, null);
				Set<String>set=new HashSet<String>();
				for (Scheme scheme : slist) {
					scheme.setIsdel("1");
					scheme.setIsMain("0");
					schemeService.update(scheme);
					KfPatientSick sick= scheme.getPatSick();
					sick.setOid(teamGid);
					kfPatientSickService.update(sick);
					set.add(scheme.getKfPatientId());
				}
				
				for (String pat : set) {
					TeamKfpatient tp=new TeamKfpatient();
					tp.setPgid(pat);
					tp.setStgid(teamGid);
					tp.setIsdel("0");
					tp.setCreatetime(new Date());
					tp.setUpdateTime(new Date());
					teamKfpatientService.save(tp);
				}
			}
			if(operType.equals("0")){
				teamApply.setIsdel("1");
				teamApply.setFlag("3");
			}
			teamApplyService.update(teamApply);
			
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
	//新申请成员全部忽略
//	userId	是	字符串	用户gid	
//	token	是	字符串	token	
//	teamGid	是	字符串	团队gid
		public void applyIgnore(){
			JSONObject ret = new JSONObject();
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				boolean logined = userTokenService.checkToken(request);
				if (!logined)
					throw new CustomException(NO_LOGIN);
				Map<String, String> map=new HashMap<String, String>();
				map.put("teamId", teamGid);
				List<TeamApply> list= teamApplyService.getConditonList(map, null, false, null);
				for (TeamApply teamApply : list) {
					TeamApply tt =teamApplyService.get(teamApply.getGid());
					tt.setIsdel("1");
					tt.setFlag("3");
					teamApplyService.update(tt);
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
	//退出团队
//		userId	是	字符串	用户gid	
//		token	是	字符串	token	
//		teamGid	是	字符串	团队gid
	public void teamQuit(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			User user =userService.get(request.getHeader("userid"));
			Map<String, String> condition=new HashMap<String, String>();
			condition.put("teamId", user.getTeamId());
			List<User> userList=userService.getConditonList(condition, "", false, null);
			//如果团队里面有其他医生，把他制定的方案转移给其他成员
			if(userList.size()>1){
				String substitute="";
				for (User myUser : userList) {
					if(myUser.getGid()!=user.getGid()){
						substitute=myUser.getGid();
						break;
					}
				}
				Map<String, String> condition1=new HashMap<String, String>();
				condition1.put("createUser", user.getGid());
				List<Scheme> list=schemeService.getConditonList(condition1, "", false, null);
				for (Scheme scheme : list) {
					scheme.setCreateUser(substitute);
					schemeService.update(scheme);
				}
			}
			
			user.setTeamId(null);
			userService.update(user);
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
	//申请加入团队（取消申请）
//	userId	是	字符串	用户gid	
//	token	是	字符串	token	
//	teamGid	是	字符串	团队gid	
//	operType	是	字符串	操作类型（1-申请/0-取消申请）
	public void teamApply(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if(operType.equals("1")){
				Map<String, String> map=new HashMap<String, String>();
				map.put("teamId", teamGid);
				map.put("userId", request.getHeader("userid"));
				map.put("flag", "1");
				List<TeamApply> list= teamApplyService.getConditonList(map, null, false, null);
				if(list.size()!=0){
					ret.put("code", "0");
					ret.put("message", "您已申请过此团队");
					ret.put("result_data", new JSONObject());
					putDataOut(ret.toString());
					return;
				}
				TeamApply teamApply=new TeamApply();
				teamApply.setGid(UUID.randomUUID().toString());
				teamApply.setUserId(request.getHeader("userid"));
				teamApply.setTeamId(teamGid);
				teamApply.setCreateTime(new Date());
				teamApply.setFlag("1");
				teamApply.setIsdel("0");
				teamApplyService.save(teamApply);
			}else{
				Map<String, String> map=new HashMap<String, String>();
				map.put("teamId", teamGid);
				map.put("userId", request.getHeader("userid"));
				List<TeamApply> list= teamApplyService.getConditonList(map, null, false, null);
				TeamApply teamApply=teamApplyService.get(list.get(0).getGid());
//				teamApply.setIsdel("0");
//				teamApplyService.update(teamApply);
				teamApplyService.delete(teamApply);
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
	//团队编辑
//	userId	是	字符串	用户gid	
//	token	是	字符串	token	
//	teamGid	是	字符串	团队gid	
//	name	否	字符串	团队名称	
//	local	否	字符串	团队地区	
	public void updateTeamInfo(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			Team team =teamService.get(teamGid);
			if (StringUtils.isNotBlank(name))team.setName(name);
			if (StringUtils.isNotBlank(local))team.setLocal(local);
			team.setUpdateTime(new Date());
			teamService.update(team);
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
	 * 获取团队信息
	 * @param userid
	 * @param token
	 * @param teamGid
	 */
	public void getTeamInfo(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(teamGid))
				throw new CustomException(NO_ID);
				
			Team team = teamService.get(teamGid);
			if (team == null)
				throw new CustomException(NO_TEAM);
			
			JSONObject data = new JSONObject();
			data.put("id", team.getId());
			data.put("gid", team.getGid());
			data.put("name", team.getName());
			data.put("no", team.getId());
			data.put("local", team.getLocal());
			// 新申请
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("teamId", teamGid);
			condition.put("flag", "1");
			condition.put("isdel", "0");
			List<TeamApply> teamApplyList = teamApplyService.getConditonList(condition, "createTime", true, null);
			JSONArray apps = new JSONArray();
			for (TeamApply teamApply : teamApplyList){
				JSONObject app = new JSONObject();
				User appUser = teamApply.getUser();
				if (appUser != null){
					app.put("uGid", appUser.getGid());
					app.put("uName", appUser.getName());
//					app.put("uSex", appUser.getSex());
//					app.put("uBirthday", ComUtil.dateTime2Str(appUser.getBirthday()));
					app.put("uAvatars", appUser.getAvatars());
				} else {
					app.put("uGid", "");
					app.put("uName", "");
					app.put("uAvatars", "");
				}
				apps.add(app);
			}
			data.put("apply", apps);
			// 团队成员
			condition.clear();
			condition.put("teamId", teamGid);
			condition.put("isdel", "1");
			List<User> memberList = userService.getConditonList(condition, "createTime", true, null);
			JSONArray mebs = new JSONArray();
			for (User member : memberList){
				JSONObject meb = new JSONObject();
				meb.put("mGid", member.getGid());
				meb.put("mName", member.getName());
				meb.put("mAvatars", member.getAvatars());
				mebs.add(meb);
			}
			data.put("member", mebs);
			// 康复模版
//			data.put("template", 3);
			// 查房助手
			data.put("ward", templateService.getTmplNumByTeam(teamGid));
			// 患教材料
//			data.put("edu", "");
			
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
	 * 获取团队信息
	 * @param userid
	 * @param token
	 * @param teamId
	 */
	public void getTeamById(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			if (StringUtils.isBlank(teamId))
				throw new CustomException(NO_ID);
			
			Map<String, String > map = new HashMap<String, String>();
			map.put("id",teamId);
			map.put("isdel","0");
			List<Team> list = teamService.getConditonList(map, null, false, null);
			if (list.size() == 0)
				throw new CustomException(NO_TEAM);
			Team team = list.get(0);
			JSONObject data = new JSONObject();
			data.put("gid", team.getGid());
			data.put("name", team.getName());
			data.put("no", team.getId());
			data.put("local", team.getLocal());
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getTeamGid() {
		return teamGid;
	}

	public void setTeamGid(String teamGid) {
		this.teamGid = teamGid;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getApplyGid() {
		return applyGid;
	}
	public void setApplyGid(String applyGid) {
		this.applyGid = applyGid;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
}

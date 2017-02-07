package com.sd.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AnnouncementService;
import com.sd.service.KfDataService;
import com.sd.service.KfPatientService;
import com.sd.service.KfPatientSickService;
import com.sd.service.MotionService;
import com.sd.service.OperationService;
import com.sd.service.RdPatientService;
import com.sd.service.SchemeService;
import com.sd.service.SickService;
import com.sd.service.StandardMotionService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.Announcement;
import com.sd.vo.KfData;
import com.sd.vo.KfPatient;
import com.sd.vo.KfPatientSick;
import com.sd.vo.Motion;
import com.sd.vo.Operation;
import com.sd.vo.RdPatient;
import com.sd.vo.Scheme;
import com.sd.vo.StandardMotion;
import com.sd.vo.User;

public class PatientAction extends BaseAction {
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private UserTokenService userTokenService;
	@Resource
	private AnnouncementService announcementService;
	@Resource
	private UserService userService;
	@Resource
	private KfDataService kfDataService;
	@Resource
	private KfPatientSickService kfPatientSickService;
	@Resource
	private SchemeService schemeService;
	@Resource
	private KfPatientService kfPatientService;
	@Resource
	private RdPatientService rdPatientService;
	@Resource
	private SickService sickService;
	@Resource
	private MotionService motionService;
	@Resource
	private StandardMotionService stMotionService;
	@Resource
	private OperationService operationService;

	private String gid;
	private String sickGid;
	private String leftOperationGid;
	private String rightOperationGid;
	private String leftOperationDate;
	private String rightOperationDate;
	private String name; // 姓名
	private String sex; // 性别
	private String age; // 年龄
	private String visitDate; // 就诊日期
	private String phone;
	private String relativesPhone;
	private String address;
	private String postcode;
	private String other;
	private String caseNo;
	private String bedNo;
	private String patientNo;
	private String IDCardNo;

	// 获取手术名称
	// 手术gid gid
	public void queryOperation() {
		JSONObject ret = new JSONObject();
		try {
			JSONArray reslist = new JSONArray();
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("parentGid", gid);
			List<Operation> list = operationService.getConditonList(condition,
					"", false, null);
			for (Operation operation : list) {
				JSONObject res = new JSONObject();
				res.put("gid", operation.getGid());
				res.put("name", operation.getName());
				reslist.add(res);
			}

			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", reslist);
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 更新患者信息
	public void modifyPatientInformation() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			User user = userService.get(userId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> pcondition = new HashMap<String, String>();
			pcondition.put("pid", gid);
			pcondition.put("oid", user.getTeamId());
			List<KfPatientSick> plist = kfPatientSickService.getConditonList(
					pcondition, "", false, null);
			if (plist.size() != 0) {
				KfPatientSick s = plist.get(0);
				s.setSid(sickGid);
				s.setUpdatetime(new Date());
				s.setOperationGidL(leftOperationGid);
				s.setOperationGidR(rightOperationGid);
				if (StringUtils.isNotBlank(leftOperationDate))
					s.setOperLDate(sdf2.parse(leftOperationDate));
				if (StringUtils.isNotBlank(rightOperationDate))
					s.setOperRDate(sdf2.parse(rightOperationDate));
				kfPatientSickService.update(s);
			} else {
				KfPatientSick s = new KfPatientSick();
				s.setSid(sickGid);
				s.setOperationGidL(leftOperationGid);
				s.setOperationGidR(rightOperationGid);
				if (StringUtils.isNotBlank(leftOperationDate))
					s.setOperLDate(sdf2.parse(leftOperationDate));
				if (StringUtils.isNotBlank(rightOperationDate))
					s.setOperRDate(sdf2.parse(rightOperationDate));
				kfPatientSickService.save(s);
			}
			Map<String, String> rcondition = new HashMap<String, String>();
			rcondition.put("pid", gid);
			rcondition.put("teamGid", user.getTeamId());
			List<RdPatient> rlist = rdPatientService.getConditonList(
					rcondition, "", false, null);
			if (rlist.size() != 0) {
				RdPatient r = rlist.get(0);
				r.setName(name);
				r.setGender(sex);
				r.setAge(age);
				if (StringUtils.isNotBlank(visitDate))
					r.setVisitDate(sdf2.parse(visitDate));
				r.setPhone(phone);
				r.setRelativesPhone(relativesPhone);
				r.setPatientNo(patientNo);
				r.setBedNo(bedNo);
				r.setCaseNo(caseNo);
				r.setIDCardNo(IDCardNo);
				r.setAddress(address);
				r.setPostcode(postcode);
				r.setOther(other);
				rdPatientService.update(r);
			} else {
				RdPatient r = new RdPatient();
				r.setGid(UUID.randomUUID().toString());
				r.setPid(gid);
				r.setTeamGid(user.getTeamId());
				r.setName(name);
				r.setGender(sex);
				r.setAge(age);
				if (StringUtils.isNotBlank(visitDate))
					r.setVisitDate(sdf2.parse(visitDate));
				r.setPhone(phone);
				r.setRelativesPhone(relativesPhone);
				r.setPatientNo(patientNo);
				r.setBedNo(bedNo);
				r.setCaseNo(caseNo);
				r.setIDCardNo(IDCardNo);
				r.setAddress(address);
				r.setPostcode(postcode);
				r.setOther(other);
				r.setUpdateTime(new Date());
				r.setCreateTime(new Date());
				r.setIsdel("1");
				rdPatientService.save(r);
			}
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

	// 查询患者信息
	public void queryPatientInformation() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			User user = userService.get(userId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			KfPatient patient = kfPatientService.get(gid);
			JSONObject data = new JSONObject();
			data.put("gid", patient.getGid());
			data.put("code", patient.getId());
			data.put("avatars", patient.getAvatars());
			data.put("local", patient.getLocal());
			Map<String, String> map = new HashMap<String, String>();
			map.put("pid", gid);
			map.put("teamGid", user.getTeamId());
			List<RdPatient> list = rdPatientService.getConditonList(map, "",
					false, null);
			data.put("name", patient.getName());
			data.put("age", patient.getAge());
			data.put("gender", patient.getGender());
			data.put("phone", patient.getPhone());
			if (list.size() != 0) {
				RdPatient record = list.get(0);
				data.put("caseGid", record.getGid());
				if (record.getVisitDate() != null) {
					data.put("visitDate", sdf.format(record.getVisitDate()));
				} else {
					data.put("visitDate", "");
				}
				data.put("relativesPhone", record.getRelativesPhone());
				data.put("patientNo", record.getPatientNo());
				data.put("bedNo", record.getBedNo());
				data.put("caseNo", record.getCaseNo());
				data.put("IDCardNo", record.getIDCardNo());
				data.put("address", record.getAddress());
				data.put("postCode", record.getPostcode());
				data.put("other", record.getOther());
			} else {
				data.put("relativesPhone", "");
				data.put("patientNo", "");
				data.put("bedNo", "");
				data.put("caseNo", "");
				data.put("IDCardNo", "");
				data.put("address", "");
				data.put("postCode", "");
				data.put("other", "");
			}

			if (user.getTeamId() != null || user.getTeamId() != "") {
				KfPatientSick pSick = kfPatientSickService.get(
						patient.getGid(), user.getTeamId(), 1);
				if (pSick != null) {
					String sickName = sickService.getFullSickName(pSick
							.getSid());
					data.put("sickId", pSick.getSid());
					data.put("sickName", sickName);
					if (pSick.getOperationL() != null) {
						data.put("leftOperationName", pSick.getOperationL()
								.getName());
						data.put("leftOperationGid", pSick.getOperationGidL());
					}
					if (pSick.getOperationR() != null) {
						data.put("rightOperationName", pSick.getOperationR()
								.getName());
						data.put("rightOperationGid", pSick.getOperationGidR());
					}
					for (int i = 1; i <= 2; i++) {// 1-左，2-右
						String ss = "";
						if (i == 1) {
							ss = "left";
						} else {
							ss = "right";
						}
						Map<String, String> scondition = new HashMap<String, String>();
						scondition.put("leg", i + "");
						scondition.put("patSickId", pSick.getId());
						scondition.put("kfPatientId", gid);
						List<Scheme> slist = schemeService.getConditonList(
								scondition, "", false, null);
						if (slist.size() != 0)
							data.put(ss + "SchemeName", slist.get(0).getName());
						Date operDate = i == 1 ? pSick.getOperLDate() : pSick
								.getOperRDate();
						data.put(ss + "DaysLater",
								ComUtil.daysBetweenDate(operDate, new Date()));
						data.put(ss + "OperDate", ComUtil.date2Str(operDate));

					}
				}
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

	// 查询患者信息
	public void queryPatientData() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			User user = userService.get(userId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			KfPatient patient = kfPatientService.get(gid);
			JSONObject data = new JSONObject();
			data.put("gid", patient.getGid());
			data.put("code", patient.getId());
			data.put("avatars", patient.getAvatars());
			data.put("local", patient.getLocal());
			Map<String, String> map = new HashMap<String, String>();
			map.put("pid", gid);
			map.put("teamGid", user.getTeamId());
			List<RdPatient> list = rdPatientService.getConditonList(map, "",
					false, null);
			data.put("name", patient.getName());
			data.put("age", patient.getAge());
			data.put("gender", patient.getGender());
			data.put("phone", patient.getPhone());
			if (list.size() != 0) {
				RdPatient record = list.get(0);
				data.put("caseGid", record.getGid());
				if (record.getVisitDate() != null) {
					data.put("visitDate", sdf.format(record.getVisitDate()));
				} else {
					data.put("visitDate", "");
				}
				data.put("relativesPhone", record.getRelativesPhone());
				data.put("patientNo", record.getPatientNo());
				data.put("bedNo", record.getBedNo());
				data.put("caseNo", record.getCaseNo());
				data.put("IDCardNo", record.getIDCardNo());
				data.put("address", record.getAddress());
				data.put("postCode", record.getPostcode());
				data.put("other", record.getOther());
			}

			if (user.getTeamId() != null || user.getTeamId() != "") {
				String teamIdString = user.getTeamId();
				KfPatientSick pSick = kfPatientSickService.get(
						patient.getGid(), user.getTeamId(), 1);
				if (pSick != null) {
					String sickName = sickService.getFullSickName(pSick
							.getSid());
					data.put("sickId", pSick.getSid());
					data.put("sickName", sickName);
					if (pSick.getOperationL() != null) {
						data.put("leftOperationName", pSick.getOperationL()
								.getName());
						data.put("leftOperationGid", pSick.getOperationGidL());
					}
					if (pSick.getOperationR() != null) {
						data.put("rightOperationName", pSick.getOperationR()
								.getName());
						data.put("rightOperationGid", pSick.getOperationGidR());
					}
					for (int i = 1; i <= 2; i++) {// 1-左，2-右
						String ss = "";
						if (i == 1) {
							ss = "left";
						} else {
							ss = "right";
						}
						Map<String, String> scondition = new HashMap<String, String>();
						scondition.put("leg", i + "");
						scondition.put("patSickId", pSick.getId());
						scondition.put("kfPatientId", gid);
						List<Scheme> slist = schemeService.getConditonList(
								scondition, "", false, null);
						if (slist.size() != 0)
							data.put(ss + "SchemeName", slist.get(0).getName());
						Date operDate = i == 1 ? pSick.getOperLDate() : pSick
								.getOperRDate();
						data.put(ss + "DaysLater",
								ComUtil.daysBetweenDate(operDate, new Date()));
						data.put(ss + "OperDate", ComUtil.date2Str(operDate));
						List<KfData> userData = kfDataService.getMotionList(
								gid, i + "", user.getTeamId());
						String last = "";
						JSONArray calone = new JSONArray();
						for (KfData object : userData) {
							Motion motion = motionService.get(object.getMgid());
							objNullException(motion,
									"动作gid为" + object.getMgid());
							if (last.equals(motion.getId())) {
								continue;
							}
							JSONObject calen = new JSONObject();
							JSONArray dataja = new JSONArray();

							List<Object[]> kfDataList = kfDataService
									.getDataByType(motion.getId(), 3 + "", i
											+ "", teamIdString, gid);
							if (kfDataList.size() != 0) {
								for (Object[] obj : kfDataList) {
									JSONObject kfd = new JSONObject();
									kfd.put("date", obj[0]);
									kfd.put("type", obj[1]);
									kfd.put("score", obj[2]);
									kfd.put("num", obj[3]);
									dataja.add(kfd);
								}
								calen.put("gid", motion.getId());

								last = motion.getId();
								StandardMotion motionStandard = stMotionService
										.get(motion.getStMotionId());
								calen.put("name", motionStandard.getName());
								calen.put("type", motionStandard.getType());
								calen.put("mid", motion.getId());
								calen.put("completeData", dataja);
								calone.add(calen);
							}
						}
						data.put(ss + "Data", calone);
					}
				}
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

	// 患者首页数据
	public void queryPatientHomePage() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);

			// 轮转列表
			Map<String, String> condition1 = new HashMap<String, String>();
			condition1.put("category", "患者");
			List<Announcement> list = announcementService.getConditonList(
					condition1, null, false, null);
			JSONArray aja = new JSONArray();
			for (Announcement announcement : list) {
				JSONObject jo = new JSONObject();
				jo.put("gid", announcement.getGid());
				jo.put("pic", announcement.getPic());
				jo.put("url", announcement.getUrl());
				jo.put("title", announcement.getTitle());
				aja.add(jo);
			}

			JSONArray dja = new JSONArray();
			JSONArray mja = new JSONArray();
			if (logined) {
				String userId = request.getHeader("userid");
				User user = userService.get(userId);
				if (StringUtils.isNoneBlank(user.getTeamId())) {
					// 数据列表
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					//最近一个月有数据的患者
					List<KfData> plist = kfDataService.getDate(
							user.getTeamId(),
							sdf.format(new Date(new Date().getTime()
									- new Long(30) * 24 * 3600 * 1000)));
					for (KfData kfData : plist) {
						List<Object[]> objList = kfPatientService
								.queryPatientOperationInfo(kfData.getPid(),
										kfData.getTeamGid());
						for (Object[] patientObj : objList) {
							JSONObject jo = new JSONObject();
							if (objList.size() > 0) {
								// 患者基本信息
								jo.put("gid", kfData.getPid());
								jo.put("code", patientObj[0]);
								jo.put("name", patientObj[2]);
								jo.put("avatars", patientObj[3]);
								jo.put("gender", patientObj[4]);
								jo.put("age", patientObj[5]);
								Date operDate1 = (Date) patientObj[8];
								Date operDate2 = (Date) patientObj[9];
								jo.put("leftOperationName", patientObj[10]);
								jo.put("rightOperationName", patientObj[11]);
								jo.put("sick", patientObj[12]);
								String days1 = "";
								String days2 = "";
								if (operDate1 != null) {
									days1 = ComUtil.daysBetweenDate(operDate1,
											new Date()) + "";
								}
								if (operDate2 != null) {
									days2 = ComUtil.daysBetweenDate(operDate2,
											new Date()) + "";
								}
								jo.put("operDistL", days1);
								jo.put("operDistR", days2);
								jo.put("scoreDate", kfData.getDate());
								// 当天分数
								int leftScore = 0, rightScore = 0;
								int leg = (int) patientObj[15];
								if (leg == 1) {
									Map<String, String> map1 = this.getScore(
											(String) patientObj[13],kfData.getDate());
									leftScore = Integer.parseInt(map1
											.get("score"));
								} else if (leg == 2) {
									Map<String, String> map2 = this.getScore(
											(String) patientObj[13],kfData.getDate());
									rightScore = Integer.parseInt(map2
											.get("score"));
								}
								if (leftScore == 0) {
									jo.put("score", rightScore);
								} else if (rightScore == 0) {
									jo.put("score", leftScore);
								} else {
									jo.put("score",
											leftScore < rightScore ? leftScore
													: rightScore);
								}
							}
							dja.add(jo);
						}
					}
					// 病例列表
					Map<String, String> condition = new HashMap<String, String>();
					condition.put("teamGid", user.getTeamId());
					List<RdPatient> myList = rdPatientService.getConditonList(
							condition, "updateTime", true, null);
					for (RdPatient rdPatient : myList) {
						JSONObject jo = new JSONObject();
						jo.put("gid", rdPatient.getPid());
						List<Object[]> objList = kfPatientService
								.queryPatientOperationInfo(rdPatient.getPid(),
										user.getTeamId());
						for (Object[] patientObj : objList) {
							// 患者基本信息
							jo.put("code", patientObj[0]);
							jo.put("name", patientObj[2]);
							jo.put("avatars", patientObj[3]);
							jo.put("gender", patientObj[4]);
							jo.put("age", patientObj[5]);
							jo.put("sick", patientObj[10]);
							Date operDate1 = (Date) patientObj[8];
							Date operDate2 = (Date) patientObj[9];
							jo.put("leftOperationName", patientObj[10]);
							jo.put("rightOperationName", patientObj[11]);
							jo.put("sick", patientObj[12]);
							String days1 = "";
							String days2 = "";
							if (operDate1 != null) {
								days1 = ComUtil.daysBetweenDate(operDate1,
										new Date()) + "";
							}
							if (operDate2 != null) {
								days2 = ComUtil.daysBetweenDate(operDate2,
										new Date()) + "";
							}
							jo.put("operDistL", days1);
							jo.put("operDistR", days2);
							// 当天分数
							int leftScore = 0, rightScore = 0;
							int leg = (int) patientObj[15];
							
							SimpleDateFormat sdf2 = new SimpleDateFormat(
									"yyyy-MM-dd");
							if (leg == 1) {
								Map<String, String> map1 = this.getScore(
										(String) patientObj[13],sdf2.format(new Date()));
								leftScore = Integer.parseInt(map1.get("score"));
							} else {
								Map<String, String> map2 = this.getScore(
										(String) patientObj[13],sdf2.format(new Date()));
								rightScore = Integer
										.parseInt(map2.get("score"));
							}
							if (leftScore == 0) {
								jo.put("score", rightScore);
							} else if (rightScore == 0) {
								jo.put("score", leftScore);
							} else {
								jo.put("score",
										leftScore < rightScore ? leftScore
												: rightScore);
							}
						}
						mja.add(jo);
					}
				}
			}

			JSONObject res = new JSONObject();
			res.put("advertisementList", aja);
			res.put("patientDataList", dja);
			res.put("patientCaseList", mja);

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

	public Map<String, String> getScore(String schemeGid,String dataDate) {
		int sumScore = 0;
		int maxAngle = 0;
		//逻辑存在问题，需完善
		if (!TextUtils.isEmpty(schemeGid)) {
			List<Object[]> dataList = schemeService.getSchemeData(schemeGid,dataDate);
			int angleScore = 0, heightScore = 0, timeScore=0;
			int exist = 0;
			for (Object[] kfData : dataList) {
				int type = kfData[12] != null ? Integer
						.parseInt((String) kfData[12]) : 0;
				int group = kfData[7] != null ? Integer
						.parseInt((String) kfData[7]) : 0;
				int num = kfData[8] != null ? Integer
						.parseInt((String) kfData[8]) : 0;
				int tempScore = kfData[9] != null ? Integer
						.parseInt((String) kfData[9]) : 0;  // 分数
				int tempAngle = kfData[10] != null ? Integer
						.parseInt((String) kfData[10]) : 0; //角度或训练次数/时长
				if (type == 1) { //角度类
					angleScore += tempScore/group;
					if(tempAngle>maxAngle)
						maxAngle = tempAngle;
				} else if (type == 2) { //高度类
					heightScore += tempScore/(group*num);
				}else if (type == 3) { //时长类，如持续联系30分钟
					exist = 1;
					timeScore = tempAngle * 100 / (group*num * 60);
				}
			}
			sumScore = ((angleScore<100?angleScore:100)+(heightScore<100?heightScore:100)+(timeScore<100?timeScore:100))/(exist==0?2:3);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("score", sumScore + "");
		map.put("angle", maxAngle + "");
		return map;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSickGid() {
		return sickGid;
	}

	public void setSickGid(String sickGid) {
		this.sickGid = sickGid;
	}

	public String getLeftOperationGid() {
		return leftOperationGid;
	}

	public void setLeftOperationGid(String leftOperationGid) {
		this.leftOperationGid = leftOperationGid;
	}

	public String getRightOperationGid() {
		return rightOperationGid;
	}

	public void setRightOperationGid(String rightOperationGid) {
		this.rightOperationGid = rightOperationGid;
	}

	public String getLeftOperationDate() {
		return leftOperationDate;
	}

	public void setLeftOperationDate(String leftOperationDate) {
		this.leftOperationDate = leftOperationDate;
	}

	public String getRightOperationDate() {
		return rightOperationDate;
	}

	public void setRightOperationDate(String rightOperationDate) {
		this.rightOperationDate = rightOperationDate;
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

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
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

}

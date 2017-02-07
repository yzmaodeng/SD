package com.sd.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sd.dao.KfDataDao;
import com.sd.service.KfDataResultService;
import com.sd.service.KfDataService;
import com.sd.service.KfPatientSickService;
import com.sd.service.StandardMotionService;
import com.sd.service.impl.KfDataServiceImpl;
import com.sd.vo.KfData;
import com.sd.vo.KfPatientDataResult;

/**
 * 1、创建一个执行任务的类。要做到这一点，你需要从Spring的QuartzJobBean中派生子类
 */
public class QuartzJobBeanImpl extends QuartzJobBean {
	@Resource
	private KfDataService kfDataService;
	@Resource
	private StandardMotionService stMotionService;
	@Resource
	private KfPatientSickService kfPatientSickService;
	@Resource
	private KfDataResultService kfDataResultService;
	@Resource private KfDataDao kfDataDao;
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// 1、獲得當前記錄時間最大達一條記錄
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 獲得當天的數據 
		KfDataServiceImpl kfDataServiceImpl = new KfDataServiceImpl();
		List<KfData> userData = kfDataServiceImpl.queryTodayData();
		// 备胎
		KfData btkfData = new KfData();
		HashMap<String, ArrayList<KfData>> hashMap = new HashMap<String, ArrayList<KfData>>();
		ArrayList<KfData> nowList = null;
		if (userData.size() != 0) {
			for (int i = 0; i < userData.size(); i++) {
				// 当前的对象
				KfData kfData = userData.get(i);
				if (i == 0) {
					nowList = new ArrayList<KfData>();
					nowList.add(kfData);
					btkfData = userData.get(i);
					continue;
				}
				String nowString = kfData.getLeg() + kfData.getTeamGid()
						+ kfData.getPid() + kfData.getMgid();
				String beforString = btkfData.getLeg() + btkfData.getTeamGid()
						+ btkfData.getPid() + btkfData.getMgid();
				if (nowString.equals(beforString)) {
					nowList.add(kfData);
				} else {
					hashMap.put((i - 1) + "", nowList);
					nowList = new ArrayList<KfData>();
					nowList.add(kfData);

				}
				btkfData = userData.get(i);

			}

			hashMap.put("last", nowList);
		}

		Set<String> keySet = hashMap.keySet();
		for (String string : keySet) {
			ArrayList<KfData> arrayList = hashMap.get(string);
			process(arrayList);
		}

	}

	private void process(ArrayList<KfData> userData) {
		KfPatientDataResult kfPatientDataResult = new KfPatientDataResult();
		try {
			KfData kfData = userData.get(0);
			String gid = kfData.getPid();
			String teamGid = kfData.getTeamGid();
			String date = kfData.getDate();
			String leg = kfData.getLeg();
			String mgid = kfData.getMgid();
			String type = kfData.getType();
			if (gid==null||teamGid==null||date==null||leg==null||mgid==null||type==null) {
				throw new CustomException(gid+"对象的属性字段为null");
			}
			int totoleNum = userData.size();
			kfPatientDataResult.setGid(ComUtil.getuuid());
			kfPatientDataResult.setDate(date);
			kfPatientDataResult.setLeg(leg);
			kfPatientDataResult.setType(type);
			SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int i = 0, max = 0, array = 0, excellent = 80, good = 60, qualified = 0, min = 0;
//		String gradeString = "";

			// //
			for (KfData myData : userData) {
				if ("1".equals(myData.getType())) {
					// 角度
					int angle = Integer.parseInt(myData.getNum());
					array += angle;
					// 最大
					if (angle > max) {
						max = angle;
					}
					// 最小
					if (angle < min) {
						min = angle;
					}
//				if (myData.getMotion().getStMotion().getId().equals("8")) {
//					if (angle <= 8) {
//						gradeString = "1";
//						qualified++;
//					} else if (angle <= 15) {
//						gradeString = "2";
//						qualified++;
//					} else {
//						gradeString = "3";
//					}
//				} else {
//					KfPatientSick pSick = kfPatientSickService.get(gid,
//							teamGid, 1);
//					int days = 0;
//					if ("1".equals("leg")) {
//						days = Integer.parseInt(ComUtil.daysBetweenDate(
//								pSick.getOperLDate(), new Date())
//								+ "");
//					} else {
//						days = Integer.parseInt(ComUtil.daysBetweenDate(
//								pSick.getOperRDate(), new Date())
//								+ "");
//					}
//
//					Set<Target> t = myData.getMotion().getTargetSet();
//					for (Target target : t) {
//						if (Integer.parseInt(target.getBdays()) <= days
//								&& days <= Integer.parseInt(target.getEdays())) {
//							excellent = Integer.parseInt(target.getNum()) - 10;
//							good = Integer.parseInt(target.getNum()) - 30;
//						}
//					}
//					if (angle >= excellent) {
//						gradeString = "1";
//						qualified++;
//					} else if (angle >= good) {
//						gradeString = "2";
//						qualified++;
//					} else {
//						gradeString = "3";
//					}
//				}
//				
				} else {
					// 肌力
					int score = Integer.parseInt(myData.getScore());
					// 总分
					array += score;
					// 最大分
					if (score > max) {
						max = score;
					}
					// 最小分
					if (score < min) {
						min = score;
					}

//				if (score >= excellent) {
//					gradeString = "1";
//					qualified++;
//				} else if (score >= good) {
//					gradeString = "2";
//					qualified++;
//				} else {
//					gradeString = "3";
//				}
					if (max > 100)
						max = 100;
				}

			}
			// 平均分
			double avg = (double) array / (double) totoleNum;
//		String qualify = (int) ((double) qualified / i * 100) + "";
			if ("1".equals(type)) {
				kfPatientDataResult.setAvgNum(avg);
				kfPatientDataResult.setMaxNum(max);
				kfPatientDataResult.setMinNum(min);
				
			}else{
				kfPatientDataResult.setAvgScore(avg);
				kfPatientDataResult.setMaxScore(max);
				kfPatientDataResult.setMinScore(min);
			}
			
//		kfPatientDataResult.setGrade(gradeString);
//		kfPatientDataResult.setQualify(qualify);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		kfDataResultService.save(kfPatientDataResult);

	}

}

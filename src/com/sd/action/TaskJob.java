package com.sd.action;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sd.service.KfDataService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.vo.BaseVo;
import com.sd.vo.KfData;
import com.sd.vo.KfPatientDataResult;

public class TaskJob extends QuartzJobBean {

	private KfDataService kfDataService;
	private SessionFactory sessionFactory;
	private static final Logger log = Logger.getLogger(TaskJob.class);

	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		System.out.println("dddddd");
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.getResource(getSessionFactory());
		boolean existingTransaction = sessionHolder != null;
		Session session;
		if (existingTransaction) {
			log.error("Found thread-bound Session for HibernateInterceptor");
			session = sessionHolder.getSession();
		} else {
			session = openSession();
			TransactionSynchronizationManager.bindResource(getSessionFactory(),
					new SessionHolder(session));
		}
		try {
			queryTodayData(session);

		} catch (HibernateException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (existingTransaction) {
				log.debug("Not closing pre-bound Hibernate Session after TransactionalQuartzTask");
			} else {
				SessionFactoryUtils.closeSession(session);
				TransactionSynchronizationManager
						.unbindResource(getSessionFactory());
			}
		}

	}

	public void queryTodayData(Session session) {
		String Sql = "select * from tkfpatient_data where date(tpd_date) = curdate() and  teamGid<>'' and teamGid is not null  order by leg desc,teamGid desc,pid desc,tpd_mgid desc";
//		String sqlString = "select * from tkfpatient_data where tpd_date = '2016-07-25' and  teamGid<>'' and teamGid is not null  order by leg desc,teamGid desc,pid desc,tpd_mgid desc";
		List<KfData> userData = session.createSQLQuery(Sql)
				.addEntity(KfData.class).list();
		System.out.println(userData.size());
		if (userData.size() == 0) {
			return;
		}
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
			process(arrayList, session);
		}

	}

	private void process(ArrayList<KfData> userData, Session session) {
		KfPatientDataResult kfPatientDataResult = new KfPatientDataResult();
		try {
			KfData kfData = userData.get(0);
			String gid = kfData.getPid();
			String teamGid = kfData.getTeamGid();
			String date = kfData.getDate();
			String leg = kfData.getLeg();
			String mgid = kfData.getMgid();
			String type = kfData.getType();
			if (gid == null || teamGid == null || date == null || leg == null
					|| mgid == null || type == null) {
				throw new CustomException(gid + "对象的属性字段为null");
			}
			int totoleNum = userData.size();
			kfPatientDataResult.setGid(ComUtil.getuuid());
			kfPatientDataResult.setDate(date);
			kfPatientDataResult.setLeg(leg);
			kfPatientDataResult.setType(type);
			SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int i = 0, max = 0, array = 0, aMax = 0, aMin = 0, aTotle = 0, min = 0;
			ArrayList<Integer> scoreList = new ArrayList<Integer>();
			ArrayList<Integer> numList = new ArrayList<Integer>();
			for (KfData myData : userData) {
				int angle = Integer.parseInt(myData.getNum());
				aTotle += angle;
				numList.add(angle);
				int score = Integer.parseInt(myData.getScore());
				array += score;
				if (score > max) {
					max = score;
				}
				scoreList.add(score);
			}
			aMax = Collections.max(numList);
			aMin = Collections.min(numList);
			max = Collections.max(scoreList);
			min = Collections.min(scoreList);
			double aAvg = (double) aTotle / (double) totoleNum;
			double avg = (double) array / (double) totoleNum;
			kfPatientDataResult.setAvgNum(aAvg);
			kfPatientDataResult.setMaxNum(aMax);
			kfPatientDataResult.setMinNum(aMin);
			kfPatientDataResult.setAvgScore(avg);
			kfPatientDataResult.setMaxScore(max);
			kfPatientDataResult.setMinScore(min);
			kfPatientDataResult.setSumNum(aTotle);
			kfPatientDataResult.setSumScore(array);
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO tkfpatientResult VALUES").append("(")
					.append("null").append(",'").append(ComUtil.getuuid())
					.append("','").append(gid).append("','").append(teamGid)
					.append("','").append(date).append("','").append(mgid)
					.append("','").append(leg).append("','").append(type)
					.append("','").append(aMax).append("','").append(aMin)
					.append("','").append(aAvg).append("','").append(aTotle)
					.append("','").append(max).append("','").append(min)
					.append("','").append(avg).append("','").append(array)
					.append("')");
			session.createSQLQuery(sql.toString()).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected Session openSession() throws DataAccessResourceFailureException {
		try {
			Session session = getSessionFactory().openSession();
			session.setFlushMode(FlushMode.MANUAL);
			return session;
		} catch (HibernateException ex) {
			throw new DataAccessResourceFailureException(
					"Could not open Hibernate Session", ex);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setKfDataService(KfDataService kfDataService) {
		this.kfDataService = kfDataService;
	}

}

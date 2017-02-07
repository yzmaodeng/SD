package com.sd.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.ConfDao;
import com.sd.util.ComUtil;
import com.sd.vo.Conf;
import com.sd.vo.Topic;

@Repository
public class ConfDaoImpl extends BaseDaoImpl<Conf, String> implements ConfDao{
	@SuppressWarnings("unchecked")
	public List<Conf> getByMonth(Date time) {
		Criteria criteria = getSession().createCriteria(Conf.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		criteria.add(Restrictions.gt("dateBegin", time));	
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1);   
		Date time2=calendar.getTime();
		criteria.add(Restrictions.lt("dateBegin",time2));
		criteria.add(Restrictions.sqlRestriction(" 1=1 order by cf_date asc"));
		return criteria.list();
	}
	/**
	 * 查询会议列表
	 * @param dateBegin
	 * @param selectType
	 * @param count
	 * @param isFav
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Conf> getConfList(String dateBegin, String selectType,
			String count, String isFav, String userId) {
		Criteria criteria = getSession().createCriteria(Conf.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		String order = " desc ";
		if ("1".equals(selectType))
			order = "";
		if (StringUtils.isNotBlank(dateBegin)){
			if ("1".equals(selectType)){
				criteria.add(Restrictions.gt("dateBegin", ComUtil.str2Time(dateBegin)));
			} else if ("2".equals(selectType)){
				criteria.add(Restrictions.lt("dateBegin", ComUtil.str2Time(dateBegin)));
			}else if("0".equals(selectType)){
				criteria.add(Restrictions.eq("dateBegin", ComUtil.str2Time(dateBegin)));
			}
		} else {
			if ("2".equals(isFav))
				criteria.add(Restrictions.lt("dateEnd", new Date()));
			else
				criteria.add(Restrictions.gt("dateEnd", new Date()));
		}
		if ("1".equals(isFav)){
			criteria.add(Restrictions.sqlRestriction(" EXISTS (select * from tfav c " +
					"where c.fav_isdel='1' and this_.cf_gid = c.fav_gid" +
					" and c.fav_usergid='"+userId+"')"));
		}
		if (StringUtils.isBlank(count)){
			count = "10";// 默认10条
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by cf_date " + order + ",cf_CreateTime limit " + count));
		}else if(count.equals("-1")){
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by cf_date " + order+",cf_CreateTime"));
		}else{
			criteria.add(Restrictions.sqlRestriction(" 1=1 order by cf_date " + order + ",cf_CreateTime limit " + count));
		}
		return criteria.list();
	}

	/**
	 * 议程查询
	 * @param gid
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Topic> getTopicList(String gid, String level) {
		Criteria criteria = getSession().createCriteria(Topic.class);
		criteria.add(Restrictions.eq("isdel", "1"));
		if ("1".equals(level)){
			criteria.add(Restrictions.eq("confGid", gid));
			criteria.add(Restrictions.eq("level", level));
		}else if ("2".equals(level)){
			criteria.add(Restrictions.eq("parentGid", gid));
			criteria.add(Restrictions.eq("level", level));
		}else{
			criteria.add(Restrictions.eq("gid", gid));
		}
		
		criteria.add(Restrictions.sqlRestriction(" 1=1 order by tpc_datetime_begin "));
		return criteria.list();
	}
	@Override
	public List<Conf> getConfListFactor(String userId, String count,
			String isFav, String status, String city,String date) {
		Criteria criteria = getSession().createCriteria(Conf. class);
        criteria.add(Restrictions. eq("isdel", "1"));
        if(city.endsWith("市")){
    		city=city.substring(0,city.length()-1);
    	}
        if(StringUtils.isBlank(userId)){
            if (StringUtils.isNotBlank(city)&&!"0".equals(city)){
            	
                criteria.add(Restrictions. like("local",city,MatchMode. ANYWHERE));
           }
            if (StringUtils.isNotBlank(status)&&!"0".equals(status)){
                 Date now = new Date();
                  if ("1" .equals(status)){
                       criteria.add(Restrictions. gt("dateBegin", now));
                 } else if ("2" .equals(status)){
                       criteria.add(Restrictions. le("dateBegin", now));
                       criteria.add(Restrictions. ge("dateEnd", now));
                 } else if ("3" .equals(status)){
                       criteria.add(Restrictions. lt("dateEnd", now));
                 }
           }
        	
        }else{
            if (StringUtils.isNotBlank(city)&&!"0".equals(city)){
                criteria.add(Restrictions. like("local",city,MatchMode. ANYWHERE));
          }
           if (StringUtils.isNotBlank(status)&&!"0".equals(status)){
                Date now = new Date();
                 if ("1" .equals(status)){
                      criteria.add(Restrictions. gt("dateBegin", now));
                } else if ("2" .equals(status)){
                      criteria.add(Restrictions. le("dateBegin", now));
                      criteria.add(Restrictions. ge("dateEnd", now));
                } else if ("3" .equals(status)){
                      criteria.add(Restrictions. lt("dateEnd", now));
                }
          }
           if (StringUtils.isNotBlank(isFav)&&!"0".equals(isFav)){
        	   if ("1".equals(isFav)){
        		   criteria.add(Restrictions.sqlRestriction(" EXISTS (select * from tfav c " +
        				   "where c.fav_isdel='1' and this_.cf_gid = c.fav_gid" +
        				   " and c.fav_usergid='"+userId+"')"));
        	   }
        	   if ("2".equals(isFav)){
        		   criteria.add(Restrictions.sqlRestriction(" not EXISTS (select * from tfav c " +
        				   "where c.fav_isdel='1' and this_.cf_gid = c.fav_gid" +
        				   " and c.fav_usergid='"+userId+"')"));
        	   }
         }
        }
        count="10";
        if (StringUtils.isNotBlank(date)&&!("".equals(date))){
			//创建的时间
			criteria.add(Restrictions.gt("dateBegin", ComUtil.str2Time(date)));
		}
        criteria.add(Restrictions.sqlRestriction(" 1=1 order by cf_date asc limit " + count));
         return criteria.list();

	}
}

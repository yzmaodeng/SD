
package com.sd.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sd.dao.TextBookDao;
import com.sd.vo.Textbook;
@Repository
public class TextBookDaoImpl extends BaseDaoImpl<Textbook,String> implements TextBookDao {
	@SuppressWarnings("unchecked")
	public List<Textbook> query(int selected,String currId,String count,String keyword,String category,String isFav,String userId){
		Criteria criteria = getSession().createCriteria(Textbook.class);
		criteria.add(Restrictions.eq("tbIsdel", "1"));
		if(category!=null&&!category.equals("0"))criteria.add(Restrictions.eq("tbCategory", category));
		if(selected==1){
			criteria.add(Restrictions.eq("tbSelected", "1"));
			criteria.add(Restrictions.sqlRestriction("1=1 order by tb_createtime desc,tb_id desc limit 0,5"));
		}else{
			if(keyword!=null){
				Criterion cron = Restrictions.like("tbTitle",'%'+keyword+'%');
				Criterion author = Restrictions.like("tbAuthor",'%'+keyword+'%');
				criteria.add(Restrictions.or(cron,Restrictions.or(author)));
			}else if(isFav!=null){
				criteria.add(Restrictions.sqlRestriction(" EXISTS (select * from tfav c " +
						"where c.fav_isdel='1' and this_.tb_gid = c.fav_gid and c.fav_usergid='"+userId+"')"));
			}else{
				criteria.add(Restrictions.eq("tbSelected", "0"));
			}
			if(currId!=null&&!("".equals(currId)))criteria.add(Restrictions.sqlRestriction("tb_createtime <(select tb_createtime from ttextbook tb where tb.tb_id="+currId+")"));
			if(count!=null)criteria.add(Restrictions.sqlRestriction("1=1 order by tb_createtime desc,tb_id desc limit 0,"+count));
		}
		return criteria.list();
	}
}

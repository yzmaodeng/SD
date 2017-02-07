package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.TextBookDao;
import com.sd.service.TextBookService;
import com.sd.vo.Textbook;
@Service
public class TextBookServiceImpl extends BaseServiceImpl<Textbook,String> implements TextBookService {
	@Resource
	public void setBaseDao(TextBookDao textBookDao) {
		super.setBaseDao(textBookDao);
	}
	@Resource private TextBookDao textBookDao;
	
	public List<Textbook> query(int selected,String currId,String count,String keyword,String category,String isFav,String userId){
		return textBookDao.query(selected, currId, count, keyword, category,isFav,userId);
	}

	@Override
	public List<Object[]> getClassificationSelectionAll(int count,String s,String orderString,String category) {
		  int length = category.length();
		  String cString=length==0?"":(length==1?"LEFT(category,1)='"+category+"' AND":"category='"+category+"' AND");
	             StringBuffer sql = new StringBuffer();
			     sql.append("SELECT* FROM(")
				.append("(SELECT gid, createTime,'4' AS ty ,watchNumber FROM `subject`")
				.append(" WHERE ").append(cString)
				.append(" title LIKE  '%")
				.append(s).append("%'ORDER BY ")
				.append("2".equals(orderString)?"watchNumber":"createTime")
				.append(" DESC LIMIT 0,").append(count*20).append(")")
				.append("UNION ALL(SELECT tb_gid AS gid,tb_createtime AS createTime,'3' AS ty ,readNumber AS   watchNumber  FROM ttextbook")
				.append(" WHERE ")
				.append(cString)
				.append(" tb_title LIKE '%")
				.append(s).append("%'ORDER BY ")
				.append("2".equals(orderString)?"watchNumber":"createTime")
				.append(" DESC LIMIT 0,").append(count*20).append(")")
				.append("UNION ALL(SELECT gid,createTime,'1' AS ty,watchNumber FROM course ")
				.append(" WHERE ").append(cString)
				.append(" title LIKE  '%")
				.append(s).append("%'ORDER BY ")
				.append("2".equals(orderString)?"watchNumber":"createTime")
				.append(" DESC LIMIT 0,").append(count*20).append(")")
//				.append("UNION ALL (SELECT pst_Gid AS gid,pst_CreateTime AS createTime,4 AS ty FROM tpost ").append(" WHERE title LIKE '%").append(s).append("%'ORDER BY pst_CreateTime DESC LIMIT 0,").append(count*20).append(")")
			    .append("UNION ALL (SELECT gid,createTime,'2' AS ty,watchNumber FROM courseVideo").append(" WHERE ")
			    .append(cString)
			    .append(" title LIKE  '%").append(s).append("%'ORDER BY ")
			    .append("2".equals(orderString)?"watchNumber":"createTime")
			    .append(" DESC LIMIT 0,")
			    .append(count*20)
			    .append(")")
			    .append(") AS d ORDER BY")
			    .append("2".equals(orderString)?" watchNumber ":" createTime ")
			    .append(" DESC LIMIT ")
			    .append(count*20-20).append(",20;");
		        return textBookDao.getListBySql(sql.toString());
	}

	@Override
	public List<Object[]> classificationSingleSelection(String type,int count,String s, String orderString, String category) {
		 StringBuffer sql = new StringBuffer();
		  int length = category.length();
		  String cString=length==0?"":(length==1?"LEFT(category,1)='"+category+"' AND":"category='"+category+"' AND");
		 if ("1".equals(type)) {
		     sql.append("SELECT* FROM(")
					.append("(SELECT gid,createTime,'1' AS ty,watchNumber FROM course ")
					.append(" WHERE ").append(cString)
					.append(" title LIKE  '%")
					.append(s).append("%'ORDER BY ")
					.append("2".equals(orderString)?"watchNumber":"createTime")
					.append(" DESC LIMIT 0,").append(count*20)
					.append(")")
				    .append("UNION ALL (SELECT gid,createTime,'2' AS ty,watchNumber FROM courseVideo")
				    .append(" WHERE ").append(cString)
				    .append(" title LIKE  '%").append(s).append("%'ORDER BY ")
				    .append("2".equals(orderString)?"watchNumber":"createTime")
				    .append(" DESC LIMIT 0,").append(count*20)
				    .append(")")
				    .append(") AS d ORDER BY")
				    .append("2".equals(orderString)?" watchNumber ":" createTime ")
				    .append(" DESC LIMIT ")
				    .append(count*20-20)
				    .append(",20;");
			
		}else if("2".equals(type)){
			sql.append("SELECT gid, createTime,'4' AS ty ,watchNumber FROM `subject`")
			.append(" WHERE ").append(cString)
			.append(" title LIKE  '%")
			.append(s).append("%'ORDER BY ")
			.append("2".equals(orderString)?"watchNumber":"createTime")
			.append(" DESC LIMIT 0,").append(count*20).append(";");
			
		}else if("3".equals(type)){
			sql.append("SELECT tb_gid AS gid,tb_createtime AS createTime,'3' AS ty ,readNumber AS   watchNumber  FROM ttextbook")
			.append(" WHERE ").append(cString)
			.append(" tb_title LIKE '%")
			.append(s).append("%'ORDER BY ")
			.append("2".equals(orderString)?"watchNumber":"createTime")
			.append(" DESC LIMIT 0,").append(count*20).append(";");
			
		}
		return textBookDao.getListBySql(sql.toString());
	}

	@Override
	public List<Object[]> queryCategory() {
		
		String sqlString="SELECT id, gid, TYPE, minitype FROM category";
		 
		return textBookDao.getListBySql(sqlString);
	}

	@Override
	public List<Textbook> queryHomeTexts() {
		String string="SELECT 	* FROM ttextbook  ORDER BY tb_createtime DESC LIMIT 5";
		return textBookDao.getVoListBySql(string);
		
	}
}

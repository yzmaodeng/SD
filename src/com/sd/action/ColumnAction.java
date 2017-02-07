package com.sd.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.sd.service.AcademyFileService;
import com.sd.service.AcademyService;
import com.sd.util.CustomException;
import com.sd.util.MyPage;
import com.sd.vo.ColumnContent;
import com.sd.vo.Columns;
import com.sd.vo.Coupon;
import com.sd.vo.OrderInfo;

public class ColumnAction extends BaseAction {
	private Logger logger = Logger.getLogger(this.getClass());
	private String userId;
	@Resource
	private AcademyFileService academyFileService;
	public String getUserId() {
		return userId;
	}
	private Integer pageNum;
    

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Resource
	private AcademyService academyService;
	private String gid;


	
	public void queryColumn() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();

			Columns columns=columnsService.getById(gid);
			if (columns==null) {
				throw new CustomException("专栏不存在");
			}
			JSONObject jsonObject = new JSONObject();
			JSONArray ccja = new JSONArray();
			jsonObject.put("intruduction", columns.getIntruduction());
			jsonObject.put("notice", columns.getNotice());
			jsonObject.put("author", columns.getAuthor());
			jsonObject.put("price", columns.getPrice());
			jsonObject.put("subscriberNum", columns.getSubscriberNum());
			jsonObject.put("title", columns.getTitle());
			jsonObject.put("pic", columns.getPic());
			jsonObject.put("suitablecrowd", columns.getSuitablecrowd());
			List<ColumnContent> ColumnContents=columnContentService.getByParentID(columns.getGid());
			for (ColumnContent columnContent : ColumnContents) {
				JSONObject cObject = new JSONObject();
				Date now = new Date();
				long i=now.getTime()-columnContent.getCreatetime().getTime();
				if (i<0) {
					throw new CustomException(columnContent.getTitle()+"时间设置不正确");
				} else {
                    Long pLong= i/1000*60*60;
                    cObject.put("updateTime", pLong);
				}
				cObject.put("title", columnContent.getTitle());
				cObject.put("ISSN", columnContent.getIssn());
				cObject.put("introduction", columnContent.getIntroduction());
				cObject.put("gid", columnContent.getGid());
				cObject.put("url", columnContent.getUrl());
				cObject.put("pic", columnContent.getPic());
				cObject.put("parentGid", columnContent.getParentgid());
				ccja.add(cObject);
			}
			
			jsonObject.put("ColumnContents", ccja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", jsonObject);
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

	public void queryColumnContentList(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			JSONArray ccja = new JSONArray();
			MyPage myPage = new MyPage();
			List<Object> max=new ArrayList<Object>();
			max.add(20);
			myPage.setPage(pageNum);
			myPage.setRows(max);
			Map<String, String> condition1 = new HashMap<String, String>();
			condition1.put("parentgid", gid);
			List<ColumnContent> ColumnContents=columnContentService.getConditonList(
					condition1, "createtime", orderBy, myPage);
			for (ColumnContent columnContent : ColumnContents) {
				JSONObject cObject = new JSONObject();
				Date now = new Date();
				long i=now.getTime()-columnContent.getCreatetime().getTime();
				if (i<0) {
					throw new CustomException(columnContent.getTitle()+"时间设置不正确");
				} else {
                    Long pLong= i/1000*60*60;
                    cObject.put("updateTime", pLong);
				}
				cObject.put("title", columnContent.getTitle());
				cObject.put("ISSN", columnContent.getIssn());
				cObject.put("introduction", columnContent.getIntroduction());
				cObject.put("gid", columnContent.getGid());
				cObject.put("url", columnContent.getUrl());
				cObject.put("pic", columnContent.getPic());
				cObject.put("parentGid", columnContent.getParentgid());
				ccja.add(cObject);
			}
			
		
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", ccja);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONArray());
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());
		
	}
	public void queryColumnList() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
			MyPage myPage = new MyPage();
			List<Object> max=new ArrayList<Object>();
			max.add(20);
			myPage.setPage(pageNum);
			myPage.setRows(max);
			Map<String, String> condition1 = new HashMap<String, String>();
			List<Columns> columnss = columnsService.getConditonList(
					condition1, "createtime", true, myPage);
			JSONArray uja = new JSONArray();
			for (Columns columns : columnss) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("purchase", false);
				if (logined) {
					List<OrderInfo> orderInfos=orderInfoService.getByUserIdAcademyId(userId,columns.getGid());
					if (orderInfos.size()!=0) {
						jsonObject.put("purchase", true);
					}
				}
				jsonObject.put("intruduction", columns.getIntruduction());
				jsonObject.put("notice", columns.getNotice());
				jsonObject.put("author", columns.getAuthor());
				jsonObject.put("price", columns.getPrice());
				jsonObject.put("subscriberNum", columns.getSubscriberNum());
				jsonObject.put("title", columns.getTitle());
				jsonObject.put("pic", columns.getPic());
				jsonObject.put("suitablecrowd", columns.getSuitablecrowd());
				uja.add(jsonObject);
			}
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", uja);
		
		} catch (Exception e) {
			logger.error("", e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONArray());
		}
		putDataOut(ret.toString());

	}

	

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTelephoneNum() {
		return telephoneNum;
	}

	public void setTelephoneNum(String telephoneNum) {
		this.telephoneNum = telephoneNum;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	private String location;
	private String telephoneNum;
	private String mail;
	private String pageCount;
	private String pageIndex;
	private String type;
	private String detail;
	private String price;
	private String title;
	private String company;
	private String companyNature;
	private String identification;
	private boolean orderBy;

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public AcademyFileService getAcademyFileService() {
		return academyFileService;
	}

	public void setAcademyFileService(AcademyFileService academyFileService) {
		this.academyFileService = academyFileService;
	}

	public AcademyService getAcademyService() {
		return academyService;
	}

	public void setAcademyService(AcademyService academyService) {
		this.academyService = academyService;
	}

	public boolean isOrderBy() {
		return orderBy;
	}

	public void setOrderBy(boolean orderBy) {
		this.orderBy = orderBy;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyNature() {
		return companyNature;
	}

	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}

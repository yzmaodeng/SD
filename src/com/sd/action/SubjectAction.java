/*
 * 专题接口
 */
package com.sd.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.sd.service.CourseVideoService;
import com.sd.service.SubjectService;
import com.sd.service.TextBookService;
import com.sd.util.ComUtil;
import com.sd.util.MyPage;
import com.sd.vo.Course;
import com.sd.vo.CourseVideo;
import com.sd.vo.Subject;
import com.sd.vo.Textbook;

public class SubjectAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private SubjectService subjectService;
	@Resource
	private TextBookService textBookService;
	@Resource
	private CourseVideoService videoService;

	private String subjectGid;
	private Integer pageNum;
    

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 获得专题
	 * @param pageNum
	 * @param type 排序类型
	 */
	public void querySubjectList() {
		JSONObject ret = new JSONObject();
		try {
			MyPage myPage = new MyPage();
			List<Object> max=new ArrayList<Object>();
			max.add(20);
			myPage.setPage(pageNum);
			myPage.setRows(max);
			Map<String, String> condition1 = new HashMap<String, String>();
			condition1.put("parentGid", "0");
			condition1.put("isRecommend", "1");
			List<Subject> bannerList = subjectService.getConditonList(
					condition1, "", false, myPage);
			Map<String, String> condition2 = new HashMap<String, String>();
			condition2.put("parentGid", "0");
			condition2.put("isRecommend", "0");
			List<Subject> subjectList = subjectService.getConditonList(
					condition2, "", false, myPage);
			JSONObject data = new JSONObject();
			data.put("bannerList", bannerList);
			data.put("subjectList", subjectList);
			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	/**
	 * 获得专题
	 * @param subjectGid
	 */
	public void queryParentSubjectDetail() {
		JSONObject ret = new JSONObject();
		try {
			Subject subject = subjectService.get(subjectGid);
			JSONObject data = new JSONObject();
			data.put("gid", subject.getGid());
			data.put("name", subject.getTitle());
			data.put("description", subject.getDescription());
			data.put("pic", subject.getPic());
			JSONArray ja = new JSONArray();
			Map<String, String> condition2 = new HashMap<String, String>();
			condition2.put("parentGid", subject.getGid());
			List<Subject> list2 = subjectService.getConditonList(condition2,
					"", false, null);

			for (Subject cSubject : list2) {
				JSONObject jo = new JSONObject();
				jo.put("mark", cSubject.getTitle());
				//子专题下的文章
				Map<String, String> condition3 = new HashMap<String, String>();
				condition3.put("subjectGid", cSubject.getGid());
				List<Textbook> textbookList = textBookService.getConditonList(
						condition3, "", false, null);
				JSONArray jaTextbook = new JSONArray();
				for (Textbook textbook : textbookList) {
					JSONObject art = new JSONObject();
					art.put("tbId", textbook.getTbId());
					art.put("textbookGid", textbook.getGid());
					art.put("pic", textbook.getTbAvatars());
					art.put("publish",
							ComUtil.dateTime2Str(textbook.getTbCreatetime()));
					art.put("author", textbook.getTbAuthor());
					art.put("title", textbook.getTbTitle());
					art.put("readNumber", textbook.getReadNumber());
					art.put("subjectGid", textbook.getSubjectGid());
					art.put("selected", "1");
					art.put("url", textbook.getTbDetail());
					art.put("type", "3");
					jaTextbook.add(art);
				}
				// 子专题下的慕课
				List<Course> courseList = courseService.getConditonList(
						condition3, "", false, null);
				for (Course course : courseList) {
					JSONObject art = new JSONObject();
					art.put("tbId", course.getId());
					art.put("textbookGid", course.getGid());
					art.put("pic", course.getAvatar());
					art.put("publish",
							ComUtil.dateTime2Str(course.getCreateTime()));
					art.put("author", "骨今中外");
					art.put("title", course.getTitle());
					art.put("readNumber", course.getWatchNumber());
					art.put("subjectGid", course.getSubjectGid());
					art.put("selected", "1");
					art.put("url", "");
					art.put("type", "1");
					jaTextbook.add(art);
				}
				// 子课程下的视频
				List<CourseVideo> videoList = videoService.getConditonList(
						condition3, "", false, null);
				for (CourseVideo video : videoList) {
					JSONObject art = new JSONObject();
					art.put("tbId", video.getId());
					art.put("textbookGid", video.getGid());
					art.put("pic", video.getAvatar());
					art.put("publish",
							ComUtil.dateTime2Str(video.getCreateTime()));
					art.put("author", "骨今中外");
					art.put("title", video.getTitle());
					art.put("readNumber", video.getWatchNumber());
					art.put("subjectGid", video.getSubjectGid());
					art.put("selected", "1");
					art.put("url", "");
					art.put("type", "2");
					jaTextbook.add(art);
				}
				jo.put("textbookList", jaTextbook);
				ja.add(jo);
			}

			data.put("markList", ja);

			ret.put("code", "1");
			ret.put("message", "查询成功");
			ret.put("result_data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
	// 专题订阅
	public void subscribeSubject() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userId");
			boolean logined = userTokenService.checkToken(request);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	public String getSubjectGid() {
		return subjectGid;
	}

	public void setSubjectGid(String subjectGid) {
		this.subjectGid = subjectGid;
	}
	
}

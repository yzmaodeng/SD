package com.sd.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sd.service.TextBookService;
import com.sd.util.CustomException;
import com.sd.vo.Academy;
import com.sd.vo.Advertisement;
import com.sd.vo.Announcement;
import com.sd.vo.Course;
import com.sd.vo.CourseExpert;
import com.sd.vo.CourseVideo;
import com.sd.vo.Posts;
import com.sd.vo.Textbook;

public class HomePageAction extends BaseAction {

	private static final long serialVersionUID = 121268L;

	@Resource
	private TextBookService textBookService;

	public void queryHomePageData() {
		JSONObject ret = new JSONObject();
		try {
			JSONObject res = new JSONObject();
			JSONArray dayRecja = new JSONArray();
			JSONArray Textja = new JSONArray();
			JSONArray CourseVideoja = new JSONArray();
			JSONArray postja = new JSONArray();
			JSONArray live = new JSONArray();
			JSONArray banners = new JSONArray();
			// 轮播
			Map<String, String> condition1 = new HashMap<String, String>();
			condition1.put("category", "专题");
			List<Announcement> list = announcementService.getConditonList(
					condition1, null, false, null);
			for (Announcement announcement : list) {
				JSONObject jo = new JSONObject();
				jo.put("gid", announcement.getGid());
				jo.put("pic", announcement.getPic());
				jo.put("url", announcement.getUrl());
				jo.put("typestr", announcement.getTypestr());
				jo.put("title", announcement.getTitle());
				jo.put("type", announcement.getType());
				banners.add(jo);
			}
			// 每日推荐
			List<Advertisement> DailyRecs = advertisementService.getDailyRec();
			for (Advertisement advertisement : DailyRecs) {
				JSONObject re = new JSONObject();
				if ("13".equals(advertisement.getType())) {
					re.put("type", "13");
					re.put("url", advertisement.getUrl());
					re.put("picUrl", advertisement.getTypegid());
					re.put("author", advertisement.getAuthor());
					re.put("title", advertisement.getTitle());
				} else if ("11".equals(advertisement.getType())) {
					re.put("type", "11");
					re.put("gid", advertisement.getTypegid());
					Academy academy = academyService.get(advertisement
							.getTypegid());
					re.put("picGid", academy.getPic());
					re.put("author", advertisement.getAuthor());
					re.put("title", advertisement.getTitle());
				} else {
					throw new CustomException("类型错误");
				}
				dayRecja.add(re);
			}
			List<Textbook> Textbooks = textBookService.queryHomeTexts();
			for (Textbook textbook : Textbooks) {
				JSONObject text = new JSONObject();
				text.put("title", textbook.getTbTitle());
				text.put("gid", textbook.getGid());
				Textja.add(text);
			}

			Map<String, String> condition = new HashMap<String, String>();
			// 建议的
			condition.put("recommendation", "1");
			condition.put("parentGid", "0");
			List<Course> clist = courseService.getConditonList(condition,
					"updateTime", true, null);
			for (Course course : clist) {
				JSONObject cour = new JSONObject();
				cour.put("type", "1");
				cour.put("pic", course.getAvatar());
				cour.put("gid", course.getGid());
				cour.put("title", course.getTitle());
				cour.put("watchNum", course.getWatchNumber());
				cour.put("price", course.getPrice());
				String[] strs = course.getExpertGid().split(",");
				JSONArray eja = new JSONArray();
				for (String string : strs) {
					CourseExpert courseExpert = courseExpertService.get(string);
					JSONObject ejo = new JSONObject();
					ejo.put("expertGid", courseExpert.getGid());
					ejo.put("expertName", courseExpert.getName());
					ejo.put("expertHospital", courseExpert.getHospital());
					eja.add(ejo);
				}

				cour.put("experts", eja);
				CourseVideoja.add(cour);

			}
			List<CourseVideo> tlist = courseVideoService.getConditonList(
					condition, "updateTime", true, null);
			for (CourseVideo courseVideo : tlist) {
				JSONObject videoObj = new JSONObject();
				videoObj.put("type", "2");
				videoObj.put("pic", courseVideo.getAvatar());
				videoObj.put("gid", courseVideo.getGid());
				videoObj.put("title", courseVideo.getTitle());
				videoObj.put("watchNum", courseVideo.getWatchNumber());
				videoObj.put("price", courseVideo.getPrice());
				JSONArray eja = new JSONArray();
				String[] strs = courseVideo.getExpertGid().split(",");
				for (String string : strs) {
					CourseExpert courseExpert = courseExpertService.get(string);
					JSONObject ejo = new JSONObject();
					ejo.put("expertGid", courseExpert.getGid());
					ejo.put("expertName", courseExpert.getName());
					ejo.put("expertHospital", courseExpert.getHospital());
					eja.add(ejo);
				}
				videoObj.put("expert", eja);
				CourseVideoja.add(videoObj);
			}
			List<Posts> posts = postsService.queryHomePosts();
			for (Posts posts2 : posts) {
				JSONObject post = new JSONObject();
				post.put("gid", posts2.getGid());
				String[] picArray = posts2.getPic().split(",");
				if (picArray.length > 0 && picArray[0].length() > 0) {
					post.put("pic", picArray[0]);
				} else {
					post.put("pic", "12958127-7698-4c01-8351-64b72b85db15");
				}
				post.put("title", posts2.getTitle());
				post.put("userName", posts2.getUser().getName());
				post.put("createTime", sdf.format(posts2.getCreateTime()));
				postja.add(post);
			}
			JSONObject liv = new JSONObject();
			liv.put("watchNum", 1200);
			liv.put("pic", "12958127-7698-4c01-8351-64b72b85db15");
			liv.put("title", "人工膝关节置换技巧—精品实战诀窍");
			live.add(liv);

			res.put("banners", banners);
			res.put("live", live);
			res.put("recommend", dayRecja);
			res.put("texts", Textja);
			res.put("courseVideo", CourseVideoja);
			res.put("posts", postja);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", res);
		} catch (Exception e) {
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

}

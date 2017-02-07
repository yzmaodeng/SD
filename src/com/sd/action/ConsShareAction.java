package com.sd.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sd.service.ConfService;
import com.sd.util.CustomException;
import com.sd.vo.Conf;
import com.sd.vo.Topic;

@Controller
@RequestMapping("/conference")
public class ConsShareAction extends BaseAction{
	private static final long serialVersionUID = -8426935184387900148L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource private ConfService confService;
	
	@RequestMapping("/shareQuery.out")
	public ModelAndView shareQuery(Model model,HttpServletRequest request,@Param String gid){
		try {
			Conf conf = confService.get(gid);
			conf.setDetail(conf.getDetail().replaceAll("\n", "<br/>"));
			if (conf == null)
				throw new CustomException(NO_CONF);
			model.addAttribute("conf", conf);
			List<Topic> topicList = confService.getTopicList(gid, "1");
			model.addAttribute("topicList", topicList);
			Map<String,List<Topic>> map=new HashMap<String, List<Topic>>();
			for (Topic top : topicList){
				List<Topic> topic2List = confService.getTopicList(top.getGid(), "2");
				map.put(top.getGid(),topic2List);
			}
			model.addAttribute("map", map);
		} catch (Exception e) {
			logger.error("",e);
		}
		ModelAndView mav = new ModelAndView("/jsp/share/html/conferenceShare.jsp");
		return mav;
	}
	@RequestMapping("/listShare.out")
	public ModelAndView shareQuery(Model model,HttpServletRequest request,@Param String year,@Param String month){
		try {
			Calendar calendar = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,1);   
			Date time=calendar.getTime();
			List<Conf> confList = confService.getByMonth(time);
			model.addAttribute("confList", confList);
		} catch (Exception e) {
			logger.error("",e);
		}
		ModelAndView mav = new ModelAndView("/jsp/share/html/conListShare.jsp");
		return mav;
	}
}

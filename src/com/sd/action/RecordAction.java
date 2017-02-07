package com.sd.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sd.service.PatientService;
import com.sd.service.RdPatientService;
import com.sd.util.CustomException;
import com.sd.vo.Channel;
import com.sd.vo.Posts;
import com.sd.vo.RdPatient;
import com.sd.vo.Record;
import com.sd.vo.User;

@Controller
@RequestMapping("/record")
public class RecordAction extends BaseAction{
	@Resource private RdPatientService rdPatientService;
	@Resource private PatientService patientService;
	
	@RequestMapping("/share.out")
	public ModelAndView shareQuery(Model model,HttpServletRequest request,@Param String gid){
			try {
				if (StringUtils.isBlank(gid))
					throw new CustomException(NO_ID);
				
				RdPatient record=rdPatientService.get(gid);
				if (record == null)
					throw new CustomException(NO_POST);
				model.addAttribute("record", record);
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ModelAndView mav = new ModelAndView("/jsp/share/html/record.jsp");
			return mav;
	}
	@RequestMapping("/queryCourse.do")
	public void queryCourse(HttpServletRequest request,HttpServletResponse response,@Param String gid){
			try {
				if (StringUtils.isBlank(gid))
					throw new CustomException(NO_ID);
				
				Map<String, String> condition = new HashMap<String, String>();
				condition.put("pgid", gid);
				List<Record> list=patientService.getConditonList(condition, "updateTime", true, null);
				JSONArray ja=JSONArray.fromObject(list);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				response.getWriter().write(ja.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}

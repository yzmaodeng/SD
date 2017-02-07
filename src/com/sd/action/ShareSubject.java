package com.sd.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sd.util.CustomException;
import com.sd.vo.Channel;
import com.sd.vo.Posts;
import com.sd.vo.User;

@Controller
@RequestMapping("/subject")
public class ShareSubject extends BaseAction{
	@RequestMapping("/shareQuery.out")
	public ModelAndView shareQuery(Model model,HttpServletRequest request,@Param String gid){
			try {
				if (StringUtils.isBlank(gid))
					throw new CustomException(NO_ID);
				
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ModelAndView mav = new ModelAndView("/jsp/share/html/subject.jsp");
			return mav;
	}
}

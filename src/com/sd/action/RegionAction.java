package com.sd.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opensymphony.xwork2.ActionSupport;
import com.sd.service.RegionService;
import com.sd.vo.Area;
@Controller
@RequestMapping("/region")
public class RegionAction extends ActionSupport{
	private static final long serialVersionUID = -3416186509209943695L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource private RegionService regionService;
	
	private String code;
	private int type;
	private int level;
	
	@RequestMapping("/query.do")
	public void myRegion(HttpServletResponse rep,@ModelAttribute Area area){
		try {
			List<Area> list=new ArrayList<Area>();
			list=regionService.queryRegion(area);
			JSONArray jsonArray = JSONArray.fromObject(list);
			rep.setCharacterEncoding("UTF-8");
			rep.setContentType("text/plain");
			rep.getWriter().write(jsonArray.toString());
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	public void region(){
		try {
			Area tarea=new Area();
			List<Area> list=new ArrayList<Area>();
			if(code!=null){
				tarea.setTaCode(code);
			}
			tarea.setTaLevel(level);
			list=regionService.queryRegion(tarea);
			JSONObject json = new JSONObject();
			json.put("code", "1");
			json.put("message", "查询成功");
			JSONArray data = new JSONArray();
			for (int i=0; i<list.size(); i++){
				JSONObject art = new JSONObject();
				art.put("code",list.get(i).getTaCode());
				art.put("name",list.get(i).getTaName());
				art.put("level", list.get(i).getTaLevel());
				data.add(art);
			}
			json.put("result_data", data);
			HttpServletResponse rep = ServletActionContext.getResponse();
			rep.setCharacterEncoding("UTF-8");
			rep.setContentType("text/plain");
			rep.getWriter().write(json.toString());
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}

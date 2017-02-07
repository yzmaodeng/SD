package com.sd.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sd.service.HospitalService;
import com.sd.vo.Thospital;
@Controller
@RequestMapping("/hospital")
public class HospitalAction extends BaseAction{
	private static final long serialVersionUID = 4945629574781980267L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private HospitalService hospitalService;
	
	//这个是地区编码，命名这个参数的人怎么想的，服
	private String areCode;
	private String code;
	private String name;
	
	@RequestMapping("/query.do")
	public void myRegion(HttpServletResponse rep,@ModelAttribute Thospital hospital){
		try {
			List<Thospital> list=hospitalService.query(hospital);
			JSONArray jsonArray = JSONArray.fromObject(list);
			rep.setCharacterEncoding("UTF-8");
			rep.setContentType("text/plain");
			rep.getWriter().write(jsonArray.toString());
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	public void query(){
		Thospital thospital=new Thospital();
		if(areCode!=null)thospital.setHosLocal(areCode);
		if(code!=null)thospital.setHosCode(code);
		if(name!=null)thospital.setHosName(name);
		List<Thospital> list=hospitalService.query(thospital);
		JSONObject ret = new JSONObject();
		ret.put("code", "1");
		ret.put("message", "查询成功");
		JSONArray datas = new JSONArray();
		for (int i=0; i<list.size(); i++){
			JSONObject data = new JSONObject();
			data.put("code", list.get(i).getHosCode());
			data.put("name", list.get(i).getHosName());
			data.put("descript", list.get(i).getHosDesc());
			data.put("local", list.get(i).getHosLocal());
			data.put("localName",list.get(i).getLocalName());
			datas.add(data);
		}
		
		ret.put("result_data", datas);
		putDataOut(ret.toString());
	}

	public String getAreCode() {
		return areCode;
	}

	public void setAreCode(String areCode) {
		this.areCode = areCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

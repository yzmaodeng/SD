
package com.sd.action;

import java.text.SimpleDateFormat;
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

import com.sd.service.ActivityDataService;
import com.sd.service.UserTokenService;
import com.sd.util.CustomException;
import com.sd.vo.ActivityData;

public class ActivityDataAction extends BaseAction{
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource private ActivityDataService activityDataService;
	@Resource private UserTokenService userTokenService;
	private String pid;
	
	public void getData(){
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			
			JSONArray ja=new JSONArray();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String>condition=new HashMap<String, String>();
			condition.put("pid", pid);
			List<ActivityData>list=activityDataService.getConditonList(condition, "time", false, null);
			if(list.size()!=0){
				String first="";
				JSONObject jo=new JSONObject();
				int act[]=new int[72];
				int step[]=new int[72];
				for (ActivityData activityData : list) {
					if(!first.equals(sdf.format(activityData.getTime()))){
						if(!first.equals("")){
							jo.put("activity", JSONArray.fromObject(act));
							jo.put("step", JSONArray.fromObject(step));
							ja.add(jo);
							jo.clear();
							act=new int[72];
							step=new int[72];
							if(sdf.parse(sdf.format(activityData.getTime())).getTime()-sdf.parse(first).getTime()>3600*24){
								jo.put("date", sdf.format(new Date(sdf.parse(first).getTime()+3600*24)));
								first=jo.getString("date");
							}
						}
						first=sdf.format(activityData.getTime());
						jo.put("date", sdf.format(activityData.getTime()));
					}
					act[Integer.parseInt(activityData.getElement())]+=Integer.parseInt(activityData.getActivity());
					step[Integer.parseInt(activityData.getElement())]+=Integer.parseInt(activityData.getStep());
				}
				jo.put("activity", JSONArray.fromObject(act));
				jo.put("step", JSONArray.fromObject(step));
				ja.add(jo);
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", ja);	
			}else{
				ret.put("code", "1");
				ret.put("message", SUCCESS_INFO);
				ret.put("result_data", ja);	
			}
		} catch (Exception e) {
			logger.error("",e);
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}
	
}

package com.sd.action;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.sd.duib.CreditConsumeParams;
import com.sd.duib.CreditConsumeResult;
import com.sd.duib.CreditNotifyParams;
import com.sd.duib.CreditTool;
import com.sd.service.DordersService;
import com.sd.service.ScoreService;
import com.sd.service.SignService;
import com.sd.service.TaskCompleteService;
import com.sd.service.TaskService;
import com.sd.service.UserService;
import com.sd.service.UserTokenService;
import com.sd.util.Conditions;
import com.sd.util.CustomException;
import com.sd.util.scoreUtil;
import com.sd.vo.Dorders;
import com.sd.vo.Score;
import com.sd.vo.Task;
import com.sd.vo.TaskComplete;
import com.sd.vo.User;

public class ScoreAction extends BaseAction {
	@Resource
	private UserTokenService userTokenService;
	@Resource
	private SignService signService;
	@Resource
	private ScoreService scoreService;
	@Resource
	private UserService userService;
	@Resource
	private TaskService taskService;
	@Resource
	private DordersService dordersService;
	@Resource
	private TaskCompleteService taskCompleteService;
	HttpServletRequest request = ServletActionContext.getRequest();
	private String gid;
	private String score;
	
	
	
	public void createTokenURL() {
		CreditTool creditTool = new CreditTool("46kbTVRJ9E7uSdmCuTedhsZn5KJ3","j1cQDHM5Kgp5h6YkZ5iQb3V6BXs");
		JSONObject ret = new JSONObject();
		Map params = new HashMap();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = request.getHeader("userid");
			boolean logined = userTokenService.checkToken(request);
			if (logined){
				User user = userService.get(userId);
				params.put("uid", userId);
				params.put("credits", user.getScore().toString());
			}else{
				params.put("uid", "not_login");
				params.put("credits", "0");
			}
//			params.put("redirect", "http://www.duiba.com.cn/chome/index");
			String url = creditTool.buildUrlWithSign("http://www.duiba.com.cn/autoLogin/autologin?", params);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("url", url);
		} catch (Exception e) {
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 用户签到
	// userId 是 字符串 用户GID
	// token 是 字符串 验证码
	public void userSignIn() {
		JSONObject ret = new JSONObject();
		JSONObject result = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			String userId = request.getHeader("userid");
			if (scoreUtil.userSignInChangeScor(signService, scoreService,
					userService, userId, 1) == 0) {
				result.put("info", "恭喜你加一分!!!");
				result.put("status", true);
			} else {
				result.put("info", "签到加分完毕!!!");
				result.put("status", false);

			}
			// List<Conditions> conditions=new ArrayList<Conditions>();
			// Conditions conditions1=new Conditions();
			// conditions1.setType("map");
			// Map<String, String>map=new HashMap<String, String>();
			// map.put("uid", userId);
			// conditions1.setMap(map);
			// conditions.add(conditions1);
			// Conditions conditions2=new Conditions();
			// conditions2.setType("timeA");
			// conditions2.setKey("time");
			// conditions2.setValue(myFmt.format(day));
			// List<Sign> list= signService.getConditonsList(conditions, "time",
			// true, null);
			// int myScore=100;
			// for (Sign mySign : list) {
			// if(ComUtil.daysBetweenDate(mySign.getTime(),myFmt.parse(myFmt.format(now)))<myScore/100){
			// myScore+=100;
			// }
			// }
			// Score score=new Score();
			// score.setGid(uuid);
			// score.setScore(myScore+"");
			// score.setSource("签到");
			// score.setTime(now);
			// score.setUid(userId);
			// scoreService.save(score);
			// User user=userService.get(userId);
			// user.setScore(user.getScore()+myScore);
			// userService.update(user);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", result);
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// 完成积分任务
	// userId 是 字符串 用户GID
	// token 是 字符串 验证码
	// gid 是 字符串 任务的Gid
	// score 是 字符串 任务获得的分值
	public void finishTask() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			String userId = request.getHeader("userid");
			String uuid = UUID.randomUUID().toString();
			SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("uid", userId);
			condition.put("taskId", gid);
			// 当前时间
			condition.put("time", myFmt.format(new Date()));
			List<TaskComplete> list = taskCompleteService.getConditonList(
					condition, null, false, null);
			TaskComplete taskComplete = list.get(0);
			taskComplete.setFinish("1");
			taskCompleteService.update(taskComplete);
			Score myScore = new Score();
			myScore.setGid(uuid);
			myScore.setSource("每日任务");
			myScore.setTime(new Date());
			myScore.setUid(userId);
			myScore.setScore(Integer.parseInt(score));
			scoreService.save(myScore);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", "");
		} catch (CustomException e) {
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		putDataOut(ret.toString());
	}

	// uid string sign yes string 255 用户id
	// credits yes long 20 本次兑换扣除的积分
	// appKey yes string 255 接口appKey，应用的唯一标识
	// timestamp yes string 20 1970-01-01开始的时间戳，毫秒为单位。
	// orderNum yes string 255 兑吧订单号(请记录到数据库中)
	// type yes string 255 兑换类型：alipay(支付宝), qb(Q币), coupon(优惠券), object(实物),
	// phonebill(话费), phoneflow(流量), virtual(虚拟商品), turntable(大转盘),
	// singleLottery(单品抽奖)，hdtoolLottery(活动抽奖),manualLottery(手动开奖),gameLottery(游戏)，所有类型不区分大小写
	// actualPrice yes int 11 此次兑换实际扣除开发者账户费用，单位为分
	// ip no string 255 用户ip，不保证获取到
	// 实物商品：返回收货信息(姓名:手机号:省份:城市:区域:详细地址)、支付宝：返回账号信息(支付宝账号:实名)、话费：返回手机号、QB：返回QQ号
	// sign yes string 255 MD5签名，详见签名规则
	// description no string 255 本次积分消耗的描述(带中文，请用utf-8进行url解码)
	// facePrice no int 11 兑换商品的市场价值，单位是分，请自行转换单位
	// params no string 255 详情参数，不同的类型，返回不同的内容，中间用英文冒号分隔。(支付宝类型带中文，请用utf-8进行解码)
	// waitAudit no boolean 是否需要审核(如需在自身系统进行审核处理，请记录下此信息)

	public void consumeScore() {
		CreditTool creditTool = new CreditTool("46kbTVRJ9E7uSdmCuTedhsZn5KJ3",
				"j1cQDHM5Kgp5h6YkZ5iQb3V6BXs");
		CreditConsumeResult creditConsumeResult;
		Integer nowScore = 0;
		try {
			CreditConsumeParams parseCreditConsume = creditTool
					.parseCreditConsume(request);
			 String uid = parseCreditConsume.getUid();
			Integer credit = parseCreditConsume.getCredits();
			User user=userService.get(uid);
			nowScore = user.getScore();
			if (nowScore < credit)
				throw new Exception("积分不足");
			userService.changeScore(uid, -credit);
			int remains = nowScore - credit;
			Score myScore = new Score();
			myScore.setGid(UUID.randomUUID().toString());
			myScore.setSource("对吧兑换");
			myScore.setTime(new Date());
			myScore.setUid(uid);
			myScore.setScore(-credit);
			scoreService.save(myScore);
			Dorders dorders = new Dorders();
			String bizId = UUID.randomUUID().toString();
			dorders.setId(bizId );
			HashMap<String,String> condition = new HashMap<String, String>();
			condition.put("orderNum", parseCreditConsume.getOrderNum());
			String conditonCount = dordersService.getConditonCount(condition);
			if(!"0".equals(conditonCount))throw new Exception("订单已经存在");
			dorders.setOrderNum(parseCreditConsume.getOrderNum());
			dorders.setActualPrice(parseCreditConsume.getActualPrice());
			dorders.setAppId(parseCreditConsume.getAppKey());
			dorders.setCredits(credit);
			// order_status：订单状态（创建、成功、失败）。
			// credits_status：积分状态（预扣、成功、返还）。
			dorders.setCreditStatus("1");
			System.out.println(parseCreditConsume.getDescription()+":::"+URLDecoder.decode(parseCreditConsume.getDescription(), "UTF-8"));
			dorders.setDescription(URLDecoder.decode(parseCreditConsume.getDescription(), "UTF-8"));
			dorders.setUserId(uid);
			dorders.setOrderStatus("1");
			dorders.setType(parseCreditConsume.getType());
			dorders.setGmtCreate(parseCreditConsume.getTimestamp());
			dorders.setFacePrice(parseCreditConsume.getFacePrice());
			dorders.setParams(parseCreditConsume.getParams());
			dorders.setWaitAudit(parseCreditConsume.isWaitAudit());
			dorders.setBizId(bizId);
			dordersService.save(dorders);
			creditConsumeResult = new CreditConsumeResult(true);
			creditConsumeResult.setBizId(bizId);
			creditConsumeResult.setCredits(remains);
		} catch (Exception e) {
			creditConsumeResult = new CreditConsumeResult(false);
			creditConsumeResult.setErrorMessage(e.getMessage());
			creditConsumeResult.setCredits(nowScore);
		}
		
		putDataOut(creditConsumeResult.toString());
	}
	
	/*
	*  兑换订单的结果通知请求的解析方法
	*  当兑换订单成功时，兑吧会发送请求通知开发者，兑换订单的结果为成功或者失败，如果为失败，开发者需要将积分返还给用户
	*/
	
	
	public void notifyResult(){
		CreditTool creditTool = new CreditTool("46kbTVRJ9E7uSdmCuTedhsZn5KJ3",
				"j1cQDHM5Kgp5h6YkZ5iQb3V6BXs");
		try {
		   CreditNotifyParams params = creditTool.parseCreditNotify(request);
		   String orderNum = params.getOrderNum();
		   HashMap<String,String> condition = new HashMap<String, String>();
		   condition.put("orderNum",orderNum );
		   List<Dorders> conditonList = dordersService.getConditonList(condition, null, false, null);
		   if(conditonList.size()==0)throw new Exception("订单不存在");
		   if(conditonList.size()>1)throw new Exception("订单存在重复");
		   String orderStatus = conditonList.get(0).getOrderStatus();
		   if(!"1".equals(orderStatus)){
			   putDataOut("ok");
		   }else{
			   String userId = conditonList.get(0).getUserId();
			    if(params.isSuccess()){
			        //兑换成功
			    	Dorders dorders = conditonList.get(0);
	    	    	dorders.setOrderStatus("2");
	    	    	dorders.setCreditStatus("2");
	    	    	dordersService.update(dorders);
	    	    	putDataOut("ok");
			    }else{
			      	Dorders dorders = conditonList.get(0);
	    	    	Integer credits = dorders.getCredits();
	    	    	userService.changeScore(dorders.getUserId(),credits);
	    	    	Score myScore = new Score();
	    			myScore.setGid(UUID.randomUUID().toString());
	    			myScore.setSource("对吧返还");
	    			myScore.setTime(new Date());
	    			myScore.setUid(userId);
	    			myScore.setScore(credits);
	    			scoreService.save(myScore);
	    			dorders.setOrderStatus("3");
	    	    	dorders.setCreditStatus("3");
	    	    	dordersService.update(dorders);
	    	    	putDataOut("ok");
			        //兑换失败，根据orderNum，对用户的金币进行返还，回滚操作
			    }
			   
		   }
		   
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
	

	// 获取任务列表
	// userId 是 字符串 用户GID
	// token 是 字符串 验证码
	public void getTask() {
		JSONObject ret = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			boolean logined = userTokenService.checkToken(request);
			if (!logined)
				throw new CustomException(NO_LOGIN);
			JSONArray res = new JSONArray();
			String userId = request.getHeader("userid");
			SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
			List<Conditions> conditions = new ArrayList<Conditions>();
			Conditions one = new Conditions();
			one.setType("time");
			one.setKey("time");
			one.setValue(myFmt.format(new Date()));
			conditions.add(one);

			Conditions two = new Conditions();
			two.setType("map");
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", userId);
			two.setMap(map);
			conditions.add(two);

			List<TaskComplete> tasklist = taskCompleteService.getConditonsList(
					conditions, null, false, null);
			if (tasklist.size() != 0) {
				for (TaskComplete taskComplete : tasklist) {
					JSONObject jo = new JSONObject();
					Task mytask = taskService.get(taskComplete.getTaskId());
					jo.put("gid", taskComplete.getTaskId());
					jo.put("name", mytask.getName());
					jo.put("taskScore", mytask.getScore());
					jo.put("isFinish", taskComplete.getFinish());
				}
			} else {
				int count = 0;
				// 加入基本任务
				List<Task> baseTask = taskService.getBaseList(userId);
				List<Task> ran = this.randomList(baseTask);
				for (Task task : ran) {
					JSONObject jo = new JSONObject();
					jo.put("gid", task.getGid());
					jo.put("name", task.getName());
					jo.put("taskScore", task.getScore());
					jo.put("isFinish", "0");
					TaskComplete taskComplete = new TaskComplete();
					taskComplete.setUid(userId);
					taskComplete.setTaskId(task.getGid());
					taskComplete.setTime(myFmt.parse(myFmt.format(new Date())));
					taskComplete.setFinish("0");
					taskCompleteService.save(taskComplete);
					res.add(jo);
					if (++count == 5) {
						break;
					}
				}
				// 加入日常任务
				if (count != 5) {
					Map<String, String> condition = new HashMap<String, String>();
					condition.put("type", "2");
					List<Task> tasks = taskService.getConditonList(condition,
							null, false, null);
					for (Task task : tasks) {
						JSONObject jo = new JSONObject();
						jo.put("gid", task.getGid());
						jo.put("name", task.getName());
						jo.put("taskScore", task.getScore());
						jo.put("isFinish", "0");
						TaskComplete taskComplete = new TaskComplete();
						taskComplete.setUid(userId);
						taskComplete.setTaskId(task.getGid());
						taskComplete.setTime(myFmt.parse(myFmt
								.format(new Date())));
						taskComplete.setFinish("0");
						taskCompleteService.save(taskComplete);
						res.add(jo);
						if (++count == 5) {
							break;
						}
					}
				}
			}
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

	public List<Task> randomList(List<Task> list) {
		List<Task> res = new ArrayList<Task>();
		Random randow = new Random();
		for (int i = list.size(); i > 0; i--) {
			int c = randow.nextInt(list.size());
			res.add(list.get(c));
			list.remove(c);
		}
		return res;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public UserTokenService getUserTokenService() {
		return userTokenService;
	}

	public void setUserTokenService(UserTokenService userTokenService) {
		this.userTokenService = userTokenService;
	}

	public SignService getSignService() {
		return signService;
	}

	public void setSignService(SignService signService) {
		this.signService = signService;
	}

	public ScoreService getScoreService() {
		return scoreService;
	}

	public void setScoreService(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public TaskCompleteService getTaskCompleteService() {
		return taskCompleteService;
	}

	public void setTaskCompleteService(TaskCompleteService taskCompleteService) {
		this.taskCompleteService = taskCompleteService;
	}



	

}

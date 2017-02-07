package com.sd.util;


import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import com.sd.service.PostsService;
import com.sd.service.ScoreService;
import com.sd.service.SignService;
import com.sd.service.UserService;
import com.sd.vo.Score;
import com.sd.vo.Sign;
import com.sd.vo.User;

public class scoreUtil {
	public  static void secRegistChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
		
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("注册登录");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
	}
	public  static void userAuthChangeScor(UserService userService,String userId,int score){
		userService.changeScore(userId,score);
	}
	//注册和更新的时候的头像都是用的update
	public  static void userUpdateAvatarsChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
		User user = userService.get(userId);
		String avatars = user.getAvatars();
		if("40aaa107-1fad-4e62-8438-09966b77368b".equals(avatars)){
			userService.changeScore(userId,score);
			Score score1=new Score();
			score1.setGid(UUID.randomUUID().toString());
			score1.setScore(score);
			score1.setSource("注册头像");
			score1.setTime(new Date());
			score1.setUid(userId);
			scoreService.save(score1);
		}else{
  			return;
		}
		
	}
	//发病例帖子加分
	public  static void addMedicalRecordChangeScor(ScoreService scoreService,PostsService postsService,UserService userService,String userId,int score){
		
		String s= "select count(*) from tpost where to_days(pst_CreateTime) = to_days(now()) and  pst_userGid='"+userId+"' and type='3'";
		int num=postsService.countNumBySql(s);
		if(num<2){
			
			userService.changeScore(userId,score);
			Score score1=new Score();
			score1.setGid(UUID.randomUUID().toString());
			score1.setScore(score);
			score1.setSource("发病例帖子");
			score1.setTime(new Date());
			score1.setUid(userId);
			scoreService.save(score1);
		}
	}
public  static void addReplyPostChangeScor(ScoreService scoreService,PostsService postsService,UserService userService,String userId,int score){
//		String s= "select count(*) from treply where to_days(rpy_CreateTime) = to_days(now()) and  rpy_userGid='"+userId+"'";
		String s= "select count(*) from score where to_days(time) = to_days(now()) and  uid='"+userId+"'"+"and source='评论帖子'";
		int num=postsService.countNumBySql(s);
		if(num<5){
			
			userService.changeScore(userId,score);
			Score score1=new Score();
			score1.setGid(UUID.randomUUID().toString());
			score1.setScore(score);
			score1.setSource("评论帖子");
			score1.setTime(new Date());
			score1.setUid(userId);
			scoreService.save(score1);
		}
		}
	//收藏
public  static void postFavChangeScor(ScoreService scoreService,PostsService postsService,UserService userService,String userId,int score){
	String s= "select count(*) from tfav where to_days(fav_CreateTime) = to_days(now()) and  fav_userGID='"+userId+"' and fav_type='3'";
	int num=postsService.countNumBySql(s);
	if(num<2){
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("帖子收藏");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
	}else{
		return;
	}
	
}


public  static void postPraiseChangeScor(ScoreService scoreService,PostsService postsService,UserService userService,String userId,int score){
	String s= "select count(*) from tpostfav where to_days(pf_CreateTime) = to_days(now()) and  pf_userGid='"+userId+"'";
	int num=postsService.countNumBySql(s);
	if(num<2){
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("帖子点赞");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
	}else{
		return;
	}
	
}


public  static int addTeamInfoChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
	String s= "select count(*) from score where   uid='"+userId+"'"+"and source='创建团队'";
	int countNumBySql = scoreService.countNumBySql(s);
	if(countNumBySql<1){
		userService.changeScore(userId,score);
		
		
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("创建团队");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
		
		
	}
	return countNumBySql;

	
}

//申請團隊
public  static int applyOperChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
	String s= "select count(*) from score where   uid='"+userId+"'"+"and source='申请团队'";
	int countNumBySql = scoreService.countNumBySql(s);
	if(countNumBySql<1){
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("申请团队");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
		
		
	}
	return countNumBySql;

}
//用戶簽到
@SuppressWarnings("unused")
public  static int userSignInChangeScor(SignService signService,ScoreService scoreService,UserService userService,String userId,int score){
	String s= "select count(*) from score where to_days(time) = to_days(now()) and  uid='"+userId+"'"+"and source='签到'";
	String s1="select count(*) from sign where to_days(time) = to_days(now()) and  uid='"+userId+"'";
	int countNumBySql2 = signService.countNumBySql(s1);
	int countNumBySql = scoreService.countNumBySql(s);
	int sum=countNumBySql2+countNumBySql;
	if(sum<1){
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("签到");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
		
		
		Sign sign=new Sign();
		Date now=new Date();
		String uuid=UUID.randomUUID().toString();
		sign.setGid(uuid);
		sign.setTime(now);
		sign.setUid(userId);
		signService.save(sign);
	}
	return sum;

}
//添加方案代碼為“1”
public  static int planAddChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
	String s= "select count(*) from score where to_days(time) = to_days(now()) and  uid='"+userId+"'"+"and source='创建方案'";
	int countNumBySql = scoreService.countNumBySql(s);
	if(countNumBySql<2){
		userService.changeScore(userId,score);
		
		
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("创建方案");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
		
		
	}
	return countNumBySql;

}

public  static int shareQueryChangeScor(ScoreService scoreService,UserService userService,String userId,int score){
	String s= "select count(*) from score where to_days(time) = to_days(now()) and  uid='"+userId+"'"+"and source='分享帖子'";
	int countNumBySql = scoreService.countNumBySql(s);
	if(countNumBySql<2){
		userService.changeScore(userId,score);
		Score score1=new Score();
		score1.setGid(UUID.randomUUID().toString());
		score1.setScore(score);
		score1.setSource("分享帖子");
		score1.setTime(new Date());
		score1.setUid(userId);
		scoreService.save(score1);
	}
	return countNumBySql;
}





}

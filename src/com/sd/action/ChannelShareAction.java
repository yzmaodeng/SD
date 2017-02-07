package com.sd.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sd.service.PostsService;
import com.sd.service.ReplyService;
import com.sd.util.ComUtil;
import com.sd.util.CustomException;
import com.sd.util.scoreUtil;
import com.sd.vo.Channel;
import com.sd.vo.Posts;
import com.sd.vo.Reply;
import com.sd.vo.User;

@Controller
@RequestMapping("/channel")
public class ChannelShareAction extends BaseAction{
	private static final long serialVersionUID = -2106840782058733098L;
	
	@Resource private PostsService postsService;
	@Resource private ReplyService replyService;
	
	@RequestMapping("/comment.do")
	public void postDetailSelect(HttpServletResponse response,@Param String gid){
		JSONObject ret = new JSONObject();
		try {
			if (StringUtils.isBlank(gid))
				throw new CustomException(NO_ID);
			
			Posts post = postsService.get(gid);
			if (post == null)
				throw new CustomException(NO_POST);
			
			JSONObject data = new JSONObject();
			data.put("gid", post.getGid());
			User user = post.getUser();
			if (user != null){
				data.put("userId", user.getGid());
				data.put("userName", user.getName());
				data.put("userAvatars", user.getAvatars());
			} else {
				data.put("userId", "");
				data.put("userName", "");
				data.put("userAvatars", "");
			}
			data.put("detail", post.getDetail());
			data.put("createTime", ComUtil.dateTime2Str(post.getCreateTime()));
			data.put("lstPic", post.getPic());
			data.put("lstVid", post.getVid());
			data.put("favCount", post.getFavCount());
			data.put("replyCount", post.getReplyCount());
			Channel channel = post.getChannel();
			if (channel != null){
				data.put("chGid", channel.getGid());
				data.put("chName", channel.getName());
			} else {
				data.put("chGid", "");
				data.put("chName", "");
			}
			List<Reply> replyList = replyService.getReplyClient(gid);
			JSONArray replys = new JSONArray();
			for (Reply rep : replyList){
				JSONObject reply = new JSONObject();
				reply.put("rpyGid", rep.getGid());
				User rpyUser = rep.getUser();
				if (rpyUser != null){
					reply.put("rpyUserId", rpyUser.getGid());
					reply.put("rpyUserName", rpyUser.getName());
					reply.put("rpyUserAvatars", rpyUser.getAvatars());
				} else {
					reply.put("rpyUserId", "");
					reply.put("rpyUserName", "");
					reply.put("rpyUserAvatars", "");
				}
				User rpyToUser = rep.getToUser();
				if (rpyToUser != null){
					reply.put("rpyToUserId", rpyToUser.getGid());
					reply.put("rpyToUserName", rpyToUser.getName());
				} else {
					reply.put("rpyToUserId", "");
					reply.put("rpyToUserName", "");
				}
				reply.put("rpyDetail", rep.getDetail());
				reply.put("rpyCreateTime", ComUtil.dateTime2Str(rep.getCreateTime()));
				replys.add(reply);
			}
			data.put("lstreply", replys);
			ret.put("code", "1");
			ret.put("message", SUCCESS_INFO);
			ret.put("result_data", data);
		} catch(CustomException e){
			ret.put("code", "0");
			ret.put("message", e.getMessage());
			ret.put("result_data", new JSONObject());
		} catch (Exception e) {
			ret.put("code", "0");
			ret.put("message", ERROR_INFO);
			ret.put("result_data", new JSONObject());
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.getWriter().write(ret.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("/shareQuery.out")
	public ModelAndView shareQuery(Model model,HttpServletRequest request,@Param String gid){
		String gidString="";
			try {
				if (StringUtils.isBlank(gid))
					throw new CustomException(NO_ID);
				
				Posts post = postsService.get(gid);
				if (post == null)
					throw new CustomException(NO_POST);
				post.setDetail(post.getDetail().replace("\n",""));
				model.addAttribute("post", post);
				String[] imgs=post.getPic().split(",");
				model.addAttribute("imgs", imgs);
				User user = post.getUser();
				if (user != null){
					model.addAttribute("user", user);
					gidString = user.getGid();
				}
				Channel channel = post.getChannel();
				if (channel != null){
					model.addAttribute("channel", channel);
				}
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ModelAndView mav = new ModelAndView("/jsp/share/html/channelShare.jsp");
//			scoreUtil.shareQueryChangeScor(scoreService,userService,gidString,1);
			return mav;
	}
}

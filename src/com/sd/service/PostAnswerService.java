package com.sd.service;

import com.sd.vo.PostAnswer;

public interface PostAnswerService extends BaseService<PostAnswer, String> {

	String getStatus(String userId, String string);


}

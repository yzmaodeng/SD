package com.sd.service;

import com.sd.vo.NeedsChange;

public interface NeedsChangeService extends BaseService<NeedsChange, String> {



	boolean checkByCodeToken(String codeToken, String mobile);
	
	
}

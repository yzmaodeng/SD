package com.sd.service;

import com.sd.vo.Motion;
import com.sd.vo.SchemeTmpl;

public interface MotionService extends BaseService<Motion, String> {
	/**
	 * 复制模版动作，目标成为患者的动作，目标
	 * @param newScheme	方案模版
	 * @param newSgid	新方案id
	 */
	void saveMotionByCopy(SchemeTmpl newScheme, String newSgid);

}

package com.sd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sd.dao.InvoiceDao;
import com.sd.service.InvoiceService;
import com.sd.vo.Invoice;
import com.sd.vo.OrderInfo;

@Service
public class InvoiceServiceImpl extends BaseServiceImpl<Invoice, String>
		implements InvoiceService {
	@Resource
	public void setBaseDao(InvoiceDao invoiceDao) {
		super.setBaseDao(invoiceDao);
	}
	@Resource private InvoiceDao invoiceDao;

	@Override
	public List<Invoice> getInvoiceByGid(String gid) {
		String sqlString="select  * from  invoice  where  gid='"+gid+"'";
		return invoiceDao.getVoListBySql(sqlString);
	}
	
	
	

}

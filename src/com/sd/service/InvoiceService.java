package com.sd.service;

import java.util.List;

import com.sd.vo.Invoice;

public interface InvoiceService extends BaseService<Invoice, String> {
	List<Invoice> getInvoiceByGid(String gid);

	
}

package com.jije.boh.core.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jije.boh.core.jpa.repository.UserRepository;
import com.jije.boh.core.service.inter.IOperateService;

@Component("operateService")
public class OperateService implements IOperateService {

	@Autowired
	private UserRepository userDao;

	public long getCount() {
		return userDao.count();
	}

	public String getDate() {
		return new Date().toString() + "there are: 10 records";
	}
}

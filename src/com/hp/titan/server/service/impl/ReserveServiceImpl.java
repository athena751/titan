package com.hp.titan.server.service.impl;

//import java.util.Iterator;


import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;

import com.hp.titan.server.model.Reservation;

import com.hp.titan.server.dao.ReserveDao;

import com.hp.titan.server.service.ReserveService;


public class ReserveServiceImpl implements ReserveService{
	
   private ReserveDao reserveDao;

	
	public void doSaveReservation(Reservation reservation)throws BaseDaoException {
		//server.setUpdateTime(DateUtils.date2Sz(new Date()));
		reserveDao.saveOrUpdate(reservation);
	}


	public ReserveDao getReserveDao() {
		return reserveDao;
	}


	public void setReserveDao(ReserveDao reserveDao) {
		this.reserveDao = reserveDao;
	}
	
	
}
	
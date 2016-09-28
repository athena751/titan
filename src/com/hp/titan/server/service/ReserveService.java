package com.hp.titan.server.service;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.server.model.Reservation;

public interface ReserveService {

	public void doSaveReservation(Reservation reservation)throws BaseDaoException;

}

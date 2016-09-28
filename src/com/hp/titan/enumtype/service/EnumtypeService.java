package com.hp.titan.enumtype.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.enumtype.model.EnumType;
import com.hp.titan.enumtype.model.EnumValue;
import com.hp.titan.server.model.Card;
import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.vo.*;

public interface EnumtypeService {
	
	public List<EnumValue> getValueByType(Integer typeId) throws BaseDaoException;
	public List<EnumType> getAllType() throws BaseDaoException;
	public void saveType(EnumType enumType) throws BaseDaoException;
	public EnumType getTypeById(Integer typeId) throws BaseDaoException;
	public void saveValue(EnumValue enumValue) throws BaseDaoException;
	public EnumValue getValueById(Integer valueId) throws BaseDaoException;

}

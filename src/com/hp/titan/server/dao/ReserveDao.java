package com.hp.titan.server.dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.vo.ReserveVo;


public class ReserveDao extends DefaultBaseDao<Server, String> {

	public Reservation findById(String reserveId)throws BaseDaoException {
		return (Reservation) getHibernateTemplate().get(Reservation.class, reserveId);
	}

	public List<Reservation> findAllReservation(String type)throws BaseDaoException {
		String hql = "from com.hp.titan.server.model.Reservation as re where re.reservationType = ? and re.isValid=0";
		return this.getHibernateTemplate().find(hql,type);
	}
	public List<Reservation> findReservationByOwner(Integer userId)throws BaseDaoException {
		String hql = "from com.hp.titan.server.model.Reservation as re where re.userId = ? and re.isValid=0";
		return this.getHibernateTemplate().find(hql, userId);
	}
	public Reservation findReservationByServer(String serverId)throws BaseDaoException {
		String hql = "from com.hp.titan.server.model.Reservation as re where re.serverId = ? and re.isValid=0";
		List<Reservation> reservation = this.getHibernateTemplate().find(hql,serverId.toUpperCase());		
		return reservation.size()>0?reservation.get(0):null;
	}
	
	public void saveReservation(Reservation reservation)throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(reservation);
	}

	public void saveOrUpdate(Reservation reservation)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(reservation);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public List<ReserveVo> getMyServer(Integer userId,String showType)throws BaseDaoException {
		List<ReserveVo> resVoList  = new ArrayList<ReserveVo>();
		StringBuffer s = new StringBuffer();
		if(showType==null){
			showType="SERVERRES";
		}
		if(showType.equals("SERVERRES")){
			s.append("SELECT s.SERVER_ID, r.RESERVE_ID, s.SERVER_NAME, r.TAKE_USER_ID, s.STATUS, r.START_DATE, r.END_DATE, r.REMARK FROM reservation AS r, server AS s WHERE r.SERVER_ID = s.SERVER_ID AND r.IS_VALID = 0 AND r.USER_ID =" );
		}else if(showType.equals("CARDRES")){
			s.append("SELECT c.CARD_ID, r.RESERVE_ID, c.CARD_NAME, r.TAKE_USER_ID, c.CARD_STATUS, r.START_DATE, r.END_DATE, r.REMARK FROM reservation AS r, card AS c WHERE r.SERVER_ID = c.CARD_ID AND r.IS_VALID = 0 AND r.USER_ID =" );	
		}
		s.append(userId);
		Query query = this.getSession().createSQLQuery(s.toString());		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			ReserveVo resVo = new ReserveVo();
			resVo.setServerId(String.valueOf(object[0]));
			resVo.setReserveId(String.valueOf(object[1]));
			resVo.setServerName(String.valueOf(object[2]));
			if(null != object[3] && !"".equals(object[3])){
				resVo.setType(TitanContent.SERVER_STATUS_TAKEOVER);
			}
			else{
				resVo.setType(TitanContent.SERVER_STATUS_RESERVED);
			}
			resVo.setStatus(String.valueOf(object[4]));
			resVo.setStartDate(String.valueOf(object[5]));
			resVo.setEndDate(String.valueOf(object[6]));
			resVo.setRemark(String.valueOf(object[7]));
			resVoList.add(resVo);
		}
		return resVoList;
	}
}

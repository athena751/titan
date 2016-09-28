package com.hp.titan.server.dao;
import java.util.List;
import java.util.ArrayList;

import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.vo.*;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.model.Reservation;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import org.hibernate.Query;


public class ReserHistDao extends DefaultBaseDao<Server, String> {

	public ReserHist findById(String reserHistId)throws BaseDaoException {
		return (ReserHist) getHibernateTemplate().get(ReserHist.class, reserHistId);
	}

	public List<ReserHist> findAllReserHist()throws BaseDaoException {
		String hql = "from com.hp.titan.server.model.ReserHist as re where re.isValid=0";
		return this.getHibernateTemplate().find(hql);
	}
	public List<ReserHist> findReserHistByServer(String serverId)throws BaseDaoException {
		String hql = "from com.hp.titan.server.model.ReserHist as re where re.serverId = ? and re.isValid=0";
		return this.getHibernateTemplate().find(hql, serverId);
	}
	
	public List<ReserveHistVo> findReserHistVoByServer(String serverId)throws BaseDaoException {
		List<ReserveHistVo> rtn = new ArrayList<ReserveHistVo>();
		String sql = "select h.RESER_HIST_ID,r.RESERVE_ID, u.USER_CODE,h.ACTION_TYPE, h.START_DATE, h.END_DATE, h.CREATE_DATE "
			       + "from titan_user u, reservation r, reser_hist h "
			       + "where u.USER_ID=h.USER_ID and r.RESERVE_ID=h.RESERVE_ID and h.IS_VALID=0 and h.SERVER_ID = '" + serverId + "' order by h.CREATE_DATE desc";
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			ReserveHistVo vo = new ReserveHistVo();
			vo.setReserHistId(object[0].toString());
			vo.setReserveId(object[1].toString());
			vo.setUserName(object[2].toString());
			vo.setActionType(object[3].toString());
			if(null != object[4] && !"".equals(object[4])){
				vo.setStartDate(object[4].toString().substring(0, 10));
			}
			if(null != object[5] && !"".equals(object[5])){
				vo.setEndDate(object[5].toString().substring(0, 10));
			}
			if(null != object[6] && !"".equals(object[6])){
				vo.setCreateDate(object[6].toString().substring(0,19));
			}
			rtn.add(vo);			
		}
		return rtn;		
		
	}
	
	public void saveReserHist(ReserHist reserHist)throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(reserHist);
	}

	public void saveOrUpdate(ReserHist reserHist)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(reserHist);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}

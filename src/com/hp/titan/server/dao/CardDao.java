package com.hp.titan.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.titan.auth.model.User;
import com.hp.titan.project.model.Project;
import com.hp.titan.server.model.Card;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;

public class CardDao extends DefaultBaseDao<Card, String> {

	public Card findById(String cardId) throws BaseDaoException{
		return (Card) getHibernateTemplate().get(Card.class, cardId);
	}

	public List<Card> findAllCards() {
		String hql = "from com.hp.titan.cards.model.Cards as cards";
		return this.getHibernateTemplate().find(hql);
	}
	
	
	public void saveCards(Card card)throws BaseDaoException  {
		this.getHibernateTemplate().saveOrUpdate(card);
	}

	public void saveOrUpdate(Card card)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(card);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public Card getCardByName(String cardName) {
		String hql = "select c from Card c where c.cardName = ? and c.isValid=0";
		List<Card> card = this.getHibernateTemplate().find(hql,cardName.toUpperCase());
		return card.size()>0?card.get(0):null;
	}
	
	public List<CardVo> findAllCard(CardVo cardVo)throws BaseDaoException {
		List<CardVo> rtn = new ArrayList<CardVo>();
		String sql = "select c.CARD_ID, c.CARD_NAME, c.CARD_TYPE, c.SN, c.PN, c.VENDOR, c.PROJECT_ID, c.DESCRIPTION, c.SERVER_ID, c.CARD_STATE,"
			    + "p.PROJECT_ID, p.PROJECT_NAME, s.SERVER_NAME, c.OWNER_ID "			  
			    + "from card c, PROJECT p, SERVER s "
			    + "where c.PROJECT_ID=p.PROJECT_ID and s.SERVER_ID=c.SERVER_ID and c.IS_VALID=0";
		if (cardVo != null) {
			
			if (cardVo.getCardName() != null && !cardVo.getCardName().equals("")){
				sql += " and c.CARD_NAME like '%" + cardVo.getCardName() + "%'";
			}	
			if (cardVo.getServerId() != null && !cardVo.getServerId().equals("")){
				sql += " and c.SERVER_ID='" + cardVo.getServerId() + "'";
			}
			if (cardVo.getProjectId() != null && !cardVo.getProjectId().equals("")){
				sql += " and c.PROJECT_ID='" + cardVo.getProjectId() + "'";
			}
			if (cardVo.getGroupId()!= null && !cardVo.getGroupId().equals("")){
				sql += " and c.GROUP_ID='" + cardVo.getGroupId() + "'";
			}
			if (cardVo.getVendor()!= null && !cardVo.getVendor().equals("")){
				sql += " and c.VENDOR like '%" + cardVo.getVendor() + "%'";
			}
			if (cardVo.getCardSn()!= null && !cardVo.getCardSn().equals("")){
				sql += " and c.SN like '%" + cardVo.getCardSn() + "%'";
			}
			if (cardVo.getCardPn()!= null && !cardVo.getCardPn().equals("")){
				sql += " and c.PN like '%" + cardVo.getCardPn() + "%'";
			}
			if (cardVo.getCardType()!= null && !cardVo.getCardType().equals("")){
				sql += " and c.CARD_TYPE like '%" + cardVo.getCardType() + "%'";
			}
			if (cardVo.getCardState()!= null && !cardVo.getCardState().equals("")){
				sql += " and c.CARD_STATE like '%" + cardVo.getCardState() + "%'";
			}
		}
		
		sql +=" order by c.UPDATE_DATE desc";
		
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			CardVo vo = new CardVo();
			vo.setCardId(object[0].toString());
			vo.setCardName(object[1].toString());
			if (object[2] !=null){
			vo.setCardType(object[2].toString());
			}
			if (object[3] !=null){
				vo.setCardSn((object[3].toString()));
				}
			if (object[4] !=null){
			vo.setCardPn(object[4].toString());
			}
			if (object[5] != null){
				vo.setVendor(object[5].toString());
			}
			if (object[7] != null){
				vo.setDescription(object[7].toString());
			}
			if (object[9] != null){
				vo.setCardState(object[9].toString());
			}
			if (object[11] != null){
				vo.setProject(object[11].toString());
			}
			if (object[12] != null){
				vo.setServerName(object[12].toString());
			}
			if (object[13] != null){
				vo.setOwnerId(Integer.parseInt(object[13].toString()));
			}
		
			rtn.add(vo);
		}
		return rtn;
		
	}

}

package com.hp.titan.server.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.server.model.Card;
import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.model.Storage;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.server.vo.IPVo;
import com.hp.titan.server.vo.ReserveHistVo;
import com.hp.titan.server.vo.ReserveVo;
import com.hp.titan.server.vo.ServerVo;
import com.hp.titan.server.vo.StorageVo;

public interface ServerService {

	public List<Server> getAllServerList();
	
	public List<Reservation> getReserveByOwner(Integer userId)throws BaseDaoException;
	
	public List<Reservation> getAllReservation(String type)throws BaseDaoException;
	
	public List<ServerVo> findAllServer(ServerVo serverVo, String type) throws BaseException;
	
	public void findAllServerByRefresh() throws BaseException;
	
	public void insertOrUpdateDatabaseByCheckbox(JSONArray jsonArray) throws BaseException;
	
	public List<Server> getServerByOwner(Integer userId)throws BaseDaoException;
	
	public List<Server> findAllServerByDiscover(IPVo ips) throws BaseException;
	
	public List<Server> getServerByType(String serverType)throws BaseDaoException;
	
	public Reservation getReserveByServer(String serverId)throws BaseDaoException;

	public Server fillServer(String ip1,String ip2,String ip3,String ip4);
			
	
	public void doSaveServer(Server server)throws BaseDaoException;
	
	public boolean isExistServerName(String serverName);
 
	public Server getServerById(String serverId)throws BaseDaoException;
	
	public void doSaveReservation(Reservation reservation)throws BaseDaoException;
	
	public void doSaveReserHist(ReserHist reserHist)throws BaseDaoException;
	
	public Reservation getResById(String reserveId)throws BaseDaoException;
	
	public List<ReserveHistVo> findReserHistVoByServer(String serverId)throws BaseDaoException;
	
	public List<CardVo> findAllCard(CardVo cardVo) throws BaseDaoException;
	
	public List<StorageVo> findAllStorage(StorageVo storageVo) throws BaseDaoException;
	
	public void doSaveCard(Card card)throws BaseDaoException;
	
	public Card getCardById(String cardId)throws BaseDaoException;
	
	public List<Storage> getAllStorageList()throws BaseDaoException;
	
	public List<Storage> getStorageByGroup(Integer groupId)throws BaseDaoException;
	
	public void doSaveStorage(Storage storage)throws BaseDaoException;
	
	public Storage getStorageById(String torageId)throws BaseDaoException;
	
	public List<ReserveVo> getMyServer(Integer userId,String showType)throws BaseDaoException;
	
}

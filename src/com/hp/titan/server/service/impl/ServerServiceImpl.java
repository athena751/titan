package com.hp.titan.server.service.impl;

//import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.server.action.SnmpFactory;
import com.hp.titan.server.dao.CardDao;
import com.hp.titan.server.dao.ReserHistDao;
import com.hp.titan.server.dao.ReserveDao;
import com.hp.titan.server.dao.ServerDao;
import com.hp.titan.server.dao.StorageDao;
import com.hp.titan.server.model.Card;
import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.model.Storage;
import com.hp.titan.server.service.ServerService;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.server.vo.IPVo;
import com.hp.titan.server.vo.ReserveHistVo;
import com.hp.titan.server.vo.ReserveVo;
import com.hp.titan.server.vo.ServerVo;
import com.hp.titan.server.vo.StorageVo;

public class ServerServiceImpl implements ServerService{
	
   private ServerDao serverDao;
   private ReserveDao reserveDao;
   private ReserHistDao reserHistDao;
   private CardDao cardDao;
   private StorageDao storageDao;
	public ServerDao getServerDao() {
		return serverDao;
	}

	public void setServerDao(ServerDao serverDao) {
		this.serverDao = serverDao;
	}
	
	public Server fillServer(String ip1,String ip2,String ip3,String ip4){
		String IP = ip1+"."+ip2+"."+ip3+"."+ip4;
		return serverDao.fillStringArray(IP);
	}	
	
	public void doSaveServer(Server server)throws BaseDaoException {
		//server.setUpdateTime(DateUtils.date2Sz(new Date()));
		serverDao.saveOrUpdate(server);
	}
	
	public List<Server> getAllServerList(){
		return serverDao.findAllServer();
	}
	
	public List<Server> getServerByOwner(Integer userId)throws BaseDaoException{
		return serverDao.getServerByOwner(userId);
	}
	
	public List<Server> getServerByType(String serverType)throws BaseDaoException{
		return serverDao.getServerByType(serverType);
	}
	
	public List<Reservation> getReserveByOwner(Integer userId)throws BaseDaoException{
		return reserveDao.findReservationByOwner(userId);
	}
	public Reservation getReserveByServer(String serverId)throws BaseDaoException{
		return reserveDao.findReservationByServer(serverId);
	}
	public List<Reservation> getAllReservation(String type)throws BaseDaoException{
		return reserveDao.findAllReservation(type);
	}
	public List<ServerVo> findAllServer(ServerVo serverVo, String type) throws BaseException{
		return serverDao.findAllServer(serverVo, type);		
	}
	public void findAllServerByRefresh() throws BaseException{
		
		List<Server> listServer = serverDao.findAllServer();
		Server server1 = new Server();
		for(Server server : listServer){
			if(server.getConsoleIp()!=null && !"".equals(server.getConsoleIp()) && !" ".equals(server.getConsoleIp())&& !"Unknown".equalsIgnoreCase(server.getConsoleIp())){
				if(serverDao.isIpv4(server.getConsoleIp())==true){
					server1 = serverDao.fillStringArray(server.getConsoleIp());
					if(server1!=null && !"".equals(server1)){
						server.setMemory(server1.getMemory());
						server.setServerName(server1.getServerName());
						server.setDistro(server1.getDistro());
						server.setSn(server1.getSn());
						server.setServerIp(server1.getServerIp());
						server.setMacAddr(server1.getMacAddr());
						serverDao.saveOrUpdate(server);
					}	
				}
			}
		}
	}
	public void insertOrUpdateDatabaseByCheckbox(JSONArray jsonArray) throws BaseException{
		if(jsonArray.size()>0){
			Date currentDate = new Date();
			Iterator<JSONObject> iter = jsonArray.iterator();
			while(iter.hasNext()){
				JSONObject js = iter.next();
				Server server = new Server();
				server.setServerName(js.getString("serverName"));
				server.setServerIp(js.getString("serverIp"));
				server.setConsoleIp(js.getString("consoleIp"));
				server.setCpu(js.getString("cpu"));
				server.setDistro(js.getString("distro"));
				server.setMemory(js.getString("memory"));
				server.setDescription(js.getString("remark"));
				if(js.getString("groupName")!=null && !"".equals(js.getString("groupName"))){
					server.setGroupId(Integer.parseInt(js.getString("groupName")));
				}
				server.setServerType(js.getString("enumValue"));
				server.setProjectId(js.getString("projectId"));
				if(server.getKeyServer()==null){
					server.setKeyServer(0);
				}
				server.setServerState(TitanContent.SERVER_STATUS_FREE);
				server.setIsValid(0);
				server.setOwnerId(0);
				server.setStatus("Not used");
				server.setCreateUserId(UserSessionUtil.getUser().getUserId());
				server.setCreateDate(currentDate);
				server.setUpdateDate(currentDate);
				server.setUpdateUserId(UserSessionUtil.getUser().getUserId());
				serverDao.saveOrUpdate(server);
			  
			}
		}
	}
	public List<Server> findAllServerByDiscover(IPVo ips) throws BaseException{
		List<Server> listServer = new ArrayList<Server>();
	    int[] ipBegin = {ips.getIp11(),ips.getIp12(),ips.getIp13(),ips.getIp14()-1};
		int[] ipEnd = {ips.getIp21(),ips.getIp22(),ips.getIp23(),ips.getIp24()};
		while (true) {
			if (ipBegin[3] != ipEnd[3]) {
				ipBegin[3] += 1;
			} else if (ipBegin[2] != ipEnd[2]) {
				ipBegin[3] = 0;
				ipBegin[2] += 1;
			} else if (ipBegin[1] != ipEnd[1]) {
				ipBegin[3] = 0;
				ipBegin[2] = 0;
				ipBegin[1] += 1;
			} else if (ipBegin[0] != ipEnd[0]) {
				ipBegin[3] = 0;
				ipBegin[2] = 0;
				ipBegin[1] = 0;
				ipBegin[0] += 1;
			} else {
				System.out.println("end");
				break;
			}
			String ip = ipBegin[0]+"."+ipBegin[1]+"."+ipBegin[2]+"."+ipBegin[3];
			Server server = serverDao.fillStringArray(ip);
			if(server!=null){
				Server s = serverDao.getServerByServerName(server.getServerName());
				if(s==null){
					listServer.add(server);
				}
			}
		}
		return listServer;
	}
	public boolean isExistServerName(String serverName) {
		// TODO Auto-generated method stub
		Server server = serverDao.getServerByServerName(serverName);
		if(server==null){
			return false;
		}else{
			return true;
		}
	}
	public Server getServerById(String serverId)throws BaseDaoException{
		return serverDao.findById(serverId);		
	}

		
	public void doSaveReservation(Reservation reservation)throws BaseDaoException {
			//server.setUpdateTime(DateUtils.date2Sz(new Date()));
			reserveDao.saveOrUpdate(reservation);
	}
	public void doSaveReserHist(ReserHist reserHist)throws BaseDaoException {
		//server.setUpdateTime(DateUtils.date2Sz(new Date()));
		   reserHistDao.saveOrUpdate(reserHist);
    }
	
	public Reservation getResById(String reserveId)throws BaseDaoException{
		   
		   return reserveDao.findById(reserveId);
	}
    
	public List<ReserveHistVo> findReserHistVoByServer(String serverId)throws BaseDaoException{
		
		return reserHistDao.findReserHistVoByServer(serverId);
		   
	}
	
	public List<CardVo> findAllCard(CardVo cardVo) throws BaseDaoException{
		
		return cardDao.findAllCard(cardVo);
		   
	}
    public List<StorageVo> findAllStorage(StorageVo storageVo) throws BaseDaoException{
		
		return storageDao.findAllStorage(storageVo);
		   
	}
	public void doSaveCard(Card card)throws BaseDaoException {
		//server.setUpdateTime(DateUtils.date2Sz(new Date()));
		cardDao.saveOrUpdate(card);
	}
	
	public Card getCardById(String cardId)throws BaseDaoException {
		return cardDao.findById(cardId);
	}
	
	public List<Storage> getAllStorageList()throws BaseDaoException {
		return storageDao.findAllStorage();
	}
	
	public List<Storage> getStorageByGroup(Integer groupId)throws BaseDaoException {
		return storageDao.findStorageByGroup(groupId);
	}
	
	public void doSaveStorage(Storage storage)throws BaseDaoException {
		//server.setUpdateTime(DateUtils.date2Sz(new Date()));
		storageDao.saveOrUpdate(storage);
	}
	public Storage getStorageById(String torageId)throws BaseDaoException {
		return storageDao.findById(torageId);
	}
	/**
	 * get my server reservation or my card reservation
 	 *@param userId user id
 	 *@param showType show reservation type
	 */
	public List<ReserveVo> getMyServer(Integer userId,String showType)throws BaseDaoException {
		return reserveDao.getMyServer(userId,showType);
	}
	
	public ReserveDao getReserveDao() {
			return reserveDao;
	}


	public void setReserveDao(ReserveDao reserveDao) {
			this.reserveDao = reserveDao;
	}

	public ReserHistDao getReserHistDao() {
		return reserHistDao;
	}

	public void setReserHistDao(ReserHistDao reserHistDao) {
		this.reserHistDao = reserHistDao;
	}

	public CardDao getCardDao() {
		return cardDao;
	}

	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}

	public StorageDao getStorageDao() {
		return storageDao;
	}

	public void setStorageDao(StorageDao storageDao) {
		this.storageDao = storageDao;
	}
		
	
}
	
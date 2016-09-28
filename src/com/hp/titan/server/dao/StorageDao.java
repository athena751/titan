package com.hp.titan.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.titan.auth.model.User;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.project.model.Project;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Storage;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.server.vo.StorageVo;
import com.hp.titan.server.vo.ServerVo;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;

public class StorageDao extends DefaultBaseDao<Storage, String> {

	public Storage findById(String storageId) throws BaseDaoException{
		return (Storage) getHibernateTemplate().get(Storage.class, storageId);
	}

	public List<Storage> findAllStorage() {
		String hql = "from com.hp.titan.server.model.Storage as storage";
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Storage> findStorageByGroup(Integer groupId) {
		String hql = "from com.hp.titan.server.model.Storage as storage where storage.groupId = ?";
		return this.getHibernateTemplate().find(hql, groupId);
	}
	
	public List<StorageVo> findAllStorage(StorageVo storageVo)throws BaseDaoException {
		List<StorageVo> rtn = new ArrayList<StorageVo>();
		String sql = "select s.STORAGE_ID, s.STORAGE_NAME, s.STORAGE_TYPE, s.SN, s.PN, s.CAPACITY, s.SLOT_NUM, s.SLOT_USED, s.PROJECT_ID, s.DESCRIPTION, "
			    + "p.PROJECT_ID, p.PROJECT_NAME "			  
			    + "from storage s, PROJECT p "
			    + "where s.PROJECT_ID=p.PROJECT_ID and s.IS_VALID=0";
		if (storageVo != null) {
			
			if (storageVo.getStorageName() != null && !storageVo.getStorageName().equals("")){
				sql += " and s.STORAGE_NAME like '%" + storageVo.getStorageName() + "%'";
			}	
		
			if (storageVo.getProjectId() != null && !storageVo.getProjectId().equals("")){
				sql += " and s.PROJECT_ID='" + storageVo.getProjectId() + "'";
			}
			if (storageVo.getGroupId()!= null && !storageVo.getGroupId().equals("")){
				sql += " and s.GROUP_ID='" + storageVo.getGroupId() + "'";
			}			
			if (storageVo.getStorageType()!= null && !storageVo.getStorageType().equals("")){
				sql += " and s.STORAGE_TYPE like '%" + storageVo.getStorageType() + "%'";
			}
		}
		
		sql +=" order by s.UPDATE_DATE desc";
		
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
		List<Object[]> objects = query.list();
		for (Object[] object : objects){
			StorageVo vo = new StorageVo();
			vo.setStorageId(object[0].toString());
			vo.setStorageName(object[1].toString());
			if (object[2] !=null){
			vo.setStorageType(object[2].toString());
			}
			if (object[3] !=null){
				vo.setSn((object[3].toString()));
				}
			if (object[4] !=null){
			vo.setPn(object[4].toString());
			}
			if (object[5] != null){
				vo.setCapacity(object[5].toString());
			}
			if (object[6] != null){
				vo.setSlotNum(object[6].toString());
			}
			if (object[7] != null){
				vo.setSlotUsed(object[7].toString());
			}
			if (object[10] != null){
				vo.setProject(object[10].toString());
			}
			if (object[8] != null){
				vo.setDescription(object[8].toString());
			}			
		
			rtn.add(vo);
		}
		return rtn;
		
	}
//
//	public List<ServerVo> findAllServer(ServerVo serverVo, String type)throws BaseDaoException {
//		List<ServerVo> rtn = new ArrayList<ServerVo>();
//		String sql = "select s.SERVER_ID, s.SERVER_NAME, s.SERVER_IP, s.DISTRO, s.SERVER_STATE, s.OWNER_ID, s.PROJECT_ID, s.DESCRIPTION, "
//			    + "u.USER_CODE, p.PROJECT_ID, p.PROJECT_NAME, "
//			    + "s.LOCATION, s.SN, s.MAC_ADDR, s.SERVER_TYPE, s.MEMORY, s.CPU "
//			    + "from server s, titan_user u, PROJECT p "
//			    + "where s.OWNER_ID=u.USER_ID and s.PROJECT_ID=p.PROJECT_ID and s.IS_VALID=0";
//		
//		if(type.equals("list")) sql +=" and s.KEY_SERVER=0";
//		if (serverVo != null) {
//			
//			if (serverVo.getServerName() != null && !serverVo.getServerName().equals("")){
//				sql += " and s.Server_NAME like '%" + serverVo.getServerName() + "%'";
//			}
//			if (serverVo.getServerIp() != null && !serverVo.getServerIp().equals("")){
//				sql += " and s.Server_IP like '%" + serverVo.getServerIp() + "%'";
//			}
//			if (serverVo.getDistro() != null && !serverVo.getDistro().equals("")){
//				sql += " and s.DISTRO like '%" + serverVo.getDistro() + "%'";
//			}
//			if (serverVo.getServerStatus() != null && !serverVo.getServerStatus().equals("")){
//				sql += " and s.SERVER_STATE like '%" + serverVo.getServerStatus() + "%'";
//			}
//			if (serverVo.getOwnerId() != null && !serverVo.getOwnerId().equals("")){
//				sql += " and s.OWNER_ID='" + serverVo.getOwnerId() + "'";
//			}
//			if (serverVo.getProjectId() != null && !serverVo.getProjectId().equals("")){
//				sql += " and s.PROJECT_ID='" + serverVo.getProjectId() + "'";
//			}
//			if (serverVo.getLocation()!= null && !serverVo.getLocation().equals("")){
//				sql += " and s.LOCATION like '%" + serverVo.getLocation() + "%'";
//			}
//			if (serverVo.getSn()!= null && !serverVo.getSn().equals("")){
//				sql += " and s.SN like '%" + serverVo.getSn() + "%'";
//			}
//			if (serverVo.getMacAddr()!= null && !serverVo.getMacAddr().equals("")){
//				sql += " and s.MAC_ADDR like '%" + serverVo.getMacAddr() + "%'";
//			}
//			if (serverVo.getServerType()!= null && !serverVo.getServerType().equals("")){
//				sql += " and s.SERVER_TYPE like '%" + serverVo.getServerType() + "%'";
//			}
//			if (serverVo.getMemory()!= null && !serverVo.getMemory().equals("")){
//				sql += " and s.MEMORY like '%" + serverVo.getMemory() + "%'";
//			}
//			if (serverVo.getCpu()!= null && !serverVo.getCpu().equals("")){
//				sql += " and s.CPU like '%" + serverVo.getCpu() + "%'";
//			}
//		}
//		
//		sql +=" order by s.UPDATE_DATE desc";
//		
//		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
//		List<Object[]> objects = query.list();
//		for (Object[] object : objects){
//			ServerVo vo = new ServerVo();
//			vo.setServerId(object[0].toString());
//			vo.setServerName(object[1].toString());
//			if (object[2] !=null){
//			vo.setServerIp(object[2].toString());
//			}
//			if (object[3] !=null){
//				vo.setDistro((object[3].toString()));
//				}
//			if (object[4] !=null){
//			vo.setServerStatus(object[4].toString());
//			}			
//			if (object[7] != null){
//			    vo.setRemark(object[7].toString());
//			}	
//			if (object[8] !=null){
//				vo.setOwner(object[8].toString());		
//				}
//			if (object[10] != null){
//				vo.setProject(object[10].toString());
//			}
//			if (object[11] != null){
//				vo.setLocation(object[11].toString());
//			}
//			if (object[12] != null){
//				vo.setSn(object[12].toString());
//			}
//			if (object[13] != null){
//				vo.setMacAddr(object[12].toString());
//			}
//			if (object[14] != null){
//				vo.setServerType(object[14].toString());
//			}
//			if (object[15] != null){
//				vo.setMemory(object[15].toString());
//			}
//			if (object[16] != null){
//				vo.setCpu(object[16].toString());
//			}
//			if (object[4].toString().equals(TitanContent.SERVER_STATUS_RESERVED)){
//				
//				String hql = "from com.hp.titan.server.model.Reservation as re where re.serverId = ? and re.isValid=0";
//				List<Reservation> reservationList = this.getHibernateTemplate().find(hql,object[0].toString().toUpperCase());		
//				vo.setResNote(reservationList.get(0).getRemark());
//				}			
//			
//			rtn.add(vo);
//		}
//		return rtn;
//		
//	}

	public void saveStorage(Storage storage)throws BaseDaoException  {
		this.getHibernateTemplate().saveOrUpdate(storage);
	}

	public void saveOrUpdate(Storage storage)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(storage);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public Storage getStorageByName(String storageName) {
		String hql = "select s from Storage s where s.storageName = ? and s.isValid=0";
		List<Storage> storage = this.getHibernateTemplate().find(hql,storageName.toUpperCase());
		return storage.size()>0?storage.get(0):null;
	}
	

}

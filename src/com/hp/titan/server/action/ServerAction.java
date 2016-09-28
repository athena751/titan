package com.hp.titan.server.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.UserGroup;
import com.hp.titan.auth.service.GroupService;
import com.hp.titan.auth.service.RoleService;
import com.hp.titan.auth.service.UserService;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.enumtype.model.EnumValue;
import com.hp.titan.enumtype.service.EnumtypeService;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.server.model.Card;
import com.hp.titan.server.model.ReserHist;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.model.Storage;
import com.hp.titan.server.service.ServerService;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.server.vo.IPVo;
import com.hp.titan.server.vo.ReDeviceVo;
import com.hp.titan.server.vo.ResReportVo;
import com.hp.titan.server.vo.ReserveHistVo;
import com.hp.titan.server.vo.ReserveVo;
import com.hp.titan.server.vo.ServerVo;
import com.hp.titan.server.vo.StorageVo;


public class ServerAction extends DefaultBaseAction {
	private Server server;
	private String serverName;
	private List<Group> groupList;
	private List<Server> serverList;
	private List<ServerVo> serverVoList;
	private List<CardVo> cardVoList;
	private List<User> userList;
	public GroupService groupService;
	public ServerService serverService;
	private RoleService roleService;
	private List<Project> projectList;
	public ProjectService projectService;
	private UserService userService;
	private String[] serverIdAry;
	private int isAdmin;
	private UserVo userVo = new UserVo();
	private ServerVo serverVo;
	private CardVo cardVo;
	private StorageVo storageVo;
	private Reservation reservation;
	private String serverId;
	private List<Reservation> reserveList;
	private List<ReserveVo> reserveVoList;
	private String reserveId;
	private Integer currentUserId;
	private List<ReserveHistVo> histVoList;
	private List<ReserveHistVo> histVoSubList;	
	private ReserveVo reserveVo;
	private String currentUserName;
	private int isView;
	private String fromTag;
	private Card card;
	private String cardId;
	private String type;
	private List<EnumValue> enumList;
	private EnumtypeService enumtypeService;
	private List<Storage> storageList;
	private List<StorageVo> storageVoList;
	private Storage storage;
	private String storageId;
	private ReDeviceVo reDeviceVo;
	private ResReportVo resReportVo;
	private List<ReDeviceVo> reDeviceVoList;	
	private List<ReDeviceVo> reCardVoList;
	private List<ReDeviceVo> reStorageVoList;
	private List<ResReportVo> resReportList;
	private List<ResReportVo> resProjReportList;
	private List<EnumValue> enumCardList;
	private List<EnumValue> enumStorageList;
	private String projectId;
	private String showType;
	private String navigation = " <div class=\"port_bar\"><a href=\"../project/projectList.do\" class=\"marfin_lef10\">" + UserSessionUtil.getDefaultProjectName() + "</a>";
	
	int serverTotal=0;
	int cardTotal=0;
	int storageTotal=0;

	//new parameters
	
	private String ip1;
	private String ip2;
	private String ip3;
	private String ip4;
	
	private IPVo ips;
	private String strJson;
	
	public String doSearchServer(){
		 isAdmin = 0;
		 if(serverVo == null)   serverVo = new ServerVo();
		 if(cardVo == null)   cardVo = new CardVo();
		 try{
			User currentUser = UserSessionUtil.getUser();
			serverList = serverService.getAllServerList();
			serverVo.setGroupId(currentUser.getGroupId());
			serverVoList = serverService.findAllServer(serverVo, "list");
			cardVo.setGroupId(currentUser.getGroupId());
			cardVoList = serverService.findAllCard(cardVo);
			userList = userService.getAllUserByUserVo(userVo);
			projectList = projectService.getAllProjectList();
			currentUserName = currentUser.getUserCode();
			currentUserId = currentUser.getUserId();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }catch (BaseException e) {
				e.printStackTrace();
		  }
		  if(showType==null){
			  showType = "SERVERRES";
		  }
		  navigation += "->Reservation</div>";
		return SUCCESS;
	}
	
	public String doSearchCard(){
		
		 isAdmin = 0;
		 if(cardVo == null)   cardVo = new CardVo();
		 try{
			User currentUser = UserSessionUtil.getUser();
			cardVo.setGroupId(currentUser.getGroupId());
//			cardList = serverService.getAllCardList();
			cardVoList = serverService.findAllCard(cardVo);
//			userList = userService.getAllUserByUserVo(userVo);
			projectList = projectService.getAllProjectList();
			serverList = serverService.getAllServerList();
			
			currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }catch (BaseException e) {
				e.printStackTrace();
		  }
		  navigation += "->Card</div>";
		return SUCCESS;
		
	}
	
	public String goAdvanceSearch(){
		
		 isAdmin = 0;
		 if(serverVo == null)   serverVo = new ServerVo();
		 try{
		    User currentUser = UserSessionUtil.getUser();
			serverList = serverService.getAllServerList();
			serverVo.setGroupId(currentUser.getGroupId());
			serverVoList = serverService.findAllServer(serverVo, "list");
			userList = userService.getAllUserByUserVo(userVo);
			projectList = projectService.getAllProjectList();
			currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }catch (BaseException e) {
				e.printStackTrace();
		  }
		return SUCCESS;
		
	}
	
	public String doAdvanceSearch(){
		
		isAdmin = 0;
		 if(serverVo == null)   serverVo = new ServerVo();
		 try{
			User currentUser = UserSessionUtil.getUser();
			serverList = serverService.getAllServerList();
			serverVo.setGroupId(currentUser.getGroupId());
			serverVoList = serverService.findAllServer(serverVo, "list");
			userList = userService.getAllUserByUserVo(userVo);
			projectList = projectService.getAllProjectList();
			
			currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }catch (BaseException e) {
				e.printStackTrace();
		  }
		return SUCCESS;
		
	}
	
	public String serverAdmin(){
		 isAdmin = 0;
		 if(serverVo == null)   serverVo = new ServerVo();
		 try{
			User currentUser = UserSessionUtil.getUser();
			serverList = serverService.getAllServerList();
			serverVo.setGroupId(currentUser.getGroupId());
			serverVoList = serverService.findAllServer(serverVo, "admin");
			userList = userService.getAllUserByUserVo(userVo);
			projectList = projectService.getAllProjectList();
			currentUserName = currentUser.getUserCode();
			List<Role> roles = new ArrayList<Role>(currentUser.getUserRoles());
			if (roles.size() > 0) {
				String roleName = roles.get(0).getRoleName();	
				if("ADMIN".equals(roleName)) isAdmin = 1;
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }catch (BaseException e) {
				e.printStackTrace();
		  }
		  navigation += "->Server</div>";
		return SUCCESS;
	}
	
	public String serverDiscover(){
		try{
			if(ips!=null && !"".equals(ips)){
				serverList = serverService.findAllServerByDiscover(ips);
				groupList = groupService.getAllGroupList();
				enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_SERVER);
				Set<Project> s = UserSessionUtil.getUser().getUserProjects();
//				projectList = projectService.getAllProjectList(s);
				projectList = new ArrayList<Project>();
				if(null != s && s.size() != 0){
					Iterator<Project> it = s.iterator();
					while(it.hasNext()){
						Project p = it.next();
						if(p.getIsValid() == 0){
							projectList.add(p);
						}
					}
				}
			}
		}catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goServerReg(){		
		server = new Server();	
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			groupList = groupService.getAllGroupList();
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_SERVER);
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doServerSave(){
		Date currentDate = new Date();
		try{
			if(server.getKeyServer()==null){
				server.setKeyServer(0);
			}
			server.setIsValid(0);
			server.setOwnerId(0);
			if(server.getKeyServer()==1) server.setServerState(TitanContent.SERVER_STATUS_KEY);
			else server.setServerState(TitanContent.SERVER_STATUS_FREE);
//			server.setStatus("Not used");
			server.setCreateUserId(UserSessionUtil.getUser().getUserId());
			server.setCreateDate(currentDate);
			server.setUpdateDate(currentDate);
			server.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			serverService.doSaveServer(server);	
		}catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return SUCCESS;
	}
	/**
	 * 方法说明：AJAX检查server是否存在
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String checkServerName() throws IOException {
		 PrintWriter out = this.getResponse().getWriter();
		  if(serverService.isExistServerName(serverName)){
			  out.print("exist");
			  out.flush();
		  }else{
			  out.print("noexist");
			  out.flush();
		  }
		out.close();
		return null;
	}

	public String goServerUpdate(){
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			groupList = groupService.getAllGroupList();
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if ((this.serverId != null) && !this.serverId.equals("")) {	
			      server= serverService.getServerById(this.serverId);
			      return SUCCESS;
			    }else if((this.serverIdAry != null) && (this.serverIdAry.length == 1)) {			 
			      String updateId = this.serverIdAry[0];
			      server= serverService.getServerById(updateId);
			      return SUCCESS;
			    }
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return ERROR;
	}
	
	public String goServerUpdateAll(){
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();
			groupList = groupService.getAllGroupList();
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_SERVER);
			
//			serverTypeList = enumtypeService.getValueByType("903e421646d1363a0146d13740800001");
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if ((this.serverId != null) && !this.serverId.equals("")) {	
			      server= serverService.getServerById(this.serverId);
			      return SUCCESS;
			    }else if((this.serverIdAry != null) && (this.serverIdAry.length == 1)) {			 
			      String updateId = this.serverIdAry[0];
			      server= serverService.getServerById(updateId);
			      return SUCCESS;
			    }
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return ERROR;
	}
	
	public String doServerUpdate(){
		Date currentDate = new Date();
		try{
			Server s = serverService.getServerById(serverId);
			if(null != s){
				s.setUpdateUserId(UserSessionUtil.getUser().getUserId());
				s.setUpdateDate(currentDate);
				s.setDescription(server.getDescription());
				s.setHastab(server.getHastab());
				s.setGroupId(server.getGroupId());
				s.setConsoleIp(server.getConsoleIp());
				s.setProjectId(server.getProjectId());
				s.setServerIp(server.getServerIp());
				s.setServerPasswd(server.getServerPasswd());
				s.setPn(server.getPn());
				s.setPurchaseDate(server.getPurchaseDate());
				s.setStatus(server.getStatus());
				serverService.doSaveServer(s);
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		return SUCCESS;
	}
	
	public String doServerUpdateAll(){
		Date currentDate = new Date();
		try{
			Server s = serverService.getServerById(server.getServerId());
			if(null != s){
				server.setIsValid(0);
				if(server.getKeyServer()!= null&& server.getKeyServer()==1) 
				    { server.setServerState(TitanContent.SERVER_STATUS_KEY);
				}else{
					server.setKeyServer(0);
				}			
				server.setCreateDate(s.getCreateDate());
				server.setCreateUserId(s.getCreateUserId());
//				server.setStatus(s.getStatus());
				server.setServerState(s.getServerState());
				server.setUpdateUserId(UserSessionUtil.getUser().getUserId());
				server.setUpdateDate(currentDate);
				serverService.doSaveServer(server);
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		return SUCCESS;
	}
	
	public String goServerDisable(){
		Date currentDate = new Date();
		try{
			if ((this.serverId != null) && !this.serverId.equals("")) {	
				 server= serverService.getServerById(this.serverId);
			}
			server.setIsValid(1);
			server.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			server.setUpdateDate(currentDate);
			serverService.doSaveServer(server);
			
		}catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		return SUCCESS;
	}
	
	public String goServerView(){
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();			
			groupList = groupService.getAllGroupList();
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if ((this.serverId != null) && !this.serverId.equals("")) {	
			      server= serverService.getServerById(this.serverId);
			      if(server.getServerState().equals(TitanContent.SERVER_STATUS_RESERVED)){
			    	  reservation = serverService.getReserveByServer(this.serverId);
			          reserveVo = new ReserveVo();
			          reserveVo.setUserName(userService.getUserById(reservation.getUserId()).getUserCode());
			          reserveVo.setStartDate(reservation.getStartDate().toString().substring(0, 10));
			          reserveVo.setEndDate(reservation.getEndDate().toString().substring(0, 10));
			      }			      
			  
			 }
			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return SUCCESS;
	}
	
	public String goCardViewDetail(){
		try {
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();			
			groupList = groupService.getAllGroupList();
			projectList = new ArrayList<Project>();
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_CARD);
			serverList = serverService.getServerByType(TitanContent.SERVER_TYPE_X86);
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if((this.cardId != null) && !this.cardId.equals("")) {
				card = serverService.getCardById(this.cardId);
				if(card.getCardState().equals(TitanContent.CARD_STATUS_RESERVED)){
					reservation = serverService.getReserveByServer(this.cardId);
					reserveVo = new ReserveVo();
					reserveVo.setUserName(userService.getUserById(reservation.getUserId()).getUserCode());
					reserveVo.setStartDate(reservation.getStartDate().toString().substring(0, 10));
					reserveVo.setEndDate(reservation.getEndDate().toString().substring(0, 10));
				}	
			}
			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String goServerViewFromSearch(){
		try {
			fromTag = "searchPage";
			Set<Project> s = UserSessionUtil.getUser().getUserProjects();			
			groupList = groupService.getAllGroupList();
//			projectList = projectService.getAllProjectList(s);
			projectList = new ArrayList<Project>();
			if(null != s && s.size() != 0){
				Iterator<Project> it = s.iterator();
				while(it.hasNext()){
					Project p = it.next();
					if(p.getIsValid() == 0){
						projectList.add(p);
					}
				}
			}
			
			if ((this.serverId != null) && !this.serverId.equals("")) {	
			      server= serverService.getServerById(this.serverId);
			      
			      if(server.getServerState().equals(TitanContent.SERVER_STATUS_RESERVED)){
			    	  reservation = serverService.getReserveByServer(this.serverId);
			          reserveVo = new ReserveVo();
			          reserveVo.setUserName(userService.getUserById(reservation.getUserId()).getUserCode());
			          reserveVo.setStartDate(reservation.getStartDate().toString().substring(0, 10));
			          reserveVo.setEndDate(reservation.getEndDate().toString().substring(0, 10));
			      }			      
			  
			 }
			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return SUCCESS;
	}
	
	
	public String goServerReserve(){		
		reservation = new Reservation();
		reserveVo = new ReserveVo();
		try {
			server = serverService.getServerById(serverId);	
			userList = userService.getAllUserByUserVo(userVo);
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			currentUserId = UserSessionUtil.getUser().getUserId();	
			histVoList = serverService.findReserHistVoByServer(serverId);	
			if(histVoList.size()>=5){
				histVoSubList = histVoList.subList(0, 5);
				
			}			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showType="SERVERRES";
		return SUCCESS;
	}
	
	public String goCardReserve(){		
		reservation = new Reservation();
		reserveVo = new ReserveVo();
		try {
			card = serverService.getCardById(cardId);
			userList = userService.getAllUserByUserVo(userVo);
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			currentUserId = UserSessionUtil.getUser().getUserId();	
			histVoList = serverService.findReserHistVoByServer(cardId);	
			if(histVoList.size()>=5){
				histVoSubList = histVoList.subList(0, 5);				
			}			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showType="CARDRES";
		return SUCCESS;
	}
	
	public String goReserveUpdate(){		
		
		try {
			if ((this.reserveId != null) && !this.reserveId.equals("")) {
				reservation = serverService.getResById(reserveId);
				reserveVo = new ReserveVo();
				reserveVo.setStartDate(reservation.getStartDate().toString().substring(0, 10));
				reserveVo.setEndDate(reservation.getEndDate().toString().substring(0, 10));
			}
			if(showType==null){
				showType = "SERVERRES";
			}
			if(showType.equals("SERVERRES")){
				server = serverService.getServerById(reservation.getServerId());	
			}else if(showType.equals("CARDRES")){
				card = serverService.getCardById(reservation.getServerId());
			}
			userList = userService.getAllUserByUserVo(userVo);
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			currentUserId = UserSessionUtil.getUser().getUserId();	
			histVoList = serverService.findReserHistVoByServer(reservation.getServerId());			
			if(histVoList.size()>=5){
				histVoSubList = histVoList.subList(0, 5);				
			}
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String goReserveView(){	
		isView = 1;
		
		try {
			if ((this.reserveId != null) && !this.reserveId.equals("")) {
				reservation = serverService.getResById(reserveId);
				reserveVo = new ReserveVo();
				reserveVo.setStartDate(reservation.getStartDate().toString());
				reserveVo.setEndDate(reservation.getEndDate().toString());
			}
			if(showType==null){
				showType = "SERVERRES";
			}
			if(showType.equals("SERVERRES")){
				server = serverService.getServerById(reservation.getServerId());	
			}else if(showType.equals("CARDRES")){
				card = serverService.getCardById(reservation.getServerId());
			}
			userList = userService.getAllUserByUserVo(userVo);
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			currentUserId = UserSessionUtil.getUser().getUserId();	
			histVoList = serverService.findReserHistVoByServer(reservation.getServerId());
			if(histVoList.size()>=5){
				histVoSubList = histVoList.subList(0, 5);				
			}
			
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String updateReserve(){	
		  try{
			 int currentUserId = UserSessionUtil.getUser().getUserId();
			 Date currentDate = new Date();
			 String mark = reservation.getRemark();
			 reservation = serverService.getResById(reservation.getReserveId());
			 reservation.setStartDate(DateUtils.convertStringToDate(reserveVo.getStartDate()));
			 reservation.setEndDate(DateUtils.convertStringToDate(reserveVo.getEndDate()));		
			 reservation.setUpdateDate(currentDate);
			 reservation.setUpdateUserId(currentUserId);
			 reservation.setRemark(mark);
			 serverService.doSaveReservation(reservation);			
			//save reservation history
			 this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_RESERVE);			 
			//notify
			 EmailManageAction email = new EmailManageAction();
			 String title = "Reservation notify";
//			 User resUser = userService.getUserById(UserSessionUtil.getUser().getUserId());
			 String name = "";
			 if(showType==null){
				 showType = "SERVERRES";
			 }
			 if(showType.equals("SERVERRES")){
				 name = server.getServerName();
			 }else if(showType.equals("CARDRES")){
				 name = card.getCardName();
			 }
		     email.sendEmail(title, this.createEmailContext(title,reservation,UserSessionUtil.getUser().getUserCode(), name,TitanContent.SERVER_ACTION_RESERVE),UserSessionUtil.getUser().getMail());
		  } catch(BaseDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }catch (ParseException e) {
				e.printStackTrace();
		  }
		  
		  return SUCCESS;
		}
	
	
	public String saveReserv(){	
	  try{		  
		 if ((this.serverId != null) && !this.serverId.equals("")) {	
		      server= serverService.getServerById(this.serverId);
		      server.setServerState(TitanContent.SERVER_STATUS_RESERVED); 
		      server.setOwnerId(UserSessionUtil.getUser().getUserId());		
		      serverService.doSaveServer(server);	
		 }else if((this.cardId != null) && !this.cardId.equals("")) {
			  card = serverService.getCardById(this.cardId);
			  card.setCardState(TitanContent.CARD_STATUS_RESERVED);
			  card.setOwnerId(UserSessionUtil.getUser().getUserId());
		      serverService.doSaveCard(card);
		      reservation.setServerId(cardId);
		 }
		 int currentUserId = UserSessionUtil.getUser().getUserId();
		 Date currentDate = new Date();
		 reservation.setIsValid(0);
		 reservation.setUserId(UserSessionUtil.getUser().getUserId());
		 reservation.setStartDate(DateUtils.convertStringToDate(reserveVo.getStartDate()));
		 reservation.setEndDate(DateUtils.convertStringToDate(reserveVo.getEndDate()));
		 reservation.setCreateDate(currentDate);
		 reservation.setCreateUserId(currentUserId);
		 if(!"".equals(showType) && showType.equals("SERVERRES")){
			 reservation.setReservationType(TitanContent.RESERVATION_TYPE_SERVER);
		 }else if(!"".equals(showType) && showType.equals("CARDRES")){
			 reservation.setReservationType(TitanContent.RESERVATION_TYPE_CARD);
		 }else{
			 reservation.setReservationType(TitanContent.RESERVATION_TYPE_SERVER);
		 }
		 serverService.doSaveReservation(reservation);
		
		//save reservation history
		 this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_RESERVE);
		 
		//notify
		 EmailManageAction email = new EmailManageAction();
		 String title = "Reservation notify";
//		 User resUser = userService.getUserById(UserSessionUtil.getUser().getUserId());
		 
	     email.sendEmail(title, this.createEmailContext(title,reservation,UserSessionUtil.getUser().getUserCode(), server.getServerName(),TitanContent.SERVER_ACTION_RESERVE),UserSessionUtil.getUser().getMail());
	  } catch(BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }catch (ParseException e) {
			e.printStackTrace();
		}
	  
	  return SUCCESS;
	}
	
	
	private void saveReserHistory(Reservation reservation, String type){
		
		try{
			 int currentUserId = UserSessionUtil.getUser().getUserId();
//			 Date currentDate = new Date();
			 ReserHist reserHist = new ReserHist();
			 reserHist.setActionType(type);		 
			 reserHist.setServerId(reservation.getServerId());
			 reserHist.setReserveId(reservation.getReserveId());
			 reserHist.setUserId(UserSessionUtil.getUser().getUserId());
//			 reserHist.setCreateDate(currentDate);
			 if(reservation.getUpdateDate()!=null){
				 reserHist.setCreateDate(reservation.getUpdateDate());
			 }else{
				 reserHist.setCreateDate(reservation.getCreateDate());
			 }
			 reserHist.setCreateUserId(currentUserId);
			 reserHist.setIsValid(0);
			 reserHist.setReserHistType(reservation.getReservationType());
			 reserHist.setStartDate(reservation.getStartDate());
			 reserHist.setEndDate(reservation.getEndDate());
			 reserHist.setRemark(reservation.getRemark());
			 serverService.doSaveReserHist(reserHist);	
			 
		}catch(BaseDaoException e) {
			e.printStackTrace();
	  }		
	}
	
//	private String createEmailContext(String title, Reservation reservation,String userName, String serverName){
//		StringBuffer sb = new StringBuffer();
//		sb.append(title);
//		sb.append("<br>");		
//		sb.append("<br>");
//		sb.append(serverName + " reserved by " + userName + " from " + reservation.getStartDate() + " to " + reservation.getEndDate());
//		sb.append("<br>");
//		sb.append("<br>");
//		sb.append("Check output at " + TitanContent.TITAN_URL + " to view the details.");
//		sb.append("<br>");
//		sb.append("<br>");
//		sb.append("DO NOT REPLY TO THIS EMAIL." );
//		return sb.toString();
//	}
//	
	public String reserveList(){ 		 
		 try{
			User currentUser = UserSessionUtil.getUser();
			reserveVoList = serverService.getMyServer(currentUser.getUserId(),showType);
			if(showType==null){
				showType="SERVERRES";
			}
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }
		  navigation += "->My reservations</div>";
		return SUCCESS;
	}
	
	public List<ResReportVo> getResReportList() {
		return resReportList;
	}

	/**
	 * return data from ip
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String returnDataFromIp() {
		PrintWriter out = null;
	    server = serverService.fillServer(ip1,ip2,ip3,ip4);
		try {
			out = this.getResponse().getWriter();
			JSONArray json;
			this.getResponse().setHeader("Cache-Control", "no-cache");
			if(server==null){
				 json = JSONArray.fromObject("");
			}else{
				 json = JSONArray.fromObject(server);
			}
			out.print(json);
			out.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
		return SUCCESS;
	}
	
	public void refreshFromiloIP(){
		PrintWriter out = null;
		 try{
			serverService.findAllServerByRefresh();
			out = this.getResponse().getWriter();
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	public void insertOrUpdateDatabase(){
		PrintWriter out = null;
		JSONObject json = JSONObject.fromObject(strJson);
		JSONArray jsonArray = json.getJSONArray("strJSON");
		try{
			serverService.insertOrUpdateDatabaseByCheckbox(jsonArray);
			out = this.getResponse().getWriter();
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}
		
	}
	
	public void setResReportList(List<ResReportVo> resReportList) {
		this.resReportList = resReportList;
	}

	public String releaseServer(){
		try{
			if ((this.reserveId != null) && !this.reserveId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				// clear reservation
				reservation = serverService.getResById(this.reserveId);
				reservation.setIsValid(1);
				reservation.setUpdateDate(currentDate);
				reservation.setUpdateUserId(currentUserId);
				serverService.doSaveReservation(reservation);
				// update server status to free
				server = serverService.getServerById(reservation.getServerId());
				server.setServerState(TitanContent.SERVER_STATUS_FREE);
				server.setOwnerId(0);
				serverService.doSaveServer(server);
				
				//save reservation history
				 this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_RELEASE);
				 EmailManageAction email = new EmailManageAction();
				 String title = "Server Release Notify";
				 User resUser = userService.getUserById(reservation.getUserId());
				 email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_RELEASE),resUser.getMail());
				 
			}		
			
			 
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		
		
		return SUCCESS;
	}
	
	public String releaseCard(){
		try{
			if ((this.reserveId != null) && !this.reserveId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				// clear reservation
				reservation = serverService.getResById(this.reserveId);
				reservation.setIsValid(1);
				reservation.setUpdateDate(currentDate);
				reservation.setUpdateUserId(currentUserId);
				serverService.doSaveReservation(reservation);
				// update card status to free
				
				card = serverService.getCardById(reservation.getServerId());
				card.setCardState(TitanContent.CARD_STATUS_FREE);
				card.setOwnerId(0);
				serverService.doSaveCard(card);
				
				//save reservation history
				this.saveReserHistory(reservation, TitanContent.CARD_ACTION_RELEASE);
				EmailManageAction email = new EmailManageAction();
				String title = "Card Release Notify";
				User resUser = userService.getUserById(reservation.getUserId());
				email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), card.getCardName(), TitanContent.CARD_ACTION_RELEASE),resUser.getMail());
				
			}		
			
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		
		showType="CARDRES";
		return SUCCESS;
	}
	
	private String createEmailContext(String title, Reservation reservation, String userName, String serverName, String type){
//		HttpServletRequest httpRequest= (HttpServletRequest)request;
//		String titanURL = "http://" + request.getServerName()
//		                 + ":"
//		                 +request.getServerPort()
//		                 +httpRequest.getContextPath()
//		                 +"Login.do";
		StringBuffer sb = new StringBuffer();
		sb.append(title);
		sb.append("<br>");		
		sb.append("<br>");
		if(type.equals(TitanContent.SERVER_ACTION_RESERVE)){
			sb.append(serverName + " reserved by " + userName + " from " + reservation.getStartDate() + " to " + reservation.getEndDate());
		}
		if(type.equals(TitanContent.SERVER_ACTION_RELEASE)){
		    sb.append(serverName + " released by " + userName);
		}else if (type.equals(TitanContent.SERVER_ACTION_TAKEOVER)){
		    sb.append(serverName + " taken over by " + userName);	
		}else if (type.equals(TitanContent.SERVER_ACTION_RETURN)){
			sb.append(serverName + " returned by " + userName);				
		}
		sb.append("<br>");
		sb.append("<br>");
		sb.append("Check output at " + TitanContent.TITAN_URL + " to view the details.");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("DO NOT REPLY TO THIS EMAIL." );
		return sb.toString();
	}
	
	public String goServerTakeOver(){
		
		try{
			if(this.serverId != null && !this.serverId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				//update reservation info
				reservation = serverService.getReserveByServer(this.serverId);
			    reservation.setTakeUserId(reservation.getUserId());
			    reservation.setUserId(UserSessionUtil.getUser().getUserId());
			    reservation.setUpdateDate(currentDate);
			    reservation.setUpdateUserId(currentUserId);
			    serverService.doSaveReservation(reservation);
			    
			    //update server info
			    server= serverService.getServerById(this.serverId);
			    server.setServerState(TitanContent.SERVER_STATUS_TAKEOVER); 
			    server.setOwnerId(UserSessionUtil.getUser().getUserId());		
			    serverService.doSaveServer(server);
			    
			    //history record
			    this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_TAKEOVER);
			    
			    //notify
			    EmailManageAction email = new EmailManageAction();
				String title = "Server Take over Notify";
				User resUser = userService.getUserById(reservation.getUserId());
				User takeUser = userService.getUserById(reservation.getTakeUserId());
				email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_TAKEOVER),resUser.getMail());
				email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_TAKEOVER),takeUser.getMail());			    
				
			    
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		
		return SUCCESS;
		
	}
	
	public String goCardTakeOver(){
		
		try{
			if(this.cardId != null && !this.cardId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				//update reservation info
				reservation = serverService.getReserveByServer(this.cardId);
				reservation.setTakeUserId(reservation.getUserId());
				reservation.setUserId(UserSessionUtil.getUser().getUserId());
				reservation.setUpdateDate(currentDate);
				reservation.setUpdateUserId(currentUserId);
				serverService.doSaveReservation(reservation);
				
				//update server info
				card= serverService.getCardById(this.cardId);
				card.setCardState(TitanContent.CARD_STATUS_TAKEOVER);
				card.setOwnerId(UserSessionUtil.getUser().getUserId());
				serverService.doSaveCard(card);
				//notify
				EmailManageAction email = new EmailManageAction();
				String title = "Card Take over Notify";
				User resUser = userService.getUserById(reservation.getUserId());
				User takeUser = userService.getUserById(reservation.getTakeUserId());
				email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), card.getCardName(), TitanContent.CARD_ACTION_TAKEOVER),resUser.getMail());
				email.sendEmail(title, this.createEmailContext(title,reservation, resUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_TAKEOVER),takeUser.getMail());
				
				//history record
				this.saveReserHistory(reservation, TitanContent.CARD_ACTION_TAKEOVER);
				
				
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		showType="CARDRES";
		return SUCCESS;
		
	}
	
	public String returnServer(){
		
		try{
			
			if ((this.reserveId != null) && !this.reserveId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				// clear reservation
				reservation = serverService.getResById(this.reserveId);
				reservation.setUserId(reservation.getTakeUserId());
				reservation.setTakeUserId(null);	
				reservation.setUpdateDate(currentDate);
				reservation.setUpdateUserId(currentUserId);
				serverService.doSaveReservation(reservation);
				// update server status to free
				server = serverService.getServerById(reservation.getServerId());
				server.setServerState(TitanContent.SERVER_STATUS_RESERVED);
				server.setOwnerId(reservation.getUserId());
				serverService.doSaveServer(server);
				
				//notify
				
				 //notify
			    EmailManageAction email = new EmailManageAction();
				String title = "Server Return Notify";
				User returnUser = UserSessionUtil.getUser();
					
				//	userService.getUserById(reservation.getUserId());
				User formerOwner = userService.getUserById(reservation.getUserId());
				email.sendEmail(title, this.createEmailContext(title, reservation, returnUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_RETURN),returnUser.getMail());
				email.sendEmail(title, this.createEmailContext(title, reservation, returnUser.getUserCode(), server.getServerName(), TitanContent.SERVER_ACTION_RETURN),formerOwner.getMail());
				
				//history record
				this.saveReserHistory(reservation, TitanContent.SERVER_ACTION_RETURN);
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		
		return SUCCESS;
	}
	
	public String returnCard(){
		try{
			if ((this.reserveId != null) && !this.reserveId.equals("")){
				int currentUserId = UserSessionUtil.getUser().getUserId();
				Date currentDate = new Date();
				// clear reservation
				reservation = serverService.getResById(this.reserveId);
				reservation.setUserId(reservation.getTakeUserId());
				reservation.setTakeUserId(null);	
				reservation.setUpdateDate(currentDate);
				reservation.setUpdateUserId(currentUserId);
				serverService.doSaveReservation(reservation);
				// update card status to free
				card = serverService.getCardById(reservation.getServerId());
				card.setCardState(TitanContent.CARD_STATUS_RESERVED);
				card.setOwnerId(reservation.getUserId());
				serverService.doSaveCard(card);
				
				//history record
				this.saveReserHistory(reservation, TitanContent.CARD_ACTION_RETURN);
				
				//notify
				EmailManageAction email = new EmailManageAction();
				String title = "Card Return Notify";
				User returnUser = UserSessionUtil.getUser();
				
				//	userService.getUserById(reservation.getUserId());
				User formerOwner = userService.getUserById(reservation.getUserId());
				email.sendEmail(title, this.createEmailContext(title, reservation, returnUser.getUserCode(), card.getCardName(), TitanContent.CARD_ACTION_RETURN),returnUser.getMail());
								
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		showType="CARDRES";
		return SUCCESS;
	}

	public String checkServerStatus() throws IOException {
		 PrintWriter out = this.getResponse().getWriter();
		 try{
			server = serverService.getServerById(serverId);
			if(server.getServerState().equals(TitanContent.SERVER_STATUS_RESERVED)){
				  out.print("reserved");
				  out.flush();
			}else if(server.getServerState().equals(TitanContent.SERVER_STATUS_FREE)){
				  out.print("free");
				  out.flush();
			}else if(server.getServerState().equals(TitanContent.SERVER_STATUS_TAKEOVER)){
				  out.print("takeover");
				  out.flush();
			}	 
		}catch (BaseDaoException e) {
			e.printStackTrace();
    	}finally{
    		out.close();
    	}	
		return null;
	}
	
	public String checkCardStatus() throws IOException {
		PrintWriter out = this.getResponse().getWriter();
		try{
			card = serverService.getCardById(cardId);
			if(card.getCardState().equals(TitanContent.CARD_STATUS_RESERVED)){
				out.print("reserved");
				out.flush();
			}else if(card.getCardState().equals(TitanContent.CARD_STATUS_FREE)){
				out.print("free");
				out.flush();
			}else if(card.getCardState().equals(TitanContent.CARD_STATUS_TAKEOVER)){
				out.print("takeover");
				out.flush();
			}	 
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}	
		return null;
	}
	
	public String goCardReg(){
		card = new Card();	
		try {		
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			serverList = serverService.getServerByType(TitanContent.SERVER_TYPE_X86);
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_CARD);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block		
		}
		return SUCCESS;
		
	}
	
	public String doCardSave(){
		
		Date currentDate = new Date();
		try{		
			card.setIsValid(0);
			card.setOwnerId(1);				
			card.setCreateUserId(UserSessionUtil.getUser().getUserId());
			card.setCreateDate(currentDate);
			card.setUpdateDate(currentDate);
			card.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			serverService.doSaveCard(card);
		}catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return SUCCESS;
		
	}
	
	public ReDeviceVo getReDeviceVo() {
		return reDeviceVo;
	}

	public void setReDeviceVo(ReDeviceVo reDeviceVo) {
		this.reDeviceVo = reDeviceVo;
	}

	public String goStorageReg(){
		storage = new Storage();	
		try {		
			groupList = groupService.getAllGroupList();
			projectList = projectService.getAllProjectList();
			serverList = serverService.getAllServerList();
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_STORAGE);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block		
		}
		return SUCCESS;
		
	}
	
	
	
    public String doStorageSave(){
		
		Date currentDate = new Date();
		try{		
			storage.setIsValid(0);
			storage.setOwnerId(1);				
			storage.setCreateUserId(UserSessionUtil.getUser().getUserId());
			storage.setCreateDate(currentDate);
			storage.setUpdateDate(currentDate);
			storage.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			serverService.doSaveStorage(storage);
		}catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return SUCCESS;
		
	}
	public List<Storage> getStorageList() {
		return storageList;
	}

	public void setStorageList(List<Storage> storageList) {
		this.storageList = storageList;
	}

	public String goCardUpdate(){
		try {
			
			type = "edit";
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_CARD);
			groupList = groupService.getAllGroupList();
            projectList = projectService.getAllProjectList();
            serverList = serverService.getAllServerList();
			
			if ((this.cardId != null) && !this.cardId.equals(""))	
			      card= serverService.getCardById(this.cardId);

		  } catch (BaseDaoException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		 
		   return SUCCESS;		
	}
	
	public String goStorageUpdate(){
		try {
			
			type = "edit";
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_STORAGE);
			groupList = groupService.getAllGroupList();
            projectList = projectService.getAllProjectList();
//            serverList = serverService.getAllServerList();
			
			if ((this.storageId != null) && !this.storageId.equals(""))	
			      storage= serverService.getStorageById(this.storageId);

		  } catch (BaseDaoException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		 
		   return SUCCESS;		
	}
	
	public String goCardView(){
		try {
			
			type = "view";
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_CARD);
			groupList = groupService.getAllGroupList();
            projectList = projectService.getAllProjectList();
            serverList = serverService.getAllServerList();
			
			if ((this.cardId != null) && !this.cardId.equals(""))	
			      card= serverService.getCardById(this.cardId);

		  } catch (BaseDaoException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		 
		   return SUCCESS;		
	}
	
	public String goStorageView(){
		try {
			
			type = "view";
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_STORAGE);
			groupList = groupService.getAllGroupList();
            projectList = projectService.getAllProjectList();
//          serverList = serverService.getAllServerList();
			
			if ((this.storageId != null) && !this.storageId.equals(""))	
				  storage= serverService.getStorageById(this.storageId);

		  } catch (BaseDaoException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		 
		   return SUCCESS;		
	}

	public String doStorageUpdate(){
		
		Date currentDate = new Date();
		try{
			if ((this.storageId != null) && !this.storageId.equals("")) {	
			      storage.setStorageId(this.storageId);
			}
			storage.setIsValid(0);
			storage.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			storage.setUpdateDate(currentDate);
			serverService.doSaveStorage(storage);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		return SUCCESS;		
	}
	
	public String doCardUpdate(){
		
		Date currentDate = new Date();
		try{
			if ((this.cardId != null) && !this.cardId.equals("")) {	
			      card.setCardId(this.cardId);
			}
			card.setIsValid(0);
			card.setUpdateUserId(UserSessionUtil.getUser().getUserId());
			card.setUpdateDate(currentDate);
			serverService.doSaveCard(card);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(showType!=null && !"".equals(showType)){
			return "cardResUpdate";
		}
		return SUCCESS;		
	}
	
	public String doSearchStorage(){		
		
		 try{
			 User currentUser = UserSessionUtil.getUser();				
			 storageList = serverService.getStorageByGroup(currentUser.getGroupId());
		  }catch (BaseDaoException e) {
				e.printStackTrace();
		  }
		  navigation += "->Storage</div>";
		return SUCCESS;
		
	}
	
	public ReDeviceVo getDeviceReVoByProject(String projectId, String serverType){
		reDeviceVo  = new ReDeviceVo();
		reDeviceVo.setServerType(serverType);
		serverVo = new ServerVo();
		if(!projectId.equals("all")) serverVo.setProjectId(projectId);
		serverVo.setServerType(serverType);
		try{
			User currentUser = UserSessionUtil.getUser();
			serverVo.setGroupId(currentUser.getGroupId());
			serverVo.setServerStatus(TitanContent.SERVER_STATUS_FREE);
			serverVoList = serverService.findAllServer(serverVo, "");
			reDeviceVo.setFreeNum(serverVoList.size());
			serverVo.setServerStatus(TitanContent.SERVER_STATUS_RESERVED);
			serverVoList = serverService.findAllServer(serverVo, "");
			reDeviceVo.setReserveNum(serverVoList.size());
			
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		 }catch (BaseException e) {
			e.printStackTrace();
		 }
		
		
		return reDeviceVo;
		
	}
	public ReDeviceVo getCardReVoByProject(String projectId, String cardType){
		reDeviceVo  = new ReDeviceVo();
		reDeviceVo.setCardType(cardType);
		cardVo = new CardVo();
		if(!projectId.equals("all")) cardVo.setProjectId(projectId);
		cardVo.setCardType(cardType);
		try{
			User currentUser = UserSessionUtil.getUser();
			cardVo.setGroupId(currentUser.getGroupId());			
			cardVoList = serverService.findAllCard(cardVo);
			reDeviceVo.setCardNum(cardVoList.size());
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		 }catch (BaseException e) {
			e.printStackTrace();
		 }
		
		
		return reDeviceVo;
		
	}
	
	public ReDeviceVo getStorageReVoByProject(String projectId, String storageType){
		reDeviceVo  = new ReDeviceVo();
		reDeviceVo.setStorageType(storageType);
		storageVo = new StorageVo();
		if(!projectId.equals("all")) storageVo.setProjectId(projectId);
		storageVo.setStorageType(storageType);
		try{
			User currentUser = UserSessionUtil.getUser();
			storageVo.setGroupId(currentUser.getGroupId());	
			storageVoList = serverService.findAllStorage(storageVo);
			reDeviceVo.setStorageNum(storageVoList.size());
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		 }catch (BaseException e) {
			e.printStackTrace();
		 }
		
		return reDeviceVo;
		
	}
	
	public String goDeviceReport(){
		
		try {
			User currentUser = UserSessionUtil.getUser();		
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			if ((this.projectId != null) && !this.projectId.equals("")) {
				projectId = this.projectId;
				this.createDeviceReVoList(projectId);		
			}else{
				projectId = "all";
				this.createDeviceReVoList(projectId);
			}
			   
		 } catch (BaseException e) {
			e.printStackTrace();
		 }
		 navigation += "->Device report</div>";
		 return SUCCESS;
		 
		}

	public void createDeviceReVoList(String projectId) {
		reDeviceVoList = new ArrayList<ReDeviceVo>();
		reCardVoList = new ArrayList<ReDeviceVo>();
		reStorageVoList = new ArrayList<ReDeviceVo>();
		try {
			enumList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_SERVER);
			enumCardList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_CARD);
			enumStorageList = enumtypeService.getValueByType(TitanContent.META_DATA_TYPE_STORAGE);
			
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
		
		if (enumList != null && enumList.size() != 0) {
			for (EnumValue type : enumList) {
				reDeviceVoList.add(this.getDeviceReVoByProject(projectId, type.getEnumValue()));
			}
			
			for(ReDeviceVo servervo: reDeviceVoList){
				serverTotal += servervo.getFreeNum() + servervo.getReserveNum();
			}
		}
		if (enumCardList != null && enumCardList.size() != 0) {
			for (EnumValue type : enumCardList) {
				reCardVoList.add(this.getCardReVoByProject(projectId, type.getEnumValue()));
			}
			for(ReDeviceVo cardvo: reCardVoList){
				cardTotal += cardvo.getCardNum();
			}
		}
		if (enumStorageList != null && enumStorageList.size() != 0) {
			for (EnumValue type : enumStorageList) {
				reStorageVoList.add(this.getStorageReVoByProject(projectId, type.getEnumValue()));
			}
			for(ReDeviceVo storagedvo: reStorageVoList){
				storageTotal += storagedvo.getStorageNum();
			}
		}
	}
	
//	public void getChartXml(){
//		PrintWriter out = null;
//		this.createResReportListForProject();
//		if ((this.projectId != null) && !this.projectId.equals("")) {
//				projectId = this.projectId;
//				this.createDeviceReVoList(projectId);
//				this.createResReportList(projectId);		
//		}else{
//				projectId = "all";
//				this.createDeviceReVoList(projectId);
//				this.createResReportList(projectId);		
//		}
//		
//		String strXml = "";
//		if("1".equals(showType)){
//			strXml = this.getChartXmlForCard();
//		}
//		else if("2".equals(showType)){
//			strXml = this.getChartXmlForStorage();
//		}else if("0".equals(showType)){
//			strXml = this.getChartXmlForServer();
//		}else if("3".equals(showType)){
//			strXml = this.getChartXmlForReserve();			
//		}else if("4".equals(showType)){
//			strXml = this.getChartXmlForPorjReserve();
//			
//		}
//		try {
//			out = this.getResponse().getWriter();
//			out.print(strXml);
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			out.close();
//		}
//	}
	//jqplot
	public void getChartXml(){
		PrintWriter out = null;
		JSONArray json =new JSONArray();
		this.createResReportListForProject();
		if ((this.projectId != null) && !this.projectId.equals("")) {
				projectId = this.projectId;
				this.createDeviceReVoList(projectId);
				this.createResReportList(projectId);	//return resReportList	
		}else{
				projectId = "all";
				this.createDeviceReVoList(projectId);
				this.createResReportList(projectId);		
		}
		
		if("1".equals(showType)){
			json = JSONArray.fromObject(reCardVoList);
		}
		else if("2".equals(showType)){
			json = JSONArray.fromObject(reStorageVoList);
		}else if("0".equals(showType)){
			json = JSONArray.fromObject(reDeviceVoList);
		}else if("3".equals(showType)){
			json = JSONArray.fromObject(resReportList);
		}else if("4".equals(showType)){
			json = JSONArray.fromObject(resProjReportList);			
		}
		try {
			out = this.getResponse().getWriter();
			out.print(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	
	public void getChartXmlForRes(){
		PrintWriter out = null;
	
		if ((this.projectId != null) && !this.projectId.equals("")) {
				projectId = this.projectId;
				this.createResReportList(projectId);		
		}else{
				projectId = "all";
				this.createResReportList(projectId);
		}
		
		String strXml = "";
			strXml = this.getChartXmlForReserve();
	
		try {
			out = this.getResponse().getWriter();
			out.print(strXml);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public String getChartXmlForCard(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Cards in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");

		String[] colorList = {"00FF00", "FF3300", "CCCCFF", "33CCCC", "99FF99","88FF88"};
		if(reCardVoList!=null &&reCardVoList.size()!=0){
			int i=0;
			for(ReDeviceVo cardReport : reCardVoList){
				Element passElement = rootElement.addElement("set");
				passElement.addAttribute("label", cardReport.getCardType() );
				passElement.addAttribute("value", String.valueOf(cardReport.getCardNum()));
				passElement.addAttribute("color",colorList[i]);
				i++;				
			}
		}

		return document.asXML();
	}
	
	public String getChartXmlForReserve(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Reservations By User");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");

		String[] colorList = {"FFEFD5", "DEB887", "CDCD00", "CDAA7D", "BF3EFF","9BCD9B","00F5FF","00F5FF"};
		if(resReportList!=null &&resReportList.size()!=0){
			int i=0;
			for(ResReportVo resReport : resReportList){
				Element passElement = rootElement.addElement("set");
				passElement.addAttribute("label", resReport.getUserCode());
				passElement.addAttribute("value", String.valueOf(resReport.getReserveNum()));
				if(i<colorList.length){
				    passElement.addAttribute("color",colorList[i]);
				}else{
					passElement.addAttribute("color",colorList[i-colorList.length]);
				}
				i++;				
			}
		}

		return document.asXML();
	}
	public String getChartXmlForPorjReserve(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Reservations in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");

		String[] colorList = {"FFEFD5", "DEB887", "CDCD00", "CDAA7D", "BF3EFF","9BCD9B","00F5FF","00F5FF"};
		if(resProjReportList!=null &&resProjReportList.size()!=0){
			int i=0;
			for(ResReportVo resReport : resProjReportList){
				Element passElement = rootElement.addElement("set");
				passElement.addAttribute("label", resReport.getProjectName());
				passElement.addAttribute("value", String.valueOf(resReport.getRePorjNum()));
				if(i<colorList.length){
				    passElement.addAttribute("color",colorList[i]);
				}else{
					passElement.addAttribute("color",colorList[i-colorList.length]);
				}
				i++;				
			}
		}

		return document.asXML();
	}
	
	
	public String getChartXmlForStorage(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Stroages in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Sprints");
		rootElement.addAttribute("yAxisName", "Test jobs");
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");

		String[] colorList = {"00FF00", "FF3300", "CCCCFF", "33CCCC", "99FF99","88FF88"};
		if(reStorageVoList!=null &&reStorageVoList.size()!=0){
			int i=0;
			for(ReDeviceVo storageReport : reStorageVoList){
				Element passElement = rootElement.addElement("set");
				passElement.addAttribute("label", storageReport.getStorageType() );
				passElement.addAttribute("value", String.valueOf(storageReport.getStorageNum()));
				passElement.addAttribute("color",colorList[i]);
				i++;				
			}
		}

		return document.asXML();
	}
	
	public String getChartXmlForServer(){
		Document document = DocumentHelper.createDocument();
		// root element
		Element rootElement=document.addElement("chart");
		rootElement.addAttribute("showLegend", "1");
		rootElement.addAttribute("legendPosition", "RIGHT");
		rootElement.addAttribute("caption", "Servers in Project");
		rootElement.addAttribute("showValues", "0");
		rootElement.addAttribute("xAxisName", "Server Type");
		rootElement.addAttribute("yAxisName", "Server Status");//paletteColors 
		rootElement.addAttribute("paletteColors", "00FF00, FF3300, CCCCFF, 33CCCC, 99FF99, 88FF88");
		
		// columns element
		Element categoriesElement = rootElement.addElement("categories");
		// pass data element
		Element freeDataElement = rootElement.addElement("dataset");
		freeDataElement.addAttribute("seriesName", "free");
		
		// fail data element
		Element resDataElement = rootElement.addElement("dataset");
		resDataElement.addAttribute("seriesName", "reserved");
		
		
		Iterator<ReDeviceVo> it = reDeviceVoList.iterator();
		while(it.hasNext()){
			ReDeviceVo rv = it.next();
			Element sprintElement = categoriesElement.addElement("category");
			sprintElement.addAttribute("label", rv.getServerType());
			
			Element passValueElement = freeDataElement.addElement("set");
			passValueElement.addAttribute("value", String.valueOf(rv.getFreeNum()));
			passValueElement.addAttribute("color", "00FF00");
			
			Element failValueElement = resDataElement.addElement("set");
			failValueElement.addAttribute("value", String.valueOf(rv.getReserveNum()));
			failValueElement.addAttribute("color", "FF3300");			
			
		}
		return document.asXML();
	}
	
	public String goResReport(){
		try{
			User currentUser = UserSessionUtil.getUser();
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
			this.createResReportListForProject();
			if ((this.projectId != null) && !this.projectId.equals("")) {
				this.createResReportList(this.projectId);		
			}else{
				projectId = "all";
				this.createResReportList(projectId);
			}
			navigation += "->Reservation report</div>";
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	public void createResReportListForProject(){
		resProjReportList = new ArrayList<ResReportVo>();
		try{
			User currentUser = UserSessionUtil.getUser();
			projectList = projectService.getProjectByGroup(currentUser.getGroupId());
		    if(projectList!=null && projectList.size()!=0){
			for(Project proj : projectList){
				 String servers = "";
				 serverVo = new ServerVo();		
				 serverVo.setProjectId(proj.getProjectId());
				 serverVo.setServerStatus(TitanContent.SERVER_STATUS_RESERVED);
				 serverVoList = serverService.findAllServer(serverVo, "list");
				 
				 if(serverVoList!=null&&serverVoList.size()!=0){
					 for(ServerVo server :serverVoList){
						 servers += server.getServerName() + ",  "; 
					 }
				 }
				 resReportVo = new ResReportVo();
				 resReportVo.setRePorjNum(serverVoList.size());
				 resReportVo.setProjectName(proj.getProjectName());
				 resReportVo.setServers(servers);
				 resProjReportList.add(resReportVo);
			}
		}
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}catch (BaseException e) {
			e.printStackTrace();
		}
	}
	
	public void createResReportList(String projectId) {
		resReportList = new ArrayList<ResReportVo>();
		try {
			User currentUser = UserSessionUtil.getUser();
			List<User> userList1 = userService.getAllUserByUserVo(userVo);
			userList = new ArrayList<User>();
			if(userList1!=null && userList1.size()!=0){
				for(User user : userList1){
					UserGroup group = groupService.findGroupUserById(user.getUserId());
					if((group!=null)&&(group.getGroup().getGroupId()==currentUser.getGroupId())){
						userList.add(user);
					    if(user.getUserId()!=0){
					    	String servers = "";
					        serverVo = new ServerVo();					 
					        serverVo.setOwnerId(user.getUserId());
					        if(!projectId.equals("all")) serverVo.setProjectId(projectId);
					        serverVoList = serverService.findAllServer(serverVo, "list");
					 
					    if(serverVoList!=null&&serverVoList.size()!=0){
						     for(ServerVo server :serverVoList){
							    servers += server.getServerName() + ",  "; 
						      }
					     }
					     resReportVo = new ResReportVo();
					     resReportVo.setReserveNum(serverVoList.size());
					     resReportVo.setUserCode(user.getUserCode());
					     resReportVo.setServers(servers);
					     resReportList.add(resReportVo);
					 }
				   }
				}
			}
		 }catch (BaseDaoException e) {
			e.printStackTrace();
		}catch (BaseException e) {
			e.printStackTrace();
		}
	}
			
	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<ServerVo> getServerVoList() {
		return serverVoList;
	}

	public void setServerVoList(List<ServerVo> serverVoList) {
		this.serverVoList = serverVoList;
	}

	public ServerVo getServerVo() {
		return serverVo;
	}

	public void setServerVo(ServerVo serverVo) {
		this.serverVo = serverVo;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public List<Reservation> getReserveList() {
		return reserveList;
	}

	public void setReserveList(List<Reservation> reserveList) {
		this.reserveList = reserveList;
	}

	public List<ReserveVo> getReserveVoList() {
		return reserveVoList;
	}

	public void setReserveVoList(List<ReserveVo> reserveVoList) {
		this.reserveVoList = reserveVoList;
	}

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public Integer getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}
	
	public String[] getServerIdAry() {
		return serverIdAry;
	}

	public void setServerIdAry(String[] serverIdAry) {
		this.serverIdAry = serverIdAry;
	}

	public List<Server> getServerList() {
		return serverList;
	}

	public void setServerList(List<Server> serverList) {
		this.serverList = serverList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}



	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public ServerService getServerService() {
		return serverService;
	}

	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}

	public List<ReserveHistVo> getHistVoList() {
		return histVoList;
	}

	public void setHistVoList(List<ReserveHistVo> histVoList) {
		this.histVoList = histVoList;
	}

	public ReserveVo getReserveVo() {
		return reserveVo;
	}

	public void setReserveVo(ReserveVo reserveVo) {
		this.reserveVo = reserveVo;
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public int getIsView() {
		return isView;
	}

	public void setIsView(int isView) {
		this.isView = isView;
	}

	public CardVo getCardVo() {
		return cardVo;
	}

	public void setCardVo(CardVo cardVo) {
		this.cardVo = cardVo;
	}

	public List<CardVo> getCardVoList() {
		return cardVoList;
	}

	public void setCardVoList(List<CardVo> cardVoList) {
		this.cardVoList = cardVoList;
	}

	public List<ReserveHistVo> getHistVoSubList() {
		return histVoSubList;
	}

	public void setHistVoSubList(List<ReserveHistVo> histVoSubList) {
		this.histVoSubList = histVoSubList;
	}

	public String getFromTag() {
		return fromTag;
	}

	public void setFromTag(String fromTag) {
		this.fromTag = fromTag;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EnumtypeService getEnumtypeService() {
		return enumtypeService;
	}

	public void setEnumtypeService(EnumtypeService enumtypeService) {
		this.enumtypeService = enumtypeService;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public List<EnumValue> getEnumList() {
		return enumList;
	}

	public void setEnumList(List<EnumValue> enumList) {
		this.enumList = enumList;
	}

	public List<ReDeviceVo> getReDeviceVoList() {
		return reDeviceVoList;
	}

	public void setReDeviceVoList(List<ReDeviceVo> reDeviceVoList) {
		this.reDeviceVoList = reDeviceVoList;
	}

	public List<ReDeviceVo> getReCardVoList() {
		return reCardVoList;
	}

	public void setReCardVoList(List<ReDeviceVo> reCardVoList) {
		this.reCardVoList = reCardVoList;
	}

	public List<ReDeviceVo> getReStorageVoList() {
		return reStorageVoList;
	}

	public void setReStorageVoList(List<ReDeviceVo> reStorageVoList) {
		this.reStorageVoList = reStorageVoList;
	}

	public List<EnumValue> getEnumCardList() {
		return enumCardList;
	}

	public void setEnumCardList(List<EnumValue> enumCardList) {
		this.enumCardList = enumCardList;
	}

	public List<EnumValue> getEnumStorageList() {
		return enumStorageList;
	}

	public void setEnumStorageList(List<EnumValue> enumStorageList) {
		this.enumStorageList = enumStorageList;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public StorageVo getStorageVo() {
		return storageVo;
	}

	public void setStorageVo(StorageVo storageVo) {
		this.storageVo = storageVo;
	}

	public List<StorageVo> getStorageVoList() {
		return storageVoList;
	}

	public void setStorageVoList(List<StorageVo> storageVoList) {
		this.storageVoList = storageVoList;
	}

	public int getServerTotal() {
		return serverTotal;
	}

	public void setServerTotal(int serverTotal) {
		this.serverTotal = serverTotal;
	}

	public int getCardTotal() {
		return cardTotal;
	}

	public void setCardTotal(int cardTotal) {
		this.cardTotal = cardTotal;
	}

	public int getStorageTotal() {
		return storageTotal;
	}

	public void setStorageTotal(int storageTotal) {
		this.storageTotal = storageTotal;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public ResReportVo getResReportVo() {
		return resReportVo;
	}

	public void setResReportVo(ResReportVo resReportVo) {
		this.resReportVo = resReportVo;
	}

	public List<ResReportVo> getResProjReportList() {
		return resProjReportList;
	}

	public void setResProjReportList(List<ResReportVo> resProjReportList) {
		this.resProjReportList = resProjReportList;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}
	
	public String getStrJson() {
		return strJson;
	}

	public void setStrJson(String strJson) {
		this.strJson = strJson;
	}

	public IPVo getIps() {
		return ips;
	}

	public void setIps(IPVo ips) {
		this.ips = ips;
	}

	public String getIp1() {
		return ip1;
	}

	public void setIp1(String ip1) {
		this.ip1 = ip1;
	}

	public String getIp2() {
		return ip2;
	}

	public void setIp2(String ip2) {
		this.ip2 = ip2;
	}

	public String getIp3() {
		return ip3;
	}

	public void setIp3(String ip3) {
		this.ip3 = ip3;
	}

	public String getIp4() {
		return ip4;
	}

	public void setIp4(String ip4) {
		this.ip4 = ip4;
	}
}

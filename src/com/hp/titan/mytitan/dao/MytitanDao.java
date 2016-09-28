package com.hp.titan.mytitan.dao;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.User;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.Consumption;
import com.hp.titan.mytitan.model.ExpAccount;
import com.hp.titan.mytitan.model.ExpLog;
import com.hp.titan.mytitan.model.Goods;
import com.hp.titan.mytitan.model.InBox;
import com.hp.titan.mytitan.model.Point;
import com.hp.titan.mytitan.model.PointHist;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.model.WeeklyReport;
import com.hp.titan.mytitan.model.WeeklyReportTask;
import com.hp.titan.mytitan.model.Winner;
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.server.model.Server;


public class MytitanDao extends DefaultBaseDao<Server, String> {

	
	
	public void saveReport(WeeklyReport weeklyReport)throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(weeklyReport);
	}
	
	public void saveReportTask(WeeklyReportTask wrt)throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(wrt);
	}
	public void saveOrUpdate(PointHist ph) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(ph);
	}
	
	public void saveOrUpdate (ExpLog el) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(el);
	}

	public List<WeeklyReport> getWeeklyReportByUserId(Integer userId)throws BaseDaoException{
		List<WeeklyReport> wrList = new ArrayList<WeeklyReport>();
		String hql = "select wr.type, wr.sendTo, wr.title, wr.createDate, wr.reportId from com.hp.titan.mytitan.model.WeeklyReport as wr where wr.userId = ? ";
		List<Object[]> objList = this.getHibernateTemplate().find(hql, userId);
		Iterator<Object[]> iter = objList.iterator();
		while(iter.hasNext()){
			WeeklyReport wr = new WeeklyReport();
			Object obj[] = iter.next();
			wr.setType(String.valueOf(obj[0]));
			wr.setSendTo(String.valueOf(obj[1]));
			wr.setTitle(String.valueOf(obj[2]));
			wr.setStrDate(String.valueOf(obj[3]));
			wr.setReportId(String.valueOf(obj[4]));
			wrList.add(wr);
		}
		return wrList;
	}
	
	public List<Object[]> getStrongFieldReportByUserMail(String userMail,String projectId, String startDate, String endDate) throws BaseDaoException{
		String sql = "SELECT cr.COMMITTEDBY AS strongfield," +
				"sum(case WHEN ccp.path LIKE '%.java' THEN ccp.CODE_CHANGED ELSE 0 END) AS javasum," +
				"sum(case WHEN ccp.path LIKE '%.xml' THEN ccp.CODE_CHANGED ELSE 0 END) AS  xmlsum," +
				"sum(case WHEN ccp.path LIKE '%.ftl' THEN ccp.CODE_CHANGED ELSE 0 END) AS ftlsum," +
				"sum(case WHEN ccp.path LIKE '%.sql' THEN ccp.CODE_CHANGED ELSE 0 END) AS sqlsum," +
				"sum(case WHEN ccp.path LIKE '%.js' THEN ccp.CODE_CHANGED ELSE 0 END) AS jssum," +
				"sum(case WHEN ccp.path LIKE '%.png' THEN ccp.CODE_CHANGED ELSE 0 END) AS pngsum," +
				"sum(case WHEN ccp.path LIKE '%.jsp' THEN ccp.CODE_CHANGED ELSE 0 END) AS jspsum," +
				"sum(case WHEN ccp.path LIKE '%.css' THEN ccp.CODE_CHANGED ELSE 0 END) AS casssum " +
				"FROM commit_report AS cr, reportary as r, commit_changed_path AS ccp " +
				"WHERE cr.REPORTARY_ID = r.REPORTARY_ID AND r.PROJECT_ID = '8ae584a8480db70701480e73a34202a3' and " +
				"ccp.COMMIT_REPORT_ID=cr.COMMIT_REPORT_ID and cr.COMMITTEDBY='"+userMail+"' and cr.COMMIT_TIME between '2015-02-02' and '2015-02-17'"; 
		List<Object[]> objects  = this.getSession().createSQLQuery(sql).list();
		
		return objects;
	}
	
	
	public WeeklyReport getReportById(String reportId) throws BaseDaoException{
		return (WeeklyReport) getHibernateTemplate().get(WeeklyReport.class, reportId);
	}
	
	public List<WeeklyReportTask> getReportTask(String reportId, String type) throws BaseDaoException{
		String hql = "from WeeklyReportTask wrt where wrt.reportId = ? and wrt.taskType=?";
		return this.getHibernateTemplate().find(hql,reportId, type);
	}
	
	public List<UserstoryInfo> getUserStoriesFromDB(String email)throws BaseDaoException{
		String hql = "from UserstoryInfo us where us.ownerEmail = ?";
		return this.getHibernateTemplate().find(hql,email);
	}
	public List<PointHist> getPointHistByUser(int userId)throws BaseDaoException, ParseException{
		String sql = "SELECT ph.CREATE_DATE, ph.POINT_FARE, ph.POINT_TOTAL, ph.TYPE, di.DEFECT_NUM FROM point_hist AS ph, defect_info AS di WHERE ph.OBJECT_ID = di.OBJECT_ID  AND ph.USER_ID =" + String.valueOf(userId);
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects1 = query.list();
		
		sql = "SELECT ph.CREATE_DATE, ph.POINT_FARE, ph.POINT_TOTAL, ph.TYPE, ui.US_NUM FROM point_hist AS ph,  user_story_info AS ui WHERE  ph.OBJECT_ID = ui.OBJECT_ID AND ph.USER_ID =" + String.valueOf(userId);
		query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects2 = query.list();
		
		sql = "SELECT ph.CREATE_DATE, ph.POINT_FARE, ph.POINT_TOTAL, ph.TYPE, u.USER_CODE FROM point_hist as ph, titan_user as u where ph.OBJECT_ID = u.USER_ID and ph.TYPE = 'award' and ph.USER_ID = " + userId;
		query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects3 = query.list();
		
		sql = "SELECT ph.CREATE_DATE, ph.POINT_FARE, ph.POINT_TOTAL, ph.TYPE, c.goods_name FROM point_hist AS ph,  consumption AS c WHERE  ph.OBJECT_ID = c.CONSUME_ID AND ph.USER_ID =" + String.valueOf(userId);
		query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects4 = query.list();
		objects1.addAll(objects2);
		objects1.addAll(objects3);
		objects1.addAll(objects4);
		List<PointHist> voList = new ArrayList<PointHist>();
		if(null != objects1 && objects1.size() != 0){
			for (Object[] object : objects1){
				PointHist ph = new PointHist();
				String cdate = String.valueOf(object[0]);
				if(null != cdate && !"".equals(cdate)){
					cdate = cdate.substring(0, cdate.length()-2);
				}
				ph.setStrcreateDate(cdate);
				ph.setPointFare(Integer.valueOf(object[1].toString()));
				ph.setPointTotal(Integer.valueOf(object[2].toString()));
				ph.setType(String.valueOf(object[3]));
				ph.setObjectId(String.valueOf(object[4]));
				voList.add(ph);			
			}
		}
		return voList;
	}
	
	public Point getPointByUserId(int userId) throws BaseDaoException{
		String hql = "from Point pt where pt.userId = ?";
		List<Point> pointList = this.getHibernateTemplate().find(hql, userId);
		return pointList.size()>0 ? pointList.get(0) : null;
	}
	
	public List<PointHist> getAwardPointHistByUser(String userId)throws BaseDaoException, ParseException{
		String sql = "SELECT ph.CREATE_DATE, ph.POINT_FARE, u.USER_CODE FROM point_hist as ph, titan_user as u where ph.USER_ID = u.USER_ID and ph.OBJECT_ID = " + userId;
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<PointHist> voList = new ArrayList<PointHist>();
		if(null != objects && objects.size() != 0){
			for (Object[] object : objects){
				PointHist ph = new PointHist();
				String cdate = String.valueOf(object[0]);
				if(null != cdate && !"".equals(cdate)){
					cdate = cdate.substring(0, cdate.length()-2);
				}
				ph.setStrcreateDate(cdate);
				ph.setPointFare(Integer.valueOf(object[1].toString()));
				ph.setObjectId((String.valueOf(object[2])));
				voList.add(ph);			
			}
		}
		return voList;
	}
	
	public ExpAccount getExpByUserId(int userId) throws BaseDaoException{
		String hql = "from ExpAccount ea where ea.userId = ?";
		List<ExpAccount> exptList = this.getHibernateTemplate().find(hql, userId);
		return exptList.size()>0 ? exptList.get(0) : null;
	}
	
	public List<PointHist> getPointHistByBean(PointHist ph) throws BaseException{
		String hql = "from PointHist ph where 1 = 1";
		if(ph.getObjectId() != null && !"".equals(ph.getObjectId())){
			hql += " and ph.objectId =:objId";
		}
		if(ph.getType() != null && !"".equals(ph.getType())){
			hql += " and ph.type =:type";
		}
		Query query = this.getSession().createQuery(hql);
		if(ph.getObjectId() != null && !"".equals(ph.getObjectId())){
			query.setString("objId", ph.getObjectId());
		}
		if(ph.getType() != null && !"".equals(ph.getType())){
			query.setString("type", ph.getType());
		}
		return query.list();
	}

	public List<UserstoryVo> getUserStoriesList(String state, Calendar calendar, String email)throws BaseDaoException{
		Calendar calendar1 = Calendar.getInstance();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date =df.format(calendar.getTime());
		String date2 = df.format(calendar1.getTime());
		String sql = "SELECT us.OBJECT_ID, us.US_NUM, us.US_NAME, us.PLAN_ESTIMATE, us.ACCEPTED_DATE, us.OWNER_EMAIL from user_story_info as us "
			   + "where us.STATE = '" + state + "'" + " and us.ACCEPTED_DATE BETWEEN '" + date + "' and '" + date2 + "' and OWNER_EMAIL = '" + email + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<UserstoryVo> voList = new ArrayList<UserstoryVo>();
		for (Object[] object : objects){
			UserstoryVo vo = new UserstoryVo();
			vo.setObjectId(object[0].toString());
			vo.setOwnerEmail(object[5].toString());
			vo.setUsName(object[2].toString());
			vo.setAcceptedDate(object[4].toString());
			voList.add(vo);			
		}
		return voList;

	}
	
	public void saveMyUserStory(UserstoryInfo usInfo)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(usInfo);
	}
	
	public List<WeeklyReportTask> getUsTasks(String usNum)throws BaseDaoException{
		String hql = "from WeeklyReportTask wrt where wrt.parent = ?";
		return this.getHibernateTemplate().find(hql, usNum);
	}
	
	public List<MyCommitVo> getCommitList(String email, String reportaryId)throws BaseDaoException{
		String sql = "SELECT cr.CODE_CHANGED, cr.`COMMENT`, cr.COMMITTEDBY, cr.COMMIT_REPORT_ID, cr.COMMIT_TIME, cr.REVISION, p.PROJECT_NAME, cr.COMMIT_REPORT_ID "
		       + "from commit_report as cr, reportary as r, project as p "
		       + "where r.PROJECT_ID = p.PROJECT_ID and cr.REPORTARY_ID = r.REPORTARY_ID";
		if(null != email && !"".equals(email)){
			sql +=" and cr.COMMITTEDBY = '" + email + "'";
		}
		if(null != reportaryId && !"".equals(reportaryId)){
			sql +=" and r.REPORTARY_ID = '" + reportaryId + "'";
		}
		
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<MyCommitVo> voList = new ArrayList<MyCommitVo>();
		for (Object[] object : objects){
			MyCommitVo vo = new MyCommitVo();
			vo.setCodeChange(object[0].toString());
			vo.setComment(object[1].toString());
			vo.setCommittedBy(object[2].toString());
			vo.setCommitreportId(object[3].toString());
			vo.setCommitTime(object[4].toString());
			vo.setRevision(object[5].toString());
			vo.setProjectName(object[6].toString());
			vo.setCommitreportId(object[7].toString());
			voList.add(vo);			
		}
		return voList;
	}
	public void savePointHist(PointHist ph)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(ph);
	}
	public void savePoint(Point pt)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(pt);
	}
	public void saveExp(ExpAccount ea)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(ea);
	}
	
//	public void saveCommit(CommitReport cr)throws BaseDaoException{
//		this.getHibernateTemplate().saveOrUpdate(cr);
//	}
	
//	public void saveCommitPath(CommitPath cp)throws BaseDaoException{
//		this.getHibernateTemplate().saveOrUpdate(cp);
//	}
	
//	public String getMaxSvnVersion(String reportaryId)throws BaseDaoException{
//		String sql = "SELECT MAX(cr.REVISION) FROM commit_report cr WHERE cr.REPORTARY_ID ='" + reportaryId + "'";
//		Query query = this.getSession().createSQLQuery(sql);
//		List<Object[]> objects = query.list();
//		if(null != objects && objects.size() != 0){
//			if(null != objects.get(0)){
//				return String.valueOf(objects.get(0));
//			}
//		}
//		return "0";
//	}
	
	public List<CommitPath> getCommitChangeList(String commitId)throws BaseDaoException{
		String hql = "from CommitPath cp where cp.commitreportId = ?";
		return this.getHibernateTemplate().find(hql,commitId);
	}
	
//	public void deleteChangePath(String commitReportId)throws BaseDaoException{
//		String hql = "delete from CommitPath cp where cp.commitreportId = ?";
//		Query query = this.getSession().createQuery(hql.toString());
//		query.setString(0, commitReportId) ;
//		query.executeUpdate() ;
//	}
	
//	public void deleteCommitReport(String reportaryId)throws BaseDaoException{
//		String hql = "delete from CommitReport cr where cr.reportaryId = ?";
//		Query query = this.getSession().createQuery(hql.toString());
//		query.setString(0, reportaryId) ;
//		query.executeUpdate() ;
//	}
	
//	public void doProjectAddRally(ProjectRallyQuix projectRallyQuix)throws BaseDaoException{
//		this.getHibernateTemplate().saveOrUpdate(projectRallyQuix);
//	}
	
//	public ProjectRallyQuix getProjectRallyQuixById(String rallyQuixId) throws BaseDaoException{
//		String hql = "from ProjectRallyQuix prq where prq.rallyquixId = ?";
//		List<ProjectRallyQuix> projectRallyQuixList = this.getHibernateTemplate().find(hql, rallyQuixId);
//		return projectRallyQuixList.size()>0?projectRallyQuixList.get(0):null;
//	}
	
	public void doRallyDeleteById(String rallyQuixId) throws BaseDaoException{
		String hql = "delete from ProjectRallyQuix prq where prq.rallyquixId = ?";
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, rallyQuixId) ;
		query.executeUpdate() ;
	}
	
	public Boolean checkDupReportByDate(Integer userId) throws BaseDaoException{
		Calendar calendar1 = Calendar.getInstance();  
		calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date =df.format(calendar1.getTime());
		String date2 = df.format(Calendar.getInstance().getTime());
		String sql = "SELECT * from exp_log as el "
			   + "where el.USER_ID = '" + userId + "'" + " and el.CREATE_DATE BETWEEN '" + date + "' AND '" + date2 +"'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		return objects.size()>0 ? true : false;
	}
	
	public List<Goods> getAllGoods() throws BaseDaoException{
		String hql = "from Goods g where g.isValid = 0 or g.isValid = 2 order by sort";
		List<Goods> goodList = this.getHibernateTemplate().find(hql);
		return goodList;
	}
	
	public List<Point> getAllPoint() throws BaseDaoException{
		String hql = "from Point p where p.isValid = 0";
		List<Point> pointList = this.getHibernateTemplate().find(hql);
		return pointList;
	}
	
	public void saveConsume(Consumption c) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(c);
	}
	
	public Goods getGoodsByName(String goodName) throws BaseDaoException{
		String hql = "from Goods g where g.goodsName = ?";
		List<Goods> goodsList = this.getHibernateTemplate().find(hql, goodName);
		return goodsList.size()>0 ? goodsList.get(0) : null;
	}
	
	public List<Consumption> getConsumptionByBean(Consumption c) throws BaseDaoException{
		String hql = "from Consumption c where c.isValid = 0";
		if(c.getUserId() != null && !"".equals(c.getUserId())){
			hql += " and c.userId =:userId";
		}
		if(c.getConsumeId() != null && !"".equals(c.getConsumeId())){
			hql += " and c.consumeId =:consumeId";
		}
		if(c.getGoodsName() != null && !"".equals(c.getGoodsName())){
			hql += " and c.goodsName =:goodsName";
		}
		if(c.getStatus() != null && !"".equals(c.getStatus())){
			hql += " and c.status =:status";
		}
		if(c.getType() != null && !"".equals(c.getType())){
			hql += " and c.type =:type";
		}
		Query query = this.getSession().createQuery(hql);
		if(c.getUserId() != null && !"".equals(c.getUserId())){
			query.setInteger("userId", c.getUserId());
		}
		if(c.getConsumeId() != null && !"".equals(c.getConsumeId())){
			query.setString("consumeId", c.getConsumeId());
		}
		if(c.getGoodsName() != null && !"".equals(c.getGoodsName())){
			query.setString("goodsName", c.getGoodsName());
		}
		if(c.getStatus() != null && !"".equals(c.getStatus())){
			query.setString("status", c.getStatus());
		}
		if(c.getType() != null && !"".equals(c.getType())){
			query.setInteger("type", c.getType());
		}
		return query.list();
	}
	
	public PointHist getPointHistByObjectId(String objectId) throws BaseDaoException{
		String hql = "from PointHist ph where ph.objectId = ? and ph.isValid = 0";
		List<PointHist> pointHistList = this.getHibernateTemplate().find(hql, objectId);
		return pointHistList.size()>0 ? pointHistList.get(0) : null;
	}
    
    public void chargeExpint(Integer exPoint) throws BaseDaoException{
         String hql = "update Point pt set pt.exPoint = ? where pt.isValid = 0";
         Query query = this .getSession().createQuery(hql.toString());
         query.setInteger(0, exPoint);
         query.executeUpdate();
    }
    
    public List<Consumption> comsumptionCount(int groupId) throws BaseDaoException {
    	String sql = "SELECT u.USER_CODE, SUM(c.PRICE) FROM consumption AS c, titan_user AS u, titan_user_group AS ug, titan_group g WHERE c.GOODS_NAME = 'Recognition Points' AND c.USER_ID = u.USER_ID AND c.IS_VALID = 0 AND c.`STATUS` = 'submitted' AND u.USER_ID = ug.USER_ID AND ug.GROUP_ID = g.GROUP_ID AND ug.GROUP_ID = " + groupId + " GROUP BY c.USER_ID";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Consumption> cc = new ArrayList<Consumption>();
		for (Object[] object : objects){
			Consumption c = new Consumption();
			c.setGoodsName((object[0].toString()));
			c.setPrice(Integer.valueOf(object[1].toString()));
			cc.add(c);			
		}
		return cc;
    }
    
    
    public List<Consumption> getAllConsumptionByGroup(int groupId) throws BaseDaoException, ParseException {
    	String sql = "SELECT c.CONSUME_DATE, c.GOODS_NAME, c.PRICE, c.`STATUS`, u.USER_CODE, c.CONSUME_ID FROM consumption AS c, titan_user AS u, titan_group AS g, titan_user_group AS ug WHERE c.USER_ID = u.USER_ID AND u.USER_ID = ug.USER_ID AND c.IS_VALID = 0 AND ug.GROUP_ID = g.GROUP_ID AND g.GROUP_ID = " + groupId ;
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Consumption> cc = new ArrayList<Consumption>();
		for (Object[] object : objects){
			Consumption c = new Consumption();
			c.setStrConsumeDate(String.valueOf(object[0]));
			c.setGoodsName((object[1].toString()));
			c.setPrice(Integer.valueOf(object[2].toString()));
			c.setStatus(object[3].toString());
			c.setUserCode(object[4].toString());
			c.setConsumeId(object[5].toString());
			cc.add(c);			
		}
		return cc;
    }
    
    public List<Consumption> getGambleConsumption(int userId ) throws BaseDaoException {
    	String sql = "SELECT c.GOODS_NAME, c.CONSUME_DATE, c.`STATUS`, c.CONSUME_ID, c.SELECTED_NUMBER FROM goods as g, consumption as c WHERE g.GOODS_NAME = c.GOODS_NAME  AND g.TYPE = 3 AND c.USER_ID = " + userId + " AND c.IS_VALID = 0";
    	Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Consumption> cc = new ArrayList<Consumption>();
		for (Object object[] : objects){
			Consumption c = new Consumption();
			c.setGoodsName(String.valueOf(object[0]));
			c.setStrConsumeDate(String.valueOf(object[1]));
			c.setStatus(String.valueOf(object[2]));
			c.setConsumeId(String.valueOf(object[3]));
			if(null != object[4]){
				c.setSelectedNum(Integer.valueOf(String.valueOf(object[4])));
			}
			cc.add(c);			
		}
		return cc;
    }
    
    public Consumption getConsumptionById (String consumeId) throws BaseDaoException {
    	String hql = "from Consumption c where c.consumeId = ? ";
		List<Consumption> cList = this.getHibernateTemplate().find(hql,consumeId);
		return cList.get(0);
    }
    
    public List<Integer> getSelectNumberByGroup(int groupId) throws BaseDaoException{
    	String sql = "SELECT c.SELECTED_NUMBER FROM consumption AS c, titan_user AS u, titan_group AS g, titan_user_group AS ug WHERE c.USER_ID = u.USER_ID AND u.USER_ID = ug.USER_ID AND ug.GROUP_ID = g.GROUP_ID AND c.TYPE = 3 AND c.SELECTED_NUMBER IS NOT NULL AND c.`STATUS` = 'submitted' AND g.GROUP_ID = " + groupId;
    	Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Integer> res = new ArrayList<Integer>();
		for (Object object : objects){
			res.add(Integer.valueOf(String.valueOf(object)));
		}
		return res;
    }
    
    public User getUserByNumber(int selectNum) throws BaseDaoException{
    	String sql = "SELECT u.USER_ID, u.USER_CODE FROM titan_user AS u, consumption AS c WHERE u.USER_ID = c.USER_ID AND c.STATUS = 'submitted' AND c.SELECTED_NUMBER = " + selectNum;
    	Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		User u = new User();
		u.setUserId(Integer.valueOf(String.valueOf(objects.get(0)[0])));
		u.setUserCode(String.valueOf(objects.get(0)[1]));
		return u;
    }
    
    public void saveWinner(Winner winner) throws BaseDaoException{
    	this.getHibernateTemplate().saveOrUpdate(winner);
    }
    
    public List<Winner> getWinnersByBeam(Winner winner) throws BaseDaoException{
    	String sql = "SELECT w.GOODS_NAME, u.USER_CODE, w.CREATE_DATE, w.`STATUS`, w.WINNER_ID, w.USER_ID FROM winner w, titan_user u WHERE w.USER_ID = u.USER_ID ";
    	if(winner.getWinnerId() != null && !"".equals(winner.getWinnerId())){
    		sql += " and w.WINNER_ID = '" + winner.getWinnerId() + "'";
    	}
    	Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Winner> wList = new ArrayList<Winner>();
		for (Object object[] : objects){
			Winner w = new Winner();
			w.setGoodsName(String.valueOf(object[0]));
			w.setUserCode(String.valueOf(object[1]));
			w.setStrcreateDate(String.valueOf(object[2]));
			w.setStatus(String.valueOf(object[3]));
			w.setWinnerId(String.valueOf(object[4]));
			w.setUserId(Integer.valueOf(String.valueOf(object[5])));
			wList.add(w);
		}
		return wList;
    }
    
    public List<InBox> getInBoxByBean(InBox inBox) throws BaseDaoException{
    	String hql = "from InBox ib where isValid = 0  ";
    	if(null != inBox.getUserId() && !"".equals(inBox.getUserId())){
    		hql += " and ib.userId =:userId";
    	}
    	if(null != inBox.getIsReaded() && !"".equals(inBox.getIsReaded())){
    		hql += " and ib.isReaded =:isReaded";
    	}
    	if(null != inBox.getMsgId() && !"".equals(inBox.getMsgId())){
    		hql += " and ib.msgId =:msgId";
    	}
    	hql += " order by createTime asc";
    	Query query = this.getSession().createQuery(hql);
    	if(null != inBox.getUserId() && !"".equals(inBox.getUserId())){
			query.setInteger("userId", inBox.getUserId());
		}
    	if(null != inBox.getIsReaded() && !"".equals(inBox.getIsReaded())){
    		query.setInteger("isReaded", inBox.getIsReaded());
    	}
    	if(null != inBox.getMsgId() && !"".equals(inBox.getMsgId())){
    		query.setString("msgId", inBox.getMsgId());
    	}
		return query.list();
    }
    
    public void saveInbox(InBox inBox)  throws BaseDaoException{
    	this.getHibernateTemplate().saveOrUpdate(inBox);
    }
}

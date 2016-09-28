package com.hp.titan.report.dao;

import java.util.List;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.titan.report.vo.ContributionReportVo;

public class ReportDao extends DefaultBaseDao<ContributionReportVo, String> {
	
	public List<Object[]> getContributionReportInfo(String projectId,String startDate,String endDate){
		String sql = "SELECT cr.COMMITTEDBY,SUM(DISTINCT(cr.CODE_CHANGED)) AS total," +
				"SUM(CASE WHEN cr.COMMIT_REPORT_ID = cud.COMMIT_REPORT_ID  THEN cr.CODE_CHANGED ELSE 0 END) AS subtotal," +
				"SUM(CASE WHEN cr.COMMIT_REPORT_ID = cud.COMMIT_REPORT_ID AND cud.US_DEFECT_NUM LIKE 'DE%' THEN cr.CODE_CHANGED ELSE 0 END) AS defectCount," +
				"SUM(CASE WHEN cr.COMMIT_REPORT_ID = cud.COMMIT_REPORT_ID AND cud.US_DEFECT_NUM LIKE 'US%' THEN cr.CODE_CHANGED ELSE 0 END) As USCount," +
				"SUM(CASE WHEN cr.COMMIT_REPORT_ID = cud.COMMIT_REPORT_ID AND cud.US_DEFECT_NUM LIKE 'QXCR%' THEN cr.CODE_CHANGED ELSE 0 END) AS QxcrCount " +
				"FROM commit_report AS cr, reportary as r, commit_us_defect AS cud " +
				"WHERE cr.REPORTARY_ID = r.REPORTARY_ID AND r.PROJECT_ID = '"+projectId+"' and cr.COMMIT_TIME between '"+startDate+"' and '"+endDate+"' " +
//				"WHERE cr.REPORTARY_ID = r.REPORTARY_ID AND r.PROJECT_ID = '8ae584a8480db70701480e73a34202a3' and cr.COMMIT_TIME between '"+startDate+"' and '"+endDate+"' " +
				"GROUP BY cr.COMMITTEDBY";
		 
		List<Object[]> contributionReportInfo = this.getSession().createSQLQuery(sql).list();
		return contributionReportInfo;
	}
	
	public List<Object[]> getStrongFieldReportInfo(String projectId,String startDate,String endDate){
		String sql = "SELECT cr.COMMITTEDBY AS name," +
				"sum(case WHEN ccp.path LIKE '%.java' THEN ccp.CODE_CHANGED ELSE 0 END) AS javasum," +
				"sum(case WHEN ccp.path LIKE '%.xml' THEN ccp.CODE_CHANGED ELSE 0 END) AS  xmlsum," +
				"sum(case WHEN ccp.path LIKE '%.ftl' THEN ccp.CODE_CHANGED ELSE 0 END) AS ftlsum," +
				"sum(case WHEN ccp.path LIKE '%.sql' THEN ccp.CODE_CHANGED ELSE 0 END) AS sqlsum," +
				"sum(case WHEN ccp.path LIKE '%.js' THEN ccp.CODE_CHANGED ELSE 0 END) AS jssum," +
				"sum(case WHEN ccp.path LIKE '%.png' THEN ccp.CODE_CHANGED ELSE 0 END) AS pngsum," +
				"sum(case WHEN ccp.path LIKE '%.jsp' THEN ccp.CODE_CHANGED ELSE 0 END) AS jspsum," +
				"sum(case WHEN ccp.path LIKE '%.css' THEN ccp.CODE_CHANGED ELSE 0 END) AS csssum " +
				"FROM commit_report AS cr, reportary as r, commit_changed_path AS ccp " +
				"WHERE cr.REPORTARY_ID = r.REPORTARY_ID AND r.PROJECT_ID = '8ae584a8480db70701480e73a34202a3' and ccp.COMMIT_REPORT_ID=cr.COMMIT_REPORT_ID and cr.COMMIT_TIME between '2015-02-03' and '2015-02-17' " +
				"GROUP BY cr.COMMITTEDBY";
		List<Object[]> strongFieldReportInfo = this.getSession().createSQLQuery(sql).list();
		return strongFieldReportInfo;
	}
	
}

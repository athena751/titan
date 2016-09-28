package com.hp.titan.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.report.dao.ReportDao;
import com.hp.titan.report.service.ReportService;
import com.hp.titan.report.vo.ContributionReportVo;
import com.hp.titan.report.vo.StrongFieldReportVo;

public class ReportServiceImpl implements ReportService{
	private ReportDao reportDao;
	@Override
	public List<ContributionReportVo> getContributionReportVoAll(String projectId,String startDate,String endDate)
			throws BaseDaoException {
		
		List<ContributionReportVo> contributionReportVoList = new ArrayList<ContributionReportVo>();
		List<Object[]> contributionReportList = reportDao.getContributionReportInfo(projectId,startDate, endDate);
		Object[] commitReport;
		for(int i=0;i<contributionReportList.size();i++){
		    commitReport = contributionReportList.get(i);
			String commitName = String.valueOf(commitReport[0]);
			ContributionReportVo contributionReportVo = new ContributionReportVo();
			contributionReportVo.setDevName(commitName.substring(0,commitName.length()-7));
			contributionReportVo.setTotalCommit(Integer.valueOf(commitReport[1].toString().replace(".0", "")));
			contributionReportVo.setUnknowcodeCount(Integer.valueOf(commitReport[1].toString().replace(".0", "")) - Integer.valueOf(commitReport[2].toString().replace(".0", "")));
			contributionReportVo.setDefectcodeCount(Integer.valueOf(commitReport[3].toString().replace(".0", "")));
			contributionReportVo.setUscodeCount(Integer.valueOf(commitReport[4].toString().replace(".0", "")));
			contributionReportVo.setQuixcodeCount(Integer.valueOf(commitReport[5].toString().replace(".0", "")));
			contributionReportVoList.add(contributionReportVo);
		}
		return contributionReportVoList;
	}
	
	@Override
	public List<StrongFieldReportVo> getStrongFieldReportVoAll(String projectId,String startDate,String endDate)
	throws BaseDaoException {
		
		List<StrongFieldReportVo> strongFieldReportVoList = new ArrayList<StrongFieldReportVo>();
		List<Object[]> strongFieldReportList = reportDao.getStrongFieldReportInfo(projectId,startDate, endDate);
		Object[] strongFieldReport;
		for(int i=0;i<strongFieldReportList.size();i++){
			strongFieldReport = strongFieldReportList.get(i);
			StrongFieldReportVo strongFieldReportVo = new StrongFieldReportVo();
			int java = Integer.valueOf(strongFieldReport[1].toString().replace(".0", ""));
			int xml = Integer.valueOf(strongFieldReport[2].toString().replace(".0", ""));
			int ftl = Integer.valueOf(strongFieldReport[3].toString().replace(".0", ""));
			int sql = Integer.valueOf(strongFieldReport[4].toString().replace(".0", ""));
			int js = Integer.valueOf(strongFieldReport[5].toString().replace(".0", ""));
			int png = Integer.valueOf(strongFieldReport[6].toString().replace(".0", ""));
			int jsp = Integer.valueOf(strongFieldReport[7].toString().replace(".0", ""));
			int css = Integer.valueOf(strongFieldReport[8].toString().replace(".0", ""));
			
			int[] count = {java,xml,ftl,sql,js,png,jsp,css};
			int max = java;
			for (int k=1;k<count.length;k++) {
				if(count[k]>max){
					max = count[k];
				}
			}
			StringBuffer sb = new StringBuffer();
			if(java==max){
				sb.append("java");
			}
			if(xml==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("xml");
			}
			if(sql==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("sql");
			}
			if(ftl==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("ftl");
			}
			if(css==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("css");
			}
			if(jsp==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("jsp");
			}
			if(png==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("png");
			}
			if(js==max){
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("js");
			}
			strongFieldReportVo.setStrongFieldName(sb.toString());
			strongFieldReportVo.setName(String.valueOf(strongFieldReport[0]));
			strongFieldReportVo.setJavaCommitLineCount(java);
			strongFieldReportVo.setXmlCommitLineCount(xml);
			strongFieldReportVo.setFtlCommitLineCount(ftl);
			strongFieldReportVo.setSqlCommitLineCount(sql);
			strongFieldReportVo.setJsCommitLineCount(js);
			strongFieldReportVo.setPngCommitLineCount(png);
			strongFieldReportVo.setJspCommitLineCount(jsp);
			strongFieldReportVo.setCssCommitLineCount(css);
			strongFieldReportVoList.add(strongFieldReportVo);
		}
		return strongFieldReportVoList;
	}
	public ReportDao getReportDao() {
		return reportDao;
	}
	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	
	
}

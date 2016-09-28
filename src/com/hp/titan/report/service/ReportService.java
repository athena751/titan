package com.hp.titan.report.service;

import java.util.List;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.report.vo.ContributionReportVo;
import com.hp.titan.report.vo.StrongFieldReportVo;

public interface ReportService {

	public List<ContributionReportVo> getContributionReportVoAll(String projectId,String startDate,String endDate) throws BaseDaoException;
	
	public List<StrongFieldReportVo> getStrongFieldReportVoAll(String projectId,String startDate,String endDate) throws BaseDaoException;

}

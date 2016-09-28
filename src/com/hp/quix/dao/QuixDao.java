package com.hp.quix.dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import com.hp.quix.client.ErrorHandler;
import com.hp.quix.client.PropertiesUtility;
import com.hp.quix.client.QuixServiceClient;
import com.hp.quix.client.parser.ServiceMessage;
import com.hp.titan.common.vo.DefectVo;

/**
 * Class passes an SQL statement to the QuixSQLService which queries
 * the BO database and returns results to STDOUT.
 */
public class QuixDao {

	protected PropertiesUtility prop = null;
	protected static String classname = "com.hp.quix.client.QuixDao";
	protected ErrorHandler eh = null;
	
	public QuixDao(){
	}
	
	/**
	 * Constructs a QuixSQL object using the configured PropertiesUtility.
	 *
	 */
	public QuixDao(PropertiesUtility pu, String querySql){
		
		pu.setProperty("server.name", "quix-pct.houston.hpe.com");
//		pu.setProperty("keystore.file", "/opt/titan/keystore/client.jks");
//		pu.setProperty("keystore.file", "C:/DEV-TITAN/tf-titan/tf-titan/titan/keystore/client.jks" );
		pu.setProperty("keystore.file", "C:/DiscD/MyWork/workspace/titanProject/titan/keystore/client.jks");
		pu.setProperty("keystore.passwd", "X5s&.C;&pX");
		pu.setProperty("key.username", "21762355");
		pu.setProperty("key.userpasswd", "Y5z-aKRcBQ");
		pu.setProperty("org.apache.ws.security.crypto.provider", "com.hp.quix.client.QuixMerlin");
		pu.setProperty("sql.statement", querySql);
		pu.setProperty("sql.header", "yes");
		this.prop = pu;
	
		//
		// Validate common properties
		//
		if (this.prop.validateDefaultProperties() != 0) {
			printUsage();
//			System.exit(1);
		}
	}
	
	/**
	 * Method calls the QuixSQLService which processes the SQL statement and
	 * prints the results to STDOUT.
	 * 
	 * <pre>
	 *  
	 *   Usage: Calling QuixSQL directly.
	 *   
	 *          -Dserver.name= -Dkeystore.file= -Dkeystore.passwd= 
	 *          -Dkey.username= -Dkey.userpasswd= 
	 *          -Dsql.statement= 
	 *          -D[sql.delimiter=]
	 *          -D[sql.header=yes|no]         
	 *          com.hp.quix.client.QuixSQL
	 *   
	 *   Example:
	 *          -Dsql.statement=&quot;SELECT STATE as FROM QUIX.BO_QX_CR where ISSUEID='QXCR1000051504'&quot; \
	 *          com.hp.quix.client.QuixSQL 
	 *    
	 *   Usage: Calling the driver class QuixClient.
	 *    
	 *          -Dserver.name= -Dkeystore.file= -Dkeystore.passwd= 
	 *          -Dkey.username= -Dkey.userpasswd= 
	 *          -Doperation=getVersion 
	 *          -Dsql.statement=
	 *          -D[sql.delimiter=]
	 *          -D[sql.header=yes|no]   
	 *          com.hp.quix.client.QuixClient
	 *   
	 *   Example:
	 *          -Doperation=getVersion \
	 *          -Dsql.statement=&quot;SELECT STATE as FROM QUIX.BO_QX_CR where ISSUEID='QXCR1000051504'&quot; \
	 *          com.hp.quix.client.QuixClient 
	 *  
	 * </pre>
	 * 
	 * @param serviceoperation
	 *            String array consisting of the service name "QuixSQLService"
	 *            and operation "getSQLResults".
	 * @throws Exception
	 */
	public Object[] processServiceOperation(String[] serviceoperation) throws Exception {

		OMElement ret = null;
		
		String service = serviceoperation[0];
		String method = serviceoperation[1];
		String servername = this.prop.getProperty("server.name");
		String keystorefile = this.prop.getProperty("keystore.file");
		String keystorepasswd = this.prop.getProperty("keystore.passwd");
		String keyusername = this.prop.getProperty("key.username");
		String keyuserpasswd = this.prop.getProperty("key.userpasswd");
		String sqlstatement = this.prop.getProperty("sql.statement");
		
		
		//
		// Validate and normalize properties
		//
		if (validateInputParams(sqlstatement,this.prop.getProperty("sql.delimiter"),this.prop.getProperty("sql.header")) == false) {
			printUsage();
		}
		
		// Must be listed after validation because we normalize in validateInputParams as needed.
//		String sqlhead = this.prop.getProperty("sql.header");
		String sqldelim = this.prop.getProperty("sql.delimiter");
		

		QuixServiceClient qsc = new QuixServiceClient(servername,service,method);
		qsc.setQuIXSecurity(keystorefile, keystorepasswd, keyusername, keyuserpasswd);			

		ArrayList<String> parms = new ArrayList<String>();
		parms.add(sqlstatement+"");	

		ret = qsc.send(parms);

		ServiceMessage sqlrset = new ServiceMessage(ret);

		//
		// Check the return code from sevice
		//
		if (sqlrset.getServiceReturnCode() != 0) {
			System.err.println(sqlrset.getServiceReturnMsg());
		}
		
//		// Write content as utf-8 since content in Oracle is encoded as utf-8
//		OutputStreamWriter osw = new OutputStreamWriter(System.out, "UTF-8");

		//
		// Format data
		//
		Object[] rowdata = sqlrset.getSQLRowCSV(sqldelim).toArray();
		if(null != rowdata && rowdata.length != 0){
			return rowdata;
		}

		// Column
//		if (sqlhead.equalsIgnoreCase("yes") && rowdata.length > 0) {
//			String columndata = sqlrset.getSQLColumns(sqldelim);
//			osw.write(columndata.toString());
//		}
		
		// Row 
//		for (int i = 0; i < rowdata.length; i++) {
//			osw.write(rowdata[i].toString());
//		}

//		osw.flush();
		return null;
		// Don't close writer since it is System.out
		//osw.close();

	}


	/**
	 * Validate if input parameters are valid for calling QuixSQLService web service.
	 *
	 * @param sqlstatement SQL statement.
	 * @param sqldelim Delimiter field.
	 * @param sqlhead SQL header.
	 * @return Status of validation.
	 * @throws Exception
	 */
	private boolean validateInputParams(String sqlstatement, String sqldelim, String sqlhead) throws Exception {
		boolean code = true;

		//
		// Validate other required properties
		//
		if (sqlstatement == null) {
			System.err.println("Error: Please set the sql.statement property. " +
					"This should contain the SQL statement to pass to the BO database.");
			code = false;
		} 
		
		//
		// Set default values to make logic easier elsewhere
		//
		if (sqldelim == null || sqldelim.equalsIgnoreCase("")) {
			this.prop.setProperty("sql.delimiter", "\t");
		}
		
		if (sqlhead == null) {
			this.prop.setProperty("sql.header", "yes");
		}
		

		
		return code;
	}
	
	/**
	 * Support method for main program. This support method can also be invoked from subclasses
	 * that support the same command line interface.
	 * @param args Command line arguments if desired.
	 * @param rsql QuixSQL object.
	 * @param name Class name.
	 */
    protected Object[] runMain(QuixDao rsql, String name) {
    	classname = name;
		try {
			//
			// Process commandline arguments. If HP-UX approves
			// Java parameter approach, this will be removed. If not,
			// a mechanism to read the arguments from the command line will
			// need to be provided.
			//
			String[] op = new String[2];
			op[0] = "QuixSQLService";
			op[1] = "getSQLResults";
			return rsql.processServiceOperation(op);
			
		} catch (AxisFault e) {
			eh = new ErrorHandler(e);
			eh.handleAxisFault();
		} catch (Exception e){
			eh.handleException();
		}
		return null;

	}
	
    
    /**
	 * QuixSQL usage message
	 *
	 */
	private static void printUsage(){
		System.err.println(	"" +
				"Usage: [-D<propertyname>=<propertyvalue>..] " +
				classname +"\n\n" +
				"Required Common Properties:\n" +
				"\t-Dserver.name=<value>\n" +
				"\t-Dkeystore.file=<value>\n" +
				"\t-Dkeystore.passwd=<value>\n" +				
				"\t-Dkey.username=<value>\n" + 
				"\t-Dkey.userpasswd=<value>\n\n" +
				"Required Service Specific Properties:\n" +
				"\t-Dsql.statement=<value>\n" +
				"\t-Doperation=getSQLResult //required if called from com.hp.quix.client.RQuix\n\n" +
				"Optional Service Specific properties\n" +
				"\t-Dproperty.file=<value>\n" +
				"\t-Dsql.delimiter=<value>\n" +
				"\t-Dsql.header=<value>\n\n" +
				
				"Example:\n" +
				"-Dsql.statement=\"SELECT ISSUEID, STATE FROM QUIX.BO_QX_CR where ISSUEID='QXCR1000051504'\" " +
				classname);			
	}
	
	public DefectVo getQuixVoByQuixNum(String quixNum){
		PropertiesUtility prop = new PropertiesUtility();
    	// the real work is delegated to another routine so that it can be used in a subclass
		String querySql = "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, OWNER_EMAIL, ENGINEERING_PRIORITY FROM QUIX.BO_QX_CR CR where CR.ISSUEID='"+quixNum+"'";
    	Object[] obj = (new QuixDao(prop, querySql)).runMain(new QuixDao(prop, querySql), "com.hp.quix.client.QuixSQL");
    	if(null == obj){
    		return null;
    	}
    	String[] res = String.valueOf(obj[0]).split("\t");
    	DefectVo dVo = new DefectVo();
    	dVo.setObjectId(String.valueOf(res[0]));
    	dVo.setAddUrl(String.valueOf(res[1]));
    	dVo.setDefectNum(String.valueOf(res[2]));
    	dVo.setDefectName(String.valueOf(res[3]));
    	dVo.setSubmittedBy(String.valueOf(res[4]));
    	dVo.setLastupdateDate(String.valueOf(res[5]));
    	dVo.setState(String.valueOf(res[6]));
    	dVo.setDeveloper(String.valueOf(res[7]));
    	dVo.setPriority(String.valueOf(res[8]));
    	return dVo;
	}
	
	public List<DefectVo> getQuixDefectInfoByEmail(String email) throws IOException{
		PropertiesUtility prop = new PropertiesUtility();
    	// the real work is delegated to another routine so that it can be used in a subclass
		String querySql = "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, OWNER_EMAIL, ENGINEERING_PRIORITY, PROJECT_NAME, COMPONENT, SUBMITDATE FROM QUIX.BO_QX_CR CR where CR.SUBMITTER_EMAIL='"+email+"'";
    	Object[] obj = (new QuixDao(prop, querySql)).runMain(new QuixDao(prop, querySql), "com.hp.quix.client.QuixSQL");
    	if(null == obj){
    		return null;
    	}
    	List<DefectVo> resList = new ArrayList<DefectVo>();
    	for(int i = 0; i < obj.length; i++){
    		String[] res = String.valueOf(obj[i]).split("\t");
    		DefectVo dVo = new DefectVo();
        	dVo.setObjectId(String.valueOf(res[0]));
        	dVo.setAddUrl(String.valueOf(res[1]));
        	dVo.setDefectNum(String.valueOf(res[2]));
        	dVo.setDefectName(String.valueOf(res[3]));
        	dVo.setSubmittedBy(String.valueOf(res[4]));
        	dVo.setLastupdateDate(String.valueOf(res[5]));
        	dVo.setState(String.valueOf(res[6]));
        	dVo.setDeveloper(String.valueOf(res[7]));
        	dVo.setPriority(String.valueOf(res[8]));
        	dVo.setProjectName(String.valueOf(res[9]));
        	if(null != res[10] && !"".equals(res[10])){
        		dVo.setComponent(String.valueOf(res[10]));
        	}
        	dVo.setSubmitDate(String.valueOf(res[11]));
        	resList.add(dVo);
    	}
    	return resList;
	}
	
	public List<DefectVo> getQuixDefectInfoByProjectModule(String projectName, String module) throws IOException{
		PropertiesUtility prop = new PropertiesUtility();
    	// the real work is delegated to another routine so that it can be used in a subclass
		String querySql = "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, DEVELOPMENT_ENGINEER, ENGINEERING_PRIORITY, PROJECT_NAME, COMPONENT, SUBMITDATE FROM QUIX.BO_QX_CR CR where CR.PROJECT_NAME='"+projectName+"'";
//    	String querySql = "SELECT * from QUIX.BO_QX_CR";
		Object[] obj = (new QuixDao(prop, querySql)).runMain(new QuixDao(prop, querySql), "com.hp.quix.client.QuixSQL");
    	if(null == obj){
    		return null;
    	}
    	List<DefectVo> resList = new ArrayList<DefectVo>();
    	for(int i = 0; i < obj.length; i++){
    		String[] res = String.valueOf(obj[i]).split("\t");
    		DefectVo dVo = new DefectVo();
        	dVo.setObjectId(String.valueOf(res[0]));
        	dVo.setAddUrl(String.valueOf(res[1]));
        	dVo.setDefectNum(String.valueOf(res[2]));
        	dVo.setDefectName(String.valueOf(res[3]));
        	dVo.setSubmittedBy(String.valueOf(res[4]));
        	dVo.setLastupdateDate(String.valueOf(res[5]));
        	dVo.setState(String.valueOf(res[6]));
        	if(null != res[7] && !"".equals(res[7])){
        		dVo.setDeveloper(String.valueOf(res[7]).split(" ")[1].replace("]", "").replace("[", ""));
        	}
        	dVo.setPriority(String.valueOf(res[8]));
        	dVo.setSubmitDate(String.valueOf(res[11]));
        	dVo.setProjectName(projectName);
        	dVo.setComponent(module);
        	resList.add(dVo);
    	}
    	return resList;
	}
	
	public List<DefectVo> getQuixDefectInfoByDeveloper(String email) throws IOException{
		PropertiesUtility prop = new PropertiesUtility();
    	// the real work is delegated to another routine so that it can be used in a subclass
		String querySql = "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, OWNER_EMAIL, ENGINEERING_PRIORITY, PROJECT_NAME, COMPONENT, SUBMITDATE FROM QUIX.BO_QX_CR CR where CR.OWNER_EMAIL='"+email+"'";
    	Object[] obj = (new QuixDao(prop, querySql)).runMain(new QuixDao(prop, querySql), "com.hp.quix.client.QuixSQL");
    	if(null == obj){
    		return null;
    	}
    	List<DefectVo> resList = new ArrayList<DefectVo>();
    	for(int i = 0; i < obj.length; i++){
    		String[] res = String.valueOf(obj[i]).split("\t");
    		DefectVo dVo = new DefectVo();
        	dVo.setObjectId(String.valueOf(res[0]));
        	dVo.setAddUrl(String.valueOf(res[1]));
        	dVo.setDefectNum(String.valueOf(res[2]));
        	dVo.setDefectName(String.valueOf(res[3]));
        	dVo.setSubmittedBy(String.valueOf(res[4]));
        	dVo.setLastupdateDate(String.valueOf(res[5]));
        	dVo.setState(String.valueOf(res[6]));
        	dVo.setDeveloper(String.valueOf(res[7]));
        	dVo.setPriority(String.valueOf(res[8]));
        	dVo.setProjectName(String.valueOf(res[9]));
        	if(null != res[10] && !"".equals(res[10])){
        		dVo.setComponent(String.valueOf(res[10]));
        	}
        	dVo.setSubmitDate(String.valueOf(res[11]));
        	resList.add(dVo);
    	}
    	return resList;
	}
	
	
	public List<DefectVo> getQuixForReport(String email, String startDate, String endDate, String projects) throws IOException{
		
		PropertiesUtility prop = new PropertiesUtility();
		String querySql = "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, DEVELOPMENT_ENGINEER, ENGINEERING_PRIORITY, PROJECT_NAME, COMPONENT, SUBMITDATE FROM QUIX.BO_QX_CR CR where CR.PROJECT_NAME IN (" + projects + ")";
		querySql = querySql + " AND CR.DEVELOPMENT_ENGINEER like '%"+email+"%'" ;
		
		if(null != startDate && null != endDate){
			querySql = querySql + " AND CR.LASTSTATECHANGEDATE >= TO_DATE('" +startDate + "','yyyy-mm-dd hh24:mi:ss') AND CR.LASTSTATECHANGEDATE <= + TO_DATE('" +endDate + "','yyyy-mm-dd hh24:mi:ss')" ;
		}		
		Object[] obj = (new QuixDao(prop, querySql)).runMain(new QuixDao(prop, querySql), "com.hp.quix.client.QuixSQL");
    	if(null == obj){
    		return null;
    	}
    	
    	
    	
    	List<DefectVo> resList = new ArrayList<DefectVo>();
    	for(int i = 0; i < obj.length; i++){
    		String[] res = String.valueOf(obj[i]).split("\t");
    		DefectVo dVo = new DefectVo();
        	dVo.setObjectId(String.valueOf(res[0]));
        	dVo.setAddUrl(String.valueOf(res[1]));
        	dVo.setDefectNum(String.valueOf(res[2]));
        	dVo.setDefectName(String.valueOf(res[3]));
        	dVo.setSubmittedBy(String.valueOf(res[4]));
        	dVo.setLastupdateDate(String.valueOf(res[5]));
        	dVo.setState(String.valueOf(res[6]));
        	dVo.setDeveloper(String.valueOf(res[7]));
        	dVo.setPriority(String.valueOf(res[8]));
        	dVo.setProjectName(String.valueOf(res[9]));
        	if(null != res[10] && !"".equals(res[10])){
        	    dVo.setComponent(String.valueOf(res[10]));
        	}
        	dVo.setSubmitDate(String.valueOf(res[11]));
        	resList.add(dVo);
    	}
    	return resList;
	}
    
    
	/**
	 * This class can be invoked from the command line by setting system properties
	 * on the command line or via a properties file.
	 * @param args Command line arguments if desired.
	 * @throws Exception
	 */
    public static void main (String args[]) throws Exception {
    	PropertiesUtility prop = new PropertiesUtility();
    	// the real work is delegated to another routine so that it can be used in a subclass
    	(new QuixDao(prop, "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, OWNER_EMAIL, ENGINEERING_PRIORITY FROM QUIX.BO_QX_CR CR where CR.OWNER_EMAIL='xu.yang@hp.com'")).runMain(new QuixDao(prop, "SELECT INTERNAL_SYSTEM_ID, PCT_URL, ISSUEID, TITLE, SUBMITTER_EMAIL, LASTSTATECHANGEDATE, STATE, OWNER_EMAIL, ENGINEERING_PRIORITY FROM QUIX.BO_QX_CR CR where CR.OWNER_EMAIL='xu.yang@hp.com'"), "com.hp.quix.client.QuixDao");
    }

}

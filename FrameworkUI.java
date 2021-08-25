package base;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import customExceptions.ProjectConfigurationException;
import customExceptions.ProjectNotFoundException;

import base.CommonOperations;
import dataXML.DataXMLGenerator;
import execution.Executor;
import functionCollection.BaseExecutionFilePaths;
import functionCollection.BaseFilePaths;
import functionCollection.TimeRelatedFunctions;
import functionCollection.essentials;
import functionCollection.fileRelatedFunctions;
import functionCollection.folderRelatedFunctions;
import learning.BackwardCompatibility;
import learning.NaviLearner;
import obbrn.execution.IExecutor;
import obbrn.learning.INaviLearner;
import functionCollection.MessageObject;

import readingConfigFile.ConfigObject;
import readingConfigFile.ConfigXML;
import readingConfigFile.DatabaseDetails;
import readingConfigFile.FlexcubeUser;
import readingConfigFile.ProjectAccessDetails;
import readingConfigFile.ProjectComponent;
import standardValues.LEARN_BUTTON_SUBSCREENS_OPTIONS;
import standardValues.LEARN_LINK_SUBSCREENS_OPTIONS;
import standardValues.LEARN_OPTIONS;
import standardValues.LEARN_REKEY_OPTIONS;
import standardValues.LEARN_SCREEN_TYPE;login
import standardValues.OATSF_Activities;
import standardValues.StandardCommonVal;
import standardValues.StandardConfigFileVal;
import standardValues.StandardFolderAndFileVal;
import standardValues.StandardFunctionListVal;
import standardValues.StandardSummaryXMLVal;
import summary.SummaryExcelToXML;
import summary.SummaryGenerator;
import com.oracle.session.learn.SessionVariables;login
import com.oracle.session.exec.ExecutionSessionVariables;

public class FrameworkUI {
	
	static String ATRepository = "";
	static String ATFolderPath="";
	static String adminConfigFolderPath=StandardCommonVal.ADMIN_CONFIG_DOES_NOT_EXIST;
	static String adminConfigFilePath = "";
	static String OATSHome="";
	static String product = "";
	
	static boolean branchChange = false;
	static String centralLogFilePath = "";
	
	static boolean parallelProcessingFlag = false;
	static boolean browserLaunched = false;
	
	static String language = "English";
	static String preferredBrowser = "Chrome";
	
	static ConfigObject configDetails = null;
	static ArrayList<MessageObject> messages;
	static String[] browsersUnderConsideration = {"Firefox","Chrome", "Edge"};
	
	static SessionVariables sv = new SessionVariables();
	static ExecutionSessionVariables esv= new ExecutionSessionVariables();
	
	static WebDriver driver = null;
	static xPathObjectRepository OR = null;
	public static boolean backwardCompatibility = false;
	
	public static void main(String[] args) {
		
		try 
		{
  			String functionToCall = "execute";
			String paramToPass = "";
			
			if(functionToCall.equals("learn")) {
			//	paramToPass = "C:\\AutomationTesting\\ADMIN\\FunctionList.xls,C:\\AutomationTesting,,C:\\AutomationTesting\\ServerConfiguration,C:\\AutomationTesting\\ServerConfiguration\\config.xml,,Chrome,JCAPSEL_URL,JCAPSEL_DB";
				paramToPass = "C:\\JCAP_Setup\\AutomationTesting\\ADMIN\\FunctionList.xls,C:\\JCAP_Setup\\AutomationTesting,,C:\\JCAP_Setup\\AutomationTesting\\ServerConfiguration,C:\\JCAP_Setup\\AutomationTesting\\ServerConfiguration\\config.xml,,"+ preferredBrowser +",JCAPSEL_URL,JCAPSEL_DB";

			}
			else if(functionToCall.equals("backwardCompatibility")) {
				paramToPass = "D:\\Softwares\\AutomationTesting\\ADMIN\\FunctionList.xls,D:\\Softwares\\AutomationTesting,,D:\\Softwares\\AutomationTesting\\ServerConfiguration,D:\\Softwares\\AutomationTesting\\ServerConfiguration\\config.xml,,Chrome,JCAPSEL_URL,JCAPSEL_DB";
			}
			else if(functionToCall.equals("generateSummary")) {
				//paramToPass = "C:\\Users\\esekar\\Desktop\\Selenium\\Demo\\Tests\\ParentChild\\,C:\\AutomationTesting,false";
				paramToPass = "C:\\Execution_JCAPSel\\CFDFLTRI\\,C:\\JCAP_Setup\\AutomationTesting\\,false";

			}
			else if(functionToCall.equals("generateXMLs")) {
				//paramToPass = "C:\\Users\\esekar\\Desktop\\Selenium\\Demo\\Tests\\ParentChild\\,C:\\AutomationTesting,C:\\AutomationTesting\\ServerConfiguration";
				paramToPass = "C:\\Execution_JCAPSel\\CFDFLTRI\\,C:\\JCAP_Setup\\AutomationTesting\\,C:\\JCAP_Setup\\AutomationTesting\\ServerConfiguration";

			}
			else if(functionToCall.equals("execute")) {
				// paramToPass = "C:\\Users\\esekar\\Desktop\\Selenium\\Demo\\Tests\\ParentChild\\SUMMARY.xls,C:\\AutomationTesting,C:\\AutomationTesting\\ServerConfiguration,C:\\AutomationTesting\\ServerConfiguration\\config.xml,,Chrome,null,null,null,null,null";
				paramToPass = "C:\\Execution_JCAPSel\\CFDFLTRI\\SUMMARY.xls,C:\\JCAP_Setup\\AutomationTesting,C:\\JCAP_Setup\\AutomationTesting\\ServerConfiguration,C:\\JCAP_Setup\\AutomationTesting\\ServerConfiguration\\config.xml,,Chrome,null,null,null,null,null";

			}
			
			System.out.println("parameters: "+paramToPass);

			if(functionToCall != null && paramToPass != null )
			{
				processCommands(functionToCall, paramToPass);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Exception in run() in FrameworkUI Script : " + e.getMessage());
		}
	}
	
	public static void processCommands(String functionToCall, String paramToPass)
	{
		messages = new ArrayList<MessageObject>();
		if(functionToCall.equals("backwardCompatibility"))
		{
			functionToCall="learn";
			backwardCompatibility = true;
		}
		
		try{
			if(functionToCall.equals("learn"))
			{
				if(paramToPass.contains(","))
				{
					String[] ArgumentsParam = paramToPass.split(",");
					if(ArgumentsParam.length == 9)
					{
						String functionListPath = ArgumentsParam[0].trim();
						ATFolderPath = ArgumentsParam[1].trim();
						//String QueryDBPath = ArgumentsParam[2].trim();
						adminConfigFolderPath = ArgumentsParam[3].trim();
						adminConfigFilePath = ArgumentsParam[4].trim();
						//String serverRepository = ArgumentsParam[5].trim();
						preferredBrowser = ArgumentsParam[6].trim();
						String componentName = ArgumentsParam[7].trim();
						//componentName = "FLEXPAY_URL";//temporary - remove this
						String dbName = ArgumentsParam[8].trim();
						readConfigXML(OATSF_Activities.LEARNING);
						
						learn(componentName, dbName, functionListPath);
					}
				}
			}
			else if(functionToCall.equals("generateSummary"))
			{
				if(paramToPass.contains(","))
				{
					String[] ArgumentsParam = paramToPass.split(",");
					if(ArgumentsParam.length == 3)
					{
						String summaryFolderPath = ArgumentsParam[0].trim();
						ATFolderPath = ArgumentsParam[1].trim();
						String appendString = ArgumentsParam[2].trim();

						boolean append = false;

						if(appendString.equalsIgnoreCase("true"))
						{
							append = true;
						}
						else
						{
							append = false;
						}

						readConfigXML(OATSF_Activities.SUMMARY_GENERATION);

						generateSummary(summaryFolderPath, append);
					}
				}
			}
			else if(functionToCall.equals("generateXMLs"))
			{
				if(paramToPass.contains(","))
				{
					String[] ArgumentsParam = paramToPass.split(",");
					if(ArgumentsParam.length == 3)
					{
						String summaryFolderPath = ArgumentsParam[0].trim();
						ATFolderPath = ArgumentsParam[1].trim();
						adminConfigFolderPath = ArgumentsParam[2].trim();

						readConfigXML(OATSF_Activities.REFRESH_DATA);

						generateXMLs(summaryFolderPath, configDetails.getProjectID());
					}
				}
			}
			else if(functionToCall.equals("execute"))
			{
				if(paramToPass.contains(","))
				{
					String[] ArgumentsParam = paramToPass.split(",");
					if(ArgumentsParam.length == 11)
					{
						String summaryFilePath = ArgumentsParam[0].trim();
						ATFolderPath = ArgumentsParam[1].trim();
						adminConfigFolderPath = ArgumentsParam[2].trim();
						adminConfigFilePath = ArgumentsParam[3].trim();
						//String serverRepository = ArgumentsParam[4].trim();
						preferredBrowser = ArgumentsParam[5].trim();
						String fpsConfigFolderPath = ArgumentsParam[6].trim();
						String fpsProjectId = ArgumentsParam[7].trim();

						String userScheduledBy = ArgumentsParam[8].trim();
						String scheduledJobFile = ArgumentsParam[9].trim();
						String jobScheduled = ArgumentsParam[10].trim();


						if(fpsProjectId.equals("OATSF_NOFPS"))
						{
							fpsProjectId="";
						}

						System.out.println("SERVER PATH: "+adminConfigFolderPath);

						readConfigXML(OATSF_Activities.EXECUTION);

						System.out.println("summaryfilepath: "+summaryFilePath);
						execute(summaryFilePath, fpsConfigFolderPath, fpsProjectId, scheduledJobFile, jobScheduled, userScheduledBy);
					}
				}
			}
		}
		catch(ProjectNotFoundException pe)
		{
			essentials.displayMessages(messages, StandardConfigFileVal.VALUE_DEFAULT_MSG_COUNT_PER_PANE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			essentials.displayMessages(messages, configDetails.getAdvancedSettings().getNumberOfMessagesPerPane());
			System.out.println("Exception in processCommands: "+e.getMessage());
		}
	}
	
	public static void readConfigXML(OATSF_Activities activity) throws ProjectNotFoundException, ProjectConfigurationException
	{
		String activeProjectId = essentials.getActiveProjectID(ATFolderPath, activity, messages);
		readConfigXML(activeProjectId, activity);
	}
	
	public static void readConfigXML(String activeProjectId, OATSF_Activities activity) throws ProjectNotFoundException, ProjectConfigurationException
	{
		configDetails = ConfigXML.readConfigXML(ATFolderPath, activeProjectId, adminConfigFolderPath, messages, activity);
		
		String dateString = TimeRelatedFunctions.getCurrentTimeString();
		String defaultComponent = configDetails.getDefaultProjectComponentName();
		product = configDetails.getProjectComponentMap().get(defaultComponent).getProjectDetails().getProduct();
		centralLogFilePath = configDetails.getProjectComponentMap().get(defaultComponent).getProjectArtifactFolderPaths().getProjectLogFolderPath() + "\\" + activity.toString() + "-" + dateString + ".txt";
		
		BaseFilePaths baseFilePaths = new BaseFilePaths(ATFolderPath, adminConfigFolderPath);
		ATRepository = baseFilePaths.getATCodeRepositoryPath();
		OATSHome = BaseFilePaths.getOATSHome();
		
		sv.setActiveProjectID(configDetails.getProjectID());
		sv.setAtFolderPath(ATFolderPath);
		sv.setFpsConfigFolderPath(adminConfigFolderPath);
		sv.setServerPath(adminConfigFolderPath);
		sv.setAdminConfigPath(adminConfigFilePath);
		sv.setServerRepository(ATRepository);
		sv.setPreferredBrowser(preferredBrowser);
		
		esv.setPreferencesOATSActiveProjectID(configDetails.getProjectID());
		esv.setEnvironmentATFolderPath(ATFolderPath);
		esv.setEnvironmentFPSServerConfigFolderPath(adminConfigFolderPath);
		esv.setEnvironmentOATSServerConfigFolderPath(adminConfigFolderPath);
		esv.setEnvironmentAdminConfigFilePath(adminConfigFilePath);
		esv.setEnvironmentServerRepository(ATRepository);
		esv.setPreferencesActiveBrowser(preferredBrowser);
	}
	
	private static void setupBrowser(String preferredBrowser2, ProjectComponent projectComponent, String learnLogFileName) {
		switch(preferredBrowser2)
		{
			case "Chrome":
			{
				System.setProperty("webdriver.chrome.driver",esv.getEnvironmentATFolderPath()+"\\ADMIN\\Browsers\\Chrome\\chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--window-size=1920,1200","--ignore-certificate-errors");
				driver=new ChromeDriver(options);
				driver.get(projectComponent.getProjectAccessDetails().getProjectURL());
				driver.manage().window().maximize(); 
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				fileRelatedFunctions.updateFile(learnLogFileName,"Chrome browser Launched."+"\n");
				break;
			}
			case "Firefox":
			{
				System.setProperty("webdriver.gecko.driver",esv.getEnvironmentATFolderPath()+"\\ADMIN\\Browsers\\Mozilla\\geckodriver.exe");
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--window-size=1920,1200","--ignore-certificate-errors");
	
				driver=new FirefoxDriver(options);
				driver.get(projectComponent.getProjectAccessDetails().getProjectURL());
				driver.manage().window().maximize(); 
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

				fileRelatedFunctions.updateFile(learnLogFileName,"Firefox browser Launched."+"\n");
				break;
			}	
			case  "Edge":
			{
				System.setProperty("webdriver.edge.driver", esv.getEnvironmentATFolderPath()+"\\ADMIN\\Browsers\\Edge\\msedgedriver.exe");
				EdgeOptions options = new EdgeOptions();
				options.setCapability("--window-size=1920,1200","--ignore-certificate-errors");
				DesiredCapabilities capabilities = new DesiredCapabilities().edge();
				capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capabilities.setCapability("ignoreZoomSetting", true);
				capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);				
				//capabilities.setCapability(CapabilityType., true);
				options.merge(capabilities);
	
				driver=new EdgeDriver(options);
				driver.get(projectComponent.getProjectAccessDetails().getProjectURL());
				driver.manage().window().maximize(); 
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	
				fileRelatedFunctions.updateFile(learnLogFileName,"Edge browser Launched."+"\n");
			}
		}		
		
		browserLaunched = true;
		handleErrorsOrOverrides();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}	
		
	}
	
	public static void handleErrorsOrOverrides() {
		try {
			if(driver.findElements(By.xpath("//*[@id='ifr_AlertWin']")) != null)
			{
				if(driver.findElement(By.xpath("//*[@id='ifr_AlertWin']")).isDisplayed())
				{
					driver.switchTo().frame("ifr_AlertWin");
					String pagesourceArlt = driver.getPageSource();
					org.jsoup.nodes.Document alertdoc = Jsoup.parse(pagesourceArlt);
					String msgtype = alertdoc.selectFirst("h1.WNDtitletxt").text().toLowerCase().trim();
					
					if(msgtype.equals("information message"))
					{
						StringBuilder alertmsg = new StringBuilder();
						List<org.jsoup.nodes.Element> tblelements = alertdoc.select("[id=ERRTBL] tr span");
						if(tblelements != null)
						{
							for(int msg=0; msg<tblelements.size(); msg++)
							{
								alertmsg.append(tblelements.get(msg).text()).append("; ");
							}
						}
						
						driver.findElement(By.xpath("//*[@id='BTN_OK']")).click();
						
						System.out.println("information message : "+alertmsg.toString());
					}
					else if(msgtype.equals("error message"))
					{
						StringBuilder errormsg = new StringBuilder();
						List<org.jsoup.nodes.Element> tblelements = alertdoc.select("[id=ERRTBL] tr span");
						if(tblelements != null)
						{
							for(int msg=0; msg<tblelements.size(); msg++)
							{
								errormsg.append(tblelements.get(msg).text()).append("; ");
							}
						}
						driver.findElement(By.xpath("//*[@id='BTN_OK']")).click();
						
						System.out.println("Error Found : "+errormsg.toString());
					}
					
					driver.switchTo().parentFrame();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void learn(String componentName, String dbName, String functionListPath)
	{
		setupProject(ATFolderPath);

		ProjectComponent projectComponent = configDetails.getProjectComponentMap().get(componentName);
		
		String dateString = TimeRelatedFunctions.getCurrentTimeString();
		String learnLogFileName = projectComponent.getProjectArtifactFolderPaths().getProjectLogFolderPath() + StandardFolderAndFileVal.OATSF_LEARN_LOG_FILENAME + " - " + dateString + ".txt";
		fileRelatedFunctions.updateFile(learnLogFileName, "learning started"+"\n");
		
		if(!new File(functionListPath).exists())
		{
			JOptionPane.showMessageDialog(null, "Looking for Function List in "+functionListPath+". Unable to locate FunctionList.xls file.", "Alert", JOptionPane.ERROR_MESSAGE);
			System.out.println("FunctionList.xls File Not Found during learning in FrameworkUI: "+functionListPath);
			fileRelatedFunctions.updateFile(learnLogFileName,"FunctionList.xls File Not Found during learning in FrameworkUI: "+functionListPath+"\n");
			return;
		}
		
		if(projectComponent.getProjectDetails().getProduct().equalsIgnoreCase("OBBRN"))
		{
			INaviLearner learner=new obbrn.learning.NaviLearner();
			learner.learnOBBRN(projectComponent, esv, configDetails, ATFolderPath, learnLogFileName, functionListPath);
			return;
		}
		
		FileInputStream fileInputStream = null;
		HSSFWorkbook workbook = null;
		
		try {
			fileInputStream = new FileInputStream(functionListPath);
			workbook = new HSSFWorkbook(fileInputStream);
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			
		} finally
		{
			if(fileInputStream!=null)
			{
				try {
					fileInputStream.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		if(workbook==null)
		{
			return;
		}

		String msg="Learning Completed ";
		
		

		boolean loginSuccessful = false;

		if(!projectComponent.getProjectDetails().getProduct().equalsIgnoreCase("FCDB"))
		{
			setupBrowser(preferredBrowser, projectComponent,learnLogFileName );
			loginSuccessful = FCUBSandFCISlogin(projectComponent, learnLogFileName);
		}
		else
		{
			// -- check later fcdbLogin(projectComponent, learnLogFileName);
		}

		if(loginSuccessful)
		{
			HSSFSheet functionListSheet = workbook.getSheet(StandardFolderAndFileVal.OATSF_LEARN_FUNCTION_LIST_SHEETNAME);
			HSSFSheet queryListSheet = workbook.getSheet(StandardFolderAndFileVal.OATSF_LEARN_QUERY_LIST_SHEETNAME);

			int rowsCount = functionListSheet.getLastRowNum();
			String FunctionID="";
			int totalfunctioncount=0;

			for(int functionCount=StandardFunctionListVal.FUNCTION_LIST_FIRST_ROW_NUMBER; functionCount<=rowsCount;functionCount++)
			{
				if(functionListSheet.getRow(functionCount)!=null)
				{
					String learn = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_LEARN_SELECTION_COLUMN_NUMBER, functionListSheet);
					LEARN_OPTIONS learnOption = LEARN_OPTIONS.getLearningType(learn);
					if(learnOption!=LEARN_OPTIONS.INVALID_SELECTION && learnOption!=LEARN_OPTIONS.DO_NOT_LEARN)
					{
						totalfunctioncount++;
					}
				}
			}
			
			sv.setTotalfunctioncount(totalfunctioncount);

			String currentbranchCode = "Home";

			//Multiple Login Logout  End
			for(int functionCount=StandardFunctionListVal.FUNCTION_LIST_FIRST_ROW_NUMBER; functionCount<=rowsCount;functionCount++)
			{
				String learningType = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_LEARN_SELECTION_COLUMN_NUMBER, functionListSheet).trim().toLowerCase();
				
				if(backwardCompatibility)
				{
					if(learningType.equalsIgnoreCase("Artifacts Conversion"))
					{
						learningType = "Learn";
					}
					else if(learningType.equalsIgnoreCase("Assisted Artifacts Conversion"))
					{
						learningType = "Assisted Learn";
					}
				}
				
				LEARN_OPTIONS learnOption = LEARN_OPTIONS.getLearningType(learningType);
				
				if(learnOption!=LEARN_OPTIONS.INVALID_SELECTION && learnOption!=LEARN_OPTIONS.DO_NOT_LEARN)
				{
					FunctionID =CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_FUNCTION_ID_COLUMN_NUMBER, functionListSheet);
					
					if(LEARN_OPTIONS.isAssisted(learnOption))
					{
						sv.setAssistedLrnRequired(true);
					}
					else
					{
						sv.setAssistedLrnRequired(false);
					}

					String screenType = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_SCREEN_TYPE_COLUMN_NUMBER, functionListSheet).trim().toLowerCase();
//					LEARN_SCREEN_TYPE learnScreenType = LEARN_SCREEN_TYPE.getScreenType(screenType);
					
					sv.setTellerScreen(false);
					sv.setSummaryScreen(false);
					sv.setHolidayScreen(false);
					sv.setRekeyOnly(false);
					sv.setPopUpOnly(false);
					
					sv.setSummaryRekeyScreens("");
					
					switch(LEARN_SCREEN_TYPE.getScreenType(screenType)){
					case TELLER_SCREEN:
						sv.setTellerScreen(true);

						String numberOfForms = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_TELLER_FORM_COUNT_COLUMN_NUMBER, functionListSheet).trim().toLowerCase();
						if(numberOfForms.equals(""))
						{
							numberOfForms = "1";
						}
						sv.setTellerFormCount(numberOfForms);

						String localAuthScrnFormNumber = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_TELLER_LOCAL_AUTH_FORM_NUMBER_COLUMN_NUMBER, functionListSheet);
						sv.setLocalAuthScrnFormNumber(localAuthScrnFormNumber);
						break;
					
					case SUMMARY_SCREEN:
						sv.setSummaryScreen(true);
						String summaryRekeyScreens = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_SUMMARY_REKEY_SCREENS_COLUMN_NUMBER, functionListSheet);

						if(summaryRekeyScreens!=null&&!summaryRekeyScreens.equals(""))
						{
							sv.setSummaryRekeyScreens(summaryRekeyScreens);
						}
						else
						{
							sv.setSummaryRekeyScreens("");
						}
						break;
					
					case HOLIDAY_SCREEN:
						sv.setHolidayScreen(true);
						break;
					
					case REKEY_ONLY:
						sv.setRekeyOnly(true);
						break;
						
					case POPUP_SCREEN_ONLY:
						sv.setPopUpOnly(true);
						break;
					}

					String rekeyScreens = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_REKEY_OPS_COLUMN_NUMBER, functionListSheet).trim().toLowerCase();
					sv.setAuthorizeLrnRequired(false);
					sv.setCloseLrnRequired(false);
					sv.setReverseLrnRequired(false);
					sv.setReopenLrnRequired(false);
					
					if(rekeyScreens.contains("~"))
					{
						String[] rekeyScreensInd = rekeyScreens.split("~");
						for(int rk=0; rk<rekeyScreensInd.length; rk++)
						{
							switch(LEARN_REKEY_OPTIONS.getRekeyType(rekeyScreensInd[rk])){
							case AUTHORIZE:
								sv.setAuthorizeLrnRequired(true);
								break;
							case CLOSE:
								sv.setCloseLrnRequired(true);
								break;
							case REVERSE:
								sv.setReverseLrnRequired(true);
								break;
							case REOPEN:
								sv.setReopenLrnRequired(true);login
								break;
							}
						}
					}
					else if(!rekeyScreens.equals(""))
					{
						switch(LEARN_REKEY_OPTIONS.getRekeyType(rekeyScreens)){
							case AUTHORIZE:
								sv.setAuthorizeLrnRequired(true);
								break;
							case CLOSE:
								sv.setCloseLrnRequired(true);
								break;
							case REVERSE:
								sv.setReverseLrnRequired(true);
								break;
							case REOPEN:
								sv.setReopenLrnRequired(true);
								break;
						}
					}

					//for branch change
					String checkbranchCode = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_BRANCH_COLUMN_NUMBER, functionListSheet);

					if(checkbranchCode!=null&&!checkbranchCode.equals(""))
					{
						if(currentbranchCode.equalsIgnoreCase(checkbranchCode))
						{
							//do nothing
						}
						else
						{
							try{
								//   -- check later
								changebranch(projectComponent, checkbranchCode,learnLogFileName);
							}catch(Exception e)
							{
								System.out.println("Exception in changeBranch: "+e.getMessage());
								return;
							}
							
							if(branchChange)
							{
								currentbranchCode = checkbranchCode;
							}
						}
					}

					//button screens
					String includeOrExclude = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_BUTTON_SUBSCREENS_OPTIONS_COLUMN_NUMBER, functionListSheet);
					sv.setIncludeAll(true);
					sv.setIncludeFlag(false);
					sv.setExcludeFlag(false);
					sv.setExcludeAll(false);
					
					
					switch(LEARN_BUTTON_SUBSCREENS_OPTIONS.getSelectedOption(includeOrExclude)){
					case INCLUDE_SELECTED_SCREENS:
						sv.setIncludeAll(false);
						sv.setIncludeFlag(true);
						break;
					case EXCLUDE_SELECTED_SCREENS:
						sv.setIncludeAll(false);
						sv.setExcludeFlag(true);
						break;
					case EXCLUDE_ALL:
						sv.setIncludeAll(false);
						sv.setExcludeAll(true);
						break;
					}
					
						
					if(sv.isIncludeFlag() || sv.isExcludeFlag())
					{
						String buttons = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_BUTTON_SUBSCREENS_LIST_COLUMN_NUMBER, functionListSheet);
						if(buttons!=null&&!buttons.trim().equals(""))
						{
							sv.setButtonsSpecified(buttons);
						}
						else
						{
							JDialog.setDefaultLookAndFeelDecorated(true);
							int response = JOptionPane.showConfirmDialog(null, "Buttons have not been specified for "+FunctionID+".If you wish to contine, framework will proceed to learn all the buttons. Do you want to continue?", "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (response == JOptionPane.NO_OPTION) {
								System.out.println("No button clicked");
								logout(projectComponent, "Exit", learnLogFileName);
								return;
							} else if (response == JOptionPane.YES_OPTION) {
								System.out.println("Yes button clicked");
								
								sv.setIncludeAll(true);
								sv.setIncludeFlag(false);
								sv.setExcludeFlag(false);
								sv.setExcludeAll(false);
								
							} else if (response == JOptionPane.CLOSED_OPTION) {
								System.out.println("JOptionPane closed");
								logout(projectComponent, "Exit", learnLogFileName);
								return;
							}
						}
					}
					
					String includeOrExcludeSubScreens = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_LINK_SUBSCREENS_OPTIONS_COLUMN_NUMBER, functionListSheet);
					sv.setIncludeAllSubScreens(true);
					sv.setIncludeSelectedSubScreens(false);
					sv.setExcludeSelectedSubScreens(false);
					sv.setExcludeAllSubScreens(false);
					
					switch(LEARN_LINK_SUBSCREENS_OPTIONS.getSelectedOption(includeOrExcludeSubScreens)){
						case INCLUDE_SELECTED_SCREENS:
							sv.setIncludeAllSubScreens(false);
							sv.setIncludeSelectedSubScreens(true);
							break;
						case EXCLUDE_SELECTED_SCREENS:
							sv.setIncludeAllSubScreens(false);
							sv.setExcludeSelectedSubScreens(true);
							break;
						case EXCLUDE_ALL:
							sv.setIncludeAllSubScreens(false);
							sv.setExcludeAllSubScreens(true);
							break;
					}
					
					if(sv.isIncludeSelectedSubScreens() || sv.isExcludeSelectedSubScreens())
					{
						String subScreens = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_LINK_SUBSCREENS_LIST_COLUMN_NUMBER, functionListSheet);
						if(subScreens!=null&&!subScreens.trim().equals(""))
						{
							sv.setSubScreensSpecified(subScreens);
						}
						else
						{
							JDialog.setDefaultLookAndFeelDecorated(true);
							int response = JOptionPane.showConfirmDialog(null, "subScreens have not been specified for "+FunctionID+".If you wish to contine, framework will proceed to learn all the subScreens. Do you want to continue?", "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (response == JOptionPane.NO_OPTION) {
								System.out.println("No button clicked");
								logout(projectComponent, "Exit", learnLogFileName);
								return;
							} else if (response == JOptionPane.YES_OPTION) {
								System.out.println("Yes button clicked");
								sv.setIncludeAllSubScreens(true);
								sv.setIncludeSelectedSubScreens(false);
								sv.setExcludeSelectedSubScreens(false);
								sv.setExcludeAllSubScreens(false);
							} else if (response == JOptionPane.CLOSED_OPTION) {
								System.out.println("JOptionPane closed");
								logout(projectComponent, "Exit", learnLogFileName);
								return;
							}
						}
					}
					
					String popUpsReq = CommonOperations.getcellContent(functionCount, StandardFunctionListVal.FUNCTION_LIST_SUBSCREENS_POPUP_LIST_COLUMN_NUMBER, functionListSheet);
					sv.setPopUpsReq(popUpsReq);

					System.out.println(functionCount + " : " +FunctionID);
					fileRelatedFunctions.updateFile(learnLogFileName,functionCount + " : " +FunctionID+"\n");
					
					if (sv.isAuthorizeLrnRequired())
					{
						int rowno=findRow(queryListSheet, FunctionID);
						String authQuery=CommonOperations.getcellContent(rowno,StandardFunctionListVal.QUERY_LIST_AUTH_QUERY_OR_DATA_COLUMN_NUMBER,queryListSheet);
						String contRefName=CommonOperations.getcellContent(rowno, StandardFunctionListVal.QUERY_LIST_FIELD_NAMES_COLUMN_NUMBER, queryListSheet);
						sv.setAuthQuery(authQuery);
						sv.setContRefName(contRefName);
						System.out.println("Query for Authorization --:"+authQuery);
						fileRelatedFunctions.updateFile(learnLogFileName,"Query for Authorization --:"+authQuery+"\n");
						System.out.println("contRefName for Authorization --:"+contRefName);
						fileRelatedFunctions.updateFile(learnLogFileName,"contRefName for Authorization --:"+contRefName+"\n");
					}
					
					if (sv.isReverseLrnRequired())
					{
						int rowno=findRow(queryListSheet, FunctionID);
						String revQuery=CommonOperations.getcellContent(rowno,StandardFunctionListVal.QUERY_LIST_REVERSE_QUERY_OR_DATA_COLUMN_NUMBER,queryListSheet);
						String revcontRefName=CommonOperations.getcellContent(rowno, StandardFunctionListVal.QUERY_LIST_FIELD_NAMES_COLUMN_NUMBER, queryListSheet);
						sv.setRevQuery(revQuery);
						sv.setRevcontRefName(revcontRefName);
						System.out.println("Query for Reverse--:"+revQuery);
						fileRelatedFunctions.updateFile(learnLogFileName,"Query for Reverse--:"+revQuery+"\n");
						System.out.println("contRefName for Reverse --:"+revcontRefName);
						fileRelatedFunctions.updateFile(learnLogFileName,"contRefName for Reverse --:"+revcontRefName+"\n");
					}

					if (sv.isCloseLrnRequired())
					{
						int rowno=findRow(queryListSheet, FunctionID);
						String closeQuery=CommonOperations.getcellContent(rowno,StandardFunctionListVal.QUERY_LIST_CLOSE_QUERY_OR_DATA_COLUMN_NUMBER,queryListSheet);
						String closeContRefName=CommonOperations.getcellContent(rowno, StandardFunctionListVal.QUERY_LIST_FIELD_NAMES_COLUMN_NUMBER, queryListSheet);
						sv.setCloseQuery(closeQuery);
						sv.setCloseContRefName(closeContRefName);
						System.out.println("Query for Close--:"+closeQuery);
						fileRelatedFunctions.updateFile(learnLogFileName,"Query for Close--:"+closeQuery+"\n");
						System.out.println("contRefName for Close --:"+closeContRefName);
						fileRelatedFunctions.updateFile(learnLogFileName,"contRefName for Close --:"+closeContRefName+"\n");
					}

					if (sv.isReopenLrnRequired())
					{
						int rowno=findRow(queryListSheet, FunctionID);
						String reopenQuery=CommonOperations.getcellContent(rowno,StandardFunctionListVal.QUERY_LIST_REOPEN_QUERY_OR_DATA_COLUMN_NUMBER,queryListSheet);
						String reopenContRefName=CommonOperations.getcellContent(rowno, StandardFunctionListVal.QUERY_LIST_FIELD_NAMES_COLUMN_NUMBER, queryListSheet);
						sv.setReopenQuery(reopenQuery);
						sv.setReopenContRefName(reopenContRefName);
						System.out.println("Query for Reopen--:"+reopenQuery);
						fileRelatedFunctions.updateFile(learnLogFileName,"Query for Reopen--:"+reopenQuery+"\n");
						System.out.println("contRefName for Reopen --:"+reopenContRefName);
						fileRelatedFunctions.updateFile(learnLogFileName,"contRefName for Reopen --:"+reopenContRefName+"\n");
					}
					
					try{
						if(!projectComponent.getProjectDetails().getProduct().equalsIgnoreCase("FCDB"))
						{
							if(projectComponent.getProjectDetails().getFlexcubeVersion().equals("10"))
							{
								// fcubsV10Learn();		-- check later
							}
							else
							{
								boolean learnSummaryScreen = false;
								fcubsV12Learn(projectComponent, dbName, learnSummaryScreen,FunctionID,learnLogFileName);
							}
						}
					}catch(Exception e)
					{
						e.printStackTrace();
						fileRelatedFunctions.updateFile(learnLogFileName, "Exception while calling learn script: "+e.getMessage()+"\n");
					}
				}
			}
			
			if(BackwardCompatibility.strBackCompMessage.equals("") == false)
			{
				String strFieldsToCheck1 = BackwardCompatibility.strBackCompMessage + "</body></html>";
				
				final JComponent[] inputttts = new JComponent[] {
						new JLabel(String.format(strFieldsToCheck1)) };
				JOptionPane.showMessageDialog(null, inputttts, "Information Message", JOptionPane.WARNING_MESSAGE);
				
				BackwardCompatibility.strBackCompMessage = strFieldsToCheck1.replace("<br>", "\n").replace("</br>", "\n");
				fileRelatedFunctions.updateFile(learnLogFileName,
						"There were more than one locations with same display name. Please contact Framework Support team.\n" + strFieldsToCheck1);
				BackwardCompatibility.strLogText = "There were more than one locations with same display name. Please contact Framework Support team.\n" + strFieldsToCheck1.replace("<br>", "\n").
						replace("</br>", "\n").replace("</body></html>", "").replace("<body>", "").replace("<html>", "").
						replace("<br/>", "\n").replace("<p>", "\t");;
				BackwardCompatibility.writeLogFile();
				BackwardCompatibility.strFieldsToCheck = "";
				BackwardCompatibility.strBackCompMessage = "";
				strFieldsToCheck1 = "";
				
				BackwardCompatibility.repeatDisplayName = false;
			}
	
			if((FunctionID.equals("")))
			{
				JOptionPane.showMessageDialog(null, "Mark as 'Y' in Learn column in FunctionList.xls for the required Function Id inorder to learn ", "Alert", JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			if(!projectComponent.getProjectDetails().getProduct().equalsIgnoreCase("FCDB"))
			{
				logout(projectComponent, msg, learnLogFileName);
			}
			else
			{
//				fcdbLogout();
			}
		}
		else
		{
			fileRelatedFunctions.updateFile(learnLogFileName, "Login failed.");
		}
	}
	
	private static int findRow(HSSFSheet sheet, String FunctionID) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equalsIgnoreCase(FunctionID)) {
						return row.getRowNum();  
					}
				}
			}
		}               
		return 0;
	}
	
	public static boolean setupProject (String atpath) {

		String FullPathFolderAutomationDirectory =atpath+"\\";
		String FullPathFolderATS = FullPathFolderAutomationDirectory + StandardFolderAndFileVal.ENVIRONMENT_OATSF_ATS_FOLDERNAME + "\\";
		
		try 
		{
			for(String componentName: configDetails.getProjectComponentMap().keySet()) {
				
				ProjectComponent projectComponent = configDetails.getProjectComponentMap().get(componentName);
				
				String Product = projectComponent.getProjectDetails().getProduct();
				String FlexcubeVersion = projectComponent.getProjectDetails().getFlexcubeVersion();
				String FlexcubeSubVersion = projectComponent.getProjectDetails().getFlexcubeSubVersion();
				String ProjectEntity = projectComponent.getProjectDetails().getProjectEntity();
				String Country = projectComponent.getProjectDetails().getCountry();
				String Release = projectComponent.getProjectDetails().getRelease();
				
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product);
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product+"\\"+FlexcubeVersion+"." +FlexcubeSubVersion );
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product+"\\"+FlexcubeVersion+"." +FlexcubeSubVersion + "\\" + 
							StandardFolderAndFileVal.ARTIFACTS_OATSF_PROJECTS_FOLDERNAME);
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product+"\\"+FlexcubeVersion+"." +FlexcubeSubVersion + "\\"+
							StandardFolderAndFileVal.ARTIFACTS_OATSF_PROJECTS_FOLDERNAME+"\\" + ProjectEntity );
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product+"\\"+FlexcubeVersion+"." +FlexcubeSubVersion + "\\"+
							StandardFolderAndFileVal.ARTIFACTS_OATSF_PROJECTS_FOLDERNAME+"\\" + ProjectEntity + "\\" + Country); 
				folderRelatedFunctions.CreateDirectory(FullPathFolderATS+Product+"\\"+FlexcubeVersion+"." +FlexcubeSubVersion + "\\"+
							StandardFolderAndFileVal.ARTIFACTS_OATSF_PROJECTS_FOLDERNAME+"\\" + ProjectEntity + "\\" + Country + "\\" + Release ); 
				
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getSMDFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getBLTFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getDataSheetFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getOATSFResultConfigFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getRELFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getProjectLogFolderPath());
				folderRelatedFunctions.CreateDirectory(projectComponent.getProjectArtifactFolderPaths().getQLFolderPath());
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in setupProject(): " + e.getMessage());
			return false;
		} 
		
		return true;
	}

	public static void enterText(WebElement we, String strText)
	{
		if(we.isEnabled() && we.isDisplayed())
		{
			we.sendKeys(strText);
		}
		else
		{
			System.out.println("Fail " + we.getTagName() + " was not found");
		}
	}
	
	public static void clickButton(WebElement we)
	{
		WebDriverWait w = new WebDriverWait(driver, 20);
		w.until(ExpectedConditions.elementToBeClickable(we));
		if(we.isEnabled() && we.isDisplayed())
		{
			we.click();
		}
		else
		{
			System.out.println("Fail " + we.getTagName() + " was not found");
		}
	}
	
	public static boolean FCUBSandFCISlogin(ProjectComponent projectComponent, String logfilename)
	{
		try{
			
			ProjectAccessDetails defaultProjectAccessDetails = projectComponent.getProjectAccessDetails();			
			if(defaultProjectAccessDetails.isSsoLogin())
			{
				return false;
			}
			else
			{
				OR = PageFactory.initElements(driver, xPathObjectRepository.class);				
				
				boolean login=false;
				
				if(driver.findElements(By.xpath("//*[@id='ifr_AlertWin']")) != null)
				{
					if(driver.findElement(By.xpath("//*[@id='ifr_AlertWin']")).isDisplayed())
					{
						driver.switchTo().frame("ifr_AlertWin");
						driver.findElement(By.xpath("//*[@id='BTN_OK']")).click();
						driver.switchTo().defaultContent();
					}
				}
				
				try {
					if(OR.Login_UserID.isDisplayed()) {	
						login = true;
					}
				} catch (Exception e) {
					System.out.println("----No login page----");
				}			
				
				if(!login)
				{
					System.out.println("---login page not displayed---------");
					
					/*driver.findElement(By.xpath("//*[@id='details-button']")).click();
					
					driver.findElement(By.xpath("//a[@id='proceed-link']")).click();
					
					Thread.sleep(3000);
					
					System.out.println("---done with override link---");	*/					
				}	
				
				ArrayList<FlexcubeUser> availableUsers = defaultProjectAccessDetails.getAvailableUserList();
				FlexcubeUser defaultUser = FlexcubeUser.getDefaultFlexcubeUser(availableUsers);
				
				if(projectComponent.getProjectDetails().getFlexcubeVersion().equals("12") ||
						projectComponent.getProjectDetails().getFlexcubeVersion().equals("14"))
				{
					enterText(OR.Login_UserID, defaultUser.getUsername());
					enterText(OR.Login_Password, defaultUser.getPassword());
					Thread.sleep(2000);
					
					clickButton(OR.Btn_SignIn);
	
					if(OR.txt_Fastpath.isDisplayed())
					{
						System.out.println("Login Successful");
						if(OR.frame_LoginSuccessful.isDisplayed())
						{
							driver.switchTo().frame(OR.frame_LoginSuccessful);
							clickButton(OR.btn_Ok_Popup_info);
							driver.switchTo().defaultContent();
						}
					}
					else
					{
						System.out.println("Login Failed");
						//  		-- check later for error messages
						return false;
					}
					
					return true;					
				}	
				else if(projectComponent.getProjectDetails().getFlexcubeVersion().equals("11"))
				{
					//  -- check later
					return false;
				}
				else if(projectComponent.getProjectDetails().getFlexcubeVersion().equals("10"))
				{
					// -- check later
					return false;
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Not a valid version number", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static void fcubsV12Learn(ProjectComponent projectComponent, String dbName, boolean learnSummaryScreen, String FunctionID, String learnLogFileName)
	{
		DatabaseDetails dbDetails = configDetails.getDbDetailsMap().get(dbName);
		
		if(backwardCompatibility == false)
		{
			NaviLearner.learn(driver, OR, sv,ATFolderPath, dbDetails.getDbServerName(), dbDetails.getDbPortNumber(), dbDetails.getDbSchemaID(),
				dbDetails.getDbUsername(), dbDetails.getDbPassword(), dbDetails.getBaseSchema(),
				projectComponent.getProjectArtifactFolderPaths().getRepositoryBaseFolderPath(),
				projectComponent.getProjectArtifactFolderPaths().getBareRepositoryBaseFolderPath(),
				projectComponent.getProjectDetails().getFlexcubeVersion(),
				projectComponent.getProjectDetails().getFlexcubeSubVersion(), projectComponent.getProjectDetails().getProduct(),
				FunctionID, preferredBrowser, learnLogFileName);
		}
		else if(backwardCompatibility == true)
		{
			BackwardCompatibility.learn(driver, OR, sv,ATFolderPath, dbDetails.getDbServerName(), dbDetails.getDbPortNumber(), dbDetails.getDbSchemaID(),
					dbDetails.getDbUsername(), dbDetails.getDbPassword(), dbDetails.getBaseSchema(),
					projectComponent.getProjectArtifactFolderPaths().getRepositoryBaseFolderPath(),
					projectComponent.getProjectArtifactFolderPaths().getBareRepositoryBaseFolderPath(),
					projectComponent.getProjectDetails().getFlexcubeVersion(),
					projectComponent.getProjectDetails().getFlexcubeSubVersion(), projectComponent.getProjectDetails().getProduct(),
					FunctionID, preferredBrowser, learnLogFileName);
		}
			
		
/*		-- check later
 * 
		if(learnSummaryScreen)
		{
			StringBuilder summaryFID = new StringBuilder(FunctionID);
			summaryFID.setCharAt(2, 'S');
			getSettings().set("FunctionID", summaryFID.toString());
			getScript("FC_NaviLearner").callFunction("run");
		}*/
	}
	
	public static void logout (ProjectComponent projectComponent, String msg, String logfilename)
	{
		try {
			if(branchChange)
			{
				Thread.sleep(2000);
				if(driver.findElement(By.id("Branch_Menu")).isEnabled())
				{
					driver.findElement(By.id("Branch_Menu")).click();
					Thread.sleep(1000);
					if(driver.findElement(By.id("home_branch")).isEnabled())
					{
						driver.findElement(By.id("home_branch")).click();
					}
				}
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("ifr_AlertWin");
				driver.findElement(By.xpath("//*[@title=\"Ok\" and @id=\"BTN_OK\"]")).click();
				driver.switchTo().defaultContent();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		dialogPanelogout(msg);
		
		if(projectComponent.getProjectDetails().getFlexcubeVersion().equals("12") ||
				projectComponent.getProjectDetails().getFlexcubeVersion().equals("14"))
		{
			try {
				
				/*if(preferredBrowser.equalsIgnoreCase("Chrome"))
				{
					List<WebElement> lstExitButton = driver.findElements(By.name("BTN_EXIT"));					
					if(lstExitButton.size() > 0)
					{
						WebElement exitButton = lstExitButton.get(0);
						exitButton.click();
					}					
					Thread.sleep(2000);
					driver.switchTo().frame(OR.frame_logout);
					if(OR.exitMessage.isDisplayed())
					{
						clickButton(OR.btn_Ok_Popup_info);
						driver.switchTo().parentFrame();
					}
					
					Thread.sleep(1000);
					
					lstExitButton = driver.findElements(By.name("BTN_EXIT"));
					if(lstExitButton.size() > 0)
					{
						WebElement exitButton = lstExitButton.get(0);
						exitButton.click();
						driver.switchTo().defaultContent();
					}*/
					Thread.sleep(2000);
					
					Actions act = new Actions(driver);
					WebElement logOutMenu = driver.findElement(By.xpath("//li[@class='user']"));
					act.moveToElement(logOutMenu,10,10);
					act.perform();
					Thread.sleep(1000);
					WebElement logOutButton = driver.findElement(By.xpath("//li[contains(text(),'Sign Off')]"));
					clickButton(logOutButton);
					Thread.sleep(1000);
					driver.switchTo().frame(OR.frame_LoginSuccessful);
					clickButton(OR.btn_Ok_Popup_info);
					Thread.sleep(1000);

					if(OR.Login_UserID.isDisplayed())
					{
						System.out.println("Logout successful");
					}
					else
					{
						System.out.println("Logout not successful");
					}
					driver.quit();
				//}

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void changebranch(ProjectComponent projectComponent, String Arguments, String logFile) throws Exception
	{
		System.out.println("Changing Branch...");
		WebDriverWait wait=new WebDriverWait(driver, 50);
		
		try
		{
			boolean found = false;
			
			if(driver.findElement(By.id("Branch_Menu")).isEnabled())
			{
				driver.findElement(By.id("Branch_Menu")).click();
				Thread.sleep(2000);
				if(driver.findElement(By.id("select_branch")).isEnabled())
				{
					found = true;
					driver.findElement(By.id("select_branch")).click();
				}
			}
				
			if(found)
			{
				try{
					
					driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
					driver.switchTo().frame("ifrSubScreen");
					driver.findElement(By.id("1")).sendKeys(Arguments);
					
					if(driver.findElement(By.xpath("//button[contains(text(),'Fetch')]")).isEnabled())
					{
						driver.findElement(By.xpath("//button[contains(text(),'Fetch')]")).click();
					}
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='"+Arguments+"']")));
					driver.findElement(By.xpath("//a[text()='"+Arguments+"']")).click();
					Thread.sleep(2000);
					driver.switchTo().defaultContent();
					driver.switchTo().frame("ifr_AlertWin");
					driver.findElement(By.xpath("//*[@title=\"Ok\" and @id=\"BTN_OK\"]")).click();
					driver.switchTo().defaultContent();
					
					branchChange = true;
					
				} catch(Exception e) {
					e.printStackTrace();
					fileRelatedFunctions.updateFile(logFile, "Failed to visit branch: "+Arguments+"\n");
					logout(projectComponent, "Exit", logFile);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fileRelatedFunctions.updateFile(logFile, "Could not find change branch link...contact OATS dev"+"\n");
			logout(projectComponent, "Exit", logFile);
		}
	}
	
	public static void generateSummary(String summaryFolderPath, boolean append)
	{
		String logFolderPath = summaryFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXEC_LOG_FOLDERNAME;
		folderRelatedFunctions.CreateDirectory(logFolderPath);
		
		String dateString = TimeRelatedFunctions.getCurrentTimeString();
		String logFileName = logFolderPath + "\\" + StandardFolderAndFileVal.OATSF_GENERATE_SUMMARY_LOG_FILENAME + " - " + dateString + ".txt";
		String userlogFileName = logFolderPath + "\\" + "generate_summary.xml";
		String defaultComponentName = configDetails.getDefaultProjectComponentName();
		String centralLogFileName = configDetails.getProjectComponentMap().get(defaultComponentName).getProjectArtifactFolderPaths().getProjectLogFolderPath() + "\\" + StandardFolderAndFileVal.OATSF_GENERATE_SUMMARY_LOG_FILENAME + " - " + dateString + ".txt";

		try {
			
			boolean success = SummaryGenerator.processFolderAndCreateSummaryExcel(summaryFolderPath, centralLogFileName, logFileName, configDetails, append, messages, userlogFileName);
			if(success)
			{
				JOptionPane.showMessageDialog(null, "Summary Generation completed", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Summary Generation failed. Please check logs", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
			}
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fileRelatedFunctions.updateFile(logFileName,"Exception in generateSummary() in FrameworkUI Script \n"+exceptionAsString+"\n");

			System.out.println("Exception in generateSummary() in FrameworkUI Script : " + e.getMessage());
		}
	}
	
	public static void generateXMLs(String summaryFolderPath, String activeProjectId)
	{
		String logFolderPath = summaryFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXEC_LOG_FOLDERNAME;
		folderRelatedFunctions.CreateDirectory(logFolderPath);
		
		String dateString = TimeRelatedFunctions.getCurrentTimeString();
		String logRefreshXML = logFolderPath + "\\" + StandardFolderAndFileVal.OATSF_REFRESH_XMLS_LOG_FILENAME + " - " + dateString + ".txt";
		String userLogfilePath = logFolderPath + "\\" + "refresh_xmls.xml";
		String defaultComponentName = configDetails.getDefaultProjectComponentName();
		String centralLogFileName = configDetails.getProjectComponentMap().get(defaultComponentName).getProjectArtifactFolderPaths().getProjectLogFolderPath() + "\\" + StandardFolderAndFileVal.OATSF_REFRESH_XMLS_LOG_FILENAME + " - " + dateString + ".txt";

		try{
			boolean success = DataXMLGenerator.processFolderUsingSummaryExcel(summaryFolderPath, centralLogFileName, logRefreshXML, configDetails, messages, userLogfilePath);
			
			if(success)
			{
				JOptionPane.showMessageDialog(null, "Refresh data has been completed successfully. ", "Information Message", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Refresh data has failed. ", "Error Message", JOptionPane.INFORMATION_MESSAGE);
			}
		}	
		catch (Exception e) { 
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fileRelatedFunctions.updateFile(logRefreshXML,"Exception in generateXMLs() in FrameworkUI Script"+exceptionAsString+"\n");
			System.out.println("Exception in generateXMLs() in FrameworkUI Script : " + e.getMessage());
		} 
	}
	
	public static void execute(String summaryFilePath, String fpsConfigFolderPath, String fpsProjectId, String scheduledJobFile, String jobScheduled, String userScheduledBy)
	{ 
		try 
		{
			//define the scenario where selected active project id does not match the id in summary
			//thoughts - parallel processing does not require this handling
			//replace the project id in summary sheet with the one in UI.
			//or introduce a summary update option. In this we can update project ID and usernames
			
			esv.setEnvironmentFPSServerConfigFolderPath(fpsConfigFolderPath);
			esv.setPreferencesFPSActiveProjetID(fpsProjectId);
			esv.setScheduledJobFile(scheduledJobFile);
			esv.setJobScheduled(jobScheduled);
			esv.setUserScheduledBy(userScheduledBy);
			
			String FullPathFolderAutomationDirectory =ATFolderPath+"\\";
			String FullPathFolderAdmin = FullPathFolderAutomationDirectory + StandardFolderAndFileVal.ENVIRONMENT_OATSF_ADMIN_FOLDERNAME + "\\";
			
			masterSummaryReturnObject mSRO = masterSummaryFromXML(summaryFilePath, FullPathFolderAutomationDirectory, FullPathFolderAdmin);
			
			if(mSRO==null)
			{
				return;
			}
			
			String logfilename = mSRO.getLog();
			ArrayList<String> summaryfilepathstorageArray = mSRO.getSummaryfilepathstorageArray();
			
			long Sumdifftime = 0;
			
			int numberOfSummarySheetsToProcess = summaryfilepathstorageArray.size();
			for(int i=0; i<numberOfSummarySheetsToProcess; i++)
			{
				long starttime=System.currentTimeMillis();
				String FilePath = summaryfilepathstorageArray.get(i);
				
				execute(FilePath, fpsConfigFolderPath, fpsProjectId, scheduledJobFile, jobScheduled, userScheduledBy);

				long endtime=System.currentTimeMillis();
				long difftime=endtime-starttime;
				String timeTaken = duration(difftime, logfilename);
				
				Sumdifftime = addTime(Sumdifftime, timeTaken);

				fileRelatedFunctions.updateFile(logfilename,"Updating Status, execution duration and date and time " + " \n");
				masterstatus(summaryFilePath, summaryFilePath, i+2, timeTaken, logfilename);
			}
			
			if(numberOfSummarySheetsToProcess>0)
			{
				fileRelatedFunctions.updateFile(logfilename,"Updating overall time"+ " \n");
				setoveralltime(summaryFilePath, mSRO.getLastRowNum(), convertToTimeFormat(Sumdifftime), logfilename);
			}
		} catch(Exception e){
			System.out.println("ERROR: in execute() in FrameworkUI : " + e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fileRelatedFunctions.updateFile(centralLogFilePath,"ERROR: in execute() in FrameworkUI"+exceptionAsString+"\n");
		}
	}
	
	public static class masterSummaryReturnObject{
		
		String log;
		int lastRowNum;
		ArrayList<String> summaryfilepathstorageArray;
		
		public String getLog() {
			return log;
		}
		
		public void setLog(String log) {
			this.log = log;
		}
		
		public int getLastRowNum() {
			return lastRowNum;
		}
		
		public void setLastRowNum(int lastRowNum) {
			this.lastRowNum = lastRowNum;
		}

		public ArrayList<String> getSummaryfilepathstorageArray() {
			return summaryfilepathstorageArray;
		}

		public void setSummaryfilepathstorageArray(ArrayList<String> summaryfilepathstorageArray) {
			this.summaryfilepathstorageArray = summaryfilepathstorageArray;
		}
	}
	
	public static masterSummaryReturnObject masterSummaryFromXML (String masterFilePath, String FullPathFolderAutomationDirectory, String FullPathFolderAdmin)
	{
		File sourceFile = new File(masterFilePath);

		ArrayList<String> summaryfilepathstorageArray = new ArrayList<String>();
		masterSummaryReturnObject mSRO = new masterSummaryReturnObject();

		if(sourceFile.exists())
		{
			String fileName = sourceFile.getName();
			String executionFolderPath = sourceFile.getAbsolutePath().replace(fileName, "");
			System.out.println("filename: "+fileName);
			System.out.println("executeFolderPath: "+executionFolderPath);
			String userLogfilepath = executionFolderPath + "\\LOGS\\Execute.xml" ;
			
			boolean proceed = true;
			
			if(fileName.toLowerCase().endsWith(".xls"))
			{
				proceed = false;
				System.out.println("calling convert");
				
				String dateString = TimeRelatedFunctions.getCurrentTimeString();
				proceed = SummaryExcelToXML.convert(configDetails, executionFolderPath, executionFolderPath+"\\LOGS\\Convert" + "-" + dateString + ".txt", messages, userLogfilepath);
				
				System.out.println("proceed: "+proceed);
				if(!proceed) {
					return null;
				}
			}
			
			String summaryXMLPath = BaseExecutionFilePaths.getSummaryXMLPath(executionFolderPath);
			
			String dateString = TimeRelatedFunctions.getCurrentTimeString();
			String logFolderPath = BaseExecutionFilePaths.getLogFolderPath(executionFolderPath);
			String logfilename = logFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXEC_PROCESS_SUMMARY_LOG_FILENAME + " - " + dateString + ".txt";
			folderRelatedFunctions.CreateDirectory(logFolderPath);
			
			mSRO.setLog(logfilename);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = docFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Document doc = null;
			try {
				doc = docBuilder.parse(summaryXMLPath);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(doc==null)
			{
				return null;
			}

			//String workbookName =  fileName;
			String summaryType = essentials.getTextContentOfChildNode(essentials.getChildElement(doc, StandardSummaryXMLVal.OATSF_SUMMARY_XML_INFO_TAG_NAME), StandardSummaryXMLVal.OATSF_SUMMARY_XML_INFO_SUMMARY_TYPE);
		//	doc.getElementsByTagName("");
			System.out.println("Summary type: "+summaryType);
			
			if(summaryType.equalsIgnoreCase("MASTERSUMMARY"))
			{
				boolean errorsExist = false;
				
				NodeList listOfNodes = essentials.getChildElement(doc, "SUMMARYLIST").getElementsByTagName("SUMMARY");
				int numberOfNodes = listOfNodes.getLength();
				
				for(int countRow=0; countRow<numberOfNodes; countRow++)
				{
					org.w3c.dom.Element summaryNode = (org.w3c.dom.Element) listOfNodes.item(countRow);
					String summaryFilePath = essentials.getTextContentOfChildNode(summaryNode, "SUMMARY_PATH");

					if(summaryFilePath.toLowerCase().endsWith(StandardFolderAndFileVal.OATSF_SUMMARY_NAME.toLowerCase() + ".xml"))
					{
						File indSummaryFile = new File(summaryFilePath);

						if(indSummaryFile.exists())
						{
							String summaryProjectID = getActiveProjectIdFromSummaryXML(summaryFilePath);
							if(!summaryProjectID.equals(configDetails.getProjectID()))
							{
								errorsExist = true;
								System.out.println("Selected project id and master summary project id are not matching. They should match: "+summaryFilePath);
								fileRelatedFunctions.updateFile(logfilename,"Selected project id and master summary project id are not matching. They should match: "+summaryFilePath+"\n");
							}
							else
							{
								summaryfilepathstorageArray.add(summaryFilePath);
							}
						}
						else
						{
							errorsExist = true;
							System.out.println("The mentioned file is not a summary file: "+summaryFilePath);
							fileRelatedFunctions.updateFile(logfilename,"The mentioned file is not a summary file: "+summaryFilePath+"\n");
						}
					}
					else
					{
						errorsExist = true;
						System.out.println("Summary does not exist: "+summaryFilePath);
						fileRelatedFunctions.updateFile(logfilename,"Summary does not exist: "+summaryFilePath+"\n");
					}
				}

				if(errorsExist)
				{
					summaryfilepathstorageArray.clear();
					JOptionPane.showMessageDialog(null, "Errors exist. Please verify logs.", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					mSRO.setSummaryfilepathstorageArray(summaryfilepathstorageArray);
					return mSRO;
				}
			}
			else
			{
				System.out.println("Looking for SUMMARY sheet");login
				fileRelatedFunctions.updateFile(logfilename,"Looking for SUMMARY sheet "+"\n");
				fileRelatedFunctions.updateFile(logfilename,"ERROR" +"\n");
				fileRelatedFunctions.updateFile(logfilename,"Execution might be based on SUMMARY sheet and not on MASTER SUMMARY"+"\n");
				System.out.println("calling executeIndividualSummary");
				executeIndividualSummary(executionFolderPath, summaryXMLPath, FullPathFolderAutomationDirectory, FullPathFolderAdmin);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please create summary/master summary before execution.", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
		}

		return null;
	}
	
	public static String getActiveProjectIdFromSummaryXML(String summaryPath)
	{
		String activeProjectId = "";
		
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(summaryPath);
			
			activeProjectId = essentials.getTextContentOfChildNode(essentials.getChildElement(doc, StandardSummaryXMLVal.OATSF_SUMMARY_XML_INFO_TAG_NAME), StandardSummaryXMLVal.OATSF_SUMMARY_XML_INFO_OATS_PROJECT_ID);
		}catch(Exception e)
		{

		}
		
		return activeProjectId;
	}
	
	public static void executeIndividualSummary(String executionFolderPath, String summaryFilePath, String FullPathFolderAutomationDirectory, String FullPathFolderAdmin)
	{
		System.out.println("folderToExecute: "+executionFolderPath);
		
		String logFolderPath = executionFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXEC_LOG_FOLDERNAME;
		folderRelatedFunctions.CreateDirectory(logFolderPath);
		String dateString = TimeRelatedFunctions.getCurrentTimeString();
		
		String logExeFileName = logFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXECUTE_LOG_FILENAME + " - " + dateString + ".txt";
		esv.setExecutionLogFilePath(logExeFileName);
		String userExecLogPath = logFolderPath + "\\" + "Execute.xml";
		esv.setUserExecutionLogPath(userExecLogPath);
		String relativeFPSResponseXMLSFolderPath = StandardFolderAndFileVal.FPS_RESPONSE_FOLDERNAME + "\\" + StandardFolderAndFileVal.FPS_RESPONSE_XMLS_FOLDERNAME_PREFIX + dateString;
		String fpsReponseXMLsFolderPath = executionFolderPath + "\\" + relativeFPSResponseXMLSFolderPath;
		String relativeScreenShotFolderPath = StandardFolderAndFileVal.OATSF_EXEC_SCREENSHOT_FOLDERNAME + "\\" + StandardFolderAndFileVal.OATSF_SCREENSHOT_SESSION_FOLDERNAME_PREFIX + dateString;
		
		setupExecutionFolder(executionFolderPath, fpsReponseXMLsFolderPath, relativeScreenShotFolderPath); 
		
		String ResultsFolderPath = executionFolderPath + "\\" + StandardFolderAndFileVal.OATSF_EXEC_RESULTS_FOLDERNAME;
		String handOffFilePath = executionFolderPath + "\\" + StandardFolderAndFileVal.OATSF_HANDOFF_FOLDER_NAME + "\\" + StandardFolderAndFileVal.OATSF_HANDOFF_FILE_NAME + ".xml";
		esv.setExecutionFolderToExecute(executionFolderPath);
		esv.setExecutionSummaryFilePath(summaryFilePath);
		esv.setExecutionResultsFolderPath(ResultsFolderPath);
		esv.setExecutionHandOffFilePath(handOffFilePath);
		esv.setExecutionSessionRelativeScreenshotFolderPath(relativeScreenShotFolderPath);
		esv.setExecutionSessionRelativeFPSResultXMLSFolderPath(relativeFPSResponseXMLSFolderPath);
		
		String easyDataAccessXMLPath = BaseExecutionFilePaths.getEasyDataAccessXMLFilePath(executionFolderPath);
		
		esv.setParam("NOTuivalidation");
		//Performing this check so that all latest features will apply to older packs
		if(new File(easyDataAccessXMLPath).exists()) {
			try{
				if(product.equalsIgnoreCase("OBBRN"))
				{
					try
					{
						IExecutor exec=new obbrn.execution.Executor();
						exec.executoinInit(esv);				
					}
					catch (Exception e) {
						System.out.println("Learning Failed");
					}
				}
				else
				{
					Executor exec=new Executor();
					exec.executoinInit(esv);
				}
/*				Executor2 executor2 = new Executor2();
				executor2.executoinInit(esv);*/
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("ERROR: in executeIndividualSummary() in FrameworkUI : " + e.getMessage());
				fileRelatedFunctions.updateFile(logExeFileName,"ERROR: in executeIndividualSummary() in FrameworkUI"+e.getMessage()+"\n");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please refresh Data before proceeding.", "Complete Message", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static void setupExecutionFolder(String folderToExecute, String fpsReponseXMLsFolderPath, String relativeScreenShotFolderPath)
	{
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_LOG_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_LOG_FOLDERNAME + "\\" + StandardFolderAndFileVal.OATSF_EXEC_FLEXCUBE_DEBUG_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_SCREENSHOT_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_QUERY_RESULTS_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_QUERY_RESULTS_COMPARISON_RESULTS_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.FPS_RESPONSE_FOLDERNAME);
		folderRelatedFunctions.CreateDirectory(fpsReponseXMLsFolderPath);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + relativeScreenShotFolderPath);
		folderRelatedFunctions.CreateDirectory(folderToExecute + "\\" + StandardFolderAndFileVal.OATSF_EXEC_EMS_FOLDERNAME);
	}
	
	private static String duration(long difftime, String logfilename) 
	{
		String time2 = "";
		try 
		{
			String time=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(difftime),
				TimeUnit.MILLISECONDS.toMinutes(difftime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difftime)),
				TimeUnit.MILLISECONDS.toSeconds(difftime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difftime)));

			DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm:ss");
			time2=TWENTY_FOUR_TF.format(TWENTY_FOUR_TF.parse(time));
		}
		catch (Exception e) {
			System.out.println(" Exception in duration method : "+e.getMessage());
			fileRelatedFunctions.updateFile(logfilename," Exception in duration method: "+e.getMessage()+"\n" );
		}
		return time2;
	}
	
	private static long addTime(long Sumdifftime, String time)
	{
		//for converting to seconds
		String[] tokens = time.split(":");
		int hours = Integer.parseInt(tokens[0]);
		int minutes = Integer.parseInt(tokens[1]);
		int seconds = Integer.parseInt(tokens[2]);
		int duration = 3600 * hours + 60 * minutes + seconds;

		Sumdifftime = Sumdifftime+duration;

		return Sumdifftime;
	}
	
	public static void masterstatus (String readpath, String writepath, int rowno, String timeTaken, String logfilename)
	{
		String content = "Incomplete";
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		Date dt=new Date();
		String dateandtime=sdf.format(dt);

		try{File sourceExcelFile = new File(readpath);

		if(sourceExcelFile.exists())
		{
			FileInputStream file = new FileInputStream(sourceExcelFile);
			Workbook workbook = new HSSFWorkbook(file); 
			Sheet sSheet = workbook.getSheet("SUMMARY");
			
			int numberOfRows = sSheet.getLastRowNum()+1;

			content = CommonOperations.getcellContent(numberOfRows-1,8,sSheet);
			System.out.println("Status fetched from summary " + content);
			fileRelatedFunctions.updateFile(logfilename,"Status fetched from summary  "+content + " \n");
			
			setstatus (writepath, rowno, content, timeTaken, dateandtime, logfilename);
		}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			fileRelatedFunctions.updateFile(logfilename,"Exception in masterstatus() in FrameworkUI Script"+exceptionAsString+"\n");
			System.out.println("Exception : " + e.getMessage());
			fileRelatedFunctions.updateFile(logfilename,"ERROR: Summary file not found - masterStatus()"+e.getMessage()+"\n");
		}
	}
	
	public static void setstatus (String writepath, int rowno, String content, String time, String dateandtime, String logfilename)
	{
		try{
			System.out.println("Write path is "+writepath);
			fileRelatedFunctions.updateFile(logfilename,"Write path is "+writepath+"\n");
			File destinationExcelFile = new File(writepath);
			if(destinationExcelFile.exists())
			{
				FileInputStream file2 = new FileInputStream(new File(writepath));
				HSSFWorkbook workbook2 = new HSSFWorkbook(file2);
				//CreationHelper createHelper = workbook2.getCreationHelper();
				HSSFSheet mastersheet = workbook2.getSheetAt(0);

				HSSFCellStyle style1 = workbook2.createCellStyle();
				style1.setFillForegroundColor(HSSFColor.RED.index);
				style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				style1.setAlignment(CellStyle.ALIGN_CENTER);
				style1=setStyle(style1);

				HSSFCellStyle style2 = workbook2.createCellStyle();
				style2.setFillForegroundColor(HSSFColor.GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				style2.setAlignment(CellStyle.ALIGN_CENTER);
				style2=setStyle(style2);

				Cell status_cell=null;
				Cell time_cell=null;
				Cell dateandtime_cell=null;

				status_cell = mastersheet.getRow(rowno).createCell(3);
				time_cell = mastersheet.getRow(rowno).createCell(4);
				dateandtime_cell = mastersheet.getRow(rowno).createCell(5);

				status_cell.setCellValue(content);
				time_cell.setCellValue(time);
				dateandtime_cell.setCellValue(dateandtime);


				if(content.equalsIgnoreCase("RED"))
				{
					//status_cell.setCellValue("FAIL");
					status_cell.setCellStyle(style1);
				}
				else if(content.equalsIgnoreCase("GREEN"))
				{
					//status_cell.setCellValue("SUCCESS");
					status_cell.setCellStyle(style2);
				}
				
				file2.close();
				FileOutputStream outFile =new FileOutputStream(new File(writepath));
				workbook2.write(outFile);
				outFile.close();

				fileRelatedFunctions.updateFile(logfilename,"Updated status as :" +content+ " \n");
				fileRelatedFunctions.updateFile(logfilename,"Updated duration" + " \n");
				fileRelatedFunctions.updateFile(logfilename,"Updated date and time"+ " \n");
			}
		}catch(Exception e){
			System.out.println("Exception : " + e.getMessage());
			fileRelatedFunctions.updateFile(logfilename,"ERROR: MASTER SUMMARY sheet not found in setstatus() in FrameworkUI"+e.getMessage()+"\n");
		}
	}
	
	public static HSSFCellStyle setStyle(HSSFCellStyle style)
	{
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
	
	private static String convertToTimeFormat(long Sumdifftime)
	{
		//for converting back to hh:mm:ss

		int hr = (int)(Sumdifftime/3600);
		int rem = (int)(Sumdifftime%3600);
		int mn = rem/60;
		int sec = rem%60;
		String hrStr = (hr<10 ? "0" : "")+hr;
		String mnStr = (mn<10 ? "0" : "")+mn;
		String secStr = (sec<10 ? "0" : "")+sec; 
		String overalltimeVal=(hrStr+ ":"+mnStr+ ":"+secStr+"");
		return overalltimeVal;
	}
	
	public static void setoveralltime(String filePath, int rowno, String overalltime, String logfilename)
	{
		try{
			File destinationExcelFile = new File(filePath);
			if(destinationExcelFile.exists())
			{
				FileInputStream file2 = new FileInputStream(new File(filePath));
				HSSFWorkbook workbook2 = new HSSFWorkbook(file2);
				HSSFSheet mastersheet = workbook2.getSheetAt(0);
				Cell overalltime1_cell = null;
				Cell overalltime2_cell = null;
				overalltime1_cell = mastersheet.createRow(rowno).createCell(3);
				overalltime2_cell = mastersheet.createRow(rowno).createCell(4);
				overalltime1_cell.setCellValue("Overall Time");
				overalltime2_cell.setCellValue(overalltime);
				file2.close();
				FileOutputStream outFile =new FileOutputStream(new File(filePath));
				workbook2.write(outFile);
				outFile.close();

				fileRelatedFunctions.updateFile(logfilename,"Updated overall time taken"+ " \n");
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception : "+e.getMessage());
			fileRelatedFunctions.updateFile(logfilename,"ERROR: in setoveralltime() in FrameworkUI"+e.getMessage()+"\n");
		}
	}
	
	private static void dialogPanelogout(String msg)
	{
		String message1 = "<html>"+msg+"<br/> Logging Off from Application</html>         ";
		JLabel label1 = new JLabel(message1);
		JPanel panel1=new JPanel();
		java.awt.Font myFont2 = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 17);
		label1.setFont(myFont2);
		ImageIcon gifImage;

		if(!adminConfigFolderPath.equals(StandardCommonVal.ADMIN_CONFIG_DOES_NOT_EXIST))
		{
			gifImage = new ImageIcon(ATRepository + "/ADMIN/BIN/RES/CircularProcessing.gif");
		}
		else
		{
			gifImage = new ImageIcon(ATFolderPath+"//ADMIN//BIN//RES//CircularProcessing.gif");
		}
		JLabel label3 = new JLabel(gifImage);

		label3.setOpaque(true);
		label3.setVisible(true);
		label3.setAlignmentX(Component.LEFT_ALIGNMENT);
		label3.setHorizontalAlignment(JLabel.LEFT);
		label3.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		label3.setVerticalAlignment(JLabel.BOTTOM);
		label3.setIcon(gifImage);
		panel1.add(label1);
		//panel1.add(label2);
		panel1.add(label3);


		final JDialog dialog1 = new JDialog();
		dialog1.add(panel1);
		dialog1.setVisible(true);
		dialog1.setSize(360,110);
		dialog1.setTitle("Selenium Automation Framework");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog1.setLocation((screenSize.width- screenSize.width/4),(screenSize.height - screenSize.height/7));
		dialog1.setAlwaysOnTop(true);


		label1.repaint();
		//label2.repaint();
		label3.repaint();
	}
}

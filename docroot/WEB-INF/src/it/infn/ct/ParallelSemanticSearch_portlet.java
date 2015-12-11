/**
 * ************************************************************************
 * Copyright (c) 2011: Istituto Nazionale di Fisica Nucleare (INFN), Italy
 * Consorzio COMETA (COMETA), Italy
 *
 * See http://www.infn.it and and http://www.consorzio-cometa.it for details on
 * the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(COMETA)
 * **************************************************************************
 */
package it.infn.ct;

// Import generic java libraries
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;

// Importing portlet libraries
import javax.portlet.*;

// Importing liferay libraries
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

// Importing Apache libraries
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;

// Importing GridEngine Job libraries 
//import it.infn.ct.GridEngine.Job.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

//
// This is the class that overrides the GenericPortlet class methods
// You can create your own portlet just customizing the code skeleton
// available below. It provides mainly a working example on:
//    1) How to manage combination of Actions/Views
//    2) How to manage portlet preferences and help
//    3) How to show information using the Log object
//    4) How to execute a distributed application with GridEngine
//
public class ParallelSemanticSearch_portlet extends GenericPortlet {

    // AppLogger class (No customizations needed)
    // Although developers can use System.out.println to watch their own console outputs
    // the use of Java logs is highly recommended.
    // Java Log object offers different output levels to show information:
    //    trace
    //    debug
    //    info
    //    warn
    //    error
    //    fatal
    // All of them accept a String as parameter containing the proper message to show.
    // AppLogger class uses  LogLevel eunerated type to express the log level verbosity
    // the setLogLevel method allows the portlet to print-out all logs types equal
    // or below the given log level accordingly to the priority:
    //       trace,debug,info,warn,erro,fatal
    private enum LogLevels {

        trace,
        debug,
        info,
        warn,
        error,
        fatal
    }
    // The AppLogger class wraps the apache.common Log object allowing the user to
    // enable/disable log accordingly to a given loglevel; the higher is the level 
    // more verbose will be the produced output

    private class AppLogger {
        // Values associated 

        private static final int TRACE_LEVEL = 6;
        private static final int DEBUG_LEVEL = 5;
        private static final int INFO_LEVEL = 4;
        private static final int WARN_LEVEL = 3;
        private static final int ERROR_LEVEL = 2;
        private static final int FATAL_LEVEL = 1;
        private static final int UNKNOWN_LEVEL = 0;
        private Log _log;
        private int logLevel = AppLogger.INFO_LEVEL;

        public void setLogLevel(String level) {
            switch (LogLevels.valueOf(level)) {
                case trace:
                    logLevel = AppLogger.TRACE_LEVEL;
                    break;
                case debug:
                    logLevel = AppLogger.DEBUG_LEVEL;
                    break;
                case info:
                    logLevel = AppLogger.INFO_LEVEL;
                    break;
                case warn:
                    logLevel = AppLogger.WARN_LEVEL;
                    break;
                case error:
                    logLevel = AppLogger.ERROR_LEVEL;
                    break;
                case fatal:
                    logLevel = AppLogger.FATAL_LEVEL;
                    break;
                default:
                    logLevel = AppLogger.UNKNOWN_LEVEL;
                    break;
            }
        }

        public AppLogger(Class cname) {
            _log = LogFactory.getLog(cname);
        }

        public void trace(String s) {
            if (_log.isTraceEnabled()
              && logLevel >= AppLogger.TRACE_LEVEL) _log.trace(s);            
        }

        public void debug(String s) {
            if (_log.isDebugEnabled()
              && logLevel >= AppLogger.DEBUG_LEVEL) _log.trace(s);            
        }

        public void info(String s) {
            if (_log.isInfoEnabled()
              && logLevel >= AppLogger.INFO_LEVEL) _log.info(s);            
        }

        public void warn(String s) {
            if (_log.isWarnEnabled()
              && logLevel >= AppLogger.WARN_LEVEL) _log.warn(s);            
        }

        public void error(String s) {
            if (_log.isErrorEnabled()
              && logLevel >= AppLogger.ERROR_LEVEL) _log.error(s);            
        }

        public void fatal(String s) {
            if (_log.isFatalEnabled()
              && logLevel >= AppLogger.FATAL_LEVEL) _log.fatal(s);            
        }
    } // AppLogger
    
    // Instantiate the logger object
    public AppLogger _log = new AppLogger(ParallelSemanticSearch_portlet.class);

    // This portlet uses Aciont/Views enumerations in order to 
    // manage the different portlet modes and the corresponding 
    // view to display
    // You may override the current values with your own business
    // logic best identifiers and manage them through: jsp and java code
    // The jsp parameter PortletStatus will be the responsible of
    // portlet mode switching. This parameter will be read by
    // the processAction method who will select the proper view mode
    // registering again into 'PortletStatus' renderResponse parameter
    // the next view mode.
    // The default prortlet mode by default is: ACTION_INPUT (see ProcessAction)
    private enum Actions {

        ACTION_INPUT // Called before to show the INPUT view
        //,ACTION_SUBMIT     // Called after the user press the submit button   
        , ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE, ACTION_GET_MORE_INFO,
        ACTION_GET_MORE_INFO_OPENAGRIS, ACTION_GET_MORE_INFO_CULTURAITALIA, ACTION_GET_MORE_INFO_EUROPEANA,
        ACTION_GET_MORE_INFO_ISIDORE, ACTION_GET_MORE_INFO_PUBMED, ACTION_GET_MORE_INFO_ENGAGE, 
        ACTION_GET_MORE_INFO_DBPEDIA,ACTION_GET_CITATIONS_GSCHOLAR,ACTION_ISLEX,ACTION_GET_ALTMETRICS
    }

    private enum Views {

        VIEW_INPUT // View containing application input fields
        // ,VIEW_SUBMIT     // View reporting the job submission     
        , VIEW_SEMANTIC_SEARCH_ALL_LANGUAGE, VIEW_GET_MORE_INFO,
        VIEW_GET_MORE_INFO_OPENAGRIS, VIEW_GET_MORE_INFO_CULTURAITALIA, VIEW_GET_MORE_INFO_EUROPEANA,
        VIEW_GET_MORE_INFO_ISIDORE, VIEW_GET_MORE_INFO_PUBMED,VIEW_GET_MORE_INFO_DBPEDIA,
        VIEW_GET_MORE_INFO_ENGAGE, VIEW_CITATIONS_GSCHOLAR,VIEW_ISLEX,VIEW_ALTMETRICS
    }

    // The init values will be read form portlet.xml from <init-param> xml tag
    // This tag will be useful to setup defaults values for your own portlet
    class App_Init {

        String default_OpenAgris;
        String default_OpenAgrisEndPoint;
        String default_Europeana;
        String default_EuropeanaEndPoint;
        String default_CulturaItalia;
        String default_CulturaItaliaEndPoint;
        String default_Isidore;
        String default_IsidoreEndPoint;
        String default_Pubmed;
        String default_PubmedEndPoint;
        String default_Engage;
        String default_EngageEndPoint;
        String default_NumberRecordsForPage;
        String default_LodLive;
        String default_LodLiveEndPoint;
        String default_TimeOut;
        String default_DBPedia;
        String default_DBPediaEndPoint;

        public App_Init() {
            default_OpenAgris = "";
            default_OpenAgrisEndPoint = "";
            default_Europeana = "";
            default_EuropeanaEndPoint = "";
            default_CulturaItalia = "";
            default_Isidore = "";
            default_CulturaItaliaEndPoint = "";
            default_Pubmed = "";
            default_PubmedEndPoint = "";
            default_Engage = "";
            default_EngageEndPoint = "";
            default_NumberRecordsForPage = "";
            default_LodLive = "";
            default_LodLiveEndPoint = "";
            default_TimeOut = "";
            default_DBPedia = "";
            default_DBPediaEndPoint = "";

        }
    } // App_Init
    // Instanciate the App_Init object
    public App_Init appInit = new App_Init();

    // This object is used to store the values of portlet preferences
    // The init method will initialize their values with corresponding init_*
    // variables when the portlet first starts (see init_Preferences var).
    // Please notice that not all init_* variables have a corresponding pref_* value
    class App_Preferences {

        String OpenAgris;
        String OpenAgrisEndPoint;
        String Europeana;
        String EuropeanaEndPoint;
        String CulturaItalia;
        String CulturaItaliaEndPoint;
        String Isidore;
        String IsidoreEndPoint;
        String Pubmed;
        String PubmedEndPoint;
        String Engage;
        String EngageEndPoint;
        String NumberRecordsForPage;
        String LodLive;
        String LodLiveEndPoint;
        String TimeOut;
        String DBPedia;
        String DBPediaEndPoint;

        public App_Preferences() {
            OpenAgris = OpenAgrisEndPoint = Europeana = EuropeanaEndPoint = 
            CulturaItalia = CulturaItaliaEndPoint = Isidore = IsidoreEndPoint =
            Pubmed = PubmedEndPoint = Engage = EngageEndPoint = NumberRecordsForPage = 
            LodLive = LodLiveEndPoint = TimeOut = DBPedia = DBPediaEndPoint="";
        }
    } // App_Preferences
    // Instanciate the App_Preferences object
    public App_Preferences appPreferences = new App_Preferences();

    //
    // Application input values
    //
    // Job submission values are collected inside a single object
    class App_Input {

        String search_word;
        String numRecordsForPage;
        String LodLiveEndPoint;
        String TimeOut;
        String selected_language;
        String jobIdentifier;
        String nameSubject;
        //CHAIN
        String idResouce;
        String numberPage;
        String moreResourceCHAIN;
        String moreInfo;
        String numResource;
        String numResourceFromDetails;
        //OPENAGRIS
        String idResourceOpenAgris;
        String numberPageOpenAgris;
        String moreResourceOpenAgris;
        String moreInfoOpenAgris;
        String numResourceOpenAgris;
        String numResourceOpenAgrisFromDetails;
        //CULTURA ITALIA
        String idResourceCulturaItalia;
        String numberPageCulturaItalia;
        String moreResourceCulturaItalia;
        String moreInfoCulturaItalia;
        String numResourceCulturaItalia;
        String numResourceCulturaItaliaFromDetails;
        //EUROPEANA
        String idResourceEuropeana;
        String numberPageEuropeana;
        String moreResourceEuropeana;
        String moreInfoEuropeana;
        String numResourceEuropeana;
        String numResourceEuropeanaFromDetails;
        //ISIDORE
        String idResourceIsidore;
        String numberPageIsidore;
        String moreResourceIsidore;
        String moreInfoIsidore;
        String numResourceIsidore;
        String numResourceIsidoreFromDetails;
        //PUBMED
        String idResourcePubmed;
        String numberPagePubmed;
        String moreResourcePubmed;
        String moreInfoPubmed;
        String numResourcePubmed;
        String numResourcePubmedFromDetails;
        //ENAGGE
        String idResourceEngage;
        String numberPageEngage;
        String moreResourceEngage;
        String moreInfoEngage;
        String numResourceEngage;
        String numResourceEngageFromDetails;
          //DBPEDIA
        String idResourceDBPedia;
        String numberPageDBPedia;
        String moreResourceDBPedia;
        String moreInfoDBPedia;
        String numResourceDBPedia;
        String numResourceDBPediaFromDetails;
        
        
        
        
        String title_GS;
        
        //ALTMETRIC
        String chek_altmetric;
        String url_altmetric;
        String doi_altmetric;
        
        // Some user level information
        // must be stored as well
        String username;
        String timestamp;

        public App_Input() {
            search_word = selected_language = nameSubject = idResouce = numberPage = numRecordsForPage = jobIdentifier = username = timestamp = "";
            LodLiveEndPoint = TimeOut = "";
            
            numberPageOpenAgris = "";
            moreResourceCHAIN = "";
            moreResourceOpenAgris = "";
            idResourceOpenAgris = "";
            moreInfoOpenAgris = "";
            moreInfo = "";
            numberPageCulturaItalia = "";
            moreResourceCulturaItalia = "";
            numResource = "";
            numResourceFromDetails = "";
            numResourceOpenAgris = "0";
            moreInfoCulturaItalia = "";
            numResourceOpenAgrisFromDetails = "";
            numResourceCulturaItalia = "";
            numResourceCulturaItaliaFromDetails = "";

            idResourceCulturaItalia = "";
            idResourceEuropeana = "";
            numberPageEuropeana = "";
            moreResourceEuropeana = "";
            moreInfoEuropeana = "";
            numResourceEuropeana = "";
            numResourceEuropeanaFromDetails = "";

            idResourceIsidore = "";
            numberPageIsidore = "";
            moreResourceIsidore = "";
            moreInfoIsidore = "";
            numResourceIsidore = "";
            numResourceIsidoreFromDetails = "";

            idResourcePubmed = "";
            numberPagePubmed = "";
            moreResourcePubmed = "";
            moreInfoPubmed = "";
            numResourcePubmed = "";
            numResourcePubmedFromDetails = "";

            idResourceEngage = "";
            numberPageEngage = "";
            moreResourceEngage = "";
            moreInfoEngage = "";
            numResourceEngage = "";
            numResourceEngageFromDetails = "";
            
            idResourceDBPedia="";
            numberPageDBPedia="";
            moreResourceDBPedia="";
            moreInfoDBPedia="";
            numResourceDBPedia="";
            numResourceDBPediaFromDetails="";

            title_GS = "";
            chek_altmetric="";
            url_altmetric="";
            doi_altmetric="";

            // numberPage=0;
        }
    } // App_Input
    // public String searched_word;
    // public String searched_subject;
    public String language;
    // public String selected_page;
    public String selected_graph;
    //public int numRecords;
    public int numTotRecords;
    public ArrayList arrayLanguageSubject;
    public ArrayList arrayCodesLanguage = new ArrayList();
    boolean firstAction = true;
    //VARIABILI GLOBALI CHAIN
//    ArrayList virtuosoResourceList;
//    ArrayList chainTitleList;
//    ArrayList chainIdentifierList;
//    ArrayList chainAuthorList;
//    ArrayList chainDescriptionList;
//    ArrayList chainRepositoryList;
    String[] sArray;
    String[] sArrayChainTitle;
    String[] sArrayChainIdentifier;
    String[] sArrayChainAuthor;
    String[] sArrayChainDescription;
    String[] sArrayChainRepository;
    //VARIABILI GLOBALI OPENAGRIS
    //ArrayList openAgrisResourceList;
//    ArrayList openAgrisTitleList;
//    ArrayList openAgrisAuthorList;
//    ArrayList openAgrisDescriptionList;
    String[] sArrayOpenAgris;
    String[] sArrayOpenAgrisTitle;
    String[] sArrayOpenAgrisAuthor;
    String[] sArrayOpenAgrisDescription;
    //VARIABILI GLOBALI CULTURA ITALIA
//    ArrayList culturaItaliaResourceList;
//    ArrayList culturaItaliaTitleList;
//    ArrayList culturaItaliaTypeList;
//    ArrayList culturaItaliaAuthorList;
//    ArrayList culturaItaliaDescriptionList;
    String[] sArrayCulturaItalia;
    String[] sArrayCulturaItaliaTitle;
    String[] sArrayCulturaItaliaType;
    String[] sArrayCulturaItaliaAuthor;
    String[] sArrayCulturaItaliaDescription;
    //VARIABILI GLOBALI EUROPEANA
//    ArrayList europeanaResourceList;
//    ArrayList europeanaTitleList;
//    ArrayList europeanaAuthorList;
//    ArrayList europeanaDescriptionList;
//    ArrayList europeanaIdentifierList;
//    ArrayList europeanaTypeList;
    String[] sArrayEuropeana;
    String[] sArrayEuropeanaTitle;
    String[] sArrayEuropeanaAuthor;
    String[] sArrayEuropeanaDescription;
    String[] sArrayEuropeanaIdentifier;
    String[] sArrayEuropeanaType;
    //VARIABILI GLOBALI ISIDORE
//    ArrayList isidoreResourceList;
//    ArrayList isidoreTitleList;
//    ArrayList isidoreAuthorList;
//    ArrayList isidoreDescriptionList;
//    ArrayList isidoreIdentifierList;
    String[] sArrayIsidore;
    String[] sArrayIsidoreTitle;
    String[] sArrayIsidoreAuthor;
    String[] sArrayIsidoreDescription;
    String[] sArrayIsidoreIdentifier;
    //VARIABILI GLOBALI ISIDORE
//    ArrayList pubmedResourceList;
//    ArrayList pubmedTitleList;
//    ArrayList pubmedAuthorList;
//    ArrayList pubmedDescriptionList;
//    ArrayList pubmedIdentifierList;
    String[] sArrayPubmed;
    String[] sArrayPubmedTitle;
    String[] sArrayPubmedAuthor;
    String[] sArrayPubmedDescription;
    String[] sArrayPubmedURL;
    String [] sArrayPubmedURI;
    //VARIABILI GLOBALI ENGAGE
    String[] sArrayEngage;
    String[] sArrayEngageTitle;
    String[] sArrayEngageAuthor;
    String[] sArrayEngageDescription;
    String[] sArrayEngageHomepage;
    
    //VARIABILI GLOBALI DBPEDIA
    String[] sArrayDBPedia;
    String[] sArrayDBPediaTitle;
    String[] sArrayDBPediaDescription;
   
    
    //VARIABILI GLOBALI PER GESTIRE IL BACK DA MORE INFO
    //String search_word;
    String selected_page = "";
    String selected_pageOpenAgris = "";
    String selected_pageCulturaItalia = "";
    String selected_pageEuropeana = "";
    String selected_pageIsidore = "";
    String selected_pagePubmed = "";
    String selected_pageEngage = "";
    String selected_pageDBPedia = "";
    String moreResourceCHAIN;
    String moreResourceCulturaItalia = "";
    String moreResourceOpenAgris = "";
    String moreResourceEuropeana = "";
    String moreResourceIsidore = "";
    String moreResourcePubmed = "";
    String moreResourceEngage = "";
    String moreResourceDBPedia = "";
    // Liferay user data
    // Classes below are used by this portlet code to get information
    // about the current user
    public ThemeDisplay themeDisplay;  // Liferay' ThemeDisplay variable
    public User user;                  // From ThemeDisplay get User data
    public String username;            // From User data the username        
    // Liferay portlet data        
    PortletSession portletSession;  // PorteltSession
    PortletContext portletContext;  // PortletContext
    public String appServerPath;   // This variable stores the absolute path of the Web applications
    public String p_session;
    // Other misc valuse
    // (!) Pay attention that altough the use of the LS variable
    //     the replaceAll("\n","") has to be used 
    public String LS = System.getProperty("line.separator");
    // Users must have separated inputSandbox files
    // these file will be generated into /tmp directory
    // and prefixed with the format <timestamp>_<user>_*
    // The timestamp format is:
    public static final String tsFormat = "yyyyMMddHHmmss";
    @Resource(name = "SemanticSearch-Pool") //("concurrency/TP");SemanticSearch-Pool
    private ThreadPoolExecutor tp;
    
    protected Boolean init_Preferences=true;
    
    
    public static int PID;
    //
    // Portlet Methods
    // 
    //
    // init
    //
    // The init method will be called when installing the portlet for the first time 
    // This is the right time to get default values from WEBINF/portlet.xml file
    // Those values will be assigned into parameters the first time the processAction
    // will be called thanks to the appPreferences object
    //

    @Override
    public void init()
            throws PortletException {
        // Load default values from portlet.xml              

        appInit.default_OpenAgris = "" + getInitParameter("OpenAgris");
        appInit.default_OpenAgrisEndPoint = "" + getInitParameter("OpenAgrisEndPoint");
        appInit.default_Europeana = "" + getInitParameter("Europeana");
        appInit.default_EuropeanaEndPoint = "" + getInitParameter("EuropeanaEndPoint");
        appInit.default_CulturaItalia = "" + getInitParameter("CulturaItalia");
        appInit.default_CulturaItaliaEndPoint = "" + getInitParameter("CulturaItaliaEndPoint");
        appInit.default_Isidore = "" + getInitParameter("Isidore");
        appInit.default_IsidoreEndPoint = "" + getInitParameter("IsidoreEndPoint");
        appInit.default_Pubmed = "" + getInitParameter("Pubmed");
        appInit.default_PubmedEndPoint = "" + getInitParameter("PubmedEndPoint");
        appInit.default_Engage = "" + getInitParameter("Engage");
        appInit.default_EngageEndPoint = "" + getInitParameter("EngageEndPoint");
        appInit.default_DBPedia = "" + getInitParameter("DBPedia");
        appInit.default_DBPediaEndPoint = "" + getInitParameter("DBPediaEndPoint");
        appInit.default_NumberRecordsForPage = "" + getInitParameter("NumberRecordsForPage");
        appInit.default_LodLive = "" + getInitParameter("LodLive");
        appInit.default_LodLiveEndPoint = "" + getInitParameter("LodLiveEndPoint");
        appInit.default_TimeOut = "" + getInitParameter("TimeOut");
        
        
        // WARNING: Although the pilot script field is considered here it is not
        // Possible to specify a bash script code inside thie init_pilotScript
        // xml field. The content of pilot script must be inserted manually upon
        // the portlet installation through its configuration pane.        

        // Show loaded values into log
        _log.info(
                LS + "Loading default values "
                + LS + "-----------------------"
                + LS + " OpenAgris DEFAULT: " + appInit.default_OpenAgris
                + LS + " OpenAgrisEndPoint DEFAULT: " + appInit.default_OpenAgrisEndPoint
                + LS + " Europeana DEFAULT: " + appInit.default_Europeana
                + LS + " EuropeanaEndPoint DEFAULT: " + appInit.default_EuropeanaEndPoint
                + LS + " Cultura Italia DEFAULT: " + appInit.default_CulturaItalia
                + LS + " Cultura ItaliaEndPoint DEFAULT: " + appInit.default_CulturaItaliaEndPoint
                + LS + " Isidore DEFAULT: " + appInit.default_Isidore
                + LS + " IsidoreEndPoint DEFAULT: " + appInit.default_IsidoreEndPoint
                + LS + " Pubmed DEFAULT: " + appInit.default_Pubmed
                + LS + " PubmedEndPoint DEFAULT: " + appInit.default_PubmedEndPoint
                + LS + " Engage DEFAULT: " + appInit.default_Engage
                + LS + " EngageEndPoint DEFAULT: " + appInit.default_EngageEndPoint
                + LS + " DBPedia DEFAULT: " + appInit.default_DBPedia
                + LS + " DBPediaEndPoint DEFAULT: " + appInit.default_DBPediaEndPoint
                + LS + " NumberRecordsForPage DEFAULT: " + appInit.default_NumberRecordsForPage
                + LS + " LodLive DEFAULT: " + appInit.default_LodLive
                + LS + " LodLiveEndPoint DEFAULT: " + appInit.default_LodLiveEndPoint
                + LS + " TimeOut (in minutes) DEFAULT: " + appInit.default_TimeOut
                + LS);
    } // init

    //
    // processAction
    //
    // This method allows the portlet to process an action request; this method is normally
    // called upon each user interaction (a submit button inside a jsp' <form statement)
    //
    @Override
    public void processAction(ActionRequest request, ActionResponse response)
            throws PortletException, IOException {
        _log.info("calling processAction ...");

        // Determine the username
        themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        user = themeDisplay.getUser();
        username = user.getScreenName();
        _log.info("User: '" + user + "'");

        // int numRecords = 0;
        //   String[] sArray=null;

        // Determine the application pathname                
        portletSession = request.getPortletSession();
        portletContext = portletSession.getPortletContext();
        appServerPath = portletContext.getRealPath("/");
        _log.info("Web Application path: '" + appServerPath + "'");

        p_session = portletSession.getId();
        _log.info("portlet session: '" + p_session + "'");

        // Determine the current portlet mode and forward this state to the response
        // Accordingly to JSRs168/286 the standard portlet modes are:
        // VIEW, EDIT, HELP
        PortletMode mode = request.getPortletMode();
        response.setPortletMode(request.getPortletMode());

        // Switch among different portlet modes: VIEW, EDIT, HELP
        // Custom modes are not covered by this template
        if (mode.equals(PortletMode.VIEW)) {

            // The VIEW mode is the normal portlet mode where normal portlet
            // content will be shown to the user
            _log.info("Portlet mode: VIEW");

            // The actionStatus value will be taken from the calling jsp file
            // through the 'PortletStatus' parameter; the corresponding
            // VIEW mode will be stored registering the portlet status
            // as render parameter. See the call to setRenderParameter
            // If the actionStatus parameter is null or empty the default
            // action will be the ACTION_INPUT (input form)
            // This happens the first time the portlet is shown
            // The PortletStatus variable is managed by jsp and this java code
            String actionStatus = request.getParameter("PortletStatus");
            // Assigns the default ACTION
            if (null == actionStatus
                    || actionStatus.equals("")) {
                actionStatus = "" + Actions.ACTION_INPUT;
            }

            // Different actions will be performed accordingly to the
            // different possible statuses
            switch (Actions.valueOf(actionStatus)) {
                case ACTION_INPUT:
                    _log.info("Got action: 'ACTION_INPUT'");

                    // Create the appInput object
                    App_Input appInput = new App_Input();

                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_INPUT);
                    break;

                case ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE:
                    _log.info("Got action: 'ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE'");

                    // Get current preference values
                    // getPreferences(request, null);
                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;
                    //SemanticQuery.stopQuery=false;
                    // Determine the submissionTimeStamp                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat(tsFormat);
                    String timestamp = dateFormat.format(Calendar.getInstance().getTime());
                    appInput.timestamp = timestamp;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);
                    setNotNullInputParameter(appInput);

                    PortletPreferences portletPreferences = request.getPreferences();

                    int numberRecordsForAll = Integer.parseInt(portletPreferences.getValue("NumberRecordsForPage",""));
                    int TimeOut = Integer.parseInt(portletPreferences.getValue("TimeOut","1"));
                    // int numberRecordsForAll = Integer.parseInt(appPreferences.NumberRecordsForPage);                    
                    doGet(request, response, appInput, numberRecordsForAll, portletPreferences, TimeOut);
                    //------------ CHAIN KB ----------------------



                    //System.out.println("MOREINFOPROCESSCHAIN--->" + appInput.moreInfo);

//                        handlerTabCHAIN(request, response, appInput, numberRecordsForAll);
//
//
//
//                        //------------ OPEN AGRIS ----------------------
//
//
//                        if (portletPreferences.getValue("OpenAgris", appPreferences.OpenAgris).equals("true")) {
//                            //if (appPreferences.OpenAgris.equals("true")) {
//                            handlerTabOpenAgris(request, response, appInput, numberRecordsForAll);
//                        }




                    //------------CULTURA ITALIA------------------
                    // if (appPreferences.CulturaItalia.equals("true")) {
//                        if (portletPreferences.getValue("CulturaItalia", appPreferences.CulturaItalia).equals("true")) {
//                            handlerTabCulturaItalia(request, response, appInput, numberRecordsForAll);
//                        }

                    //------------EUROPEANA------------------
                    //if (appPreferences.Europeana.equals("true")) {
//                        if (portletPreferences.getValue("Europeana", appPreferences.Europeana).equals("true")) {
//                            handlerTabEuropeana(request, response, appInput, numberRecordsForAll);
//                        }
//
//                        //------------ISIDORE------------------
//                        // if (appPreferences.Isidore.equals("true")) {
//                        if (portletPreferences.getValue("Isidore", appPreferences.Isidore).equals("true")) {
//                            handlerTabIsidore(request, response, appInput, numberRecordsForAll);
//                        }
//
//
//
//
//                        //------------PUBMED------------------
//                        // if (appPreferences.Isidore.equals("true")) {
//                        if (portletPreferences.getValue("Pubmed", appPreferences.Pubmed).equals("true")) {
//                            handlerTabPubmed(request, response, appInput, numberRecordsForAll);
//                        }
//
//                        //------------ENGAGE------------------
//                        // if (appPreferences.Isidore.equals("true")) {
//                        if (portletPreferences.getValue("Engage", appPreferences.Engage).equals("true")) {
//                            handlerTabEngage(request, response, appInput, numberRecordsForAll);
//                        }
//

                    //appInput.moreInfo = "NO";
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_SEMANTIC_SEARCH_ALL_LANGUAGE);
                    break;

//                  
                case ACTION_GET_MORE_INFO:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    portletPreferences = request.getPreferences();

                    response.setRenderParameter("title_GS", appInput.title_GS);
                    _log.info("Got action: 'ACTION_GET_MORE_INFO TITLEEEEEEEEEEEEE'" + appInput.title_GS);

                    String[] info_GS = executeCommand(appInput.title_GS);
                    response.setRenderParameter("info_GS", info_GS);
                    response.setRenderParameter("idResource", appInput.idResouce);
                    response.setRenderParameter("search_word", appInput.search_word);
                    //System.out.println("NUMRESOURCECHAIN-->" + appInput.numResource);
                    response.setRenderParameter("numResource", appInput.numResource);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO);

                    break;

                case ACTION_GET_MORE_INFO_OPENAGRIS:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_OPENAGRIS'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEOPENAGRIS: " + appInput.idResourceOpenAgris);
                    response.setRenderParameter("idResourceOpenAgris", appInput.idResourceOpenAgris);
                    response.setRenderParameter("numResourceOpenAgris", appInput.numResourceOpenAgris);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_OPENAGRIS);

                    break;

                case ACTION_GET_MORE_INFO_CULTURAITALIA:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_CULTURAITALIA'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    // System.out.println("ID-RESOURCEOPENAGRIS: " + appInput.);
                    response.setRenderParameter("idResourceCulturaItalia", appInput.idResourceCulturaItalia);
                    response.setRenderParameter("numResourceCulturaItalia", appInput.numResourceCulturaItalia);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_CULTURAITALIA);

                    break;

                case ACTION_GET_MORE_INFO_EUROPEANA:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_EUROPEANA'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEEUROPEANA: " + appInput.idResourceEuropeana);
                    response.setRenderParameter("idResourceEuropeana", appInput.idResourceEuropeana);
                    response.setRenderParameter("numResourceEuropeana", appInput.numResourceEuropeana);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_EUROPEANA);

                    break;

                case ACTION_GET_MORE_INFO_ISIDORE:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_ISIDORE'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEISIDORE: " + appInput.idResourceIsidore);
                    response.setRenderParameter("idResourceIsidore", appInput.idResourceIsidore);
                    response.setRenderParameter("numResourceIsidore", appInput.numResourceIsidore);
                    response.setRenderParameter("search_word", appInput.search_word);
                    
                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_ISIDORE);
                    break;
                    
                case ACTION_GET_MORE_INFO_PUBMED:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_PUBMED'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEISIDORE: " + appInput.idResourceIsidore);
                    response.setRenderParameter("idResourcePubmed", appInput.idResourcePubmed);
                    response.setRenderParameter("numResourcePubmed", appInput.numResourcePubmed);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_PUBMED);
                    break;
                    
                    
               case ACTION_GET_MORE_INFO_DBPEDIA:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_DBPEDIA'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEEUROPEANA: " + appInput.idResourceEuropeana);
                    response.setRenderParameter("idResourceDBPedia", appInput.idResourceDBPedia);
                    response.setRenderParameter("numResourceDBPedia", appInput.numResourceDBPedia);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_DBPEDIA);

                    break;     
                    

                case ACTION_GET_MORE_INFO_ENGAGE:
                    _log.info("Got action: 'ACTION_GET_MORE_INFO_ENGAGE'");

                    // Get current preference values
                    //getPreferences(request, null);

                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    //System.out.println("ID-RESOURCEISIDORE: " + appInput.idResourceIsidore);
                    response.setRenderParameter("idResourceEngage", appInput.idResourceEngage);
                    response.setRenderParameter("numResourceEngage", appInput.numResourceEngage);
                    response.setRenderParameter("search_word", appInput.search_word);

                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_GET_MORE_INFO_ENGAGE);
                    break;

                case ACTION_GET_CITATIONS_GSCHOLAR:
                    // Get current preference values
                    _log.info("Got action: 'ACTION_CITATIONS_GS'");
                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);

                    // response.setRenderParameter("title_GS", appInput.title_GS);
                    response.setRenderParameter("title_GS", appInput.title_GS);
                    response.setRenderParameter("chek_altmetric", appInput.chek_altmetric);
                    
                   // System.out.println("appInput.chek_altmetric---->"+appInput.chek_altmetric);
                    if(!appInput.chek_altmetric.equals("SI")){
                    info_GS = executeCommand(appInput.title_GS);

                    response.setRenderParameter("info_GS", info_GS);
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_CITATIONS_GSCHOLAR);
                    }
                    else
                    {
                         _log.info("Got action: 'ACTION_ALTMETRICS'");
                          System.out.println("appInput.chek_altmetric---->"+appInput.url_altmetric);
                          System.out.println("appInput.doi_altmetric---->"+appInput.doi_altmetric);
                         response.setRenderParameter("url_altmetric", appInput.url_altmetric);
                         response.setRenderParameter("doi_altmetric", appInput.doi_altmetric);
                         response.setRenderParameter("PortletStatus", "" + Views.VIEW_ALTMETRICS);
                    }
                    break;
                    
                    case ACTION_ISLEX:
                    // Get current preference values
                    _log.info("Got action: 'ACTION_ISLEX'");
                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);
                    
                    ReadXMLIslex.readFile(appServerPath);
                    
                    response.setRenderParameter("search_word", appInput.search_word);
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_ISLEX);
                    break;
                        
                    case ACTION_GET_ALTMETRICS:
                         _log.info("Got action: 'ACTION_ALTMETRICS'");
                    // Create the appInput object
                    appInput = new App_Input();

                    // Stores the user submitting the job
                    appInput.username = username;

                    // Process input fields and files to upload
                    getInputForm(request, appInput);
                    
                    
                    
                    response.setRenderParameter("search_word", appInput.search_word);
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_ALTMETRICS);
                    break;
                        
                        
                default:
                    _log.info("Unhandled action: '" + actionStatus + "'");
                    response.setRenderParameter("PortletStatus", "" + Views.VIEW_INPUT);                   
                    break;
            }

        } else if (mode.equals(PortletMode.HELP)) {
            // The HELP mode used to give portlet usage HELP to the user
            // This code will be called after the call to doHelp method                         
            _log.info("Portlet mode: HELP");
        } else if (mode.equals(PortletMode.EDIT)) {
            // The EDIT mode is used to view/setup portlet preferences
            // This code will be called after the user sends the actionURL 
            // generated by the doEdit method 
            // The code below just stores new preference values
            _log.info("Portlet mode: EDIT");

            PortletPreferences portletPreferences = request.getPreferences();

            // new preferences will takem from edit.jsp

            //**************OPEN AGRIS*************
            String new_OpenAgris = "";
            if (request.getParameter("OpenAgris") != null) {
                new_OpenAgris = request.getParameter("OpenAgris");
            } else {
                new_OpenAgris = "false";
            }
            //System.out.println("IN ACTION new_OpenAgris----> " + new_OpenAgris);
            response.setRenderParameter("OpenAgris", "" + new_OpenAgris);
            appPreferences.OpenAgris = new_OpenAgris;
            portletPreferences.setValue("OpenAgris", new_OpenAgris);            
            
            //**************OPEN AGRIS ENDPOINT*************
            String new_OpenAgrisEndPoint = "";
            if (request.getParameter("OpenAgrisEndPoint") != null && 
               !request.getParameter("OpenAgrisEndPoint")
                    .equals(appInit.default_OpenAgrisEndPoint)) {
                new_OpenAgrisEndPoint = request.getParameter("OpenAgrisEndPoint");
            } else {
                new_OpenAgrisEndPoint = appInit.default_OpenAgrisEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("OpenAgrisEndPoint", "" + new_OpenAgrisEndPoint);
            appPreferences.OpenAgrisEndPoint = new_OpenAgrisEndPoint;
            portletPreferences.setValue("OpenAgrisEndPoint", new_OpenAgrisEndPoint);
            
            //**************EUROPEANA*************
            String new_Europeana = "";
            if (request.getParameter("Europeana") != null) {
                new_Europeana = request.getParameter("Europeana");
            } else {
                new_Europeana = "false";
            }
            //System.out.println("IN ACTION new_Europeana----> " + new_Europeana);
            response.setRenderParameter("Europeana", "" + new_Europeana);
            appPreferences.Europeana = new_Europeana;
            portletPreferences.setValue("Europeana", new_Europeana);           
            
            //**************EUROPEANA ENDPOINT*************
            String new_EuropeanaEndPoint = "";
            if (request.getParameter("EuropeanaEndPoint") != null && 
               !request.getParameter("EuropeanaEndPoint")
                    .equals(appInit.default_EuropeanaEndPoint)) {
                new_EuropeanaEndPoint = request.getParameter("EuropeanaEndPoint");
            } else {
                new_EuropeanaEndPoint = appInit.default_EuropeanaEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("EuropeanaEndPoint", "" + new_EuropeanaEndPoint);
            appPreferences.EuropeanaEndPoint = new_EuropeanaEndPoint;
            portletPreferences.setValue("EuropeanaEndPoint", new_EuropeanaEndPoint);            

            //**************CULTURA ITALIA*************
            String new_CulturaItalia = "";
            if (request.getParameter("CulturaItalia") != null) {
                new_CulturaItalia = request.getParameter("CulturaItalia");
            } else {
                new_CulturaItalia = "false";
            }
            //System.out.println("IN ACTION new_CulturaItalia----> " + new_CulturaItalia);
            response.setRenderParameter("CulturaItalia", "" + new_CulturaItalia);
            appPreferences.CulturaItalia = new_CulturaItalia;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("CulturaItalia", new_CulturaItalia);
            
            //**************CULTURAITALIA ENDPOINT*************
            String new_CulturaItaliaEndPoint = "";
            if (request.getParameter("CulturaItaliaEndPoint") != null && 
               !request.getParameter("CulturaItaliaEndPoint")
                    .equals(appInit.default_CulturaItaliaEndPoint)) {
                new_CulturaItaliaEndPoint = request.getParameter("CulturaItaliaEndPoint");
            } else {
                new_CulturaItaliaEndPoint = appInit.default_CulturaItaliaEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("CulturaItaliaEndPoint", "" + new_CulturaItaliaEndPoint);
            appPreferences.CulturaItaliaEndPoint = new_CulturaItaliaEndPoint;
            portletPreferences.setValue("CulturaItaliaEndPoint", new_CulturaItaliaEndPoint);
                        
            //**************ISIDORE*************
            String new_Isidore = "";
            if (request.getParameter("Isidore") != null) {
                new_Isidore = request.getParameter("Isidore");
            } else {
                new_Isidore = "false";
            }
            //System.out.println("IN ACTION new_Isidore----> " + new_Isidore);
            response.setRenderParameter("Isidore", "" + new_Isidore);
            appPreferences.Isidore = new_Isidore;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("Isidore", new_Isidore);
            
            //**************ISIDORE ENDPOINT*************
            String new_IsidoreEndPoint = "";
            if (request.getParameter("IsidoreEndPoint") != null && 
               !request.getParameter("IsidoreEndPoint")
                    .equals(appInit.default_IsidoreEndPoint)) {
                new_IsidoreEndPoint = request.getParameter("IsidoreEndPoint");
            } else {
                new_IsidoreEndPoint = appInit.default_IsidoreEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("IsidoreEndPoint", "" + new_IsidoreEndPoint);
            appPreferences.IsidoreEndPoint = new_IsidoreEndPoint;
            portletPreferences.setValue("IsidoreEndPoint", new_IsidoreEndPoint);

            //**************PUBMED*************
            String new_Pubmed = "";
            if (request.getParameter("Pubmed") != null) {
                new_Pubmed = request.getParameter("Pubmed");
            } else {
                new_Pubmed = "false";
            }
            //System.out.println("IN ACTION new_Isidore----> " + new_Isidore);
            response.setRenderParameter("Pubmed", "" + new_Pubmed);
            appPreferences.Pubmed = new_Pubmed;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("Pubmed", new_Pubmed);            
            
            //**************PUBMED ENDPOINT*************
            String new_PubmedEndPoint = "";
            if (request.getParameter("PubmedEndPoint") != null && 
               !request.getParameter("PubmedEndPoint")
                    .equals(appInit.default_PubmedEndPoint)) {
                new_PubmedEndPoint = request.getParameter("PubmedEndPoint");
            } else {
                new_PubmedEndPoint = appInit.default_PubmedEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("PubmedEndPoint", "" + new_PubmedEndPoint);
            appPreferences.PubmedEndPoint = new_PubmedEndPoint;
            portletPreferences.setValue("PubmedEndPoint", new_PubmedEndPoint);

            //**************ENGAGE*************
            String new_Engage = "";
            if (request.getParameter("Engage") != null) {
                new_Engage = request.getParameter("Engage");
                // System.out.println("ENGAGE1: " + new_Engage);
            } else {
                new_Engage = "false";
            }
            //System.out.println("IN ACTION new_Engage----> " + new_Engage);
            response.setRenderParameter("Enagage", "" + new_Engage);
            appPreferences.Engage = new_Engage;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("Engage", new_Engage);
            
            //**************ENGAGE ENDPOINT*************
            String new_EngageEndPoint = "";
            if (request.getParameter("EngageEndPoint") != null && 
               !request.getParameter("EngageEndPoint")
                    .equals(appInit.default_EngageEndPoint)) {
                new_EngageEndPoint = request.getParameter("EngageEndPoint");
            } else {
                new_EngageEndPoint = appInit.default_EngageEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("EngageEndPoint", "" + new_EngageEndPoint);
            appPreferences.EngageEndPoint = new_EngageEndPoint;
            portletPreferences.setValue("EngageEndPoint", new_EngageEndPoint); 
            
            
            //**************DBPEDIA*************
            String new_DBPedia = "";
            if (request.getParameter("DBPedia") != null) {
                new_DBPedia = request.getParameter("DBPedia");
                // System.out.println("ENGAGE1: " + new_Engage);
            } else {
                new_DBPedia = "false";
            }
            //System.out.println("IN ACTION new_Engage----> " + new_Engage);
            response.setRenderParameter("new_DBPedia", "" + new_DBPedia);
            appPreferences.DBPedia = new_DBPedia;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("DBPedia", new_DBPedia);
            
            //**************DBPEDIA ENDPOINT*************
            String new_DBPediaEndPoint = "";
            if (request.getParameter("DBPediaEndPoint") != null && 
               !request.getParameter("DBPediaEndPoint")
                    .equals(appInit.default_DBPediaEndPoint)) {
                new_DBPediaEndPoint = request.getParameter("DBPediaEndPoint");
            } else {
                new_DBPediaEndPoint = appInit.default_DBPediaEndPoint;
            }
            //System.out.println("IN ACTION new_OpenAgrisEndPoint----> " + new_OpenAgrisEndPoint);
            response.setRenderParameter("DBPediaEndPoint", "" + new_DBPediaEndPoint);
            appPreferences.DBPediaEndPoint = new_DBPediaEndPoint;
            portletPreferences.setValue("DBPediaEndPoint", new_DBPediaEndPoint);            
            
            
            

            //**************NUMBER RECORDS FOR PAGE*************
            String new_NumberRecordsForPage = "";
            if (request.getParameter("NumberRecordsForPage") != null) {
                new_NumberRecordsForPage = request.getParameter("NumberRecordsForPage");
            } else {
                new_NumberRecordsForPage = "20";
            }
            //System.out.println("IN ACTION new_NumberRecordsForPage----> " + new_NumberRecordsForPage);
            response.setRenderParameter("NumberRecordsForPage", "" + new_NumberRecordsForPage);
            appPreferences.NumberRecordsForPage = new_NumberRecordsForPage;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("NumberRecordsForPage", new_NumberRecordsForPage);

            //**************LODLIVE*************
            String new_LodLive = "";
            if (request.getParameter("LodLive") != null) {
                new_LodLive = request.getParameter("LodLive");
                // System.out.println("LODLIVE1: " + new_LodLive);
            } else {
                new_LodLive = "false";
            }
            //System.out.println("IN ACTION new_LodLive----> " + new_LodLive);
            response.setRenderParameter("LodLive", "" + new_LodLive);
            appPreferences.LodLive = new_LodLive;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("LodLive", new_LodLive);
            
            //**************ENGAGE ENDPOINT*************            
            String new_LodLiveEndPoint = "";
            if (request.getParameter("LodLiveEndPoint") != null) {
                new_LodLiveEndPoint = request.getParameter("LodLiveEndPoint");                
            } else {
                new_LodLiveEndPoint = "http://localhost:8080/testlodlive/?<%=resource%>";
            }
            //System.out.println("IN ACTION new_LodLiveEndPoint----> " + new_LodLiveEndPoint);
            response.setRenderParameter("LodLiveEndPoint", "" + new_LodLiveEndPoint);
            appPreferences.LodLiveEndPoint = new_LodLiveEndPoint;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("LodLiveEndPoint", new_LodLiveEndPoint);
            
            //**************TIME OUT*************
            String new_TimeOut = "";
            if (request.getParameter("TimeOut") != null) {
                new_TimeOut = request.getParameter("TimeOut");
            } else {
                new_TimeOut = "1";
            }
            //System.out.println("IN ACTION new_TimeOut----> " + new_TimeOut);
            response.setRenderParameter("Time Out", "" + new_TimeOut);
            appPreferences.TimeOut = new_TimeOut;
            // request.setAttribute("pref_value", new_pref_value);
            portletPreferences.setValue("TimeOut", new_TimeOut);

            portletPreferences.store();
           // response.setPortletMode(PortletMode.VIEW);

        } else {
            // Unsupported portlet modes come here
            _log.warn("Custom portlet mode: '" + mode.toString() + "'");
        }
    } // processAction

    //
    // Method responsible to show portlet content to the user accordingly to the current view mode
    //
    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        _log.info("calling doView ...");
        response.setContentType("text/html");

        //PortletPreferences portletPreferences = request.getPreferences();

        //handlingPrefernces(portletPreferences);

       if(init_Preferences)
       {
           PortletPreferences portletPreferences = request.getPreferences();
           handlingPrefernces(portletPreferences);
           init_Preferences=false;
       }

        // Determine the application pathname                
        portletSession = request.getPortletSession();

        portletContext = portletSession.getPortletContext();
        appServerPath = portletContext.getRealPath("/");
        _log.info("Web Application path: '" + appServerPath + "'");

        // Switch among supported views; the currentView is determined by the
        // portlet render parameter value stored into PortletStatus identifier
        // this value has been assigned by the actionStatus or it will be 
        // null in case the doView method will be called without a
        // previous processAction call; in such a case the default VIEW_INPIUT
        // will be selected.
        //The PortletStatus variable is managed by jsp and this java code
        String currentView = request.getParameter("PortletStatus");
        if (null == currentView
                || currentView.equals("")) {
            //currentView = "" + Views.VIEW_INPUT;
            currentView = "" + Views.VIEW_INPUT;
        }
               
        // Different actions will be performed accordingly to the
        // different possible view modes
        switch (Views.valueOf(currentView)) {
            // The following code is responsible to call the proper jsp file
            // that will provide the correct portlet interface
            case VIEW_INPUT: {
                _log.info("VIEW_INPUT Selected ...");
                //PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/viewDetailsResourceCulturaItalia.jsp");
               
                PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/input.jsp");
                dispatcher.include(request, response);
            }
            break;

            case VIEW_SEMANTIC_SEARCH_ALL_LANGUAGE: {
                _log.info("VIEW_SEMANTIC_SEARCH_ALL_LANGUAGE Selected ...");
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/resultFromAllLanguage.jsp");
                dispatcher.include(request, response);

                //  firstAction = false;

            }
            break;
                
            case VIEW_GET_MORE_INFO: {
                _log.info("VIEW_GET_MORE_INFO Selected ...");
                String idResource = request.getParameter("idResource");
                request.setAttribute("idResource", idResource);
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResource.jsp");
                dispatcher.include(request, response);
            }

            break;

            case VIEW_GET_MORE_INFO_OPENAGRIS: {
                _log.info("VIEW_GET_MORE_INFO_OPENAGRIS Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceOpenAgris.jsp");

                dispatcher.include(request, response);
            }
            
            break;

            case VIEW_GET_MORE_INFO_CULTURAITALIA: {
                _log.info("VIEW_GET_MORE_INFO_CULTURAITALIA Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceCulturaItalia.jsp");

                dispatcher.include(request, response);
            }
            break;

            case VIEW_GET_MORE_INFO_EUROPEANA: {
                _log.info("VIEW_GET_MORE_INFO_EUROPEANA Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceEuropeana.jsp");

                dispatcher.include(request, response);
            }
            break;

            case VIEW_GET_MORE_INFO_ISIDORE: {
                _log.info("VIEW_GET_MORE_INFO_ISIDORE Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceIsidore.jsp");

                dispatcher.include(request, response);
            }
            break;


            case VIEW_GET_MORE_INFO_PUBMED: {
                _log.info("VIEW_GET_MORE_INFO_PUBMED Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourcePubmed.jsp");

                dispatcher.include(request, response);
            }
                break;
             
            case VIEW_GET_MORE_INFO_DBPEDIA: {
                _log.info("VIEW_GET_MORE_INFO_DBPEDIA Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceDBPedia.jsp");

                dispatcher.include(request, response);
            }
                break;     
                
                
            case VIEW_GET_MORE_INFO_ENGAGE: {
                _log.info("VIEW_GET_MORE_INFO_ENGAGE Selected ...");
                //String idResource = request.getParameter("idResource");
                // request.setAttribute("idResource", idResource);                               
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewDetailsResourceEngage.jsp");

                dispatcher.include(request, response);
            }
            break;
            case VIEW_CITATIONS_GSCHOLAR: {
                _log.info("VIEW_CITATIONS_GSCHOLAR Selected ...");
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewCitationsGS.jsp");
                dispatcher.include(request, response);
            }
            
            break;
            case VIEW_ISLEX: {
                _log.info("VIEW_ISLEX Selected ...");
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewIslex.jsp");
                dispatcher.include(request, response);
            }
            break;    
            case VIEW_ALTMETRICS: {
                _log.info("VIEW_ALTMETRICS Selected ...");
                PortletRequestDispatcher dispatcher = 
			getPortletContext().getRequestDispatcher("/viewAltmetrics.jsp");
                dispatcher.include(request, response);
            }
            break;
                

            default:
                _log.info("Unknown view mode: " + currentView.toString());
                break;
                
        } // switch            
    } // doView

    public void handlingPrefernces(PortletPreferences portletPreferences) 
            throws ReadOnlyException, IOException, ValidatorException 
    {

        System.out.println("***********HANDLING PREFERENCES IN DOVIEW*****************");

        String OpenAgris = portletPreferences.getValue("OpenAgris", "");
        //System.out.println("appPreferences.OpenAgris--->" + OpenAgris);

        if (OpenAgris == null || OpenAgris.equals("")) {
           // System.out.println("OpenAgris null");
            //System.out.println("DefaultValue OpenAgris " + appInit.default_OpenAgris);
            appPreferences.OpenAgris = appInit.default_OpenAgris;

            portletPreferences.setValue("OpenAgris", appPreferences.OpenAgris);

            //portletPreferences.store();
        }
        
        String OpenAgrisEndPoint = portletPreferences.getValue("OpenAgrisEndPoint", "");
        //System.out.println("appPreferences.OpenAgris--->" + OpenAgris);

        if (OpenAgrisEndPoint == null || OpenAgrisEndPoint.equals("")) {
           // System.out.println("OpenAgris null");
            //System.out.println("DefaultValue OpenAgris " + appInit.default_OpenAgris);
            appPreferences.OpenAgrisEndPoint = appInit.default_OpenAgrisEndPoint;

            portletPreferences.setValue("OpenAgrisEndPoint", appPreferences.OpenAgrisEndPoint);

           // portletPreferences.store();
        }

        String Europeana = portletPreferences.getValue("Europeana", "");
        //System.out.println("appPreferences.Europeana--->" + Europeana);

        if (Europeana == null || Europeana.equals("")) {
            System.out.println("Europeana null");
            System.out.println("DefaultValue Europeana " + appInit.default_Europeana);
            appPreferences.Europeana = appInit.default_Europeana;

            portletPreferences.setValue("Europeana", appPreferences.Europeana);

            //portletPreferences.store();
            
        }
        
        String EuropeanaEndPoint = portletPreferences.getValue("EuropeanaEndPoint", "");
        //System.out.println("appPreferences.Europeana--->" + Europeana);

        if (EuropeanaEndPoint == null || EuropeanaEndPoint.equals("")) {
            //System.out.println("Europeana null");
            //System.out.println("DefaultValue Europeana " + appInit.default_Europeana);
            appPreferences.EuropeanaEndPoint = appInit.default_EuropeanaEndPoint;
            portletPreferences.setValue("EuropeanaEndPoint", appPreferences.EuropeanaEndPoint);
            //portletPreferences.store();            
        }
        
        String CulturaItalia = portletPreferences.getValue("CulturaItalia", "");
        //System.out.println("appPreferences.CulturaItalia--->" + CulturaItalia);

        if (CulturaItalia == null || CulturaItalia.equals("")) {
            //System.out.println("CulturaItalia null");
            //System.out.println("DefaultValue CulturaItalia " + appInit.default_CulturaItalia);
            appPreferences.CulturaItalia = appInit.default_CulturaItalia;
            portletPreferences.setValue("CulturaItalia", appPreferences.CulturaItalia);
           // portletPreferences.store();                        
        }
        
        String CulturaItaliaEndPoint = portletPreferences.getValue("CulturaItaliaEndPoint", "");
        //System.out.println("appPreferences.CulturaItalia--->" + CulturaItalia);

        if (CulturaItaliaEndPoint == null || CulturaItaliaEndPoint.equals("")) {
            //System.out.println("CulturaItalia null");
            //System.out.println("DefaultValue CulturaItalia " + appInit.default_CulturaItalia);
            appPreferences.CulturaItaliaEndPoint = appInit.default_CulturaItaliaEndPoint;
            portletPreferences.setValue("CulturaItaliaEndPoint", appPreferences.CulturaItaliaEndPoint);
          //  portletPreferences.store();                        
        }
        
        String Isidore = portletPreferences.getValue("Isidore", "");
        //System.out.println("appPreferences.Isidore--->" + Isidore);

        if (Isidore == null || Isidore.equals("")) {
            //System.out.println("Isidore null");
            //System.out.println("DefaultValue Isidore " + appInit.default_Isidore);
            appPreferences.Isidore = appInit.default_Isidore;
            portletPreferences.setValue("Isidore", appPreferences.Isidore);
           // portletPreferences.store();
        }
        
         String IsidoreEndPoint = portletPreferences.getValue("IsidoreEndPoint", "");
        //System.out.println("appPreferences.Isidore--->" + Isidore);

        if (IsidoreEndPoint == null || IsidoreEndPoint.equals("")) {
            //System.out.println("Isidore null");
            //System.out.println("DefaultValue Isidore " + appInit.default_Isidore);
            appPreferences.IsidoreEndPoint = appInit.default_IsidoreEndPoint;
            portletPreferences.setValue("IsidoreEndPoint", appPreferences.IsidoreEndPoint);
           // portletPreferences.store();
        }
        
        String Engage = portletPreferences.getValue("Engage", "");
        //System.out.println("appPreferences.Engage--->" + Engage);

        if (Engage == null || Engage.equals("")) {
            //System.out.println("Engage null");
            //System.out.println("DefaultValue Engage " + appInit.default_Engage);
            appPreferences.Engage = appInit.default_Engage;
            portletPreferences.setValue("Engage", appPreferences.Engage);
           // portletPreferences.store();
        }
        
        String EngageEndPoint = portletPreferences.getValue("EngageEndPoint", "");
        //System.out.println("appPreferences.Engage--->" + Engage);

        if (EngageEndPoint == null || EngageEndPoint.equals("")) {
            //System.out.println("Engage null");
            //System.out.println("DefaultValue Engage " + appInit.default_Engage);
            appPreferences.EngageEndPoint = appInit.default_EngageEndPoint;
            portletPreferences.setValue("EngageEndPoint", appPreferences.EngageEndPoint);
          //  portletPreferences.store();
        }
        
        String Pubmed = portletPreferences.getValue("Pubmed", "");
        //System.out.println("appPreferences.Pubmed--->" + Pubmed);

        if (Pubmed == null || Pubmed.equals("")) {
            //System.out.println("Pubmed null");
            //System.out.println("DefaultValue Pubmed " + appInit.default_Pubmed);
            appPreferences.Pubmed = appInit.default_Pubmed;
            portletPreferences.setValue("Pubmed", appPreferences.Pubmed);
          //  portletPreferences.store();
        }
        
        String PubmedEndPoint = portletPreferences.getValue("PubmedEndPoint", "");
        //System.out.println("appPreferences.Pubmed--->" + Pubmed);

        if (PubmedEndPoint == null || PubmedEndPoint.equals("")) {
            //System.out.println("Pubmed null");
            //System.out.println("DefaultValue Pubmed " + appInit.default_Pubmed);
            appPreferences.PubmedEndPoint = appInit.default_PubmedEndPoint;
            portletPreferences.setValue("PubmedEndPoint", appPreferences.PubmedEndPoint);
            portletPreferences.store();
        }
        
        String DBPedia = portletPreferences.getValue("DBPedia", "");
        //System.out.println("appPreferences.Pubmed--->" + Pubmed);

        if (DBPedia == null || DBPedia.equals("")) {
            //System.out.println("Pubmed null");
            //System.out.println("DefaultValue Pubmed " + appInit.default_Pubmed);
            appPreferences.DBPedia = appInit.default_DBPedia;
            portletPreferences.setValue("DBPedia", appPreferences.DBPedia);
          //  portletPreferences.store();
        }
        
        String DBPediaEndPoint = portletPreferences.getValue("DBPediaEndPoint", "");
        //System.out.println("appPreferences.Pubmed--->" + Pubmed);

        if (DBPediaEndPoint == null || DBPediaEndPoint.equals("")) {
            //System.out.println("Pubmed null");
            //System.out.println("DefaultValue Pubmed " + appInit.default_Pubmed);
            appPreferences.DBPediaEndPoint = appInit.default_DBPediaEndPoint;
            portletPreferences.setValue("DBPediaEndPoint", appPreferences.DBPediaEndPoint);
            portletPreferences.store();
        }
        
        String NumberRecordsForPage = portletPreferences.getValue("NumberRecordsForPage", "");
        //System.out.println("appPreferences.NumberRecordsForPage--->" + NumberRecordsForPage);

        if (NumberRecordsForPage == null || NumberRecordsForPage.equals("")) {
            //System.out.println("NumberRecordsForPage null");
            //System.out.println("DefaultValue NumberRecordsForPage " + appInit.default_NumberRecordsForPage);
            appPreferences.NumberRecordsForPage = appInit.default_NumberRecordsForPage;
            portletPreferences.setValue("NumberRecordsForPage", appPreferences.NumberRecordsForPage);
            //portletPreferences.store();
        }
        portletPreferences.store();
        //System.out.println("*************FINE HANDLING PREFERENCES IN DOVIEW*****************");
    }

    //
    // doEdit
    //
    // This methods prepares an actionURL that will be used by edit.jsp file into a <input ...> form
    // As soon the user press the action button the processAction will be called and the portlet mode
    // will be set as EDIT.
    @Override
    public void doEdit(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        response.setContentType("text/html");
        //System.out.println("DENTRO DOEDIT OpenAgris----> " + appPreferences.OpenAgris);
        //System.out.println("DENTRO DOEDIT Europeana----> " + appPreferences.Europeana);
        //System.out.println("DENTRO DOEDIT CulturaItalia----> " + appPreferences.CulturaItalia);

        // The edit.jsp will be the responsible to show/edit the current preference values
        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/edit.jsp");
        dispatcher.include(request, response);
    } // doEdit

    //
    // doHelp
    //
    // This method just calls the jsp responsible to show the portlet information
    @Override
    public void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        response.setContentType("text/html");

        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/help.jsp");
        dispatcher.include(request, response);
    } // doHelp

    //
    // getInputForm
    //
    // The use of upload file controls needs the use of "multipart/form-data"
    // form type. With this kind of input form it is necessary to process 
    // each item of the action request manually
    //
    // All form' input items are identified by the 'name' input property
    // inside the jsp file
    private enum inputControlsIds {

        file_inputFile // Input file textarea 
        , inputFile // Input file input file
        , JobIdentifier     // User defined Job identifier
    };
    //
    // getInputForm (method)
    //

    public void getInputForm(ActionRequest request, App_Input appInput) {
        if (PortletFileUpload.isMultipartContent(request)) {
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                PortletFileUpload upload = new PortletFileUpload(factory);
                List items = upload.parseRequest(request);
                File repositoryPath = new File("/tmp");
                DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
                diskFileItemFactory.setRepository(repositoryPath);
                Iterator iter = items.iterator();
                String logstring = "";
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    // Prepare a log string with field list
                    logstring += LS + "field name: '" + fieldName + "' - '" + item.getString() + "'";
                    switch (inputControlsIds.valueOf(fieldName)) {

                        case JobIdentifier:
                            appInput.jobIdentifier = item.getString();
                            break;
                        default:
                            _log.warn("Unhandled input field: '" + fieldName + "' - '" + item.getString() + "'");
                    } // switch fieldName                                                   
                } // while iter.hasNext()   
                _log.info(
                        LS + "Reporting"
                        + LS + "---------"
                        + LS + logstring
                        + LS);
            } // try
            catch (Exception e) {
                _log.info("Caught exception while processing files to upload: '" + e.toString() + "'");
            }
        } // The input form do not use the "multipart/form-data" 
        else {
            // Retrieve from the input form the given application values
            appInput.search_word = (String) request.getParameter("search_word");
            appInput.jobIdentifier = (String) request.getParameter("JobIdentifier");
            appInput.nameSubject = (String) request.getParameter("nameSubject");

            appInput.idResouce = (String) request.getParameter("idResource");
            appInput.selected_language = (String) request.getParameter("selLanguage");
            appInput.numberPage = (String) request.getParameter("numberOfPage");
            appInput.numberPageOpenAgris = (String) request.getParameter("numberOfPageOpenAgris");
            appInput.numRecordsForPage = (String) request.getParameter("numberOfRecords");
            appInput.moreResourceCHAIN = (String) request.getParameter("moreResourceCHAIN");
            appInput.moreResourceOpenAgris = (String) request.getParameter("moreResourceOpenAgris");
            appInput.idResourceOpenAgris = (String) request.getParameter("idResourceOpenAgris");
            appInput.moreInfoOpenAgris = (String) request.getParameter("moreInfoOpenAgris");
            appInput.moreInfo = (String) request.getParameter("moreInfo");
            appInput.numberPageCulturaItalia = (String) request.getParameter("numberOfPageCulturaItalia");
            appInput.moreResourceCulturaItalia = (String) request.getParameter("moreResourceCulturaItalia");
            appInput.moreInfoCulturaItalia = (String) request.getParameter("moreInfoCulturaItalia");
            appInput.numResource = (String) request.getParameter("numResource");
            appInput.numResourceFromDetails = (String) request.getParameter("numResourceFromDetails");
            appInput.numResourceOpenAgris = (String) request.getParameter("numResourceOpenAgris");
            appInput.numResourceOpenAgrisFromDetails = (String) request.getParameter("numResourceOpenAgrisFromDetails");
            appInput.numResourceCulturaItalia = (String) request.getParameter("numResourceCulturaItalia");
            appInput.numResourceCulturaItaliaFromDetails = (String) request.getParameter("numResourceCulturaItaliaFromDetails");
            appInput.idResourceCulturaItalia = (String) request.getParameter("idResourceCulturaItalia");

            appInput.numberPageIsidore = (String) request.getParameter("numberOfPageIsidore");
            appInput.moreResourceIsidore = (String) request.getParameter("moreResourceIsidore");
            appInput.idResourceIsidore = (String) request.getParameter("idResourceIsidore");
            appInput.numResourceIsidore = (String) request.getParameter("numResourceIsidore");
            appInput.numResourceIsidoreFromDetails = (String) request.getParameter("numResourceIsidoreFromDetails");
            appInput.numberPageIsidore = (String) request.getParameter("numberOfPageIsidore");
            appInput.moreInfoIsidore = (String) request.getParameter("moreInfoIsidore");

            appInput.idResourceEuropeana = (String) request.getParameter("idResourceEuropeana");
            appInput.moreResourceEuropeana = (String) request.getParameter("moreResourceEuropeana");
            appInput.numberPageEuropeana = (String) request.getParameter("numberOfPageEuropeana");
            appInput.numResourceEuropeana = (String) request.getParameter("numResourceEuropeana");
            appInput.numResourceEuropeanaFromDetails = (String) request.getParameter("numResourceEuropeanaFromDetails");
            appInput.numberPageEuropeana = (String) request.getParameter("numberOfPageEuropeana");
            appInput.moreInfoEuropeana = (String) request.getParameter("moreInfoEuropeana");

            appInput.idResourcePubmed = (String) request.getParameter("idResourcePubmed");
            appInput.moreResourcePubmed = (String) request.getParameter("moreResourcePubmed");
            appInput.numberPagePubmed = (String) request.getParameter("numberOfPagePubmed");
            appInput.numResourcePubmed = (String) request.getParameter("numResourcePubmed");
            appInput.numResourcePubmedFromDetails = (String) request.getParameter("numResourcePubmedFromDetails");
            appInput.moreInfoPubmed = (String) request.getParameter("moreInfoPubmed");

            appInput.idResourceEngage = (String) request.getParameter("idResourceEngage");
            appInput.moreResourceEngage = (String) request.getParameter("moreResourceEngage");
            appInput.numberPageEngage = (String) request.getParameter("numberOfPageEngage");
            appInput.numResourceEngage = (String) request.getParameter("numResourceEngage");
            appInput.numResourceEngageFromDetails = (String) request.getParameter("numResourceEngageFromDetails");
            appInput.moreInfoEngage = (String) request.getParameter("moreInfoEngage");
            
            appInput.idResourceDBPedia = (String) request.getParameter("idResourceDBPedia");
            appInput.moreResourceDBPedia = (String) request.getParameter("moreResourceDBPedia");
            appInput.numberPageDBPedia = (String) request.getParameter("numberOfPageDBPedia");
            appInput.numResourceDBPedia = (String) request.getParameter("numResourceDBPedia");
            appInput.numResourceDBPediaFromDetails = (String) request.getParameter("numResourceDBPediaFromDetails");
            appInput.moreInfoDBPedia = (String) request.getParameter("moreInfoDBPedia");
            
            appInput.title_GS = (String) request.getParameter("title_GS");
            appInput.chek_altmetric=(String) request.getParameter("chek_altmetric");
            appInput.url_altmetric=(String) request.getParameter("url_altmetric");
            appInput.doi_altmetric=(String) request.getParameter("doi_altmetric");

        } // ! isMultipartContent

        // Show into the log the taken inputs
        _log.info(
                LS + "Taken input parameters:"
                + LS + "-----------------------"
                + LS + "Search Word: '" + appInput.search_word + "'"
                + LS + "jobIdentifier: '" + appInput.jobIdentifier + "'"
                + LS + "subject: '" + appInput.nameSubject + "'"
                + LS + "idResource: '" + appInput.idResouce + "'"
                + LS + "language selected: '" + appInput.selected_language + "'"
                + LS + "number page selected: '" + appInput.numberPage + "'"
                + LS + "number record for page: '" + appInput.numRecordsForPage + "'"
                + LS + "number page selected OpenAgris: '" + appInput.numberPageOpenAgris + "'"
                + LS + "moreResourceCHAIN: '" + appInput.moreResourceCHAIN + "'"
                + LS + "moreInfo: '" + appInput.moreInfo + "'"
                + LS + "moreResourceOpenAgris: '" + appInput.moreResourceOpenAgris + "'"
                + LS + "idResourceOpenAgris: '" + appInput.idResourceOpenAgris + "'"
                + LS + "moreInfoOpenAgris: '" + appInput.moreInfoOpenAgris + "'"
                + LS + "number page selected CulturaItalia: '" + appInput.numberPageCulturaItalia + "'"
                + LS + "moreResourceCulturaItalia: '" + appInput.moreResourceCulturaItalia + "'"
                + LS + "moreInfoCulturaItalia: '" + appInput.moreInfoCulturaItalia + "'"
                + LS + "NumResource: '" + appInput.numResource + "'"
                + LS + "NumResourceFromDetails: '" + appInput.numResourceFromDetails + "'"
                + LS + "NumResourceOpenAgris: '" + appInput.numResourceOpenAgris + "'"
                + LS + "NumResourceOpenAgrisFromDetails: '" + appInput.numResourceOpenAgrisFromDetails + "'"
                + LS + "NumResourceCulturaItalia: '" + appInput.numResourceCulturaItalia + "'"
                + LS + "NumResourceCulturaItaliaFromDetails: '" + appInput.numResourceCulturaItaliaFromDetails + "'"
                + LS + "idResourceCulturaItalia: '" + appInput.idResourceCulturaItalia + "'"
                + LS + "NumResourceDBPedia: '" + appInput.numResourceDBPedia + "'"
                + LS + "NumResourceDBPediaFromDetails: '" + appInput.numResourceDBPediaFromDetails + "'"
                + LS + "idResourceCulturaItalia: '" + appInput.idResourceCulturaItalia + "'"
                + LS + "moreResourceEuropeana: '" + appInput.moreResourceEuropeana + "'"
                + LS);
    } // getInputForm 

    public void handlerTabCHAIN(ActionRequest request, ActionResponse response, 
                                App_Input appInput, int numberRecords) 
            throws RepositoryException, MalformedQueryException, 
                   QueryEvaluationException, UnsupportedEncodingException, 
                   MalformedURLException 
    {
        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
          && !appInput.moreInfoDBPedia.equals("OK")) 
        {

            SemanticQuery query = new SemanticQuery();

            if (appInput.numberPage == null || appInput.numberPage == "") 
            {
                // virtuosoResourceList=new ArrayList();
                selected_page = "1";

                //  search_word = appInput.search_word;

                ArrayList virtuosoResourceList = 
                        query.queryVirtuosoResource(
                        appInput.search_word, 
                        selected_page, 
                        numberRecords);

                ArrayList chainTitleList = new ArrayList();
                ArrayList chainIdentifierList = new ArrayList();
                ArrayList chainAuthorList = new ArrayList();
                ArrayList chainDescriptionList = new ArrayList();
                ArrayList chainRepositoryList = new ArrayList();

                for (int i = 0; i < virtuosoResourceList.size(); i++) {
                    String resource = virtuosoResourceList.get(i).toString();

                    String listTempTitle = query.getTitle(resource);
                    String listTempIdentifier = query.getIdentifiers(resource);
                    String listTempAuthor = query.getAuthors(resource);
                    String listTempDescription = query.getDescription(resource);
                    String listTempRepository = query.getNameUrlRepository(resource);


                    chainTitleList.add(listTempTitle);
                    chainIdentifierList.add(listTempIdentifier);
                    chainAuthorList.add(listTempAuthor);
                    chainDescriptionList.add(listTempDescription);
                    chainRepositoryList.add(listTempRepository);
                }

                sArray = (String[]) virtuosoResourceList.toArray(new String[virtuosoResourceList.size()]);
                sArrayChainTitle = (String[]) chainTitleList.toArray(new String[chainTitleList.size()]);
                sArrayChainIdentifier = (String[]) chainIdentifierList.toArray(new String[chainIdentifierList.size()]);
                sArrayChainAuthor = (String[]) chainAuthorList.toArray(new String[chainAuthorList.size()]);
                sArrayChainDescription = (String[]) chainDescriptionList.toArray(new String[chainDescriptionList.size()]);
                sArrayChainRepository = (String[]) chainRepositoryList.toArray(new String[chainRepositoryList.size()]);

                // sArray = (String[]) virtuosoResourceList.toArray(new String[virtuosoResourceList.size()]);

            } else {
                selected_page = appInput.numberPage;
                //selected_page="2";
                moreResourceCHAIN = appInput.moreResourceCHAIN;


                if (appInput.moreResourceCHAIN.equals("OK")) {

                    ArrayList newArray = query.queryVirtuosoResource(appInput.search_word, selected_page, numberRecords);
                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayId = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();
                    ArrayList newArrayRep = new ArrayList();

                    for (int i = 0; i < newArray.size(); i++) {

                        String resource = newArray.get(i).toString();
                        //virtuosoResourceList.add(resource);

                        String listTempTitle = query.getTitle(resource);
                        String listTempIdentifier = query.getIdentifiers(resource);
                        String listTempAuthor = query.getAuthors(resource);
                        String listTempDescription = query.getDescription(resource);
                        String listTempRepository = query.getNameUrlRepository(resource);

                        newArrayTitle.add(listTempTitle);
                        newArrayId.add(listTempIdentifier);
                        newArrayAuthor.add(listTempAuthor);
                        newArrayDesc.add(listTempDescription);
                        newArrayRep.add(listTempRepository);
                    }

                    sArray = (String[]) newArray.toArray(new String[newArray.size()]);
                    sArrayChainTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayChainIdentifier = (String[]) newArrayId.toArray(new String[newArrayId.size()]);
                    sArrayChainAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayChainDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                    sArrayChainRepository = (String[]) newArrayRep.toArray(new String[newArrayRep.size()]);
                }
            }

        }

        response.setRenderParameter("arrayVirtuosoResource", sArray);
        response.setRenderParameter("arrayChainTitle", sArrayChainTitle);
        response.setRenderParameter("arrayChainIdentifier", sArrayChainIdentifier);
        response.setRenderParameter("arrayChainAuthor", sArrayChainAuthor);
        response.setRenderParameter("arrayChainDescription", sArrayChainDescription);
        response.setRenderParameter("arrayChainRepository", sArrayChainRepository);
        response.setRenderParameter("moreResourceCHAIN", moreResourceCHAIN);
        response.setRenderParameter("moreInfo", appInput.moreInfo);
        response.setRenderParameter("searched_word", appInput.search_word);
        response.setRenderParameter("selected_page", selected_page);
        response.setRenderParameter("numResourceFromDetails", appInput.numResourceFromDetails);
    }

    public void handlerTabOpenAgris(
            ActionRequest request, 
            ActionResponse response, 
            App_Input appInput, 
            int numberRecords, 
            PortletPreferences portletPreferences,
            int TimeOut) 
            throws RepositoryException, MalformedQueryException, 
                   QueryEvaluationException, UnsupportedEncodingException, 
                   MalformedURLException, IOException 
    {

        System.out.println("OPEN AGRIS OK");
        //Se non è stato cliccato moreInfo in nessun tab non devo fare nulla ma solo
        //mandare tutti i parametri precedentemente calcolati al jsp
        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {

            String OpenAgrisEndPoint = portletPreferences.getValue("OpenAgrisEndPoint", "");            
            QueryOpenAgris.ConnectionToOpenAgris(OpenAgrisEndPoint);
            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPageOpenAgris == null || appInput.numberPageOpenAgris == "")) {

                // openAgrisResourceList=new ArrayList();

                // System.out.println("OPEN AGRIS appInput.numberPageOpenAgris == null");
                selected_pageOpenAgris = "1";
                
                //eseguo la query per prendere le prime 20 risorse
//                ArrayList openAgrisResourceList = QueryOpenAgris
//                        .queryOpenAgrisResource(
//                        appInput.search_word, 
//                        selected_pageOpenAgris, 
//                        numberRecords,
//                        OpenAgrisEndPoint,
//                        TimeOut);
                
                ArrayList openAgrisResourceList=executeCommandQuery( 
                        appInput.search_word, 
                        selected_pageOpenAgris, 
                        numberRecords,
                        OpenAgrisEndPoint,
                        TimeOut,
                        "TimeOutOpenAgris.jar");
                

                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList openAgrisTitleList = new ArrayList();
                ArrayList openAgrisAuthorList = new ArrayList();
                ArrayList openAgrisDescriptionList = new ArrayList();
                for (int i = 0; i < openAgrisResourceList.size(); i++) {
                    String resource = openAgrisResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitle = QueryOpenAgris.getTitle(resource);
                    String listTempAuthors = QueryOpenAgris.getAuthors(resource);
                    String listTempDescription = QueryOpenAgris.getDescription(resource);

                    //aggiungo le proprietà  in appositi ArrayList
                    openAgrisTitleList.add(listTempTitle);
                    openAgrisAuthorList.add(listTempAuthors);
                    openAgrisDescriptionList.add(listTempDescription);
                }
                
                sArrayOpenAgris = (String[]) openAgrisResourceList.toArray(new String[openAgrisResourceList.size()]);
                sArrayOpenAgrisTitle = (String[]) openAgrisTitleList.toArray(new String[openAgrisTitleList.size()]);
                sArrayOpenAgrisAuthor = (String[]) openAgrisAuthorList.toArray(new String[openAgrisAuthorList.size()]);
                sArrayOpenAgrisDescription = (String[]) openAgrisDescriptionList.toArray(new String[openAgrisDescriptionList.size()]);

            } else {

                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pageOpenAgris = appInput.numberPageOpenAgris;
                moreResourceOpenAgris = appInput.moreResourceOpenAgris;
                //se è non stato cliccato More Resource in OpenAgris non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                // System.out.println("ELSE OPENAGRIS E PAGE--->" + selected_pageOpenAgris);

                if (appInput.moreResourceOpenAgris.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array delle risorse 
                    //in base alla pagina cliccata
                    
                     ArrayList newArray=executeCommandQuery(
                        appInput.search_word, 
                        selected_pageOpenAgris, 
                        numberRecords,
                        OpenAgrisEndPoint,
                        TimeOut,
                        "TimeOutOpenAgris.jar");
                    
                    /*ArrayList newArray = QueryOpenAgris.queryOpenAgrisResource(
                            appInput.search_word, 
                            selected_pageOpenAgris, 
                            numberRecords,OpenAgrisEndPoint,
                            TimeOut);*/

                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();

                    //System.out.println("MORE RESOURCE OPENAGRIS");
                    for (int i = 0; i < newArray.size(); i++) {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta 
                        //all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();
                        //Per ogni nuova risorsa 
                        //openAgrisResourceList.add(newArray.get(i));

                        //Per ogni nuova risorsa calcolo le proprietà che verranno 
                        //aggiunte all'array di quelle già presenti
                        String listTempTitle = QueryOpenAgris.getTitle(resource);
                        String listTempAuthors = QueryOpenAgris.getAuthors(resource);
                        String listTempDescription = QueryOpenAgris.getDescription(resource);
                        newArrayTitle.add(listTempTitle);
                        newArrayAuthor.add(listTempAuthors);
                        newArrayDesc.add(listTempDescription);
                    }
                    sArrayOpenAgris = (String[]) newArray.toArray(new String[newArray.size()]);                    
                    sArrayOpenAgrisTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayOpenAgrisAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayOpenAgrisDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                }
            }

            //converto gli ArrayList in String [] in modo da poter passarli come parametri al jsp

        }
        response.setRenderParameter("selected_pageOpenAgris", selected_pageOpenAgris);
        response.setRenderParameter("arrayOpenAgrisResource", sArrayOpenAgris);
        response.setRenderParameter("arrayOpenAgrisTitle", sArrayOpenAgrisTitle);
        response.setRenderParameter("arrayOpenAgrisAuthor", sArrayOpenAgrisAuthor);
        response.setRenderParameter("arrayOpenAgrisDescription", sArrayOpenAgrisDescription);
        response.setRenderParameter("moreResourceOpenAgris", moreResourceOpenAgris);
        response.setRenderParameter("moreInfoOpenAgris", appInput.moreInfoOpenAgris);
        response.setRenderParameter("numResourceOpenAgrisFromDetails", appInput.numResourceOpenAgrisFromDetails);

    }

    public void handlerTabCulturaItalia(
            ActionRequest request, 
            ActionResponse response, 
            App_Input appInput, 
            int numberRecords, 
            PortletPreferences portletPreferences,            
            int TimeOut) 
            
            throws MalformedQueryException, QueryEvaluationException, 
                   UnsupportedEncodingException, MalformedURLException, 
                   RepositoryException, IOException
    {
              
        System.out.println("CULTURA ITALIA OK");
        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {

            String CulturaItaliaEndPoint = portletPreferences.getValue("CulturaItaliaEndPoint", "");
            
            System.out.println("CULTURA ITALIA ENDPOINT OK---->" + CulturaItaliaEndPoint);
            QueryCulturaItalia.ConnectionToCulturaItalia(CulturaItaliaEndPoint);
            //Se è la prima ricerca
            if (appInput.numberPageCulturaItalia == null || 
                appInput.numberPageCulturaItalia == "") 
            {
                selected_pageCulturaItalia = "1";

                //eseguo la query per prendere le prime 20 risorse
                /*ArrayList culturaItaliaResourceList = 
                        QueryCulturaItalia
                        .queryCulturaItaliaResource(
                        appInput.search_word, 
                        selected_pageCulturaItalia, 
                        numberRecords,CulturaItaliaEndPoint);*/
                
                  ArrayList culturaItaliaResourceList=executeCommandQuery( 
                        appInput.search_word, 
                        selected_pageCulturaItalia, 
                        numberRecords,
                        CulturaItaliaEndPoint,
                        TimeOut,
                        "TimeOutCulturaItalia.jar");
                
                  //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                  ArrayList culturaItaliaTitleList = new ArrayList();
                  ArrayList culturaItaliaTypeList = new ArrayList();
                  ArrayList culturaItaliaAuthorList = new ArrayList();
                  ArrayList culturaItaliaDescriptionList = new ArrayList();
                  
                  for (int i = 0; i < culturaItaliaResourceList.size(); i++) {
                    String resource = culturaItaliaResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    //String listTempTitle = "", listTempType = "", listTempAuthor = "", listTempDescription = "";
                    String listTempTitle = QueryCulturaItalia.getTitle(resource);
                    String listTempType = QueryCulturaItalia.getType(resource);
                    String listTempAuthor = QueryCulturaItalia.getAuthors(resource);
                    String listTempDescription = QueryCulturaItalia.getDescription(resource);                    

                    //aggiungo le proprietà  in appositi ArrayList
                    culturaItaliaTitleList.add(listTempTitle);
                    culturaItaliaTypeList.add(listTempType);
                    culturaItaliaAuthorList.add(listTempAuthor);
                    culturaItaliaDescriptionList.add(listTempDescription);
                }

                sArrayCulturaItalia = (String[]) culturaItaliaResourceList.toArray(new String[culturaItaliaResourceList.size()]);
                sArrayCulturaItaliaTitle = (String[]) culturaItaliaTitleList.toArray(new String[culturaItaliaTitleList.size()]);
                sArrayCulturaItaliaType = (String[]) culturaItaliaTypeList.toArray(new String[culturaItaliaTypeList.size()]);
                sArrayCulturaItaliaAuthor = (String[]) culturaItaliaAuthorList.toArray(new String[culturaItaliaAuthorList.size()]);
                sArrayCulturaItaliaDescription = (String[]) culturaItaliaDescriptionList.toArray(new String[culturaItaliaDescriptionList.size()]);

            } else {

                selected_pageCulturaItalia = appInput.numberPageCulturaItalia;
                moreResourceCulturaItalia = appInput.moreResourceCulturaItalia;

                if (appInput.moreResourceCulturaItalia.equals("OK")) {

                    /*ArrayList newArray = QueryCulturaItalia
                            .queryCulturaItaliaResource(
                            appInput.search_word, 
                            selected_pageCulturaItalia, 
                            numberRecords,CulturaItaliaEndPoint);*/
                    
                     ArrayList newArray=executeCommandQuery(
                        appInput.search_word, 
                        selected_pageCulturaItalia,
                        numberRecords,
                        CulturaItaliaEndPoint,
                        TimeOut,
                        "TimeOutCulturaItalia.jar");
                    
                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayType = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();

                    for (int i = 0; i < newArray.size(); i++) 
                    {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta 
                        // all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();

                        //culturaItaliaResourceList.add(resource);

                        //Per ogni nuova risorsa calcolo le proprietà che 
                        // verranno aggiunte all'array di quelle già presenti
                        //String listTempTitle = "", listTempType = "", listTempAuthor = "", listTempDescription = "";
                        
                        String listTempTitle = QueryCulturaItalia.getTitle(resource);
                        String listTempType = QueryCulturaItalia.getType(resource);
                        String listTempAuthor = QueryCulturaItalia.getAuthors(resource);
                        String listTempDescription = QueryCulturaItalia.getDescription(resource);
                        
                        //aggiungo le proprietà  in appositi ArrayList
                        newArrayTitle.add(listTempTitle);
                        newArrayType.add(listTempType);
                        newArrayAuthor.add(listTempAuthor);
                        newArrayDesc.add(listTempDescription);
                    }

                    sArrayCulturaItalia = (String[]) newArray.toArray(new String[newArray.size()]);
                    sArrayCulturaItaliaTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayCulturaItaliaType = (String[]) newArrayType.toArray(new String[newArrayType.size()]);
                    sArrayCulturaItaliaAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayCulturaItaliaDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                }
            }
        }

        response.setRenderParameter("selected_pageCulturaItalia", selected_pageCulturaItalia);
        response.setRenderParameter("arrayCulturaItaliaResource", sArrayCulturaItalia);
        response.setRenderParameter("arrayCulturaItaliaTitle", sArrayCulturaItaliaTitle);
        response.setRenderParameter("arrayCulturaItaliaType", sArrayCulturaItaliaType);
        response.setRenderParameter("arrayCulturaItaliaAuthor", sArrayCulturaItaliaAuthor);
        response.setRenderParameter("arrayCulturaItaliaDescription", sArrayCulturaItaliaDescription);
        response.setRenderParameter("moreResourceCulturaItalia", moreResourceCulturaItalia);
        response.setRenderParameter("moreInfoCulturaItalia", appInput.moreInfoCulturaItalia);
        response.setRenderParameter("numResourceCulturaItaliaFromDetails", appInput.numResourceCulturaItaliaFromDetails);

    }

    public void handlerTabEuropeana(ActionRequest request, 
                                    ActionResponse response, 
                                    App_Input appInput, 
                                    int numberRecords, 
                                    PortletPreferences portletPreferences,
                                    int TimeOut) 
            throws MalformedQueryException, QueryEvaluationException, 
                   UnsupportedEncodingException, MalformedURLException, 
                   RepositoryException, IOException 
    {

        System.out.println("EUROPEANA OK");
        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {
            
            String EuropeanaEndPoint = portletPreferences.getValue("EuropeanaEndPoint", "");
            QueryEuropeana.ConnectionToEuropeana(EuropeanaEndPoint);

            System.out.println("EUROPEANA OK ----> " + EuropeanaEndPoint);

            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPageEuropeana == null || 
                 appInput.numberPageEuropeana == "")) 
            {

                selected_pageEuropeana = "1";

                //eseguo la query per prendere le prime 20 risorse
                /*ArrayList europeanaResourceList = 
                        QueryEuropeana.queryEuropeanaResource(
                        appInput.search_word, 
                        selected_pageEuropeana, 
                        numberRecords,
                        EuropeanaEndPoint);*/
                
                ArrayList europeanaResourceList = executeCommandQuery(
                        appInput.search_word, 
                        selected_pageEuropeana, 
                        numberRecords,
                        EuropeanaEndPoint,
                        TimeOut,
                        "TimeOutEuropeana.jar");
                
                //System.out.println("PAROLA SELEZIONATA EUROPEANA " + appInput.search_word);
                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList europeanaTitleList = new ArrayList();
                ArrayList europeanaAuthorList = new ArrayList();
                ArrayList europeanaDescriptionList = new ArrayList();
                ArrayList europeanaIdentifierList = new ArrayList();
                ArrayList europeanaTypeList = new ArrayList();

                // System.out.println("DIM di europeanaResourceList " + europeanaResourceList.size());

                for (int i = 0; i < europeanaResourceList.size(); i++) {
                    //System.out.println("sono in europenana list array");
                    String resource = europeanaResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitleEuropeana = QueryEuropeana.getEuropeanaTitle(resource);
                    String listTempAuthorsEuropeana = QueryEuropeana.getEuropeanaAuthors(resource);
                    String listTempDescriptionEuropeana = QueryEuropeana.getEuropeanaDescription(resource);
                    String listTempTypeEuropeana = QueryEuropeana.getEuropeanaType(resource);
                    String listTempIdentifierEuropeana = QueryEuropeana.getEuropeanaIdentifier(resource);

                    //aggiungo le proprietà  in appositi ArrayList
                    europeanaTitleList.add(listTempTitleEuropeana);
                    europeanaAuthorList.add(listTempAuthorsEuropeana);
                    europeanaDescriptionList.add(listTempDescriptionEuropeana);
                    europeanaIdentifierList.add(listTempIdentifierEuropeana);
                    europeanaTypeList.add(listTempTypeEuropeana);
                }

                sArrayEuropeana = (String[]) europeanaResourceList.toArray(new String[europeanaResourceList.size()]);
                sArrayEuropeanaTitle = (String[]) europeanaTitleList.toArray(new String[europeanaTitleList.size()]);
                sArrayEuropeanaAuthor = (String[]) europeanaAuthorList.toArray(new String[europeanaAuthorList.size()]);
                sArrayEuropeanaDescription = (String[]) europeanaDescriptionList.toArray(new String[europeanaDescriptionList.size()]);
                sArrayEuropeanaIdentifier = (String[]) europeanaIdentifierList.toArray(new String[europeanaIdentifierList.size()]);
                sArrayEuropeanaType = (String[]) europeanaTypeList.toArray(new String[europeanaTypeList.size()]);

            } else {

                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pageEuropeana = appInput.numberPageEuropeana;
                moreResourceEuropeana = appInput.moreResourceEuropeana;
                //se è non stato cliccato More Resource in CHAIN non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                if (appInput.moreResourceEuropeana.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array dell e risorse 
                    //in base alla pagina cliccata

                    /*ArrayList newArray = 
                            QueryEuropeana.queryEuropeanaResource(
                            appInput.search_word, 
                            selected_pageEuropeana, 
                            numberRecords,
                            EuropeanaEndPoint);*/
                    
                    ArrayList newArray = 
                            executeCommandQuery(
                            appInput.search_word, 
                            selected_pageEuropeana, 
                            numberRecords,
                            EuropeanaEndPoint,
                            TimeOut,
                            "TimeOutEuropeana.jar");

                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();
                    ArrayList newArrayId = new ArrayList();
                    ArrayList newArrayType = new ArrayList();

                    for (int i = 0; i < newArray.size(); i++) 
                    {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();
                        //Per ogni nuova risorsa 
                        //europeanaResourceList.add(resource);

                        //Per ogni nuova risorsa calcolo le proprietà che verranno aggiunte all'array di quelle già presenti
                        String listTempTitleEuropeana = QueryEuropeana.getEuropeanaTitle(resource);
                        String listTempAuthorsEuropeana = QueryEuropeana.getEuropeanaAuthors(resource);
                        String listTempDescriptionEuropeana = QueryEuropeana.getEuropeanaDescription(resource);
                        String listTempIdentifierEuropeana = QueryEuropeana.getEuropeanaIdentifier(resource);
                        String listTempTypeEuropeana = QueryEuropeana.getEuropeanaType(resource);


                        newArrayTitle.add(listTempTitleEuropeana);
                        newArrayAuthor.add(listTempAuthorsEuropeana);
                        newArrayDesc.add(listTempDescriptionEuropeana);
                        newArrayId.add(listTempIdentifierEuropeana);
                        newArrayType.add(listTempTypeEuropeana);
                    }

                    sArrayEuropeana = (String[]) newArray.toArray(new String[newArray.size()]);
                    sArrayEuropeanaTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayEuropeanaAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayEuropeanaDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                    sArrayEuropeanaIdentifier = (String[]) newArrayId.toArray(new String[newArrayId.size()]);
                    sArrayEuropeanaType = (String[]) newArrayType.toArray(new String[newArrayType.size()]);
                }
            }
        }
        
        response.setRenderParameter("selected_pageEuropeana", selected_pageEuropeana);
        response.setRenderParameter("arrayEuropeanaResource", sArrayEuropeana);
        response.setRenderParameter("arrayEuropeanaTitle", sArrayEuropeanaTitle);
        response.setRenderParameter("arrayEuropeanaAuthor", sArrayEuropeanaAuthor);
        response.setRenderParameter("arrayEuropeanaDescription", sArrayEuropeanaDescription);
        response.setRenderParameter("arrayEuropeanaIdentifier", sArrayEuropeanaIdentifier);
        response.setRenderParameter("arrayEuropeanaType", sArrayEuropeanaType);
        response.setRenderParameter("moreResourceEuropeana", moreResourceEuropeana);
        response.setRenderParameter("moreInfoEuropeana", appInput.moreInfoEuropeana);
        response.setRenderParameter("numResourceEuropeanaFromDetails", appInput.numResourceEuropeanaFromDetails);
    }

    public void handlerTabIsidore(ActionRequest request, 
                                  ActionResponse response, 
                                  App_Input appInput, 
                                  int numberRecords,
                                  PortletPreferences portletPreferences,
                                  int TimeOut) 
            throws MalformedQueryException, QueryEvaluationException, 
                   UnsupportedEncodingException, MalformedURLException, 
                   RepositoryException, IOException 
    {

        System.out.println("ISIDORE OK");

        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {
            String IsidoreEndPoint = portletPreferences.getValue("IsidoreEndPoint", "");
            System.out.println("ISIDORE ENDPOINT-->"+IsidoreEndPoint);
            QueryIsidore.ConnectionToIsidore(IsidoreEndPoint);

            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPageIsidore == null || 
                 appInput.numberPageIsidore == "")) 
            {
                selected_pageIsidore = "1";
                //eseguo la query per prendere le prime 20 risorse

                /*ArrayList isidoreResourceList = QueryIsidore
                        .queryIsidoreResource(appInput.search_word, 
                                              selected_pageIsidore, 
                                              numberRecords,IsidoreEndPoint);*/
                
                ArrayList isidoreResourceList = executeCommandQuery(
                        appInput.search_word, 
                        selected_pageIsidore, 
                        numberRecords,
                        IsidoreEndPoint,
                        TimeOut,
                        "TimeOutIsidore.jar");

                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList isidoreTitleList = new ArrayList();
                ArrayList isidoreAuthorList = new ArrayList();
                ArrayList isidoreDescriptionList = new ArrayList();
                ArrayList isidoreIdentifierList = new ArrayList();
                
                for (int i = 0; i < isidoreResourceList.size(); i++) 
                {

                    String resource = isidoreResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitleIsidore = QueryIsidore.getIsidoreTitle(resource);
                    String listTempAuthorsIsidore = QueryIsidore.getIsidoreAuthors(resource);
                    String listTempDescriptionIsidore = QueryIsidore.getIsidoreDescription(resource);
                    String listTempIdentifierIsidore = QueryIsidore.getIsidoreIdentifier(resource);

                    //aggiungo le proprietà  in appositi ArrayList
                    isidoreTitleList.add(listTempTitleIsidore);
                    isidoreAuthorList.add(listTempAuthorsIsidore);
                    isidoreDescriptionList.add(listTempDescriptionIsidore);
                    isidoreIdentifierList.add(listTempIdentifierIsidore);
                }

                sArrayIsidore = (String[]) isidoreResourceList.toArray(new String[isidoreResourceList.size()]);
                sArrayIsidoreTitle = (String[]) isidoreTitleList.toArray(new String[isidoreTitleList.size()]);
                sArrayIsidoreAuthor = (String[]) isidoreAuthorList.toArray(new String[isidoreAuthorList.size()]);
                sArrayIsidoreDescription = (String[]) isidoreDescriptionList.toArray(new String[isidoreDescriptionList.size()]);
                sArrayIsidoreIdentifier = (String[]) isidoreIdentifierList.toArray(new String[isidoreIdentifierList.size()]);


            } else {

                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pageIsidore = appInput.numberPageIsidore;
                moreResourceIsidore = appInput.moreResourceIsidore;
                //se è non stato cliccato More Resource in CHAIN non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                if (appInput.moreResourceIsidore.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array dell e risorse 
                    //in base alla pagina cliccata
                    
                    /*ArrayList newArray = QueryIsidore
                            .queryIsidoreResource(
                            appInput.search_word, 
                            selected_pageIsidore, 
                            numberRecords,IsidoreEndPoint);*/
                    
                    ArrayList newArray = executeCommandQuery(
                            appInput.search_word, 
                            selected_pageIsidore, 
                            numberRecords,
                            IsidoreEndPoint,
                            TimeOut,
                            "TimeOutIsidore.jar");

                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();
                    ArrayList newArrayId = new ArrayList();

                    for (int i = 0; i < newArray.size(); i++) 
                    {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta 
                        // all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();
                        //Per ogni nuova risorsa 
                        //isidoreResourceList.add(newArray.get(i));

                        //Per ogni nuova risorsa calcolo le proprietà che verranno aggiunte all'array di quelle già presenti
                        String listTempTitleIsidore = QueryIsidore.getIsidoreTitle(resource);
                        String listTempAuthorsIsidore = QueryIsidore.getIsidoreAuthors(resource);
                        String listTempDescriptionIsidore = QueryIsidore.getIsidoreDescription(resource);
                        String listTempIdentifierIsidore = QueryIsidore.getIsidoreIdentifier(resource);

                        newArrayTitle.add(listTempTitleIsidore);
                        newArrayAuthor.add(listTempAuthorsIsidore);
                        newArrayDesc.add(listTempDescriptionIsidore);
                        newArrayId.add(listTempIdentifierIsidore);
                    }

                    sArrayIsidore = (String[]) newArray.toArray(new String[newArray.size()]);
                    sArrayIsidoreTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayIsidoreAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayIsidoreDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                    sArrayIsidoreIdentifier = (String[]) newArrayId.toArray(new String[newArrayId.size()]);
                }
            }
            //converto gli ArrayList in String [] in modo da poter passarli come parametri al jsp
        }

        response.setRenderParameter("selected_pageIsidore", selected_pageIsidore);
        response.setRenderParameter("arrayIsidoreResource", sArrayIsidore);
        response.setRenderParameter("arrayIsidoreTitle", sArrayIsidoreTitle);
        response.setRenderParameter("arrayIsidoreAuthor", sArrayIsidoreAuthor);
        response.setRenderParameter("arrayIsidoreDescription", sArrayIsidoreDescription);
        response.setRenderParameter("arrayIsidoreIdentifier", sArrayIsidoreIdentifier);
        response.setRenderParameter("moreResourceIsidore", moreResourceIsidore);
        response.setRenderParameter("moreInfoIsidore", appInput.moreInfoIsidore);
        response.setRenderParameter("numResourceIsidoreFromDetails", appInput.numResourceIsidoreFromDetails);
    }

    public void handlerTabPubmed(
            ActionRequest request, 
            ActionResponse response, 
            App_Input appInput, 
            int numberRecords,
            PortletPreferences portletPreferences,
            int TimeOut) 
            throws  MalformedQueryException, QueryEvaluationException, 
                    UnsupportedEncodingException, MalformedURLException, 
                    RepositoryException, IOException 
    {

        System.out.println("PUBMED OK");

        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {

             String PubmedEndPoint = portletPreferences.getValue("PubmedEndPoint", "");
             QueryPubMed.ConnectionToPubMed(PubmedEndPoint);

            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPagePubmed == null || 
                 appInput.numberPagePubmed == "")) 
            {
                selected_pagePubmed = "1";
                //eseguo la query per prendere le prime 20 risorse

                /*ArrayList pubmedResourceList = QueryPubMed
                        .queryPubmedResource(
                        appInput.search_word, 
                        selected_pagePubmed, 
                        numberRecords,
                        PubmedEndPoint);*/
                
                ArrayList pubmedResourceList = executeCommandQuery(
                        appInput.search_word, 
                        selected_pagePubmed, 
                        numberRecords,
                        PubmedEndPoint,
                        TimeOut,
                        "TimeOutPubMed.jar");

                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList pubmedTitleList = new ArrayList();
                ArrayList pubmedAuthorList = new ArrayList();
                ArrayList pubmedDescriptionList = new ArrayList();
                ArrayList pubmedURLList = new ArrayList();
                //ArrayList pubmedURIList = new ArrayList();
                
                
                for (int i = 0; i < pubmedResourceList.size(); i++) 
                {                    
                    String resource = pubmedResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitlePubmed = QueryPubMed.getPubmedTitle(resource);
                    String listTempAuthorsPubmed = QueryPubMed.getPubmedAuthors(resource);
                    String listTempDescriptionPubmed = QueryPubMed.getPubmedDescription(resource);
                    String listTempURLPubmed = QueryPubMed.getPubmedURL(resource);
                   // String listTempURIPubmed=QueryPubMed.getPubmedURI(resource);
                    //aggiungo le proprietà  in appositi ArrayList
                    pubmedTitleList.add(listTempTitlePubmed);
                    pubmedAuthorList.add(listTempAuthorsPubmed);
                    pubmedDescriptionList.add(listTempDescriptionPubmed);
                    pubmedURLList.add(listTempURLPubmed);
                   // pubmedURIList.add(listTempURIPubmed);
                }
                
                sArrayPubmed = (String[]) pubmedResourceList.toArray(new String[pubmedResourceList.size()]);
                sArrayPubmedTitle = (String[]) pubmedTitleList.toArray(new String[pubmedTitleList.size()]);
                sArrayPubmedAuthor = (String[]) pubmedAuthorList.toArray(new String[pubmedAuthorList.size()]);
                sArrayPubmedDescription = (String[]) pubmedDescriptionList.toArray(new String[pubmedDescriptionList.size()]);
                sArrayPubmedURL = (String[]) pubmedURLList.toArray(new String[pubmedURLList.size()]);
               // sArrayPubmedURI = (String[]) pubmedURIList.toArray(new String[pubmedURIList.size()]);

            } else {
                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pagePubmed = appInput.numberPagePubmed;
                moreResourcePubmed = appInput.moreResourcePubmed;
                //se è non stato cliccato More Resource in CHAIN non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                if (appInput.moreResourcePubmed.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array dell e risorse 
                    //in base alla pagina cliccata

                    /*ArrayList newArray = QueryPubMed
                            .queryPubmedResource(
                            appInput.search_word, 
                            selected_pagePubmed, 
                            numberRecords,
                            PubmedEndPoint);*/
                    
                    ArrayList newArray = executeCommandQuery(
                            appInput.search_word, 
                            selected_pagePubmed, 
                            numberRecords,
                            PubmedEndPoint,
                            TimeOut,
                            "TimeOutPubMed.jar");

                    ArrayList newArrayTitle = new ArrayList();
                    ArrayList newArrayAuthor = new ArrayList();
                    ArrayList newArrayDesc = new ArrayList();
                    ArrayList newArrayURL = new ArrayList();
                  //  ArrayList newArrayURI = new ArrayList();


                    for (int i = 0; i < newArray.size(); i++) 
                    {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();
                    
                        //Per ogni nuova risorsa calcolo le proprietà che verranno aggiunte all'array di quelle già presenti
                        String listTempTitlePubmed = QueryPubMed.getPubmedTitle(resource);
                        String listTempAuthorsPubmed = QueryPubMed.getPubmedAuthors(resource);
                        String listTempDescriptionPubmed = QueryPubMed.getPubmedDescription(resource);
                        String listTempURLPubmed = QueryPubMed.getPubmedURL(resource);
                       // String listTempURIPubmed=QueryPubMed.getPubmedURI(resource);

                        newArrayTitle.add(listTempTitlePubmed);
                        newArrayAuthor.add(listTempAuthorsPubmed);
                        newArrayDesc.add(listTempDescriptionPubmed);
                        newArrayURL.add(listTempURLPubmed);
                      //  newArrayURI.add(listTempURIPubmed);
                        
                    }
                    sArrayPubmed = (String[]) newArray.toArray(new String[newArray.size()]);
                    sArrayPubmedTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                    sArrayPubmedAuthor = (String[]) newArrayAuthor.toArray(new String[newArrayAuthor.size()]);
                    sArrayPubmedDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                    sArrayPubmedURL = (String[]) newArrayURL.toArray(new String[newArrayURL.size()]);
                  //  sArrayPubmedURI = (String[]) newArrayURI.toArray(new String[newArrayURI.size()]);
                }
            }
            //converto gli ArrayList in String [] in modo da poter passarli come parametri al jsp
        }
        response.setRenderParameter("selected_pagePubmed", selected_pagePubmed);
        response.setRenderParameter("arrayPubmedResource", sArrayPubmed);
        response.setRenderParameter("arrayPubmedTitle", sArrayPubmedTitle);
        response.setRenderParameter("arrayPubmedAuthor", sArrayPubmedAuthor);
        response.setRenderParameter("arrayPubmedDescription", sArrayPubmedDescription);
        response.setRenderParameter("arrayPubmedURL", sArrayPubmedURL);
       // response.setRenderParameter("arrayPubmedURI", sArrayPubmedURI);
        response.setRenderParameter("moreResourcePubmed", moreResourcePubmed);
        response.setRenderParameter("moreInfoPubmed", appInput.moreInfoPubmed);
        response.setRenderParameter("numResourcePubmedFromDetails", appInput.numResourcePubmedFromDetails);
    }
    
    
    public void handlerTabDBPedia(
            ActionRequest request, 
            ActionResponse response, 
            App_Input appInput, 
            int numberRecords,
            PortletPreferences portletPreferences,
            int TimeOut) 
            throws  MalformedQueryException, QueryEvaluationException, 
                    UnsupportedEncodingException, MalformedURLException, 
                    RepositoryException, IOException 
    {

        System.out.println("DBPedia OK");

        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")
         && !appInput.moreInfoDBPedia.equals("OK")) 
        {

             String DBPediaEndPoint = portletPreferences.getValue("DBPediaEndPoint", "");
             QueryDBPedia.ConnectionToDBPedia(DBPediaEndPoint);

            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPageDBPedia == null || 
                 appInput.numberPageDBPedia == "")) 
            {
                selected_pageDBPedia = "1";
                //eseguo la query per prendere le prime 20 risorse

                /*ArrayList pubmedResourceList = QueryPubMed
                        .queryPubmedResource(
                        appInput.search_word, 
                        selected_pagePubmed, 
                        numberRecords,
                        PubmedEndPoint);*/
                
                ArrayList dbpediaResourceList = executeCommandQuery(
                        appInput.search_word, 
                        selected_pageDBPedia, 
                        numberRecords,
                        DBPediaEndPoint,
                        TimeOut,
                        "TimeOutDBPedia.jar");

                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList dbPediaTitleList = new ArrayList();
                
                ArrayList dbPediaDescriptionList = new ArrayList();
                
                
                
                for (int i = 0; i < dbpediaResourceList.size(); i++) 
                {                    
                    String resource = dbpediaResourceList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitle = QueryDBPedia.getTitle(resource);
                    dbPediaTitleList.add(listTempTitle);
                    //String listTempAuthorsPubmed = QueryPubMed.getPubmedAuthors(resource);
                    String listTempDescription = QueryDBPedia.getDescription(resource);
                    dbPediaDescriptionList.add(listTempDescription);
                    //String listTempURLPubmed = QueryPubMed.getPubmedURL(resource);
                   // String listTempURIPubmed=QueryPubMed.getPubmedURI(resource);
                    //aggiungo le proprietà  in appositi ArrayList
                   
                    
                }
                
                sArrayDBPedia = (String[]) dbpediaResourceList.toArray(new String[dbpediaResourceList.size()]);
                System.out.println("DBPEDIA ARRAY-->"+sArrayDBPedia.length);
                sArrayDBPediaTitle = (String[]) dbPediaTitleList.toArray(new String[dbPediaTitleList.size()]);
                //sArrayPubmedAuthor = (String[]) pubmedAuthorList.toArray(new String[pubmedAuthorList.size()]);
                sArrayDBPediaDescription = (String[]) dbPediaDescriptionList.toArray(new String[dbPediaDescriptionList.size()]);
               // sArrayPubmedURL = (String[]) pubmedURLList.toArray(new String[pubmedURLList.size()]);
               // sArrayPubmedURI = (String[]) pubmedURIList.toArray(new String[pubmedURIList.size()]);

            } else {
                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pageDBPedia = appInput.numberPageDBPedia;
                moreResourceDBPedia = appInput.moreResourceDBPedia;
                //se è non stato cliccato More Resource in CHAIN non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                if (appInput.moreResourceDBPedia.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array dell e risorse 
                    //in base alla pagina cliccata

                    //ArrayList newArray = QueryPubMed
                     //       .queryPubmedResource(
                     //       appInput.search_word, 
                     //       selected_pagePubmed, 
                     //       numberRecords,
                     //       PubmedEndPoint);
                    
                    ArrayList newArray = executeCommandQuery(
                            appInput.search_word, 
                            selected_pageDBPedia, 
                            numberRecords,
                            DBPediaEndPoint,
                            TimeOut,
                            "TimeOutDBPedia.jar");

                    ArrayList newArrayTitle = new ArrayList();
                   
                    ArrayList newArrayDesc = new ArrayList();
                   


                    for (int i = 0; i < newArray.size(); i++) 
                    {
                        //Ogni risorsa di questo nuovo Array  viene aggiunta all'array già definito delle risorse openAgrisResourceList
                        String resource = newArray.get(i).toString();
                    
                        //Per ogni nuova risorsa calcolo le proprietà che verranno aggiunte all'array di quelle già presenti
                        String listTempTitleDBPedia = QueryDBPedia.getTitle(resource);
                       
                        String listTempDescriptionDBPedia = QueryDBPedia.getDescription(resource);
                      

                        newArrayTitle.add(listTempTitleDBPedia);
                        
                        newArrayDesc.add(listTempDescriptionDBPedia);
                       
                        
                    }
                    sArrayDBPedia = (String[]) newArray.toArray(new String[newArray.size()]);
                    
                    System.out.println("DBPEDIA ARRAY-->"+sArrayDBPedia.length);
                    sArrayDBPediaTitle = (String[]) newArrayTitle.toArray(new String[newArrayTitle.size()]);
                
                sArrayDBPediaDescription = (String[]) newArrayDesc.toArray(new String[newArrayDesc.size()]);
                }
            }
            //converto gli ArrayList in String [] in modo da poter passarli come parametri al jsp
        }
        response.setRenderParameter("selected_pageDBPedia", selected_pageDBPedia);
        response.setRenderParameter("arrayDBPediaResource", sArrayDBPedia);
        response.setRenderParameter("arrayDBPediaTitle", sArrayDBPediaTitle);
        
        response.setRenderParameter("arrayDBPediaDescription", sArrayDBPediaDescription);
       
        response.setRenderParameter("moreResourceDBPedia", moreResourceDBPedia);
        response.setRenderParameter("moreInfoDBPedia", appInput.moreInfoDBPedia);
        response.setRenderParameter("numResourceDBPediaFromDetails", appInput.numResourceDBPediaFromDetails);
    }
    
    
    

    public void handlerTabEngage(
            ActionRequest request, 
            ActionResponse response, 
            App_Input appInput, 
            int numberRecords,
            PortletPreferences portletPreferences,
            int TimeOut) 
            throws MalformedQueryException, QueryEvaluationException, 
                   UnsupportedEncodingException, MalformedURLException, 
                   RepositoryException, IOException 
    {
        System.out.println("Engare OK");

        if (!appInput.moreInfo.equals("OK") 
         && !appInput.moreInfoOpenAgris.equals("OK")
         && !appInput.moreInfoCulturaItalia.equals("OK") 
         && !appInput.moreInfoEuropeana.equals("OK")
         && !appInput.moreInfoIsidore.equals("OK") 
         && !appInput.moreInfoPubmed.equals("OK")
         && !appInput.moreInfoEngage.equals("OK")) 
        {

            String EngageEndPoint = portletPreferences.getValue("EngageEndPoint", "");
            QueryEngage.ConnectionToEngage(EngageEndPoint);

            //se è la prima ricerca quindi siamo a pagina1
            if ((appInput.numberPageEngage == null || 
                 appInput.numberPageEngage == "")) 
            {
//                System.out.println("PAROLA SELEZIONATA ISIDORE  " + appInput.search_word);
//                System.out.println("ISIDORE appInput.Isidore == null");
                selected_pageEngage = "1";
                //eseguo la query per prendere le prime 20 risorse

                /*ArrayList engageResourceHomepageList = QueryEngage
                        .queryEngageResourceFromHomepage(
                        appInput.search_word, 
                        selected_pageEngage, 
                        numberRecords,EngageEndPoint);*/
                
                ArrayList engageResourceHomepageList = executeCommandQuery(
                        appInput.search_word, 
                        selected_pageEngage, 
                        numberRecords,EngageEndPoint,
                        TimeOut,
                        "TimeOutEngage.jar");

                //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                ArrayList engageTitleList = new ArrayList();
                ArrayList engageAuthorList = new ArrayList();
                ArrayList engageDescriptionList = new ArrayList();
                ArrayList engageHomePageList = new ArrayList();
                
                for (int i = 0; i < engageResourceHomepageList.size(); i++) 
                {

                    String homepageResourceEngage = engageResourceHomepageList.get(i).toString();
                    //per ogni risorsa eseguo le query per le sue prorietà

                    //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                    String listTempTitleEngage = QueryEngage.getEngageTitle(homepageResourceEngage);
                    String listTempAuthorsEngage = QueryEngage.getEngageAuthors(homepageResourceEngage);
                    String listTempDescriptionEngage = QueryEngage.getEngageDescription(homepageResourceEngage);
                    // String listTempHomepageEngage = QueryEngage.getEngageHomepage(homepageResourceEngage);

                    //aggiungo le proprietà  in appositi ArrayList
                    engageTitleList.add(listTempTitleEngage);
                    engageAuthorList.add(listTempAuthorsEngage);
                    engageDescriptionList.add(listTempDescriptionEngage);
                    engageHomePageList.add(homepageResourceEngage);
                }

                sArrayEngage = (String[]) engageResourceHomepageList.toArray(new String[engageResourceHomepageList.size()]);
                sArrayEngageTitle = (String[]) engageTitleList.toArray(new String[engageTitleList.size()]);
                sArrayEngageAuthor = (String[]) engageAuthorList.toArray(new String[engageAuthorList.size()]);
                sArrayEngageDescription = (String[]) engageDescriptionList.toArray(new String[engageDescriptionList.size()]);
                sArrayEngageHomepage = (String[]) engageHomePageList.toArray(new String[engageHomePageList.size()]);

            } else {

                // se non siamo a pagina 1 e quindi è stato cliccato More Resource in OpenAgris,
                //oppure è stato cliccato More Resource in un qualche tab e quindi appInput.numberPageOpenAgris != null
                selected_pageEngage = appInput.numberPageEngage;
                moreResourceEngage = appInput.moreResourceEngage;
                //se è non stato cliccato More Resource in CHAIN non devo ricalcolare niente perchè la pagina di OpenAgris deve 
                //rimanere immutata
                if (appInput.moreResourceEngage.equals("OK")) 
                {
                    //Se invece è stato cliccato More Resource in OpenAgris devo calcore il nuovo array dell e risorse 
                    //in base alla pagina cliccata

                    /*ArrayList engageResourceHomepageList = QueryEngage
                            .queryEngageResourceFromHomepage(
                            appInput.search_word, 
                            selected_pageEngage, 
                            numberRecords,EngageEndPoint);*/
                    
                    ArrayList engageResourceHomepageList = executeCommandQuery(
                            appInput.search_word, 
                            selected_pageEngage, 
                            numberRecords,EngageEndPoint,
                            TimeOut,
                            "TimeOutEngage.jar");

                    //istanzio gli arraylist per le proprietà delle risorse (title,author,description...)
                    ArrayList engageTitleList = new ArrayList();
                    ArrayList engageAuthorList = new ArrayList();
                    ArrayList engageDescriptionList = new ArrayList();
                    ArrayList engageHomePageList = new ArrayList();
                    
                    for (int i = 0; i < engageResourceHomepageList.size(); i++) 
                    {

                        String homepageResourceEngage = engageResourceHomepageList.get(i).toString();
                        //per ogni risorsa eseguo le query per le sue prorietà

                        //le proprietà sono una singola stringa perchè i vari titoli vengono concatenati con ##
                        String listTempTitleEngage = QueryEngage.getEngageTitle(homepageResourceEngage);
                        String listTempAuthorsEngage = QueryEngage.getEngageAuthors(homepageResourceEngage);
                        String listTempDescriptionEngage = QueryEngage.getEngageDescription(homepageResourceEngage);
                        // String listTempHomepageEngage = QueryEngage.getEngageHomepage(homepageResourceEngage);                        

                        //aggiungo le proprietà  in appositi ArrayList
                        engageTitleList.add(listTempTitleEngage);
                        engageAuthorList.add(listTempAuthorsEngage);
                        engageDescriptionList.add(listTempDescriptionEngage);
                        engageHomePageList.add(homepageResourceEngage);
                    }

                    sArrayEngage = (String[]) engageResourceHomepageList.toArray(new String[engageResourceHomepageList.size()]);
                    sArrayEngageTitle = (String[]) engageTitleList.toArray(new String[engageTitleList.size()]);
                    sArrayEngageAuthor = (String[]) engageAuthorList.toArray(new String[engageAuthorList.size()]);
                    sArrayEngageDescription = (String[]) engageDescriptionList.toArray(new String[engageDescriptionList.size()]);
                    sArrayEngageHomepage = (String[]) engageHomePageList.toArray(new String[engageHomePageList.size()]);
                }
            }

        }

        response.setRenderParameter("selected_pageEngage", selected_pageEngage);
        response.setRenderParameter("arrayEngageResource", sArrayEngage);
        response.setRenderParameter("arrayEngageTitle", sArrayEngageTitle);
        response.setRenderParameter("arrayEngageAuthor", sArrayEngageAuthor);
        response.setRenderParameter("arrayEngageDescription", sArrayEngageDescription);
        response.setRenderParameter("arrayEngageHomepage", sArrayEngageHomepage);
        response.setRenderParameter("moreResourceEngage", moreResourceEngage);
        response.setRenderParameter("moreInfoEngage", appInput.moreInfoEngage);
        response.setRenderParameter("numResourceEngageFromDetails", appInput.numResourceEngageFromDetails);
    }

    public void setNotNullInputParameter(App_Input appInput) {

        //*****CHAIN******
        if (appInput.moreResourceCHAIN == null) {
            appInput.moreResourceCHAIN = "NO";
            moreResourceCHAIN = appInput.moreResourceCHAIN;
        }
        if (appInput.moreInfo == null) {
            appInput.moreInfo = "NO";
        }
        if (appInput.numResourceFromDetails == null) {
            appInput.numResourceFromDetails = "0";
        }

        //OPENAGRIS
        if (appInput.moreResourceOpenAgris == null) {
            appInput.moreResourceOpenAgris = "NO";
            moreResourceOpenAgris = appInput.moreResourceOpenAgris;
        }
        if (appInput.moreInfoOpenAgris == null) {
            appInput.moreInfoOpenAgris = "NO";
        }

        if (appInput.numResourceOpenAgrisFromDetails == null) {
            appInput.numResourceOpenAgrisFromDetails = "0";
        }

        //CULTURA ITALIA
        if (appInput.moreResourceCulturaItalia == null) {
            appInput.moreResourceCulturaItalia = "NO";
            moreResourceCulturaItalia = appInput.moreResourceCulturaItalia;
        }
        if (appInput.moreInfoCulturaItalia == null) {
            appInput.moreInfoCulturaItalia = "NO";
        }

        if (appInput.numResourceCulturaItaliaFromDetails == null) {
            appInput.numResourceCulturaItaliaFromDetails = "0";
        }

        //EUROPEANA
        if (appInput.moreResourceEuropeana == null) {
            appInput.moreResourceEuropeana = "NO";
            moreResourceEuropeana = appInput.moreResourceEuropeana;
        }
        if (appInput.moreInfoEuropeana == null) {
            appInput.moreInfoEuropeana = "NO";
        }

        if (appInput.numResourceEuropeanaFromDetails == null) {
            appInput.numResourceEuropeanaFromDetails = "0";
        }

        //ISIDORE
        if (appInput.moreResourceIsidore == null) {
            appInput.moreResourceIsidore = "NO";
            moreResourceIsidore = appInput.moreResourceIsidore;
        }
        if (appInput.moreInfoIsidore == null) {
            appInput.moreInfoIsidore = "NO";
        }

        if (appInput.numResourceIsidoreFromDetails == null) {
            appInput.numResourceIsidoreFromDetails = "0";
        }
        //PUBMED
        if (appInput.moreResourcePubmed == null) {
            appInput.moreResourcePubmed = "NO";
            moreResourcePubmed = appInput.moreResourcePubmed;
        }
        if (appInput.moreInfoPubmed == null) {
            appInput.moreInfoPubmed = "NO";
        }

        if (appInput.numResourcePubmedFromDetails == null) {
            appInput.numResourcePubmedFromDetails = "0";
        }
        
        //DBPEDIA
        if (appInput.moreResourceDBPedia == null) {
            appInput.moreResourceDBPedia = "NO";
            moreResourceDBPedia = appInput.moreResourceDBPedia;
        }
        if (appInput.moreInfoDBPedia == null) {
            appInput.moreInfoDBPedia = "NO";
        }

        if (appInput.numResourceDBPediaFromDetails == null) {
            appInput.numResourceDBPediaFromDetails = "0";
        }

        //ENGAGE
        if (appInput.moreResourceEngage == null) {
            appInput.moreResourceEngage = "NO";
            moreResourceEngage = appInput.moreResourceEngage;
        }
        if (appInput.moreInfoEngage == null) {
            appInput.moreInfoEngage = "NO";
        }

        if (appInput.numResourceEngageFromDetails == null) {
            appInput.numResourceEngageFromDetails = "0";
        }



    }

    public void doGet(final ActionRequest request, 
                      final ActionResponse response, 
                      final App_Input appInput, 
                      final int numberRecords,
                      final PortletPreferences portletPreferences,
                      final int TimeOut) 
    {
        testLookup();
        int numThread = countTab(portletPreferences);
        System.out.println("About to submit tasks to " + tp);        
        // PortletPreferences portletPreferences = request.getPreferences();
        System.out.println("PREF DBPEDIA-->"+portletPreferences.getValue("DBPedia", ""));
        final Semaphore s = new Semaphore(0);
        Thread thread_openAgris = null;
        Thread thread_culturaItalia = null;
        Thread thread_engage = null;
        Thread thread_europeana = null;
        Thread thread_isidore = null;
        Thread thread_pubmed = null;
        Thread thread_dbpedia = null;
        Thread thread_chain = new Thread("CHAIN_THREAD") {
            @Override
            public void run() {
                
                    System.out.println("Executing task in " + Thread.currentThread());

                    System.out.println("################### init_thread chain");
                    try {
                        handlerTabCHAIN(request, response, appInput, numberRecords);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    s.release();
                    System.out.println("thread_chain isAlive: "+Thread.currentThread().getName()+"---"+ Thread.currentThread().isAlive());
                    System.out.println("###################### finish thread chain"+ Thread.currentThread().isAlive());               
            }
        };

        if ( portletPreferences.getValue("OpenAgris", "").equals("true")) 
        {
            //  if (appPreferences.OpenAgris.equals("true")) {
            thread_openAgris = new Thread("OPENAGRIS_THREAD") {
                
                @Override
                public void run() {
                                      
                    System.out.println("Executing task in " + Thread.currentThread());                   
                    System.out.println("################### init_thread OpenAgris");
                    
                    try {
                        try {
                            handlerTabOpenAgris(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                        } catch (IOException ex) {
                            Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } 

                    s.release();
                    System.out.println("###################### finish thread OpenAgris");
                }
            };
        }

        if ( portletPreferences.getValue("CulturaItalia", "").equals("true")) 
        {
            //  if (appPreferences.CulturaItalia.equals("true")) {
            thread_culturaItalia = new Thread("CULTURAITALIA_THREAD") {

                @Override
                public void run() {
                    
                    System.out.println("Executing task in " + Thread.currentThread());
                    System.out.println("################### init_thread CulturaItalia");
                    
                    try {
                        try {
                            handlerTabCulturaItalia(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                        } catch (IOException ex) {
                            Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread CulturaItalia");
                }
            };
        }

        if (portletPreferences.getValue("Engage", "").equals("true")) 
        {
            //  if (appPreferences.Engage.equals("true")) {
            thread_engage = new Thread("ENGAGE_THREAD") {

                @Override
                public void run() {

                    System.out.println("Executing task in " + Thread.currentThread());


                    System.out.println("################### init_thread Engage");
                    try {
                        handlerTabEngage(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread Engage");

                }
            };
        }
        
        if (portletPreferences.getValue("Europeana", "").equals("true")) 
        {        
            thread_europeana = new Thread("EUROPEANA_THREAD") {

                @Override
                public void run() {
                    
                    System.out.println("Executing task in " + Thread.currentThread());
                    System.out.println("################### init_thread Europeana");
                    
                    try {
                        handlerTabEuropeana(request, response, appInput, numberRecords, portletPreferences, TimeOut);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread europeana");
                }
            };
        }

        if (portletPreferences.getValue("Isidore", "").equals("true")) {
        //if (appPreferences.Isidore.equals("true")) {
            thread_isidore = new Thread("ISIDORE_THREAD") {

                @Override
                public void run() {

                    System.out.println("Executing task in " + Thread.currentThread());
                    System.out.println("################### init_thread Isidore");
                    
                    try {
                        handlerTabIsidore(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread Isidore");
                }
            };
        }

        if (portletPreferences.getValue("Pubmed", "").equals("true")) {
        //if (appPreferences.Pubmed.equals("true")) {
            thread_pubmed = new Thread("PUBMED_THREAD") {

                @Override
                public void run() {

                    System.out.println("Executing task in " + Thread.currentThread());
                    System.out.println("################### init_thread Pubmed");
                    
                    try {
                        handlerTabPubmed(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread Pubmed");
                }
            };
        }
        
        if (portletPreferences.getValue("DBPedia", "").equals("true")) {
        //if (appPreferences.Pubmed.equals("true")) {
            thread_dbpedia = new Thread("DBPEDIA_THREAD") {

                @Override
                public void run() {

                    System.out.println("Executing task in " + Thread.currentThread());
                    System.out.println("################### init_thread DBPedia");
                    
                    try {
                        handlerTabDBPedia(request, response, appInput, numberRecords,portletPreferences, TimeOut);
                    } catch (MalformedQueryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (QueryEvaluationException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RepositoryException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    s.release();

                    System.out.println("###################### finish thread DBPedia");
                }
            };
        }
        
        
        if (tp != null) {
            tp.execute(thread_chain);
            if (thread_openAgris != null) {
                tp.execute(thread_openAgris);
            }
            if (thread_culturaItalia != null) {
                tp.execute(thread_culturaItalia);
            }
            if (thread_engage != null) {
                tp.execute(thread_engage);
            }
            if (thread_europeana != null) {
                tp.execute(thread_europeana);
                
            }
            if (thread_isidore != null) {
                tp.execute(thread_isidore);
            }
            if (thread_pubmed != null) {
                tp.execute(thread_pubmed);
            }
             if (thread_dbpedia != null) {
                tp.execute(thread_dbpedia);
            }


            try {
                s.acquire(numThread);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelSemanticSearch_portlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           
                thread_chain.start();
        }
        
        //tp.shutdown();
       //while (!tp.isTerminated()) {}
        System.out.println("###################### finish threadPoolMio");
    }

    private void testLookup() {
    //ThreadPoolExecutor tp=null;
        try {
            
             tp = InitialContext.<ThreadPoolExecutor>doLookup("SemanticSearch-Pool");//("concurrency/TP");SemanticSearch-Pool
            // tp = InitialContext.<ThreadPoolExecutor>doLookup("concurrency/TP");
            System.out.println("ThreadPoolExecutor from lookup: " + tp);

        } catch (NamingException e) {
            System.out.println("Exception: error in thread-pool inizialization! " + e.toString());
        }
       
    }

    public int countTab(PortletPreferences pref) {
        int count = 1;


        if(pref.getValue("OpenAgris", "").equals("true")){
        //if (appPreferences.OpenAgris.equals("true")) {
            count = count + 1;

        }

        if(pref.getValue("CulturaItalia", "").equals("true")){
        //if (appPreferences.CulturaItalia.equals("true")) {
            count = count + 1;

        }

         if(pref.getValue("Europeana", "").equals("true")){
        //if (appPreferences.Europeana.equals("true")) {
            count = count + 1;

        }

       if(pref.getValue("Isidore", "").equals("true")){  
      //  if (appPreferences.Isidore.equals("true")) {
            count = count + 1;

        }


       if(pref.getValue("Engage", "").equals("true")){  
       // if (appPreferences.Engage.equals("true")) {
            count = count + 1;

        }

       if(pref.getValue("Pubmed", "").equals("true")){  
        //if (appPreferences.Pubmed.equals("true")) {
            count = count + 1;

        }
         if(pref.getValue("DBPedia", "").equals("true")){  
        //if (appPreferences.Pubmed.equals("true")) {
            count = count + 1;

        }

        // System.out.println("TOT COUNT TAB=>>>>>>>>>>> "+count);

        return count;
    }

    private String[] executeCommand(String title) {


        System.out.println("TITLE ANALIZE: " + title);

        String[] command = new String[]{
            "python", appServerPath 
                + "/WEB-INF/job/scholar.py", 
                "-c 1", 
                "--phrase", 
                title};


//          System.out.println("*********COMMAND*********** "); 
//          for (int j=0;j<command.length;j++)
//          System.out.print(command[j]);
//         System.out.println("********* END COMMAND*********** "); 

        String[] info_GS = new String[6];
        String url = "";
        String versions = "";
        String versions_list = "";
        String citations = "";
        String citations_list = "";
        String year = "";

        Process p;
        boolean control = false;
        
        try {

            p = Runtime.getRuntime().exec(command);

            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";

            condition:
            while ((line = reader.readLine()) != null) {

                //System.out.println("ECCO: "+line.split(" ")[0]);
                control = true;
                System.out.println("LINE: " + line);

                if (line.contains("Title")) {
                    String title_GS = line.split("Title ")[1];
                    //System.out.println("Title_GS: " + title_GS);

                    String newTitle_GS = title_GS.toUpperCase()
                            .replace(" ", "")
                            .replace("'", "")
                            .replace("?", "")
                            .replace(".", "");

//                    char c1 = '%u2019';
//                    for (int i = 0; i < title.length(); i++) {
//                        char l = title.charAt(i);
//                        System.out.println("CHAR: " + l);
//                        if (l == c1) {
//                            System.out.println("BECCATO APICE");
//                        }
//
//                    }
                    String tt1 = new String(title.getBytes("ISO-8859-1"), "UTF-8");
                    // System.out.println("******TTTTTT UTF8: "+tt1);

                    String newTitle_CHAIN = tt1.toUpperCase()
                            .replace(" ", "")
                            .replace("'", "")
                            .replace("?", "")
                            .replace(".", "");
                    
                    System.out.println("Title_GS: " 
                            + newTitle_GS 
                            + "\nTITLE_CH: " 
                            + newTitle_CHAIN);

                    if (!newTitle_GS.equals(newTitle_CHAIN)) {

                        System.out.println("*******TITOLO NOT FOUND IN GOOGLE SCHOLAR********");

                        control = false;
                        break condition;
                    }
                }
                
                if (line.contains("URL")) {
                    url = line.split("URL ")[1];
                    System.out.println("URL: " + url);
                    info_GS[0] = url;
                }

                if (line.contains("Versions") && !line.contains("Versions list")) {
                    versions = line.split("Versions ")[1];
                    // System.out.println("Versions: " + versions);"
                    info_GS[1] = versions;
                }

                if (line.contains("Versions list")) {
                    versions_list = line.split("Versions list ")[1];
                    //System.out.println("Versions_list: " + versions_list);
                    info_GS[2] = versions_list;
                }

                if (line.contains("Citations") && !line.contains("Citations list")) {

                    citations = line.split("Citations ")[1];
                    // System.out.println("NUM CIT: " + citations);
                    info_GS[3] = citations;
                }

                if (line.contains("Citations list")) {

                    citations_list = line.split("Citations list ")[1];
                    //System.out.println("URL CIT: " + citations_list);
                    info_GS[4] = citations_list;
                }

                if (line.contains("Year")) {

                    year = line.split("Year ")[1];
                    //System.out.println("Year: " + year);
                    info_GS[5] = year;
                }
            }
            
            if (!control) {

                for (int i = 0; i < info_GS.length; i++) {
                    info_GS[i] = "No Information available";
                }
            }

        } catch (Exception e) {

            System.out.println("EXCEPTION IN GOOGLE SCHOLAR: " + e.getMessage());
            for (int i = 0; i < info_GS.length; i++) {
                info_GS[i] = "No Available Service";
            }
        }

        return info_GS;
        //return output.toString();

    }
    
    
    /*private void executeCommandQueryOpenAgris (String word) 
    {
        System.out.println("TITLE ANALIZE: " + word);

       // String[] command = new String[]{"javac", appServerPath + "/WEB-INF/job/scholar.py", "-c 1", "--phrase", title};
        String[] command = new String[]{"export CLASSPATH=.:"
                + appServerPath
                + "SPARQL/libs/*","&& java "
                + appServerPath
                + "SPARQL/SPARQL_OpenAgris"};
      
        Process p;
        boolean control = false;
        
        try {

            System.out.println("APPSERVERPATH: "+appServerPath);
            p = Runtime.getRuntime().exec("java -jar "
                    + appServerPath
                    + "SPARQLOPENAGRIS/TimeOutOpenAgris.jar giusi");
           
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            String line = "";

            System.out.println("-------->"+reader.readLine());

            condition:
            while ((line = reader.readLine()) != null) 
            {
                //System.out.println("ECCO: "+line.split(" ")[0]);
                control = true;
                System.out.println("LINE: " + line);
            }
            
            if (!control) System.out.println("No records found!");            

        } catch (Exception e) {
            System.out.println("EXCEPTION IN COMPILE.sh: " + e.getMessage());           
        }
    }*/
      
    
    private ArrayList executeCommandQuery(
            String word, String numPage, 
            int numRecords, String EndPoint,
            int TimeOut,
            String JARfile) 
            throws IOException 
    {

        ArrayList listRecources = new ArrayList();
        String line;
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Task myTask=new Task(word, numPage, numRecords, EndPoint, JARfile);
        Future<String> future = executor.submit(myTask);
        
       try {
	      System.out.println("Running SPARQL query and set a Max Timeout of "
                      + TimeOut
                      + " minutes");
              
	      //System.out.println(future.get(1, TimeUnit.MINUTES));
              System.out.println(future.get(TimeOut, TimeUnit.MINUTES));
	      System.out.println("Finished!");
              listRecources = myTask.getArray();
	      //System.out.println("PID = " + (new Task(word)).getPID());
	      // Process p = Runtime.getRuntime().exec("kill -15 " + PID.substring(0, PID.indexOf("@")));
              //BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
              //while ((line = input.readLine()) != null) System.out.println(line);
              //input.close();
              //int exitVal = p.waitFor() ;
              //System.out.println("exit value: " + exitVal);

    } catch (Exception err) { 
		future.cancel(true);
                System.out.println("Terminated!");
                System.out.println("Killing process with PID() = " + PID);
                if(PID==0) PID=714184579;
    		Process p = Runtime.getRuntime().exec("kill -9 " + PID);
        	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
        	while ((line = input.readLine()) != null) System.out.println(line);
        	input.close();
                
    }
       System.out.println("ARRAY = " + listRecources.size());
       
       return listRecources;

    }
    
  class Task implements Callable<String> 
  {
    Process p = null;
    String word_search;
    String page;
    int nRec;
    String endPoint;
    String JARFile;
    //Process p2=null;
    ArrayList listRes=new ArrayList();
    
    public Task(String word, String num_page, 
                int numRecords, String EndPoint, String JARfile)
    {
        word_search=word;
        page=num_page;
        nRec=numRecords;
        endPoint=EndPoint;
        JARFile=JARfile;
        
    }
    public Task(){}
 
    public int getPID() { return PID; }
    public ArrayList getArray() { return listRes; }

    @Override
    public String call() throws Exception 
    {
        
        ArrayList array=new ArrayList();
	String line;
        
        //String word="'"+word_search+"'";
        String[] command = new String[]{"java", "-jar",
            appServerPath 
            + "SPARQL_TIMEOUT/" + JARFile, //+ "SPARQL_TIMEOUT/TimeOutOpenAgris.jar", 
            word_search, 
            page, 
            String.valueOf(nRec),
            endPoint};
        System.out.println("JARFile-->"+JARFile);
      // String command="java -jar "+appServerPath+"SPARQL_OPENAGRIS/TimeOutOpenAgris.jar "+ word+ " "+page+" "+nRec+" "+endPoint;
      // System.out.println("COMMAND-->"+command);
       p = Runtime.getRuntime().exec(command);
       
//       BufferedReader inputErr =
//        	new BufferedReader (new InputStreamReader(p.getErrorStream()));
//        while ((lineErr = inputErr.readLine()) != null) 
//        {
//            System.out.println("ERROR");
//            System.out.println(lineErr);
//        }
       
	//PID = ManagementFactory.getRuntimeMXBean().getName();
       // System.out.println("Process started with PID = " + PID);
        PID=getUnixPID(p);
        
        System.out.println("PID ---> "+PID);
        
        BufferedReader input =
        	new BufferedReader (new InputStreamReader(p.getInputStream()));
        
       // PID=line.split(" ")[1];
        System.out.println("Process started with PID = " + PID);
              
        while ((line = input.readLine()) != null) 
        {
            System.out.println(line);
            if(line.contains("==> "))
            {
                String resourceOpenAgris=line.split("==> ")[1];
                System.out.println("RESOURCE:" + resourceOpenAgris);
                array.add(resourceOpenAgris);
            }
            
            listRes = getListNotDuplicate(array);
        
        }
        input.close();

        int exitVal = p.waitFor() ;
        System.out.println("exit value: " + exitVal);
        return "Task completed successfully!";
    }
  }
     
     
  int getUnixPID(Process process) 
          throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException 
  {
    if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
        Class proc = process.getClass();
        Field field = proc.getDeclaredField("pid");
        field.setAccessible(true);
        Object pid = field.get(process);
        return (Integer) pid;
    } else {
        throw new IllegalArgumentException("Not a UNIX Process");
    }
  }
     
  public static ArrayList getListNotDuplicate(ArrayList listOriginal) 
  {

        ArrayList listNuova = new ArrayList();

        if (listOriginal.size() > 1) 
        {
            int k = 1;
            int j, i = 0;
            boolean duplicato;
            
            listNuova.add(listOriginal.get(0));
            for (i = 1; i < listOriginal.size(); i++) 
            {
                duplicato = false;
                for (j = 0; j < i; j++) 
                {
                    if (listOriginal.get(i).equals(listOriginal.get(j)))
                        duplicato = true;                    
                }
                
                if (!duplicato) 
                    listNuova.add(listOriginal.get(i));
            }

            return listNuova;
        } else {
            return listOriginal;
        }
    }
}

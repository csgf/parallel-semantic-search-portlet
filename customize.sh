#!/bin/sh
#
# Script that customizes the hostname' portlet
# just fill properly any environment variable
# accordingly to your application and then run
#
# $ ./customize.sh
# 
# Author: riccardo.bruno@ct.infn.it

# Author' information
# Used to fill-up portlet' licence information
AUTH_EMAIL=riccardo.bruno@ct.infn.it
AUTH_NAME='Riccardo Bruno'
AUTH_INSTITUTE='COMETA'

# Application information
APP_NAME=custom
APP_VERSION=

#
# docroot/WEB-INF/portlet.xml tag values
#

# <portlet-name>
PORTLET_NAME=SemanticSearch-portlet

# <title>
PORTLET_TITLE=SemantiSearch-portlet

# <short-title>
PORTLET_SHTITLE=SemanticSearch-portlet

# <keywords>
PORTLET_KEYWORDS="SemanticSearch-portlet"

# <display-name>
DISPLAY_NAME=SemanticSearch-portlet

# <portlet-class>
# Do not use '-' character; it's illegal in java class name (use '_' instead)
BASE_CLASS=it.infn.ct
CLASS_NAME=SemanticSearch_portlet

#
# Init parameters
#

#init_PortletVersion
# Use this value to specify the portlet version
INI_PVERSION=1.0

#init_logLevel
# The portlet template code uses a customizable log levels: trace, info, debug, error, fatal
# Log outputs having a lower level will be not printed out while the portlet executes
INI_LOGLEVEL=info

#init_bdiiHost
# specify the information system BDII in the form ldap://<info_provider_hostname>:<info_provide_port>
INI_BDIIHOST=ldap://bdii.eumedgrid.eu:2170

#init_wmsHost
# The wms host can be obtained from an UI with the gLite command line
# lcg-infosites --vo eumed wms
#
INI_WMSHOST=wms://wms-4.dir.garr.it:7443/glite_wms_wmproxy_server

#init_pxServerHost
INI_PXHOST=myproxy.ct.infn.it

#init_pxServerPort
INI_PXPORT=

#init_pxServerSecure
INI_PXSECURE=

#init_pxRobotId
INI_ROBOTID=21057

#init_pxRobotVO
INI_ROBOTVO=eumed

#init_pxRobotRole
INI_ROBOROLE=eumed

#init_pxUserProxy
INI_USERPX=

#init_pxRobotRenewalFlag (true/false)
INI_RENEWALFLAG=true

#init_sciGwyAppId (Refer to UserTracking database table 'GridInteractions')
UTDB_APPID=9

#init_sciGwyUserTrackingDB_Hostname  
UTDB_HOSTNAME=localhost

#init_sciGwyUserTrackingDB_Username
UTDB_USERNAME=tracking_user

#init_sciGwyUserTrackingDB_Password
UTDB_PASSWORD=usertracking         

#init_sciGwyUserTrackingDB_Database
UTDB_DATABASE=userstracking

#init_jobRequirements
# More requirements can be specified by a ';' separated list of items
INI_JOBREQUIREMENTS=''

#init_pilotScript
# The pilot scrit is not yet supported by the init_parameters
# leave this value empty unless it uses only one line
INI_PILOTSCRIPT=


#
# docroot/WEB-INF/liferay-display.xml tag values
#
PORTLET_CATEGORYNAME=GILDA
PORTLET_IDENTIFIER=$PORTLET_NAME

#
# docroot/WEB-INF/liferay-portlet.xml tag values
#

# <portlet-name>
LFRY_PORTLETNAME=$PORTLET_NAME
# <css-class-wrapper> (! this filed does not accept '-')
LFRY_CSSCLWRAPPER=$CLASS_NAME


#
# docroot/WEB-INF/glassfish-web.xml tag values
#
GLFH_CONTEXTROOT=$PORTLET_NAME

#-----------------------------
# Customization script ...
#-----------------------------

#
# Generates the portlet.xml file
#
cat > docroot/WEB-INF/portlet.xml <<EOF
<?xml version="1.0"?>

<portlet-app
	version="2.0"
	xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
>
	<portlet>
		<portlet-name>${PORTLET_NAME}</portlet-name>
		<display-name>${DISPLAY_NAME}</display-name>
		<portlet-class>${BASE_CLASS}.${CLASS_NAME}</portlet-class>
		<init-param>    
			<name>portletVersion</name>
			<value>${INI_PVERSION}</value>
		</init-param> 
		<init-param>
			<name>logLevel</name>
			<value>${INI_LOGLEVEL}</value>
		</init-param>
		<init-param>    
			<name>bdiiHost</name>
			<value>${INI_BDIIHOST}</value>
		</init-param>
		<init-param>    
			<name>wmsHost</name>
			<value>${INI_WMSHOST}</value>
		</init-param>
		<init-param>
			<name>pxServerHost</name>
			<value>${INI_PXHOST}</value>
		</init-param>
		<init-param>
			<name>pxServerPort</name>
			<value>${INI_PXPORT}</value>
			</init-param>
		<init-param>
			<name>pxServerSecure</name>
			<value>${INI_PXPORT}</value>
		</init-param>
		<init-param>
			<name>pxRobotId</name>
			<value>${INI_ROBOTID}</value>
		</init-param>
		<init-param>
			<name>pxRobotVO</name>
			<value>${INI_ROBOTVO}</value>
		</init-param>
		<init-param>
			<name>pxRobotRole</name>
			<value>${INI_ROBOROLE}</value>
		</init-param>
		<init-param>
			<name>pxUserProxy</name>
			<value>${INI_USERPX}</value>
		</init-param>
		<init-param>
			<name>pxRobotRenewalFlag</name>
			<value>${INI_RENEWALFLAG}</value>
		</init-param>
		<init-param>
			<name>sciGwyAppId</name>
			<value>${UTDB_APPID}</value>
		</init-param>
		<init-param>
			<name>sciGwyUserTrackingDB_Hostname</name>
			<value>${UTDB_HOSTNAME}</value>
		</init-param>
		<init-param>
			<name>sciGwyUserTrackingDB_Username</name>
			<value>${UTDB_USERNAME}</value>
		</init-param>
		<init-param>
			<name>sciGwyUserTrackingDB_Password</name>
			<value>${UTDB_PASSWORD}</value>
		</init-param>
		<init-param>
			<name>sciGwyUserTrackingDB_Database</name>
			<value>${UTDB_DATABASE}</value>
		</init-param>
		<init-param>    
			<name>jobRequirements</name>
			<value>${INI_JOBREQUIREMENTS}</value>
		</init-param>            
		<init-param>    
			<name>pilotScript</name>
			<value>${INI_PILOTSCRIPT}</value>
		</init-param>
		<expiration-cache>0</expiration-cache>
		<supports>
			<mime-type>text/html</mime-type>
			<portlet-mode>view</portlet-mode>
			<portlet-mode>edit</portlet-mode>
			<portlet-mode>help</portlet-mode>
		</supports>
		<portlet-info>
			<title>${PORTLET_TITLE}</title>
			<short-title>${PORTLET_SHTITLE}</short-title>
			<keywords>${PORTLET_KEYWORDS}</keywords>
		</portlet-info>
		<security-role-ref>
			<role-name>administrator</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>guest</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>power-user</role-name>
		</security-role-ref>
		<security-role-ref>
			<role-name>user</role-name>
		</security-role-ref>
	</portlet>
</portlet-app>
EOF

# Create the portlet class directory
mkdir -p docroot/WEB-INF/src/$(echo $BASE_CLASS | sed s/'\.'/'\/'/g)

#
# Generates the liferay-display.xml
#
cat > docroot/WEB-INF/liferay-display.xml <<EOF
<?xml version="1.0"?>
<!DOCTYPE display PUBLIC "-//Liferay//DTD Display 6.0.0//EN" "http://www.liferay.com/dtd/liferay-display_6_0_0.dtd">

<display>
	<category name="${PORTLET_CATEGORYNAME}">
		<portlet id="${PORTLET_IDENTIFIER}" />
	</category>
</display>
EOF

#
# Generates the docroot/WEB-INF/liferay-portlet.xml
#
cat > docroot/WEB-INF/liferay-portlet.xml << EOF
<?xml version="1.0"?>
<!DOCTYPE liferay-portlet-app PUBLIC "-//Liferay//DTD Portlet Application 6.0.0//EN" "http://www.liferay.com/dtd/liferay-portlet-app_6_0_0.dtd">

<liferay-portlet-app>
	<portlet>
		<portlet-name>${LFRY_PORTLETNAME}</portlet-name>
		<icon>/icon.png</icon>
		<instanceable>true</instanceable>
		<header-portlet-css>/css/main.css</header-portlet-css>
		<footer-portlet-javascript>/js/main.js</footer-portlet-javascript>
		<css-class-wrapper>${LFRY_CSSCLWRAPPER}</css-class-wrapper>
	</portlet>
	<role-mapper>
		<role-name>administrator</role-name>
		<role-link>Administrator</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>guest</role-name>
		<role-link>Guest</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>power-user</role-name>
		<role-link>Power User</role-link>
	</role-mapper>
	<role-mapper>
		<role-name>user</role-name>
		<role-link>User</role-link>
	</role-mapper>
</liferay-portlet-app>
EOF

#
# Generates the docroot/WEB-INF/glassfish-web.xml
# 
cat > docroot/WEB-INF/glassfish-web.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE glassfish-web-app PUBLIC "-//GlassFish.org//DTD GlassFish Application Server 3.1 Servlet 3.0//EN" "http://glassfish.org/dtds/glassfish-web-app_3_0-1.dtd">
<glassfish-web-app error-url="">
  <context-root>/${GLFH_CONTEXTROOT}</context-root>
  <class-loader delegate="true"/>
  <jsp-config>
    <property name="keepgenerated" value="true">
      <description>Keep a copy of the generated servlet class' java code.</description>
    </property>
  </jsp-config>
</glassfish-web-app>
EOF

#
# Generates the java code
#
cat > docroot/WEB-INF/src/$(echo $BASE_CLASS | sed s/'\.'/'\/'/g)/${CLASS_NAME}.java <<EOF
/**************************************************************************
Copyright (c) 2011:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on
the copyright holders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author <a href="mailto:${AUTH_EMAIL}">${AUTH_NAME}</a>(${AUTH_INSTITUTE})
****************************************************************************/
package ${BASE_CLASS};

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

// Importing Apache libraries
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.apache.commons.fileupload.*; 
import org.apache.commons.fileupload.disk.DiskFileItemFactory; 
import org.apache.commons.fileupload.portlet.PortletFileUpload;

// Importing GridEngine Job libraries 
import it.infn.ct.GridEngine.Job.*;

//
// This is the class that overrides the GenericPortlet class methods
// You can create your own portlet just customizing the code skeleton
// available below. It provides mainly a working example on:
//    1) How to manage combination of Actions/Views
//    2) How to manage portlet preferences and help
//    3) How to show information using the Log object
//    4) How to execute a distributed application with GridEngine
//
public class ${CLASS_NAME} extends GenericPortlet {
    
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
        private static final int   TRACE_LEVEL=6;
        private static final int   DEBUG_LEVEL=5;
        private static final int    INFO_LEVEL=4;
        private static final int    WARN_LEVEL=3;
        private static final int   ERROR_LEVEL=2;
        private static final int   FATAL_LEVEL=1;
        private static final int UNKNOWN_LEVEL=0;
            
        private Log _log;                        
        private int logLevel=AppLogger.INFO_LEVEL;
            
        public void setLogLevel(String level) {
            switch(LogLevels.valueOf(level)) {
                case trace:
                    logLevel=AppLogger.TRACE_LEVEL;
                    break;                    
                case debug:
                    logLevel=AppLogger.DEBUG_LEVEL;
                    break;
                case info:
                    logLevel=AppLogger.INFO_LEVEL;
                    break;
                case warn:
                    logLevel=AppLogger.WARN_LEVEL;
                    break;
                case error:
                    logLevel=AppLogger.ERROR_LEVEL;
                    break;
                case fatal:
                    logLevel=AppLogger.FATAL_LEVEL;
                    break;
                default:
                    logLevel=AppLogger.UNKNOWN_LEVEL;                                                              
            }            
        }
        public AppLogger(Class cname) {
            _log = LogFactory.getLog(cname);
        }
        public void trace(String s) {
            if(   _log.isTraceEnabled()
               && logLevel >= AppLogger.TRACE_LEVEL)
                  _log.trace(s);
        }
        public void debug(String s) {
            if(   _log.isDebugEnabled()
               && logLevel >= AppLogger.DEBUG_LEVEL)
                  _log.trace(s);
        }
        public void info(String s) {
            if(   _log.isInfoEnabled()
               && logLevel >= AppLogger.INFO_LEVEL)
                  _log.info(s);
        }
        public void warn(String s) {
            if(   _log.isWarnEnabled()
               && logLevel >= AppLogger.WARN_LEVEL)
                  _log.warn(s);
        }
        public void error(String s) {
            if(   _log.isErrorEnabled()
               && logLevel >= AppLogger.ERROR_LEVEL)
                  _log.error(s);
        }
        public void fatal(String s) {
            if(   _log.isFatalEnabled()
               && logLevel >= AppLogger.FATAL_LEVEL)
                  _log.fatal(s);
        }
    } // AppLogger
    
    // Instantiate the logger object
    public AppLogger _log = new AppLogger(${CLASS_NAME}.class);
    
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
         ACTION_INPUT    // Called before to show the INPUT view
        ,ACTION_SUBMIT     // Called after the user press the submit button    
        }

    private enum Views {
         VIEW_INPUT      // View containing application input fields
        ,VIEW_SUBMIT     // View reporting the job submission                
    }

    // The init values will be read form portlet.xml from <init-param> xml tag
    // This tag will be useful to setup defaults values for your own portlet
    class App_Init {
        String portletVersion;
        String logLevel;
        String bdiiHost;
        String wmsHost;
        String pxServerHost;
        String pxServerPort;
        String pxServerSecure;
        String pxRobotId;
        String pxRobotVO;
        String pxRobotRole;
        String pxUserProxy;
        String pxRobotRenewalFlag;
        String sciGwyAppId;
        String sciGwyUserTrackingDB_Hostname; 
        String sciGwyUserTrackingDB_Username;  
        String sciGwyUserTrackingDB_Password;
        String sciGwyUserTrackingDB_Database;
        String jobRequirements;
        String pilotScript;
        
        public App_Init() {
            portletVersion
           =logLevel
           =bdiiHost
           =wmsHost
           =pxServerHost
           =pxServerPort
           =pxServerSecure
           =pxRobotId
           =pxRobotVO
           =pxRobotRole
           =pxUserProxy
           =pxRobotRenewalFlag
           =sciGwyAppId
           =sciGwyUserTrackingDB_Hostname 
           =sciGwyUserTrackingDB_Username  
           =sciGwyUserTrackingDB_Password
           =sciGwyUserTrackingDB_Database
           =jobRequirements
           =pilotScript
           ="";
        }
    } // App_Init
    
    // Instanciate the App_Init object
    public App_Init appInit = new App_Init();
    
    // This object is used to store the values of portlet preferences
    // The init method will initialize their values with corresponding init_*
    // variables when the portlet first starts (see init_Preferences var).
    // Please notice that not all init_* variables have a corresponding pref_* value
    class App_Preferences {
        String logLevel;
        String bdiiHost;
        String wmsHost;
        String pxServerHost;
        String pxServerPort;
        String pxServerSecure;
        String pxRobotId;
        String pxRobotVO;
        String pxRobotRole;
        String pxUserProxy;
        String pxRobotRenewalFlag;
        String sciGwyAppId;
        String jobRequirements;
        String pilotScript;
        
        public App_Preferences() {
            logLevel
           =bdiiHost
           =wmsHost
           =pxServerHost
           =pxServerPort
           =pxServerSecure
           =pxRobotId
           =pxRobotVO
           =pxRobotRole
           =pxUserProxy
           =pxRobotRenewalFlag
           =sciGwyAppId
           =jobRequirements
           =pilotScript
           ="";
        }
    } // App_Preferences
    
    // Instanciate the App_Preferences object
    public App_Preferences appPreferences = new App_Preferences();
    
    //
    // Application input values
    //
    // Job submission values are collected inside a single object
    class App_Input {
        // Applicatoin inputs
        String inputFileName;   // Filename for application input file
        String inputFileText;   // Text for application input file 
        String jobIdentifier;   // User' given job identifier                
        
        // Each inputSandobox file must be declared below
        // This variable contains the content of an uploaded file
        String inputSandbox_inputFile;        
        
        // Some user level information
        // must be stored as well
        String username;
        String timestamp;
        
        public App_Input() {
           inputFileName
           =inputFileText
           =jobIdentifier
           =inputSandbox_inputFile
           =username
           =timestamp
           ="";
        }
    } // App_Input
        
    // Liferay user data
    // Classes below are used by this portlet code to get information
    // about the current user
    public ThemeDisplay themeDisplay;  // Liferay' ThemeDisplay variable
    public User user;                  // From ThemeDisplay get User data
    public String username;            // From User data the username        

    // Liferay portlet data        
    PortletSession portletSession;  // PorteltSession
    PortletContext portletContext;  // PortletContext
    public String  appServerPath;   // This variable stores the absolute path of the Web applications
        
    // Other misc valuse
    // (!) Pay attention that altough the use of the LS variable
    //     the replaceAll("\n","") has to be used 
    public String LS = System.getProperty("line.separator");
        
    // Users must have separated inputSandbox files
    // these file will be generated into /tmp directory
    // and prefixed with the format <timestamp>_<user>_*
    // The timestamp format is:
    public static final String tsFormat = "yyyyMMddHHmmss"; 
        
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
    throws PortletException                
    { 
        // Load default values from portlet.xml              
        appInit.portletVersion                = ""+getInitParameter("portletVersion");
        appInit.logLevel                      = ""+getInitParameter("logLevel");
        appInit.bdiiHost                      = ""+getInitParameter("bdiiHost");
        appInit.wmsHost                       = ""+getInitParameter("wmsHost");
        appInit.pxServerHost                  = ""+getInitParameter("pxServerHost");
        appInit.pxServerPort                  = ""+getInitParameter("pxServerPort");
        appInit.pxServerSecure                = ""+getInitParameter("pxServerSecure");
        appInit.pxRobotId                     = ""+getInitParameter("pxRobotId");
        appInit.pxRobotVO                     = ""+getInitParameter("pxRobotVO");
        appInit.pxRobotRole                   = ""+getInitParameter("pxRobotRole");
        appInit.pxUserProxy                   = ""+getInitParameter("pxUserProxy");
        appInit.pxRobotRenewalFlag            = ""+getInitParameter("pxRobotRenewalFlag");
        appInit.sciGwyAppId                   = ""+getInitParameter("sciGwyAppId");
        appInit.sciGwyUserTrackingDB_Hostname = ""+getInitParameter("sciGwyUserTrackingDB_Hostname");
        appInit.sciGwyUserTrackingDB_Username = ""+getInitParameter("sciGwyUserTrackingDB_Username");
        appInit.sciGwyUserTrackingDB_Password = ""+getInitParameter("sciGwyUserTrackingDB_Password");
        appInit.sciGwyUserTrackingDB_Database = ""+getInitParameter("sciGwyUserTrackingDB_Database");
        appInit.jobRequirements               = ""+getInitParameter("jobRequirements");
        // WARNING: Although the pilot script field is considered here it is not
        // Possible to specify a bash script code inside thie init_pilotScript
        // xml field. The content of pilot script must be inserted manually upon
        // the portlet installation through its configuration pane.        
        appInit.pilotScript = ""+getInitParameter("pilotScript");
        appInit.pilotScript = appInit.pilotScript.replaceAll("\r", "");
                
        // Assigns the log level      
        _log.setLogLevel(appInit.logLevel);
                
        // Show loaded values into log
        _log.info(
               LS+"Loading default values "
              +LS+"-----------------------"                             
              +LS+"init_portletVersion               : '"+appInit.portletVersion               +"'"
              +LS+"init_logLevel                     : '"+appInit.logLevel                     +"'"
              +LS+"init_bdiiHost                     : '"+appInit.bdiiHost                     +"'"
              +LS+"init_wmsHost                      : '"+appInit.wmsHost                      +"'"
              +LS+"init_pxServerHost                 : '"+appInit.pxServerHost                 +"'"
              +LS+"init_pxServerPort                 : '"+appInit.pxServerPort                 +"'"
              +LS+"init_pxServerSecure               : '"+appInit.pxServerSecure               +"'"
              +LS+"init_pxRobotId                    : '"+appInit.pxRobotId                    +"'"
              +LS+"init_pxRobotVO                    : '"+appInit.pxRobotVO                    +"'"
              +LS+"init_pxRobotRole                  : '"+appInit.pxRobotRole                  +"'"
              +LS+"init_pxUserProxy                  : '"+appInit.pxUserProxy                  +"'"
              +LS+"init_pxRobotRenewalFlag           : '"+appInit.pxRobotRenewalFlag           +"'"
              +LS+"init_sciGwyAppId                  : '"+appInit.sciGwyAppId                  +"'"
              +LS+"init_sciGwyUserTrackingDB_Hostname: '"+appInit.sciGwyUserTrackingDB_Hostname+"'"
              +LS+"init_sciGwyUserTrackingDB_Username: '"+appInit.sciGwyUserTrackingDB_Username+"'"
              +LS+"init_sciGwyUserTrackingDB_Password: '"+appInit.sciGwyUserTrackingDB_Password+"'"
              +LS+"init_sciGwyUserTrackingDB_Database: '"+appInit.sciGwyUserTrackingDB_Database+"'"
              +LS+"init_jobRequirements              : '"+appInit.jobRequirements              +"'"
              +LS+"init_pilotScript                  : '"+appInit.pilotScript                  +"'"
              +LS
              +LS+"!WARNING: Although the pilot script field is considered into the portlet.xml"
              +LS+"it is not possible to specify a bash script code inside the init_pilotScript"
              +LS+"xml' field. The content of the pilot script must be inserted manually upon"
              +LS+"the portlet installation through its configuration pane."
              +LS);                                  
    } // init

    //
    // processAction
    //
    // This method allows the portlet to process an action request; this method is normally
    // called upon each user interaction (a submit button inside a jsp' <form statement)
    //
    @Override
    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        _log.info("calling processAction ...");

        // Determine the username
        themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
        user         = themeDisplay.getUser();
        username     = user.getScreenName();
        _log.info("User: '"+user+"'");
                
        // Determine the application pathname                
        portletSession = request.getPortletSession();
        portletContext = portletSession.getPortletContext();
        appServerPath  = portletContext.getRealPath("/");
        _log.info("Web Application path: '"+appServerPath+"'");
    
        // Determine the current portlet mode and forward this state to the response
        // Accordingly to JSRs168/286 the standard portlet modes are:
        // VIEW, EDIT, HELP
        PortletMode mode = request.getPortletMode();
        response.setPortletMode(request.getPortletMode());

        // Switch among different portlet modes: VIEW, EDIT, HELP
        // Custom modes are not covered by this template
        if (mode.equals(PortletMode.VIEW)){
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
            String actionStatus=request.getParameter("PortletStatus");
            // Assigns the default ACTION
            if(   null==actionStatus 
               || actionStatus.equals("")) 
            	actionStatus=""+Actions.ACTION_INPUT;
            
            // Different actions will be performed accordingly to the
            // different possible statuses
            switch(Actions.valueOf(actionStatus)) {
                case ACTION_INPUT:
                    _log.info("Got action: 'ACTION_INPUT'");
                    
                    // Assign the correct view
                    response.setRenderParameter("PortletStatus",""+Views.VIEW_INPUT);
                break;
                case ACTION_SUBMIT:
                    _log.info("Got action: 'ACTION_SUBMIT'");
                                        
                    // Get current preference values
                    getPreferences(request,null);
                                        
                    // Create the appInput object
                    App_Input appInput = new App_Input();
                    
                    // Stores the user submitting the job
                    appInput.username=username;
                    
                    // Determine the submissionTimeStamp                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat(tsFormat);
                    String timestamp = dateFormat.format(Calendar.getInstance().getTime());
                    appInput.timestamp=timestamp;
                                       
                    // Process input fields and files to upload
                    getInputForm(request,appInput);
                    
                    // Following files have to be updated with
                    // values taken from textareas or from uploaded files:
                    // input_file.txt
                    updateFiles(appInput);                                                                                
                                        
                    // Submit the job
                    submitJob(appInput);
                    
                    // Send the jobIdentifier and assign the correct view                    
                    response.setRenderParameter("PortletStatus",""+Views.VIEW_SUBMIT);
                    response.setRenderParameter("jobIdentifier",""+appInput.jobIdentifier);
                break;
                default:
                     _log.info("Unhandled action: '"+actionStatus+"'");                            
                     response.setRenderParameter("PortletStatus",""+Views.VIEW_INPUT);
            }
        }
        else if(mode.equals(PortletMode.HELP)) {
            // The HELP mode used to give portlet usage HELP to the user
            // This code will be called after the call to doHelp method                         
            _log.info("Portlet mode: HELP");                        
        }
        else if(mode.equals(PortletMode.EDIT)) {
            // The EDIT mode is used to view/setup portlet preferences
            // This code will be called after the user sends the actionURL 
            // generated by the doEdit method 
            // The code below just stores new preference values
            _log.info("Portlet mode: EDIT");
            
            // new preferences will takem from edit.jsp
            String newpref_logLevel           = ""+request.getParameter("pref_logLevel");
            String newpref_bdiiHost           = ""+request.getParameter("pref_bdiiHost"); 
            String newpref_wmsHost            = ""+request.getParameter("pref_wmsHost"); 
            String newpref_pxServerHost       = ""+request.getParameter("pref_pxServerHost"); 
            String newpref_pxServerPort       = ""+request.getParameter("pref_pxServerPort"); 
            String newpref_pxServerSecure     = ""+request.getParameter("pref_pxServerSecure"); 
            String newpref_pxRobotId          = ""+request.getParameter("pref_pxRobotId"); 
            String newpref_pxRobotVO          = ""+request.getParameter("pref_pxRobotVO"); 
            String newpref_pxRobotRole        = ""+request.getParameter("pref_pxRobotRole"); 
            String newpref_pxRobotRenewalFlag = ""+request.getParameter("pref_pxRobotRenewalFlag"); 
            String newpref_pxUserProxy        = ""+request.getParameter("pref_pxUserProxy"); 
            String newpref_sciGwyAppId        = ""+request.getParameter("pref_sciGwyAppId"); 
            String newpref_jobRequirements    = ""+request.getParameter("pref_jobRequirements");
            String newpref_pilotScript        = ""+request.getParameter("pref_pilotScript");
            newpref_pilotScript=newpref_pilotScript.replaceAll("\r", "");   
            
            // Show preference values changes
            _log.info(
                   LS+"variable name          : 'Old Value' -> 'New value'"
                  +LS+"---------------------------------------------------"  
                  +LS+"pref_logLevel          : '"+appPreferences.logLevel          +"' -> '"+newpref_logLevel          +"'"
                  +LS+"pref_bdiiHost          : '"+appPreferences.bdiiHost          +"' -> '"+newpref_bdiiHost          +"'"
                  +LS+"pref_wmsHost           : '"+appPreferences.wmsHost           +"' -> '"+newpref_wmsHost           +"'"
                  +LS+"pref_pxServerHost      : '"+appPreferences.pxServerHost      +"' -> '"+newpref_pxServerHost      +"'"
                  +LS+"pref_pxServerPort      : '"+appPreferences.pxServerPort      +"' -> '"+newpref_pxServerPort      +"'"
                  +LS+"pref_pxServerSecure    : '"+appPreferences.pxServerSecure    +"' -> '"+newpref_pxServerSecure    +"'"
                  +LS+"pref_pxRobotId         : '"+appPreferences.pxRobotId         +"' -> '"+newpref_pxRobotId         +"'"
                  +LS+"pref_pxRobotVO         : '"+appPreferences.pxRobotVO         +"' -> '"+newpref_pxRobotVO         +"'"
                  +LS+"pref_pxRobotRole       : '"+appPreferences.pxRobotRole       +"' -> '"+newpref_pxRobotRole       +"'"
                  +LS+"pref_pxRobotRenewalFlag: '"+appPreferences.pxRobotRenewalFlag+"' -> '"+newpref_pxRobotRenewalFlag+"'"
                  +LS+"pref_pxUserProxy       : '"+appPreferences.pxUserProxy       +"' -> '"+newpref_pxUserProxy       +"'"
                  +LS+"pref_SciGwyAppId       : '"+appPreferences.sciGwyAppId       +"' -> '"+newpref_sciGwyAppId       +"'"
                  +LS+"pref_jobRequirements   : '"+appPreferences.jobRequirements   +"' -> '"+newpref_jobRequirements   +"'"
                  +LS+"pref_pilotScript       : '"+appPreferences.pilotScript       +"' -> '"+newpref_pilotScript       +"'"
                  +LS);

            // The code below stores the portlet preference values
            PortletPreferences prefs = request.getPreferences();
            prefs.setValue("pref_logLevel"          , newpref_logLevel);
            prefs.setValue("pref_bdiiHost"          , newpref_bdiiHost); 
            prefs.setValue("pref_wmsHost"           , newpref_wmsHost); 
            prefs.setValue("pref_pxServerHost"      , newpref_pxServerHost);
            prefs.setValue("pref_pxServerPort"      , newpref_pxServerPort); 
            prefs.setValue("pref_pxServerSecure"    , newpref_pxServerSecure); 
            prefs.setValue("pref_pxRobotId"         , newpref_pxRobotId); 
            prefs.setValue("pref_pxRobotVO"         , newpref_pxRobotVO); 
            prefs.setValue("pref_pxRobotRole"       , newpref_pxRobotRole); 
            prefs.setValue("pref_pxRobotRenewalFlag", newpref_pxRobotRenewalFlag);
            prefs.setValue("pref_pxUserProxy"       , newpref_pxUserProxy);
            prefs.setValue("pref_sciGwyAppId"       , newpref_sciGwyAppId);
            prefs.setValue("pref_jobRequirements"   , newpref_jobRequirements);
            prefs.setValue("pref_pilotScript"       , newpref_pilotScript);
            prefs.store();
                        
            // The pilot script file have to be updated
            storeString(appServerPath+"/WEB-INF/job/pilot_script.sh",newpref_pilotScript);                        

            // Determine the next view mode (return to the input pane)
            response.setPortletMode(PortletMode.VIEW);                     
        }
        else {
            // Unsupported portlet modes come here
            _log.warn("Custom portlet mode: '"+mode.toString()+"'");
        }                                
    } // processAction

    //
    // Method responsible to show portlet content to the user accordingly to the current view mode
    //
    @Override
    protected void doView(RenderRequest request, RenderResponse response) 
    throws PortletException, IOException 
    {
        _log.info("calling doView ...");    
        response.setContentType("text/html");
        
        // Determine the application pathname                
        portletSession = request.getPortletSession();
        portletContext = portletSession.getPortletContext();
        appServerPath  = portletContext.getRealPath("/");
        _log.info("Web Application path: '"+appServerPath+"'");

        // Switch among supported views; the currentView is determined by the
        // portlet render parameter value stored into PortletStatus identifier
        // this value has been assigned by the actionStatus or it will be 
        // null in case the doView method will be called without a
        // previous processAction call; in such a case the default VIEW_INPIUT
        // will be selected.
        //The PortletStatus variable is managed by jsp and this java code
        String currentView=request.getParameter("PortletStatus");
        if(  null==currentView 
           ||currentView.equals("")) 
            currentView=""+Views.VIEW_INPUT;
        
        // Different actions will be performed accordingly to the
        // different possible view modes
        switch(Views.valueOf(currentView)) {
            // The following code is responsible to call the proper jsp file
            // that will provide the correct portlet interface
            case VIEW_INPUT: {
                _log.info("VIEW_INPUT Selected ...");
                PortletRequestDispatcher dispatcher=getPortletContext().getRequestDispatcher("/input.jsp");
                dispatcher.include(request, response);
            }
            break;            
            case VIEW_SUBMIT: {
                _log.info("VIEW_SUBMIT Selected ...");    
                String jobIdentifier = request.getParameter("jobIdentifier");
                request.setAttribute("jobIdentifier", jobIdentifier);                                                    
                PortletRequestDispatcher dispatcher=getPortletContext().getRequestDispatcher("/submit.jsp");
                dispatcher.include(request, response);
            }
            break;
            default:
                _log.info("Unknown view mode: "+currentView.toString());
        } // switch            
    } // doView

    //
    // doEdit
    //
    // This methods prepares an actionURL that will be used by edit.jsp file into a <input ...> form
    // As soon the user press the action button the processAction will be called and the portlet mode
    // will be set as EDIT.
    @Override
    public void doEdit(RenderRequest request,RenderResponse response)
    throws PortletException,IOException {
        response.setContentType("text/html");
                
        // Get current preference values
        getPreferences(null,request);

        // ActionURL and the current preference value will be passed to the edit.jsp
        PortletURL prefURL = response.createActionURL();
        request.setAttribute("prefURL",prefURL.toString()); 
        request.setAttribute("pref_logLevel"          ,appPreferences.logLevel);
        request.setAttribute("pref_bdiiHost"          ,appPreferences.bdiiHost);
        request.setAttribute("pref_wmsHost"           ,appPreferences.wmsHost);
        request.setAttribute("pref_pxServerHost"      ,appPreferences.pxServerHost);
        request.setAttribute("pref_pxServerPort"      ,appPreferences.pxServerPort);
        request.setAttribute("pref_pxServerSecure"    ,appPreferences.pxServerSecure);
        request.setAttribute("pref_pxRobotId"         ,appPreferences.pxRobotId);
        request.setAttribute("pref_pxRobotVO"         ,appPreferences.pxRobotVO);
        request.setAttribute("pref_pxRobotRole"       ,appPreferences.pxRobotRole);
        request.setAttribute("pref_pxRobotRenewalFlag",appPreferences.pxRobotRenewalFlag);        
        request.setAttribute("pref_pxUserProxy"       ,appPreferences.pxUserProxy);
        request.setAttribute("pref_sciGwyAppId"       ,appPreferences.sciGwyAppId);
        request.setAttribute("pref_jobRequirements"   ,appPreferences.jobRequirements);
        request.setAttribute("pref_pilotScript"       ,appPreferences.pilotScript);
                                        
        // The edit.jsp will be the responsible to show/edit the current preference values
        PortletRequestDispatcher dispatcher=getPortletContext().getRequestDispatcher("/edit.jsp");
        dispatcher.include(request, response);
    } // doEdit

    //
    // doHelp
    //
    // This method just calls the jsp responsible to show the portlet information
    @Override
    public void doHelp(RenderRequest request, RenderResponse response)
    throws PortletException,IOException {
        response.setContentType("text/html");
        request.setAttribute("portletVersion",appInit.portletVersion); 
        PortletRequestDispatcher dispatcher=getPortletContext().getRequestDispatcher("/help.jsp");
        dispatcher.include(request, response);
    } // doHelp
        
    //
    // updateString
    //
    // This method takes as input a filename and will transfer its
    // content inside a String variable
    private String updateString(String file) throws IOException {
        String line  = null;
        StringBuilder stringBuilder = new StringBuilder();            
        BufferedReader reader = new BufferedReader( new FileReader (file));                
        while((line = reader.readLine()) != null ) {
            stringBuilder.append(line);
            stringBuilder.append(LS);
        }                        
        return stringBuilder.toString();
    }
        
    //
    // storeString
    //
    // This method will transfer the content of a given String into
    // a given filename
    private void storeString(String fileName,String fileContent) throws IOException {                        
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));                
        writer.write(fileContent);
        writer.close();
    }
        
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
        file_inputFile    // Input file textarea 
       ,inputFile         // Input file input file
       ,JobIdentifier     // User defined Job identifier
    };
    //
    // getInputForm (method)
    //
    public void getInputForm(ActionRequest request,App_Input appInput) {
    if (PortletFileUpload.isMultipartContent(request))
        try {                
            FileItemFactory factory = new DiskFileItemFactory();
            PortletFileUpload upload = new PortletFileUpload( factory );
            List items = upload.parseRequest(request);
            File repositoryPath = new File("/tmp");
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(repositoryPath);
            Iterator iter = items.iterator();
            String logstring="";
            while (iter.hasNext()) {
                FileItem item = (FileItem)iter.next();
                String   fieldName  =item.getFieldName();
                String   fileName   =item.getName();
                String   contentType=item.getContentType();
                boolean  isInMemory =item.isInMemory();
                long     sizeInBytes=item.getSize();
                // Prepare a log string with field list
                logstring+=LS+"field name: '"+fieldName+"' - '"+item.getString()+"'";
                switch(inputControlsIds.valueOf(fieldName)) {
                    case file_inputFile:
                        appInput.inputFileName=item.getString();
                        processInputFile(item,appInput);
                    break;
                    case inputFile:
                        appInput.inputFileText=item.getString();
                    break;
                    case JobIdentifier:
                        appInput.jobIdentifier=item.getString();
                    break;
                    default:
                        _log.warn("Unhandled input field: '"+fieldName+"' - '"+item.getString()+"'");
                } // switch fieldName                                                   
            } // while iter.hasNext()   
            _log.info(
                   LS+"Reporting"
                  +LS+"---------"
                  +LS+logstring
                  +LS);
        } // try
        catch (Exception e) {
            _log.info("Caught exception while processing files to upload: '"+e.toString()+"'");
        }
        // The input form do not use the "multipart/form-data" 
        else  {                
            // Retrieve from the input form the given application values
            appInput.inputFileName=(String)request.getParameter("file_inputFile");
            appInput.inputFileText=(String)request.getParameter("inputFile");
            appInput.jobIdentifier=(String)request.getParameter("JobIdentifier");
        } // ! isMultipartContent
        
        // Show into the log the taken inputs
        _log.info(
               LS+"Taken input parameters:"
              +LS+"-----------------------"
              +LS+"inputFileName: '"+appInput.inputFileName+"'"
              +LS+"inputFileText: '"+appInput.inputFileText+"'"
              +LS+"jobIdentifier: '"+appInput.jobIdentifier+"'"
              +LS);    
    } // getInputForm 
        
    //
    // processInputFile
    //
    // This method is called when the user specifies a input file to upload
    // the file will be saved first into /tmp directory and then its content
    // stored into the corresponding String variable
    // Before to submit the job the String value will be stored in the 
    // proper job inputSandbox file
    void processInputFile(FileItem item,App_Input appInput) {            
        // Determin the filename
        String fileName = item.getName();
        if(!fileName.equals("")) {
            // Determine the fieldName
            String fieldName = item.getFieldName();
            
            // Create a filename for the uploaded file
            String theNewFileName = "/tmp/"
                                   +appInput.timestamp
                                   +"_"
                                   +appInput.username
                                   +"_"
                                   +fileName;        
            File uploadedFile = new File(theNewFileName);
            _log.info("Uploading file: '"+fileName+"' into '"+theNewFileName+"'");
            try {
                item.write(uploadedFile);
            } 
            catch (Exception e) {
                _log.error("Caught exception while uploading file: 'file_inputFile'");
            }
            // File content has to be inserted into a String variables:
            //   inputFileName -> inputFileText
            try {
                if(fieldName.equals("file_inputFile"))
                 appInput.inputFileText=updateString(theNewFileName);
                 // Other params can be added as below ...
                 //else if(fieldName.equals("..."))
                 //   ...=updateString(theNewFileName);
            else { // Never happens
                 }    
            }
            catch (Exception e) {
                _log.error("Caught exception while processing strings: '"+e.toString()+"'");
            }
        } // if
    } // processInputFile
    
    //
    // updateFiles
    //
    // Before to submit the job this method creates the inputSandbox files
    // starting from users' input fields (textareas or uploaded files)
    public void updateFiles(App_Input appInput) {
        // First of all remomve all possible ^Ms from Strings
        appInput.inputFileText=appInput.inputFileText.replaceAll("\r","");
        // Now save string content into files
        // This must be done for each input sandbox file
        try {            
            appInput.inputSandbox_inputFile="/tmp/"
                                                +appInput.timestamp
                                                +"_"
                                                +appInput.username
                                                +"_input_file.txt"
                                                ;
            FileWriter fwInput=new FileWriter(appInput.inputSandbox_inputFile);
            BufferedWriter bwInput = new BufferedWriter(fwInput);
            bwInput.write(appInput.inputFileText);
            bwInput.close();
        }
        catch (Exception e) {
            _log.error("Caught exception while creating inputSandbox files");
        }
    } // updateFiles
        
    //
    // getPreferences
    //
    // This method retrieves current portlet preference values and it can
    // be called by both processAction or doView methods
    private void getPreferences( ActionRequest actionRequest
                                ,RenderRequest renderRequest) {        
        PortletPreferences prefs=null;
        if(null!=actionRequest)
            prefs = actionRequest.getPreferences(); 
        else if(null != renderRequest)
            prefs = renderRequest.getPreferences(); 
        else _log.warn("Both render request and action request are null");
            
        if (null != prefs) {
            appPreferences.logLevel          =""+prefs.getValue("pref_logLevel"          ,appInit.logLevel); 
            appPreferences.bdiiHost          =""+prefs.getValue("pref_bdiiHost"          ,appInit.bdiiHost); 
            appPreferences.wmsHost           =""+prefs.getValue("pref_wmsHost"           ,appInit.wmsHost); 
            appPreferences.pxServerHost      =""+prefs.getValue("pref_pxServerHost"      ,appInit.pxServerHost);
            appPreferences.pxServerPort      =""+prefs.getValue("pref_pxServerPort"      ,appInit.pxServerPort);
            appPreferences.pxServerSecure    =""+prefs.getValue("pref_pxServerSecure"    ,appInit.pxServerSecure);
            appPreferences.pxRobotId         =""+prefs.getValue("pref_pxRobotId"         ,appInit.pxRobotId); 
            appPreferences.pxRobotVO         =""+prefs.getValue("pref_pxRobotVO"         ,appInit.pxRobotVO); 
            appPreferences.pxRobotRole       =""+prefs.getValue("pref_pxRobotRole"       ,appInit.pxRobotRole); 
            appPreferences.pxRobotRenewalFlag=""+prefs.getValue("pref_pxRobotRenewalFlag",appInit.pxRobotRenewalFlag);
            appPreferences.pxUserProxy       =""+prefs.getValue("pref_pxUserProxy"       ,appInit.pxUserProxy);
            appPreferences.sciGwyAppId       =""+prefs.getValue("pref_sciGwyAppId"       ,appInit.sciGwyAppId);
            appPreferences.jobRequirements   =""+prefs.getValue("pref_jobRequirements"   ,appInit.jobRequirements);
            appPreferences.pilotScript       =""+prefs.getValue("pref_pilotScript"       ,appInit.pilotScript);

            // Assigns the log level      
            _log.setLogLevel(appPreferences.logLevel);
        
            // Show preference values into log
            _log.info(
                   LS+"Preference values:"
                  +LS+"------------------"
                  +LS+"pref_logLevel          : '"+appPreferences.logLevel          +"'"    
                  +LS+"pref_bdiiHost          : '"+appPreferences.bdiiHost          +"'"
                  +LS+"pref_wmsHost           : '"+appPreferences.wmsHost           +"'"            
                  +LS+"pref_pxServerHost      : '"+appPreferences.pxServerHost      +"'"
                  +LS+"pref_pxServerPort      : '"+appPreferences.pxServerPort      +"'"
                  +LS+"pref_pxServerSecure    : '"+appPreferences.pxServerSecure    +"'"
                  +LS+"pref_pxRobotId         : '"+appPreferences.pxRobotId         +"'"
                  +LS+"pref_pxRobotVO         : '"+appPreferences.pxRobotVO         +"'"
                  +LS+"pref_pxRobotRole       : '"+appPreferences.pxRobotRole       +"'"
                  +LS+"pref_pxUserProxy       : '"+appPreferences.pxUserProxy       +"'"
                  +LS+"pref_pxRobotRenewalFlag: '"+appPreferences.pxRobotRenewalFlag+"'"
                  +LS+"pref_sciGwyAppId       : '"+appPreferences.sciGwyAppId       +"'"
                  +LS+"pref_jobRequirements   : '"+appPreferences.jobRequirements   +"'"
                  +LS+"pref_pilotScript       : '"+appPreferences.pilotScript       +"'"
                  +LS); // _log.info; show loaded preference values
        } // if
    } // getPreferences
        
     //
     // submitJob
     //
     // This method sends the job into the distributed infrastructure using
     // the GridEngine methods.
     public void submitJob(App_Input appInput) {
         // Portlet preference Values for job submission 
         String  bdiiHost    =appPreferences.bdiiHost;     // "ldap://bdii.eumedgrid.eu:2170"; 
         String  wmsHost     =appPreferences.wmsHost;      // "wms:wms-4.dir.garr.it:7443/glite_wms_wmproxy_server"; 
         String  pxServerHost=appPreferences.pxServerHost; // "voms.ct.infn.it"
         String  pxRobotId   =appPreferences.pxRobotId;    // "21057";
         String  pxRobotVO   =appPreferences.pxRobotVO;    // "eumed";
         String  pxRobotRole =appPreferences.pxRobotRole;  // "eumed/ScienceGateway";
         String  pxUserProxy =appPreferences.pxUserProxy;  // "/etc/GILDA/eToken/proxy.txt";        
         Boolean pxRobotRenewalFlag;                       // It will be evaluated later from preferences                

         // SciGtw UserTraking data
         // Data below should not used by portlets    
         int applicationId=Integer.parseInt(appPreferences.sciGwyAppId); // GridOperations Application Id
         String   hostUTDB=appInit.sciGwyUserTrackingDB_Hostname;        // Grid Engine' UserTraking database
         String  unameUTDB=appInit.sciGwyUserTrackingDB_Username;        // Username
         String passwdUTDB=appInit.sciGwyUserTrackingDB_Password;        // Password
         String dbnameUTDB=appInit.sciGwyUserTrackingDB_Database;        // Database

         // Job details
         String executable="/bin/sh";               // Application executable
         String arguments ="pilot_script.sh";       // executable' arguments 
         String outputPath="/tmp/";                 // Output Path
         String outputFile="${CLASS_NAME}-Output.txt";   // Distributed application standard output
         String errorFile ="${CLASS_NAME}-Error.txt";    // Distrubuted application standard error
         String appFile   ="${CLASS_NAME}-Files.tar.gz"; // Hostname output files (created by the pilot script)
            
         // InputSandbox (string with comma separated list of file names)
         String inputSandbox= appServerPath+"/WEB-INF/job/pilot_script.sh" // pilot script
                         +","+appInput.inputSandbox_inputFile              // input file
                         ;  
         // OutputSandbox (string with comma separated list of file names)
         String outputSandbox=appFile;                                     // Output file                    
                         
         // Take care of job requirements
         // More requirements can be specified in the preference value 'jobRequirements'
         // separating each requirement by the ';' character        
         String jdlRequirements[] = appPreferences.jobRequirements.split(";");
         int numRequirements=0;         
         for(int i=0; i<jdlRequirements.length; i++) {
             if(!jdlRequirements[i].equals("")) {
               jdlRequirements[numRequirements] = "JDLRequirements=("+jdlRequirements[i]+")";
               numRequirements++;
             }  
             _log.info("Requirement["+i+"]='"+jdlRequirements[i]+"'");
         }

         // Instanciate GridEngine JobSubmission object
         // There are two ways to instanciate this object
         // 1) When submitting the job with a stand-alone java code
         //JSagaJobSubmission tmpJSaga = new JSagaJobSubmission("jdbc:mysql://"+hostUTDB+"/"+dbnameUTDB,unameUTDB,passwdUTDB);                
         // 2) When submitting the job from an application server (glassfish)
         JSagaJobSubmission jobSumbission = new JSagaJobSubmission(); 
         
         // Associate the infrastructure top BDII
         jobSumbission.setBDII(bdiiHost);        

         // Proxy renewal flag
         if(appPreferences.pxRobotRenewalFlag.equalsIgnoreCase("true")) {
                 pxRobotRenewalFlag=true;
         }
         else { 
                 pxRobotRenewalFlag=false;
         }

         // Associate a valid proxy
         // If the user specifies a path containing a local proxy it will be used
         if(pxUserProxy==null || pxUserProxy.equals("")) {
             _log.info("Using ROBOT Proxy ...");
             if(    appPreferences.pxServerHost   != null 
                && !appPreferences.pxServerHost.equals("")
                &&  appPreferences.pxServerPort   != null 
                && !appPreferences.pxServerPort.equals("")
                &&  appPreferences.pxServerSecure != null 
                && !appPreferences.pxServerSecure.equals("")) {
                 boolean secureFlag=false;
                 if(appPreferences.pxServerSecure.equalsIgnoreCase("true"))
                         secureFlag=true;
                 // The secure flag is not yet used it will be introduced
                 // in next GridEngine releases
                 _log.info(
                        LS+"Calling: useRobotProxy"
                       +LS+"  pxServerHost      : '"+appPreferences.pxServerHost  +"'"
                       +LS+"  pxServerPort      : '"+appPreferences.pxServerPort  +"'"
                       +LS+"  pxServerSecure    : '"+appPreferences.pxServerSecure+"'"
                       +LS+"  pxRobotId         : '"+pxRobotId                    +"'"
                       +LS+"  pxRobotVO         : '"+pxRobotVO                    +"'"
                       +LS+"  pxRobotRole       : '"+pxRobotRole                  +"'"
                       +LS+"  pxRobotRenewalFlag: '"+pxRobotRole                  +"'" 
                       +LS);
                 jobSumbission.useRobotProxy( appPreferences.pxServerHost
                                             ,appPreferences.pxServerPort
                                             ,pxRobotId
                                             ,pxRobotVO
                                             ,pxRobotRole
                                             ,pxRobotRenewalFlag
                                            );
             }
             else {
                 // In case no Host and Port and Secure flag are specified use the default call
                 _log.info(
                        LS+"Calling: useRobotProxy"
                       +LS+"  pxRobotId         : '"+pxRobotId  +"'"
                       +LS+"  pxRobotVO         : '"+pxRobotVO  +"'"
                       +LS+"  pxRobotRole       : '"+pxRobotRole+"'"
                       +LS+"  pxRobotRenewalFlag: '"+pxRobotRole+"'"  
                       +LS);
                 jobSumbission.useRobotProxy( pxRobotId
                                             ,pxRobotVO
                                             ,pxRobotRole
                                             ,pxRobotRenewalFlag
                                            );                 
             }
         }
         else {
             _log.info(
                    LS+"Calling: setUserProxy"
                   +LS+"  pxUserProxy: '"+pxUserProxy+"'"
                   +LS);
             jobSumbission.setUserProxy(pxUserProxy);
         }

         // Other job description values
         jobSumbission.setExecutable(executable);     // Specify the executeable                
         jobSumbission.setArguments(arguments);       // Specify the application' arguments
         jobSumbission.setOutputPath(outputPath);     // Specify the output directory
         jobSumbission.setOutputFiles(outputSandbox); // Setup output files (OutputSandbox)                                        
         jobSumbission.setJobOutput(outputFile);      // Specify the std-outputr file
         jobSumbission.setJobError(errorFile);        // Specify the std-error file
         if(   null != inputSandbox                   // Setup input files (InputSandbox) avoiding empty inputSandboxes
           && inputSandbox.length() > 0)
        	jobSumbission.setInputFiles(inputSandbox);          
         if(numRequirements>0)                        // Assign job requirements if any
             jobSumbission.setJDLRequirements(jdlRequirements); 

         // Submit the job ...
         // If a WMS is specified the call for the Job submission changes
         String submitJobCall="";
         if(wmsHost!=null && !wmsHost.equals("")) {
             _log.info("Using jobSubmit with WMS");
             jobSumbission.submitJobAsync(username, hostUTDB, applicationId, wmsHost, appInput.jobIdentifier);
             submitJobCall="submitJobAsync("+username+","+hostUTDB+","+applicationId+","+wmsHost+","+appInput.jobIdentifier+")";
         }
         else {
            _log.info("Using jobSubmit without WMS"); 
            jobSumbission.submitJobAsync(username, hostUTDB, applicationId, appInput.jobIdentifier);      
            submitJobCall="submitJobAsync("+username+","+hostUTDB+","+applicationId+","+appInput.jobIdentifier+")";
         }           

         // View jobSubmission details in the log
         _log.info(
                LS+"submitJob"
               +LS+"---------"
               +LS+submitJobCall
               +LS
               +LS+"bdiiHost            : '"+bdiiHost                      +"'"
               +LS+"wmsHost             : '"+wmsHost                       +"'"
               +LS+"pxServerHost        : '"+appPreferences.pxServerHost   +"'"
               +LS+"pxServerPort        : '"+appPreferences.pxServerPort   +"'"
               +LS+"pxServerSecure      : '"+appPreferences.pxServerSecure +"'"
               +LS+"pxRobotId           : '"+pxRobotId                     +"'"
               +LS+"pxRobotVO           : '"+pxRobotVO                     +"'"
               +LS+"pxRobotRole         : '"+pxRobotRole                   +"'"
               +LS+"pxUserProxy         : '"+pxUserProxy                   +"'"
               +LS+"pxRobotRenewalFlag  : '"+pxRobotRenewalFlag.toString() +"'"
               +LS+"inputSandbox        : '"+inputSandbox                  +"'"
               +LS+"outputSandbox       : '"+outputSandbox                 +"'"
               +LS+"jobRequirements     : '"+appPreferences.jobRequirements+"'"
               +LS+"jobIdentifier       : '"+appInput.jobIdentifier   +"'"
               +LS); // _log.info        
    } // submitJob
} // ${CLASS_NAME} 
EOF

#
# docroot/edit.jsp 
#
mv docroot/edit.jsp docroot/edit.jsp_old
cat docroot/edit.jsp_old | sed s/'hostname-portlet'/'${CLASS_NAME}'/g > docroot/edit.jsp 
rm -f docroot/edit.jsp_old

#
# docroot/help.jsp
#
mv docroot/help.jsp docroot/help.jsp_old
cat docroot/help.jsp_old | sed s/'Author: Riccardo Bruno (COMETA)'/'Author: ${AUTH_NAME} (${AUTH_INSTITUTE}) - ${AUTH_EMAIL}'/g > docroot/help.jsp
rm -f docroot/help.jsp_old

#
# docroot/WEB-INF/job/pilot_script.sh
#
cat > docroot/WEB-INF/job/pilot_script.sh <<EOF
#!/bin/sh 
#
# ${PORTLET_NAME} - pilot script
#
# The following script does:
#   o The execution start/end dates
#   o Listing of the worker node' $HOME directory
#   o Listing of the worker node' $PWD current directory
#   o Shows the template input file
#   o Simulates the creation of an output file  

# Get the hostname
HOSTNAME=$(hostname -f)

# In order to avoid concurrent accesses to files, the 
# portlet uses filename prefixes like
# <timestamp>_<username>_filename
# for this reason the file must be located before to use it
INFILE=$(ls -1 | grep input_file.txt)

echo "--------------------------------------------------"
echo "${PORTLET_NAME} job landed on: '"$HOSTNAME"'"
echo "--------------------------------------------------"
echo "Job execution starts on: '"$(date)"'"

echo "---[WN HOME directory]----------------------------"
ls -l $HOME

echo "---[WN Working directory]-------------------------"
ls -l $(pwd)

echo "---[Your message]---------------------------------"
cat $INFILE
echo

#
# Following statement simulates a produced job file
#
OUTFILE=job_output.txt
echo "--------------------------------------------------" > $OUTFILE
echo "hostname job landed on: '"$HOSTNAME"'" >> $OUTFILE
echo "infile:  '"$INFILE"'"
echo "outfile: '"$OUTFILE"'"
echo "--------------------------------------------------" >> $OUTFILE
cat $INFILE >> $OUTFILE

#
# At the end of the script file it's a good practice to 
# collect all generated job files into a single tar.gz file
# the generated archive may include the input files as well
#
tar cvfz ${CLASS_NAME}-Files.tar.gz $INFILE $OUTFILE

echo "Job execution ends on: '"$(date)"'"
EOF

#--------------------------------
# Final Report
#--------------------------------
echo ""
echo "Reporting configured variables ..."
echo ""
echo "AUTH_EMAIL          : '"$AUTH_EMAIL"'"
echo "AUTH_NAME           : '"$AUTH_NAME"'"
echo "AUTH_INSTITUTE      : '"$AUTH_INSTITUTE"'"
echo "PORTLET_NAME        : '"$PORTLET_NAME"'"
echo "PORTLET_TITLE       : '"$PORTLET_TITLE"'"
echo "PORTLET_SHTITLE     : '"$PORTLET_SHTITLE"'"
echo "PORTLET_KEYWORDS    : '"$PORTLET_KEYWORDS"'"
echo "DISPLAY_NAME        : '"$DISPLAY_NAME"'"
echo "BASE_CLASS          : '"$BASE_CLASS"'"
echo "CLASS_NAME          : '"$CLASS_NAME"'"
echo "INI_PVERSION        : '"$INI_PVERSION"'"
echo "INI_LOGLEVEL        : '"$INI_LOGLEVEL"'"
echo "INI_BDIIHOST        : '"$INI_BDIIHOST"'"
echo "INI_WMSHOST         : '"$INI_WMSHOST"'"
echo "INI_PXHOST          : '"$INI_PXHOST"'"
echo "INI_PXPORT          : '"$INI_PXPORT"'"
echo "INI_PXSECURE        : '"$INI_PXSECURE"'"
echo "INI_ROBOTID         : '"$INI_ROBOTID"'"
echo "INI_ROBOTVO         : '"$INI_ROBOTVO"'"
echo "INI_ROBOROLE        : '"$INI_ROBOROLE"'"
echo "INI_USERPX          : '"$INI_USERPX"'"
echo "INI_RENEWALFLAG     : '"$INI_RENEWALFLAG"'"
echo "UTDB_APPID          : '"$UTDB_APPID"'"
echo "UTDB_HOSTNAME       : '"$UTDB_HOSTNAME"'"
echo "UTDB_USERNAME       : '"$UTDB_USERNAME"'"
echo "UTDB_PASSWORD       : '"$UTDB_PASSWORD"'"
echo "UTDB_DATABASE       : '"$UTDB_DATABASE"'"
echo "INI_JOBREQUIREMENTS : '"$INI_JOBREQUIREMENTS"'"
echo "INI_PILOTSCRIPT     : '"$INI_PILOTSCRIPT"'"
echo "PORTLET_CATEGORYNAME: '"$PORTLET_CATEGORYNAME"'"
echo "PORTLET_IDENTIFIER  : '"$PORTLET_IDENTIFIER"'"
echo "LFRY_PORTLETNAME    : '"$LFRY_PORTLETNAME"'"
echo "LFRY_CSSCLWRAPPER   : '"$LFRY_CSSCLWRAPPER"'"
echo "GLFH_CONTEXTROOT    : '"$GLFH_CONTEXTROOT"'"
echo ""
echo "Customization done!"
echo ""

# Check directory name
DIR_NAME=$(basename $(pwd))
if [ $DIR_NAME != $PORTLET_NAME ]
then
  echo "!ATTENTION: The portlet name and the current direcory have a different name"
  echo "            Please rename your directory to: '"$PORTLET_NAME"'"
  echo ""
fi

# Remove existing classes
rm -rf docroot/WEB-INF/classes
mkdir -p docroot/WEB-INF/classes


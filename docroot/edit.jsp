<%
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>


<%//
  // Application peference Form
  //
  // This form defines values for the GirdEngine interaction
  // These parameters are:
  //	o 	bdiiHost - Hostname of the VO' top BDII
  //	o	pxServerHost - Hostname of the Proxy Robot server
  // 	o	pxRobotId - Proxy Robot Identifier
  //	o	RobotVO - Proxy Robot Virtual Organization
  // 	o	pxRobotRole - Proxy Robot Role
  //	o	pxUserProxy - A complete path to an user proxy
  //	o	pxRobotRenewalFlag - Proxy Robot renewal flag;
  //	 	(When specified it overrides the use of robot proxy) 
%>

<portlet:defineObjects />
<%@page import="javax.portlet.*"%>

<% 
    
PortletPreferences preferences = renderRequest.getPreferences();
String OpenAgris = preferences.getValue("OpenAgris", "");
String OpenAgrisEndPoint = preferences.getValue("OpenAgrisEndPoint", "");
String Europeana = preferences.getValue("Europeana", "");
String EuropeanaEndPoint = preferences.getValue("EuropeanaEndPoint", "");
String CulturaItalia = preferences.getValue("CulturaItalia", "");
String CulturaItaliaEndPoint = preferences.getValue("CulturaItaliaEndPoint", "");
String Isidore = preferences.getValue("Isidore", "");
String IsidoreEndPoint = preferences.getValue("IsidoreEndPoint", "");
String Pubmed = preferences.getValue("Pubmed", "");
String PubmedEndPoint = preferences.getValue("PubmedEndPoint", "");
String Engage = preferences.getValue("Engage", "");
String EngageEndPoint = preferences.getValue("EngageEndPoint", "");
String DBPedia = preferences.getValue("DBPedia", "");
String DBPediaEndPoint = preferences.getValue("DBPediaEndPoint", "");
String NumberRecordsForPage=preferences.getValue("NumberRecordsForPage", "");
String LodLive = preferences.getValue("LodLive", "");
String LodLiveEndPoint=preferences.getValue("LodLiveEndPoint", "");
String TimeOut = preferences.getValue("TimeOut", "");
           
// System.out.println("VALORE_OPENAGRIS: "+OpenAgris);
// System.out.println("VALORE_OPENAGRISENDPOINT: "+OpenAgrisEndPoint);
System.out.println("VALORE_Europeana: " + Europeana);
System.out.println("VALORE_EuropeanaEndPoint: " + EuropeanaEndPoint);
System.out.println("VALORE_CulturaItalia: " + CulturaItalia);
System.out.println("VALORE_CulturaItaliaEndPoint: " + CulturaItaliaEndPoint);
System.out.println("VALORE_Isidore: " + Isidore);
System.out.println("VALORE_IsidoreEndPoint: " + IsidoreEndPoint);
System.out.println("VALORE_Pubmed: " + Pubmed);
System.out.println("VALORE_PubmedEndPoint: " + PubmedEndPoint);
System.out.println("VALORE_Engage: " + Engage);
System.out.println("VALORE_EngageEndPoint: " + EngageEndPoint);
System.out.println("VALORE_DBPedia: " + DBPedia);
System.out.println("VALORE_DBPediaEndPoint: " + DBPediaEndPoint);
System.out.println("VALORE_NumberRecordsForPage: " + NumberRecordsForPage);
System.out.println("VALORE_LodLiveEndPoint: " + LodLiveEndPoint);
System.out.println("VALORE_TimeOut: " + TimeOut);
            
String OpenAgrisEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("OpenAgrisEndPoint");
String EuropeanaEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("EuropeanaEndPoint");
String CulturaItaliaEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("CulturaItaliaEndPoint");
String IsidoreEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("IsidoreEndPoint");              
String PubmedEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("PubmedEndPoint");            
String EngageEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("EngageEndPoint");  
String DBPediaEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("DBPediaEndPoint"); 
String LodLiveEndPoint_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("LodLiveEndPoint");
String TimeOut_default = ((PortletConfig) request.getAttribute("javax.portlet.config")).getInitParameter("TimeOut");
%>

    <div  style="border-style: ridge;">    
        <div style="float: left; padding-left: 20px;">
            <form id="<portlet:namespace/>Preferences" 
                  action="<portlet:actionURL 
                  portletMode="edit"></portlet:actionURL>" 
                  method="post">                        
            
                <table>
                    <tr>                        
                        <th colspan="2" 
                            style="height:40px;">Select <u>Repositories</u> for your semantic search engine
                        </th> 
                    </tr>

                    
                    <tr class="rowList" style="height: 30px;">
                        <td>
                            <input type="checkbox" 
                                   id="1" 
                                   name="OpenAgris"
                                   onClick="ckeckOpenAgris()"/>OpenAgris
                        </td> 
                        <td>
                            <input id="idOpenAgrisEndPoint" disabled  
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="OpenAgrisEndPoint" 
                                   value="<%=OpenAgrisEndPoint%>"/><br/> 
                        </td>

                        
                    </tr>
                    <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="2" 
                                   name="Europeana" 
                                   onClick="ckeckEuropeana()"/>Europeana 
                        </td>
                        <td>
                            <input id="idEuropeanaEndPoint" disabled 
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="EuropeanaEndPoint" 
                                   value="<%=EuropeanaEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                    
                    <tr class="rowList" style="height: 50px;"> 
                        <td>
                            <input type="checkbox" 
                                   id="3" 
                                   name="CulturaItalia" 
                                   onClick="ckeckCulturaItalia()"/>Cultura Italia
                        </td>
                        <td>
                            <input id="idCulturaItaliaEndPoint" 
                                   disabled 
                                   class="rounded" 
                                   type="text"
                                   size="50px;"
                                   name="CulturaItaliaEndPoint" 
                                   value="<%=CulturaItaliaEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                    
                    <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="4" 
                                   name="Isidore" 
                                   onClick="ckeckIsidore()"/>Isidore
                        </td>
                        <td>
                            <input id="idIsidoreEndPoint" disabled 
                                   class="rounded" 
                                   type="text"
                                   size="50px;"
                                   name="IsidoreEndPoint" 
                                   value="<%=IsidoreEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                    
                    <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="5" 
                                   name="Pubmed" 
                                   onClick="ckeckPubmed()"/>Pubmed
                        </td>
                        <td>
                            <input id="idPubmedEndPoint" disabled 
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="PubmedEndPoint" 
                                   value="<%=PubmedEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                    
                    <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="6" 
                                   name="Engage" 
                                   onClick="ckeckEngage()"/>Engage
                        </td>
                        <td>
                            <input id="idEngageEndPoint" disabled 
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="EngageEndPoint" 
                                   value="<%=EngageEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                     <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="8" 
                                   name="DBPedia" 
                                   onClick="ckeckDBPedia()"/>DBPedia
                        </td>
                        <td>
                            <input id="idDBPediaEndPoint" disabled 
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="DBPediaEndPoint" 
                                   value="<%=DBPediaEndPoint%>"/><br/> 
                        </td>
                    </tr>

                    <tr>
                        <th colspan="2" 
                            style="height:40px;">Choose record's number to view in the result page</th>
                    </tr>

                    <tr class="rowList" style="height: 30px;">
                        <td>(Default value is 10)</td>
                        <td>
                            <select id="idNumberRecords" name="NumberRecordsForPage">
                                <option value="10">10</option>
                                <option value="20" >20</option>
                                <option value="30">30</option>
                                <option value="40">40</option>
                            </select>
                        </td>                        
                    </tr>
                    
                    
                    
                    <!-- Giuseppe LA ROCCA -->
                    <tr>
                        <th colspan="2" 
                            style="height:40px;">Choose the default endpoint for LodLive</th>
                    </tr>
                    <tr class="rowList" style="height: 50px;">
                        <td>
                            <input type="checkbox" 
                                   id="7" 
                                   name="LodLive" 
                                   onClick="ckeckLodLive()"/>LodLive
                        </td>
                        <td>
                            <input id="idLodLiveEndPoint" disabled 
                                   class="rounded" 
                                   type="text" 
                                   size="50px;"
                                   name="LodLiveEndPoint" 
                                   value="<%=LodLiveEndPoint%>"/><br/> 
                        </td>
                    </tr>
                    
                    <tr>
                        <th colspan="2" 
                            style="height:40px;">Choose the default time out</th>
                    </tr>
                    <tr class="rowList" style="height: 50px;">
                        <td>(Default value is 1 min.)</td>
                        <td>
                            <select id="idTimeOut" name="TimeOut">                                
                                <option value="1">1 min.</option>
                                <option value="2">2 min.</option>
                                <option value="3">3 min.</option>
                                <option value="5">5 min.</option>                                
                            </select>
                        </td>                        
                    </tr>
                    <!-- Giuseppe LA ROCCA -->
                    
                    
                    
                    


                    <tr style="height:60px;">
                        <td colspan="2">
                            <input type="button" 
                                   id="setPreferences" 
                                   value="Set preferences" 
                                   onclick="SetPreferences()"/>
                        </td>
                    </tr>
                </table>  

            </form>


        </div>
        <!--div style="float:right">
            <img href="" 
                 src="<%=renderRequest.getContextPath()%>/images/loghiSemanticSearch3.png" 
                 style="padding-top:30px;" />
        </div-->
                 
        <div>
            <center>
                <table>   
                    <tr>
                        <td style="padding:20px;">
                            <form action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_INPUT"/></portlet:actionURL>" method="post">
                                <input type="submit" value="Back To Application"/>
                            </form>
                        </td>
                        <td>
                            <input type="reset" value="Reset" onclick="resetForm()"/></td>
                    </tr>
                </table>
            </center>
        </div> 

    </div>        

<script type="text/javascript" >
    

    <%if(OpenAgris.equals("true")){%>
        
        document.getElementById("1").checked=true;
        document.getElementById('idOpenAgrisEndPoint').disabled = false;
    <%} %>
    <%if(Europeana.equals("true")){%>
        
        document.getElementById("2").checked=true;
        document.getElementById('idEuropeanaEndPoint').disabled = false;
    <%} %>   
    <%if(CulturaItalia.equals("true")){%>
        
        document.getElementById("3").checked=true;
        document.getElementById('idCulturaItaliaEndPoint').disabled = false;
    <%} %>
    <%if(Isidore.equals("true")){%>
        
        document.getElementById("4").checked=true;
        document.getElementById('idIsidoreEndPoint').disabled = false;
    <%} %> 
    <%if(Pubmed.equals("true")){%>
        
        document.getElementById("5").checked=true;
        document.getElementById('idPubmedEndPoint').disabled = false;
    <%} %>  
    <%if(Engage.equals("true")){%>
        
        document.getElementById("6").checked=true;
        document.getElementById('idEngageEndPoint').disabled = false;
    <%} %>  
         <%if(DBPedia.equals("true")){%>
        
        document.getElementById("8").checked=true;
        document.getElementById('idDBPediaEndPoint').disabled = false;
    <%} %>  
        
    <%if(LodLive.equals("true")){%>
        
        document.getElementById("7").checked=true;
        document.getElementById('idLodLiveEndPoint').disabled = false;
    <%} %>  
        
        document.getElementById("idNumberRecords").value="<%=NumberRecordsForPage%>";
        document.getElementById("idTimeOut").value="<%=TimeOut%>";


        // This function is responsible to enable all textareas
        // when the user press the 'reset' form button
        function resetForm() {
            // Get components
       
            document.getElementById("1").checked=false;   
            document.getElementById("2").checked=false;
            document.getElementById("3").checked=false;
            document.getElementById("4").checked=false;
            document.getElementById("5").checked=false;
            document.getElementById("6").checked=false;
            document.getElementById("7").checked=false;            
            document.getElementById("idOpenAgrisEndPoint").value="http://202.45.139.84:10035/catalogs/fao/repositories/agrovoc#query";
            document.getElementById('idOpenAgrisEndPoint').disabled = true;
            document.getElementById("idEuropeanaEndPoint").value="<%=EuropeanaEndPoint_default%>";
            document.getElementById('idEuropeanaEndPoint').disabled = true;
            document.getElementById("idCulturaItaliaEndPoint").value="<%=CulturaItaliaEndPoint_default%>";
            document.getElementById('idCulturaItaliaEndPoint').disabled = true;
            document.getElementById("idIsidoreEndPoint").value="<%=IsidoreEndPoint_default%>";
            document.getElementById('idIsidoreEndPoint').disabled = true;
            document.getElementById("idPubmedEndPoint").value="<%=PubmedEndPoint_default%>";
            document.getElementById('idPubmedEndPoint').disabled = true;
            document.getElementById("idEngageEndPoint").value="<%=EngageEndPoint_default%>";
            document.getElementById('idEngageEndPoint').disabled = true;
            document.getElementById("idDBPediaEndPoint").value="<%=DBPediaEndPoint_default%>";
            document.getElementById('idDBPediaEndPoint').disabled = true;
            document.getElementById("idNumberRecords").value="10";
            document.getElementById("idTimeOut").value="1";
            document.getElementById("idLodLiveEndPoint").value="<%=LodLiveEndPoint_default%>";
            document.getElementById('idLodLiveEndPoint').disabled = true;
        }
    
    
        function SetPreferences() {
            // Get components
       
            //alert("SET PREFERENCES");
            
            var control=0;
            
            var number=document.getElementById("idNumberRecords").value;
            var timeout=document.getElementById("idTimeOut").value;
            
            //alert("NUMBER: "+number);
            var rep=new Array("NO","NO","NO","NO","NO","NO", "NO","NO");
       
            if (document.getElementById("1").checked==true)
            {
                rep[0]="OpenAgris";
            
                document.getElementById("1").value="true";
                
                if(document.getElementById("idOpenAgrisEndPoint").value==""){
                    alert("The EndPoint OpenAgris is mandatory!!!");
                    control=1;
                }
                    
            }
         
            if (document.getElementById("2").checked==true)
            {
                rep[1]="Europeana";
                document.getElementById("2").value="true"; 
                
                if(document.getElementById("idEuropeanaEndPoint").value==""){
                    alert("The EndPoint Europeana is mandatory!!!");
                    control=1;
                }
            }  
         
            if (document.getElementById("3").checked==true)
            {
                rep[2]="CulturaItalia";
                document.getElementById("3").value="true"; 
                
                if(document.getElementById("idCulturaItaliaEndPoint").value==""){
                    alert("The EndPoint CulturaItalia is mandatory!!!");
                    control=1;
                }
            }
            
            if (document.getElementById("4").checked==true)
            {
                rep[3]="Isidore";
                document.getElementById("4").value="true";
                
                if(document.getElementById("idIsidoreEndPoint").value==""){
                    alert("The EndPoint Isidore is mandatory!!!");
                    control=1;
                }
            } 
            
            if (document.getElementById("5").checked==true)
            {
                rep[4]="Pubmed";
                document.getElementById("5").value="true"; 
                
                if(document.getElementById("idPubmedEndPoint").value==""){
                    alert("The EndPoint Pubmed is mandatory!!!");
                    control=1;
                }
            } 
            
            if (document.getElementById("6").checked==true)
            {
                rep[5]="Engage";
                document.getElementById("6").value="true"; 
                
                if(document.getElementById("idEngageEndPoint").value==""){
                    alert("The EndPoint Engage is mandatory!!!");
                    control=1;
                }
            }
            
            if (document.getElementById("8").checked==true)
            {
                rep[7]="DBPedia";
                document.getElementById("8").value="true"; 
                
                if(document.getElementById("idDBPediaEndPoint").value==""){
                    alert("The EndPoint DBPedia is mandatory!!!");
                    control=1;
                }
            }
            
            if (document.getElementById("7").checked==true)
            {
                rep[6]="LodLive";
                document.getElementById("7").value="true"; 
                
                if(document.getElementById("idLodLiveEndPoint").value==""){
                    alert("The LodLive EndPoint is mandatory!!!");
                    control=1;
                }
            }
            
            var size=rep.length;
            alert("SIZE-->"+size);
            var repChoose="";            
            
            if(control==0)
            {
                // size-1 to exlude from the list of repositories 
                // the LodLive Endpoint
                for(var i=0;i<size;i++)
                { 
                     if (rep[i]!="NO") {
                         
                         repChoose+=rep[i]+",";}   
                     alert ("REP: "+repChoose);
                }

                if(repChoose=="")
                    alert("You don't have choose repositories. \n Number records for page is: "
                    + number);
                else
                    alert("You have choose repositories :"
                    + repChoose
                    + "\n Number records for page is: "
                    + number);
            }
        
              // alert("CONTROL: "+control);      
        
               if(control==0)
                    document.getElementById("<portlet:namespace/>Preferences").submit();
	                
        }
        
        function ckeckOpenAgris()
        {
            //alert("VALUE: "+document.getElementById("1").checked);            
            if(document.getElementById("1").checked==true)
                document.getElementById('idOpenAgrisEndPoint').disabled = false;
            else
               document.getElementById('idOpenAgrisEndPoint').disabled = true; 
        }
        
        function ckeckEuropeana()
        {
            //alert("VALUE: "+document.getElementById("2").checked);            
            if(document.getElementById("2").checked==true)
                document.getElementById('idEuropeanaEndPoint').disabled = false;
            else
               document.getElementById('idEuropeanaEndPoint').disabled = true; 
        }
    
        function ckeckCulturaItalia()
        {
            //alert("VALUE: "+document.getElementById("3").checked);            
            if(document.getElementById("3").checked==true)
                document.getElementById('idCulturaItaliaEndPoint').disabled = false;
            else
               document.getElementById('idCulturaItaliaEndPoint').disabled = true; 
        }
        
        function ckeckIsidore()
        {
            //alert("VALUE: "+document.getElementById("4").checked);            
            if(document.getElementById("4").checked==true)
                document.getElementById('idIsidoreEndPoint').disabled = false;
            else
               document.getElementById('idIsidoreEndPoint').disabled = true; 
        }
        
        function ckeckPubmed()
        {
            //alert("VALUE: "+document.getElementById("5").checked);            
            if(document.getElementById("5").checked==true)
                document.getElementById('idPubmedEndPoint').disabled = false;
            else
               document.getElementById('idPubmedEndPoint').disabled = true; 
        }
        
        function ckeckEngage()
        {
            //alert("VALUE: "+document.getElementById("6").checked);            
            if(document.getElementById("6").checked==true)
                document.getElementById('idEngageEndPoint').disabled = false;
            else
               document.getElementById('idEngageEndPoint').disabled = true; 
        }
        
         function ckeckDBPedia()
        {
            //alert("VALUE: "+document.getElementById("6").checked);            
            if(document.getElementById("8").checked==true)
                document.getElementById('idDBPediaEndPoint').disabled = false;
            else
               document.getElementById('idDBPediaEndPoint').disabled = true; 
        }
        
        function ckeckLodLive()
        {                        
            //alert("VALUE: "+document.getElementById("7").checked);           
            if(document.getElementById("7").checked==true)
                document.getElementById('idLodLiveEndPoint').disabled = false;
            else
               document.getElementById('idLodLiveEndPoint').disabled = true; 
        }      	
</script>




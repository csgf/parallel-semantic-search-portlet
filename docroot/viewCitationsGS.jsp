<%-- 
    Document   : viewCitationsGS
    Created on : Mar 27, 2014, 11:31:56 AM
    Author     : grid
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<portlet:defineObjects />
<%
String title_GS = renderRequest.getParameter("title_GS");
String []info_GS=(String[])request.getParameterValues("info_GS");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <center>
        <table>
            <tr>
                <td align="center" style=" padding: 5px;" >
                    <a href="http://scholar.google.com/" target="_blank"><img src="<%=renderRequest.getContextPath()%>/images/gscholar.jpeg" /></a>
                </td>
            <td align="center" style=" padding: 10px;">
                <h1><%=title_GS%></h1>
            </td> 
            </tr>
            
        </table>
        
         
    </center>   
        
         <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >Information from Google Scholar</legend>
                <%
                    if(info_GS[0].equals(" ") || info_GS[0].equals("No Information available") || info_GS[0].equals("None") || info_GS[0].equals("No Available Service"))
                    {%>
                         <p><b>Resource location: </b><%=info_GS[0]%></p>
                    <%}
                   else{%>
                     <p><b>Resource location: </b><a href="<%=info_GS[0]%>" target="_blank"><%=info_GS[0]%></a></p>
                   <%}%>
               
                <p><b>No. of versions:  </b><%=info_GS[1]%></p>
                <%
                if(info_GS[2].equals(" ") || info_GS[2].equals("No Information available") || info_GS[2].equals("None") || info_GS[2].equals("No Available Service"))
                    {%>
                         <p><b>List of versions: </b><%=info_GS[2]%></p>
                    <%}
                   else{%>
                      <p><b>List of versions: </b><a href="<%=info_GS[2]%>" target="_blank"><%=info_GS[2]%></a></p>
                   <%}%>
               
                <p><b>No. of citations: </b><%=info_GS[3]%></p>
                
                 <%
                if(info_GS[4].equals(" ") || info_GS[4].equals("No Information available") || info_GS[4].equals("None") || info_GS[4].equals("No Available Service"))
                    {%>
                          <p><b>List of citations: </b><%=info_GS[4]%></p>
                    <%}
                   else{%>
                      <p><b>List of citations: </b><a href="<%=info_GS[4]%>" target="_blank"><%=info_GS[4]%></a></p>
                   <%}%>              
                <p><b>Year: </b><%=info_GS[5]%></p>
        </fieldset> 
        <center>
            <br>  
        <div>
            <input type="button" value="<< Back" onclick="history.go( -1 );"/>
        </div>
        </center>
    </body>
    
    <!--Aggiustare l'iimagine non funzioan il link se i link sono uguali a None o No Informations non ci deve essere 
    i link
    
    No Information
    None
    Stringa vuota
    
    
    -->
    
</html>

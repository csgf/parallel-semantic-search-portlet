<%-- 
    Document   : viewDeatilsResourceOpenAgris
    Created on : Sep 17, 2013, 10:55:41 AM
    Author     : grid
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@page import="javax.portlet.*"%>
<%@page import="it.infn.ct.QueryOpenAgris"%>

<portlet:defineObjects />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body link="BLACK" vlink="red">

        <%
            String idResource = renderRequest.getParameter("idResourceOpenAgris");
             String numResourceOpenAgris = renderRequest.getParameter("numResourceOpenAgris");
             //System.out.println("numResource------->:"+numResourceOpenAgris);
             String searched_word = renderRequest.getParameter("search_word");
            //String idResource = "http://agris.fao.org/aos/records/US201300617909";
            //String idResource="http://agris.fao.org/aos/records/KR2013002489";
            //String idResource="http://agris.fao.org/aos/records/US2012406007";
            //QueryOpenAgris.ConnectionToEuropeana();
            String titles = QueryOpenAgris.getTitle(idResource);
            String authors=QueryOpenAgris.getAuthors(idResource);
            
            String authors1=authors.substring(2);
            String authorsFinal=authors1.replace("##","; ");   
            String dateSubmitted=QueryOpenAgris.getDateSubmitted(idResource);
            String description=QueryOpenAgris.getDescription(idResource);
            String description1=description.substring(2);
            String [] descriptionList=description1.split("##");
            ArrayList listPublisher= QueryOpenAgris.getPublisher(idResource);
            String subject=QueryOpenAgris.getSubject(idResource);
            String subject1=subject.substring(2);
            String [] subjectList=subject1.split("##");
            String typeRDF=QueryOpenAgris.getRDFType(idResource);
            String dctermsType=QueryOpenAgris.getdctermsType(idResource);
            
            String language=QueryOpenAgris.getLanguage(idResource);
            String rights=QueryOpenAgris.getRight(idResource);
            String alternative=QueryOpenAgris.getAlternative(idResource);
            String extent=QueryOpenAgris.getExtent(idResource);
            String source=QueryOpenAgris.getSource(idResource);
            String isPartOf=QueryOpenAgris.getIsPartOf(idResource);
            String issued=QueryOpenAgris.getIssued(idResource);
            String uri=QueryOpenAgris.getUri(idResource);
            String presentedAt=QueryOpenAgris.getPresentedAt(idResource);
            String biboAbstract=QueryOpenAgris.getAbstract(idResource);
            
            //ArrayList listPublisher=new ArrayList(); 
            //listPublisher.add("ciao##bravo");
        %>


        <div id="conteinerRecord" style="">
            <%
                String[] stitles = titles.split("##");
                for (int i = 0; i < stitles.length; i++) {
                    String title = stitles[i];
                    %> <h1 id="Title"><%=title%></h1><%
                 }

            %>
            
            <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >General Information</legend>
                
                <p class="klios_p"><b>Authors: </b><%=authorsFinal%></p>
                <p class="klios_p"><b>DateSubmitted: </b><%=dateSubmitted%></p>
                <%
                for (int j=0;j<descriptionList.length;j++)
                {
                    String desc=descriptionList[j];
                    if(!desc.equals("---")){
                   %> <p class="klios_p"><b>Description : </b><%=desc%></p>
                   <% 
                 }
                }
                   
                
                for (int j=0;j<listPublisher.size();j++)
                {
                     String publisher=listPublisher.get(j).toString();
                     if(!publisher.equals("---"))
                                                {                
                    %>
                    <p class="klios_p"><b>Publisher : </b><%=publisher%></p>
                   <% }
                }
                
                if(!subjectList[0].equals("-"))
                {
                for (int j=0;j<subjectList.length;j++)
                {
                     String mySubject=subjectList[j];
                    
                     
                                        
                    %>
                    <p class="klios_p"><b>Subject : </b><a href="<%=mySubject%>" target="_blank"><u><%=mySubject%></u></a></p>
                   <% 
                }
                }
                
                if(!dctermsType.equals("---")){
                %>
                
                <p class="klios_p"><b>Type: </b><%=typeRDF%>  ( <%=dctermsType%> ) </p>
                 <%}
                   else{
                   %>               
                   <p class="klios_p"><b>Type: </b><%=typeRDF%> </p>
                 <%}%>
                
                <%if(!language.equals("---")){
                %>
                
                <p class="klios_p"><b>Language: </b><%=language%></p>
                 <%} %>
                 
               <%if(!rights.equals("---")){
                %>
                
                <p class="klios_p"><b>Rights: </b><%=rights%></p>
                 <%} %>  
                 
               <%if(!alternative.equals("---")){
                %>
                
                <p class="klios_p"><b>Alternative: </b><%=alternative%></p>
                 <%} %> 
                 
               <%if(!extent.equals("---")){
                %>
                
                <p class="klios_p"><b>Extent: </b><%=extent%></p>
                 <%} %> 
                 
                <%if(!source.equals("---")){
                    
                    if(source.substring(0, 4).equals("http"))
                    {%>    
                    <p class="klios_p"><b>Source: </b><a href="<%=source%>" target="_blank"><u><%=source%></u></a></p>
                   
                    <%    
                    }
                   else{
                      %>    
                     <p class="klios_p"><b>Source: </b><%=source%></p>
                    <%   
                   }
                   }
                %>
                
                 
                 
                <%if(!isPartOf.equals("---")){
                %>
                
                <p class="klios_p"><b>IsPartOf: </b><%=isPartOf%></p>
                 <%} %>  
                 
                <%if(!issued.equals("---")){
                %>
                
                <p class="klios_p"><b>Issued: </b><%=issued%></p>
                 <%} %>  
                 
                 <%if(!uri.equals("---")){
                %>
                
                <p class="klios_p"><b>Uri: </b><a href="<%=uri%>" target="_blank"><u>LINK</u></a></p>
                 <%} %> 
                 
                <%if(!presentedAt.equals("---")){
                %>
                
                <p class="klios_p"><b>PresentedAt: </b><%=presentedAt%></p>
                 <%} %>
                 
                <%if(!biboAbstract.equals("---")){
                %>
                
                <p class="klios_p"><b>Abstract </b><%=biboAbstract%></p>
                 <%} %>   
                    
                
            </fieldset>   
                 <br>    
             <br>
            <a id="LinkedData" class="Link" href="http://www.chain-project.eu/LodLiveGraph/?<%=idResource%>" target="_blank">Linked Data </a>
           
            <br> 
            <br>
            <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >Repository Information</legend>
                <p class="klios_p"><b>Name: </b>  International Information System for the Agricultural Sciences and Technology (OpenAgris) </p> 
                <p class="klios_p"><b>URL : </b> <a href="http://agris.fao.org" target="_blank" title="http://agris.fao.org"><u> http://agris.fao.org </u> </a></p>
                <p class="klios_p"><b>Domain : </b>Agricultural Sciences and Technology</p>
                <p class="klios_p"><b>Project : </b>EC 7th framework program INFRA-2011-1.2.2 - Grant agr. no: 283770</p>
                <p class="klios_p"><b>Organization : </b>Food and Agriculture Organization of the United Nations (FAO)</p>
                
            </fieldset>
             <br><br>
        </div>

        <div>
           <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
           <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
            <input type="submit" value="<< Back" />
            <input  hidden="true" name="moreInfoOpenAgris" id="idMoreInfo" value="OK" />
            <input  hidden="true" name="numResourceOpenAgrisFromDetails" id="idnumResourceFromDetails" value="<%=numResourceOpenAgris%>" />
            <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
           </form>
        </div> 

    </body>
</html>

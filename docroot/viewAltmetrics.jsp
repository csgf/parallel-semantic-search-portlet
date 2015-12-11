<%-- 
    Document   : viewAltmetrics
    Created on : Dec 10, 2015, 5:12:54 PM
    Author     : grid
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.SemanticQueryMoreInfo"%>
<%@page import="it.infn.ct.SemanticQuery"%>
<%@page import="it.infn.ct.Altmetric"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<portlet:defineObjects />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <%
       String title_GS = renderRequest.getParameter("title_GS");
       String url_altmetric = renderRequest.getParameter("url_altmetric");
       String doi_altmetric = renderRequest.getParameter("doi_altmetric");
       System.out.println("url_altmetric--->"+url_altmetric);
       System.out.println("title_GS--->"+title_GS);
       System.out.println("doi_altmetric--->"+doi_altmetric);
       
       if(url_altmetric.equals("no link")){
           
       %><h1>
           
           <p style="text-align: center;">No Altmetrics available for this resource</p>
       </h1>
       <br>
      <% }
       
             else{
           
                    ArrayList arrayAltmetric = Altmetric.QueryApi(doi_altmetric);
           
                    
                    %>
                    <fieldset class="fieldsetInformations" id="IdFieldSetAltmetric">
                        <legend class="legendFieldset" >Altmetrics Information</legend>
                <%




                    //  System.out.println("repository size "+arrayRepository.size());
                    if (arrayAltmetric.size() > 0) {

                        // for(int i=0; i<arrayAlmetric.size(); i++){

                        String title_altmetric = arrayAltmetric.get(0).toString();
                        doi_altmetric = arrayAltmetric.get(1).toString();
                        String issns_altmetric = arrayAltmetric.get(2).toString();
                        String journal_altmetric = arrayAltmetric.get(3).toString();
                        String cohorts_pub_altmetric = arrayAltmetric.get(4).toString();
                        String type_altmetric = arrayAltmetric.get(5).toString();
                        String id_altmetric = arrayAltmetric.get(6).toString();
                        String schema_altmetric = arrayAltmetric.get(7).toString();
                        String cited_by_posts_count_altmetric = arrayAltmetric.get(8).toString();
                        String cited_by_tweeters_count_altmetric = arrayAltmetric.get(9).toString();
                        String cited_by_accounts_count_altmetric = arrayAltmetric.get(10).toString();
                        String last_updated_altmetric = arrayAltmetric.get(11).toString();
                        String score_altmetric = arrayAltmetric.get(12).toString();
                        String history_altmetric = arrayAltmetric.get(13).toString();
                     //   String url_altmetric = arrayAltmetric.get(14).toString();


                        String added_on_altmetric = arrayAltmetric.get(15).toString();

                        String published_on_altmetric = arrayAltmetric.get(16).toString();

                        String readers_altmetric = arrayAltmetric.get(17).toString();


                        String readers_count_altmetric = arrayAltmetric.get(18).toString();



                        String images_altmetric = arrayAltmetric.get(19).toString();

                        String details_url_altmetric = arrayAltmetric.get(20).toString();
                        System.out.println("URLLLLLL" + details_url_altmetric);





                        //System.out.println("URL REP "+urlRep);

                %>
                
                    
                
                <p class="klios_p"><b>Title: </b> <%=title_altmetric%></p> 
                <p class="klios_p"><b>DOI: </b> <%=doi_altmetric%></p>
                <p class="klios_p"><b>ISSNS: </b> <%=issns_altmetric%></p>
                <p class="klios_p"><b>JOURNAL: </b> <%=journal_altmetric%></p>
                <p class="klios_p"><b>COHORTS PUB: </b> <%=cohorts_pub_altmetric%></p>
                <p class="klios_p"><b>TYPE: </b> <%=type_altmetric%></p>
                <p class="klios_p"><b>ID: </b> <%=id_altmetric%></p>
                <p class="klios_p"><b>SCHEMA: </b> <%=schema_altmetric%></p>
                <p class="klios_p"><b>CITED BY POSTS COUNT: </b> <%=cited_by_posts_count_altmetric%></p>
                <p class="klios_p"><b>CITED BY TWEETERS COUNT: </b> <%=cited_by_tweeters_count_altmetric%></p>
                <p class="klios_p"><b>CITED BY ACCOUNTS COUNT: </b> <%=cited_by_accounts_count_altmetric%></p>
                <p class="klios_p"><b>LAST UPDATED: </b> <%=last_updated_altmetric%></p>
                <p class="klios_p"><b>SCORE: </b> <%=score_altmetric%></p>
                <p class="klios_p"><b>HISTORY: </b> <%=history_altmetric%></p>
                <p class="klios_p"><b>URL: </b><a href="<%=url_altmetric%>" target="_blank"> <%=url_altmetric%></a></p>
                <p class="klios_p"><b>ADDED ON: </b> <%=added_on_altmetric%></p>
                <p class="klios_p"><b>PUBLISHED ON: </b> <%=published_on_altmetric%></p>
                <p class="klios_p"><b>READERS: </b> <%=readers_altmetric%></p>
                <p class="klios_p"><b>READERS COUNT: </b> <%=readers_count_altmetric%></p>
                <p class="klios_p"><b>IMAGES: </b><img src="<%=images_altmetric%>"> </p>
                <p class="klios_p"><b>DETAILS URL: </b> <a href="<%=details_url_altmetric%>"  target="_blank"><%=details_url_altmetric%></a></p>



                <%
                        // }
                    }
                %>
            </fieldset>
            <br>
                    
           
           
            <% }
        %>
       

        
        <div style="text-align: center;">
            <input type="button" value="<< Back" onclick="history.go( -1 );"/>
        </div>
    </body>
</html>

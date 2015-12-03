<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.QueryEngage"%>
<%@page import="it.infn.ct.SemanticQuery"%>
<%@page import="javax.portlet.*"%>


<portlet:defineObjects />
<%//
    // Application Submission page
    //
    //
    // The portlet code assigns the jobIdentifier as input parameter for this jsp file
    //
%>

<%
// Below the submission web form
//
// It only have a button that will show the input form again for a new job submission
%>



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />



    </head>
    <body link="BLACK" vlink="red">

        <div id="conteinerRecord" style="">
            <%
                String LodLiveEndPoint = renderRequest.getParameter("LodLiveEndPoint");
                String idResourceEngage = renderRequest.getParameter("idResourceEngage");
                String res="www.engagedata.eu/rdf/dataset/";
                String serialNumbResource= idResourceEngage.substring(7).split("/")[2];
                String resourceEng=res+""+serialNumbResource; 
               //System.out.println("RISORSA DI ENGAGE===>>"+resourceEng);
                
              
                String numResourceEngage = renderRequest.getParameter("numResourceEngage");
              
                String searched_word = renderRequest.getParameter("search_word");
                String sEngageTitle = QueryEngage.getEngageTitle(idResourceEngage);
              
                //String sEngageID = QueryEngage.getEngageIdentifier(idResourceEuropeana);
                String linkHomepage = idResourceEngage;
                
               
                

                

                String sEngageAuthors = QueryEngage.getEngageAuthors(idResourceEngage);
                
               String authors="";
                if(!sEngageAuthors.equals("")){
                authors = sEngageAuthors.replace("##", ";").substring(1);

                               }

                String sEngageDescriptions = QueryEngage.getEngageDescription(idResourceEngage);
               
                ArrayList arrayEngagePublisher = QueryEngage.getEngagePublisher(idResourceEngage);
                ArrayList arrayEngageKeywords = QueryEngage.getEngageKeywords(idResourceEngage);
                String engageLanguage = QueryEngage.getEngageLanguage(idResourceEngage);
                String engageDate = QueryEngage.getEngageDate(idResourceEngage);
                String engageModified = QueryEngage.getEngageModified(idResourceEngage);
                String engageTemporal = QueryEngage.getEngageTemporal(idResourceEngage);
               // ArrayList arrayEngageContributor = QueryEngage.getEngageContributor(idResourceEngage);
                String coverageEngage = QueryEngage.getEngageCoverage(idResourceEngage);
               // String[] distributions=QueryEngage.getEngageDistribution(idResourceEngage);
                String distributionTitle=QueryEngage.getEngageDistributionTitle(idResourceEngage);
                String distributionAccessURL=QueryEngage.getEngageDistributionAccessURL(idResourceEngage);
                String distributionDownloadURL=QueryEngage.getEngageDistributionDownloadURL(idResourceEngage);
                
               

            %>



            <br>
            <%
                if (sEngageTitle.length() != 0) {
                    String[] listTitleEngage = sEngageTitle.split("##");

                    for (int j = 0; j < listTitleEngage.length; j++) {
                        if (listTitleEngage.length > 1) {

                            if (linkHomepage!="") {
                                if (!listTitleEngage[j].equals("")) {


            %>
            <a id="Title" href="<%=linkHomepage%>" target="_blank" style="color:black"><h1><u><%=listTitleEngage[j]%></u></h1></a> 

            <%}
            } else {%>
            <h1 id="Title"><u><%=listTitleEngage[j]%></u></h1>               
            <% }
                        }

                    }


                }



            %>
            <br><br>   

            <div>

                <fieldset class="fieldsetInformations">
                    <legend class="legendFieldset" >General Information</legend>
                    <%if (authors != "") {%>
                    <p class="klios_p"><b>Authors: </b><%=authors%></p>
                    <%}
                    
                        //DESCRIPTION
                        if (sEngageDescriptions.length() != 0) {
                            String[] listDescritionEngage = sEngageDescriptions.split("##");
                            for (int j = 0; j < listDescritionEngage.length; j++) {
                                if (listDescritionEngage.length > 1) {

                                    if (!listDescritionEngage[j].equals("")) {

                    %><p class="klios_p"><b>Description: </b><%=listDescritionEngage[j]%></p>  
                    <%}
                    } else {

                    %><p class="klios_p"><b>Description: </b><%=listDescritionEngage[j]%></p>  
                    <%

                                }
                            }
                        }

                        //PUBLISHER
                        if (arrayEngagePublisher.size() != 0) {
                            //System.out.println("sono dentro publisher " + arrayEuropeanaPublisher.size());
                            for (int j = 0; j < arrayEngagePublisher.size(); j++) {
                    %>
                    <p class="klios_p"><b>Publisher: </b><%=arrayEngagePublisher.get(j).toString()%></p> 
                    <%  }


                        }
                
                        //KEYWORDS
                        if (arrayEngageKeywords.size() != 0) {
                            //System.out.println("sono dentro publisher " + arrayEuropeanaPublisher.size());
                            for (int j = 0; j < arrayEngageKeywords.size(); j++) {
                    %>
                    <p class="klios_p"><b>Keyword : </b><%=arrayEngageKeywords.get(j).toString()%></p> 
                    <%  }


                        }


                
                    
                   
                  

                        //LANGUAGE

                        if (engageLanguage!="") {

                            
                    %>
                    <p class="klios_p"><b>Language  : </b><%=engageLanguage%></p> 
                    <%  }


                        
                        //DATE
                        if (engageDate !="") {

                           
                    %>
                    <p class="klios_p"><b>Date  : </b><%=engageDate%></p> 
                    <%  }

                     //MODIFIED
                        if (engageModified !="") {

                           
                    %>
                    <p class="klios_p"><b>Modified : </b><%=engageModified%></p> 
                    <%  }
                
                     //TEMPORAL
                        if (engageTemporal !="") {

                           
                    %>
                    <p class="klios_p"><b>Temporal : </b><%=engageTemporal%></p> 
                    <%  }

                        

               


                       
                        //COVERAGE
                        if (!coverageEngage.equals("")) {


                    %>
                    <p class="klios_p"><b>Coverage : </b><%=coverageEngage%></p>    
                    <%}
                        


                    %>

                </fieldset>
            </div>
                    
             <br>
            <a id="LinkedData" class="Link" href="<%=LodLiveEndPoint%>/?<%=resourceEng%>" target="_blank">Linked Data </a>
           
            <br>         
            <br>    

            
            <div> 
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Dataset Information</legend>
                    <%
                    String titleDistribution="no title";
                    String downloadURL="no url";
                    String accessURL="no url";
                    
                 
                      
                      if(distributionTitle!="")
                         titleDistribution= distributionTitle;
                   
                      
                      
                      if(distributionAccessURL!="")
                         accessURL=  distributionAccessURL; 
                      
                      if(distributionDownloadURL!="")
                         downloadURL= distributionDownloadURL;  
                                 
                       
                   
                    
                    %>
                    <p class="klios_p"><b>Distribution title : </b><%=titleDistribution%></p>    
                    
                    <% if(downloadURL.length()>4 && downloadURL.substring(0,4).equals("http")){%>
                    <p class="klios_p"> <b>Download URL: </b><a href="<%=downloadURL%>" target="_blank" style="color:black"><u><%=downloadURL%></u></a> </p>
                    
                    <% }else{ %>
                    <p class="klios_p"><b>Download URL: </b><%=downloadURL%></p>    
                   <%}%>
                   
                   
                    <% if(accessURL.length()>4 && accessURL.substring(0,4).equals("http")){%>
                    <p class="klios_p"> <b>Access URL: </b><a href="<%=accessURL%>" target="_blank" style="color:black"><u><%=accessURL%></u></a> </p>
                    
                    <% }else{ %>
                    <p class="klios_p"><b>Access URL: </b><%=accessURL%></p>    
                   <%}%>
                </fieldset>
            </div>

            <br>
            <div>
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Repository Information</legend>
                    <p class="klios_p"><b>Name: </b> Engage   </p> 
                    <p class="klios_p"><b>URL : </b> <a href="http://www.engagedata.eu/" target="_blank" title="http://www.engagedata.eu/"><u>http://www.engagedata.eu/</u> </a></p>
                    <p class="klios_p"><b>SPARQL endpoint : </b><a href="http://engagesrv.epu.ntua.gr:8890/sparql" target="_blank"><u>http://engagesrv.epu.ntua.gr:8890/sparql</u></a></p>
                    <p class="klios_p"><b>Domain : </b> e-government </p>


                </fieldset>
            </div>
            <br>
        </div>
        <br>
        <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfoEngage" id="idMoreInfoEngage" value="OK" />
                <input  hidden="true" name="numResourceEngageFromDetails" id="idnumResourceFromDetails" value="<%=numResourceEngage%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 
    </body>

</html>

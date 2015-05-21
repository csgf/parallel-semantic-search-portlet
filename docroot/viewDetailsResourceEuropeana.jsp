<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.QueryEuropeana"%>
<%@page import="it.infn.ct.SemanticQuery"%>



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
                String idResourceEuropeana = renderRequest.getParameter("idResourceEuropeana");
                String numResourceEuropeana = renderRequest.getParameter("numResourceEuropeana");
                String searched_word = renderRequest.getParameter("search_word");
                String sEuropeanaTitle = QueryEuropeana.getEuropeanaTitle(idResourceEuropeana);
                String sEuropeanaID = QueryEuropeana.getEuropeanaIdentifier(idResourceEuropeana);
                ArrayList id_httpEuropeana = new ArrayList();
                String id_DOI = "";
                String link_img="";

                int c = 0;

                if (sEuropeanaID.length() > 0) {
                    String[] idfs = sEuropeanaID.split("##");
                    for (int ii = 0; ii < idfs.length; ii++) {
                        if (idfs[ii].length() > 4 && idfs[ii].substring(0, 4).equals("http")) {
                            id_httpEuropeana.add(idfs[ii].toString());
                             String extLink=idfs[ii].toString().substring(idfs[ii].toString().length()-3, idfs[ii].toString().length());
                           
                           if(extLink.equals("jpg")){  
                            //System.out.println("LINK IMG "+idfs[ii].toString()+" --- "+extLink);
                           
                                
                            link_img=idfs[ii].toString();
                                
                            
                         }
                        } else {

                            //CONTROLLO SE E' UN DOI:
                            if (idfs[ii].length() > 3 && idfs[ii].substring(0, 3).equals("doi")) {

                                id_DOI = idfs[ii].toString();
                            }
                        }

                    }
                }
                String sEuropeanaAuthors = QueryEuropeana.getEuropeanaAuthors(idResourceEuropeana);
                String authors = "";
                if (sEuropeanaAuthors.length() != 0) {
                    authors = sEuropeanaAuthors.replace("##", ";").substring(1);
                    //System.out.println("autori dopo replace");
                }

                String sEuropeanaDescriptions = QueryEuropeana.getEuropeanaDescription(idResourceEuropeana);
                ArrayList arrayEuropeanaPublisher = QueryEuropeana.getEuropeanaPublisher(idResourceEuropeana);
                ArrayList arrayEuropeanaSubject = QueryEuropeana.getEuropeanaSubject(idResourceEuropeana);
                ArrayList arrayEuropeanaSource = QueryEuropeana.getEuropeanaSource(idResourceEuropeana);
                ArrayList arrayEuropeanaLanguage = QueryEuropeana.getEuropeanaLanguage(idResourceEuropeana);
                ArrayList arrayEuropeanaDate = QueryEuropeana.getEuropeanaDate(idResourceEuropeana);
                ArrayList arrayEuropeanaContributor = QueryEuropeana.getEuropeanaContributor(idResourceEuropeana);
                String coverageInfo = QueryEuropeana.getEuropeanaCoverage(idResourceEuropeana);
                ArrayList arrayEuropeanaRights = QueryEuropeana.getEuropeanaRights(idResourceEuropeana);
                String sEuropeanaType = QueryEuropeana.getEuropeanaType(idResourceEuropeana);
                ArrayList arrayEuropeanaFormat = QueryEuropeana.getEuropeanaFormat(idResourceEuropeana);
                ArrayList arrayEuropeanaRelation = QueryEuropeana.getEuropeanaRelation(idResourceEuropeana);

            %>



            <br>
            <%
                if (sEuropeanaTitle.length() != 0) {
                    String[] listTitleEuropeana = sEuropeanaTitle.split("##");

                    for (int j = 0; j < listTitleEuropeana.length; j++) {
                        if (listTitleEuropeana.length > 1) {

                            if (id_httpEuropeana.size() > 0) {
                                if (!listTitleEuropeana[j].equals("")) {

                                  //  System.out.println("TITOLO:>>>" + listTitleEuropeana[j].toString());
            %>
            <a id="Title" href="<%=id_httpEuropeana.get(0).toString()%>" target="_blank" style="color:black"><h1><u><%=listTitleEuropeana[j]%></u></h1></a> 

            <%}
            } else {%>
            <h1 id="Title"><u><%=listTitleEuropeana[j]%></u></h1>               
            <% }
                        }

                    }


                }



            %>
            <br><br>   

            <div>

                <fieldset class="fieldsetInformations">
                    <legend class="legendFieldset" >General Information</legend>
                    <%if (sEuropeanaAuthors != "") {%>
                    <p class="klios_p"><b>Authors: </b><%=authors%></p>
                    <%}%>
                    <%
                        //DESCRIPTION
                        if (sEuropeanaDescriptions.length() != 0) {
                            String[] listDescritionEuropeana = sEuropeanaDescriptions.split("##");
                            for (int j = 0; j < listDescritionEuropeana.length; j++) {
                                if (listDescritionEuropeana.length > 0) {

                                    if (!listDescritionEuropeana[j].equals("")) {

                    %><p class="klios_p"><b>Description : </b><%=listDescritionEuropeana[j]%></p>  
                    <%}
                    } 
                            }
                        }

                        //PUBLISHER
                        if (arrayEuropeanaPublisher.size() != 0) {
                           // System.out.println("sono dentro publisher " + arrayEuropeanaPublisher.size());
                            for (int j = 0; j < arrayEuropeanaPublisher.size(); j++) {
                    %>
                    <p class="klios_p"><b>Publisher : </b><%=arrayEuropeanaPublisher.get(j).toString()%></p> 
                    <%  }


                        }





                        //IDENTIFIER
                        if (sEuropeanaID.length() != 0) {

                            String[] listIdentifierEuropeana = sEuropeanaID.split("##");
                            for (int j = 0; j < listIdentifierEuropeana.length; j++) {
                                String identifierResource = listIdentifierEuropeana[j];

                                if (listIdentifierEuropeana.length > 1) {


                                    if (!listIdentifierEuropeana[j].equals("")) {

                                        if (identifierResource.length() > 4 && identifierResource.substring(0, 4).equals("http")) {
                                           // System.out.println("la j vale dall'if " + j);

                    %><p class="klios_p"><b>Identifier : </b><a href="<%=identifierResource%>" target="_blank"><u><%=identifierResource%> </u></a></p> 
                    <%} else {
                       // System.out.println("la j vale dall'else " + j);
                    %>
                    <p class="klios_p"><b>Identifier : </b><%=identifierResource%></p> 

                    <%}

                        }

                    } else {
                        if (!identifierResource.equals("")) {

                            if (identifierResource.length() > 4 && identifierResource.substring(0, 4).equals("http")) {
                    %><p class="klios_p"><b>Identifier : </b><a href="<%=identifierResource%>" target="_blank"><%=identifierResource%> </a></p> 
                    <%} else {
                        }


                    %>
                    <p class="klios_p"><b>Identifier : </b><%=identifierResource%></p> 

                    <%}



                                }

                            }
                        }

                        //SOURCE
                        if (arrayEuropeanaSource.size() != 0) {
                            //System.out.println("sono dentro source " + arrayEuropeanaSource.size());
                            for (int j = 0; j < arrayEuropeanaSource.size(); j++) {

                                if (arrayEuropeanaSource.get(j).toString().length() > 4 && arrayEuropeanaSource.get(j).toString().substring(0, 4).equals("http")) {
                                    String linksource = arrayEuropeanaSource.get(j).toString();
                    %>
                    <p class="klios_p"><b>Source : </b><a href="<%=linksource%>" target="_blank" value="<%=linksource%>"><u><%=linksource%></u></a></p> 
                    <%  } else {%>

                    <p class="klios_p"><b>Source : </b><%=arrayEuropeanaSource.get(j).toString()%></p> 
                    <%  }
                            }
                        }
                        //SUBJECT
                        if (arrayEuropeanaSubject.size() != 0) {
                            //System.out.println("sono dentro subject " + arrayEuropeanaSubject.size());
                            for (int j = 0; j < arrayEuropeanaSubject.size(); j++) {

                                if (arrayEuropeanaSubject.get(j).toString().length() > 4 && arrayEuropeanaSubject.get(j).toString().substring(0, 4).equals("http")) {
                                    String linksubject = arrayEuropeanaSubject.get(j).toString();
                    %>
                    <p class="klios_p"><b>Subject : </b><a href="<%=linksubject%>" target="_blank" value="<%=linksubject%>"><u><%=linksubject%></u></a></p> 
                    <%  } else {

                        String sbj = arrayEuropeanaSubject.get(j).toString().replace(",", ", ");

                    %>
                    <p class="klios_p"><b>Subject : </b><%=sbj%></p> 
                    <%  }


                            }
                        }

                        //LANGUAGE

                        if (arrayEuropeanaLanguage.size() != 0) {
                            //System.out.println("sono dentro language " + arrayEuropeanaLanguage.size());
                            for (int j = 0; j < arrayEuropeanaLanguage.size(); j++) {
                    %>
                    <p class="klios_p"><b>Language : </b><%=arrayEuropeanaLanguage.get(j).toString()%></p> 
                    <%  }


                        }
                        //DATE
                        if (arrayEuropeanaDate.size() != 0) {
                            //System.out.println("sono dentro date " + arrayEuropeanaDate.size());
                            for (int j = 0; j < arrayEuropeanaDate.size(); j++) {
                    %>
                    <p class="klios_p"><b>Date : </b><%=arrayEuropeanaDate.get(j).toString()%></p> 
                    <%  }


                        }

                        //CONTRIBUTOR

                        if (arrayEuropeanaContributor.size() != 0) {
                            //System.out.println("sono dentro contributor " + arrayEuropeanaContributor.size());
                            for (int j = 0; j < arrayEuropeanaContributor.size(); j++) {
                    %>
                    <p class="klios_p"><b>Contributor : </b><%=arrayEuropeanaContributor.get(j).toString()%></p> 
                    <%  }


                        }
                        //COVERAGE
                        if (!coverageInfo.equals("")) {
                           // System.out.println("sono dentro coverage " + coverageInfo);

                    %>
                    <p class="klios_p"><b>Coverage : </b><%=coverageInfo%></p>    
                    <%}
                        //RIGHT


                        if (arrayEuropeanaRights.size() != 0) {
                            //System.out.println("sono dentro contributor " + arrayEuropeanaRights.size());
                            for (int j = 0; j < arrayEuropeanaRights.size(); j++) {
                    %>
                    <p class="klios_p"><b>Rights : </b><%=arrayEuropeanaRights.get(j).toString()%></p> 
                    <%  }


                        }


                    %>

                </fieldset>
            </div>
                         <br>
            <a id="LinkedData" class="Link" href="http://www.chain-project.eu/LodLiveGraph/?<%=idResourceEuropeana%>" target="_blank">Linked Data </a>
           
            <br> 
            <br>    

            <div> 
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Dataset Information</legend>
                    <%
                         //IMG
                    if(!link_img.equals("")){%>
                     <p class="klios_p"> <a href="<%=link_img%>" target="_blank" style="cursor: -webkit-zoom-in; cursor: -moz-zoom-in;" > <img src="<%=link_img%>" style="height: 100px; width:100px;"></a></p>   
                   <% }
                        //TYPE DATA SET
                        if (sEuropeanaType.length() != 0) {
                            String[] listTypeEuropeana = sEuropeanaType.split("##");

                            for (int j = 0; j < listTypeEuropeana.length; j++) {
                                if (listTypeEuropeana.length > 1) {

                                    if (!listTypeEuropeana[j].equals("")) {

                    %><p class="klios_p"><b>Type : </b><%=listTypeEuropeana[j]%></p>  
                    <%}
                    } else {%>


                    <p class="klios_p"><b>Type : </b><%=listTypeEuropeana[j]%></p>  

                    <%
                                }
                            }
                        }

                        //FORMAT DATA SET

                        if (arrayEuropeanaFormat.size() != 0) {
                            //System.out.println("sono dentro format " + arrayEuropeanaFormat.size());
                            for (int j = 0; j < arrayEuropeanaFormat.size(); j++) {
                    %>
                    <p class="klios_p"><b>Format : </b><%=arrayEuropeanaFormat.get(j).toString()%></p> 
                    <%  }

                                                       }

                        if (id_DOI != "") 
                        {%>
                         <p class="klios_p"><b>Identifier DOI : </b><a href="http://dx.doi.org/<%=id_DOI%>" target="_blank"><u><%=id_DOI%></u></a></p> 
                      <% }
                        //RELATION DATA SET

                        if (arrayEuropeanaRelation.size() != 0) {
                            //System.out.println("sono dentro format " + arrayEuropeanaRelation.size());
                            for (int j = 0; j < arrayEuropeanaRelation.size(); j++) {
                    %>
                    <p class="klios_p"><b>Relation : </b><%=arrayEuropeanaRelation.get(j).toString()%></p> 
                    <%  }


                        }
                    %>
                </fieldset>
            </div>
            

            <br>
            <div>
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Repository Information</legend>
                    <p class="klios_p"><b>Name: </b> Europeana   </p> 
                    <p class="klios_p"><b>URL : </b> <a href="http://www.europeana.eu/" target="_blank" title="http://www.europeana.eu/"><u>http://www.europeana.eu/</u> </a></p>
                    <p class="klios_p"><b>SPARQL endpoint : </b><a href="http://europeana.ontotext.com/sparql" target="_blank"><u>http://europeana.ontotext.com/sparql</u></a></p>
                    <p class="klios_p"><b>Domain : </b>Cultural Heritage</p>


                </fieldset>
            </div>
            <br>
        </div>
        <br>
        <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfoEuropeana" id="idMoreInfoEuropeana" value="OK" />
                <input  hidden="true" name="numResourceEuropeanaFromDetails" id="idnumResourceFromDetails" value="<%=numResourceEuropeana%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 
    </body>

</html>

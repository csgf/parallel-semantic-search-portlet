



<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.QueryPubMed"%>
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
                 String idResourcePubmed = renderRequest.getParameter("idResourcePubmed");
                 String numResourcePubmed = renderRequest.getParameter("numResourcePubmed");
                 String searched_word = renderRequest.getParameter("search_word");
                String sPubmedTitle = QueryPubMed.getPubmedTitle(idResourcePubmed);
                String sPubmedID = QueryPubMed.getPubmedIdentifier(idResourcePubmed);
                ArrayList id_httpPubmed = new ArrayList();
                int c = 0;

                if (sPubmedID.length() > 0) {
                    String[] idfs = sPubmedID.split("##");
                    for (int ii = 0; ii < idfs.length; ii++) {
                        if (idfs[ii].length() > 4 && idfs[ii].substring(0, 4).equals("http")) {
                            id_httpPubmed.add(idfs[ii].toString());
                        }

                    }
                }
                String sPubmedAuthors = QueryPubMed.getPubmedAuthors(idResourcePubmed);
                String authors = "";
                if (sPubmedAuthors.length() != 0) {
                    authors = sPubmedAuthors.replace("##", ";").substring(1);
                    //System.out.println("autori dopo replace");
                }

                String sPubmedDescriptions = QueryPubMed.getPubmedDescription(idResourcePubmed);
                ArrayList arrayPubmedPublisher = QueryPubMed.getPubmedPublisher(idResourcePubmed);
              //  ArrayList arrayIsidoreSubject = QueryPubMed.getIsidoreSubject(idResourceIsidore);
               // ArrayList arrayIsidoreSource = QueryPubMed.getIsidoreSource(idResourceIsidore);
                ArrayList arrayPubmedLanguage = QueryPubMed.getPubmedLanguage(idResourcePubmed);
                ArrayList arrayPubmedDate = QueryPubMed.getPubmedDate(idResourcePubmed);
               // ArrayList arrayIsidoreContributor = QueryPubMed.getIsidoreContributor(idResourceIsidore);
               // String coverageInfo = QueryPubMed.getIsidoreCoverage(idResourceIsidore);
               // ArrayList arrayIsidoreRights = QueryPubMed.getIsidoreRights(idResourceIsidore);
                ArrayList arrayPubmedType = QueryPubMed.getPubmedType(idResourcePubmed);
              //  ArrayList arrayIsidoreFormat = QueryPubMed.getIsidoreFormat(idResourceIsidore);
              //  ArrayList arrayIsidoreRelation = QueryPubMed.getIsidoreRelation(idResourceIsidore);

            %>



            <br>
            <%
                if (sPubmedTitle.length() != 0) {
                    String[] listTitlePubmed = sPubmedTitle.split("##");

                    for (int j = 0; j < listTitlePubmed.length; j++) {
                        if (listTitlePubmed.length > 1) {

                            if (id_httpPubmed.size() > 0) {
                                if (!listTitlePubmed[j].equals("")) {

                                    //System.out.println("TITOLO:>>>" + listTitleIsidore[j].toString());
            %>
            <a href="<%=id_httpPubmed.get(0).toString()%>" target="_blank" style="color:black"><h1>(<%=(c + 1)%>)<u><%=listTitlePubmed[j]%></u></h1></a> 

            <%}
            } else {%>
            <h1><u><%=listTitlePubmed[j]%></u></h1>               
            <% }
                        }

                    }


                }



            %>
            <br><br>   

            <div>

                <fieldset class="fieldsetInformations">
                    <legend class="legendFieldset" >General Information</legend>
                    <%if (sPubmedAuthors != "") {%>
                    <p class="klios_p"><b>Authors: </b><%=authors%></p>
                    <%}%>
                    <%
                        //DESCRIPTION
                        if (sPubmedDescriptions.length() != 0) {
                            String[] listDescritionPubmed = sPubmedDescriptions.split("##");
                            for (int j = 0; j < listDescritionPubmed.length; j++) {
                                if (listDescritionPubmed.length > 1) {

                                    if (!listDescritionPubmed[j].equals("")) {

                    %><p class="klios_p"><b>Description (<%=(j)%>) : </b><%=listDescritionPubmed[j]%></p>  
                    <%}
                    } else {

                    %><p class="klios_p"><b>Description : </b><%=listDescritionPubmed[j]%></p>  
                    <%

                                }
                            }
                        }

               





                        //IDENTIFIER
                        if (sPubmedID.length() != 0) {

                            String[] listIdentifierPubmed= sPubmedID.split("##");
                            for (int j = 0; j < listIdentifierPubmed.length; j++) {
                                String identifierResource = listIdentifierPubmed[j];

                                if (listIdentifierPubmed.length > 1) {


                                    if (!listIdentifierPubmed[j].equals("")) {

                                        if (identifierResource.length() > 4 && identifierResource.substring(0, 4).equals("http")) {
                                            //System.out.println("la j vale dall'if " + j);

                    %><p class="klios_p"><b>Identifier (<%=(j)%>) : </b><a href="<%=identifierResource%>" target="_blank"><u><%=identifierResource%> </u></a></p> 
                    <%} else {
                        //System.out.println("la j vale dall'else " + j);
                    %>
                    <p class="klios_p"><b>Identifier (<%=(j)%>) : </b><%=identifierResource%></p> 

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

                
                  //LANGUAGE

                        if (arrayPubmedLanguage.size() != 0) {
                            //System.out.println("sono dentro language " + arrayIsidoreLanguage.size());
                            for (int j = 0; j < arrayPubmedLanguage.size(); j++) {
                    %>
                    <p class="klios_p"><b>Language (<%=(j + 1)%>) : </b><%=arrayPubmedLanguage.get(j).toString()%></p> 
                    <%  }


                        }
                        //DATE
                        if (arrayPubmedDate.size() != 0) {
                            //System.out.println("sono dentro date " + arrayIsidoreDate.size());
                            for (int j = 0; j < arrayPubmedDate.size(); j++) {
                    %>
                    <p class="klios_p"><b>Date (<%=(j + 1)%>) : </b><%=arrayPubmedDate.get(j).toString()%></p> 
                    <%  }


                        }
                  //TYPE
                        if (arrayPubmedType.size() != 0) {
                            //System.out.println("sono dentro date " + arrayIsidoreDate.size());
                            for (int j = 0; j < arrayPubmedType.size(); j++) {
                    %>
                    <p class="klios_p"><b>Type (<%=(j + 1)%>) : </b><%=arrayPubmedType.get(j).toString()%></p> 
                    <%  }


                        }
                       
                  //PUBLISHER
                        if (arrayPubmedPublisher.size() != 0) {
                            //System.out.println("sono dentro date " + arrayIsidoreDate.size());
                            for (int j = 0; j < arrayPubmedPublisher.size(); j++) {
                    %>
                    <p class="klios_p"><b>Affiliation (<%=(j + 1)%>) : </b><%=arrayPubmedPublisher.get(j).toString()%></p> 
                    <%  }


                        }
                     
                    %>


                </fieldset>
            </div>
            <br>    

            
            <div>
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Repository Information</legend>
                    <p class="klios_p"><b>Name: </b> Pubmed   </p> 
                    <p class="klios_p"><b>URL : </b> <a href="http://www.ncbi.nlm.nih.gov/pubmed" target="_blank" title="http://www.ncbi.nlm.nih.gov/pubmed"><u>http://www.ncbi.nlm.nih.gov/pubmed</u> </a></p>
                    <p class="klios_p"><b>SPARQL endpoint : </b><a href="http://pubmed.bio2rdf.org/sparql" target="_blank"><u>http://pubmed.bio2rdf.org/sparql</u></a></p>
                    <p class="klios_p"><b>Country Code : </b>USA</p>
                    <p class="klios_p"><b>Domain : </b>Medicine and Health</p>


                </fieldset>
            </div>
            <br>
        </div>
        <br>
         <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfoPubmed" id="idMoreInfoPubmed" value="OK" />
                <input  hidden="true" name="numResourcePubmedFromDetails" id="idnumResourceFromDetails" value="<%=numResourcePubmed%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 
    </body>

</html>





<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.QueryIsidore"%>
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
                 String idResourceIsidore = renderRequest.getParameter("idResourceIsidore");
                 String numResourceIsidore = renderRequest.getParameter("numResourceIsidore");
                 String searched_word = renderRequest.getParameter("search_word");
                String sIsidoreTitle = QueryIsidore.getIsidoreTitle(idResourceIsidore);
                String sIsidoreID = QueryIsidore.getIsidoreIdentifier(idResourceIsidore);
                ArrayList id_httpIsidore = new ArrayList();
                int c = 0;

                if (sIsidoreID.length() > 0) {
                    String[] idfs = sIsidoreID.split("##");
                    for (int ii = 0; ii < idfs.length; ii++) {
                        if (idfs[ii].length() > 4 && idfs[ii].substring(0, 4).equals("http")) {
                            id_httpIsidore.add(idfs[ii].toString());
                        }

                    }
                }
                String sIsidoreAuthors = QueryIsidore.getIsidoreAuthors(idResourceIsidore);
                String authors = "";
                if (sIsidoreAuthors.length() != 0) {
                    authors = sIsidoreAuthors.replace("##", ";").substring(1);
                    //System.out.println("autori dopo replace");
                }

                String sIsidoreDescriptions = QueryIsidore.getIsidoreDescription(idResourceIsidore);
                ArrayList arrayIsidorePublisher = QueryIsidore.getIsidorePublisher(idResourceIsidore);
                ArrayList arrayIsidoreSubject = QueryIsidore.getIsidoreSubject(idResourceIsidore);
                ArrayList arrayIsidoreSource = QueryIsidore.getIsidoreSource(idResourceIsidore);
                ArrayList arrayIsidoreLanguage = QueryIsidore.getIsidoreLanguage(idResourceIsidore);
                ArrayList arrayIsidoreDate = QueryIsidore.getIsidoreDate(idResourceIsidore);
                ArrayList arrayIsidoreContributor = QueryIsidore.getIsidoreContributor(idResourceIsidore);
                String coverageInfo = QueryIsidore.getIsidoreCoverage(idResourceIsidore);
                ArrayList arrayIsidoreRights = QueryIsidore.getIsidoreRights(idResourceIsidore);
                ArrayList arrayIsidoreType = QueryIsidore.getIsidoreType(idResourceIsidore);
                ArrayList arrayIsidoreFormat = QueryIsidore.getIsidoreFormat(idResourceIsidore);
                ArrayList arrayIsidoreRelation = QueryIsidore.getIsidoreRelation(idResourceIsidore);

            %>



            <br>
            <%
                if (sIsidoreTitle.length() != 0) {
                    String[] listTitleIsidore = sIsidoreTitle.split("##");

                    for (int j = 0; j < listTitleIsidore.length; j++) {
                        if (listTitleIsidore.length > 1) {

                            if (id_httpIsidore.size() > 0) {
                                if (!listTitleIsidore[j].equals("")) {

                                    //System.out.println("TITOLO:>>>" + listTitleIsidore[j].toString());
            %>
            <a id="Title" href="<%=id_httpIsidore.get(0).toString()%>" target="_blank" style="color:black"><h1><u><%=listTitleIsidore[j]%></u></h1></a> 

            <%}
            } else {%>
            <h1 id="Title"><u><%=listTitleIsidore[j]%></u></h1>               
            <% }
                        }

                    }


                }



            %>
            <br><br>   

            <div>

                <fieldset class="fieldsetInformations">
                    <legend class="legendFieldset" >General Information</legend>
                    <%if (sIsidoreAuthors != "") {%>
                    <p class="klios_p"><b>Authors: </b><%=authors%></p>
                    <%}%>
                    <%
                        //DESCRIPTION
                        if (sIsidoreDescriptions.length() != 0) {
                            String[] listDescritionIsidore = sIsidoreDescriptions.split("##");
                            for (int j = 0; j < listDescritionIsidore.length; j++) {
                                if (listDescritionIsidore.length > 1) {

                                    if (!listDescritionIsidore[j].equals("")) {

                    %><p class="klios_p"><b>Description : </b><%=listDescritionIsidore[j]%></p>  
                    <%}
                    } else {

                    %><p class="klios_p"><b>Description : </b><%=listDescritionIsidore[j]%></p>  
                    <%

                                }
                            }
                        }

                        //PUBLISHER
                        if (arrayIsidorePublisher.size() != 0) {
                           // System.out.println("sono dentro publisher " + arrayIsidorePublisher.size());
                            for (int j = 0; j < arrayIsidorePublisher.size(); j++) {
                    %>
                    <p class="klios_p"><b>Publisher : </b><%=arrayIsidorePublisher.get(j).toString()%></p> 
                    <%  }


                        }





                        //IDENTIFIER
                        if (sIsidoreID.length() != 0) {

                            String[] listIdentifierIsidore = sIsidoreID.split("##");
                            for (int j = 0; j < listIdentifierIsidore.length; j++) {
                                String identifierResource = listIdentifierIsidore[j];

                                if (listIdentifierIsidore.length > 1) {


                                    if (!listIdentifierIsidore[j].equals("")) {

                                        if (identifierResource.length() > 4 && identifierResource.substring(0, 4).equals("http")) {
                                            //System.out.println("la j vale dall'if " + j);

                    %><p class="klios_p"><b>Identifier : </b><a href="<%=identifierResource%>" target="_blank"><u><%=identifierResource%> </u></a></p> 
                    <%} else {
                        //System.out.println("la j vale dall'else " + j);
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
                        if (arrayIsidoreSource.size() != 0) {
                            //System.out.println("sono dentro source " + arrayIsidoreSource.size());
                            for (int j = 0; j < arrayIsidoreSource.size(); j++) {

                                if (arrayIsidoreSource.get(j).toString().length() > 4 && arrayIsidoreSource.get(j).toString().substring(0, 4).equals("http")) {
                                    String linksource = arrayIsidoreSource.get(j).toString();
                    %>
                    <p class="klios_p"><b>Source : </b><a href="<%=linksource%>" target="_blank" value="<%=linksource%>"><u><%=linksource%></u></a></p> 
                    <%  } else {%>

                    <p class="klios_p"><b>Source : </b><%=arrayIsidoreSource.get(j).toString()%></p> 
                    <%  }
                            }
                        }
                        //SUBJECT
                        if (arrayIsidoreSubject.size() != 0) {
                            //System.out.println("sono dentro subject " + arrayIsidoreSubject.size());
                            for (int j = 0; j < arrayIsidoreSubject.size(); j++) {

                                if (arrayIsidoreSubject.get(j).toString().length() > 4 && arrayIsidoreSubject.get(j).toString().substring(0, 4).equals("http")) {
                                    String linksubject = arrayIsidoreSubject.get(j).toString();
                         %>
                             <p class="klios_p"><b>Subject : </b><a href="<%=linksubject%>" target="_blank" value="<%=linksubject%>"><u><%=linksubject%></u></a></p> 
                         <%  } else {

                        String sbj = arrayIsidoreSubject.get(j).toString().replace(",", ", ");

                    %>
                    <p class="klios_p"><b>Subject : </b><%=sbj%></p> 
                    <%  }


                        }
                        }

                        //LANGUAGE

                        if (arrayIsidoreLanguage.size() != 0) {
                            //System.out.println("sono dentro language " + arrayIsidoreLanguage.size());
                            for (int j = 0; j < arrayIsidoreLanguage.size(); j++) {
                    %>
                    <p class="klios_p"><b>Language : </b><%=arrayIsidoreLanguage.get(j).toString()%></p> 
                    <%  }


                        }
                        //DATE
                        if (arrayIsidoreDate.size() != 0) {
                            //System.out.println("sono dentro date " + arrayIsidoreDate.size());
                            for (int j = 0; j < arrayIsidoreDate.size(); j++) {
                    %>
                    <p class="klios_p"><b>Date : </b><%=arrayIsidoreDate.get(j).toString()%></p> 
                    <%  }


                        }

                        //CONTRIBUTOR

                        if (arrayIsidoreContributor.size() != 0) {
                            //System.out.println("sono dentro contributor " + arrayIsidoreContributor.size());
                            for (int j = 0; j < arrayIsidoreContributor.size(); j++) {
                    %>
                    <p class="klios_p"><b>Contributor : </b><%=arrayIsidoreContributor.get(j).toString()%></p> 
                    <%  }


                        }
                        //COVERAGE
                        if (!coverageInfo.equals("")) {
                            //System.out.println("sono dentro coverage " + coverageInfo);

                    %>
                    <p class="klios_p"><b>Coverage : </b><%=coverageInfo%></p>    
                    <%}
                        //RIGHT


                        if (arrayIsidoreRights.size() != 0) {
                            //System.out.println("sono dentro contributor " + arrayIsidoreRights.size());
                            for (int j = 0; j < arrayIsidoreRights.size(); j++) {
                    %>
                    <p class="klios_p"><b>Rights : </b><%=arrayIsidoreRights.get(j).toString()%></p> 
                    <%  }


                        }


                    %>

                </fieldset>
            </div>
              <br>
            <a id="LinkedData" class="Link" href="http://www.chain-project.eu/LodLiveGraph/?<%=idResourceIsidore%>" target="_blank">Linked Data </a>
           
            <br>        
            <br>    

            <div> 
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Dataset Information</legend>
                    <%
                        //TYPE DATA SET
                        if (arrayIsidoreType.size() != 0) {
                            //System.out.println("sono dentro type " + arrayIsidoreType.size());
                            for (int j = 0; j < arrayIsidoreType.size(); j++) {
                    %>
                    <p class="klios_p"><b>Type : </b><%=arrayIsidoreType.get(j).toString()%></p> 
                    <%  }


                        }
                        //FORMAT DATA SET

                        if (arrayIsidoreFormat.size() != 0) {
                            //System.out.println("sono dentro format " + arrayIsidoreFormat.size());
                            for (int j = 0; j < arrayIsidoreFormat.size(); j++) {
                    %>
                    <p class="klios_p"><b>Format : </b><%=arrayIsidoreFormat.get(j).toString()%></p> 
                    <%  }


                        }
                        //RELATION DATA SET

                        if (arrayIsidoreRelation.size() != 0) {
                            //System.out.println("sono dentro format " + arrayIsidoreRelation.size());
                            for (int j = 0; j < arrayIsidoreRelation.size(); j++) {
                    %>
                    <p class="klios_p"><b>Relation : </b><%=arrayIsidoreRelation.get(j).toString()%></p> 
                    <%  }


                        }
                    %>
                </fieldset>
            </div>
            <br>
            <div>
                <fieldset class="fieldsetInformations" >
                    <legend class="legendFieldset" >Repository Information</legend>
                    <p class="klios_p"><b>Name: </b> isidore   </p> 
                    <p class="klios_p"><b>URL : </b> <a href="http://www.rechercheisidore.fr/" target="_blank" title="http://www.rechercheisidore.fr/"><u>http://www.rechercheisidore.fr/</u> </a></p>
                    <p class="klios_p"><b>SPARQL endpoint : </b><a href="http://www.rechercheisidore.fr/sparql/" target="_blank"><u>http://www.rechercheisidore.fr/sparql/</u></a></p>
                    <p class="klios_p"><b>Country Code : </b>FR</p>
                    <p class="klios_p"><b>Domain : </b>Sciences Humaines et Sociales</p>


                </fieldset>
            </div>
            <br>
        </div>
        <br>
         <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfoIsidore" id="idMoreInfoIsidore" value="OK" />
                <input  hidden="true" name="numResourceIsidoreFromDetails" id="idnumResourceFromDetails" value="<%=numResourceIsidore%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 
    </body>

</html>

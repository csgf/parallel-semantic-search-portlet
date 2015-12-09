<%-- 
    Document   : viewDetailsResourceDBPedia
    Created on : Dec 7, 2015, 12:01:33 PM
    Author     : grid
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@page import="javax.portlet.*"%>
<%@page import="it.infn.ct.QueryDBPedia"%>

<portlet:defineObjects />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body link="BLACK" vlink="red">
        <%

            PortletPreferences preferences = renderRequest.getPreferences();

            String LodLiveEndPoint = preferences.getValue("LodLiveEndPoint", "");
            System.out.println("LodLiveEndPoint------->:" + LodLiveEndPoint);
            String idResource = renderRequest.getParameter("idResourceDBPedia");
            System.out.println("idResource------->:" + idResource);
            String numResource = renderRequest.getParameter("numResourceDBPedia");
            System.out.println("numResource------->:" + numResource);
            String searched_word = renderRequest.getParameter("search_word");
            String titles = QueryDBPedia.getTitle(idResource);
            String description = QueryDBPedia.getDescription(idResource);
            String type = QueryDBPedia.getRDFType(idResource);
            String label = QueryDBPedia.getRDFLabel(idResource);
            String comment = QueryDBPedia.getRDFComment(idResource);
            String wasDerived = QueryDBPedia.getWasDerivedFrom(idResource);
            String wikiPage = QueryDBPedia.getWikiPageExternalLink(idResource);
            String sameAs = QueryDBPedia.getSameAs(idResource);
            String subject = QueryDBPedia.getSubject(idResource);
            ArrayList allInfo = QueryDBPedia.getAll(idResource);

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


                <%
                    //Descrption ontology:abstract
                    String[] descs = description.split("##");
                    for (int j = 0; j < descs.length; j++) {
                        String desc = descs[j];
                        if (!desc.equals("---")) {
                %> <p class="klios_p"><b>Description : </b><%=desc%></p>
                <%
                        }

                    }

                    //http://www.w3.org/ns/prov#wasDerivedFrom
                    String[] wdf = wasDerived.split("##");
                    for (int j = 0; j < wdf.length; j++) {
                        String wdf_ = wdf[j];
                        if (!wdf_.equals("---")) {
                %> <p class="klios_p"><b>Was derived from : </b><a href="<%=wdf_%>" target="_blank"><u><%=wdf_%></u></a></p>
                <%
                        }
                    }

                    //WikiPageExternalLink
                    String[] wikiPages = wikiPage.split("##");
                    for (int j = 0; j < wikiPages.length; j++) {
                        String wikiPages_ = wikiPages[j];
                        if (!wikiPages_.equals("---")) {
                %> <p class="klios_p"><b>Wiki Page External Link : </b><a href="<%=wikiPages_%>" target="_blank"><u><%=wikiPages_%></u></a></p>
                <%
                        }
                    }

                    //SameAs
                    String[] sAs = sameAs.split("##");
                    for (int j = 0; j < sAs.length; j++) {
                        String sAs_ = sAs[j];
                        if (!sAs_.equals("---")) {
                %> <p class="klios_p"><b>Same As: </b><a href="<%=sAs_%>" target="_blank"><u><%=sAs_%></u></a></p>
                <%
                        }
                    }

                    //SameAs
                    String[] sub = subject.split("##");
                    for (int j = 0; j < sub.length; j++) {
                        String sub_ = sub[j];
                        if (!sub_.equals("---")) {
                %> <p class="klios_p"><b>Subject: </b><%=sub_%></p>
                <%
                        }
                    }


                %>

            </fieldset>
            
            <br>
            <a id="LinkedData" class="Link" href="<%=LodLiveEndPoint%>/?<%=idResource%>" target="_blank">Linked Data </a>
            <br>
            <br>
            <a id="RDFLink" class="Link" href="#" onClick="showFieldSetRDF(); return false;">RDF-Schema<img id="ImageAnimationDate" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetRDF" style="display: none;">
                <%

                    //RDFS:type
                    String[] types = type.split("##");
                    for (int j = 0; j < types.length; j++) {
                        String type_ = types[j];
                        if (!type_.equals("---")) {
                %> <p class="klios_p"><b>Type: </b><a href="<%=type_%>" target="_blank"><u><%=type_%></u></a></p>
                <%
                        }
                    }

                    //RDFS:label
                    String[] labels = label.split("##");
                    for (int j = 0; j < labels.length; j++) {
                        String label_ = labels[j];
                        if (!label_.equals("---")) {
                %> <p class="klios_p"><b>Label: </b><%=label_%></p>
                <%
                        }
                    }

                    //RDFS:commetnt
                    String[] comments = comment.split("##");
                    for (int j = 0; j < comments.length; j++) {
                        String comment_ = comments[j];
                        if (!comment_.equals("---")) {
                %> <p class="klios_p"><b>Label: </b><%=comment_%></p>
                <%
                        }
                    }

                %>
            </fieldset>   

            <br>

            <a id="AllInformationLink" class="Link" href="#" onClick="showFieldAllInformation(); return false;">All Informations<img id="ImageAnimationDate" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetAllInformation" style="display: none;">

                <table class="tableAllInformation">
                    <tr>
                        <th class="tableAllInformation">Predicate</th>
                        <th class="tableAllInformation">Object</th>
                    </tr>

                    <%


                        for (int i = 0; i < allInfo.size(); i++) {
                    %><tr ><%
                        String[] elem = (String[]) allInfo.get(i);
                        String pred = elem[0];

                        String obj = elem[1];

                        %><td class="tableAllInformation"><%=pred%></td>
                        <td class="tableAllInformation"><%=obj%></td>
                    </tr><%
                        }

                    %>
                </table>

            </fieldset>

            <br>
            <a id="RepositoryLink" class="Link" href="#" onClick="showFieldSetRepository(); return false;">Repository Information <img id="ImageAnimationRepository" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetRepository" style="display: none;">
                <p class="klios_p"><b>Name: </b> DBPEDIA </p> 
                <p class="klios_p"><b>URL : </b> <a href="http://dbpedia.org/" target="_blank" title="http://dbpedia.org/"><u>http://dbpedia.org/</u> </a></p>
                <p class="klios_p"><b>Domain : </b>Multidisciplinary</p>
                <p class="klios_p"><b>Project : </b><a href="http://www.neofonie.de/" target="_blank">neofonie </a>,<a href="http://lod2.eu/Welcome.html" target="_blank">European Commission via the LOD2 project</a>,<a href="http://www.projecthalo.com/" target="_blank">Vulcan Inc. as part of Project Halo</a></p>
                <p class="klios_p"><b>Organization : </b>WikiPedia</p>
            </fieldset>    



            <br>    
            <br>



        </div>


        <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                    <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                    <input type="submit" value="<< Back" />
                    <input  hidden="true" name="moreInfoDBPedia" id="idMoreInfo" value="OK" />
                    <input  hidden="true" name="numResourceDBPediaFromDetails" id="idnumResourceFromDetails" value="<%=numResource%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 




    </body>

    <script>

        var controlRDF=true;
        var controlAll=true;
        var controlRepository=true;



        function showFieldSetRDF(){
            $("#IdFieldSetRDF").animate({"height": "toggle"});
                
                
                
                
            if( controlRDF==true ) {
                $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                controlRDF=false;
            }
            else {
                $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                controlRDF=true;
            }
                
        }
        
        function showFieldAllInformation(){
            $("#IdFieldSetAllInformation").animate({"height": "toggle"});
                
                
                
                
            if( controlAll==true ) {
                $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                controlAll=false;
            }
            else {
                $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                controlAll=true;
            }
        }
        
        function showFieldSetRepository(){
            $("#IdFieldSetRepository").animate({"height": "toggle"});
                
                
                
                
            if( controlRepository==true ) {
                $("#ImageAnimationRepository").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                controlRepository=false;
            }
            else {
                $("#ImageAnimationRepository").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                controlRepository=true;
            }
                
        }


    </script>

</html>
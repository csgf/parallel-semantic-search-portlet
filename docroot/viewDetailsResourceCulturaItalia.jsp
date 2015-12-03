<%-- 
    Document   : viewDetailsResourceCulturaItalia
    Created on : Sep 30, 2013, 3:10:28 PM
    Author     : grid
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.portlet.*"%>
<%@page import="it.infn.ct.QueryCulturaItalia"%>
<%@page import="java.util.ArrayList"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />
<!DOCTYPE html>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <%
            String LodLiveEndPoint = renderRequest.getParameter("LodLiveEndPoint");
            String idResource = renderRequest.getParameter("idResourceCulturaItalia");
            String TimeOut = renderRequest.getParameter("TimeOut");
            //String idResource = "http://dati.culturaitalia.it/resource/oai-polomuseale-firenze-it-sculture-825";
            // String idResource = "http://dati.culturaitalia.it/resource/oai-fc1-to-cnr-it-openbess-to082-00007";
            //String idResource="http://dati.culturaitalia.it/resource/oai-polomuseale-firenze-it-sculture-1361";
            //String idResource="http://dati.culturaitalia.it/resource/oai-sirpac-cultura-marche-it-13761";
            //String idResource = "http://dati.culturaitalia.it/resource/ansc00002120";
            //String idResource="http://dati.culturaitalia.it/resource/oai-fc1-to-cnr-it-asfiat-afiat-00391";
            //String idResource="http://dati.culturaitalia.it/resource/oai-fc1-to-cnr-it-asfiat-afiat-00414";
            //String idResource = "http://dati.culturaitalia.it/resource/oai-anagrafe-iccu-sbn-it-it-an0001";
            //String idResource="http://dati.culturaitalia.it/resource/oai-anagrafe-iccu-sbn-it-it-an0007";
            //String idResource = "http://dati.culturaitalia.it/resource/oai-www-michael-culture-it-pub-it-dcollection-it-dc-50ad3d0b";
            //String idResource="http://dati.culturaitalia.it/resource/oai-sirpac-cultura-marche-it-49452";
            //  String idResource="http://dati.culturaitalia.it/resource/oai-www-internetculturale-it-metaoaicat-oai-www-internetculturale-sbn-it-teca-20-$5$Woooojjj$JzamABDmriEYpOZgvgwOhyRkwRO8v/JhnuRXUEFaKf9";


            String numResourceCulturaItalia = renderRequest.getParameter("numResourceCulturaItalia");
            String searched_word = renderRequest.getParameter("search_word");
            //System.out.println("idResourceCulturaItalia---->" + idResource);
            //System.out.println("numResourceCulturaItalia------->:" + numResourceCulturaItalia);

            String titles = QueryCulturaItalia.getTitle(idResource);
            String authors = QueryCulturaItalia.getAuthors(idResource);
            String authorsFinal = "no";
            if (!authors.equals("---")) {
                String authors1 = authors.substring(2);
                authorsFinal = authors1.replace("##", "; ");
            }
            String description = QueryCulturaItalia.getDescription(idResource);
            String description1 = description.substring(2);
            String[] descriptionList = description1.split("##");

            String type = QueryCulturaItalia.getType(idResource);
            String urlResource = QueryCulturaItalia.getIdentifiersUrl(idResource);
            //String identifier1 = identifiers.substring(2);
            //String[] identifierList = identifier1.split("##");
            // String urlResource = QueryCulturaItalia.urlResource;
            ArrayList listhasType = QueryCulturaItalia.has_type(idResource);
            ArrayList listRef = QueryCulturaItalia.is_referred_to_by(idResource);
            ArrayList listRep = QueryCulturaItalia.has_representation(idResource);
            ArrayList listSub = QueryCulturaItalia.is_subject_to(idResource);
            ArrayList listDim = QueryCulturaItalia.has_dimension(idResource);
            ArrayList listLoc = QueryCulturaItalia.has_former_or_current_location(idResource);
            ArrayList listProd = QueryCulturaItalia.was_produced_by(idResource);
            ArrayList listDepicts = QueryCulturaItalia.depicts(idResource);
            ArrayList listFeatures = QueryCulturaItalia.show_features_of(idResource);
            ArrayList listPart = QueryCulturaItalia.forms_part_of(idResource);
            ArrayList listAbout = QueryCulturaItalia.is_about(idResource);
            ArrayList listNote = QueryCulturaItalia.has_note(idResource);
            ArrayList listTrasform = QueryCulturaItalia.was_trasformed_by(idResource);
            ArrayList listAt = QueryCulturaItalia.was_present_at(idResource);
            ArrayList listLan = QueryCulturaItalia.has_language(idResource);
            ArrayList listRes = QueryCulturaItalia.has_current_or_former_residence(idResource);
            ArrayList listPos = QueryCulturaItalia.possesses(idResource);
            ArrayList listComposed = QueryCulturaItalia.is_composed_of(idResource);
            ArrayList listPlace = QueryCulturaItalia.took_place_at(idResource);
            ArrayList listRefers = QueryCulturaItalia.refers_to(idResource);
            ArrayList listComposed2 = QueryCulturaItalia.is_composed_of2(idResource);
            ArrayList listMod = QueryCulturaItalia.was_modified_by(idResource);
            ArrayList listW = QueryCulturaItalia.witnessed(idResource);
            ArrayList listPart2 = QueryCulturaItalia.forms_part_of2(idResource);
            ArrayList listPerformed = QueryCulturaItalia.performed(idResource);
            ArrayList listMember = QueryCulturaItalia.is_current_or_former_member(idResource);
            ArrayList listBased = QueryCulturaItalia.was_based_on(idResource);
            ArrayList listIn = QueryCulturaItalia.includes(idResource);
        %>
        <div id="conteinerRecord" style="">
            <%
                String[] stitles = titles.split("##");
                for (int i = 1; i < stitles.length; i++) {
                    String title = stitles[i];
                    if (!urlResource.equals("url")) {
            %> <a id="Title" href="<%=urlResource%>" target="_blank" style="color:black"><h1><u><%=i%>) <%=title%></u></h1></a><%} else {
            %> <h1 id="Title"> <%=title%></h1>
            <%}
                }

            %>
            <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >General Information</legend>
                <% if (!authorsFinal.equals("no")) {%>
                <p class="klios_p"><b>Authors: </b><%=authorsFinal%></p>
                <%}
                    for (int j = 0; j < descriptionList.length; j++) {
                        String desc = descriptionList[j];
                        if (!desc.equals("-")) {
                %> <p class="klios_p"><b>Description : </b><%=desc%></p>
                <%
                        }
                    }
                    if (!type.equals("---")) {
                %><p class="klios_p"><b>Type : </b><%=type%></p>
                <%}


                    // for (int j = 0; j < identifierList.length; j++) {
                    // String id = identifierList[j];
                    //if (!id.equals("-")) {
                    if (urlResource.substring(0, 4).equals("http")) {
                %><a href="<%=urlResource%>" target="_blank" style="color:black"><p class="klios_p"><b>URL : </b><u><%=urlResource%></u></p></a><%}


                    //  }

                    //  }
                    for (int j = 0; j < listhasType.size(); j++) {
                        String hasType = listhasType.get(j).toString();
                        if (!hasType.equals("---")) {
                %>
                <p class="klios_p"><b>Has Type : </b><%=hasType%></p>
                <% }
                    }

                    for (int j = 0; j < listRef.size(); j++) {
                        String ref = listRef.get(j).toString();
                        if (!ref.equals("---")) {
                            if (ref.substring(0, 4).equals("http")) {
                %><a href="<%=ref%>" target="_blank" style="color:black"><p class="klios_p"><b>Referred to by : </b><u><%=ref%></u></p></a><%} else {

                %> <p class="klios_p"><b>Referred to by : </b><%=ref%></p>
                <%                       }

                        }

                    }

                    for (int j = 0; j < listRep.size(); j++) {
                        String rep = listRep.get(j).toString();
                        if (!rep.equals("---")) {
                            if (rep.substring(0, 4).equals("http")) {
                %> <p class="klios_p"><a href="<%=rep%>" target="_blank" style="cursor: -webkit-zoom-in; cursor: -moz-zoom-in;" > <img src="<%=rep%>" style="height: 100px; width:100px;"></a></p>
                <%} else {%> <p class="klios_p"><b>Representation : </b><%=rep%></p>
                <% }

                        }

                    }
                    for (int j = 0; j < listDim.size(); j++) {
                        String dim = listDim.get(j).toString();
                        if (!dim.equals("---")) {
                %>
                <p class="klios_p"><b>Dimension : </b><%=dim%></p>
                <% }
                    }

                    for (int j = 0; j < listDepicts.size(); j++) {
                        String dep = listDepicts.get(j).toString();
                        if (!dep.equals("---")) {
                %>
                <p class="klios_p"><b>Depicts : </b><%=dep%></p>
                <% }
                    }

                    for (int j = 0; j < listFeatures.size(); j++) {
                        String f = listFeatures.get(j).toString();
                        if (!f.equals("---")) {

                %>
                <a href="<%=f%>" target="_blank" style="color:black"><p class="klios_p"><b>Show features of : </b><u><%=f%></u></p></a>
                <%
                        }
                    }

                    for (int j = 0; j < listPart.size(); j++) {
                        String p = listPart.get(j).toString();
                        if (!p.equals("---")) {

                %>
                <a href="<%=p%>" target="_blank" style="color:black"><p class="klios_p"><b>Forms part of : </b><u><%=p%></u></p></a>
                <%
                        }
                    }



                    for (int j = 0; j < listSub.size(); j++) {
                        String sub = listSub.get(j).toString();
                        if (!sub.equals("---")) {
                            String name = sub.split("##")[0];
                            String url = sub.split("##")[1];

                %>
                <a href="<%=url%>" target="_blank" style="color:black"><p class="klios_p"><b>Subject to : </b><u><%=name%></u></p></a>
                <% }
                    }

                    for (int j = 0; j < listLoc.size(); j++) {
                        String loc = listLoc.get(j).toString();
                        if (!loc.equals("---")) {
                %>
                <p class="klios_p"><b>Current or ex Location : </b><%=loc%></p>
                <% }
                    }

                    for (int j = 0; j < listProd.size(); j++) {
                        String prod = listProd.get(j).toString();
                        if (!prod.equals("---")) {
                %>
                <p class="klios_p"><b>Produced by : </b><%=prod%></p>
                <% }
                    }

                    for (int j = 0; j < listAbout.size(); j++) {
                        String about = listAbout.get(j).toString();
                        if (!about.equals("---")) {
                %>
                <p class="klios_p"><b>About : </b><%=about%></p>
                <% }
                    }

                    for (int j = 0; j < listNote.size(); j++) {
                        String note = listNote.get(j).toString();
                        if (!note.equals("---")) {
                %>
                <p class="klios_p"><b>Note : </b><%=note%></p>
                <% }
                    }


                    for (int j = 0; j < listTrasform.size(); j++) {
                        String tr = listTrasform.get(j).toString();
                        if (!tr.equals("---")) {
                %>
                <p class="klios_p"><b>Trasformed by : </b><%=tr%></p>
                <% }
                    }


                    for (int j = 0; j < listAt.size(); j++) {
                        String at = listAt.get(j).toString();
                        if (!at.equals("---")) {
                %>
                <p class="klios_p"><b>Was present at : </b><%=at%></p>
                <% }
                    }

                    for (int j = 0; j < listLan.size(); j++) {
                        String lan = listLan.get(j).toString();
                        if (!lan.equals("---")) {
                %>
                <p class="klios_p"><b>Language : </b><%=lan%></p>
                <% }
                    }

                    for (int j = 0; j < listRes.size(); j++) {
                        String res = listRes.get(j).toString();
                        if (!res.equals("---")) {
                %>
                <p class="klios_p"><b>Current or ex Residence : </b><%=res%></p>
                <% }
                    }

                    for (int j = 0; j < listPos.size(); j++) {
                        String pos = listPos.get(j).toString();
                        if (!pos.equals("---")) {
                %>
                <p class="klios_p"><b>Possesses : </b><%=pos%></p>
                <% }
                    }

                    for (int j = 0; j < listComposed.size(); j++) {
                        String c = listComposed.get(j).toString();
                        if (!c.equals("---")) {
                            if (c.substring(0, 4).equals("http")) {
                %><a href="<%=c%>" target="_blank" style="color:black"><p class="klios_p"><b>Composed of : </b><u><%=c%></u></p></a><%} else {

                %> <p class="klios_p"><b>Composed of : </b><%=c%></p>
                <%                       }

                        }
                    }
                
                for (int j = 0; j < listPlace.size(); j++) {
                        String pos = listPlace.get(j).toString();
                        if (!pos.equals("---")) {
                %>
                <p class="klios_p"><b>Took place at : </b><%=pos%></p>
                <% }
                    }
                
                 for (int j = 0; j < listRefers.size(); j++) {
                        String pos = listRefers.get(j).toString();
                        if (!pos.equals("---")) {
                %>
                <p class="klios_p"><b>Refers to : </b><%=pos%></p>
                <% }
                    }
                
                 for (int j = 0; j < listComposed2.size(); j++) {
                        String c = listComposed2.get(j).toString();
                        if (!c.equals("---")) {
                            if (c.substring(0, 4).equals("http")) {
                %><a href="<%=c%>" target="_blank" style="color:black"><p class="klios_p"><b>Composed of : </b><u><%=c%></u></p></a><%} else {

                %> <p class="klios_p"><b>Composed of : </b><%=c%></p>
                <%                       }

                        }
                    }
                
                for (int j = 0; j < listMod.size(); j++) {
                        String mod = listMod.get(j).toString();
                        if (!mod.equals("---")) {
                %>
                <p class="klios_p"><b>Modified by : </b><%=mod%></p>
                <% }
                    }
                
                 for (int j = 0; j < listW.size(); j++) {
                        String w = listW.get(j).toString();
                        if (!w.equals("---")) {
                %>
                <p class="klios_p"><b>Witnessed : </b><%=w%></p>
                <% }
                    }
                
                for (int j = 0; j < listPart2.size(); j++) {
                        String p = listPart2.get(j).toString();
                        if (!p.equals("---")) {
                %>
                <p class="klios_p"><b>Forms part of : </b><%=p%></p>
                <% }
                    }
               
                for (int j = 0; j < listPerformed.size(); j++) {
                        String p = listPerformed.get(j).toString();
                        if (!p.equals("---")) {
                %>
                <p class="klios_p"><b>Performed : </b><%=p%></p>
                <% }
                    }
                
                for (int j = 0; j < listMember.size(); j++) {
                        String p = listMember.get(j).toString();
                        if (!p.equals("---")) {
                %>
                <p class="klios_p"><b>Current or ex Member of : </b><%=p%></p>
                <% }
                    }
                
                
                for (int j = 0; j < listBased.size(); j++) {
                        String p = listBased.get(j).toString();
                        if (!p.equals("---")) {
                %>
                <p class="klios_p"><b>Was based on : </b><%=p%></p>
                <% }
                    }
                 for (int j = 0; j < listIn.size(); j++) {
                        String p = listIn.get(j).toString();
                        if (!p.equals("---")) {
                %>
                <p class="klios_p"><b>Includes : </b><%=p%></p>
                <% }
                    }
               







                %>


            </fieldset>
                
             <br>
            <a id="LinkedData" class="Link" href="<%=LodLiveEndPoint%>/?<%=idResource%>" target="_blank">Linked Data </a>
           
            <br>     
                    <br>     
            <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >Repository Information</legend>
                <p class="klios_p"><b>Name: </b>  Cultura Italia </p> 
                <p class="klios_p"><b>URL : </b> <a href="http://www.culturaitalia.it" target="_blank" title="http://www.culturaitalia.it"><u> http://www.culturaitalia.it </u> </a></p>
                <p class="klios_p"><b>Country Code: </b> IT </p> 
                <p class="klios_p"><b>Domain : </b>Italian culture</p>
                <p class="klios_p"><b>Organization : </b> Ministry for Cultural Heritage and Activities and Tourism (MiBACT)</p>
                
            </fieldset>
                
                
            <br><br>    

        </div>   
        <br>        

        <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfoCulturaItalia" id="idMoreInfoCulturaItalia" value="OK" />
                <input  hidden="true" name="numResourceCulturaItaliaFromDetails" id="idnumResourceFromDetails" value="<%=numResourceCulturaItalia%>" />
                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />
            </form>
        </div> 

    </body>
</html>

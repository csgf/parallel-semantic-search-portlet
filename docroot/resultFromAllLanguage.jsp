<%
    /**
     * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
     *
     * This library is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as published by
     * the Free Software Foundation; either version 2.1 of the License, or (at
     * your option) any later version.
     *
     * This library is distributed in the hope that it will be useful, but
     * WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
     * General Public License for more details.
     */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Math"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%>
<%@page import="it.infn.ct.SemanticQueryMoreInfo"%>
<%@page import="it.infn.ct.SemanticQuery"%>
<%@page import="it.infn.ct.QueryPubMed"%>
<%@page import="it.infn.ct.QueryOpenAgris"%>
<%@page import="it.infn.ct.QueryCulturaItalia"%>
<%@page import="it.infn.ct.QueryEuropeana"%>
<%@page import="it.infn.ct.QueryIsidore"%>
<%@page import="it.infn.ct.QueryEngage"%>
<%@page import="javax.portlet.*"%>
<%@page import="it.infn.ct.Altmetric"%>

<%@page contentType="text/html; charset=utf-8" %>

<portlet:defineObjects />

<head>
    <style>
        #nav, #nav ul {
            padding: 0;
            margin: 0;
            list-style: none;

        }

        #nav a {
            display: block;
            width: 10em;
        }

        #nav li {
            float: left;
            width: 10em;
            cursor: pointer;
        }

        #nav li ul {
            position: absolute;
            width: 10em;
            left: -999em;
        }

        #nav li:hover ul {
            left: auto;

        }
        #nav li a:hover{
            left:inherit;
            background-color:#EEE;
            display:block;

        }
        #examplebutton{
            cursor: pointer;
        }

    </style>
</head>
<body>

    <%            PortletPreferences preferences = renderRequest.getPreferences();

        String OpenAgris = preferences.getValue("OpenAgris", "");
        String Europeana = preferences.getValue("Europeana", "");
        String CulturaItalia = preferences.getValue("CulturaItalia", "");
        String Isidore = preferences.getValue("Isidore", "");
        String Pubmed = preferences.getValue("Pubmed", "");
        String Engage = preferences.getValue("Engage", "");
        String DBPedia = preferences.getValue("DBPedia", "");
        String LodLiveEndPoint = preferences.getValue("LodLiveEndPoint", "");
        String TimeOut = preferences.getValue("TimeOut", "");

        String numberRecordsForPage = preferences.getValue("NumberRecordsForPage", "");
        System.out.println("VALORE_Openagris_: " + OpenAgris);
        System.out.println("VALORE_Europeana_: " + Europeana);
        System.out.println("VALORE_CulturaItalia_: " + CulturaItalia);
        System.out.println("VALORE_Isidore_: " + Isidore);
        System.out.println("VALORE_Pubmed_: " + Pubmed);
        System.out.println("VALORE_Engage_: " + Engage);
        System.out.println("VALORE_DBPedia_: " + DBPedia);
        System.out.println("VALORE_LodLiveEndPoint_: " + LodLiveEndPoint);
        System.out.println("NumberRecordsForPage " + numberRecordsForPage);

        String searched_word = renderRequest.getParameter("searched_word");
        // System.out.println("SEARCH-WORD**"+searched_word);

        String selected_page = renderRequest.getParameter("selected_page");
        String moreResourceCHAIN = renderRequest.getParameter("moreResourceCHAIN");
        //System.out.println("**moreResourceCHAIN**"+moreResourceCHAIN);
        String moreInfo = renderRequest.getParameter("moreInfo");
        String numResourceFromDetails = renderRequest.getParameter("numResourceFromDetails");
        //System.out.println("**numResourceFRomDetailsCHAIN**" + numResourceFromDetails);



        //**********CODICE OPENAGRIS************
        String selected_pageOpenAgris = renderRequest.getParameter("selected_pageOpenAgris");
        String moreInfoOpenAgris = renderRequest.getParameter("moreInfoOpenAgris");
        //System.out.println("moreInfoOpenAgris in JSP: "+moreInfoOpenAgris);
        String moreResourceOpenAgris = renderRequest.getParameter("moreResourceOpenAgris");
        String numResourceOpenAgrisFromDetails = renderRequest.getParameter("numResourceOpenAgrisFromDetails");
        //System.out.println("numResourceOpenAgrisFromDetails in JSP: " + numResourceOpenAgrisFromDetails);
        //********** FINE CODICE OPENAGRIS************

        //**********CODICE CULTURA ITALIA************
        String selected_pageCulturaItalia = renderRequest.getParameter("selected_pageCulturaItalia");
        String moreResourceCulturaItalia = renderRequest.getParameter("moreResourceCulturaItalia");
        String moreInfoCulturaItalia = renderRequest.getParameter("moreInfoCulturaItalia");
        //System.out.println("moreInfoCulturaItalia in JSP: "+moreInfoCulturaItalia);
        String numResourceCulturaItaliaFromDetails = renderRequest.getParameter("numResourceCulturaItaliaFromDetails");
        //System.out.println("numResourceCulturaItaliaFromDetails in JSP: " + numResourceCulturaItaliaFromDetails);
        //********** FINE CULTURA ITALIA************

        //EUROPEANA        
        String selected_pageEuropeana = renderRequest.getParameter("selected_pageEuropeana");
        String moreResourceEuropeana = renderRequest.getParameter("moreResourceEuropeana");
        //System.out.println("MORE RESOURCE EUROPEANA in JSP--->"+moreResourceEuropeana);
        String moreInfoEuropeana = renderRequest.getParameter("moreInfoEuropeana");
        //System.out.println("moreInfoEuropeana in JSP: "+moreInfoEuropeana);
        String numResourceEuropeanaFromDetails = renderRequest.getParameter("numResourceEuropeanaFromDetails");
        //System.out.println("numResourceEuropeanaFromDetails in JSP: " + numResourceEuropeanaFromDetails);

        //ISIDORE        
        String selected_pageIsidore = renderRequest.getParameter("selected_pageIsidore");
        String moreResourceIsidore = renderRequest.getParameter("moreResourceIsidore");
        String moreInfoIsidore = renderRequest.getParameter("moreInfoIsidore");
        //System.out.println("moreInfoIsidore in JSP: "+moreInfoEuropeana);
        String numResourceIsidoreFromDetails = renderRequest.getParameter("numResourceIsidoreFromDetails");
       // System.out.println("numResourceIsidoreFromDetails in JSP: " + numResourceIsidoreFromDetails);

        //PUBMED       
        String selected_pagePubmed = renderRequest.getParameter("selected_pagePubmed");
        String moreResourcePubmed = renderRequest.getParameter("moreResourcePubmed");
        String moreInfoPubmed = renderRequest.getParameter("moreInfoPubmed");
        String numResourcePubmedFromDetails = renderRequest.getParameter("numResourcePubmedFromDetails");

        //ENGAGE        
        String selected_pageEngage = renderRequest.getParameter("selected_pageEngage");
        String moreResourceEngage = renderRequest.getParameter("moreResourceEngage");
        String moreInfoEngage = renderRequest.getParameter("moreInfoEngage");
        String numResourceEngageFromDetails = renderRequest.getParameter("numResourceEngageFromDetails");

        String selected_pageDBPedia = renderRequest.getParameter("selected_pageDBPedia");
        String moreResourceDBPedia = renderRequest.getParameter("moreResourceDBPedia");
        String moreInfoDBPedia = renderRequest.getParameter("moreInfoDBPedia");
       // System.out.println("MOREINFO-DBPEDIA "+moreInfoDBPedia);
        String numResourceDBPediaFromDetails = renderRequest.getParameter("numResourceDBPediaFromDetails");
       // System.out.println("numResourceDBPediaFromDetails "+numResourceDBPediaFromDetails);


    %>

    <br>
    <div id="divContainer">
        <center>

            <h4>Welcome to the parallel Semantic Search Engine (SSE)<br/><br/></h4>

            <div style="margin-left:15px" 
                 align="justify"
                 id="header" 
                 style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; 
                 font-size: 14px; display:none;">
                <p>
                    Using the SSE you can search in parallel and in more than 100 languages across several Linked Data repositories:                    
                    <!-- <ul>
                     <li>the e-Infrastructure Knowledge Base (KB) containing more than 30 million resources belonging to thousands of 
                     semantically enriched <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/oadr-map/">Open Access Document Repositories</a> 
                     and <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/data-repositories-map/">Data Repositories</a>.
                     Search results are ranked according to the <a href="http://repositories.webometrics.info/">Ranking Web of Repositories</a>.
                     </li>
                     <li>
                       <a href="http://www.europeana.eu/portal/">Europeana</a>, 
                       <a href="http://dati.culturaitalia.it/?locale=it">Cultura Italia</a>,
                       <a href="http://www.rechercheisidore.fr/">Isidore</a>, 
                       <a href="http://agris.fao.org/openagris/index.do">OpenAgris</a> and 
                       <a href="http://pubmed.bio2rdf.org/">PubMed</a> (others can simply added using the configuration options).
                     </li>
                 </ul> -->
                <ol>
                    <li>The e-Infrastructure Knowledge Base (KB) containing more than 30 million resources belonging to thousands of 
                        semantically enriched <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/oadr-map/" target="_blank">Open Access Document Repositories</a> 
                        and <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/data-repositories-map/" target="_blank">Data Repositories</a>.
                        Search results are ranked according to the <a href="http://repositories.webometrics.info/" target="_blank">Ranking Web of Repositories</a>.
                    </li>
                    <li>
                        <a href="http://www.europeana.eu/portal/" target="_blank">Europeana</a>, 
                        <a href="http://dati.culturaitalia.it/?locale=it" target="_blank">Cultura Italia</a>,
                        <a href="http://www.rechercheisidore.fr/" target="_blank">Isidore</a>,
                        <a href="http://agris.fao.org/openagris/index.do" target="_blank">OpenAgris</a>,
                        <a href="http://www.ncbi.nlm.nih.gov/pubmed" target="_blank">PubMed</a> and
                        <a href=" http://wiki.dbpedia.org/" target="_blank">DBPedia</a> (Note: Cultura Italia only allows 1-keyword searches).
                    </li>
                </ol>


                </p>
            </div>

            <p>Click here to get some examples
                <input type="image" 
                       align="absmiddle"
                       style="width:20px !important; heigth:20px !important"
                       src="<%=renderRequest.getContextPath()%>/images/help.png"
                       onclick="examplebutton();"/>
            </p>

            <div id="divSearchBar"> 
                <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">         
                        <table>
                            <tr>
                                <td align="center" style=" padding: 5px;" >
                                    <img href="http://klios.ct.infn.it" src="<%=renderRequest.getContextPath()%>/images/logo_klios.png" />
                            </td>

                            <!--td>
                                <input id="examplebutton" class="rounded" type="button" value="Examples" style="text-align: right;font-size: 12px;"  />                                  
                            </td-->
                            <!--<td align="right" style="padding-right: 10px; width: 80px;">
                                <ul id="nav" class="rounded">
                                    <li style="width: 80px;"><input id="examplebutton" class="rounded" type="button" value="Examples" style="text-align: right;font-size: 12px;" />
                                        <ul id="provaId" class="rounded" style="border: solid; border-color: grey;  background:white; text-align: left; padding-left: 5px;margin-left:10px; ">
                                            <font  align="left" style="font-color:grey;font-size: 12px;"> SEARCH EXAMPLES</font>
                                            <hr align="left" style="padding-left:0px;">
                                            <li  style="padding-top:10px;" ><input id="idExampleAuthor" type="text" hidden="true" name="example" value="author:Thomas_Pete"><a id="Author" align="left" value="author:Thomas_Pete" onclick="getExampleValue(this.id);">author:Thomas_Pete</a></li>
                                            <li  style="padding-top:10px; "><input  id="idExampleSubject" type="text" hidden="true" name="example" value="subject:policy"><a id="Subject" align="left" value="subject:policy" onclick="getExampleValue(this.id);">subject:policy</a></li>
                                            <li  style="padding-top:10px; "><input  id="idExampleType" type="text" hidden="true" name="example" value="type:thesis"><a id="Type" align="left" value="type:thesis" onclick="getExampleValue(this.id);">type:thesis</a></li>
                                            <li  style="padding-top:10px; "><input id="idExampleFormat" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="Format" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li>
                                            <li  style="padding-top:10px; padding-bottom: 10px;"><input id="idExamplePublisher" type="text" hidden="true" name="example" value="publisher:elsevier"><a id="Publisher" align="left" value="publisher:elsevier" onclick="getExampleValue(this.id);">publisher:elsevier</a></li> 


                                        </ul>
                                    </li>
                                </ul>
                            </td>-->

                            <td align="center" style=" padding: 10px;">
                                <input class="rounded" id="id_search_word"  name="search_word" type="text" style="width:600px;height: 20px;font-size: 12px" value="<%=searched_word%>" />
                            </td>
                            <td align="center" style=" padding: 10px;">
                                <!-- <input hidden="true" id="id_graph"  name="graph" type="text" value="http://RepositoryOntology_v2"/>-->

                                <input id="buttonSearchImg "  type="button" onclick="submitSearch(); " style="text-align: right;font-size: 12px;" value="Search"/>
                            </td>
                        </tr>
                        <!--tr>
                            <td colspan="4" align="center" style=" padding-top:0px; padding-bottom: 14px">Enter a keyword </td>
                        </tr-->

                    </table>

                    <div id="dialogExamples" title="Examples" hidden="true" >


                        <table id="tableExample">
                            <tr>
                                <th style="width: 40px;">
                                    ENGLISH
                                </th>
                                <th>
                                    ARABIC
                                </th>
                                <th>
                                    CHINESE
                                </th>
                                <th>
                                    RUSSIAN
                                </th>
                            </tr>
                            <tr>
                                <td id="firstColumn">
                                    <input id="idExampleAuthor" type="text" hidden="true" name="example" value="author:Smith G."><a id="Author"  value="author:Smith G." onclick="getExampleValue(this.id);" >author:Smith G.</a>
                                </td>
                                <td>
                                    <input  id="idExampleAuthorA" type="text" hidden="true" name="example" value="author:الحامد"><a id="AuthorA"  value="author:الحامد" onclick="getExampleValue(this.id);">author:الحامد</a>
                                </td>
                                <td>
                                    <input  id="idExampleAuthorC" type="text" hidden="true" name="example" value="author:邓祥征"><a id="AuthorC"  value="author:邓祥征" onclick="getExampleValue(this.id);">author:邓祥征</a>

                                </td>
                                <td>
                                    <input  id="idExampleAuthorR" type="text" hidden="true" name="example" value="author:ИвановичСкупский"><a id="AuthorR"  value="author:ИвановичСкупский" onclick="getExampleValue(this.id);">author:ИвановичСкупский</a>
                                </td>


                            </tr>
                            <tr>
                                <td id="firstColumn">
                                    <input id="idExampleSubject" type="text" hidden="true" name="example" value="subject:policy"><a id="Subject"  value="subject:policy" onclick="getExampleValue(this.id);" >subject:policy</a>
                                </td>
                                <td>
                                    <input  id="idExampleSubjectA" type="text" hidden="true" name="example" value="subject:الإدارة التربوية"><a id="SubjectA"  value="subject:الإدارة التربوية" onclick="getExampleValue(this.id);">subject:الإدارة التربوية</a> <!--educativo-->
                                </td>


                                <td>
                                    <input  id="idExampleSubjectC" type="text" hidden="true" name="example" value="subject:沙堆鄉"><a id="SubjectC"  value="subject:沙堆鄉" onclick="getExampleValue(this.id);">subject:台灣文學</a> <!--Letteratura di Taiwan-->
                                </td>
                                <td>
                                    <input  id="idExampleSubjectR" type="text" hidden="true" name="example" value="subject:Христианство"><a id="SubjectR"  value="subject:Христианство" onclick="getExampleValue(this.id);">subject:Христианство</a> <!--cristianesimo-->
                                </td>
                            </tr>
                            <tr>
                                <td id="firstColumn">
                                    <input id="idExampleType" type="text" hidden="true" name="example" value="type:thesis"><a id="Type"  value="type:thesis" onclick="getExampleValue(this.id);" >type:thesis</a>
                                </td>
                                <td>
                                    <input  id="idExampleTypeA" type="text" hidden="true" name="example" value="type:مصادر ومراجع"><a id="TypeA"  value="type:مصادر ومراجع" onclick="getExampleValue(this.id);">type:مصادر ومراجع</a> <!--Fonti e riferimenti-->
                                </td>


                                <td>
                                    <input  id="idExampleTypeC" type="text" hidden="true" name="example" value="type:期刊论文"><a id="TypeC"  value="type:期刊论文" onclick="getExampleValue(this.id);">type:期刊论文</a> <!--documenti-->
                                </td>
                                <td>
                                    <input  id="idExampleTypeR" type="text" hidden="true" name="example" value="type:Монография"><a id="TypeR"  value="type:Монография" onclick="getExampleValue(this.id);">type:Монография</a> <!--Мonografia-->
                                </td>
                            </tr>
                            <tr>
                                <td id="firstColumn">
                                    <input id="idExampleFormat" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="Format" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a>
                                </td>
                                <td>
                                    <input id="idExamplePublisherA" type="text" hidden="true" name="example" value="publisher:مجلة جامعة الملك سعود"><a id="PublisherA" align="left" value="publisher:مجلة جامعة الملك سعود" onclick="getExampleValue(this.id);">publisher:مجلة جامعة الملك سعود</a>  <!--King Saud University Journal-->  
                                    <!--<input id="idExampleFormatA" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="FormatA" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li> -->
                                </td>

                                <td>
                                    <input id="idExamplePublisherC" type="text" hidden="true" name="example" value="publisher:信州大学人文学部"><a id="PublisherC" align="left" value="publisher:信州大学人文学部" onclick="getExampleValue(this.id);">publisher:信州大学人文学部</a> <!--Facoltà di lettere e filosofia-->
                                    <!-- <input id="idExampleFormatC" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="FormatC" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li> -->
                                </td>
                                <td>
                                    <input id="idExampleFormatR" type="text" hidden="true" name="example" value="format:электронная копия"><a id="FormatR" align="left" value="format:электронная копия" onclick="getExampleValue(this.id);">format:электронная копия</a><!--Copia elettronica-->
                                </td>


                            </tr>
                            <tr>
                                <td id="firstColumn">
                                    <input id="idExamplePublisher" type="text" hidden="true" name="example" value="publisher:elsevier"><a id="Publisher" align="left" value="publisher:elsevier" onclick="getExampleValue(this.id);">publisher:elsevier</a>
                                </td>
                                <td>
                                    <!--   <input id="idExamplePublisherA" type="text" hidden="true" name="example" value="publisher:مجلة جامعة الملك سعود"><a id="PublisherA" align="left" value="publisher:مجلة جامعة الملك سعود" onclick="getExampleValue(this.id);">publisher:مجلة جامعة الملك سعود</a> King Saud University Journal-->
                                </td>
                                <td>
                                    <!--  <input id="idExamplePublisherC" type="text" hidden="true" name="example" value="publisher:信州大学人文学部"><a id="PublisherC" align="left" value="publisher:信州大学人文学部" onclick="getExampleValue(this.id);">publisher:信州大学人文学部</a> Facoltà di lettere e filosofia-->
                                </td>
                                <td>
                                    <input id="idExamplePublisherR" type="text" hidden="true" name="example" value="publisher:Тбилиси"><a id="PublisherR" align="left" value="publisher:Тбилиси" onclick="getExampleValue(this.id);">publisher:Тбилиси</a> <!--Tbilisi-->
                                </td>

                            </tr>
                        </table>

                    </div>


                    <input  hidden="true" name="numberOfPage" id="numberOfPage" value="<%=selected_page%>"/>
                    <input  hidden="true" name="moreResourceCHAIN" id="idMoreResourceCHAIN" value="<%=moreResourceCHAIN%>" />
                    <input  hidden="true" name="moreInfo" id="idMoreInfo" value="<%=moreInfo%>" />


                    <!-- //********** CODICE OPENAGRIS************ -->
                    <input  hidden="true" name="numberOfPageOpenAgris" id="numberOfPageOpenAgris" value="<%=selected_pageOpenAgris%>"/>
                    <input  hidden="true" name="moreResourceOpenAgris" id="idMoreResourceOpenAgris" value="<%=moreResourceOpenAgris%>"/>
                    <input  hidden="true" name="moreInfoOpenAgris" id="idMoreInfoOpenAgris" value="<%=moreInfoOpenAgris%>" />

                    <!-- //********** FINE CODICE OPENAGRIS************ -->

                    <!-- //********** CODICE CULTURA ITALIA************ -->
                    <input  hidden="true" name="numberOfPageCulturaItalia" id="numberOfPageCulturaItalia" value="<%=selected_pageCulturaItalia%>"/>
                    <input  hidden="true" name="moreResourceCulturaItalia" id="idMoreResourceCulturaItalia" value="<%=moreResourceCulturaItalia%>"/>
                    <input  hidden="true" name="moreInfoCulturaItalia" id="idMoreInfoCulturaItalia" value="<%=moreInfoCulturaItalia%>" />

                    <!-- //**********FINE CODICE CULTURA ITALIA************ -->

                    <!-- //********** CODICE Europeana************ -->
                    <input  hidden="true" name="numberOfPageEuropeana" id="numberOfPageEuropeana" value="<%=selected_pageEuropeana%>"/>
                    <input  hidden="true" name="moreResourceEuropeana" id="idMoreResourceEuropeana" value="<%=moreResourceEuropeana%>"/>
                    <input  hidden="true" name="moreInfoEuropeana" id="idMoreInfoEuropeana" value="<%=moreInfoEuropeana%>" />

                    <!-- //**********FINE CODICE Europeana************ -->

                    <!-- //********** CODICE Isidore************ -->
                    <input  hidden="true" name="numberOfPageIsidore" id="numberOfPageIsidore" value="<%=selected_pageIsidore%>"/>
                    <input  hidden="true" name="moreResourceIsidore" id="idMoreResourceIsidore" value="<%=moreResourceIsidore%>"/>
                    <input  hidden="true" name="moreInfoIsidore" id="idMoreInfoIsidore" value="<%=moreInfoIsidore%>" />

                    <!-- //**********FINE CODICE Isidore************ -->

                    <!-- //********** CODICE Pubmed************ -->
                    <input  hidden="true" name="numberOfPagePubmed" id="numberOfPagePubmed" value="<%=selected_pagePubmed%>"/>
                    <input  hidden="true" name="moreResourcePubmed" id="idMoreResourcePubmed" value="<%=moreResourcePubmed%>"/>
                    <input  hidden="true" name="moreInfoPubmed" id="idMoreInfoPubmed" value="<%=moreInfoPubmed%>" />

                    <!-- //**********FINE CODICE Pubmed************ -->

                    <!-- //********** CODICE Engage************ -->
                    <input  hidden="true" name="numberOfPageEngage" id="numberOfPageEngage" value="<%=selected_pageEngage%>"/>
                    <input  hidden="true" name="moreResourceEngage" id="idMoreResourceEngage" value="<%=moreResourceEngage%>"/>
                    <input  hidden="true" name="moreInfoEngage" id="idMoreInfoEngage" value="<%=moreInfoEngage%>" />

                    <!-- //**********FINE CODICE Engage************ -->

                    <!-- //********** CODICE DBPedia************ -->
                    <input  hidden="true" name="numberOfPageDBPedia" id="numberOfPageDBPedia" value="<%=selected_pageDBPedia%>"/>
                    <input  hidden="true" name="moreResourceDBPedia" id="idMoreResourceDBPedia" value="<%=moreResourceDBPedia%>"/>
                    <input  hidden="true" name="moreInfoDBPedia" id="idMoreInfoDBPedia" value="<%=moreInfoDBPedia%>" />

                    <!-- //**********FINE CODICE DBPedia************ -->



                </form>
            </div>
        </center>


        <div id="divResultSearch" >


            <div id="tabMenuResult" class="tabber">

                <div id="tabactive"  class="tabbertab" title="E-INFRA-KB" >




                    <%


                        int countId = 0;
                        String[] sArray = (String[]) request.getParameterValues("arrayVirtuosoResource");
                        ArrayList arrayVirtuosoResource = new ArrayList(Arrays.asList(sArray));

                        String[] sArrayChainTitle = (String[]) request.getParameterValues("arrayChainTitle");
                        ArrayList arrayChainTitle = new ArrayList(Arrays.asList(sArrayChainTitle));

                        String[] sArrayChainIdentifier = (String[]) request.getParameterValues("arrayChainIdentifier");
                        ArrayList arrayChainIdentifier = new ArrayList(Arrays.asList(sArrayChainIdentifier));

                        String[] sArrayChainAuthor = (String[]) request.getParameterValues("arrayChainAuthor");
                        ArrayList arrayChainAuthor = new ArrayList(Arrays.asList(sArrayChainAuthor));

                        String[] sArrayChainDescription = (String[]) request.getParameterValues("arrayChainDescription");
                        ArrayList arrayChainDescription = new ArrayList(Arrays.asList(sArrayChainDescription));
                        //System.out.println("LLLLLLL--->"+arrayChainDescription.size());

                        String[] sArrayChainRepository = (String[]) request.getParameterValues("arrayChainRepository");
                        ArrayList arrayChainRepository = new ArrayList(Arrays.asList(sArrayChainRepository));

                    %>


                    <div class="showpageArea">
                        <%  if (arrayVirtuosoResource.size() > 0) {
                                if (arrayVirtuosoResource.get(0).toString().equals("Exception")) {
                        %>
                        <span> The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayVirtuosoResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>

                    </div>

                    <table>

                        <%


                            if (arrayVirtuosoResource.size() > 0 && !arrayVirtuosoResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayVirtuosoResource.size(); i++) {
                                    String resource = arrayVirtuosoResource.get(i).toString();
                                    String doi_resource = "";

                        %>

                        <tr>
                            <td>
                                <table class=" resultSearch ">


                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayChainTitle.size() > 0) {%>


                                            <h5 id="countResource_<%=i%>" class=" klios">Title</h5>

                                            <%
                                                String titles = arrayChainTitle.get(i).toString();
                                                String[] stitles = titles.split("##");

                                                String identifiers = arrayChainIdentifier.get(i).toString();
                                                String[] sId = identifiers.split("##");

                                                String url = "";
                                                String url_altmetric="no link";
                                                for (int k = 0; k < sId.length; k++) {
                                                    String id = sId[k];
                                                    if (id.length() > 4 && id.substring(0, 4).equals("http")) {
                                                        url = id;

                                                    }
                                                    if (id.contains("doi")) {
                                                        if(id.contains("info:"))
                                                     {
                                                        doi_resource = id.substring(9).toString();
                                                      }else{
                                                         doi_resource = id.substring(5).toString();   
                                                      }
                                                        System.out.println("DOI=> "+doi_resource);
                                                        url_altmetric=Altmetric.getLinkAlmetricFromDOI(doi_resource);
                                                        
                                                       // System.out.println("URL_ALMETRIC"+url_almetric);



                                                    }

                                                }
                                                // System.out.println("URL_ALMETRIC---->"+url_altmetric);

                                            %>

                                            <ul class="klios_list"> 
                                                <form id="checkCitations" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_CITATIONS_GSCHOLAR"/></portlet:actionURL>" method="post">

                                                    <input id="title_GS" name="title_GS" value="" hidden="true" />

                                                    <input id="chek_altmetric" name="chek_altmetric" value="" hidden="true" />
                                                    <input id="url_altmetric" name="url_altmetric" value="" hidden="true" />
                                                    <input id="doi_altmetric" name="doi_altmetric" value="" hidden="true" />

                                                    <input id="url_altmetric_<%=countId%>" name="url_altmetric" value="<%=url_altmetric%>" hidden="true" />
                                                    <input id="doi_altmetric_<%=countId%>" name="doi_altmetric" value="<%=doi_resource%>" hidden="true" />
                                                    <%

                                                        //System.out.println("NUM-TITLE_CHAIN: "+stitles.length);
                                                        for (int j = 1; j < stitles.length; j++) {

                                                            String title = stitles[j];
                                                            //System.out.println("TITLE_CHAIN--->"+title);
                                                            //if(!title.equals("") || !title.equals(null) || !title.equals(" "))
                                                            //{
                                                            if (url != "") {
                                                    %>  <li><a href="<%= url%>" target="_blank" title="<%= url%>"><%= title%> </a>
                                                       <!-- <input id="titleResourceVirtuoso<%=countId + "--" + j%>" name="titleResourceVirtuoso" value="<%= title%>" hidden="true" />
                                                        <img id="counterResoureGS_<%=countId%>" style="cursor: pointer;width:20px;height: 20px;" src="<%=renderRequest.getContextPath()%>/images/gscholar_icon.png" onclick="CheckCitationsImage(<%=countId%>,<%=j%>)" />
                                                     <a href="http://localhost:8080/testlodlive/?<%=resource%>" target="_blank"><img id="<%=resource%>" style="cursor: pointer;width:80px;height: 25px;" src="<%=renderRequest.getContextPath()%>/images/lodliveLogo200px.jpg" /></a></li>-->



                                                        <input id="titleResourceVirtuoso<%=countId + "--" + j%>" name="titleResourceVirtuoso" value="<%= title%>" hidden="true" />

                                                        (<a id="counterResoureGS_<%=countId%>" href="#" style="cursor: pointer;"  onclick="CheckCitationsImage(<%=countId%>,<%=j%>)">Citations</a>)


                                                        (<a id ="counterResoureAlt_<%=countId%>" href="#"  style="cursor: pointer;" onclick="CheckAltmetrics(<%=countId%>,<%=j%>,'<%=url_altmetric%>','<%=doi_resource%>')" >Altmetrics</a>)



                                                        (<a href="<%=LodLiveEndPoint%>/?<%=resource%>"  style="cursor: pointer;" target="_blank">Linked Data</a>)</li>

                                                    <%
                                                    } else {
                                                    %>


                                                    <li><%= title%> 
                                                    <!-- <img id="counterResoureGS_<%=countId%>" style="cursor: pointer;width:20px;height: 20px;" src="<%=renderRequest.getContextPath()%>/images/gscholar_icon.png" onclick="CheckCitationsImage(<%=countId%>,<%=j%>)" />
                                                     <a href="http://localhost:8080/testlodlive/?<%=resource%>" target="_blank"><img id="<%=resource%>" style="cursor: pointer;width:80px;height: 25px;" src="<%=renderRequest.getContextPath()%>/images/lodliveLogo200px.jpg" /></a></li>
                                                        <input id="titleResourceVirtuoso<%=countId + "--" + j%>" name="titleResourceVirtuoso" value="<%= title%>" hidden="true" />-->
                                                        (<a id="counterResoureGS_<%=countId%>" href="#" style="cursor: pointer;" onclick="CheckCitationsImage(<%=countId%>,<%=j%>)">Citations</a>)


                                                        (<a id ="counterResoureAlt_<%=countId%>" href="#"  style="cursor: pointer;" onclick="CheckAltmetrics(<%=countId%>,<%=j%>,'<%=url_altmetric%>','<%=doi_resource%>')">Altmetrics</a>)





                                                        (<a href="<%=LodLiveEndPoint%>/?<%=resource%>" style="cursor: pointer;" target="_blank">Linked Data</a>)</li>

                                                    <input id="titleResourceVirtuoso<%=countId + "--" + j%>" name="titleResourceVirtuoso" value="<%= title%>" hidden="true" />
                                                    <%
                                                            }  //}
                                                        }

                                                    %>
                                                </form> 
                                            </ul>   


                                            <%
                                                    } //chiudo if(arrayChainTitle.size()>0)%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 





                                    <tr> <!-- riga author -->
                                        <td>
                                            <% if (!arrayChainAuthor.get(i).toString().equals("---")) {%>
                                            <h5 class="klios">Author</h5>
                                            <%
                                                String authors = arrayChainAuthor.get(i).toString();
                                                //String authors1=authors.substring(2);
                                                //String authorsFinal=authors1.replace("##","; ");
                                            %>
                                            <i> <%= authors%></i>
                                            <%}%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 



                                    <tr> <!-- riga description -->
                                        <td>
                                            <% if (!arrayChainDescription.get(i).toString().equals("---")) {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String description = arrayChainDescription.get(i).toString();
                                                String description1 = description.substring(2);
                                                String descriptionFinal = description1.split("##")[0];
                                                String preview_desc = "";

                                                if (descriptionFinal.contains("<table")) {

                                                    // System.out.println("OKKKKKKKKKKKK");
                                                    preview_desc = descriptionFinal.substring(0, 32).toString() + "...";

                                                    //System.out.println("OKKKKKKKKKKKK--->"+preview_desc);

                                                } else {
                                                    // System.out.println("description ["+0+"]= "+description);
                                                    //  System.out.println("ELSE");
                                                    if (descriptionFinal.length() > 299) {
                                                        preview_desc = descriptionFinal.substring(0, 300).toString() + "...";
                                                    } else {
                                                        preview_desc = descriptionFinal;
                                                    }

                                                }



                                                //if(descriptionFinal.length()>299)
                                                //    preview_desc=descriptionFinal.substring(0,300).toString()+"...";
                                                //else
                                                //    preview_desc=descriptionFinal;



                                            %>
                                            <i> <%= preview_desc%></i>
                                            <%}%>


                                        </td>

                                    </tr> <!-- chiudo riga description --> 

                                    <tr>
                                        <td>
                                            <form id="checkCitations" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_CITATIONS_GSCHOLAR"/></portlet:actionURL>" method="post">
                                                <br>

                                                <input id="title_GS" name="title_GS" value="" hidden="true" />
                                                <input id="chek_altmetric" name="chek_altmetric" value="" hidden="true" />
                                                <input id="url_altmetric" name="url_altmetric" value="" hidden="true" />
                                                <input id="doi_altmetric" name="doi_altmetric" value="" hidden="true" />

                                                <p id="counterResoureGS_<%=countId%>" style="cursor: pointer;color: green;" onclick="CheckCitations(this.id)">Check citations on Google Scholar</p>  
                                                <p id ="counterResoureAlt_<%=countId%>" style="cursor: pointer;color: green;" onclick="CheckAltmetrics2(this.id)">Check altmetrics on Altmetric</p>    
                                            </form>
                                        </td> 

                                    </tr>  





                                    <tr> <!-- riga repository -->
                                        <td>
                                            <% if (!arrayChainRepository.get(i).toString().equals("---")) {%>
                                            <h5 class="klios">Repository</h5>
                                            <%
                                                String rep = arrayChainRepository.get(i).toString();

                                                String name = rep.split("##")[0];
                                                String url = "";
                                                if (rep.split("##")[1] != null || rep.split("##")[1] == "") {
                                                    url = rep.split("##")[1];
                                                }
                                                if (!(url.length() > 4 && url.substring(0, 4).equals("http"))) {
                                                        url = "http://" + url;
                                                    }


                                                    if (url != "") {%>
                                            <a href="<%= url%>" target="_blank" title="<%= url%>"><u> <%= name%></u> </a>
                                            <% } else {%>
                                            <i><%=name%></i>

                                            <%}
                                                    }%>


                                        </td>

                                    </tr> <!-- chiudo riga repositroy --> 


                                    <tr>
                                        <td>
                                            <form id="searchDetail" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO"/></portlet:actionURL>" method="post">
                                                <input id="idResource" name="idResource" value="<%= resource%>" hidden="true" />
                                                <input id="idNumResource_<%=resource%>" name="numResourceTemp" value="<%=i%>" hidden="true" />
                                                <input id="numResource" name="numResource" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                                <input id="title_GS2" name="title_GS" value="" hidden="true" />
                                                <p id="counterResoure_<%=resource%>" style="cursor: pointer;color: red;" onclick="GoDetails(this.id,<%=countId%>)">More Info</p>    
                                            </form>
                                        </td>

                                    </tr>


                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>

                                </table>
                            </td>
                        </tr>





                        <%

                                    countId++;
                                }
                            }


                        %>


                    </table>

                    <%
                        //   int limitMax=Integer.parseInt(selected_page)*20;
                        int n = Integer.parseInt(numberRecordsForPage);
                        int limitMax = Integer.parseInt(selected_page) * n;
                        //System.out.println("LIMIT MAX: "+limitMax);   
                        if (arrayVirtuosoResource.size() > 0 && !arrayVirtuosoResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayVirtuosoResource.size()%>

                            <% if (arrayVirtuosoResource.size() == limitMax) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResources();" >----More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <% }%>


                </div> <!-- Chiudo il div Chain-KB -->


                <!-- CODICE OPENAGRIS**************** -->

                <% if (OpenAgris.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="OPENAGRIS" >        

                    <%


                        String[] sArrayOpenAgris = (String[]) request.getParameterValues("arrayOpenAgrisResource");


                        ArrayList arrayOpenAgrisResource = new ArrayList(Arrays.asList(sArrayOpenAgris));

                        // System.out.println("SIZE OpenAgris-->"+arrayOpenAgrisResource.size());
                        String[] sArrayOpenAgrisTitle = (String[]) request.getParameterValues("arrayOpenAgrisTitle");
                        ArrayList arrayOpenAgrisTitle = new ArrayList(Arrays.asList(sArrayOpenAgrisTitle));

                        String[] sArrayOpenAgrisAuthor = (String[]) request.getParameterValues("arrayOpenAgrisAuthor");
                        ArrayList arrayOpenAgrisAuthor = new ArrayList(Arrays.asList(sArrayOpenAgrisAuthor));

                        String[] sArrayOpenAgrisDescription = (String[]) request.getParameterValues("arrayOpenAgrisDescription");
                        ArrayList arrayOpenAgrisDescription = new ArrayList(Arrays.asList(sArrayOpenAgrisDescription));

                    %>

                    <div class="showpageArea">
                        <%  if (arrayOpenAgrisResource.size() > 0) {
                                if (arrayOpenAgrisResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayOpenAgrisResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>


                    <table>   
                        <%

                            if (arrayOpenAgrisResource.size() > 0 && !arrayOpenAgrisResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayOpenAgrisResource.size(); i++) {
                                    String resource = arrayOpenAgrisResource.get(i).toString();

                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayOpenAgrisTitle.size() > 0) {%>


                                            <h5 id="countResourceOpenAgris_<%=i%>" class=" klios">Title</h5>

                                            <%  String titles = arrayOpenAgrisTitle.get(i).toString();
                                                String[] stitles = titles.split("##");
                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];
                                                        if (!title.equals("")) {
                                                %>
                                                <li><%= title%>
                                                 <!--<a href="http://localhost:8080/testlodlive/?<%=resource%>" target="_blank"><img id="<%=resource%>" style="cursor: pointer;width:80px;height: 25px;" src="<%=renderRequest.getContextPath()%>/images/lodliveLogo200px.jpg" /></a></li>-->
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resource%>" target="_blank" id="<%=resource%>" style="cursor: pointer;">Linked Data</a>)</li>

                                                <%
                                                        }
                                                    }

                                                %>
                                            </ul>   


                                            <%
                                                    } //chiudo if(arrayOpenAgrisTitle.size()>0)%>
                                        </td>

                                    </tr> <!-- chiudo riga title --> 


                                    <tr> <!-- riga author -->
                                        <td>
                                            <% if (arrayOpenAgrisAuthor.size() > 0) {

                                                    if (!arrayOpenAgrisAuthor.get(i).toString().equals("---")) {
                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%
                                                String authors = arrayOpenAgrisAuthor.get(i).toString();
                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");
                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}
                                                    }%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <% if (arrayOpenAgrisDescription.size() > 0) {
                                                    if (!arrayOpenAgrisDescription.get(i).toString().equals("---")) {
                                            %>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String description = arrayOpenAgrisDescription.get(i).toString();
                                                String description1 = description.substring(2);
                                                String descriptionFinal = description1.replace("##", "; ");
                                            %>
                                            <i> <%= descriptionFinal%></i>
                                            <%}
                                                    }%>


                                        </td>

                                    </tr> <!-- chiudo riga description --> 


                                    <tr>
                                        <td>
                                            <form id="searchDetailOpenAgris" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_OPENAGRIS"/></portlet:actionURL>" method="post">
                                                <input id="idResourceOpenAgris" name="idResourceOpenAgris" value="<%= resource%>" hidden="true" />
                                                <p id="counterResourceOpenAgris_<%=resource%>" style="cursor: pointer;color: red;" onclick="GoDetailsOpenAgris(this.id)">More Info</p>    
                                                <input id="idNumResourceOpenAgris_<%=resource%>" name="numResourceOpenagrisTemp" value="<%=i%>" hidden="true" />
                                                <input id="numResourceOpenAgris" name="numResourceOpenAgris" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>




                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  

                                </table> <!-- chiudo <table class=" resultSearch "> -->

                            </td>
                        </tr>
                        <%
                                    } //chiudo il  for (int i=0;i<arrayOpenAgrisResource.size;i++)

                                }%> <!-- chiudo  if (arrayOpenAgrisResource.size()>0) -->


                    </table>  <!-- chiudo table principale OpenAgrisResult-->


                    <%
                        //int n2=Integer.parseInt(numberRecordsForPage);
                        int limitMaxOpenAgris = Integer.parseInt(selected_pageOpenAgris) * n;
                        //System.out.println("selected_pageOpenAgris "+selected_pageOpenAgris);
                        //   int limitMaxOpenAgris=Integer.parseInt(selected_pageOpenAgris)*20;

                        if (arrayOpenAgrisResource.size() > 0 && !arrayOpenAgrisResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayOpenAgrisResource.size()%>

                            <% if (arrayOpenAgrisResource.size() == limitMaxOpenAgris) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesOpenAgris();" >----More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <% }%>

                </div> <!-- chiudo il div OpenAgris-->
                <% }%>
                <!-- FINE CODICE OPENAGRIS**************** -->


                <!-- CODICE EUROPEANA**************** -->
                <% if (Europeana.equals("true")) {%>

                <div id="notabactive" class="tabbertab" title="EUROPEANA" >         
                    <%
                        String[] sArrayEuropeana = (String[]) request.getParameterValues("arrayEuropeanaResource");
                        ArrayList arrayEuropeanaResource = new ArrayList(Arrays.asList(sArrayEuropeana));

                        //System.out.println("SIZE Europeana from jsp-->"+arrayEuropeanaResource.size());
                        String[] sArrayEuropeanaTitle = (String[]) request.getParameterValues("arrayEuropeanaTitle");
                        ArrayList arrayEuropeanaTitle = new ArrayList(Arrays.asList(sArrayEuropeanaTitle));

                        String[] sArrayEuropeanaAuthor = (String[]) request.getParameterValues("arrayEuropeanaAuthor");
                        ArrayList arrayEuropeanaAuthor = new ArrayList(Arrays.asList(sArrayEuropeanaAuthor));

                        String[] sArrayEuropeanaDescription = (String[]) request.getParameterValues("arrayEuropeanaDescription");
                        ArrayList arrayEuropeanaDescription = new ArrayList(Arrays.asList(sArrayEuropeanaDescription));

                        String[] sArrayEuropeanaIdentifier = (String[]) request.getParameterValues("arrayEuropeanaIdentifier");

                        ArrayList arrayEuropeanaIdentifier = new ArrayList(Arrays.asList(sArrayEuropeanaIdentifier));

                        String[] sArrayEuropeanaType = (String[]) request.getParameterValues("arrayEuropeanaType");
                        ArrayList arrayEuropeanaType = new ArrayList(Arrays.asList(sArrayEuropeanaType));


                    %>
                    <div class="showpageArea">
                        <%  if (arrayEuropeanaResource.size() > 0) {
                                if (arrayEuropeanaResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayEuropeanaResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>
                    <table>   
                        <%

                            if (arrayEuropeanaResource.size() > 0 && !arrayEuropeanaResource.get(0).toString().equals("Exception")) {

                                for (int i = 0; i < arrayEuropeanaResource.size(); i++) {
                                    String resourceEuropeana = arrayEuropeanaResource.get(i).toString();

                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayEuropeanaTitle.size() > 0) {


                                                    ArrayList id_stringEuropeana = new ArrayList();
                                                    ArrayList id_httpEuropeana = new ArrayList();



                                                    if (arrayEuropeanaIdentifier.size() > 0) {


                                                        String idf = arrayEuropeanaIdentifier.get(i).toString();
                                                        String[] idfs = idf.split("##");
                                                        for (int j = 0; j < idfs.length; j++) {

                                                            if (idfs[j].length() > 4 && idfs[j].substring(0, 4).equals("http")) {
                                                                id_httpEuropeana.add(idfs[j].toString());
                                                            } else {
                                                                id_stringEuropeana.add(idfs[j].toString());
                                                            }
                                                        }

                                                    }


                                            %>

                                            <h5 id="countResourceEuropeana_<%=i%>" class=" klios">Title</h5>

                                            <%
                                                String titles = arrayEuropeanaTitle.get(i).toString();



                                                String[] stitles = titles.split("##");

                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];
                                                        if (id_httpEuropeana.size() > 0) {


                                                            String link = id_httpEuropeana.get(0).toString();

                                                            //System.out.println("linkkkkkkkkkk europeana "+link);
                                                            if (!title.equals("")) {
                                                %>
                                                <li><a href="<%= link%>" target="_blank" title="<%= link%>"><%= title%> </a>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceEuropeana%>" target="_blank" id="<%=resourceEuropeana%>" style="cursor: pointer;">Linked Data</a>)</li>

                                                <%
                                                    }
                                                } else {
                                                    if (!title.equals("")) {
                                                %>
                                                <li><%= title%>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceEuropeana%>" target="_blank" id="<%=resourceEuropeana%>" style="cursor: pointer;" >Linked Data</a>)</li>
                                                    <%
                                                                }
                                                            }
                                                        }
                                                        if (arrayEuropeanaType.size() > 0) {



                                                            String types = arrayEuropeanaType.get(i).toString();



                                                            String type = "";
                                                            if (!types.equals("") && !types.equals("---")) {
                                                                String[] stype_eu = types.split("##");
                                                                for (int k = 0; k < stype_eu.length; k++) {
                                                                    if (!stype_eu[k].toString().equals("")) {
                                                                        type = stype_eu[k].toString() + ";" + type;
                                                                    }
                                                                }
                                                    %> <li>(TYPE: <%=type%>)</li> <%

                                                              }




                                                          }%>
                                            </ul> 





                                            <%

                                                    }//chiudo Title%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 



                                    <tr><!--AUTORI-->
                                        <td>
                                            <%
                                                //System.out.println("ArrayEuropeanaAuthor "+arrayEuropeanaAuthor.size());
                                                if (arrayEuropeanaAuthor.size() > 0 && arrayEuropeanaAuthor.get(i) != "") {

                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%
                                                String authors = arrayEuropeanaAuthor.get(i).toString();
                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");
                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}%>


                                        </td>

                                    </tr>
                                    <tr> <!-- riga description -->
                                        <td>
                                            <%
                                                    //System.out.println("ArrayIsidoreDescription "+arrayIsidoreDescription.size());
                                                    if (arrayEuropeanaDescription.size() > 0 && arrayEuropeanaDescription.get(i).toString() != "") {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String descriptions = arrayEuropeanaDescription.get(i).toString();
                                                String[] sdescriptions = descriptions.split("##");
                                            %>
                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < sdescriptions.length; j++) {

                                                        String desc = sdescriptions[j];
                                                        if (desc != "") {
                                                %>
                                                <li><%= desc%></li>

                                                <%

                                                        }
                                                    }

                                                %>
                                            </ul>   



                                            <%}%>


                                        </td>


                                    </tr> <!-- chiudo riga description --> 





                                    <tr>
                                        <td>
                                            <form id="searchDetailEuropeana" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_EUROPEANA"/></portlet:actionURL>" method="post">
                                                <input id="idResourceEuropeana" name="idResourceEuropeana" value="<%= resourceEuropeana%>" hidden="true" />
                                                <input id="idNumResourceEuropeana_<%=resourceEuropeana%>" name="numResourceEuropeanaTemp" value="<%=i%>" hidden="true" />
                                                <p id="counterResourceEuropeana_<%=resourceEuropeana%>" style="cursor: pointer;color: red;" onclick="GoDetailsEuropeana(this.id)">More Info</p>    
                                                <input id="numResourceEuropeana" name="numResourceEuropeana" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>

                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  


                                </table> <!-- chiudo <table class=" resultSearch "> -->
                            </td>
                        </tr>

                        <%
                                    } //chiudo il  for (int i=0;i<arrayEuropeanaResource.size;i++)

                                }%> <!-- chiudo  Europeana -->

                    </table>  <!-- chiudo table principale Europeana-->



                    <%

                        //System.out.println("selected_pageEuropeanajspppp "+selected_pageEuropeana);
                        int limitMaxEuropeana = Integer.parseInt(selected_pageEuropeana) * n;

                        if (arrayEuropeanaResource.size() > 0 && !arrayEuropeanaResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayEuropeanaResource.size()%>

                            <% if (arrayEuropeanaResource.size() == limitMaxEuropeana) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesEuropeana();" >---More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <% }%>


                </div>
                <%

                        System.out.println("FINE EUROPEANA");
                    }%>
                <!--FINE CODICE EUROPEANA**************** -->

                <!-- CODICE CULTURA ITALIA**************** -->
                <% if (CulturaItalia.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="CULTURAITALIA" >  


                    <%String[] sArrayCulturaItalia = (String[]) request.getParameterValues("arrayCulturaItaliaResource");
                        ArrayList arrayCulturaItaliaResource = new ArrayList(Arrays.asList(sArrayCulturaItalia));

                        String[] sArrayCulturaItaliaTitle = (String[]) request.getParameterValues("arrayCulturaItaliaTitle");
                        ArrayList arrayCulturaItaliaTitle = new ArrayList(Arrays.asList(sArrayCulturaItaliaTitle));
                        String[] sArrayCulturaItaliaType = (String[]) request.getParameterValues("arrayCulturaItaliaType");
                        ArrayList arrayCulturaItaliaType = new ArrayList(Arrays.asList(sArrayCulturaItaliaType));
                        String[] sArrayCulturaItaliaAuthor = (String[]) request.getParameterValues("arrayCulturaItaliaAuthor");
                        ArrayList arrayCulturaItaliaAuthor = new ArrayList(Arrays.asList(sArrayCulturaItaliaAuthor));
                        String[] sArrayCulturaItaliaDescription = (String[]) request.getParameterValues("arrayCulturaItaliaDescription");
                        ArrayList arrayCulturaItaliaDescription = new ArrayList(Arrays.asList(sArrayCulturaItaliaDescription));
                    %>


                    <div class="showpageArea">
                        <%  if (arrayCulturaItaliaResource.size() > 0) {
                                if (arrayCulturaItaliaResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayCulturaItaliaResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>
                    <table>  
                        <%if (arrayCulturaItaliaResource.size() > 0 && !arrayCulturaItaliaResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayCulturaItaliaResource.size(); i++) {
                                    String resource = arrayCulturaItaliaResource.get(i).toString();
                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayCulturaItaliaTitle.size() > 0) {%>

                                            <h5 id="countResourceCulturaItalia_<%=i%>" class="klios">Title</h5>
                                            <%  String titles = arrayCulturaItaliaTitle.get(i).toString();
                                                String types = arrayCulturaItaliaType.get(i).toString();
                                                String[] stitles = titles.split("##");


                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];

                                                        if (!title.equals("")) {
                                                %>
                                                <li><%= title%>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resource%>" target="_blank" id="<%=resource%>" style="cursor: pointer;">Linked Data</a>)</li>

                                                <%}
                                                        }%>
                                                <li> (Type: <%=types%>)</li>
                                            </ul>



                                            <%
                                                    } //chiudo if(arrayOpenAgrisTitle.size()>0)%>
                                        </td>
                                    </tr> <!--chiudo riga Title--->


                                    <tr> <!-- riga author -->
                                        <td>
                                            <% if (arrayCulturaItaliaAuthor.size() > 0) {


                                                    if (!arrayCulturaItaliaAuthor.get(i).toString().equals("---")) {

                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%


                                                String authors = arrayCulturaItaliaAuthor.get(i).toString();
                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");

                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}
                                                    }%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <% if (arrayCulturaItaliaDescription.size() > 0) {


                                                    if (!arrayCulturaItaliaDescription.get(i).toString().equals("---")) {

                                            %>
                                            <h5 class="klios">Description</h5>
                                            <%


                                                String descriptions = arrayCulturaItaliaDescription.get(i).toString();
                                                String descriptions1 = descriptions.substring(2);
                                                String descriptionFinal = descriptions1.replace("##", "; ");

                                            %>
                                            <i> <%= descriptionFinal%></i>
                                            <%}
                                                    }%>


                                        </td>

                                    </tr> <!-- chiudo riga description --> 

                                    <tr>
                                        <td>
                                            <form id="searchDetailCulturaItalia" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_CULTURAITALIA"/></portlet:actionURL>" method="post">
                                                <input id="idResourceCulturaItalia" name="idResourceCulturaItalia" value="<%= resource%>" hidden="true" />
                                                <p id="counterResourceCulturaItalia_<%=resource%>" style="cursor: pointer;color: red;" onclick="GoDetailsCulturaItalia(this.id)">More Info</p>    
                                                <input id="idNumResourceCulturaItalia_<%=resource%>" name="numResourceCulturaItaliaTemp" value="<%=i%>" hidden="true" />
                                                <input id="numResourceCulturaItalia" name="numResourceCulturaItalia" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>




                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>     


                                </table> <!-- chiudo <table class=" resultSearch "> -->

                            </td>
                        </tr>
                        <% } //chiudo il  for (int i=0;i<arrayCulturaItaliaResource.size;i++)

                                }%> <!-- chiudo  if (arrayCulturaItaliaResource.size()>0) -->



                    </table><!-- chiudo table principale Cultura Italia-->


                    <%

                        //System.out.println("selected_pageCulturaItalia "+selected_pageCulturaItalia);
                        int limitMaxCulturaItalia = Integer.parseInt(selected_pageCulturaItalia) * n;
                        //int limitMaxCulturaItalia=Integer.parseInt(selected_pageCulturaItalia)*20;

                        if (arrayCulturaItaliaResource.size() > 0 && !arrayCulturaItaliaResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayCulturaItaliaResource.size()%>

                            <% if (arrayCulturaItaliaResource.size() == limitMaxCulturaItalia) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesCulturaItalia();" >----More Resources---</span>
                            </center>
                            <%}%>
                        </span>
                    </div>
                    <% }%>



                </div> <!-- chiudo il div Cultura Italia-->
                <% }%>
                <!-- FINE CODICE CULTURA ITALIA**************** -->

                <!-- CODICE ISIDORE**************** -->
                <% if (Isidore.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="ISIDORE" >         

                    <%
                        String[] sArrayIsidore = (String[]) request.getParameterValues("arrayIsidoreResource");
                        ArrayList arrayIsidoreResource = new ArrayList(Arrays.asList(sArrayIsidore));

                        System.out.println("SIZE Isidore-->" + arrayIsidoreResource.size());
                        String[] sArrayIsidoreTitle = (String[]) request.getParameterValues("arrayIsidoreTitle");
                        ArrayList arrayIsidoreTitle = new ArrayList(Arrays.asList(sArrayIsidoreTitle));

                        String[] sArrayIsidoreAuthor = (String[]) request.getParameterValues("arrayIsidoreAuthor");
                        ArrayList arrayIsidoreAuthor = new ArrayList(Arrays.asList(sArrayIsidoreAuthor));

                        String[] sArrayIsidoreDescription = (String[]) request.getParameterValues("arrayIsidoreDescription");
                        ArrayList arrayIsidoreDescription = new ArrayList(Arrays.asList(sArrayIsidoreDescription));

                        String[] sArrayIsidoreIdentifier = (String[]) request.getParameterValues("arrayIsidoreIdentifier");
                        ArrayList arrayIsidoreIdentifier = new ArrayList(Arrays.asList(sArrayIsidoreIdentifier));







                    %>
                    <div class="showpageArea">
                        <%  if (arrayIsidoreResource.size() > 0) {
                                if (arrayIsidoreResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayIsidoreResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>
                    <table>   
                        <%

                            if (arrayIsidoreResource.size() > 0 && !arrayIsidoreResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayIsidoreResource.size(); i++) {
                                    String resourceIsidore = arrayIsidoreResource.get(i).toString();

                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayIsidoreTitle.size() > 0) {


                                                    ArrayList id_stringIsidore = new ArrayList();
                                                    ArrayList id_httpIsidore = new ArrayList();



                                                    if (arrayIsidoreIdentifier.size() > 0) {


                                                        String idf = arrayIsidoreIdentifier.get(i).toString();
                                                        String[] idfs = idf.split("##");
                                                        for (int j = 0; j < idfs.length; j++) {

                                                            if (idfs[j].length() > 4 && idfs[j].substring(0, 4).equals("http")) {
                                                                id_httpIsidore.add(idfs[j].toString());
                                                            } else {
                                                                id_stringIsidore.add(idfs[j].toString());
                                                            }
                                                        }

                                                    }

                                            %>


                                            <h5 id="countResourceIsidore_<%=i%>" class=" klios">Title</h5>

                                            <%
                                                String titles = arrayIsidoreTitle.get(i).toString();



                                                String[] stitles = titles.split("##");
                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];
                                                        if (id_httpIsidore.size() > 0) {


                                                            String link = id_httpIsidore.get(0).toString();
                                                            //System.out.println("linkkkkkkkkkk "+link);
                                                            if (!title.equals("")) {
                                                %>
                                                <li><a href="<%= link%>" target="_blank" title="<%= link%>"><%= title%> </a>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceIsidore%>" target="_blank" id="<%=resourceIsidore%>" style="cursor: pointer;">Linked Data</a>)</li>

                                                <%
                                                    }
                                                } else {
                                                    if (!title.equals("")) {

                                                %>
                                                <li><%= title%>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceIsidore%>" target="_blank" id="<%=resourceIsidore%>" style="cursor: pointer;" >Linked Data</a>)</li>
                                                    <%}
                                                            }
                                                        }%>
                                            </ul> 



                                            <%

                                                    }//chiudo Title%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 


                                    <tr> <!-- riga author -->
                                        <td>
                                            <%
                                                //System.out.println("ArrayIsidoreAuthor "+arrayIsidoreAuthor.size());
                                                if (arrayIsidoreAuthor.size() > 0 && arrayIsidoreAuthor.get(i) != "") {

                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%

                                                String authors = arrayIsidoreAuthor.get(i).toString();
                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");
                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <%
                                                    //System.out.println("ArrayIsidoreDescription "+arrayIsidoreDescription.size());
                                                    if (arrayIsidoreDescription.size() > 0 && arrayIsidoreDescription.get(i).toString() != "") {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String descriptions = arrayIsidoreDescription.get(i).toString();
                                                String[] sdescriptions = descriptions.split("##");
                                            %>
                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < sdescriptions.length; j++) {

                                                        String desc = sdescriptions[j];
                                                        if (desc != "") {
                                                %>
                                                <li><%= desc%></li>

                                                <%

                                                        }
                                                    }

                                                %>
                                            </ul>   



                                            <%}%>


                                        </td>


                                    </tr> <!-- chiudo riga description --> 


                                    <tr>
                                        <td>
                                            <form id="searchDetailIsidore" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_ISIDORE"/></portlet:actionURL>" method="post">
                                                <input id="idResourceIsidore" name="idResourceIsidore" value="<%= resourceIsidore%>" hidden="true" />
                                                <input id="idNumResourceIsidore_<%=resourceIsidore%>" name="numResourceIsidoreTemp" value="<%=i%>" hidden="true" />
                                                <p id="counterResourceIsidore_<%=resourceIsidore%>" style="cursor: pointer;color: red;" onclick="GoDetailsIsidore(this.id)">More Info</p>    
                                                <input id="numResourceIsidore" name="numResourceIsidore" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>

                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  


                                </table> <!-- chiudo <table class=" resultSearch "> -->
                            </td>
                        </tr>
                        <%
                                    } //chiudo il  for (int i=0;i<arrayOpenAgrisResource.size;i++)

                                }%> <!-- chiudo  Isidore -->

                    </table>  <!-- chiudo table principale OpenAgrisResult-->


                    <%

                        //System.out.println("selected_pageIsidore jspppp "+selected_pageIsidore);
                        int limitMaxIsidore = Integer.parseInt(selected_pageIsidore) * n;

                        if (arrayIsidoreResource.size() > 0 && !arrayIsidoreResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayIsidoreResource.size()%>

                            <% if (arrayIsidoreResource.size() == limitMaxIsidore) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesIsidore();" >---More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <%}%>


                </div>
                <% }%>
                <!-- FINE CODICE ISIDORE**************** -->

                <!-- CODICE PUBMED**************** -->
                <% if (Pubmed.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="PUBMED" >         

                    <%
                        String[] sArrayPubmed = (String[]) request.getParameterValues("arrayPubmedResource");
                        ArrayList arrayPubmedResource = new ArrayList(Arrays.asList(sArrayPubmed));

                        //System.out.println("SIZE Isidore-->"+arrayIsidoreResource.size());
                        String[] sArrayPubmedTitle = (String[]) request.getParameterValues("arrayPubmedTitle");
                        ArrayList arrayPubmedTitle = new ArrayList(Arrays.asList(sArrayPubmedTitle));

                        String[] sArrayPubmedAuthor = (String[]) request.getParameterValues("arrayPubmedAuthor");
                        ArrayList arrayPubmedAuthor = new ArrayList(Arrays.asList(sArrayPubmedAuthor));

                        String[] sArrayPubmedDescription = (String[]) request.getParameterValues("arrayPubmedDescription");
                        ArrayList arrayPubmedDescription = new ArrayList(Arrays.asList(sArrayPubmedDescription));

                        String[] sArrayPubmedURL = (String[]) request.getParameterValues("arrayPubmedURL");
                        ArrayList arrayPubmedURL = new ArrayList(Arrays.asList(sArrayPubmedURL));

                        //  String [] sArrayPubmedURI=(String[])request.getParameterValues("arrayPubmedURI");




                    %>
                    <div class="showpageArea">
                        <%  if (arrayPubmedResource.size() > 0) {
                                if (arrayPubmedResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayPubmedResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>
                    <table>   
                        <%

                            if (arrayPubmedResource.size() > 0 && !arrayPubmedResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayPubmedResource.size(); i++) {
                                    String resourcePubmed = arrayPubmedResource.get(i).toString();

                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayPubmedTitle.size() > 0) {

                                                    String URL = "";
                                                    if (arrayPubmedURL.size() > 0) {
                                                        String url_ = arrayPubmedURL.get(i).toString();
                                                        String[] surl = url_.split("##");

                                                        for (int j = 0; j < surl.length; j++) {
                                                            if (surl[j] != "") {
                                                                URL = surl[j];
                                                            }

                                                        }
                                                    }

                                            %>


                                            <h5 id="countResourcePubmed_<%=i%>" class=" klios">Title</h5>

                                            <%
                                                String titles = arrayPubmedTitle.get(i).toString();
                                                if (titles != null) {


                                                    String[] stitles = titles.split("##");
                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];

                                                        if (URL != "" && URL != "---") {
                                                            if (!title.equals("")) {
                                                %>
                                                <li><a href="<%= URL%>" target="_blank" title="<%=URL%>"><%= title%> </a>
                                                    [<a href="<%=LodLiveEndPoint%>/?<%=resourcePubmed%>" target="_blank" id="<%=resourcePubmed%>" style="cursor: pointer;">Linked Data</a>]</li>

                                                <%

                                                    }
                                                } else {
                                                    if (!title.equals("")) {
                                                %>
                                                <li><%= title%>
                                                    [<a href="<%=LodLiveEndPoint%>/?<%=resourcePubmed%>" target="_blank" id="<%=resourcePubmed%>" style="cursor: pointer;">Linked Data</a>]
                                                </li>

                                                <%
                                                            }
                                                        }%>

                                                <%}%>
                                            </ul>

                                            <%

                                                        }
                                                    }//chiudo Title%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 


                                    <tr> <!-- riga author -->
                                        <td>
                                            <%
                                                //System.out.println("ArrayIsidoreAuthor "+arrayIsidoreAuthor.size());
                                                if (arrayPubmedAuthor.size() > 0 && arrayPubmedAuthor.get(i) != "---") {

                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%
                                                String authors = arrayPubmedAuthor.get(i).toString();

                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");


                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <%
                                                    //System.out.println("ArrayIsidoreDescription "+arrayIsidoreDescription.size());
                                                    if (arrayPubmedDescription.size() > 0 && arrayPubmedDescription.get(i).toString() != "---") {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String descriptions = arrayPubmedDescription.get(i).toString();
                                                String[] sdescriptions = descriptions.split("##");
                                            %>
                                            <ul class="klios_list"> 
                                                <%

                                                    String desc = sdescriptions[1];
                                                    String preview_desc = "";

                                                    if (desc != "") {
                                                        if (desc.length() > 299) {
                                                            preview_desc = desc.substring(0, 300).toString() + "...";
                                                        } else {
                                                            preview_desc = desc;
                                                        }


                                                %>
                                                <li><%= preview_desc%></li>

                                                <%

                                                    }


                                                %>
                                            </ul>   



                                            <%}%>


                                        </td>


                                    </tr> <!-- chiudo riga description --> 


                                    <tr>
                                        <td>
                                           <!-- <form id="searchDetailPubmed" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_PUBMED"/></portlet:actionURL>" method="post">
                                                <input id="idResourcePubmed" name="idResourcePubmed" value="<%= resourcePubmed%>" hidden="true" />
                                                <input id="idNumResourcePubmed_<%=resourcePubmed%>" name="numResourcePubmedTemp" value="<%=i%>" hidden="true" />
                                                <p id="counterResourcePubmed_<%=resourcePubmed%>" style="cursor: pointer;color: red;" onclick="GoDetailsPubmed(this.id)">More Info</p>    
                                                <input id="numResourcePubmed" name="numResourcePubmed" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form> -->
                                            <a href="http://clinicaltrials.bio2rdf.org/describe/?url=<%=resourcePubmed%>" target="_blank " style="cursor: pointer;color: red;">  More Info</a>

                                        </td>

                                    </tr>

                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  


                                </table> <!-- chiudo <table class=" resultSearch "> -->
                            </td>
                        </tr>
                        <%
                                    } //chiudo il  for (int i=0;i<arrayOpenAgrisResource.size;i++)

                                }%> <!-- chiudo  Isidore -->

                    </table>  <!-- chiudo table principale Pubmed-->


                    <%

                        //System.out.println("selected_pageIsidore jspppp "+selected_pageIsidore);
                        int limitMaxPubmed = Integer.parseInt(selected_pagePubmed) * n;

                        if (arrayPubmedResource.size() > 0 && !arrayPubmedResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayPubmedResource.size()%>

                            <% if (arrayPubmedResource.size() == limitMaxPubmed) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesPubmed();" >---More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <%}%>


                </div>
                <% }%>




                <% if (DBPedia.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="DBPEDIA" > 
                    <%
                        String[] sArrayDBPedia = (String[]) request.getParameterValues("arrayDBPediaResource");
                        ArrayList arrayDBPediaResource = new ArrayList(Arrays.asList(sArrayDBPedia));
                        System.out.println("SIZE DBPEDIA-->" + arrayDBPediaResource.size());
                        String[] sArrayDBPediaTitle = (String[]) request.getParameterValues("arrayDBPediaTitle");
                        ArrayList arrayDBPediaTitle = new ArrayList(Arrays.asList(sArrayDBPediaTitle));
                        String[] sArrayDBPediaDesc = (String[]) request.getParameterValues("arrayDBPediaDescription");
                        ArrayList arrayDBPediaDesc = new ArrayList(Arrays.asList(sArrayDBPediaDesc));%>

                    <div class="showpageArea">
                        <%  if (arrayDBPediaResource.size() > 0) {
                                if (arrayDBPediaResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayDBPediaResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>

                    <table>   
                        <%

                            if (arrayDBPediaResource.size() > 0 && !arrayDBPediaResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayDBPediaResource.size(); i++) {
                                    String resourceDBPedia = arrayDBPediaResource.get(i).toString();

                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayDBPediaTitle.size() > 0) {

                                            %>


                                            <h5 id="countResourceDBPedia_<%=i%>" class=" klios">Title</h5>

                                            <ul class="klios_list"> 
                                                <% 
                                                    String titles = arrayDBPediaTitle.get(i).toString();
                                                        if (titles != null) {


                                                    String[] stitles = titles.split("##");
                                                
                                                
                                                
                                                for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];
                                                        
                                                        if (!title.equals("")) {

                                                %>
                                                <li><%= title%>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceDBPedia%>" target="_blank" id="<%=resourceDBPedia%>" style="cursor: pointer;" >Linked Data</a>)</li>
                                                    <%}

                                                        }%>
                                            </ul> 

                                            <%}

                                                    }//chiudo Title%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <%
                                                    //System.out.println("ArrayIsidoreDescription "+arrayIsidoreDescription.size());
                                                    if (arrayDBPediaDesc.size() > 0 && arrayDBPediaDesc.get(i).toString() != "---") {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String descriptions = arrayDBPediaDesc.get(i).toString();
                                                String desc="";
                                                 String[] sdescriptions=null;
                                                if(descriptions.contains("##")){
                                                   sdescriptions = descriptions.split("##");
                                                   desc = sdescriptions[1];
                                                }else
                                                    desc= descriptions;
                                                
                                            %>
                                            <ul class="klios_list"> 
                                                <%
                                                   
                                                    
                                                    String preview_desc = "";

                                                    if (desc != "") {
                                                        if (desc.length() > 299) {
                                                            preview_desc = desc.substring(0, 300).toString() + "...";
                                                        } else {
                                                            preview_desc = desc;
                                                        }


                                                %>
                                                <li><%= preview_desc%></li>

                                                <%

                                                    }


                                                %>
                                            </ul>   



                                            <%}%>


                                        </td>


                                    </tr> <!-- chiudo riga description --> 

                                    <tr>
                                        <td>
                                            <form id="searchDetailDBPedia" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_DBPEDIA"/></portlet:actionURL>" method="post">
                                                <input id="idResourceDBPedia" name="idResourceDBPedia" value="<%= resourceDBPedia%>" hidden="true" />
                                                <input id="idNumResourceDBPedia_<%=resourceDBPedia%>" name="numResourceDBPediaTemp" value="<%=i%>" hidden="true" />
                                                <p id="counterResourceDBPedia_<%=resourceDBPedia%>" style="cursor: pointer;color: red;" onclick="GoDetailsDBPedia(this.id)">More Info</p>    
                                                <input id="numResourceDBPedia" name="numResourceDBPedia" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>

                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  


                                </table> 
                            </td>
                        </tr>
                        <%            } //chiudo il  for (int i = 0; i < arrayDBPediaResource.size(); i++)

                                                        }%> <!-- chiudo if DBPedia -->
                    </table>
                    <%

             //System.out.println("selected_pageIsidore jspppp "+selected_pageIsidore);
             int limitMaxDBPedia = Integer.parseInt(selected_pageDBPedia) * n;

             if (arrayDBPediaResource.size() > 0 && !arrayDBPediaResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayDBPediaResource.size()%>

                            <% if (arrayDBPediaResource.size() == limitMaxDBPedia) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesDBPedia();" >---More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <%}%>


                </div>
                <%}%> <!-- chiudo DBPedia -->











                <!-- CODICE ENGAGE**************** -->
                <% if (Engage.equals("true")) {%>
                <div id="notabactive" class="tabbertab" title="ENGAGE" > 
                    <%


                        //System.out.println("SIZE Isidore-->"+arrayIsidoreResource.size());
                        String[] sArrayEngageResourceHomepage = (String[]) request.getParameterValues("arrayEngageResource");
                        ArrayList arrayEngageResource = new ArrayList(Arrays.asList(sArrayEngageResourceHomepage));


                        String[] sArrayEngageTitle = (String[]) request.getParameterValues("arrayEngageTitle");
                        ArrayList arrayEngageTitle = new ArrayList(Arrays.asList(sArrayEngageTitle));

                        String[] sArrayEngageAuthor = (String[]) request.getParameterValues("arrayEngageAuthor");
                        ArrayList arrayEngageAuthor = new ArrayList(Arrays.asList(sArrayEngageAuthor));

                        String[] sArrayEngageDescription = (String[]) request.getParameterValues("arrayEngageDescription");
                        ArrayList arrayEngageDescription = new ArrayList(Arrays.asList(sArrayEngageDescription));

                        String[] sArrayEngageHomepage = (String[]) request.getParameterValues("arrayEngageHomepage");
                        ArrayList arrayEngageHomepage = new ArrayList(Arrays.asList(sArrayEngageHomepage));







                    %>
                    <div class="showpageArea">
                        <%  if (arrayEngageResource.size() > 0) {
                                if (arrayEngageResource.get(0).toString().equals("Exception")) {
                        %>
                        <span>The service is temporarily unavailable!</span>
                        <% } else {%>
                        <span> Records found. Displaying  1 to <%=arrayEngageResource.size()%></span>
                        <% }

                        } else {
                        %>
                        <span> Records not found for "<%=searched_word%>".</span>
                        <%}%>
                    </div>
                    <table>   
                        <%

                            if (arrayEngageResource.size() > 0 && !arrayEngageResource.get(0).toString().equals("Exception")) {
                                for (int i = 0; i < arrayEngageResource.size(); i++) {
                                    String homepageResourceEngage = arrayEngageResource.get(i).toString();


                        %>  
                        <tr>
                            <td>
                                <table class=" resultSearch ">

                                    <tr> <!-- riga title -->
                                        <td>
                                            <% if (arrayEngageTitle.size() > 0) {



                                                    String id_httpEngage = "";



                                                    if (arrayEngageHomepage.size() > 0) {


                                                        String idf = arrayEngageHomepage.get(i).toString();
                                                        // System.out.println("homepage ENGage: "+idf);
                                                        //String [] idfs=  idf.split("##");


                                                        if (idf.length() > 4 && idf.substring(0, 4).equals("http")) {
                                                            id_httpEngage = idf;
                                                        }



                                                    }

                                            %>


                                            <h5 id="countResourceEngage_<%=i%>" class=" klios">Title</h5>

                                            <%
                                                String titlesEngage = arrayEngageTitle.get(i).toString();



                                                String[] stitles = titlesEngage.split("##");
                                            %>

                                            <ul class="klios_list"> 
                                                <% for (int j = 0; j < stitles.length; j++) {

                                                        String title = stitles[j];
                                                        if (id_httpEngage != "") {

                                                            if (!title.equals("")) {

                                                                    //System.out.println("linkkkkkkkkkk "+link);
                                                %>
                                                <li><a href="<%= id_httpEngage%>" target="_blank" title="<%= id_httpEngage%>"><%= title%> </a>
                                                    <%

                                                        String res = "www.engagedata.eu/rdf/dataset/";
                                                        String numResource = homepageResourceEngage.substring(7).split("/")[2];
                                                        String resourceEng = res + "" + numResource;
                                                        System.out.println("RISORSA DI ENGAGE===>>" + resourceEng);
                                                    %>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=resourceEng%>" target="_blank" id="<%=homepageResourceEngage%>" style="cursor: pointer;">Linked Data</a>)</li>

                                                <%
                                                    }
                                                } else {
                                                    if (!title.equals("")) {
                                                %>
                                                <li><%= title%>
                                                    (<a href="<%=LodLiveEndPoint%>/?<%=homepageResourceEngage%>" target="_blank" id="<%=homepageResourceEngage%>" style="cursor: pointer;">Linked Data</a>)</li>
                                                    <%
                                                                }
                                                            }
                                                        }%>
                                            </ul> 



                                            <%

                                                    }//chiudo Title%>
                                        </td>
                                    </tr> <!-- chiudo riga title --> 


                                    <tr> <!-- riga author -->
                                        <td>
                                            <%
                                                //System.out.println("ArrayIsidoreAuthor "+arrayIsidoreAuthor.size());
                                                if (arrayEngageAuthor.size() > 0 && arrayEngageAuthor.get(i) != "") {

                                            %>
                                            <h5 class="klios">Author</h5>
                                            <%
                                                String authors = arrayEngageAuthor.get(i).toString();
                                                String authors1 = authors.substring(2);
                                                String authorsFinal = authors1.replace("##", "; ");
                                            %>
                                            <i> <%= authorsFinal%></i>
                                            <%}%>


                                        </td>

                                    </tr> <!-- chiudo riga author --> 

                                    <tr> <!-- riga description -->
                                        <td>
                                            <%
                                                    //System.out.println("ArrayIsidoreDescription "+arrayIsidoreDescription.size());
                                                    if (arrayEngageDescription.size() > 0 && arrayEngageDescription.get(i).toString() != "") {%>
                                            <h5 class="klios">Description</h5>
                                            <%
                                                String descriptions = arrayEngageDescription.get(i).toString();
                                                String[] sdescriptions = descriptions.split("##");
                                            %>
                                            <ul class="klios_list"> 
                                                <%

                                                    String desc = sdescriptions[1];

                                                    String preview_desc = "";

                                                    if (desc != "") {
                                                        if (desc.length() > 299) {
                                                            preview_desc = desc.substring(0, 300).toString() + "...";
                                                        } else {
                                                            preview_desc = desc;
                                                        }

                                                %>
                                                <li><%= preview_desc%></li>

                                                <%

                                                    }
                                                %> 

                                            </ul>   



                                            <%}%>


                                        </td>


                                    </tr> <!-- chiudo riga description --> 


                                    <tr>
                                        <td>
                                            <form id="searchDetailEngage" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_GET_MORE_INFO_ENGAGE"/></portlet:actionURL>" method="post">
                                                <input id="idResourceEngage" name="idResourceEngage" value="<%= homepageResourceEngage%>" hidden="true" />
                                                <input id="idNumResourceEngage_<%=homepageResourceEngage%>" name="numResourceEngageTemp" value="<%=i%>" hidden="true" />
                                                <p id="counterResourceEngage_<%=homepageResourceEngage%>" style="cursor: pointer;color: red;" onclick="GoDetailsEngage(this.id)">More Info</p>    
                                                <input id="numResourceEngage" name="numResourceEngage" value="" hidden="true" />
                                                <input id="id_search_word2" name="search_word" value="<%=searched_word%>" hidden="true" />
                                            </form>
                                        </td>

                                    </tr>

                                    <tr><td> <hr class="endRecordAll" noshade="noshade"></td></tr>  


                                </table> <!-- chiudo <table class=" resultSearch "> -->
                            </td>
                        </tr>
                        <%
                                    } //chiudo il  for (int i=0;i<arrayOpenAgrisResource.size;i++)

                                }%> <!-- chiudo  Isidore -->

                    </table>  <!-- chiudo table principale OpenAgrisResult-->


                    <%

                        //System.out.println("selected_pageIsidore jspppp "+selected_pageIsidore);
                        int limitMaxEngage = Integer.parseInt(selected_pageEngage) * n;

                        if (arrayEngageResource.size() > 0 && !arrayEngageResource.get(0).toString().equals("Exception")) {%>
                    <div class="showpageArea">


                        <span> Records found. Displaying  1 to <%=arrayEngageResource.size()%>

                            <% if (arrayEngageResource.size() == limitMaxEngage) {%>

                            <center><span class="showMoreResurces" id="idMoreResources"  onclick="moreResourcesEngage();" >---More Resources---</span>
                            </center>
                            <%}%>
                        </span>



                    </div>
                    <%}%>


                </div>

                <%}%>



            </div>    <!-- chiudo il div gestione tabber -->       


            <div id="dialog2" title="Information" hidden="true" >
                <center>
                    <p>Searching...</p>
                    <p>This may take some time</p>
                    <p>(up to <%=TimeOut%> min.)</p>
                </center>
            </div> 

        </div>  <!-- chiudo il div divResultSearch -->

        <script>
               
            $( "#dialog2" ).dialog({ autoOpen: false });
            $( "#dialogExamples" ).dialog({ autoOpen: false });
               
            //GESTIONE TAB
            activeChainAfterMoreResource();
            activeChainAfterMoreInfo();
               
            activeOpenAgrisAfterMoreResource();
            activeOpenAgrisAfterMoreInfo();
               
            activeCulturaItaliaAfterMoreResource();
            activeCulturaItaliaAfterMoreInfo();
                
            activeEuropeanaAfterMoreResource();
            activeEuropeanaAfterMoreInfo();
               
            activeIsidoreAfterMoreResource();
            activeIsidoreAfterMoreInfo();
                
            activePubmedAfterMoreResource();
            activePubmedAfterMoreInfo();
            
            activeDBPediaAfterMoreResource();
            activeDBPediaAfterMoreInfo();
               
            activeEngageAfterMoreResource();
            activeEngageAfterMoreInfo();
               
               
                               
                               
                               
            function moreResources()
            {
                                  
                var nvalue=parseInt(document.getElementById("numberOfPage").value);
                document.getElementById("numberOfPage").value=nvalue+1;
                document.getElementById("idMoreResourceCHAIN").value="OK";
                document.getElementById("idMoreInfo").value="NO";
                document.getElementById("idMoreResourceOpenAgris").value="NO";
                document.getElementById("idMoreInfoOpenAgris").value="NO";
                document.getElementById("idMoreResourceCulturaItalia").value="NO";
                document.getElementById("idMoreInfoCulturaItalia").value="NO";
                document.getElementById("idMoreResourceIsidore").value="NO";
                document.getElementById("idMoreInfoIsidore").value="NO";
                document.getElementById("idMoreResourceEuropeana").value="NO";
                document.getElementById("idMoreInfoEuropeana").value="NO";
                document.getElementById("idMoreResourcePubmed").value="NO";
                document.getElementById("idMoreInfoPubmed").value="NO";
                document.getElementById("idMoreResourceDBPedia").value="NO";
                document.getElementById("idMoreInfoDBPedia").value="NO";
                document.getElementById("idMoreResourceEngage").value="NO";
                document.getElementById("idMoreInfoEngage").value="NO";
                // alert("VALUE MORE----> "+document.getElementById("numberOfPage").value);
                document.forms["search_form"].submit();
                document.body.style.cursor = "wait";
                showDialog();
                                    
            }
                
            function moreResourcesOpenAgris()
            {
                                  
                var nvalue=parseInt(document.getElementById("numberOfPageOpenAgris").value);
                document.getElementById("numberOfPageOpenAgris").value=nvalue+1;
                document.getElementById("idMoreResourceOpenAgris").value="OK";
                document.getElementById("idMoreInfoOpenAgris").value="NO";
                document.getElementById("idMoreResourceCHAIN").value="NO";
                document.getElementById("idMoreInfo").value="NO";
                document.getElementById("idMoreResourceCulturaItalia").value="NO";
                document.getElementById("idMoreInfoCulturaItalia").value="NO";
                document.getElementById("idMoreResourceIsidore").value="NO";
                document.getElementById("idMoreInfoIsidore").value="NO";
                document.getElementById("idMoreResourceEuropeana").value="NO";
                document.getElementById("idMoreInfoEuropeana").value="NO";
                document.getElementById("idMoreResourcePubmed").value="NO";
                document.getElementById("idMoreInfoPubmed").value="NO";
                document.getElementById("idMoreResourceDBPedia").value="NO";
                document.getElementById("idMoreInfoDBPedia").value="NO";
                document.getElementById("idMoreResourceEngage").value="NO";
                document.getElementById("idMoreInfoEngage").value="NO";
                    
                document.forms["search_form"].submit();
                document.body.style.cursor = "wait";
                showDialog();
                                    
            }
                
            function moreResourcesCulturaItalia()
            {
                                  
                var nvalue=parseInt(document.getElementById("numberOfPageCulturaItalia").value);
                var page=document.getElementById("numberOfPageCulturaItalia").value=nvalue+1;
                   
                document.getElementById("idMoreResourceCulturaItalia").value="OK";
                document.getElementById("idMoreInfoCulturaItalia").value="NO";
                document.getElementById("idMoreResourceOpenAgris").value="NO";
                document.getElementById("idMoreInfoOpenAgris").value="NO";
                document.getElementById("idMoreResourceCHAIN").value="NO";
                document.getElementById("idMoreInfoCulturaItalia").value="NO";
                document.getElementById("idMoreResourceIsidore").value="NO";
                document.getElementById("idMoreInfoIsidore").value="NO";
                document.getElementById("idMoreResourceEuropeana").value="NO";
                document.getElementById("idMoreInfoEuropeana").value="NO";
                document.getElementById("idMoreResourcePubmed").value="NO";
                document.getElementById("idMoreInfoPubmed").value="NO";
                document.getElementById("idMoreResourceDBPedia").value="NO";
                document.getElementById("idMoreInfoDBPedia").value="NO";
                document.getElementById("idMoreResourceEngage").value="NO";
                document.getElementById("idMoreInfoEngage").value="NO";
                    
                document.forms["search_form"].submit();
                document.body.style.cursor = "wait";
                showDialog();
                                    
            }
                
        function moreResourcesEuropeana()
        {
                                  
            var nvalue=parseInt(document.getElementById("numberOfPageEuropeana").value);
            var page=document.getElementById("numberOfPageEuropeana").value=nvalue+1;
                   
            document.getElementById("idMoreResourceEuropeana").value="OK";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfo").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.forms["search_form"].submit();
            document.body.style.cursor = "wait";
            showDialog();
                                    
        }
                
        function moreResourcesIsidore()
        {
                                  
            var nvalue=parseInt(document.getElementById("numberOfPageIsidore").value);
            var page=document.getElementById("numberOfPageIsidore").value=nvalue+1;
                   
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfo").value="NO";
            document.getElementById("idMoreResourceIsidore").value="OK";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.forms["search_form"].submit();
            document.body.style.cursor = "wait";
            showDialog();
                                    
        }
        function moreResourcesPubmed()
        {
                               
            var nvalue=parseInt(document.getElementById("numberOfPagePubmed").value);
                    
            var page=document.getElementById("numberOfPagePubmed").value=nvalue+1;
                   
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfo").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourcePubmed").value="OK";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.forms["search_form"].submit();
            document.body.style.cursor = "wait";
            showDialog();
        }
            
        function moreResourcesDBPedia()
        {
                               
            var nvalue=parseInt(document.getElementById("numberOfPageDBPedia").value);
                    
            var page=document.getElementById("numberOfPageDBPedia").value=nvalue+1;
                  
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfo").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="OK";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.forms["search_form"].submit();
            document.body.style.cursor = "wait";
            showDialog();
        }
                
        function moreResourcesEngage()
        {
                               
            var nvalue=parseInt(document.getElementById("numberOfPageEngage").value);
                    
            var page=document.getElementById("numberOfPageEngage").value=nvalue+1;
                   
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfo").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="OK";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.forms["search_form"].submit();
            document.body.style.cursor = "wait";
            showDialog();
        }
                
             
                



        function activeChainAfterMoreResource()
        {
                     
            //******** tab CHAIN attivo dopo MoreResource***********  
            if(document.getElementById("idMoreResourceCHAIN").value=="OK") 
            {
                //alert("CHAIN attivo");
                document.getElementById("idMoreResourceCHAIN").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB"){
                        div.id ='tabactive';
                        // alert("1")
                        var page=document.getElementById("numberOfPage").value;
                        // alert("Page"+page);
            <% int limit = Integer.parseInt(numberRecordsForPage) + 1;%>
                               
                            var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                            //alert ("COUNTER--->"+counter);
                            
                            var x=$("#countResource_"+counter).position();
                            // alert(counter+"-->TOP-->"+x.top+" --->"+x.left);
                                                                                                                                         
                            window.scrollTo(x.left,x.top);
                        }
                        if(title=="OPENAGRIS")
                            div.id='notabactive';
                        if(title=="CULTURAITALIA")
                            div.id='notabactive';
                        if(title=="ISIDORE")
                            div.id ='notabactive';
                        if(title=="EUROPEANA")
                            div.id ='notabactive';
                        if(title=="PUBMED")
                            div.id ='notabactive';
                        if(title=="DBPEDIA")
                            div.id ='notabactive';
                        if(title=="ENGAGE")
                            div.id ='notabactive';
                    }           
                }
                    
            }
                
        function activeChainAfterMoreInfo(){
            //******** tab CHAIN attivo dopo MoreInfo***********  
            if(document.getElementById("idMoreInfo").value=="OK") 
            {
                //alert("CHAIN attivo");
                document.getElementById("idMoreInfo").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB"){
                        div.id ='tabactive';
                               
                        var x=$("#countResource_"+<%=numResourceFromDetails%>).position();
                
                        window.scrollTo(x.left,x.top);
                    }
                    if(title=="OPENAGRIS")
                        div.id='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                }
                            
            }
        }
                
        function activeOpenAgrisAfterMoreResource(){
            //******** tab OpenAgris attivo dopo MoreResource************    
            if(document.getElementById("idMoreResourceOpenAgris").value=="OK"){
                //alert("OPENAGRIS attivo");
                document.getElementById("idMoreResourceOpenAgris").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS"){
                        div.id='tabactive';
                        var pageOpenAgris=document.getElementById("numberOfPageOpenAgris").value;
                        var counterOpenAgris=(pageOpenAgris*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                        // var counterOpenAgris=(pageOpenAgris*20)-21; 
                        //alert ("COUNTER OpenAgris--->"+counterOpenAgris);
                        var positionDiv=$("#tabactive").position().top;
                        var s=$("#countResourceOpenAgris_"+counterOpenAgris).position();
                        var topFinal=s.top-positionDiv;
                           
                        var left2= s.left;
                           
                        window.scrollTo(left2,(topFinal));
 
                    }
                }
                    
                   
            
            }
                
        }
                
        function activeOpenAgrisAfterMoreInfo(){
            //******** tab OpenAgris attivo dopo MoreInfo***********  
            if(document.getElementById("idMoreInfoOpenAgris").value=="OK") 
            {
                document.getElementById("idMoreInfoOpenAgris").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="OPENAGRIS"){
                        div.id ='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                                
                               
                        var x=$("#countResourceOpenAgris_"+<%=numResourceOpenAgrisFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                    }
                    if(title=="E-INFRA-KB")
                        div.id='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                }
                            
            }  
                     
        }
                
        function activeCulturaItaliaAfterMoreResource(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreResourceCulturaItalia").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreResourceCulturaItalia").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA"){
                        div.id='tabactive';
                        var pageCultura=document.getElementById("numberOfPageCulturaItalia").value;
                           
                        //var counterCultura=(pageCultura*20)-21; 
                        var counterCultura=(pageCultura*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                        var positionDiv=$("#tabactive").position().top;
  
                        // alert("CounterCultura--->>"+counterCultura);        
                        var x2=$("#countResourceCulturaItalia_"+counterCultura).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
        function activeCulturaItaliaAfterMoreInfo(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreInfoCulturaItalia").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreInfoCulturaItalia").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA"){
                        div.id='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                        var x=$("#countResourceCulturaItalia_"+<%=numResourceCulturaItaliaFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
                
                
        function activeEuropeanaAfterMoreResource(){  
                
                     
                   
            if(document.getElementById("idMoreResourceEuropeana").value=="OK"){
                // alert(" Europeana attivo");
                document.getElementById("idMoreResourceEuropeana").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA"){
                        div.id='tabactive';
                        //alert(" Europeana attivo1");
                        var page=document.getElementById("numberOfPageEuropeana").value;
                           
                        //var counterCultura=(pageCultura*20)-21; 
                        var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                        var positionDiv=$("#tabactive").position().top;
  
                        //alert("CounterEuropena--->>"+counter);        
                        var x2=$("#countResourceEuropeana_"+counter).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                   
        function activeEuropeanaAfterMoreInfo(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreInfoEuropeana").value=="OK"){
                //alert("Europeana attivo");
                document.getElementById("idMoreInfoEuropeana").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="EUROPEANA"){
                        div.id='tabactive';
                               
                        var positionDiv= $("#tabactive").position().top;
                                
                        var x=$("#countResourceEuropeana_"+<%=numResourceEuropeanaFromDetails%>).position();
                              
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
        function activeIsidoreAfterMoreResource(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreResourceIsidore").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreResourceIsidore").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="ISIDORE"){
                        div.id='tabactive';
                        var page=document.getElementById("numberOfPageIsidore").value;
                           
                        //var counterCultura=(pageCultura*20)-21; 
                        var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                        var positionDiv=$("#tabactive").position().top;
  
                               
                        var x2=$("#countResourceIsidore_"+counter).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                   
        function activeIsidoreAfterMoreInfo(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreInfoIsidore").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreInfoIsidore").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="ISIDORE"){
                        div.id='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                        var x=$("#countResourceIsidore_"+<%=numResourceIsidoreFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
                
        function activePubmedAfterMoreResource(){  
                
            //******** tab Pubmed  attivo************    
            if(document.getElementById("idMoreResourcePubmed").value=="OK"){
                         
                document.getElementById("idMoreResourcePubmed").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="PUBMED"){
                        div.id='tabactive';
                                
                              
                        var page=document.getElementById("numberOfPagePubmed").value;
                             
                        //var counterCultura=(pageCultura*20)-21; 
                        var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                               
                        var positionDiv=$("#tabactive").position().top;
  
                               
                        var x2=$("#countResourcePubmed_"+counter).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                   
        function activePubmedAfterMoreInfo(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreInfoPubmed").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreInfoPubmed").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="PUBMED"){
                        div.id='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                        var x=$("#countResourcePubmed_"+<%=numResourcePubmedFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
        function activeDBPediaAfterMoreResource(){  
                
            //******** tab Pubmed  attivo************    
            if(document.getElementById("idMoreResourceDBPedia").value=="OK"){
                         
                document.getElementById("idMoreResourceDBPedia").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA"){
                        div.id='tabactive';
                                
                              
                        var page=document.getElementById("numberOfPageDBPedia").value;
                             
                        //var counterCultura=(pageCultura*20)-21; 
                        var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                               
                        var positionDiv=$("#tabactive").position().top;
  
                               
                        var x2=$("#countResourceDBPedia_"+counter).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                   
        function activeDBPediaAfterMoreInfo(){  
              
            //******** tab DBPEDIA attivo************    
            if(document.getElementById("idMoreInfoDBPedia").value=="OK"){
                // alert("DBPEDIA attivo");
                document.getElementById("idMoreInfoDBPedia").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="ENGAGE")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA"){
                        div.id='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                        var x=$("#countResourceDBPedia_"+<%=numResourceDBPediaFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                
                
                    
                    
        function activeEngageAfterMoreResource(){  
                
            //******** tab Pubmed  attivo************    
            if(document.getElementById("idMoreResourceEngage").value=="OK"){
                //  alert("ENGAGE attivo");
                document.getElementById("idMoreResourceEngage").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE"){
                        div.id='tabactive';
                                
                              
                        var page=document.getElementById("numberOfPageEngage").value;
                             
                        //var counterCultura=(pageCultura*20)-21; 
                        var counter=(page*<%=Integer.parseInt(numberRecordsForPage)%>)-<%=limit%>;
                               
                        var positionDiv=$("#tabactive").position().top;
  
                               
                        var x2=$("#countResourceEngage_"+counter).position();
                                
                        var topFinal=x2.top-positionDiv;
                         
                           
                        window.scroll(x2.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        } 
                
        function activeEngageAfterMoreInfo(){  
                
            //******** tab Cultura ITalia attivo************    
            if(document.getElementById("idMoreInfoEngage").value=="OK"){
                // alert("CULTURA ITALIA attivo");
                document.getElementById("idMoreInfoEngage").value="NO";
                var listDiv=document.getElementsByTagName("div");
                for (var i=0;i<listDiv.length;i++)
                {
                    var div=listDiv[i];
                    var title=div.getAttribute("title");
                    if(title=="E-INFRA-KB")
                        div.id ='notabactive';
                    if(title=="OPENAGRIS")
                        div.id ='notabactive';
                    if(title=="EUROPEANA")
                        div.id ='notabactive';
                    if(title=="CULTURAITALIA")
                        div.id ='notabactive';
                    if(title=="ISIDORE")
                        div.id ='notabactive';
                    if(title=="PUBMED")
                        div.id ='notabactive';
                    if(title=="DBPEDIA")
                        div.id ='notabactive';
                    if(title=="ENGAGE"){
                        div.id='tabactive';
                        var positionDiv= $("#tabactive").position().top;
                        var x=$("#countResourceEngage_"+<%=numResourceEngageFromDetails%>).position();
                               
                        var topFinal=x.top-positionDiv;
                           
                                              
                        window.scrollTo(x.left,topFinal);
                            
                           
                           
                    }
                }

            }    
                
        }
                    
                    
                   
                   
                     
        function showDialog()
        {
        
            //alert("SHOW DIALOG");
            $("#dialog2").dialog('open');          
       
       
            $( "#dialog2" ).dialog({
                dialogClass: "no-close",
                width: 300, 
                height:150,
                                
                            
                resizable: false

            });
       
            
        } 
                    
                    
        function GoDetailsOpenAgris(x)
        {
            var resource=x.toString().split("counterResourceOpenAgris_")[1];
                        
            // alert("Open agris "+resource);
            document.getElementById('idResourceOpenAgris').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="OK";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                   
            document.getElementById('numResourceOpenAgris').value=document.getElementById('idNumResourceOpenAgris_'+resource).value;
            document.forms["searchDetailOpenAgris"].submit();
                            
        }
                        
        function GoDetailsCulturaItalia(x)
        {
            var resource=x.toString().split("counterResourceCulturaItalia_")[1];
                  
            document.getElementById('idResourceCulturaItalia').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="OK";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                    
            document.getElementById('numResourceCulturaItalia').value=document.getElementById('idNumResourceCulturaItalia_'+resource).value;
            //alert("NUM RSOURCE--->"+document.getElementById('numResourceCulturaItalia').value);
            document.forms["searchDetailCulturaItalia"].submit();
                            
        }
                
                
        function GoDetailsEuropeana(x)
        {
            var resource=x.toString().split("counterResourceEuropeana_")[1];
                    
            document.getElementById('idResourceEuropeana').value=resource;
                   
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="OK";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                   
            document.getElementById('numResourceEuropeana').value=document.getElementById('idNumResourceEuropeana_'+resource).value;
            document.forms["searchDetailEuropeana"].submit();
                            
        }
                
        function GoDetailsIsidore(x)
        {
            var resource=x.toString().split("counterResourceIsidore_")[1];
            document.getElementById('idResourceIsidore').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="OK";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                   
            document.getElementById('numResourceIsidore').value=document.getElementById('idNumResourceIsidore_'+resource).value;
            document.forms["searchDetailIsidore"].submit();
                            
        }
                    
        function GoDetailsPubmed(x)
        {
            var resource=x.toString().split("counterResourcePubmed_")[1];
            document.getElementById('idResourcePubmed').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="OK";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                   
                   
            document.getElementById('numResourcePubmed').value=document.getElementById('idNumResourcePubmed_'+resource).value;
            document.forms["searchDetailPubmed"].submit();
                            
        }
                
        function GoDetailsDBPedia(x)
        {
            var resource=x.toString().split("counterResourceDBPedia_")[1];
                        
                    
            document.getElementById('idResourceDBPedia').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="OK";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="NO";
                   
            document.getElementById('numResourceDBPedia').value=document.getElementById('idNumResourceDBPedia_'+resource).value;
            document.forms["searchDetailDBPedia"].submit();
                            
        }
                
        function GoDetailsEngage(x)
        {
            var resource=x.toString().split("counterResourceEngage_")[1];
                  
            document.getElementById('idResourceEngage').value=resource;
            document.getElementById('idMoreInfo').value="NO";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById("idMoreResourceEngage").value="NO";
            document.getElementById("idMoreInfoEngage").value="OK";
                   
                   
            document.getElementById('numResourceEngage').value=document.getElementById('idNumResourceEngage_'+resource).value;
            document.forms["searchDetailEngage"].submit();
                            
        }
                        
    
        function GoDetails(x,countResource)
        {
            cursor_wait();
            var resource=x.toString().split("counterResoure_")[1];
                    
            var myTitle=document.getElementById("titleResourceVirtuoso"+countResource+"--1").value;
                   
            document.getElementById('title_GS2').value=myTitle;
                    
            //alert("RESOURCE--->"+resource);
            document.getElementById('idResource').value=resource;
            document.getElementById('idMoreInfo').value="OK";
            document.getElementById("idMoreResourceCHAIN").value="NO";
            document.getElementById("idMoreInfoOpenAgris").value="NO";
            document.getElementById("idMoreResourceOpenAgris").value="NO";
            document.getElementById("idMoreResourceCulturaItalia").value="NO";
            document.getElementById("idMoreInfoCulturaItalia").value="NO";
            document.getElementById("idMoreResourceIsidore").value="NO";
            document.getElementById("idMoreInfoIsidore").value="NO";
            document.getElementById("idMoreResourceEuropeana").value="NO";
            document.getElementById("idMoreInfoEuropeana").value="NO";
            document.getElementById("idMoreResourcePubmed").value="NO";
            document.getElementById("idMoreInfoPubmed").value="NO";
            document.getElementById("idMoreResourceDBPedia").value="NO";
            document.getElementById("idMoreInfoDBPedia").value="NO";
            document.getElementById('numResource').value=document.getElementById('idNumResource_'+resource).value;                                    
            document.forms["searchDetail"].submit();
        }  
                
                
        function CheckCitationsImage(countId,j)
        {
                    
                    
            var myTitle=document.getElementById("titleResourceVirtuoso"+countId+"--"+j).value; 
                    
            //alert("OK---->"+myTitle); 
            document.getElementById('title_GS').value=myTitle;
            document.getElementById('chek_altmetric').value="NO";
            document.forms["checkCitations"].submit();
        }
                
        function CheckAltmetrics(countId,j,url,doi)
        {
                    
                    
            var myTitle=document.getElementById("titleResourceVirtuoso"+countId+"--"+j).value; 
                    
            // alert("OK---->"+url); 
            document.getElementById('title_GS').value=myTitle;
            // alert("OK--2-->");
            document.getElementById('chek_altmetric').value="SI";
                     
            document.getElementById('url_altmetric').value=url;
            document.getElementById('doi_altmetric').value=doi;
            // alert("OK--3-->");
            document.forms["checkCitations"].submit();
        }
                
               
                
                
        function CheckCitations(x)
        {
                    
            var countResource=x.toString().split("counterResoureGS_")[1];
                   
            var myTitle=document.getElementById("titleResourceVirtuoso"+countResource+"--1").value;
                   
            document.getElementById('title_GS').value=myTitle;
            document.getElementById('chek_altmetric').value="NO";
            // alert("OK---->"+myTitle);
            document.forms["checkCitations"].submit();
        }
                
        function CheckAltmetrics2(x)
        {
                    
                    
            var countResource=x.toString().split("counterResoureAlt_")[1];
                   
            var myTitle=document.getElementById("titleResourceVirtuoso"+countResource+"--1").value;
            var url=document.getElementById("url_altmetric_"+countResource).value;
            var doi=document.getElementById("doi_altmetric_"+countResource).value;
            // alert("DOI--->"+doi);
            document.getElementById('title_GS').value=myTitle;
            document.getElementById('chek_altmetric').value="SI";
            document.getElementById("url_altmetric").value=url;
            document.getElementById("doi_altmetric").value=doi;
                     
            document.forms["checkCitations"].submit();
        }
    
    
        function cursor_wait(){
            document.body.style.cursor = "wait";
        }

        
        function submitLanguage(id)
        {
            cursor_wait();
            var s=document.getElementById("idLanguage"+id).value;
            var e=document.getElementById("selLanguageId").value=s;
            document.getElementById("language").innerHTML=e;
           
            document.forms["form_choose_language"].submit();
            showDialog();
          
        }
        function submitAllLanguage(){
            document.body.style.cursor = "wait";
            
            document.forms["form_all_language"].submit();
      
            //  showDialog();
   
        } 
                
        function controlSearchWord(){
        
            var key=document.getElementById("id_search_word").value;
        
            if(key=="" || key.trim()=="" )
            {
            
                return false;
            }
            else{
             
                return true;   
            }
 
        }
                
    
        function submitSearch()
        {
            
            var control=controlSearchWord();
        
        
        if(control){
                    
            //Gestione Pagine
            document.getElementById("numberOfPage").value="";
            document.getElementById("numberOfPageOpenAgris").value="";
            document.getElementById("numberOfPageCulturaItalia").value="";
            document.getElementById("numberOfPageEuropeana").value="";
            document.getElementById("numberOfPageIsidore").value="";
            document.getElementById("numberOfPagePubmed").value="";
            document.getElementById("numberOfPageEngage").value="";
            document.getElementById("numberOfPageDBPedia").value="";
            document.body.style.cursor = "wait";
         
            // alert("PRIMA-->"+document.getElementById("numberOfPage").value);
         
                    
            // alert("DOPO-->"+document.getElementById("numberOfPage").value);
            document.forms["search_form"].submit();
      
            showDialog();
        }
        else
            alert("Insert a keyword in the input text!!!");
        }
                

    
        $("#id_search_word").bind('keypress', function(e)
        {   
            if(e.which == 13) 
            {
                submitSearch();
            }
        });     
                     
                     
                     
        function getExampleValue(id)
        {
          
            var s=document.getElementById("idExample"+id).value;
            var e=document.getElementById("id_search_word").value=s;
          
            $( "#dialogExamples" ).dialog( "close" );
        
       
            //submitSearch();
            
        } 
    
        /*$( "#examplebutton" ).click(function() {
    $( "#dialogExamples" ).dialog( "open" );
    showDialogExamples();
});*/
    
        function examplebutton() {
            $( "#dialogExamples" ).dialog( "open" );
            showDialogExamples();
        }
 
        function showDialogExamples()
        {      
            //document.getElementById("examplebutton").disabled;
            $("#dialogExamples").dialog({
                dialogClass: "no-close",
                resizable: false,
                modal: true,
                width: 'auto',
                height: 'auto',
                position:['center','center'],
                autoOpen: false,
                overlay: { backgroundColor: "#000", opacity: 0.5 },
                buttons: [ { text: "close", click: function() { $( this ).dialog( "close" ); } } ]
            });
        }            
        </script>                     




    </div> <!-- chiudo il divContainer -->



</body>


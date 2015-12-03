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

<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="it.infn.ct.SemanticQuery"%>
<%@page contentType="text/html; charset=utf-8" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.*"%>

<portlet:defineObjects />
<%    // Gets the current timestamp
    java.util.Date date = new java.util.Date();
%>

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

    <%


        PortletPreferences preferences = renderRequest.getPreferences();


        String OpenAgris = preferences.getValue("OpenAgris", "");
        String OpenAgrisEndPoint = preferences.getValue("OpenAgrisEndPoint", "");
        String Europeana = preferences.getValue("Europeana", "");
        String EuropeanaEndPoint = preferences.getValue("EuropeanaEndPoint", "");
        String CulturaItalia = preferences.getValue("CulturaItalia", "");
        String CulturaItaliaEndPoint = preferences.getValue("CulturaItaliaEndPoint", "");
        String Isidore = preferences.getValue("Isidore", "");
        String IsidoreEndPoint = preferences.getValue("IsidoreEndPoint", "");
        String Pubmed = preferences.getValue("Pubmed", "");
        String PubmedEndPoint = preferences.getValue("PubmedEndPoint", "");
        String Engage = preferences.getValue("Engage", "");
        String EngageEndPoint = preferences.getValue("EngageEndPoint", "");
        String NumberRecordsForPage = preferences.getValue("NumberRecordsForPage", "");
        String LodLive = preferences.getValue("LodLive", "");
        String LodLiveEndPoint = preferences.getValue("LodLiveEndPoint", "");
        String TimeOut = preferences.getValue("TimeOut", "");
        System.out.println("VALORE_OPENAGRIS_INPUT: " + OpenAgris);
        System.out.println("VALORE_OPENAGRISENDPOINT_INPUT: " + OpenAgrisEndPoint);
        System.out.println("VALORE_Europeana_INPUT: " + Europeana);
        System.out.println("VALORE_EuropeanaEndPoint_INPUT: " + EuropeanaEndPoint);
        System.out.println("VALORE_CulturaItalia_INPUT: " + CulturaItalia);
        System.out.println("VALORE_CulturaItaliaEndPoint_INPUT: " + CulturaItaliaEndPoint);
        System.out.println("VALORE_Isidore_INPUT: " + Isidore);
        System.out.println("VALORE_IsidoreEndPoint_INPUT: " + IsidoreEndPoint);
        System.out.println("VALORE_Pubmed_INPUT: " + Pubmed);
        System.out.println("VALORE_PubmedEndPoint_INPUT: " + PubmedEndPoint);
        System.out.println("VALORE_Engage_INPUT: " + Engage);
        System.out.println("VALORE_EngageEndPoint_INPUT: " + EngageEndPoint);
        System.out.println("VALORE_LodLive_INPUT: " + LodLive);
        System.out.println("VALORE_LodLiveEndPoint_INPUT: " + LodLiveEndPoint);
        System.out.println("VALORE_NumberRecordsForPage_INPUT: " + NumberRecordsForPage);
        System.out.println("VALORE_TimeOut: " + TimeOut);
    %>


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
                    <!--<ul>
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
                    </ul>-->

                <ol type="1">
                    <li>The e-Infrastructure Knowledge Base (KB) containing more than 30 million resources belonging to thousands of 
                        semantically enriched <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/oadr-map/">Open Access Document Repositories</a> 
                        and <a href="http://www.sci-gaia.eu/e-infrastructures/knowledge-base/data-repositories-map/">Data Repositories</a>.
                        Search results are ranked according to the <a href="http://repositories.webometrics.info/">Ranking Web of Repositories</a>.
                    </li>
                    <li>
                        <a href="http://www.europeana.eu/portal/">Europeana</a>, 
                        <a href="http://dati.culturaitalia.it/?locale=it">Cultura Italia</a>,
                        <a href="http://www.rechercheisidore.fr/">Isidore</a>,
                        <a href="http://agris.fao.org/openagris/index.do">OpenAgris</a> and
                        <a href="http://pubmed.bio2rdf.org/">PubMed</a> (Note: Cultura Italia only allows 1-keyword searches).
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
                        <table  >
                            <tr>
                                <td align="center" style=" padding: 5px;" >
                                    <img href="http://klios.ct.infn.it" src="<%=renderRequest.getContextPath()%>/images/logo_klios.png" />
                            </td>


                            <!--    <td align="right" style="padding-right: 10px; width: 80px;">
                                    <ul id="nav" class="rounded">
                                        <li style="width: 80px;"><input id="examplebutton" class="rounded" type="button" value="Examples" style="text-align: right;font-size: 12px;" />
                                            <ul id="provaId" class="rounded" style="border: solid; border-color: grey;  background:white; text-align: left; padding-left: 5px;margin-left:10px; ">
                                                <font  align="left" style="font-color:grey;font-size: 12px;"> SEARCH EXAMPLES</font>
                                                <hr align="left" style="padding-left:0px;">
                                                <li  style="padding-top:10px;" ><input id="idExampleAuthor" type="text" hidden="true" name="example" value="author:Thomas_Pete"><a id="Author" align="left" value="author:Thomas_Pete" onclick="getExampleValue(this.id);" >author:Thomas_Pete</a></li>
                                                <li  style="padding-top:10px; "><input  id="idExampleSubject" type="text" hidden="true" name="example" value="subject:policy"><a id="Subject" align="left" value="subject:policy" onclick="getExampleValue(this.id);">subject:policy</a></li>
                                                <li  style="padding-top:10px; "><input  id="idExampleType" type="text" hidden="true" name="example" value="type:thesis"><a id="Type" align="left" value="type:thesis" onclick="getExampleValue(this.id);">type:thesis</a></li>
                                                <li  style="padding-top:10px; "><input id="idExampleFormat" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="Format" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li>
                                                <li  style="padding-top:10px; padding-bottom: 10px;"><input id="idExamplePublisher" type="text" hidden="true" name="example" value="publisher:elsevier"><a id="Publisher" align="left" value="publisher:elsevier" onclick="getExampleValue(this.id);">publisher:elsevier</a></li>  

                                            </ul>
                                        </li>




                                    </ul>
                                </td>-->
                            <!--td>
                                <input id="examplebutton" class="rounded" type="button" value="Examples" style="text-align: right;font-size: 12px;"  />                                    
                            </td-->

                            <td align="center" style=" padding: 10px;">
                                <input class="rounded" 
                                       id="id_search_word"  
                                       name="search_word" 
                                       type="text" 
                                       style="width:600px;height: 20px;font-size: 12px"/>
                            </td>                                                                

                            <td align="center" style=" padding: 10px;">





                                <input hidden="true" id="id_graph"  name="graph" type="text" value="http://CHAIN-Reds_Test"/>
                                <input id="buttonSearchImg "  type="button" onclick="submitSearch()" style="text-align: right;font-size: 12px;" value="Search"/>
                            </td>
                        </tr>
                        <!--tr>
                            <td colspan="4" align="center" style=" padding-top:0px; padding-bottom: 14px">Enter a keyword </td>
                        </tr-->



                    </table>

                </form>
            </div>


        </center>  

        <div id="dialogExamples" title="Examples" hidden="true">

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
                        <input id="idExampleFormat" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="Format" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li> 
                    </td>

                    <td>
                        <input id="idExamplePublisherA" type="text" hidden="true" name="example" value="publisher:مجلة جامعة الملك سعود"><a id="PublisherA" align="left" value="publisher:مجلة جامعة الملك سعود" onclick="getExampleValue(this.id);">publisher:مجلة جامعة الملك سعود</a></li>  <!--King Saud University Journal-->  
                        <!--  <input id="idExampleFormatA" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="FormatA" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li> -->
                    </td>

                    <td>
                        <input id="idExamplePublisherC" type="text" hidden="true" name="example" value="publisher:信州大学人文学部"><a id="PublisherC" align="left" value="publisher:信州大学人文学部" onclick="getExampleValue(this.id);">publisher:信州大学人文学部</a></li>  <!--Facoltà di lettere e filosofia-->
                        <!-- <input id="idExampleFormatC" type="text" hidden="true" name="example" value="format:image/jpeg"><a id="FormatC" align="left" value="format:image/jpeg" onclick="getExampleValue(this.id);">format:image/jpeg</a></li> -->
                    </td>
                    <td>
                        <input id="idExampleFormatR" type="text" hidden="true" name="example" value="format:электронная копия"><a id="FormatR" align="left" value="format:электронная копия" onclick="getExampleValue(this.id);">format:электронная копия</a></li> <!--Copia elettronica-->
                    </td>


                </tr>
                <tr>
                    <td id="firstColumn">
                        <input id="idExamplePublisher" type="text" hidden="true" name="example" value="publisher:elsevier"><a id="Publisher" align="left" value="publisher:elsevier" onclick="getExampleValue(this.id);">publisher:elsevier</a></li>  
                    </td>
                    <td>
                        <!-- <input id="idExamplePublisherA" type="text" hidden="true" name="example" value="publisher:مجلة جامعة الملك سعود"><a id="PublisherA" align="left" value="publisher:مجلة جامعة الملك سعود" onclick="getExampleValue(this.id);">publisher:مجلة جامعة الملك سعود</a></li>  <!--King Saud University Journal-->
                    </td>
                    <td>
                        <!--  <input id="idExamplePublisherC" type="text" hidden="true" name="example" value="publisher:信州大学人文学部"><a id="PublisherC" align="left" value="publisher:信州大学人文学部" onclick="getExampleValue(this.id);">publisher:信州大学人文学部</a></li>  <!--Facoltà di lettere e filosofia-->
                    </td>
                    <td>
                        <input id="idExamplePublisherR" type="text" hidden="true" name="example" value="publisher:Тбилиси"><a id="PublisherR" align="left" value="publisher:Тбилиси" onclick="getExampleValue(this.id);">publisher:Тбилиси</a></li>  <!--Tbilisi-->
                    </td>

                </tr>
            </table>

        </div>






        <div id="dialog2" title="Information" hidden="true" >
            <center>
                <p>Searching...</p>
                <p>This may take some time</p>
                <p>(up to <%=TimeOut%> min.)</p>
            </center>
        </div> 

        <script language="javascript">
    
       
            function cursor_wait(){
                document.body.style.cursor = "wait";
            }               
           
            $( "#dialog2" ).dialog({ autoOpen: false });
                
            $( "#dialogExamples" ).dialog({ autoOpen: false });
                 
       
       
            $("#id_search_word").bind('keypress', function(e)
            {   
                if(e.which == 13) 
                {
                    submitSearch();
                }
            });
      
        </script>                     

    </div>

</body>                        
<script>

    function submitSearch()
    {
           
        document.body.style.cursor = "wait";
            
        document.forms["search_form"].submit();
      
        showDialog();
    }
    
    function showDialog()
    {
        
    
        $("#dialog2").dialog('open');          
       
       
        $( "#dialog2" ).dialog({
            dialogClass: "no-close",
            width: 300, 
            height:150,
                                
              
                               
            resizable: false

        });
                 
            
    }  
            
            
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
    
   
    // function closeDialogExample(){
    //    $("#dialogExamples").dialog({ 
    //        click: function() { $( this ).dialog( "close" ); }
    //    });
    //  
    // }
</script>  





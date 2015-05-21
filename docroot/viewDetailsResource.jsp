



<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="java.util.ArrayList"%>
<%@page import="it.infn.ct.SemanticQueryMoreInfo"%>
<%@page import="it.infn.ct.SemanticQuery"%>
<jsp:useBean id="idResource" class="java.lang.String" scope="request"/>



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
                SemanticQueryMoreInfo moreInfo = new SemanticQueryMoreInfo();
                //idResource = "http://openDocuments/resource/oai:eprints.bbk.ac.uk.oai2:244";
                
                 
                
                
                
                 String numResource = renderRequest.getParameter("numResource");
                 String searched_word = renderRequest.getParameter("search_word");
                 
                 
                 
                 
                
                System.out.println("L'idresource e "+idResource+" numResource--->"+numResource+" search_word: "+searched_word);
                
                
                String[] infoElem = (String[]) moreInfo.getInfoResource(idResource);
                String title = infoElem[1];
                
                String authors = infoElem[2];//.replace(","," ");
                String datestamp = infoElem[3];
                String description = infoElem[4];
                String publisher = infoElem[5];
                String identifiers = infoElem[6];
                String[] listIdentifier = identifiers.split("##");
                String identifierURL = "#";
                for (int k = 0; k < listIdentifier.length; k++) {
                    String id = listIdentifier[k];
                    if (id.length()>4 && id.substring(0, 4).equals("http")) {
                        identifierURL = id;
                    }
                }

                String sources = infoElem[7];
                String subject = infoElem[8];
                String language = infoElem[9];
                String date = infoElem[10];
                String contributor = infoElem[11];
                String coverage = infoElem[12];
                String right = infoElem[13];
                
                String DOI="NODOI";
                String DOIRelation="NODOI";
                
                String []info_GS=(String[])request.getParameterValues("info_GS");
                
                ArrayList accessRights=moreInfo.getAccessRightResource(idResource);
                ArrayList alternativeTitle=moreInfo.getAlternativeTitle(idResource);
                ArrayList audience=moreInfo.getAudience(idResource);
                ArrayList bCitation=moreInfo.getBibliographicCitation(idResource);
                ArrayList extent=moreInfo.getExtent(idResource);
                ArrayList medium=moreInfo.getMedium(idResource);
                
              //  System.out.println("AUTHOR="+authors+" Datastamp="+datestamp+" Description="+description+" Publisher= "+publisher+" Id= "+identifiers);
               // System.out.println("sources="+sources+" subject="+subject+" language="+language+" date="+date+" contributor="+contributor);
                //System.out.println("coverage="+coverage+" right="+right);

            %>

            <br>
            <%
              if(title.length()!=0){
                String[] listTitle = title.split("##");
                for (int j = 0; j < listTitle.length; j++) {
                    if (listTitle.length > 1) {
                        if (!identifierURL.equals("#")) {
            %>
            <a id="Title" href="<%=identifierURL%>" target="_blank" style="color:black"><h1>(<%=(j + 1)%>)<u><%=listTitle[j]%></u></h1></a> 
            <%}
              else 
               {%>
            <h1><u><%=listTitle[j]%></u></h1>
            <% } 
                    }
                    else {
         if (!listTitle[j].equals("")) {
                    if (!identifierURL.equals("#")) {
            %><a id="Title" href="<%=identifierURL%>" target="_blank" style="color:black"><h1><u><%=listTitle[j]%></u></h1></a> 
            <% } else {
            %><h1 id="Title"><u><%=listTitle[j]%></u></h1>
            <% }} }
                        }
                               }
                
              
                
                        
            %>

            <fieldset class="fieldsetInformations" >
                <legend class="legendFieldset" >General Information</legend>
                <p class="klios_p"><b>Authors: </b><%=authors%></p>
                
                <%
                    //ALTERNATIVE TITLE
              if(alternativeTitle.size()!=0){
                    
                    for (int j = 0; j < alternativeTitle.size(); j++) {
                       
                %><p class="klios_p"><b>Alternative Title : </b><%=alternativeTitle.get(j).toString()%></p>  
                <%
                        }
                                       }
                
                
                
                
                    //DESCRIPTION
                 if(description.length()!=0){
                    String[] listDescrition = description.split("##");
                    for (int j = 0; j < listDescrition.length; j++) {
                        if (listDescrition.length > 0 && !listDescrition[j].equals("")) {
                %><p class="klios_p"><b>Description : </b><%=listDescrition[j]%></p>  
                <%} 
                        }
                                       }
                
                    //PUBLISHER
                if(publisher.length()!=0){
                    String[] listPublisher = publisher.split("##");
                    for (int j = 0; j < listPublisher.length; j++) {
                        if (listPublisher.length > 0 && !listPublisher[j].equals("")) {
                %><p class="klios_p"><b>Publisher : </b><%=listPublisher[j]%></p>  
                <%} 
                        }
                                       }
                    //IDENTIFIER
                    
                 for (int j = 0; j < listIdentifier.length; j++) {
                        String identifierResource = listIdentifier[j];

                        if (listIdentifier.length > 0 && !identifierResource.equals("")) {
                            if (identifierResource.length()>4 && identifierResource.substring(0, 4).equals("http")) {
                %><p class="klios_p"><b>Identifier  : </b><a href="<%=identifierResource%>" target="_blank"><u><%=identifierResource%></u> </a></p> 
                <%} else {
                                
                                //CONTROLLO SE E' UN DOI:
                                
                  if (identifierResource.length()>3 && identifierResource.substring(0, 3).equals("doi")) {
                      DOI=identifierResource.substring(4);
                      //System.out.println("CODE DOI:"+DOI);
                  }
                
                
                
                %>
                <p class="klios_p"><b>Identifier  : </b><%=identifierResource%></p> 

                <%}
                            
                  
                            
                } 
                    }

                    //SOURCE
                if(sources.length()!=0){
                    String[] listSource = sources.split("##");
                    for (int j = 0; j < listSource.length; j++) {
                        if (listSource.length > 0 && !listSource[j].equals("")) {
                %><p class="klios_p"><b>Source  : </b><%=listSource[j]%></p>  
                <%} 
                        }
                                       }
                    //SUBJECT
                 if(subject.length()!=0){
                    String[] listSubject = subject.split("##");
                    for (int j = 0; j < listSubject.length; j++) {
                        if (listSubject.length > 0 && !listSubject[j].equals("")) {
                %><p class="klios_p"><b>Subject : </b><%=listSubject[j]%></p>  
                <%} 
                        }
                                       }
                
                    //LANGUAGE
                 if(language.length()!=0){
                    String[] listLanguage = language.split("##");
                    for (int j = 0; j < listLanguage.length; j++) {
                        if (listLanguage.length > 0 && !listLanguage[j].equals("")) {
                %><p class="klios_p"><b>Language  : </b><%=listLanguage[j]%></p>  
                <%} 
                        }
                                       }
                    
                        
                    //CONTRIBUTOR
                 if(contributor.length()!=0){
                    String[] listContributor = contributor.split("##");
                    for (int j = 0; j < listContributor.length; j++) {
                        if (listContributor.length > 0 && !listContributor[j].equals("")) {
                %><p class="klios_p"><b>Contributor : </b><%=listContributor[j]%></p>  
                <%} 
                        }
                                       }
                  //COVERAGE
                
                 if(coverage.length()!=0){

                    String[] listConverage = coverage.split("##");
                    for (int j = 0; j < listConverage.length; j++) {
                        if (listConverage.length > 0 && !listConverage[j].equals("")) {
                %><p class="klios_p"><b>Coverage : </b><%=listConverage[j]%></p>  
                <%} 
                        }
                                       }
                
                 //RIGHT
                 if(right.length()!=0){
                    
                    String[] listRight = right.split("##");
                    for (int j = 0; j < listRight.length; j++) {
                        if (listRight.length > 0 && !listRight[j].equals("")) {
                %><p class="klios_p"><b>Rights : </b><%=listRight[j]%></p>  
                <%} 
                        }
                                       }
                
                if(accessRights.size()!=0){
                    
                    for (int j = 0; j < accessRights.size(); j++) {
                       
                %><p class="klios_p"><b>Access Rights : </b><%=accessRights.get(j).toString()%></p>  
                <%
                        }
                                       }
                
                   //AUDIANCE
              if(audience.size()!=0){
                    
                    for (int j = 0; j < audience.size(); j++) {
                       
                %><p class="klios_p"><b>Audience : </b><%=audience.get(j).toString()%></p>  
                <%
                        }
                                       }
                
                //BCitation
              if(bCitation.size()!=0){
                    
                    for (int j = 0; j < bCitation.size(); j++) {
                       
                %><p class="klios_p"><b>Bibliographic Citation : </b><%=bCitation.get(j).toString()%></p>  
                <%
                        }
                                       }
                
                
               
                    
                %>

            </fieldset>
            
            <br>
            <a id="LinkedData" class="Link" href="http://www.chain-project.eu/LodLiveGraph/?<%=idResource%>" target="_blank">Linked Data </a>
           
            <br>    
            <br>
            <a id="GScholarLink" class="Link" href="#" onClick="showFieldSetGScholar(); return false;">Information from Google Scholar <img id="ImageAnimationGScholar" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetGScholar" style="display: none;">
                
                <%
                    if(info_GS[0].equals(" ") || info_GS[0].equals("No Information available") || info_GS[0].equals("None") || info_GS[0].equals("No Available Service"))
                    {%>
                <p><b>Resource location: </b><%=info_GS[0]%></p>
                <%}
                   else{%>
                <p><b>Resource location: </b><a href="<%=info_GS[0]%>" target="_blank"><%=info_GS[0]%></a></p>
                <%}%>

                <p><b>No. of versions: </b><%=info_GS[1]%></p>
                <%
                if(info_GS[2].equals(" ") || info_GS[2].equals("No Information available") || info_GS[2].equals("None") || info_GS[2].equals("No Available Service"))
                    {%>
                <p><b>List of versions: </b><%=info_GS[2]%></p>
                <%}
                   else{%>
                <p><b>List of versions: </b><a href="<%=info_GS[2]%>" target="_blank"><%=info_GS[2]%></a></p>
                <%}%>

                <p><b>No. of citations: </b><%=info_GS[3]%></p>

                <%
               if(info_GS[4].equals(" ") || info_GS[4].equals("No Information available") || info_GS[4].equals("None") || info_GS[4].equals("No Available Service"))
                   {%>
                <p><b>List of citations: </b><%=info_GS[4]%></p>
                <%}
                   else{%>
                <p><b>List of citations: </b><a href="<%=info_GS[4]%>" target="_blank"><%=info_GS[4]%></a></p>
                <%}%>              
                <p><b>Year: </b><%=info_GS[5]%></p>
            </fieldset>     
                
                
            <br> 
            <a id="DateLink" class="Link" href="#" onClick="showFieldSetDate(); return false;">Date Information <img id="ImageAnimationDate" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetDate" style="display: none;">
                <p class="klios_p"><b>DateStamp: </b><%=datestamp%></p>
                <%
                //DATE
                if(date.length()!=0){
                    String[] listDate = date.split("##");
                    for (int j = 0; j < listDate.length; j++) {
                        if (listDate.length > 0 && !listDate[j].equals("")) {
                %><p class="klios_p"><b>Date : </b><%=listDate[j]%></p>  
                <%} 
                        }
                                       }
                  %>      
            </fieldset>    
                
                
            <br> 
            <a id="DataSetLink" class="Link" href="#" onClick="showFieldSetDataSet(); return false;">Dataset Information <img id="ImageAnimationDataSet" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetDataSet" style="display: none;">
                <!-- <legend class="legendFieldset" >Dataset Information</legend> -->
                <%String[] dataSet = (String[]) moreInfo.getDataSetFromResource(idResource);
                    String identifierDataSet = dataSet[1];
                    
                    String typeDataSet = dataSet[2];
                    
                    String formatDataSet = dataSet[3];
                    
                    String relationDataSet = dataSet[4];
                    
                    
                    
                    
                    if(!DOI.equals("NODOI"))
                    {
                %><p class="klios_p"><b>Identifier DOI : </b><a href="http://dx.doi.org/<%=DOI%>" target="_blank"><u><%=DOI%></u></a></p> 
                                 <%}
                    
                                   //IDENTIFIER DATA SET
                                   String[] listIdentifierDataSet = identifierDataSet.split("##");
                     
                                   for (int j = 0; j < listIdentifierDataSet.length; j++) {
                                       if (listIdentifierDataSet.length > 0 && !listIdentifierDataSet[j].equals("")) {
                %><p class="klios_p"><b>Identifier  : </b><a href="<%=listIdentifierDataSet[j]%>" target="_blank"><u><%=listIdentifierDataSet[j]%></u></a></p>  
                <%} 
                    }

                    //TYPE DATA SET
                    
                    if(typeDataSet.length()!=0){
                        
                    String[] listTypeDataSet = typeDataSet.split("##");
                    for (int j = 0; j < listTypeDataSet.length; j++) {
                        if (listTypeDataSet.length > 0 && !listTypeDataSet[j].equals("")) {
                %><p class="klios_p"><b>Dataset Type : </b><%=listTypeDataSet[j]%></p>  
                <%} 
                        }
                                       }
                    //FORMAT DATA SET
                    if(formatDataSet.length()!=0){
                        
                    String[] listFormatDataSet = formatDataSet.split("##");
                    for (int j = 0; j < listFormatDataSet.length; j++) {
                        if (listFormatDataSet.length > 0 && !listFormatDataSet[j].equals("")) {
                %><p class="klios_p"><b>Format : </b><%=listFormatDataSet[j]%></p>  
                <%} 
                        }
                                       }
                    
                    //RELATION DATA SET
                    if(relationDataSet.length()!=0){
                       
                    String[] listRelationDataSet = relationDataSet.split("##");
                    for (int j = 0; j < listRelationDataSet.length; j++) {
                        if (listRelationDataSet.length > 0 && !listRelationDataSet[j].equals("")) {
                           if (listRelationDataSet[j].length()>4 && listRelationDataSet[j].substring(0, 4).equals("http")) {
                %><p class="klios_p"><b>Relation : </b><a href="<%=listRelationDataSet[j]%>" target="_blank"><u><%=listRelationDataSet[j]%></u></a></p>
                <%}
                   else {
                                
                                //CONTROLLO SE E' UN DOI:
                                
                  if (listRelationDataSet[j].length()>3 && listRelationDataSet[j].substring(0, 3).equals("doi")) {
                      DOIRelation=listRelationDataSet[j].substring(4);
                      //System.out.println("CODE DOI:"+DOIRelation);
                %><p class="klios_p"><b>Relation DOI : </b><a href="http://dx.doi.org/<%=DOIRelation%>" target="_blank"><u><%=DOIRelation%></u></a></p> <%
                  }
                  else
                    { %><p class="klios_p"><b>Relation : </b><u><%=listRelationDataSet[j]%></u></p>    

                <%} 
                   }                         
                }

   
                    }
               }
                                   
                      //Extent
              if(extent.size()!=0){
                    
                    for (int j = 0; j < extent.size(); j++) {
                       
                %><p class="klios_p"><b>Extent : </b><%=extent.get(j).toString()%></p>  
                <%
                        }
                                       }

                   //Medium
              if(medium.size()!=0){
                    
                    for (int j = 0; j < medium.size(); j++) {
                       
                %><p class="klios_p"><b>Medium : </b><%=medium.get(j).toString()%></p>  
                <%
                        }
                                       }                     


                %>
            </fieldset>  
            <br>
            <a id="RepositoryLink" class="Link" href="#" onClick="showFieldSetRepository(); return false;">Repository Information <img id="ImageAnimationRepository" class="ImageAnimation" src="<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" /></a>
            <br>
            <fieldset class="fieldsetInformations" id="IdFieldSetRepository" style="display: none;">
                <%
                   ArrayList arrayRepository=SemanticQueryMoreInfo.getRepository(idResource);
                 //  System.out.println("repository size "+arrayRepository.size());
                   if(arrayRepository.size()>0){
                   
                       // for(int d=0; d<arrayDescriptions.size(); d++){
                    
                    String nameRep=arrayRepository.get(0).toString();
                    String countryCodeRep=arrayRepository.get(1).toString();
                    String longitudeRep=arrayRepository.get(2).toString();
                    String latitudeRep=arrayRepository.get(3).toString();
                    String urlRep="";
                    String addressOAIPMHRep=arrayRepository.get(5).toString();
                    String addressRep=arrayRepository.get(6).toString();
                    String acronimRep="";
                   // System.out.println("ACRONIM "+arrayRepository.get(7).toString());
                    if(!(arrayRepository.get(7).toString()).equals(""))
                        acronimRep="( "+arrayRepository.get(7).toString()+" )";
                   
                    String domainRep=arrayRepository.get(8).toString();
                    String projectRep=arrayRepository.get(9).toString();
                    String organization=arrayRepository.get(10).toString();
                   
                    //if(arrayRepository.get(4).toString()!=""){
                    // urlRep=arrayRepository.get(4).toString();
                        if(arrayRepository.get(4).toString().length()>4 && arrayRepository.get(4).toString().substring(0,4).equals("http"))
                           urlRep=arrayRepository.get(4).toString();
                        else
                            urlRep="http://"+arrayRepository.get(4).toString();

                        //System.out.println("URL REP "+urlRep);

                %>
                <p class="klios_p"><b>Name: </b> <%=nameRep%> <%=acronimRep%>  </p> 
                <p class="klios_p"><b>URL : </b> <a href="<%= urlRep %>" target="_blank" title="<%= urlRep%>"><u> <%= urlRep %></u> </a></p>
                <p class="klios_p"><b>OAI-PMH : </b><a href="<%=addressOAIPMHRep%>" target="_blank"><u><%=addressOAIPMHRep%></u></a></p>
                <p class="klios_p"><b>Country Code : </b><%=countryCodeRep%></p>
                <p class="klios_p"><b>Address : </b><%=addressRep%></p>
                <p class="klios_p"><b>Longitude : </b><%=longitudeRep%></p>
                <p class="klios_p"><b>Latitude : </b><%=latitudeRep%></p>
                <p class="klios_p"><b>Domain : </b><%=domainRep%></p>
                <p class="klios_p"><b>Project : </b><%=projectRep%></p>
                <p class="klios_p"><b>Organization : </b><%=organization%></p>



                <%
                // }
                 }
                %>
            </fieldset>

            







            <br><br>   
        </div>



        <div>
            <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH_ALL_LANGUAGE"/></portlet:actionURL>" method="post">   
                <!-- <input type="button" value="<< Back" onclick="history.go( -1 );"/> -->
                <input type="submit" value="<< Back" />
                <input  hidden="true" name="moreInfo" id="idMoreInfo" value="OK" />
                <input  hidden="true" name="numResourceFromDetails" id="idnumResourceFromDetails" value="<%=numResource%>" />

                <input hidden="true"  id="id_search_word"  name="search_word" value="<%=searched_word%>" />








            </form>
        </div>


        <script type="text/javascript">
            
            var controlDataSet=true;
            var controlRepository=true;
            var controlGScholar=true;
            var controlDate=true;
            
            function showFieldSetDataSet(){
                $("#IdFieldSetDataSet").animate({"height": "toggle"});
                
                
                
                
                if( controlDataSet==true ) {
                    $("#ImageAnimationDataSet").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                   controlDataSet=false;
                }
                else {
                    $("#ImageAnimationDataSet").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                     controlDataSet=true;
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
            function showFieldSetGScholar(){
                $("#IdFieldSetGScholar").animate({"height": "toggle"});
                
                
                
                
                if( controlGScholar==true ) {
                    $("#ImageAnimationGScholar").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                   controlGScholar=false;
                }
                else {
                    $("#ImageAnimationGScholar").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                     controlGScholar=true;
                }
                
            }
            
          function showFieldSetDate(){
                $("#IdFieldSetDate").animate({"height": "toggle"});
                
                
                
                
                if( controlDate==true ) {
                    $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_214_resize_small.png" );
                    
                   controlDate=false;
                }
                else {
                    $("#ImageAnimationDate").attr("src","<%=renderRequest.getContextPath()%>/images/glyphicons_215_resize_full.png" );
                     controlDate=true;
                }
                
            }  
            
            
        </script>          


    </body>

</html>

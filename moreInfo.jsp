<%
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="it.infn.ct.SemanticQuery"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>


<portlet:defineObjects />
<%//
  // GATE 6.0.0 Submission Form
  //
  // The form has 3 input textareas respectively for:
  //    * Macro file
  //    * Material DB
  //    * ROOT Analysis C file
  // Beside each text area a upolad button takes as input the 
  // file name related to one of the above fields.
  // The forth submission buttons is related to the file 'phsp.root' file
  // A default phsp.root file will be used if no files will be uploaded by the user.
  // The ohter three buttons of the form are used for:
  //    o Demo:          Used to fill with demo values the text areas
  //    o SUBMIT:        Used to execute the job on the eInfrastructure
  //    o Reset values:  Used to reset input fields
  //
  
  // Gets the current timestamp
  java.util.Date date = new java.util.Date();
%>

<%
// Below the descriptive area of the GATE web form 
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="<%=renderRequest.getContextPath()%>/css/layout.css">
    </head>
    <body>
        <table>
            <tr>
                <td>
            
                    <div id="divContainer"> 
                            <center>
                            <div id="divSearchBar"> 
                                <form id="search_form" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_SEMANTIC_SEARCH"/></portlet:actionURL>" method="post">         
                                    <input id="id_search_word"  name="search_word" type="text" style="width:700px"/>
                                    <input hidden="true" id="id_graph"  name="graph" type="text" value="http://RepositoryOntology"/>
                                    <input type="submit" value="Search" onclick="cursor_wait();"/>
                                </form>
                            </div>
                            </center>

                   
                            <div id="divLeft" > 
                                    <div id="divTitleSubject"><h4>SUBJECT</h4></div>
                                    <table>   
                                        <%
                                        SemanticQuery sq=new SemanticQuery();
                                          ArrayList arraySubject =new ArrayList();
                                          arraySubject=sq.getSubject();
                                          int count=0;
                                           if(arraySubject.size()>0){
                                               for(int i=0; i<arraySubject.size(); i++){
                                               String element=arraySubject.get(i).toString();
                                               String nameSubject= element.split("#")[0].toString();
                                               String countSubject= element.split("#")[1].toString();
                                               
                                        %>
                                        <tr>     
                                            <td> 
                                                <form id="search_form<%=count%>" action="<portlet:actionURL portletMode="view"><portlet:param name="PortletStatus" value="ACTION_QUERY_FROM_SUBJECT"/></portlet:actionURL>" method="post">         
                                                <input hidden="true" id="nameSubjectId<%=count%>" name="nameSubject" value="<%=nameSubject%>"/><u id="<%=count%>" align="left"  style="cursor: pointer" onclick="actionSubmit(this.id);" ><%=nameSubject%></u> (<%=countSubject%>) 
                                                </form>
                                            </td>  
                                        </tr>     
                                        <%   count++;
                                               }
                    
                                           }
                                        %>   

                                    </table> 
                                </div>
                                <div id="divRigth">Wellcome page
                                   
                                
                                </div> 
                            

                     
                    </div>
                </td>
            </tr>
        </table>

    </body>
        <script language="javascript">
         function cursor_wait(){
            document.body.style.cursor = "wait";
        }    
            
            
        function actionSubmit(id){
            var x= document.getElementById("nameSubjectId"+id).value;
            alert("ciao "+x+"id "+id);
             document.forms["search_form"+id].submit();
             
        }
        
    </script>
</html>    

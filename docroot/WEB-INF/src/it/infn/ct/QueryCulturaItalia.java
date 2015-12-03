/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 *
 * @author grid
 */
public class QueryCulturaItalia {

    public static RepositoryConnection CulturaItaliaConnection;
    public static String urlResource = "url";
    public static boolean repositoryConnection, malformedQuery, queryEvaluation;

    static public RepositoryConnection ConnectionToCulturaItalia(String CulturaItaliaEndPoint) 
            throws RepositoryException 
    {
        //String endpointURL = "http://dati.culturaitalia.it/sparql";
        HTTPRepository myRepository = new HTTPRepository(CulturaItaliaEndPoint, "");
        myRepository.initialize();
        myRepository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.SPARQL);
        CulturaItaliaConnection = myRepository.getConnection();
        return CulturaItaliaConnection;
    }

    static public int testEndPoint(String CulturaItaliaEndPoint) 
            throws MalformedURLException 
    {
        // URL url = new URL("http://dati.culturaitalia.it/sparql/");
        URL url = new URL(CulturaItaliaEndPoint);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS CULTURA ITALIA URL--->" + statusCode);

        return statusCode;
    }

    public static ArrayList queryCulturaItaliaResource(
            String search_word, 
            String numPage, 
            int numberRecords, 
            String CulturaItaliaEndPoint) 
            throws MalformedURLException 
    {

        ArrayList arrayCulturaItaliaResource = new ArrayList();
        int statusCode = testEndPoint(CulturaItaliaEndPoint);

            if (statusCode != -1) 
            {
                try {
                    ArrayList arrayVirtuosoResourceDupl = new ArrayList();

                    ConnectionToCulturaItalia(CulturaItaliaEndPoint);
                    String word = "' " + search_word + " '";
                    String bif_word = " \"' " + search_word + " '\"";
                    int page = Integer.parseInt(numPage);

                    int numOffset = (page - 1) * numberRecords;
                    int numberFinal = numberRecords * page;

//                String queryString = ""
//                        + "PREFIX crm: <http://erlangen-crm.org/120111/>"
//                        + "select distinct ?resource  where{"
//                        + "?resource  crm:P1_is_identified_by ?id."
//                        + "filter regex(?id,'" + search_word + "','i')."
//                        + "} limit "+numberFinal;

                    String queryString = "";

                    if (search_word.contains(":")) 
                    {
                        String[] splitSword = search_word.split(":");
                        String field = splitSword[0];
                        
                        if (field.equals(SemanticQuery.search_filter.author.toString()) || 
                            field.equals(SemanticQuery.search_filter.format.toString()) || 
                            field.equals(SemanticQuery.search_filter.type.toString()) || 
                            field.equals(SemanticQuery.search_filter.publisher.toString()) || 
                            field.equals(SemanticQuery.search_filter.subject.toString())) 
                        {

                            SemanticQuery.search_filter wordFilter = 
                                    SemanticQuery.search_filter.valueOf(field);

                            System.out.println("wordFilter CULTURA ITALIA =>>>>>>" + wordFilter);

                            String s = splitSword[1];
                            String search = "'" + s + "'";
                            //String search_uppercase = "' " + s.toUpperCase() + " '";

                            switch (wordFilter) {
                            case author:
                                queryString = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                                        + "select distinct ?s where { "
                                        + "?s crm:P94i_was_created_by ?idCreator.\n"
                                        + "?idCreator crm:P14_carried_out_by ?actor.\n"
                                        + "?actor rdf:value ?name."
                                        + "FILTER regex((?name), '" + s + "','i')\n"
                                        + "}";
                                break;
                                
                            case subject:
                                 queryString="PREFIX crm: <http://erlangen-crm.org/120111/> "
                                        + " select distinct(?resource) where{\n"
                                        + " ?resource crm:P62_depicts ?s.\n"
                                        + " ?s crm:P3_has_note ?subject.\n"
                                        + " ?subject rdf:value ?valueOfsubject.\n"
                                        + " ?valueOfsubject bif:contains \""+s+"\".\n"
                                        + " } limit " + numberFinal;
                               break;
                                
                            case type:
                                queryString = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                            + "select distinct ?s where { "
                            + "?s rdf:type ?type.\n"
                            + "FILTER regex((?type), '" + s + "','i')\n"
                            + "} limit " + numberFinal;
                                break;
                                
                            case format:                         
                                break;
                                
                            case publisher:
                                queryString = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                            + "select distinct ?value where { "
                            + "?s crm:P108i_was_produced_by ?c.\n"
                            + "?c crm:P14_carried_out_by ?v.\n"
                            + "?v rdf:value ?value.\n"
                            + "FILTER regex((?type), '" + s + "','i')\n"
                            + "}";
                                break;

                            default:
                                break;
                            } //end-of-switch
                            } else {

                                queryString = ""
                            + "PREFIX crm: <http://erlangen-crm.org/120111/>"
                            + " select distinct ?resource  where{"
                            + " ?resource  crm:P1_is_identified_by ?id."
                            + " filter regex(?id,'" + search_word + "','i')."
                            + "} limit " + numberFinal;
                            }

                } else {

                   queryString = ""
                    + "PREFIX crm: <http://erlangen-crm.org/120111/>"
                    + " select distinct ?resource  where{"
                    + " ?resource  crm:P1_is_identified_by ?id."
                    + " filter regex(?id,'" + search_word + "','i')."
                    + "} limit " + numberFinal;
                }

                System.out.println("QUERY CULTURA ITALIA: " + queryString);

                TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                
                TupleQueryResult result = tupleQuery.evaluate();

                System.out.println("Cultura italia query has next? " + result.hasNext());
        
                if (result.hasNext()) {
                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        String resource = bindingSet.getValue("resource").stringValue();
                        arrayVirtuosoResourceDupl.add(resource);                        
                    }
                }

                arrayCulturaItaliaResource = getListNotDuplicate(arrayVirtuosoResourceDupl);
                
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
                arrayCulturaItaliaResource.add("Exception");
                System.out.println("CULTURA ITALIA QueryEvaluationException");
            } catch (MalformedQueryException ex) {
                Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
                arrayCulturaItaliaResource.add("Exception");
                System.out.println("CULTURA ITALIA MalformedQueryException");
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
                arrayCulturaItaliaResource.add("Exception");
                System.out.println("CULTURA ITALIA RepositoryException");
            } finally {
                try {
                    System.out.println("CULTURA ITALIA CLOSE");                    
                    CulturaItaliaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryCulturaItalia.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }    
        
    return arrayCulturaItaliaResource;
    
}

    public static ArrayList getListNotDuplicate(ArrayList listOriginal) {

        ArrayList listNuova = new ArrayList();

        if (listOriginal.size() > 1) {
            int k = 1;
            int j, i = 0;
            boolean duplicato;
            listNuova.add(listOriginal.get(0));

            for (i = 1; i < listOriginal.size(); i++) {

                duplicato = false;

                for (j = 0; j < i; j++) {

                    if (listOriginal.get(i).toString().toUpperCase().equals(listOriginal.get(j).toString().toUpperCase())) {

                        duplicato = true;
                    }

                }
                if (!duplicato) {

                    listNuova.add(listOriginal.get(i));

                }


            }


            return listNuova;
        } else {
            return listOriginal;
        }
    }

    public static String getTitle(String resource)
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList arrayOpenAgrisTitles = new ArrayList();
        String sTitles = "";
     
        try {
            
             ConnectionToCulturaItalia("http://dati.culturaitalia.it/sparql");
            String queryStringTitle = "";

            queryStringTitle = "PREFIX crm: <http://erlangen-crm.org/120111/>"
                    + " select distinct ?title where {"
                    + " <" + resource + "> crm:P1_is_identified_by  ?idtitle."
                    + " ?idtitle rdf:value ?title."
                    + " ?idtitle rdf:type ?type."
                    + " FILTER(?type!=<http://erlangen-crm.org/120111/E42_Identifier>)"
                    + "}";

            
      
            
            TupleQuery tupleQuery_title = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);
            
            TupleQueryResult result_title = tupleQuery_title.evaluate();

            String titles = "";

            if (result_title.hasNext()) 
            {
                while (result_title.hasNext()) 
                {
                    BindingSet bindingSet_title = result_title.next();
                    if (bindingSet_title.getValue("title") != null) 
                    {
                        Value title = bindingSet_title.getValue("title");
                        titles = title.stringValue();
                        String titleFinale = new String(title.stringValue().getBytes("iso-8859-1"), "utf-8");                        
                        arrayOpenAgrisTitles.add(titleFinale);
                    }
                }
            } else {
                sTitles = "---";
            }

            result_title.close();
            arrayOpenAgrisTitles = getListNotDuplicate(arrayOpenAgrisTitles);

            for (int i = 0; i < arrayOpenAgrisTitles.size(); i++) {
                sTitles = sTitles + "##" + arrayOpenAgrisTitles.get(i).toString();
            }
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }         
        
        return sTitles;
    }

    public static String getDescription(String resource)
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList arrayDescription = new ArrayList();
        String sDescription = "";
        try {
            
            String queryStringDescription = "";            
            queryStringDescription = "PREFIX crm: <http://erlangen-crm.org/120111/>\n"
                    + "select distinct ?desc where{\n"
                    + "<" + resource + "> crm:P45_consists_of ?iddesc.\n"
                    + "?iddesc rdf:value ?desc.\n"
                    + "}";

            TupleQuery tupleQuery_description = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescription);
            
            TupleQueryResult result_description = tupleQuery_description.evaluate();

            if (result_description.hasNext()) 
            {
                while (result_description.hasNext()) 
                {
                    BindingSet bindingSet_description = result_description.next();
                    if (bindingSet_description.getValue("desc") != null) 
                    {
                        Value desc = bindingSet_description.getValue("desc");
                        String descriptionFinale = new String(desc.stringValue().getBytes("iso-8859-1"), "utf-8");
                        arrayDescription.add(descriptionFinale);
                    }
                }
            } else {
                sDescription = "---";
            }
            
            result_description.close();
            arrayDescription = getListNotDuplicate(arrayDescription);

            for (int i = 0; i < arrayDescription.size(); i++) {
                sDescription = sDescription + "##" + arrayDescription.get(i).toString();
            }
            
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }        

        return sDescription;
    }

    public static String getType(String resource)
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sType = "";
        
        try {
            
            String queryStringType = "";

            queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select ?type where { "
                    + "<" + resource + "> rdf:type ?type.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("type") != null) 
                    {
                        Value type = bindingSet.getValue("type");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        String[] sTypes = typeFinale.substring(29).split("_");
           
                        for (int i = 1; i < sTypes.length; i++)
                            sType = sType + " " + sTypes[i];                        
                    }
                }
            } else {
                sType = "---";
            }
            
            result.close();        

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
        return sType;
    }

    public static String getAuthors(String resource) 
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList arrayAuthors = new ArrayList();

        String sAuthors = "";
        
        try {

            String queryStringAuthor = "";

            queryStringAuthor = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?name where { "
                    + "<" + resource + "> crm:P94i_was_created_by ?idCreator.\n"
                    + "?idCreator crm:P14_carried_out_by ?actor.\n"
                    + "?actor rdf:value ?name."
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthor);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("name") != null) 
                    {
                        Value value = bindingSet.getValue("name");
                        String authorFinale = new String(value.stringValue().getBytes("iso-8859-1"), "utf-8");
                        arrayAuthors.add(authorFinale);
                    }
                }
            } else {
                sAuthors = "---";
            }
            
            result.close();

            arrayAuthors = getListNotDuplicate(arrayAuthors);

            for (int i = 0; i < arrayAuthors.size(); i++) {
                sAuthors = sAuthors + "##" + arrayAuthors.get(i).toString();          
            }

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sAuthors;
    }

    public static String getIdentifiersUrl(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList arrayIdentifiers = new ArrayList();
        String idUrl = "";
        
        try {
            // ConnectionToCulturaItalia();

            String queryStringTitle = "";

            queryStringTitle = "PREFIX crm: <http://erlangen-crm.org/120111/>"
                    + " select distinct ?identifier where {"
                    + " <" + resource + "> crm:P1_is_identified_by  ?id."
                    + " ?id rdf:value ?identifier."
                    + " ?id rdf:type ?type."
                    + " FILTER(?type=<http://erlangen-crm.org/120111/E42_Identifier>)"
                    + "}";


            TupleQuery tupleQuery_title = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);
            
            TupleQueryResult result_title = tupleQuery_title.evaluate();

            String id = "";

            if (result_title.hasNext()) 
            {
                while (result_title.hasNext()) 
                {
                    BindingSet bindingSet_id = result_title.next();
                    if (bindingSet_id.getValue("identifier") != null) 
                    {
                        Value idd = bindingSet_id.getValue("identifier");
                        id = idd.stringValue();
                        if (id.length() > 4 && id.substring(0, 4).equals("http")) 
                        {
                            urlResource = id;
                            idUrl = id;
                        }
                    }                    
                }
            } else {
                idUrl = "---";
            }
            
            result_title.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idUrl;
    }

    public static ArrayList has_type(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sType = "";
        ArrayList listtypes = new ArrayList();
        
        try {

            String queryStringType = "";

            queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?type where { "
                    + "<" + resource + "> crm:P2_has_type ?types.\n"
                    + "?types rdf:value ?type.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("type") != null) 
                    {
                        Value type = bindingSet.getValue("type");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listtypes.add(typeFinale);
                    }
                }
            } else {
                sType = "---";
                listtypes.add(sType);
            }
            
            listtypes = getListNotDuplicate(listtypes);

            result.close();

     } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
     }
        
     return listtypes;
    }

    public static ArrayList is_referred_to_by(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {
        String sRef = "";
        ArrayList listRef = new ArrayList();
        
        try {

            String queryStringType = "";

            queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?ref where { "
                    + "<" + resource + "> crm:P67i_is_referred_to_by ?ref.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("ref") != null) 
                    {
                        Value type = bindingSet.getValue("ref");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listRef.add(typeFinale);
                    }
                }
            } else {
                sRef = "---";
                listRef.add(sRef);
            }
            
            listRef = getListNotDuplicate(listRef);

            result.close();

    } catch (QueryEvaluationException ex) {
        Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return listRef;
    }

    public static ArrayList has_representation(String resource)
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sRep = "";
        ArrayList listRep = new ArrayList();
       
        try {

            String queryStringType = "";

            queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?rep where { "
                    + "<" + resource + "> crm:P138i_has_representation ?rep.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("rep") != null) 
                    {
                        Value type = bindingSet.getValue("rep");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listRep.add(typeFinale);
                    }
                }
            } else {
                sRep = "---";
                listRep.add(sRep);
            }
            
            listRep = getListNotDuplicate(listRep);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listRep;
    }

    public static ArrayList is_subject_to(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {
        String sSub = "";
        ArrayList listSub = new ArrayList();
       
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?sub ?subs where { "
                    + "<" + resource + "> crm:P104_is_subject_to ?subs.\n"
                    + "?subs rdf:value ?sub.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("sub") != null) 
                    {
                        Value type = bindingSet.getValue("sub");
                        String urlSub = bindingSet.getValue("subs").stringValue();
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listSub.add(typeFinale + "##" + urlSub);
                    }
                }
            } else {
                sSub = "---";
                listSub.add(sSub);
            }
            
            listSub = getListNotDuplicate(listSub);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listSub;
    }

    public static ArrayList has_dimension(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sDim = "";
        ArrayList listDim = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?dim where { "
                    + "<" + resource + "> crm:P43_has_dimension ?dims.\n"
                    + "?dims rdf:value ?dim.\n"
                    + "}";

            //System.out.println("QUERY listDim CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("dim") != null) 
                    {
                        Value type = bindingSet.getValue("dim");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listDim.add(typeFinale);
                    }
                }
            } else {
                sDim = "---";
                listDim.add(sDim);
            }
            
            listDim = getListNotDuplicate(listDim);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return listDim;
    }

    public static ArrayList has_former_or_current_location(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sLoc = "";
        ArrayList listLoc = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?c ?loc where { "
                    + "<" + resource + "> crm:P53_has_former_or_current_location ?c.\n"
                    + "?c rdf:value ?loc.\n"
                    + "}";

            //System.out.println("QUERY listLoc CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("loc") != null) 
                    {
                        Value type = bindingSet.getValue("loc");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listLoc.add(typeFinale);                        
                    }
                }
            } else {
                sLoc = "---";
                listLoc.add(sLoc);
            }
            
            listLoc = getListNotDuplicate(listLoc);

            result.close();

      } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return listLoc;
    }

    public static ArrayList was_produced_by(String resource) 
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        String sProd = "";
        ArrayList listProd = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">  crm:P108i_was_produced_by ?c.\n"
                    + "?c crm:P14_carried_out_by ?v.\n"
                    + "?v rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listProd CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listProd.add(typeFinale);
                    }
                }
            } else {
                sProd = "---";
                listProd.add(sProd);
            }
            
            listProd = getListNotDuplicate(listProd);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listProd;
    }

    public static ArrayList depicts(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listDepicts = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P62_depicts ?c.\n"
                    + "?c crm:P3_has_note ?note.\n"
                    + "?note rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listDepicts CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listDepicts.add(typeFinale);
                    }
                }
            } else {

                listDepicts.add("---");
            }
            
            listDepicts = getListNotDuplicate(listDepicts);

            result.close();
            
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listDepicts;
    }

    public static ArrayList show_features_of(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listFeatures = new ArrayList();
        ArrayList listFinal = new ArrayList();
        ArrayList listFinal2 = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?valueid where { "
                    + "<" + resource + ">   crm:P130_shows_features_of ?c.\n"
                    + "?c rdf:value ?valueid.\n"
                    + "}";

            //System.out.println("QUERY listFeatures CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("valueid") != null) 
                    {
                        String resourceId = bindingSet.getValue("valueid").stringValue();
                        listFeatures.add(resourceId);
                    }
                }
            } else {

                listFeatures.add("---");
            }

            listFeatures = getListNotDuplicate(listFeatures);

            listFinal = getResourceFromId(listFeatures);

            for (int j = 0; j < listFinal.size(); j++) {
                String idResourceFeature = listFinal.get(j).toString();
                String url = getIdentifiersUrl(idResourceFeature);
                listFinal2.add(url);
            }

            result.close();
     
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listFinal2;
    }

    public static ArrayList getResourceFromId(ArrayList idResources)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listResource = new ArrayList();                
        
        for (int i = 0; i < idResources.size(); i++) 
        {
            try {

                String myId = idResources.get(i).toString();

                String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                        + " select distinct ?r where { "
                        + " ?r crm:P1_is_identified_by  ?id.\n"
                        + " ?id rdf:value ?valueid.\n"
                        + " filter (?valueid='" + myId + "')."
                        + "}";

                //System.out.println("QUERY listResourceFromId CulturaItalia-->" + queryStringType);

                TupleQuery tupleQuery = CulturaItaliaConnection
                        .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
                
                TupleQueryResult result = tupleQuery.evaluate();

                if (result.hasNext()) 
                {
                    while (result.hasNext()) 
                    {
                        BindingSet bindingSet = result.next();
                        if (bindingSet.getValue("r") != null) 
                        {
                            String resource2 = bindingSet.getValue("r").stringValue();
                            listResource.add(resource2);                            
                        }
                    }
                } else {

                    listResource.add("---");
                }

                result.close();
         
        } catch (QueryEvaluationException ex) {
                Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }        
        } //end-of-for
    
        return listResource;
    }

    public static ArrayList forms_part_of(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listResources = new ArrayList();
        ArrayList listFinal2 = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?r where { "
                    + "<" + resource + ">   crm:P46i_forms_part_of ?part.\n"
                    + "?r ?p ?part.\n"
                    + "}";

            //System.out.println("QUERY listResourcesPART CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("r") != null) 
                    {
                        String resourceId = bindingSet.getValue("r").stringValue();
                        listResources.add(resourceId);
                    }
                }
            } else {
                listResources.add("---");
            }

            listResources = getListNotDuplicate(listResources);

            for (int j = 0; j < listResources.size(); j++) {
                String idResource = listResources.get(j).toString();
                String url = getIdentifiersUrl(idResource);
                listFinal2.add(url);
            }

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listFinal2;
    }

    public static ArrayList is_about(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listAbout = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?c where { "
                    + "<" + resource + ">   crm:P129_is_about ?c.\n"
                    + "}";

            //System.out.println("QUERY listAbout CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("c") != null) 
                    {
                        Value type = bindingSet.getValue("c");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        if (typeFinale.contains("/")) 
                        {
                            String tt = typeFinale.split("/")[typeFinale.split("/").length - 1];
                            listAbout.add(tt);
                        } else {
                            listAbout.add(typeFinale);
                        }                        
                    }
                }
            } else {

                listAbout.add("---");
            }
            listAbout = getListNotDuplicate(listAbout);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return listAbout;
    }

    public static ArrayList has_note(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {
        ArrayList listNote = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P3_has_note ?note.\n"
                    + "?note rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listNote CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);                

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listNote.add(typeFinale);                        
                    }
                }
            } else {

                listNote.add("---");
            }
            
            listNote = getListNotDuplicate(listNote);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listNote;
    }

    public static ArrayList was_trasformed_by(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listTrasform = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P124i_was_transformed_by ?o.\n"
                    + "?o crm:P14_carried_out_by ?valueId.\n"
                    + "?valueId rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listTrasform CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);                    

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listTrasform.add(typeFinale);
                    }
                }
            } else {
                listTrasform.add("---");
            }
            
            listTrasform = getListNotDuplicate(listTrasform);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listTrasform;
    }

    public static ArrayList was_present_at(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listAt = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P12i_was_present_at ?o.\n"
                    + "?o crm:P4_has_time_span ?time.\n"
                    + "?time rdf:value ?value.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);                   

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listAt.add(typeFinale);                        
                    }
                }
             } else {

                listAt.add("---");
            }
            
            listAt = getListNotDuplicate(listAt);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }       

        return listAt;
    }

    public static ArrayList has_language(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listLan = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P72_has_language ?o.\n"
                    + "?o rdf:value ?value.\n"
                    + "}";

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listLan.add(typeFinale);                        
                    }
                }
            } else {
                listLan.add("---");
            }
            
            listLan = getListNotDuplicate(listLan);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listLan;
    }

    public static ArrayList has_current_or_former_residence(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listLoc = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?c ?loc where { "
                    + "<" + resource + "> crm:P74_has_current_or_former_residence ?c.\n"
                    + "?c rdf:value ?loc.\n"
                    + "}";

            //System.out.println("QUERY listLoc CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);                      

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("loc") != null) 
                    {
                        Value type = bindingSet.getValue("loc");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listLoc.add(typeFinale);                        
                    }
                }
            } else {
                listLoc.add("---");
            }
            
            listLoc = getListNotDuplicate(listLoc);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listLoc;              
    }

    public static ArrayList possesses(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listPos = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P75_possesses ?c.\n"
                    + "?c rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listPos CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listPos.add(typeFinale);                        
                    }
                }
            } else {
                listPos.add("---");
            }
            
            listPos = getListNotDuplicate(listPos);

            result.close();
       
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listPos;
    }

    public static ArrayList is_composed_of(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listComposed = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P46_is_composed_of ?c.\n"
                    + "?c rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listComposed CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);                    

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listComposed.add(typeFinale);                        
                    }
                }
            } else {
                listComposed.add("---");
            }
            
            listComposed = getListNotDuplicate(listComposed);

            ArrayList listFinale = new ArrayList();

            for (int i = 0; i < listComposed.size(); i++) {
                String elem = listComposed.get(i).toString();
                if (elem.matches("(.*) / (.*)")) {

                    String[] arrayId = elem.split("/");
                    ArrayList listId = new ArrayList();
                    for (int j = 0; j < arrayId.length; j++) {
                        String elemId = arrayId[j].trim();
                        listId.add(elemId);
                    }

                    ArrayList listResource = getResourceFromId(listId);
                    for (int k = 0; k < listResource.size(); k++) {
                        String id = listResource.get(k).toString();
                        String url = getIdentifiersUrl(id);
                        listFinale.add(url);
                    }
                    listComposed = listFinale;
                }
            }

            result.close();
        
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        return listComposed;
    }

    public static ArrayList took_place_at(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listPlace = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P7_took_place_at ?o.\n"
                    + "?o rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listPlace CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listPlace.add(typeFinale);
                    }
                }
            } else {
                listPlace.add("---");
            }
            
            listPlace = getListNotDuplicate(listPlace);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listPlace;
    }

    public static ArrayList refers_to(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

    ArrayList listRef = new ArrayList();
    
    try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P67_refers_to ?o.\n"
                    + "?o rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listRef CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listRef.add(typeFinale);                          
                    }
                }
            } else {
                listRef.add("---");
            }
            
            listRef = getListNotDuplicate(listRef);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listRef;
    }

    public static ArrayList is_composed_of2(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listComposed = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P106_is_composed_of ?c.\n"
                    + "?c rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listComposed CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listComposed.add(typeFinale);                        
                    }
                }
            } else {
                listComposed.add("---");
            }
            
            listComposed = getListNotDuplicate(listComposed);
            
            ArrayList listFinale = new ArrayList();

            for (int i = 0; i < listComposed.size(); i++) {
                String elem = listComposed.get(i).toString();
                if (elem.matches("(.*) / (.*)")) {

                    String[] arrayId = elem.split("/");
                    ArrayList listId = new ArrayList();
                    for (int j = 0; j < arrayId.length; j++) {
                        String elemId = arrayId[j].trim();
                        listId.add(elemId);
                    }

                    ArrayList listResource = getResourceFromId(listId);
                    for (int k = 0; k < listResource.size(); k++) {
                        String id = listResource.get(k).toString();
                        String url = getIdentifiersUrl(id);
                        listFinale.add(url);
                    }
                    listComposed = listFinale;
                }
            }

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }        
    
        return listComposed;
    }

    public static ArrayList was_modified_by(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listMod = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P31i_was_modified_by ?o.\n"
                    + "?o crm:P14_carried_out_by ?valueId.\n"
                    + "?valueId rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listTrasform CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);
            
            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listMod.add(typeFinale);
                    }
                }
            } else {
                listMod.add("---");
            }
            
            listMod = getListNotDuplicate(listMod);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listMod;
    }

    public static ArrayList witnessed(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listWit = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P8i_witnessed  ?w.\n"
                    + "?w crm:P4_has_time_span ?t.\n"
                    + "?t crm:P81_ongoing_throughout ?v.\n"
                    + "?v rdf:value ?value."
                    + "}";

            //System.out.println("QUERY listWit CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listWit.add(typeFinale);                        
                    }
                }
            } else {
                listWit.add("---");
            }
            
            listWit = getListNotDuplicate(listWit);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listWit;
    }

    public static ArrayList forms_part_of2(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listPart = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P106i_forms_part_of ?part.\n"
                    + "?part rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listNote CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listPart.add(typeFinale);
                    }
                }
            } else {
                listPart.add("---");
            }
            
            listPart = getListNotDuplicate(listPart);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listPart;
    }

    public static ArrayList performed(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listPerf = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + ">   crm:P14i_performed  ?p.\n"
                    + "?p crm:P12_occurred_in_the_presence_of ?t.\n"
                    + "?t rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listNote CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listPerf.add(typeFinale);                            
                    }
                }
            } else {
                listPerf.add("---");
            }
            
            listPerf = getListNotDuplicate(listPerf);

            result.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listPerf;
    }

    public static ArrayList is_current_or_former_member(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listMember = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?c ?loc where { "
                    + "<" + resource + "> crm:P107i_is_current_or_former_member_of ?c.\n"
                    + "?c rdf:value ?loc.\n"
                    + "}";

            //System.out.println("QUERY listmemeber CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("loc") != null) 
                    {
                        Value type = bindingSet.getValue("loc");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listMember.add(typeFinale);
                    }
                }
            } else {
                listMember.add("---");
            }
            
            listMember = getListNotDuplicate(listMember);

            result.close();
            
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listMember;
    }

    public static ArrayList was_based_on(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {

        ArrayList listBased = new ArrayList();

        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P136_was_based_on ?c.\n"
                    + "?c rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listmemeber CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listBased.add(typeFinale);
                    }
                }
            } else {
                listBased.add("---");
            }
            
            listBased = getListNotDuplicate(listBased);

            result.close();
            
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listBased;
    }

    public static ArrayList includes(String resource)  
            throws RepositoryException, MalformedQueryException, UnsupportedEncodingException 
    {
        ArrayList listIn = new ArrayList();
        
        try {

            String queryStringType = "PREFIX crm: <http://erlangen-crm.org/120111/> "
                    + "select distinct ?value where { "
                    + "<" + resource + "> crm:P117i_includes ?c.\n"
                    + "?c crm:P14_carried_out_by ?v.\n"
                    + "?v rdf:value ?value.\n"
                    + "}";

            //System.out.println("QUERY listmemeber CulturaItalia-->" + queryStringType);

            TupleQuery tupleQuery = CulturaItaliaConnection
                    .prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);            

            TupleQueryResult result = tupleQuery.evaluate();

            if (result.hasNext()) 
            {
                while (result.hasNext()) 
                {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("value") != null) 
                    {
                        Value type = bindingSet.getValue("value");
                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        listIn.add(typeFinale);                        
                    }
                }
            } else {
                listIn.add("---");
            }
            
            listIn = getListNotDuplicate(listIn);

            result.close();
            
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listIn;
    }
}

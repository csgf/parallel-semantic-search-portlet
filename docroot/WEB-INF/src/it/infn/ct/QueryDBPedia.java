/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 *
 * @author grid
 */
public class QueryDBPedia {
    
    public static RepositoryConnection DBPediaConnection;
    
    static public RepositoryConnection ConnectionToDBPedia(String endpointURL) 
            throws RepositoryException 
    {
        // String endpointURL = "http://europeana.ontotext.com/sparql";
        HTTPRepository myRepository = new HTTPRepository(endpointURL);
        myRepository.initialize();
       // myRepository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.SPARQL);
        myRepository.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.JSON);
        DBPediaConnection = myRepository.getConnection();
        return DBPediaConnection;
    }
    
     static public int testEndPoint(String EndPointDBPedia) 
            throws MalformedURLException, IOException 
    {

        System.out.println("DBPedia ENDPOINT" + EndPointDBPedia);

        URL url = new URL(EndPointDBPedia);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS DBPEDIA URL--->" + statusCode);

        return statusCode;
    }
     
     
      public static String getTitle(String resource) {
       String title="";
       
        try {
            

            String queryString = 
                    "SELECT distinct ?label WHERE {\n"
                    + "<"+resource+"> rdfs:label ?label "                
                    + "filter( lang(?label) = 'en' ).\n"
                    + "}";
            System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
             if (result.hasNext()) {
            while (result.hasNext()) {
                
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("label");
                System.out.println("TITLE: "+label.stringValue());
                if(i==0){
                    title=label.stringValue();
                    i++;
                }
                else
                    title=title+"##"+label.stringValue();
            }
             }
             else
                title="---";
             
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return title;
    }
      
      public static String getDescription(String resource) {
       
       String description="";
        try {
            

            String queryString = "prefix db2:<http://dbpedia.org/ontology/>\n"
                    +"SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> db2:abstract ?o. \n"                
                    + "filter( lang(?o) = 'en' ).\n"
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                System.out.println("DESC: "+label.stringValue());
                if(i==0){
                    description=label.stringValue();
                    i++;
                }else
                    description=description+"##"+label.stringValue(); 
                
            }
            }else
                description="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return description;
    }
      
      
      public static String getRDFType(String resource) {
       
       String type="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> rdf:type ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    type=label.stringValue();
                    i++;
                }else
                    type=type+"##"+label.stringValue(); 
                
            }
            }else
                type="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return type;
    }
      
      public static String getRDFLabel(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> rdfs:label ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }
      
       public static String getRDFComment(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> rdfs:comment ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }
       
     public static String getWasDerivedFrom(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> <http://www.w3.org/ns/prov#wasDerivedFrom> ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }  
     
     public static String getWikiPageExternalLink(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> <http://dbpedia.org/ontology/wikiPageExternalLink> ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }  
     
     public static String getSameAs(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?o WHERE {\n"
                    + "<"+resource+"> <http://www.w3.org/2002/07/owl#sameAs> ?o. \n"                
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("o");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }  
     
     
     public static String getSubject(String resource) {
       
       String out="";
        try {
            

            String queryString = 
                    "SELECT distinct ?label WHERE {\n"
                    + "<"+resource+"> <http://purl.org/dc/terms/subject> ?o. \n" 
                    +"?o rdfs:label ?label.\n"
                    +"filter( lang(?label) = 'en' )."
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            int i=0;
            if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value label = bindingSet.getValue("label");
                
                
                
                if(i==0){
                    out=label.stringValue();
                    i++;
                }else
                    out=out+"##"+label.stringValue(); 
                
            }
            }else
                out="---";
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }  
     
     public static ArrayList getAll(String resource) {
       
       ArrayList out=new ArrayList();
        try {
            

            String queryString = 
                    "SELECT * WHERE {\n"
                    + "<"+resource+"> ?p ?o. \n" 
                    +"filter (!regex(str(?p),'http://purl.org/dc/terms/subject') )\n"
                    +"filter (!regex(str(?p),'http://dbpedia.org/ontology/abstract') )\n"
                    +"filter (!regex(str(?p),'http://www.w3.org/ns/prov#wasDerivedFrom') )\n"
                    +"filter (!regex(str(?p),'http://www.w3.org/1999/02/22-rdf-syntax-ns#type') )\n"
                    +"filter (!regex(str(?p),'http://www.w3.org/2002/07/owl#sameAs') )\n"
                    +"filter (!regex(str(?p),'http://www.w3.org/2000/01/rdf-schema#label') )\n"
                    +"filter (!regex(str(?p),'http://www.w3.org/2000/01/rdf-schema#comment') )\n"
                    +"filter (!regex(str(?p),'http://dbpedia.org/ontology/wikiPageExternalLink') )\n"
                    + "}";
            //System.out.println(queryString);
            TupleQuery tupleQuery = DBPediaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
           
            TupleQueryResult result = tupleQuery.evaluate();
            
            if (result.hasNext()) {
            while (result.hasNext()) {
                
                String []elem=new String[2];
                BindingSet bindingSet = result.next();
                Value pred = bindingSet.getValue("p");
                Value obj = bindingSet.getValue("o");
                elem[0]=pred.stringValue();
                elem[1]=obj.stringValue();
                
                out.add(elem);
            }
            }
               
            result.close();


        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (MalformedQueryException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            ex.toString();
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                DBPediaConnection.close();
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryDBPedia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }  
     
     
     
     
       
       
      
     
     
    
}

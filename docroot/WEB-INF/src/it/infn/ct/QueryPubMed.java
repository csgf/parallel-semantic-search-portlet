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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 *
 * @author ccarrubba
 */
public class QueryPubMed {
    public static RepositoryConnection PubMedConnection;
 //public static ArrayList arrayIsidoreResource;
 
 public static ArrayList arrayPubmedResource;
 
 

    static public RepositoryConnection ConnectionToPubMed(String EndPointPubmed) throws RepositoryException {
        //String endpointURL = "http://pubmed.bio2rdf.org/sparql";
                              


       
       
        Repository myRepository = new HTTPRepository(EndPointPubmed,"");
        
        myRepository.initialize();
        
        PubMedConnection = myRepository.getConnection();
        
        return PubMedConnection;
    
    }
     static public int testEndPoint(String EndPointPubmed) throws MalformedURLException, IOException {
        //URL url = new URL("http://pubmed.bio2rdf.org/sparql");
        URL url = new URL(EndPointPubmed);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryPubMed.class.getName()).log(Level.SEVERE, null, ex);
           // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryPubMed.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS  ISIDORE URL--->" + statusCode);

        return statusCode;
    }
    public static ArrayList queryPubmedResource(String search_word, String numPage, int numberRecords,String EndPointPubmed) throws MalformedURLException, IOException {
        ArrayList arrayPubMedResourceDupl = new ArrayList();
         arrayPubmedResource = new ArrayList();
        int statusCode=testEndPoint(EndPointPubmed);
        if(statusCode!= -1){
            try {
                ConnectionToPubMed(EndPointPubmed);

                String word = "' " + search_word + " '";
               
                int page = Integer.parseInt(numPage);


                int numOffset = (page - 1) * numberRecords;
                int numberFinal=numberRecords*page;
               


                String queryString = ""
                      +"PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                      +"select ?s\n"
                      +"where{\n"
                      +"?s dc-term:title ?t.\n"
                      +"FILTER regex(?t,'"+search_word+"')\n"
                      +"}limit " +numberFinal;

 

                TupleQuery tupleQuery = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



                TupleQueryResult result = tupleQuery.evaluate();




                if (result.hasNext()) {
                    while (result.hasNext()) {


                        BindingSet bindingSet = result.next();
                        String resource = bindingSet.getValue("s").stringValue();

                        
                        arrayPubMedResourceDupl.add(resource);
                    }

                }



                arrayPubmedResource = getListNotDuplicate(arrayPubMedResourceDupl);
           } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryPubMed.class.getName()).log(Level.SEVERE, null, ex);
                 
                arrayPubmedResource.add("Exception");
                
               System.out.println("QueryPubMed QueryEvaluationException");
               
            } catch (MalformedQueryException ex) {
                Logger.getLogger(QueryPubMed.class.getName()).log(Level.SEVERE, null, ex);
                arrayPubmedResource.add("Exception");
                System.out.println(" QueryPubMed MalformedQueryException");
                
            } catch (RepositoryException ex) {
                Logger.getLogger(QueryPubMed.class.getName()).log(Level.SEVERE, null, ex);
                arrayPubmedResource.add("Exception");
                System.out.println(" QueryPubMed RepositoryException");
            }
       

        }
       
        
        return arrayPubmedResource;
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

                    if (listOriginal.get(i).equals(listOriginal.get(j))) {
                        
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
     
     
   
    public static String  getPubmedTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayPubMedTitleDupl = new ArrayList();
        ArrayList arrayPubMedTitles = new ArrayList();
        String sTitles = "";
        try {
           
            String queryStringTitle="";
                      queryStringTitle ="PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                    + "SELECT  ?title \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc-term:title ?title.\n"
                    + "}";
            
            
                      
            

            TupleQuery tupleQuery_title = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();




            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();

                        
                        String titleFinale = new String(title.stringValue().getBytes("iso-8859-1"), "utf-8");
                       
                       arrayPubMedTitleDupl.add(titleFinale);
                        

                    }

                }
                 arrayPubMedTitles = getListNotDuplicate(arrayPubMedTitleDupl);
            }
            
            else
            {
                sTitles="---";
              
            }
            result_title.close();
            
            
            
            
            for (int i=0;i<arrayPubMedTitles.size();i++)
            {
             
                if( arrayPubMedTitles.get(i).toString()!=""){
                      sTitles=sTitles+"##"+arrayPubMedTitles.get(i).toString();
                }
                
            }
         
         
            

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTitles;
    }
    
    
    public static String getPubmedAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
     
        ArrayList arrayPubMedAuthorsDupl = new ArrayList();
        
        ArrayList arrayPubMedAuthors = new ArrayList();
        String sAuthors="";
        try {
           


           
            
            String queryStringAuthors ="\n"
                    +"select ?author ?initials ?last_name\n"
                    +"where{\n"
                    +"<"+resource+"> <http://bio2rdf.org/pubmed_vocabulary:author>  ?author.\n"
                    +"?author <http://bio2rdf.org/pubmed_vocabulary:initials> ?initials.\n"
                    +"?author <http://bio2rdf.org/pubmed_vocabulary:last_name> ?last_name.}\n";
                    
            
            
            
            

            TupleQuery tupleQuery_authors = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthors);



            TupleQueryResult result_authors = tupleQuery_authors.evaluate();


           

            String author_initials = "";
            String author_lastName="";
            if (result_authors.hasNext()) {
                while (result_authors.hasNext()) {
                    BindingSet bindingSet_authors = result_authors.next();

                    if (bindingSet_authors.getValue("author") != null) {

                        Value intilias = bindingSet_authors.getValue("initials");
                        Value l_name = bindingSet_authors.getValue("last_name");

                        author_initials = intilias.stringValue();
                        author_lastName=l_name.stringValue();

                        String author= author_initials+" "+author_lastName;
                        
                        String authorsFinale = new String(author.getBytes("iso-8859-1"), "utf-8");
                        
                        arrayPubMedAuthorsDupl.add(authorsFinale);
                        

                    }

                }
               arrayPubMedAuthors = getListNotDuplicate(arrayPubMedAuthorsDupl);
            }
            else
            {
                sAuthors="---";
            }
                
            result_authors.close();
            
            
            for (int i=0;i<arrayPubMedAuthors.size();i++)
            {
               if(arrayPubMedAuthors.get(i).toString()!="")
                    sAuthors=sAuthors+"##"+arrayPubMedAuthors.get(i).toString();
                
            }
         
          
            

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sAuthors;
    }
    
    
    
    public static String getPubmedDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
     
        ArrayList arrayPubMedDescriptionDupl = new ArrayList();
        
        ArrayList arrayPubMedDescription = new ArrayList();
        String sDescriptions="";
        try {

            
            String queryStringDescriptions ="PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                    + "SELECT ?resource_desc ?desc \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc-term:abstract ?resource_desc.\n"
                    +" ?resource_desc <http://bio2rdf.org/pubmed_vocabulary:abstract_text> ?desc.\n"
                    + "}";
            
           
            
            

            TupleQuery tupleQuery_description = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescriptions);



            TupleQueryResult result_description = tupleQuery_description.evaluate();


           

            String descriptions = "";
            
            
           

            if (result_description.hasNext()) {
                while (result_description.hasNext()) {
                    BindingSet bindingSet_description = result_description.next();

                    if (bindingSet_description.getValue("resource_desc") != null) {

                        Value description = bindingSet_description.getValue("desc");

                        descriptions = description.stringValue();
                      
                 
                        String descrFinale = new String(description.stringValue().getBytes("iso-8859-1"), "utf-8");
                       
                        arrayPubMedDescriptionDupl.add(descrFinale);
                       

                    }

                }
               arrayPubMedDescription = getListNotDuplicate(arrayPubMedDescriptionDupl);
            }
            else
            {
                sDescriptions="---";
                
                
            }
                
            result_description.close();
            
            if(arrayPubMedDescription.size()>0){
            for (int i=0;i<arrayPubMedDescription.size();i++)
            {
               
                  
                    sDescriptions=sDescriptions+"##"+arrayPubMedDescription.get(i).toString();
               
            }
            }
         
          
            

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDescriptions;
    }
    
      public static String getPubmedIdentifier(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
     
        ArrayList arrayPubmedIdDupl = new ArrayList();
        
        ArrayList arrayPubmedId = new ArrayList();
        String sId="";
        try {
           
            String queryStringId ="PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                    + "SELECT ?id \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc-term:identifier ?id.\n"
                    
                    + "}";
            
            
            
            

            TupleQuery tupleQuery_id = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringId);



            TupleQueryResult result_id = tupleQuery_id.evaluate();


            

            String ids = "";
            

            if (result_id.hasNext()) {
                while (result_id.hasNext()) {
                    BindingSet bindingSet_id = result_id.next();

                    if (bindingSet_id.getValue("id") != null) {

                        Value idf = bindingSet_id.getValue("id");

                        ids = idf.stringValue();
                      

                        String idFinale = new String(ids.getBytes("iso-8859-1"), "utf-8");
                        
                        arrayPubmedIdDupl.add(idFinale);
                        

                    }

                }
               arrayPubmedId = getListNotDuplicate(arrayPubmedIdDupl);
            }
            else
            {
                sId="";
            }
                
            result_id.close();
            
            
            for (int i=0;i<arrayPubmedId.size();i++)
            {
               if(arrayPubmedId.get(i).toString()!="")
                    sId=sId+"##"+arrayPubmedId.get(i).toString();
                
            }
         
       
            

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sId;
    }
      
      public static ArrayList getPubmedPublisher(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {
     
        ArrayList arrayPubmedPublisherDupl = new ArrayList();
        
        ArrayList arrayPubmedPublisher = new ArrayList();
        String sPublisher="";

           
            
            String queryStringPublisher =""
                    + "SELECT ?publisher \n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://bio2rdf.org/pubmed_vocabulary:affiliation> ?publisher.\n"
                    
                    + "}";
            
           
            
            

            TupleQuery tupleQuery_publisher = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringPublisher);



            TupleQueryResult result_publisher = tupleQuery_publisher.evaluate();



            String publishers = "";
            
            
           

            if (result_publisher.hasNext()) {
                while (result_publisher.hasNext()) {
                    BindingSet bindingSet_publisher = result_publisher.next();

                    if (bindingSet_publisher.getValue("publisher") != null) {

                        Value publisher = bindingSet_publisher.getValue("publisher");

                        publishers = publisher.stringValue();
                      

                        String publisherFinale = new String(publisher.stringValue().getBytes("iso-8859-1"), "utf-8");
                       
                        arrayPubmedPublisherDupl.add(publisherFinale);
                        

                    }

                }
               arrayPubmedPublisher = getListNotDuplicate(arrayPubmedPublisherDupl);

            

       
        
    }
            return arrayPubmedPublisher;
      }
   
       public static ArrayList getPubmedLanguage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {
     
        ArrayList arrayIsidoreLanguageDupl = new ArrayList();
        
        ArrayList arrayIsidoreLanguage = new ArrayList();
        String sLanguage="";
       
            
            
            String queryStringLanguage ="PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                    + "SELECT ?language \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc-term:language ?language.\n"
                    
                    + "}";
            
            //System.out.println("QUERY language Isidore-->"+queryStringLanguage);
            
            

            TupleQuery tupleQuery_language = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringLanguage);



            TupleQueryResult result_language = tupleQuery_language.evaluate();


           
            String languages = "";
            

            if (result_language.hasNext()) {
                while (result_language.hasNext()) {
                    BindingSet bindingSet_language = result_language.next();

                    if (bindingSet_language.getValue("language") != null) {

                        Value language = bindingSet_language.getValue("language");

                        languages = language.stringValue();
                      

                        String languageFinale = new String(language.stringValue().getBytes("iso-8859-1"), "utf-8");
                        
                        arrayIsidoreLanguageDupl.add(languageFinale);
                        

                    }

                }
               arrayIsidoreLanguage = getListNotDuplicate(arrayIsidoreLanguageDupl);

            

       
        
    }
            return arrayIsidoreLanguage;
      }
       
       public static ArrayList getPubmedDate(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {
     
        ArrayList arrayIsidoreDateDupl = new ArrayList();
        
        ArrayList arrayIsidoreDate = new ArrayList();
        String sDtae="";
       
       
            
            String queryStringDate =""
                    + "SELECT ?date \n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://bio2rdf.org/pubmed_vocabulary:article_date> ?date.\n"
                    
                    + "}";
            
            //System.out.println("QUERY date Isidore-->"+queryStringDate);
            
            

            TupleQuery tupleQuery_date = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);



            TupleQueryResult result_date = tupleQuery_date.evaluate();



            String dates = "";
            
         

            if (result_date.hasNext()) {
                while (result_date.hasNext()) {
                    BindingSet bindingSet_date = result_date.next();

                    if (bindingSet_date.getValue("date") != null) {

                        Value date = bindingSet_date.getValue("date");

                        dates = date.stringValue();
                      

                        String dateFinale = new String(date.stringValue().getBytes("iso-8859-1"), "utf-8");
                       
                        arrayIsidoreDateDupl.add(dateFinale);
                        

                    }

                }
               arrayIsidoreDate = getListNotDuplicate(arrayIsidoreDateDupl);

            

       
        
    }
            return arrayIsidoreDate;
       }

 public static ArrayList getPubmedType(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {
     
        ArrayList arrayPubmedTypeDupl = new ArrayList();
        
        ArrayList arrayPubmedType = new ArrayList();
        String sType="";
       
            
            String queryStringType =""
                    + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                    +" select ?type where { \n"
                    +"<"+ resource+ "> <http://bio2rdf.org/pubmed_vocabulary:publication_type> ?type.\n"
                    
                    +"}";


            
           
            TupleQuery tupleQuery_type = PubMedConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);



            TupleQueryResult result_type= tupleQuery_type.evaluate();


           
            String types = "";
            
            
          

            if (result_type.hasNext()) {
                while (result_type.hasNext()) {
                    BindingSet bindingSet_type = result_type.next();

                    if (bindingSet_type.getValue("type") != null) {

                        Value type = bindingSet_type.getValue("type");

                        types = type.stringValue();
                      

                        String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");
                        
                        arrayPubmedTypeDupl.add(typeFinale);
                        

                    }

                }
               arrayPubmedType = getListNotDuplicate(arrayPubmedTypeDupl);

       
        
    }
            return arrayPubmedType;
      }

}

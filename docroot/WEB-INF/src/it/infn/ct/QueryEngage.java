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
public class QueryEngage {

    public static RepositoryConnection EngageConnection;
    public static ArrayList arrayEngageResource;
    // public static ArrayList arrayEngageTitles;

    static public RepositoryConnection ConnectionToEngage(String EngageEndPoint) throws RepositoryException {
        //String endpointURL = "http://engagesrv.epu.ntua.gr:8890/sparql";

        Repository myRepository = new HTTPRepository(EngageEndPoint);

        myRepository.initialize();

        EngageConnection = myRepository.getConnection();

        return EngageConnection;

    }

    static public int testEndPoint(String EngageEndPoint) throws MalformedURLException, IOException {
        // URL url = new URL("http://engagesrv.epu.ntua.gr:8890/sparql");
        URL url = new URL(EngageEndPoint);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);

            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);

            statusCode = -1;
        }
        System.out.println("STATUS  ENGAGE URL--->" + statusCode);

        return statusCode;
    }

    public static ArrayList queryEngageResourceFromHomepage(String search_word, String numPage, int numberRecords, String EngageEndPoint) throws RepositoryException, MalformedQueryException,  UnsupportedEncodingException, MalformedURLException, IOException {
        ArrayList arrayEngageResourceDupl = new ArrayList();
        arrayEngageResource = new ArrayList();
        int statusCode = testEndPoint(EngageEndPoint);
        if (statusCode != -1) {
            try {
                ConnectionToEngage(EngageEndPoint);

                String word = "' " + search_word + " '";

               
                int page = Integer.parseInt(numPage);


                int numOffset = (page - 1) * numberRecords;
                int numerFinal = numberRecords * page;
                String queryString = "";

                if (search_word.contains(":")) {

                    String[] splitSword = search_word.split(":");
                    String field = splitSword[0];

                    if (field.equals(SemanticQuery.search_filter.author.toString()) || field.equals(SemanticQuery.search_filter.format.toString()) || field.equals(SemanticQuery.search_filter.type.toString()) || field.equals(SemanticQuery.search_filter.publisher.toString()) || field.equals(SemanticQuery.search_filter.subject.toString())) {

                        SemanticQuery.search_filter wordFilter = SemanticQuery.search_filter.valueOf(field);

                        System.out.println("wordFilter ENGAGE =>>>>>>" + wordFilter);
                    
                   

                    String s = splitSword[1];
                    String search = "'" + s + "'";
                    
                    
                    
                    
                    switch (wordFilter) {
                            case author:

                                queryString = ""
                                + "select distinct ?homepage where { \n"
                                + "?s dcterms:creator ?creator. \n"
                                + "?creator foaf:name ?author. \n"
                                + "?s foaf:homepage ?homepage.\n"
                                + "FILTER regex(?author, '" + s + "','i').\n"
                                + "}";
                                break;
                            case subject:
                                queryString = ""
                                + "select distinct ?homepage \n"
                                + "where{ \n"
                                + "?s foaf:homepage ?homepage.\n"
                                + "?s <http://www.w3.org/ns/dcat#keyword> ?keyword.\n"
                                
                                + "FILTER regex(?keyword, '" + s + "','i').\n"
                                + "}";
                            case type:
                                queryString = ""
                            + "SELECT distinct ?homepage  WHERE {\n"
                            + "?resource dcterms:title ?title.\n"
                            + "?resource foaf:homepage ?homepage.\n"
                            + "FILTER regex(?title, '" + search_word + "','i').\n"
                            + "}limit " + numerFinal;

                                break;
                            case format:
                                queryString = ""
                            + "SELECT distinct ?homepage  WHERE {\n"
                            + "?resource dcterms:title ?title.\n"
                            + "?resource foaf:homepage ?homepage.\n"
                            + "FILTER regex(?title, '" + search_word + "','i').\n"
                            + "}limit " + numerFinal;
                                break;
                            case publisher:
                                 queryString = ""
                                + "select distinct ?homepage \n"
                                + "where{ \n"
                                + "?s foaf:homepage ?homepage.\n"
                                + "?s dcterms:publisher ?publisher.\n"
                                + "?publisher foaf:name ?publisher_name.\n"
                                
                                 + "FILTER regex(?publisher_name, '" + s + "','i').\n"   
                                + "}limit " + numerFinal;

                            default:

                                break;

                        }
                    } else {

                         queryString = ""
                            + "SELECT distinct ?homepage  WHERE {\n"
                            + "?resource dcterms:title ?title.\n"
                            + "?resource foaf:homepage ?homepage.\n"
                            + "FILTER regex(?title, '" + search_word + "','i').\n"
                            + "}limit " + numerFinal;
                    }
                    
                    

          
                        
                    

                } else {

                    queryString = ""
                            + "SELECT distinct ?homepage  WHERE {\n"
                            + "?resource dcterms:title ?title.\n"
                            + "?resource foaf:homepage ?homepage.\n"
                            + "FILTER regex(?title, '" + search_word + "','i').\n"
                            + "}limit " + numerFinal;
                }
    //            String queryString = ""
    //                    + "SELECT distinct ?homepage  WHERE {\n"
    //                    + "?resource dcterms:title ?title.\n"
    //                    + "?resource foaf:homepage ?homepage.\n"
    //                    + "FILTER regex(?title, '" + search_word + "','i').\n"
    //                    + "}limit " + numerFinal;








                System.out.println("QUERY ENGAGE : " + queryString);

                TupleQuery tupleQuery = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



                TupleQueryResult result = tupleQuery.evaluate();




                if (result.hasNext()) {
                    while (result.hasNext()) {


                        BindingSet bindingSet = result.next();
                        String resource = bindingSet.getValue("homepage").stringValue();

                        arrayEngageResourceDupl.add(resource);
                    }

                }



                arrayEngageResource = getListNotDuplicate(arrayEngageResourceDupl);
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return arrayEngageResource;
    }

    public static String getEngageTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayEngageTitleDupl = new ArrayList();
        ArrayList arrayEngageTitles = new ArrayList();
        String sTitles = "";
        try {


            String queryStringTitle = "";

            queryStringTitle = ""
                    + " select  distinct ?title \n"
                    + "where {\n"
                    + "?s dcterms:title ?title.\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "}";



            TupleQuery tupleQuery_title = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();




            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();


                        String titleFinale = new String(titles.getBytes("iso-8859-1"), "utf-8");

                        arrayEngageTitleDupl.add(titleFinale);


                    }

                }
                arrayEngageTitles = getListNotDuplicate(arrayEngageTitleDupl);
            } else {
                sTitles = "---";

            }
            result_title.close();




            for (int i = 0; i < arrayEngageTitles.size(); i++) {

                if (arrayEngageTitles.get(i).toString() != "") {
                    sTitles = sTitles + "##" + arrayEngageTitles.get(i).toString();
                }
            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTitles;
    }

    public static String getEngageAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEngageAuthorsDupl = new ArrayList();

        ArrayList arrayEngageAuthors = new ArrayList();
        String sAuthors = "";
        try {





            String queryStringAuthors = ""
                    + "select distinct ?author where { \n"
                    + "?s dcterms:creator ?creator. \n"
                    + "?creator foaf:name ?author. \n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "}";




            TupleQuery tupleQuery_authors = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthors);



            TupleQueryResult result_authors = tupleQuery_authors.evaluate();




            String authors = "";

            if (result_authors.hasNext()) {
                while (result_authors.hasNext()) {
                    BindingSet bindingSet_authors = result_authors.next();

                    if (bindingSet_authors.getValue("author") != null) {

                        Value author = bindingSet_authors.getValue("author");

                        authors = author.stringValue();


                        if (!authors.equals("0")) {

                            String authorsFinale = new String(authors.getBytes("iso-8859-1"), "utf-8");

                            arrayEngageAuthorsDupl.add(authorsFinale);

                        }

                    }

                }
                arrayEngageAuthors = getListNotDuplicate(arrayEngageAuthorsDupl);
            } else {
                sAuthors = "";
            }

            result_authors.close();


            for (int i = 0; i < arrayEngageAuthors.size(); i++) {
                if (arrayEngageAuthors.get(i).toString() != "") {
                    sAuthors = sAuthors + "##" + arrayEngageAuthors.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sAuthors;
    }

    public static String getEngageDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEngageDescriptionDupl = new ArrayList();

        ArrayList arrayEngageDescription = new ArrayList();
        String sDescriptions = "";
        try {

            String queryStringDescriptions = ""
                    + "SELECT distinct ?desc { \n"
                    + "?s dcterms:description ?desc.\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "}";

            TupleQuery tupleQuery_description = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescriptions);



            TupleQueryResult result_description = tupleQuery_description.evaluate();




            String descriptions = "";




            if (result_description.hasNext()) {
                while (result_description.hasNext()) {
                    BindingSet bindingSet_description = result_description.next();

                    if (bindingSet_description.getValue("desc") != null) {

                        Value description = bindingSet_description.getValue("desc");

                        descriptions = description.stringValue();


                        String descrFinale = new String(descriptions.getBytes("iso-8859-1"), "utf-8");

                        arrayEngageDescriptionDupl.add(descrFinale);


                    }

                }
                arrayEngageDescription = getListNotDuplicate(arrayEngageDescriptionDupl);
            } else {
                sDescriptions = "";


            }

            result_description.close();

            if (arrayEngageDescription.size() > 0) {
                for (int i = 0; i < arrayEngageDescription.size(); i++) {


                    sDescriptions = sDescriptions + "##" + arrayEngageDescription.get(i).toString();

                }
            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDescriptions;
    }

    public static String getEngageIdentifier(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEngageIdDupl = new ArrayList();

        ArrayList arrayEngageId = new ArrayList();
        String sId = "";
        try {

            String queryStringId = ""
                    + "SELECT distinct ?id { \n"
                    + "?s dcterms:identifier ?id.\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "}";


            TupleQuery tupleQuery_id = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringId);



            TupleQueryResult result_id = tupleQuery_id.evaluate();




            String ids = "";


            if (result_id.hasNext()) {
                while (result_id.hasNext()) {
                    BindingSet bindingSet_id = result_id.next();

                    if (bindingSet_id.getValue("id") != null) {

                        Value id = bindingSet_id.getValue("id");

                        ids = id.stringValue();



                        String idFinale = new String(ids.getBytes("iso-8859-1"), "utf-8");

                        arrayEngageIdDupl.add(idFinale);


                    }

                }
                arrayEngageId = getListNotDuplicate(arrayEngageIdDupl);
            } else {
                sId = "---";
            }

            result_id.close();


            for (int i = 0; i < arrayEngageId.size(); i++) {
                if (!arrayEngageId.get(i).toString().equals("")) {
                    sId = sId + "##" + arrayEngageId.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sId;
    }

    public static String getEngageHomepage(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayEngageHomepageDupl = new ArrayList();

        ArrayList arrayEngageHomepage = new ArrayList();
        String sHomepage = "";
        try {

            String queryStringHomepage = ""
                    + "SELECT ?homepage { \n"
                    + "?s foaf:homepage ?homepage.\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "}";





            TupleQuery tupleQuery_homepage = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringHomepage);



            TupleQueryResult result_homepage = tupleQuery_homepage.evaluate();




            String homepages = "";


            if (result_homepage.hasNext()) {
                while (result_homepage.hasNext()) {
                    BindingSet bindingSet_homepage = result_homepage.next();

                    if (bindingSet_homepage.getValue("homepage") != null) {

                        Value home_page = bindingSet_homepage.getValue("homepage");

                        homepages = home_page.stringValue();



                        String homepageFinale = new String(homepages.getBytes("iso-8859-1"), "utf-8");

                        arrayEngageHomepageDupl.add(homepageFinale);


                    }

                }
                arrayEngageHomepage = getListNotDuplicate(arrayEngageHomepageDupl);
            } else {
                sHomepage = "---";
            }

            result_homepage.close();


            for (int i = 0; i < arrayEngageHomepage.size(); i++) {
                if (!arrayEngageHomepage.get(i).toString().equals("")) {
                    sHomepage = sHomepage + "##" + arrayEngageHomepage.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sHomepage;
    }

    public static String getEngageLanguage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        String sLanguage = "";



        String queryStringLanguage = ""
                + "SELECT ?language \n"
                + "WHERE {\n "
                + "?s dcterms:language ?language.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";

        TupleQuery tupleQuery_language = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringLanguage);

        TupleQueryResult result_language = tupleQuery_language.evaluate();



        String linkLanguage = "";
        if (result_language.hasNext()) {
            while (result_language.hasNext()) {
                BindingSet bindingSet_language = result_language.next();

                if (bindingSet_language.getValue("language") != null) {

                    Value language = bindingSet_language.getValue("language");


                    linkLanguage = new String(language.stringValue().getBytes("iso-8859-1"), "utf-8");

                }

            }


            if (!linkLanguage.equals("")) {
                String[] split_language = linkLanguage.split("/");

                sLanguage = split_language[split_language.length - 1].toString();

            } else {
                sLanguage = "noLanguage";
            }





        }
        return sLanguage;
    }

    public static String getEngageDate(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        String sDate = "";


        String queryStringDate = ""
                + "SELECT ?date \n"
                + "WHERE {\n "
                + "?s dcterms:created ?date.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";

        TupleQuery tupleQuery_date = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);

        TupleQueryResult result_date = tupleQuery_date.evaluate();




        String dates = "";




        if (result_date.hasNext()) {
            while (result_date.hasNext()) {
                BindingSet bindingSet_date = result_date.next();

                if (bindingSet_date.getValue("date") != null) {

                    Value date = bindingSet_date.getValue("date");

                    dates = new String(date.stringValue().getBytes("iso-8859-1"), "utf-8");


                }

            }

        }
        if (!dates.equals("")) {
            sDate = dates;
        }

        return sDate;
    }

    public static String getEngageModified(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        String sModified = "";

        String queryStringDate = ""
                + "SELECT ?modified \n"
                + "WHERE {\n "
                + "?s dcterms:modified ?modified.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";





        TupleQuery tupleQuery_date = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);



        TupleQueryResult result_date = tupleQuery_date.evaluate();

        String modified = "";




        if (result_date.hasNext()) {
            while (result_date.hasNext()) {
                BindingSet bindingSet_date = result_date.next();

                if (bindingSet_date.getValue("modified") != null) {

                    Value date_mod = bindingSet_date.getValue("modified");

                    modified = new String(date_mod.stringValue().getBytes("iso-8859-1"), "utf-8");

                }

            }

        }
        if (!modified.equals("")) {
            sModified = modified;
        } else {
            sModified = "no modified";
        }

        return sModified;
    }

    public static String getEngageTemporal(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {




        String sTemporal = "";

        String queryStringDate = ""
                + "SELECT ?temporal \n"
                + "WHERE {\n "
                + "?s dcterms:temporal ?temporal.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";


        TupleQuery tupleQuery_date = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);

        TupleQueryResult result_date = tupleQuery_date.evaluate();

        String temporal = "";




        if (result_date.hasNext()) {
            while (result_date.hasNext()) {
                BindingSet bindingSet_date = result_date.next();

                if (bindingSet_date.getValue("temporal") != null) {

                    Value date_temporal = bindingSet_date.getValue("temporal");

                    temporal = new String(date_temporal.stringValue().getBytes("iso-8859-1"), "utf-8");





                }

            }

        }
        if (!temporal.equals("")) {
            sTemporal = temporal;
        }


        return sTemporal;
    }

    public static ArrayList getEngagePublisher(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEngagePublisherDupl = new ArrayList();

        ArrayList arrayEngagePublisher = new ArrayList();
        String sPublisher = "";

        String queryStringPublisher = ""
                + "select distinct ?publisher_name \n"
                + "where{ \n"
                + "?s dcterms:publisher ?publisher.\n"
                + "?publisher foaf:name ?publisher_name.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";

        TupleQuery tupleQuery_publisher = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringPublisher);

        TupleQueryResult result_publisher = tupleQuery_publisher.evaluate();


        String publishers = "";




        if (result_publisher.hasNext()) {
            while (result_publisher.hasNext()) {
                BindingSet bindingSet_publisher = result_publisher.next();

                if (bindingSet_publisher.getValue("publisher") != null) {

                    Value publisher = bindingSet_publisher.getValue("publisher");

                    publishers = publisher.stringValue();

                    String publisherFinale = new String(publisher.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEngagePublisherDupl.add(publisherFinale);


                }

            }
            arrayEngagePublisher = getListNotDuplicate(arrayEngagePublisherDupl);





        }
        return arrayEngagePublisher;
    }

    public static ArrayList getEngageKeywords(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEngageKeywordsDupl = new ArrayList();

        ArrayList arrayEngageKeywords = new ArrayList();

        String queryString = ""
                + "select  ?keyword \n"
                + "where{ \n"
                + "?s <http://www.w3.org/ns/dcat#keyword> ?keyword.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";





        TupleQuery tupleQuery = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



        TupleQueryResult result = tupleQuery.evaluate();


        String publishers = "";

        if (result.hasNext()) {
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();

                if (bindingSet.getValue("keyword") != null) {

                    Value keyword = bindingSet.getValue("keyword");

                    String keywordFinale = new String(keyword.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEngageKeywordsDupl.add(keywordFinale);


                }

            }
            arrayEngageKeywords = getListNotDuplicate(arrayEngageKeywordsDupl);

        }
        return arrayEngageKeywords;
    }

    public static String getEngageCoverage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {


        String sCoverage = "";


        String queryStringCoverage = ""
                + "select  ?coverage \n"
                + "where{ \n"
                + "?s dcterms:coverage ?coverage.\n"
                + "?s foaf:homepage <" + resource + ">.\n"
                + "}";




        TupleQuery tupleQuery_coverage = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringCoverage);



        TupleQueryResult result_coverage = tupleQuery_coverage.evaluate();


        String coverages = "";


        if (result_coverage.hasNext()) {
            while (result_coverage.hasNext()) {
                BindingSet bindingSet_coverage = result_coverage.next();

                if (bindingSet_coverage.getValue("coverage") != null) {

                    Value coverage = bindingSet_coverage.getValue("coverage");
                    coverages = coverage.stringValue();


                }

            }
            if (!coverages.equals("") && !coverages.equals("0")) {
                sCoverage = coverages;
            }



        }
        return sCoverage;
    }

    public static String getEngageDistributionTitle(String resource) {

        String sDistributionTitle = "";
        try {

            String queryStringDistribution = ""
                    + "select distinct ?distribution  ?titleDistribution \n"
                    + "where {\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "?s <http://www.w3.org/ns/dcat#distribution> ?distribution.\n"
                    + "?distribution dcterms:title ?titleDistribution.\n"
                    + "}";





            TupleQuery tupleQuery = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDistribution);



            TupleQueryResult result = tupleQuery.evaluate();




            String titleDistribution = "";





            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    if (bindingSet.getValue("distribution") != null) {

                        titleDistribution = bindingSet.getValue("titleDistribution").stringValue();




                        sDistributionTitle = titleDistribution;


                    }

                }


            }


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {

            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sDistributionTitle;
    }

    public static String getEngageDistributionAccessURL(String resource) {

        String sDistributionAccessURL = "";
        try {

            String queryStringDistribution = ""
                    + "select distinct ?distribution  ?accessURL \n"
                    + "where {\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "?s <http://www.w3.org/ns/dcat#distribution> ?distribution.\n"
                    + "?distribution <http://www.w3.org/ns/dcat#accessURL> ?accessURL.\n"
                    + "}";





            TupleQuery tupleQuery = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDistribution);



            TupleQueryResult result = tupleQuery.evaluate();




            String accessURL = "";





            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    if (bindingSet.getValue("distribution") != null) {

                        accessURL = bindingSet.getValue("accessURL").stringValue();




                        sDistributionAccessURL = accessURL;


                    }

                }


            }


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {

            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sDistributionAccessURL;
    }

    public static String getEngageDistributionDownloadURL(String resource) {

        String sDistributionDownloadURL = "";
        try {

            String queryStringDistribution = ""
                    + "select distinct ?distribution  ?downloadURL \n"
                    + "where {\n"
                    + "?s foaf:homepage <" + resource + ">.\n"
                    + "?s <http://www.w3.org/ns/dcat#distribution> ?distribution.\n"
                    + "?distribution <http://www.w3.org/ns/dcat#downloadURL> ?downloadURL.\n"
                    + "}";





            TupleQuery tupleQuery = EngageConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDistribution);



            TupleQueryResult result = tupleQuery.evaluate();




            String downloadURL = "";





            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    if (bindingSet.getValue("distribution") != null) {

                        downloadURL = bindingSet.getValue("downloadURL").stringValue();




                        sDistributionDownloadURL = downloadURL;


                    }

                }


            }


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {

            Logger.getLogger(QueryEngage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sDistributionDownloadURL;
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
}

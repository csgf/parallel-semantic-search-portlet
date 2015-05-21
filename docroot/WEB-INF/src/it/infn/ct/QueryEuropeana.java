/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct;

//import eu.europeana.api.client.Api2Query;
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
public class QueryEuropeana {

    public static String EUROPEANA_API_KEY = "vVKKnVLz7";
    //public static ArrayList arrayEuropeanaResourceInfo;
    public static RepositoryConnection EuropeanaConnection;
    public static ArrayList arrayEuropeanaResource;
    public static ArrayList arrayEuropeanaTitles;
    public static boolean europeanaEndpointAvailable;

    static public RepositoryConnection ConnectionToEuropeana(String endpointURL) throws RepositoryException {
        // String endpointURL = "http://europeana.ontotext.com/sparql";

        Repository myRepository = new HTTPRepository(endpointURL);

        myRepository.initialize();

        EuropeanaConnection = myRepository.getConnection();

        return EuropeanaConnection;

    }

    static public int testEndPoint(String EuropeanaEndPoint) throws MalformedURLException, IOException {

        System.out.println("EUROPEANA ENDPOINT FROM PREFERENCES----->" + EuropeanaEndPoint);

        URL url = new URL(EuropeanaEndPoint);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryEuropeana.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryEuropeana.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS  EUROPEANA URL--->" + statusCode);

        return statusCode;
    }

    public static ArrayList queryEuropeanaResource(String search_word, String numPage, int numberRecords, String EuropeanaEndPoint) throws MalformedURLException, IOException, RepositoryException {
        ArrayList arrayEuropeanaResourceDupl = new ArrayList();
        arrayEuropeanaResource = new ArrayList();
        int statusCode = testEndPoint(EuropeanaEndPoint);
        if (statusCode != -1) {
            try {
                ConnectionToEuropeana(EuropeanaEndPoint);

                String word = "' " + search_word + " '";

                int page = Integer.parseInt(numPage);


                int numOffset = (page - 1) * numberRecords;
                int numberFinal = numberRecords * page;





                String queryString = "";

                if (search_word.contains(":")) {

                    String[] splitSword = search_word.split(":");
                    String field = splitSword[0];
                    
                    if (field.equals(SemanticQuery.search_filter.author.toString()) || field.equals(SemanticQuery.search_filter.format.toString()) || field.equals(SemanticQuery.search_filter.type.toString()) || field.equals(SemanticQuery.search_filter.publisher.toString()) || field.equals(SemanticQuery.search_filter.subject.toString())) {

                        SemanticQuery.search_filter wordFilter = SemanticQuery.search_filter.valueOf(field);

                        System.out.println("wordFilter ENGAGE =>>>>>>" + wordFilter);

                    String s = splitSword[1];
                    String search = "'" + s + "'";
                    String search_uppercase = "' " + s.toUpperCase() + " '";

                    
                    switch (wordFilter) {
                                case author:
                                    queryString = ""
                                           + "PREFIX dct:<http://purl.org/dc/terms/>\n"
                                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                                + "select ?proxy {\n"
                                + "?proxy ore:proxyFor ?item; dct:created ?creator.\n"
                                + "?creator luc:" + search + "}limit " + numberFinal;
                                    break;
                                case subject:
                                     queryString = ""
                                + "PREFIX dct:<http://purl.org/dc/terms/>\n"
                                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                                + "select ?proxy {\n"
                                + "?proxy ore:proxyFor ?item; dc:subject ?subject.\n"
                                + "?subject luc:" + search + "}limit " + numberFinal;
                                    break;
                                case type:
                                    queryString = ""
                                + "PREFIX dct:<http://purl.org/dc/terms/>\n"
                                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                                + "select ?proxy {\n"
                                + "?proxy ore:proxyFor ?item; dc:type ?type.\n"
                                + "?type luc:" + search + "}limit " + numberFinal;

                                    break;
                                case format:

                                     queryString = ""
                                + "PREFIX dct:<http://purl.org/dc/terms/>\n"
                                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                                + "select ?proxy {\n"
                                + "?proxy ore:proxyFor ?item; dc:format ?format.\n"
                                + "?format luc:" + search + "}limit " + numberFinal;
                                    break;
                                case publisher:
                                   queryString = ""
                                + "PREFIX dct:<http://purl.org/dc/terms/>\n"
                                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                                + "select ?proxy {\n"
                                + "?proxy ore:proxyFor ?item; dc:publisher ?publisher.\n"
                                + "?publisher luc:" + search + "}limit " + numberFinal;

                                default:

                                    break;

                            }
                        } else {
                           queryString = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                            + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                            + "select ?proxy {\n"
                            + "?proxy ore:proxyFor ?item; dc:title ?title.\n"
                            + "?title luc:" + word + "}limit " + numberFinal;
                        }




                    } else {
                        queryString = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                            + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                            + "select ?proxy {\n"
                            + "?proxy ore:proxyFor ?item; dc:title ?title.\n"
                            + "?title luc:" + word + "}limit " + numberFinal;
                    }

                    
                       
                

                System.out.println("QUERY Europeana: " + queryString);

                TupleQuery tupleQuery = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



                TupleQueryResult result = tupleQuery.evaluate();


                if (result.hasNext()) {
                    while (result.hasNext()) {


                        BindingSet bindingSet = result.next();
                        String resource_proxy = bindingSet.getValue("proxy").stringValue();

                        arrayEuropeanaResourceDupl.add(resource_proxy);
                    }

                }



                arrayEuropeanaResource = getListNotDuplicate(arrayEuropeanaResourceDupl);

            } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryEuropeana.class.getName()).log(Level.SEVERE, null, ex);

                arrayEuropeanaResource.add("Exception");

                System.out.println("EUROPEANA QueryEvaluationException");

            } catch (MalformedQueryException ex) {
                Logger.getLogger(QueryEuropeana.class.getName()).log(Level.SEVERE, null, ex);
                arrayEuropeanaResource.add("Exception");
                System.out.println("EUROPEANA MalformedQueryException");

            } catch (RepositoryException ex) {
                Logger.getLogger(QueryEuropeana.class.getName()).log(Level.SEVERE, null, ex);
                arrayEuropeanaResource.add("Exception");
                System.out.println("EUROPEANA RepositoryException");
            } finally {
                System.out.println("EUROPEANA CLOSE");
                EuropeanaConnection.close();
            }

        }

        return arrayEuropeanaResource;
    }

    public static String getEuropeanaTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayEuropeanaTitleDupl = new ArrayList();
        arrayEuropeanaTitles = new ArrayList();
        String sTitles = "";
        try {

            String queryStringTitle = "";
            queryStringTitle = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                    + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                    + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                    + "select ?title {\n"
                    + "<" + resource + "> dc:title ?title.\n"
                    + "}";


            System.out.println("QUERY TITLE Europeana-->" + queryStringTitle);



            TupleQuery tupleQuery_title = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();




            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();

                        String titleFinale = new String(titles.getBytes("iso-8859-1"), "utf-8");

                        arrayEuropeanaTitleDupl.add(titleFinale);

                    }

                }
                arrayEuropeanaTitles = getListNotDuplicate(arrayEuropeanaTitleDupl);
            } else {
                sTitles = "---";

            }
            result_title.close();




            for (int i = 0; i < arrayEuropeanaTitles.size(); i++) {

                if (arrayEuropeanaTitles.get(i).toString() != "") {
                    sTitles = sTitles + "##" + arrayEuropeanaTitles.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTitles;
    }

    public static String getEuropeanaAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaAuthorsDupl = new ArrayList();

        ArrayList arrayEuropeanaAuthors = new ArrayList();
        String sAuthors = "";
        try {

            String queryStringAuthors = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                    + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                    + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                    + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                    + "SELECT ?creator { \n"
                    + "<" + resource + "> dct:created ?creator.\n"
                    + "}";

            //System.out.println("QUERY CREATOR Europeana-->" + queryStringAuthors);



            TupleQuery tupleQuery_authors = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthors);



            TupleQueryResult result_authors = tupleQuery_authors.evaluate();


            String authors = "";

            if (result_authors.hasNext()) {
                while (result_authors.hasNext()) {
                    BindingSet bindingSet_authors = result_authors.next();

                    if (bindingSet_authors.getValue("creator") != null) {

                        Value author = bindingSet_authors.getValue("creator");

                        authors = author.stringValue();

                        String authorsFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayEuropeanaAuthorsDupl.add(authorsFinale);

                    }

                }
                arrayEuropeanaAuthors = getListNotDuplicate(arrayEuropeanaAuthorsDupl);
            } else {
                sAuthors = "";
            }

            result_authors.close();


            for (int i = 0; i < arrayEuropeanaAuthors.size(); i++) {
                if (arrayEuropeanaAuthors.get(i).toString() != "") {
                    sAuthors = sAuthors + "##" + arrayEuropeanaAuthors.get(i).toString();
                }

            }



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sAuthors;
    }

    public static String getEuropeanaDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaDescriptionDupl = new ArrayList();

        ArrayList arrayEuropeanaDescrtiption = new ArrayList();
        String sDescriptions = "";
        try {

            String queryStringDescriptions = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                    + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                    + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                    + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                    + "SELECT ?desc { \n"
                    + "<" + resource + "> dc:description ?desc.\n"
                    + "}";

            //System.out.println("QUERY DESCRIPTION Europeana-->" + queryStringDescriptions);



            TupleQuery tupleQuery_description = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescriptions);



            TupleQueryResult result_description = tupleQuery_description.evaluate();




            String descriptions = "";


            if (result_description.hasNext()) {
                while (result_description.hasNext()) {
                    BindingSet bindingSet_description = result_description.next();

                    if (bindingSet_description.getValue("desc") != null) {

                        Value description = bindingSet_description.getValue("desc");

                        descriptions = description.stringValue();

                        String descrFinale = new String(description.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayEuropeanaDescriptionDupl.add(descrFinale);


                    }

                }
                arrayEuropeanaDescrtiption = getListNotDuplicate(arrayEuropeanaDescriptionDupl);
            } else {
                sDescriptions = "";


            }

            result_description.close();

            if (arrayEuropeanaDescrtiption.size() > 0) {
                for (int i = 0; i < arrayEuropeanaDescrtiption.size(); i++) {

                    sDescriptions = sDescriptions + "##" + arrayEuropeanaDescrtiption.get(i).toString();

                }
            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDescriptions;
    }

    public static String getEuropeanaIdentifier(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaIdDupl = new ArrayList();

        ArrayList arrayEuropeanaId = new ArrayList();
        String sId = "";
        try {

            String queryStringId = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                    + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                    + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                    + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                    + "SELECT ?id { \n"
                    + "<" + resource + "> dc:identifier ?id.\n"
                    + "}";

            //System.out.println("QUERY IDENTIFIER Europeana-->" + queryStringId);



            TupleQuery tupleQuery_id = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringId);



            TupleQueryResult result_id = tupleQuery_id.evaluate();


            String ids = "";


            if (result_id.hasNext()) {
                while (result_id.hasNext()) {
                    BindingSet bindingSet_id = result_id.next();

                    if (bindingSet_id.getValue("id") != null) {

                        Value id = bindingSet_id.getValue("id");

                        ids = id.stringValue();



                        String idFinale = new String(ids.getBytes("iso-8859-1"), "utf-8");

                        arrayEuropeanaIdDupl.add(idFinale);


                    }

                }
                arrayEuropeanaId = getListNotDuplicate(arrayEuropeanaIdDupl);
            } else {
                sId = "---";
            }

            result_id.close();


            for (int i = 0; i < arrayEuropeanaId.size(); i++) {
                if (!arrayEuropeanaId.get(i).toString().equals("")) {
                    sId = sId + "##" + arrayEuropeanaId.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sId;
    }

    public static ArrayList getEuropeanaPublisher(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaPublisherDupl = new ArrayList();

        ArrayList arrayEuropeanaPublisher = new ArrayList();
        String sPublisher = "";


        String queryStringPublisher = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?publisher { \n"
                + "<" + resource + "> dc:publisher ?publisher.\n"
                + "}";




        //System.out.println("QUERY PUBLISHER Europeana-->" + queryStringPublisher);



        TupleQuery tupleQuery_publisher = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringPublisher);



        TupleQueryResult result_publisher = tupleQuery_publisher.evaluate();




        String publishers = "";



        if (result_publisher.hasNext()) {
            while (result_publisher.hasNext()) {
                BindingSet bindingSet_publisher = result_publisher.next();

                if (bindingSet_publisher.getValue("publisher") != null) {

                    Value publisher = bindingSet_publisher.getValue("publisher");

                    publishers = publisher.stringValue();

                    String publisherFinale = new String(publisher.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaPublisherDupl.add(publisherFinale);

                }

            }
            arrayEuropeanaPublisher = getListNotDuplicate(arrayEuropeanaPublisherDupl);





        }
        return arrayEuropeanaPublisher;
    }

    public static ArrayList getEuropeanaSubject(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaSubjectDupl = new ArrayList();

        ArrayList arrayEuropeanaSubject = new ArrayList();
        String sSubject = "";


        String queryStringSubject = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?subject { \n"
                + "<" + resource + "> dc:subject ?subject.\n"
                + "}";

        // System.out.println("QUERY Subject Europeana-->" + queryStringSubject);



        TupleQuery tupleQuery_Subject = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSubject);



        TupleQueryResult result_Subject = tupleQuery_Subject.evaluate();

        String subjects = "";




        if (result_Subject.hasNext()) {
            while (result_Subject.hasNext()) {
                BindingSet bindingSet_Subject = result_Subject.next();

                if (bindingSet_Subject.getValue("subject") != null) {

                    Value subject = bindingSet_Subject.getValue("subject");

                    subjects = subject.stringValue();

                    String subjectFinale = new String(subject.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaSubjectDupl.add(subjectFinale);

                }

            }
            arrayEuropeanaSubject = getListNotDuplicate(arrayEuropeanaSubjectDupl);

        }
        return arrayEuropeanaSubject;
    }

    public static ArrayList getEuropeanaSource(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaSourceDupl = new ArrayList();

        ArrayList arrayEuropeanaSource = new ArrayList();
        String sSource = "";



        String queryStringSource = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?source { \n"
                + "<" + resource + "> dc:source ?source.\n"
                + "}";

        System.out.println("QUERY source Europeana-->" + queryStringSource);



        TupleQuery tupleQuery_Source = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSource);



        TupleQueryResult result_Source = tupleQuery_Source.evaluate();




        String sources = "";


        if (result_Source.hasNext()) {
            while (result_Source.hasNext()) {
                BindingSet bindingSet_Source = result_Source.next();

                if (bindingSet_Source.getValue("source") != null) {

                    Value source = bindingSet_Source.getValue("source");

                    sources = source.stringValue();


                    String sourceFinale = new String(source.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaSourceDupl.add(sourceFinale);


                }

            }
            arrayEuropeanaSource = getListNotDuplicate(arrayEuropeanaSourceDupl);





        }
        return arrayEuropeanaSource;
    }

    public static ArrayList getEuropeanaLanguage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaLanguageDupl = new ArrayList();

        ArrayList arrayEuropeanaLanguage = new ArrayList();
        String sLanguage = "";



        String queryStringLanguage = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?language \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:language ?language.\n"
                + "}";

        //System.out.println("QUERY language Europeana-->" + queryStringLanguage);



        TupleQuery tupleQuery_language = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringLanguage);



        TupleQueryResult result_language = tupleQuery_language.evaluate();
        String languages = "";


        if (result_language.hasNext()) {
            while (result_language.hasNext()) {
                BindingSet bindingSet_language = result_language.next();

                if (bindingSet_language.getValue("language") != null) {

                    Value language = bindingSet_language.getValue("language");

                    languages = language.stringValue();


                    String languageFinale = new String(language.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaLanguageDupl.add(languageFinale);


                }

            }
            arrayEuropeanaLanguage = getListNotDuplicate(arrayEuropeanaLanguageDupl);





        }
        return arrayEuropeanaLanguage;
    }

    public static ArrayList getEuropeanaDate(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaDateDupl = new ArrayList();

        ArrayList arrayEuropeanaDate = new ArrayList();
        String sDtae = "";




        System.out.println("Resource_datw-->" + resource);

        String queryStringDate = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?date \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:date ?date.\n"
                + "}";

        //System.out.println("QUERY date Eu-->" + queryStringDate);



        TupleQuery tupleQuery_date = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);



        TupleQueryResult result_date = tupleQuery_date.evaluate();


        String dates = "";


        if (result_date.hasNext()) {
            while (result_date.hasNext()) {
                BindingSet bindingSet_date = result_date.next();

                if (bindingSet_date.getValue("date") != null) {

                    Value date = bindingSet_date.getValue("date");

                    dates = date.stringValue();

                    String dateFinale = new String(date.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaDateDupl.add(dateFinale);


                }

            }
            arrayEuropeanaDate = getListNotDuplicate(arrayEuropeanaDateDupl);





        }
        return arrayEuropeanaDate;
    }

    public static ArrayList getEuropeanaContributor(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaContributorDupl = new ArrayList();

        ArrayList arrayEuropeanaContributor = new ArrayList();
        String sContributors = "";


        String queryStringContributor = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + "SELECT ?contributor { \n"
                + "<" + resource + "> dc:contributor ?contributor.\n"
                + "}";



        //System.out.println("QUERY contributor Europeana-->" + queryStringContributor);



        TupleQuery tupleQuery_contributor = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringContributor);



        TupleQueryResult result_contributor = tupleQuery_contributor.evaluate();




        String dates = "";


        if (result_contributor.hasNext()) {
            while (result_contributor.hasNext()) {
                BindingSet bindingSet_contributor = result_contributor.next();

                if (bindingSet_contributor.getValue("contributor") != null) {

                    Value date = bindingSet_contributor.getValue("contributor");

                    dates = date.stringValue();


                    String contributorFinale = new String(date.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaContributorDupl.add(contributorFinale);


                }

            }
            arrayEuropeanaContributor = getListNotDuplicate(arrayEuropeanaContributorDupl);





        }
        return arrayEuropeanaContributor;
    }

    public static ArrayList getEuropeanaRights(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaRightsDupl = new ArrayList();

        ArrayList arrayEuropeanaRights = new ArrayList();
        String sRights = "";



        String queryStringrRights = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + " select ?rights where { \n"
                + "<" + resource + "> dc:rights ?rights.\n"
                + "}";



        //System.out.println("QUERY rights Isidore-->" + queryStringrRights);



        TupleQuery tupleQuery_rights = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringrRights);



        TupleQueryResult result_rights = tupleQuery_rights.evaluate();



        String rights = "";



        if (result_rights.hasNext()) {
            while (result_rights.hasNext()) {
                BindingSet bindingSet_rights = result_rights.next();

                if (bindingSet_rights.getValue("rights") != null) {

                    Value right = bindingSet_rights.getValue("rights");

                    rights = right.stringValue();



                    String rightFinale = new String(right.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaRightsDupl.add(rightFinale);


                }

            }
            arrayEuropeanaRightsDupl = getListNotDuplicate(arrayEuropeanaRightsDupl);





        }
        return arrayEuropeanaRightsDupl;
    }

    public static String getEuropeanaCoverage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {


        String sCoverage = "";



        String queryStringCoverage = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + " select ?coverage where { \n"
                + "<" + resource + "> dc:coverage ?coverage.\n"
                + "}";






        //System.out.println("QUERY contributor Europeana-->" + queryStringCoverage);



        TupleQuery tupleQuery_coverage = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringCoverage);



        TupleQueryResult result_coverage = tupleQuery_coverage.evaluate();




        String coverages = "";
        String label_coverage = "";
        String latitude_coverage = "";
        String longitude_coverage = "";


        //System.out.println("contributor QUERY " + result_coverage.hasNext());

        if (result_coverage.hasNext()) {
            while (result_coverage.hasNext()) {
                BindingSet bindingSet_coverage = result_coverage.next();

                if (bindingSet_coverage.getValue("coverage") != null) {

                    Value coverage = bindingSet_coverage.getValue("coverage");


                    coverages = coverage.stringValue();





                }

            }
            coverages = label_coverage + " latitude: " + latitude_coverage + "longitude: " + longitude_coverage;

        }
        return coverages;
    }

    public static String getEuropeanaType(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaTypeDupl = new ArrayList();
        ArrayList arrayEuropeanaTypes = new ArrayList();
        String sTypes = "";
        try {


            String queryStringType = "";
            queryStringType = "PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                    + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                    + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                    + "select ?type {\n"
                    + "<" + resource + "> dc:type ?type.\n"
                    + "}";



            TupleQuery tupleQuery_type = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);



            TupleQueryResult result_type = tupleQuery_type.evaluate();
            String types = "";

            if (result_type.hasNext()) {
                while (result_type.hasNext()) {
                    BindingSet bindingSet_type = result_type.next();

                    if (bindingSet_type.getValue("type") != null) {

                        Value type = bindingSet_type.getValue("type");

                        types = type.stringValue();


                        String typeFinale = new String(types.getBytes("iso-8859-1"), "utf-8");

                        arrayEuropeanaTypeDupl.add(typeFinale);


                    }

                }
                arrayEuropeanaTypes = getListNotDuplicate(arrayEuropeanaTypeDupl);
            } else {
                sTypes = "---";

            }
            result_type.close();




            for (int i = 0; i < arrayEuropeanaTypes.size(); i++) {

                if (arrayEuropeanaTypes.get(i).toString() != "") {
                    sTypes = sTypes + "##" + arrayEuropeanaTypes.get(i).toString();
                }

            }



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTypes;

    }

    public static ArrayList getEuropeanaFormat(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaFormatDupl = new ArrayList();

        ArrayList arrayEuropeanaFormat = new ArrayList();
        String sFormat = "";


        String queryStringFormat = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + " select ?format where { \n"
                + "<" + resource + "> dc:format ?format.\n"
                + "}";



        //System.out.println("QUERY type Isidore-->" + queryStringFormat);



        TupleQuery tupleQuery_format = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringFormat);



        TupleQueryResult result_format = tupleQuery_format.evaluate();


        String formats = "";




        if (result_format.hasNext()) {
            while (result_format.hasNext()) {
                BindingSet bindingSet_format = result_format.next();

                if (bindingSet_format.getValue("format") != null) {

                    Value format = bindingSet_format.getValue("format");

                    formats = format.stringValue();

                    String formatFinale = new String(format.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaFormatDupl.add(formatFinale);


                }

            }
            arrayEuropeanaFormat = getListNotDuplicate(arrayEuropeanaFormatDupl);

        }
        return arrayEuropeanaFormat;
    }

    public static ArrayList getEuropeanaRelation(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayEuropeanaRelationDupl = new ArrayList();

        ArrayList arrayEuropeanaRelation = new ArrayList();
        String sRelation = "";



        String queryStringRelation = "PREFIX dct:<http://purl.org/dc/terms/>\n"
                + " PREFIX luc: <http://www.ontotext.com/owlim/lucene#>\n"
                + "PREFIX ore: <http://www.openarchives.org/ore/terms/>\n"
                + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
                + " select ?relation where { \n"
                + "<" + resource + "> dc:relation ?relation.\n"
                + "}";


        TupleQuery tupleQuery_relation = EuropeanaConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringRelation);



        TupleQueryResult result_relation = tupleQuery_relation.evaluate();



        String relations = "";



        if (result_relation.hasNext()) {
            while (result_relation.hasNext()) {
                BindingSet bindingSet_relation = result_relation.next();

                if (bindingSet_relation.getValue("relation") != null) {

                    Value relation = bindingSet_relation.getValue("relation");

                    relations = relation.stringValue();

                    String relationFinale = new String(relation.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayEuropeanaRelationDupl.add(relationFinale);


                }

            }
            arrayEuropeanaRelation = getListNotDuplicate(arrayEuropeanaRelationDupl);

        }
        return arrayEuropeanaRelation;
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

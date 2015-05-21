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
public class QueryIsidore {

    public static RepositoryConnection IsidoreConnection;
    public static ArrayList arrayIsidoreTitles;
    public static boolean isidoreServiceAvailable;

    static public RepositoryConnection ConnectionToIsidore(String IsidoreEndPoint) throws RepositoryException {
        //String endpointURL = "http://rechercheisidore.fr/sparql/";





        Repository myRepository = new HTTPRepository(IsidoreEndPoint, "");

        myRepository.initialize();

        IsidoreConnection = myRepository.getConnection();

        return IsidoreConnection;

    }

    static public int testEndPoint(String IsidoreEndPoint) throws MalformedURLException, IOException {
        //URL url = new URL("http://rechercheisidore.fr/sparql/");
        URL url = new URL(IsidoreEndPoint);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS  ISIDORE URL--->" + statusCode);

        return statusCode;
    }

    public static ArrayList queryIsidoreResource(String search_word, String numPage, int numberRecords, String IsidoreEndPoint) throws MalformedURLException, IOException {
        ArrayList arrayIsidoreResourceDupl = new ArrayList();
        ArrayList arrayIsidoreResource = new ArrayList();
        int statusCode = testEndPoint(IsidoreEndPoint);
        if (statusCode != -1) {
            try {
                ConnectionToIsidore(IsidoreEndPoint);

                String word = "' " + search_word + " '";

                String bif_word = " \"' " + search_word + " '\"";

                int page = Integer.parseInt(numPage);



                int numOffset = (page - 1) * numberRecords;
                int numberFinal = numberRecords * page;



//                String queryString = ""
//                        + "SELECT distinct ?s  WHERE {"
//                        +"?s dc:title ?title."
//                       + "FILTER regex(?title, "+word+",'i')."
//                        + "}limit "+numberFinal;
                String queryString = "";

                if (search_word.contains(":")) {

                    String[] splitSword = search_word.split(":");
                    String field = splitSword[0];

                    if (field.equals(SemanticQuery.search_filter.author.toString()) || field.equals(SemanticQuery.search_filter.format.toString()) || field.equals(SemanticQuery.search_filter.type.toString()) || field.equals(SemanticQuery.search_filter.publisher.toString()) || field.equals(SemanticQuery.search_filter.subject.toString())) {

                        SemanticQuery.search_filter wordFilter = SemanticQuery.search_filter.valueOf(field);

                        System.out.println("wordFilter ISIDORE =>>>>>>" + wordFilter);
                        String s = splitSword[1];
                        String search = "'" + s + "'";


                        switch (wordFilter) {
                            case author:

                                queryString = ""
                                        + " PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                                        + "SELECT distinct ?s \n"
                                        + "WHERE {\n "
                                        + "?s dc-term:creator ?creator.\n"
                                        + "?creator rdfs:label ?author.\n"
                                        + "FILTER regex(?author, " + search + ",'i').\n"
                                        + "}limit " + numberFinal;
                                break;
                            case subject:
                                queryString = ""
                                        + " PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                                        + "SELECT distinct ?s \n"
                                        + "WHERE {\n "
                                        + "?s dc:subject ?subject.\n"
                                        + "FILTER regex(?subject, " + search + ",'i').\n"
                                        + "}limit " + numberFinal;
                            case type:
                                queryString = ""
                                        + " PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                                        + "SELECT distinct ?s \n"
                                        + "WHERE {\n "
                                        + "?s dc:type ?type.\n"
                                        + "FILTER regex(?type, " + search + ",'i').\n"
                                        + "}limit " + numberFinal;

                                break;
                            case format:
                                queryString = ""
                                        + " PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                                        + "SELECT distinct ?s \n"
                                        + "WHERE {\n "
                                        + "?s dc:format ?format.\n"
                                        + "FILTER regex(?format, " + search + ",'i').\n"
                                        + "}limit " + numberFinal;
                                break;
                            case publisher:
                                queryString = ""
                                        + " PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                                        + "SELECT distinct ?s \n"
                                        + "WHERE {\n "
                                        + "?s dc:publisher ?publisher.\n"
                                        + "FILTER regex(?publisher, " + search + ",'i').\n"
                                        + "}limit " + numberFinal;

                            default:

                                break;

                        }
                    } else {

                        queryString = ""
                                + "SELECT distinct ?s  WHERE {"
                                + "?s dc:title ?title."
                                + "?title bif:contains " + bif_word + "."
                                + "}limit " + numberFinal;
                    }

                } else {

                    queryString = ""
                            + "SELECT distinct ?s  WHERE {"
                            + "?s dc:title ?title."
                            + "?title bif:contains " + bif_word + "."
                            + "}limit " + numberFinal;
                }



                //System.out.println("QUERY Isidore: " + queryString);

                TupleQuery tupleQuery = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



                TupleQueryResult result = tupleQuery.evaluate();



                if (result.hasNext()) {
                    while (result.hasNext()) {


                        BindingSet bindingSet = result.next();
                        String resource = bindingSet.getValue("s").stringValue();

                        arrayIsidoreResourceDupl.add(resource);
                    }

                }



                arrayIsidoreResource = getListNotDuplicate(arrayIsidoreResourceDupl);
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);

                //arrayIsidoreResource.add("Exception");

                System.out.println("ISIDORE QueryEvaluationException");

            } catch (MalformedQueryException ex) {
                Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);
                arrayIsidoreResource.add("Exception");
                System.out.println(" ISIDORE MalformedQueryException");

            } catch (RepositoryException ex) {
                Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);
                arrayIsidoreResource.add("Exception");
                System.out.println(" ISIDORE RepositoryException");
            } finally {
                try {
                    IsidoreConnection.close();
                } catch (RepositoryException ex) {
                    Logger.getLogger(QueryIsidore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return arrayIsidoreResource;
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

    public static String getIsidoreTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayIsidoreTitleDupl = new ArrayList();
        arrayIsidoreTitles = new ArrayList();
        String sTitles = "";
        try {


            String queryStringTitle = "";





            queryStringTitle = "SELECT  ?title \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:title ?title.\n"
                    + "}";


            // System.out.println("QUERY TITLE Isidore-->"+queryStringTitle);



            TupleQuery tupleQuery_title = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();




            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();


                        String titleFinale = new String(title.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayIsidoreTitleDupl.add(titleFinale);

                    }

                }
                arrayIsidoreTitles = getListNotDuplicate(arrayIsidoreTitleDupl);
            } else {
                sTitles = "---";

            }
            result_title.close();




            for (int i = 0; i < arrayIsidoreTitles.size(); i++) {

                if (arrayIsidoreTitles.get(i).toString() != "") {
                    sTitles = sTitles + "##" + arrayIsidoreTitles.get(i).toString();
                }

            }



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTitles;
    }

    public static String getIsidoreAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayIsidoreAuthorsDupl = new ArrayList();

        ArrayList arrayIsidoreAuthors = new ArrayList();
        String sAuthors = "";
        try {


            String queryStringAuthors = "PREFIX dc-term:<http://purl.org/dc/terms/>\n"
                    + "SELECT ?author \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc-term:creator ?creator.\n"
                    + "?creator rdfs:label ?author.\n"
                    + "}";

            //System.out.println("QUERY CREATOR Isidore-->"+queryStringAuthors);



            TupleQuery tupleQuery_authors = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthors);



            TupleQueryResult result_authors = tupleQuery_authors.evaluate();




            String authors = "";

            if (result_authors.hasNext()) {
                while (result_authors.hasNext()) {
                    BindingSet bindingSet_authors = result_authors.next();

                    if (bindingSet_authors.getValue("author") != null) {

                        Value author = bindingSet_authors.getValue("author");

                        authors = author.stringValue();

                        String authorsFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayIsidoreAuthorsDupl.add(authorsFinale);


                    }

                }
                arrayIsidoreAuthors = getListNotDuplicate(arrayIsidoreAuthorsDupl);
            } else {
                sAuthors = "";
            }

            result_authors.close();


            for (int i = 0; i < arrayIsidoreAuthors.size(); i++) {
                if (arrayIsidoreAuthors.get(i).toString() != "") {
                    sAuthors = sAuthors + "##" + arrayIsidoreAuthors.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sAuthors;
    }

    public static String getIsidoreDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayIsidoreDescriptionDupl = new ArrayList();

        ArrayList arrayIsidoreDescrtiption = new ArrayList();
        String sDescriptions = "";
        try {


            String queryStringDescriptions = ""
                    + "SELECT ?desc \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:description ?desc.\n"
                    + "}";

            //System.out.println("QUERY DESCRIPTION Isidore-->"+queryStringDescriptions);



            TupleQuery tupleQuery_description = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescriptions);



            TupleQueryResult result_description = tupleQuery_description.evaluate();


            String descriptions = "";



            if (result_description.hasNext()) {
                while (result_description.hasNext()) {
                    BindingSet bindingSet_description = result_description.next();

                    if (bindingSet_description.getValue("desc") != null) {

                        Value description = bindingSet_description.getValue("desc");

                        descriptions = description.stringValue();

                        String descrFinale = new String(description.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayIsidoreDescriptionDupl.add(descrFinale);


                    }

                }
                arrayIsidoreDescrtiption = getListNotDuplicate(arrayIsidoreDescriptionDupl);
            } else {
                sDescriptions = "";


            }

            result_description.close();

            if (arrayIsidoreDescrtiption.size() > 0) {
                for (int i = 0; i < arrayIsidoreDescrtiption.size(); i++) {


                    sDescriptions = sDescriptions + "##" + arrayIsidoreDescrtiption.get(i).toString();

                }
            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDescriptions;
    }

    public static String getIsidoreIdentifier(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayIsidoreIdDupl = new ArrayList();

        ArrayList arrayIsidoreId = new ArrayList();
        String sId = "";
        try {


            String queryStringId = ""
                    + "SELECT ?id \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:identifier ?id.\n"
                    + "}";

            //System.out.println("QUERY IDENTIFIER Isidore-->"+queryStringId);



            TupleQuery tupleQuery_id = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringId);



            TupleQueryResult result_id = tupleQuery_id.evaluate();




            String ids = "";


            if (result_id.hasNext()) {
                while (result_id.hasNext()) {
                    BindingSet bindingSet_id = result_id.next();

                    if (bindingSet_id.getValue("id") != null) {

                        Value description = bindingSet_id.getValue("id");

                        ids = description.stringValue();



                        String idFinale = new String(description.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayIsidoreIdDupl.add(idFinale);

                    }

                }
                arrayIsidoreId = getListNotDuplicate(arrayIsidoreIdDupl);
            } else {
                sId = "";
            }

            result_id.close();


            for (int i = 0; i < arrayIsidoreId.size(); i++) {
                if (arrayIsidoreId.get(i).toString() != "") {
                    sId = sId + "##" + arrayIsidoreId.get(i).toString();
                }

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sId;
    }

    public static ArrayList getIsidorePublisher(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidorePublisherDupl = new ArrayList();

        ArrayList arrayIsidorePublisher = new ArrayList();
        String sPublisher = "";



        String queryStringPublisher = ""
                + "SELECT ?publisher \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:publisher ?publisher.\n"
                + "}";

        //System.out.println("QUERY PUBLISHER Isidore-->"+queryStringPublisher);



        TupleQuery tupleQuery_publisher = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringPublisher);



        TupleQueryResult result_publisher = tupleQuery_publisher.evaluate();




        String publishers = "";




        if (result_publisher.hasNext()) {
            while (result_publisher.hasNext()) {
                BindingSet bindingSet_publisher = result_publisher.next();

                if (bindingSet_publisher.getValue("publisher") != null) {

                    Value publisher = bindingSet_publisher.getValue("publisher");

                    publishers = publisher.stringValue();



                    String publisherFinale = new String(publisher.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidorePublisherDupl.add(publisherFinale);


                }

            }
            arrayIsidorePublisher = getListNotDuplicate(arrayIsidorePublisherDupl);

        }
        return arrayIsidorePublisher;
    }

    public static ArrayList getIsidoreSubject(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreSubjectDupl = new ArrayList();

        ArrayList arrayIsidoreSubject = new ArrayList();
        String sSubject = "";



        String queryStringSubject = ""
                + "SELECT ?subject \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:subject ?subject.\n"
                + "}";


        TupleQuery tupleQuery_Subject = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSubject);



        TupleQueryResult result_Subject = tupleQuery_Subject.evaluate();



        String subjects = "";




        if (result_Subject.hasNext()) {
            while (result_Subject.hasNext()) {
                BindingSet bindingSet_Subject = result_Subject.next();

                if (bindingSet_Subject.getValue("subject") != null) {

                    Value subject = bindingSet_Subject.getValue("subject");

                    subjects = subject.stringValue();


                    String subjectFinale = new String(subject.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreSubjectDupl.add(subjectFinale);


                }

            }
            arrayIsidoreSubject = getListNotDuplicate(arrayIsidoreSubjectDupl);





        }
        return arrayIsidoreSubject;
    }

    public static ArrayList getIsidoreSource(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreSourceDupl = new ArrayList();

        ArrayList arrayIsidoreSource = new ArrayList();
        String sSource = "";


        String queryStringSource = ""
                + "SELECT ?source \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:source ?source.\n"
                + "}";

        //System.out.println("QUERY source Isidore-->"+queryStringSource);



        TupleQuery tupleQuery_Source = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSource);



        TupleQueryResult result_Source = tupleQuery_Source.evaluate();




        String sources = "";



        if (result_Source.hasNext()) {
            while (result_Source.hasNext()) {
                BindingSet bindingSet_Source = result_Source.next();

                if (bindingSet_Source.getValue("source") != null) {

                    Value source = bindingSet_Source.getValue("source");

                    sources = source.stringValue();



                    String sourceFinale = new String(source.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreSourceDupl.add(sourceFinale);


                }

            }
            arrayIsidoreSource = getListNotDuplicate(arrayIsidoreSourceDupl);

        }
        return arrayIsidoreSource;
    }

    public static ArrayList getIsidoreLanguage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreLanguageDupl = new ArrayList();

        ArrayList arrayIsidoreLanguage = new ArrayList();
        String sLanguage = "";


        String queryStringLanguage = ""
                + "SELECT ?language \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:language ?language.\n"
                + "}";

        //System.out.println("QUERY language Isidore-->"+queryStringLanguage);



        TupleQuery tupleQuery_language = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringLanguage);



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

    public static ArrayList getIsidoreDate(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreDateDupl = new ArrayList();

        ArrayList arrayIsidoreDate = new ArrayList();
        String sDtae = "";


        String queryStringDate = ""
                + "SELECT ?date \n"
                + "WHERE {\n "
                + "<" + resource + "> dc:date ?date.\n"
                + "}";


        TupleQuery tupleQuery_date = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDate);



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

    public static ArrayList getIsidoreContributor(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreContributorDupl = new ArrayList();

        ArrayList arrayIsidoreContributor = new ArrayList();
        String sContributors = "";


        String queryStringContributor = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + " select ?namec where { \n"
                + "<" + resource + "> dc-term:contributor ?co.\n"
                + " ?co rdfs:label ?namec.\n"
                + "}";



        //System.out.println("QUERY contributor Isidore-->"+queryStringContributor);



        TupleQuery tupleQuery_contributor = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringContributor);



        TupleQueryResult result_contributor = tupleQuery_contributor.evaluate();




        String dates = "";




        if (result_contributor.hasNext()) {
            while (result_contributor.hasNext()) {
                BindingSet bindingSet_contributor = result_contributor.next();

                if (bindingSet_contributor.getValue("contributor") != null) {

                    Value date = bindingSet_contributor.getValue("contributor");

                    dates = date.stringValue();

                    String contributorFinale = new String(date.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreContributorDupl.add(contributorFinale);


                }

            }
            arrayIsidoreContributor = getListNotDuplicate(arrayIsidoreContributorDupl);





        }
        return arrayIsidoreContributor;
    }

    public static ArrayList getIsidoreRights(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreRightsDupl = new ArrayList();

        ArrayList arrayIsidoreRights = new ArrayList();
        String sRights = "";


        String queryStringrRights = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + " select ?rights where { \n"
                + "<" + resource + "> dc:rights ?rights.\n"
                + "}";



        //System.out.println("QUERY rights Isidore-->"+queryStringrRights);



        TupleQuery tupleQuery_rights = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringrRights);



        TupleQueryResult result_rights = tupleQuery_rights.evaluate();




        String rights = "";



        if (result_rights.hasNext()) {
            while (result_rights.hasNext()) {
                BindingSet bindingSet_rights = result_rights.next();

                if (bindingSet_rights.getValue("rights") != null) {

                    Value right = bindingSet_rights.getValue("rights");

                    rights = right.stringValue();

                    String rightFinale = new String(right.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreRightsDupl.add(rightFinale);


                }

            }
            arrayIsidoreRightsDupl = getListNotDuplicate(arrayIsidoreRightsDupl);





        }
        return arrayIsidoreRightsDupl;
    }

    public static String getIsidoreCoverage(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {


        String sCoverage = "";



        String queryStringCoverage = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
                + "select *\n"
                + "where {\n"
                + "<" + resource + "> dc-term:coverage ?coverage.\n"
                + "?coverage skos:prefLabel ?labelcoverage.\n"
                + " ?coverage geo:lat ?latitude_coverage.\n"
                + " ?coverage geo:ong ?langitude_coverage.\n"
                + "}";






        //System.out.println("QUERY contributor Isidore-->"+queryStringCoverage);



        TupleQuery tupleQuery_coverage = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringCoverage);



        TupleQueryResult result_coverage = tupleQuery_coverage.evaluate();



        String coverages = "";
        String label_coverage = "";
        String latitude_coverage = "";
        String longitude_coverage = "";




        if (result_coverage.hasNext()) {
            while (result_coverage.hasNext()) {
                BindingSet bindingSet_coverage = result_coverage.next();

                if (bindingSet_coverage.getValue("coverage") != null) {

                    Value coverage = bindingSet_coverage.getValue("coverage");
                    label_coverage = bindingSet_coverage.getValue("labelcoverage").stringValue();
                    latitude_coverage = bindingSet_coverage.getValue("latitudecoverage").stringValue();
                    longitude_coverage = bindingSet_coverage.getValue("longitudecoverage").stringValue();

                }

            }
            coverages = label_coverage + " latitude: " + latitude_coverage + "longitude: " + longitude_coverage;
        }
        return coverages;
    }

    public static ArrayList getIsidoreType(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreTypeDupl = new ArrayList();

        ArrayList arrayIsidoreType = new ArrayList();
        String sType = "";


        String queryStringType = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + " select ?type where { \n"
                + "<" + resource + "> dc:type ?type.\n"
                + "}";



        TupleQuery tupleQuery_type = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringType);



        TupleQueryResult result_type = tupleQuery_type.evaluate();



        String types = "";




        if (result_type.hasNext()) {
            while (result_type.hasNext()) {
                BindingSet bindingSet_type = result_type.next();

                if (bindingSet_type.getValue("type") != null) {

                    Value type = bindingSet_type.getValue("type");

                    types = type.stringValue();



                    String typeFinale = new String(type.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreTypeDupl.add(typeFinale);


                }

            }
            arrayIsidoreTypeDupl = getListNotDuplicate(arrayIsidoreTypeDupl);



        }
        return arrayIsidoreTypeDupl;
    }

    public static ArrayList getIsidoreFormat(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreFormatDupl = new ArrayList();

        ArrayList arrayIsidoreFormat = new ArrayList();
        String sFormat = "";


        String queryStringFormat = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + " select ?format where { \n"
                + "<" + resource + "> dc:format ?format.\n"
                + "}";



        //System.out.println("QUERY type Isidore-->"+queryStringFormat);



        TupleQuery tupleQuery_format = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringFormat);



        TupleQueryResult result_format = tupleQuery_format.evaluate();



        String formats = "";




        if (result_format.hasNext()) {
            while (result_format.hasNext()) {
                BindingSet bindingSet_format = result_format.next();

                if (bindingSet_format.getValue("format") != null) {

                    Value format = bindingSet_format.getValue("format");

                    formats = format.stringValue();


                    String formatFinale = new String(format.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreFormatDupl.add(formatFinale);


                }

            }
            arrayIsidoreFormat = getListNotDuplicate(arrayIsidoreFormatDupl);





        }
        return arrayIsidoreFormat;
    }

    public static ArrayList getIsidoreRelation(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UnsupportedEncodingException {

        ArrayList arrayIsidoreRelationDupl = new ArrayList();

        ArrayList arrayIsidoreRelation = new ArrayList();
        String sRelation = "";


        String queryStringRelation = ""
                + "PREFIX dc-term: <http://purl.org/dc/terms/>\n"
                + " select ?relation where { \n"
                + "<" + resource + "> dc:relation ?relation.\n"
                + "}";



        //System.out.println("QUERY type Isidore-->"+queryStringRelation);



        TupleQuery tupleQuery_relation = IsidoreConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringRelation);



        TupleQueryResult result_relation = tupleQuery_relation.evaluate();




        String relations = "";



        if (result_relation.hasNext()) {
            while (result_relation.hasNext()) {
                BindingSet bindingSet_relation = result_relation.next();

                if (bindingSet_relation.getValue("relation") != null) {

                    Value relation = bindingSet_relation.getValue("relation");

                    relations = relation.stringValue();


                    String relationFinale = new String(relation.stringValue().getBytes("iso-8859-1"), "utf-8");

                    arrayIsidoreRelationDupl.add(relationFinale);


                }

            }
            arrayIsidoreRelation = getListNotDuplicate(arrayIsidoreRelationDupl);





        }
        return arrayIsidoreRelation;
    }
}

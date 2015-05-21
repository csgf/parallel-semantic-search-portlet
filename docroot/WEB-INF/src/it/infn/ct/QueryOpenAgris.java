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
import org.openrdf.model.Namespace;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 *
 * @author grid
 */
public class QueryOpenAgris {

    public static RepositoryConnection OpenAgrisConnection;
    public static ArrayList arrayOpenAgrisResource;
    public static boolean openAgrisServiceAvailable;

    static public int testEndPoint(String OpenAgrisEndPoint) throws MalformedURLException {
        // URL url = new URL("http://202.45.142.113:10035/");
        URL url = new URL(OpenAgrisEndPoint);
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS URL OPENAGRIS--->" + statusCode);

        return statusCode;
    }

    static public RepositoryConnection ConnectionToOpenAgris(String OpenAgrisEndPoint) throws RepositoryException {
        //String endpointURL = "http://202.45.142.113:10035/";

        Repository myRepository = new HTTPRepository(OpenAgrisEndPoint, "agris");



        myRepository.initialize();



        OpenAgrisConnection = myRepository.getConnection();




        return OpenAgrisConnection;
    }

    public static ArrayList queryOpenAgrisResource(String search_word, String numPage, int numberRecords, String OpenAgrisEndPoint) throws MalformedURLException {

        TupleQuery tupleQuery = null;

        int statusCode = testEndPoint(OpenAgrisEndPoint);
        arrayOpenAgrisResource = new ArrayList();
System.out.println("SearchW OPENAGRIS=> "+search_word);
        if (statusCode != -1) {
            try {
                ArrayList arrayVirtuosoResourceDupl = new ArrayList();


                ConnectionToOpenAgris(OpenAgrisEndPoint);

                String word = "'" + search_word + "'";

                String queryString = "";


                int page = Integer.parseInt(numPage);

                int numOffset = (page - 1) * numberRecords;
                int numberFinal = numberRecords * page;

                

                if (search_word.contains(":")) {

                    String[] splitSword = search_word.split(":");
                    String field = splitSword[0];

                    if (field.equals(SemanticQuery.search_filter.author.toString()) || field.equals(SemanticQuery.search_filter.format.toString()) || field.equals(SemanticQuery.search_filter.type.toString()) || field.equals(SemanticQuery.search_filter.publisher.toString()) || field.equals(SemanticQuery.search_filter.subject.toString())) {

                        SemanticQuery.search_filter wordFilter = SemanticQuery.search_filter.valueOf(field);

                        System.out.println("wordFilter OPENAGRIS =>>>>>>" + wordFilter);



                        String s = splitSword[1];
                        String search = "'" + s + "'";




                        switch (wordFilter) {
                            case author:

                                queryString = ""
                                        + "select distinct ?s where { \n"
                                       +"?s <http://purl.org/ontology/bibo/authorList> ?a.\n"
                                        +"?a ?p ?name.\n"
                                        +"FILTER regex(?name, '" + s + "','i').\n"
                                        + "}limit " + numberFinal;

                                      
                                break;
                            case subject:
                                queryString = ""
                                        + "select distinct ?s \n"
                                        + "where{ \n"
                                        +"?s  dc:subject ?subject.\n"
                                        +"Filter regex(?subject, '"+ s +"','i').  "
                                        + "}limit " + numberFinal;
                            case type:
                                queryString = ""
                                        + "SELECT distinct ?s  WHERE {\n"
                                        +"?s rdf:type ?rdf_type.\n"
                                        +" Filter regex(?rdf_type,'"+ s +"' ,'i').\n"
                                        + "}limit " + numberFinal;

                                break;
//                            case format:
//                                queryString = ""
//                                        + "SELECT distinct ?homepage  WHERE {\n"
//                                        + "?resource dcterms:title ?title.\n"
//                                        + "?resource foaf:homepage ?homepage.\n"
//                                        + "FILTER regex(?title, '" + search_word + "','i').\n"
//                                        + "}limit " + numberFinal;
//                                break;
//                            case publisher:
//                                queryString = ""
//                                        + "select distinct ?homepage \n"
//                                        + "where{ \n"
//                                        + "?s foaf:homepage ?homepage.\n"
//                                        + "?s dcterms:publisher ?publisher.\n"
//                                        + "?publisher foaf:name ?publisher_name.\n"
//                                        + "FILTER regex(?publisher_name, '" + s + "','i').\n"
//                                        + "}limit " + numberFinal;

                            default:

                                break;
                        }

                    } else {

                        queryString = ""
                                + "SELECT distinct ?s  WHERE {"
                                + "?s dcterms:title ?title."
                                + "FILTER regex(?title, " + word + ",'i')."
                                + "}limit " + numberFinal;
                    }
                } else {



                    queryString = ""
                            + "SELECT distinct ?s  WHERE {"
                            + "?s dcterms:title ?title."
                            + "FILTER regex(?title, " + word + ",'i')."
                            + "}limit " + numberFinal;
                }

                System.out.println("QUERY OpenAgris: " + queryString);

                tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);




                TupleQueryResult result = null;


                result = tupleQuery.evaluate();
                if (result != null) {
                    if (result.hasNext()) {
                        while (result.hasNext()) {


                            BindingSet bindingSet = result.next();
                            String resource = bindingSet.getValue("s").stringValue();
                            arrayVirtuosoResourceDupl.add(resource);
                        }

                    }
                }



                result.close();
                arrayOpenAgrisResource = getListNotDuplicate(arrayVirtuosoResourceDupl);
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);

                arrayOpenAgrisResource.add("Exception");

                System.out.println("OPENAGRIS QueryEvaluationException");

            } catch (MalformedQueryException ex) {
                Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);
                arrayOpenAgrisResource.add("Exception");
                System.out.println(" OPENAGRIS MalformedQueryException");

            } catch (RepositoryException ex) {
                Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);
                arrayOpenAgrisResource.add("Exception");
                System.out.println(" OPENAGRIS RepositoryException");
            } finally {
                try {
                    OpenAgrisConnection.close();
                } catch (RepositoryException ex) {
                    Logger.getLogger(QueryOpenAgris.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



        }

        return arrayOpenAgrisResource;
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

    public static String getTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayOpenAgrisTitles = new ArrayList();
        String sTitles = "";
        try {
            //ConnectionToOpenAgris();

            String queryStringTitle = "";
            queryStringTitle = "SELECT distinct ?title \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:title ?title.\n"
                    + "}";


            System.out.println("QUERY TITLE OpenAgris-->" + queryStringTitle);



            TupleQuery tupleQuery_title = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();




            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

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

    public static String getAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayOpenAgrisAuthors = new ArrayList();
        String sAuthors = "";
        try {


            String queryStringAuthors = "SELECT   ?c \n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://purl.org/ontology/bibo/authorList> ?a.\n"
                    + "?a ?p ?c.\n"
                    + "}";

            System.out.println("QUERY CREATOR OpenAgris-->" + queryStringAuthors);



            TupleQuery tupleQuery_authors = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthors);



            TupleQueryResult result_authors = tupleQuery_authors.evaluate();




            if (result_authors.hasNext()) {
                while (result_authors.hasNext()) {
                    BindingSet bindingSet_authors = result_authors.next();

                    if (bindingSet_authors.getValue("c") != null) {

                        Value author = bindingSet_authors.getValue("c");


                        String authorsFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");

                        // String newAuthor=authorsFinale.split("Author:")[1];
                        if (!authorsFinale.contains("http")) {
                            arrayOpenAgrisAuthors.add(authorsFinale);
                        }


                    }

                }
            } else {
                sAuthors = "---";
            }

            result_authors.close();


            for (int i = 0; i < arrayOpenAgrisAuthors.size(); i++) {
                sAuthors = sAuthors + "##" + arrayOpenAgrisAuthors.get(i).toString();

            }


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sAuthors;
    }

    public static String getDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayOpenAgrisDescription = new ArrayList();
        String sDescription = "";
        try {


            String queryStringDescription = "";
            queryStringDescription = "SELECT ?desc \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:description ?desc.\n"
                    + "}";


            System.out.println("QUERY DESCRIPTION OpenAgris-->" + queryStringDescription);



            TupleQuery tupleQuery_description = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDescription);



            TupleQueryResult result_description = tupleQuery_description.evaluate();




            if (result_description.hasNext()) {
                while (result_description.hasNext()) {
                    BindingSet bindingSet_description = result_description.next();

                    if (bindingSet_description.getValue("desc") != null) {

                        Value desc = bindingSet_description.getValue("desc");



                        String descriptionFinale = new String(desc.stringValue().getBytes("iso-8859-1"), "utf-8");

                        arrayOpenAgrisDescription.add(descriptionFinale);

                        System.out.println("DESCRIPTION:-------> " + descriptionFinale);
                    }

                }
            } else {
                sDescription = "---";
            }
            result_description.close();




            for (int i = 0; i < arrayOpenAgrisDescription.size(); i++) {
                sDescription = sDescription + "##" + arrayOpenAgrisDescription.get(i).toString();

            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDescription;
    }

    public static String getDateSubmitted(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String date = "";

        try {

            String queryString = "SELECT ?o \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:dateSubmitted ?o.\n"
                    + "}";


            //System.out.println("QUERY TITLE OpenAgris-->" + queryString);



            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



            TupleQueryResult result = tupleQuery.evaluate();





            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    if (bindingSet.getValue("o") != null) {

                        Value o = bindingSet.getValue("o");


                        date = new String(o.stringValue().getBytes("iso-8859-1"), "utf-8");


                    }

                }
            } else {
                date = "---";
            }
            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;

    }

    public static ArrayList getPublisher(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String publisher = "";

        ArrayList listPublisher = new ArrayList();

        try {

            String queryString = "SELECT ?namePublisher \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:publisher ?o.\n"
                    + "?o foaf:name ?namePublisher.\n"
                    + "}";


            // System.out.println("QUERY PUBLISHER OpenAgris-->" + queryString);



            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    if (bindingSet.getValue("namePublisher") != null) {

                        Value namePublisher = bindingSet.getValue("namePublisher");

                        String name = new String(namePublisher.stringValue().getBytes("iso-8859-1"), "utf-8");

                        listPublisher.add(name);


                    }

                }
            } else {
                publisher = "---";
                listPublisher.add(publisher);
            }



            result.close();





        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listPublisher;

    }

    public static String getSubject(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String subject = "";



        try {

            String queryString = "SELECT ?subject \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:subject ?subject.\n"
                    + "}";


            //System.out.println("QUERY subject OpenAgris-->" + queryString);



            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



            TupleQueryResult result = tupleQuery.evaluate();







            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("subject") != null) {



                        subject = subject + "##" + bindingSet.getValue("subject").stringValue();

                    }
                }
            } else {
                subject = "---";
            }
            result.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subject;

    }

    public static String getRDFType(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String RDFtype = "";

        try {

            String queryString = "SELECT ?rdf_type \n"
                    + "WHERE {\n "
                    + "<" + resource + "> rdf:type ?rdf_type.\n"
                    + "}";


            //System.out.println("QUERY RDFTYPE OpenAgris-->" + queryString);



            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("rdf_type") != null) {

                        RDFtype = bindingSet.getValue("rdf_type").stringValue();
                        RDFtype = RDFtype.split("http://purl.org/ontology/bibo/")[1];
                    }


                }
            } else {
                RDFtype = "---";
            }
            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RDFtype;

    }

    public static String getdctermsType(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String dctermsType = "";

        try {

            String queryString = "SELECT ?dcterms_type \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:type ?dcterms_type.\n"
                    + "}";


            //System.out.println("QUERY TITLE OpenAgris-->" + queryString);



            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



            TupleQueryResult result = tupleQuery.evaluate();






            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("dcterms_type") != null) {

                        dctermsType = bindingSet.getValue("dcterms_type").stringValue();
                    }


                }
            } else {
                dctermsType = "---";
            }
            result.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dctermsType;
    }

    public static String getLanguage(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String lang = "";

        try {

            String queryString = "prefix bibo:<http://purl.org/ontology/bibo/>\n"
                    + "SELECT ?lang \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:language ?lang.\n"
                    + "}";


            //System.out.println("QUERY LANGUAGE OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("lang") != null) {

                        lang = bindingSet.getValue("lang").stringValue();
                    }
                }
            } else {
                lang = "---";
            }

            result.close();

            System.out.println("LANGUAGE--->" + lang);


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lang;
    }

    public static String getRight(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String right = "";

        try {

            String queryString = "SELECT ?right \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:rights ?right.\n"
                    + "}";


            //System.out.println("QUERY RIGHT OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("right") != null) {

                        right = bindingSet.getValue("right").stringValue();
                    }
                }
            } else {
                right = "---";
            }

            result.close();


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return right;
    }

    public static String getAlternative(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String alternative = "";

        try {

            String queryString = "SELECT ?alternative \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:alternative ?alternative.\n"
                    + "}";


            //System.out.println("QUERY Alternative OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("alternative") != null) {

                        alternative = bindingSet.getValue("alternative").stringValue();
                    }
                }
            } else {
                alternative = "---";
            }

            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alternative;
    }

    public static String getExtent(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String extent = "";

        try {

            String queryString = "SELECT ?extent \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:extent ?extent.\n"
                    + "}";


            //System.out.println("QUERY EXTENT OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("extent") != null) {

                        extent = bindingSet.getValue("extent").stringValue();
                    }
                }
            } else {
                extent = "---";
            }

            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return extent;
    }

    public static String getSource(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String source = "";

        try {

            String queryString = "SELECT ?source \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:source ?source.\n"
                    + "}";


            //System.out.println("QUERY source OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("source") != null) {

                        source = bindingSet.getValue("source").stringValue();
                    }
                }
            } else {
                source = "---";
            }

            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return source;
    }

    public static String getIsPartOf(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String isPartOf = "";

        try {

            String queryString = "SELECT ?isPartOf \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:isPartOf ?isPartOf.\n"
                    + "}";


            //System.out.println("QUERY isPartOf OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("isPartOf") != null) {

                        isPartOf = bindingSet.getValue("isPartOf").stringValue();
                    }
                }
            } else {
                isPartOf = "---";
            }

            result.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isPartOf;
    }

    public static String getIssued(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String issued = "";

        try {

            String queryString = "SELECT ?issued \n"
                    + "WHERE {\n "
                    + "<" + resource + "> dcterms:issued ?issued.\n"
                    + "}";


            // System.out.println("QUERY issued OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("issued") != null) {

                        issued = bindingSet.getValue("issued").stringValue();
                    }
                }
            } else {
                issued = "---";
            }

            result.close();


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return issued;
    }

    public static String getUri(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String uri = "";

        try {

            String queryString = "prefix bibo:<http://purl.org/ontology/bibo/>\n"
                    + "SELECT ?uri \n"
                    + "WHERE {\n "
                    + "<" + resource + "> bibo:uri ?uri.\n"
                    + "}";


            // System.out.println("QUERY uri OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("uri") != null) {

                        uri = bindingSet.getValue("uri").stringValue();
                    }
                }
            } else {
                uri = "---";
            }

            result.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uri;
    }

    public static String getPresentedAt(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String presentedAt = "";

        try {

            String queryString = "prefix bibo:<http://purl.org/ontology/bibo/>\n"
                    + "SELECT ?presentedAt \n"
                    + "WHERE {\n "
                    + "<" + resource + "> bibo:presentedAt ?presentedAt.\n"
                    + "}";


            //System.out.println("QUERY presentedAt OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("presentedAt") != null) {

                        presentedAt = bindingSet.getValue("presentedAt").stringValue();
                    }
                }
            } else {
                presentedAt = "---";
            }

            result.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return presentedAt;
    }

    public static String getAbstract(String resource) throws UnsupportedEncodingException, RepositoryException, MalformedQueryException {
        String biboAbstract = "";

        try {

            String queryString = "prefix bibo:<http://purl.org/ontology/bibo/>\n"
                    + "SELECT ?abstract \n"
                    + "WHERE {\n "
                    + "<" + resource + "> bibo:abstract ?abstract.\n"
                    + "}";


            //System.out.println("QUERY abstract OpenAgris-->" + queryString);

            TupleQuery tupleQuery = OpenAgrisConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    if (bindingSet.getValue("abstract") != null) {

                        biboAbstract = bindingSet.getValue("abstract").stringValue();
                        biboAbstract = new String(biboAbstract.getBytes("iso-8859-1"), "utf-8");
                    }
                }
            } else {
                biboAbstract = "---";
            }

            result.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return biboAbstract;
    }
}

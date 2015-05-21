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
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Value;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.event.RepositoryConnectionListener;
import org.openrdf.repository.http.HTTPRepository;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author ccarrubba
 */
public class SemanticQuery {

    public static ArrayList arrayVirtuoso;
    public static ArrayList arrayVirtuosoResource;
    public static ArrayList arrayResourceFromSubject;
    public static RepositoryConnection virtuosoConnection;
    public static ArrayList arrayTotalResourceByKeyword;
    public static String graph = "http://CHAIN-Reds_Test";
    public static boolean chainServiceAvailable;

    public enum search_filter {

        author,
        subject,
        type,
        format,
        publisher
    }

    public RepositoryConnection ConnectionToVirtuoso() throws RepositoryException {
        String endpointURL = "http://virtuoso.ct.infn.it:8890/sparql";
        // String endpointURL ="http://virtuoso.ct.infn.it:8896/chain-reds-kb/sparql";


        Repository myRepository = new HTTPRepository(endpointURL, "");

        myRepository.initialize();
        RepositoryConnection virtuosoConnection = myRepository.getConnection();

        return virtuosoConnection;
    }

    static public int testEndPoint() throws MalformedURLException {
        URL url = new URL("http://virtuoso.ct.infn.it:8890/sparql");
        HttpURLConnection http = null;
        int statusCode = 0;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("CATCH1");
            statusCode = -1;
        }

        try {
            statusCode = http.getResponseCode();
        } catch (IOException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("CATCH2");
            statusCode = -1;
        }
        System.out.println("STATUS CHAIN URL --->" + statusCode);

        return statusCode;
    }

    public int getNumRecords(String search_word) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        //RepositoryConnection con = ConnectionToVirtuoso();
        int numRecords = 0;

        RepositoryConnection virtuosoConnection = ConnectionToVirtuoso();

        String word = "' " + search_word + " '";
        ArrayList numRecConn = new ArrayList();




        String queryString = ""
                + "SELECT  count(distinct(?s)) as ?num FROM <" + graph + "> WHERE {"
                + "?s dc:title ?title."
                + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep."
                + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. "
                + "FILTER regex(str(?title), " + word + ")."
                + "}";

        System.out.println("QUERY: " + queryString);



        TupleQuery tupleQuery = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        System.out.println("Valutazione Query");




        TupleQueryResult result = tupleQuery.evaluate();



        System.out.println("VIRTUOSO GET NUM RECORDS query has next? " + result.hasNext() + "   " + queryString);
        int countTitle0 = 0;
        if (result.hasNext()) {
            while (result.hasNext()) {


                BindingSet bindingSet = result.next();
                numRecords = Integer.parseInt(bindingSet.getValue("num").stringValue());

            }


            System.out.println("NUMERO RECORDS: " + numRecords);

        }

        return numRecords;

    }

    public int getNumTotalRecords() throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        //RepositoryConnection con = ConnectionToVirtuoso();
        RepositoryConnection virtuosoConnection = ConnectionToVirtuoso();
        int numTotRecords = 0;

        String queryString = ""
                + "SELECT count(?s) as ?num FROM <" + graph + "> WHERE {"
                + "?s rdf:type <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#Resource>."
                + "}";


        TupleQuery tupleQuery = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



        TupleQueryResult result = tupleQuery.evaluate();


        if (result.hasNext()) {
            while (result.hasNext()) {


                BindingSet bindingSet = result.next();
                numTotRecords = Integer.parseInt(bindingSet.getValue("num").stringValue());
            }

        }

        System.out.println("NUMEROOOOOOOOOO " + numTotRecords);




        return numTotRecords;

    }

    public ArrayList queryVirtuosoResource(String search_word, String numPage, int numberRecords) {
        try {
            ArrayList arrayVirtuosoResourceDupl = new ArrayList();
            arrayVirtuosoResource = new ArrayList();

            int statusCode = testEndPoint();

            if (statusCode != -1) {
                try {
                    virtuosoConnection = ConnectionToVirtuoso();

                    //String bif_word = " \"' " + search_word + " '\"";




                    String bif_word = "";


                    String test_word = "";
                    if (search_word.contains(" ")) {
                        //  String newSearch_word=search_word.replace("  ", " ");

                        String[] word_split = search_word.split(" ");
                        if (word_split.length > 1) {
                            for (int i = 0; i < word_split.length; i++) {

                                // se facendo lo split tra le parole, la singola parola non è uguale alla stringa vuota
                                // e quindi non ha lunghezza pari a zero
                                if (word_split[i].length() != 0) {




                                    // System.out.println("PAROLA:--->"+word_split[i]);

                                    String singleWord = word_split[i];

                                    //se la singola parola è un numero vanno aggiunti i singoli apici
                                    if (isInteger(singleWord) || isDouble(singleWord)) {
                                        singleWord = "'" + singleWord + "'";
                                    }

                                    //se siamo all'ultima parola
                                    if (i == word_split.length - 1) {
                                        test_word += singleWord;
                                    } else {
                                        test_word += singleWord + " AND ";
                                    }
                                }
                            }
                            bif_word = "\"" + test_word + "\"";
                        }
                    } else {
                        bif_word = "\"'" + search_word + "'\"";
                    }


                    System.out.println("PAROLA 2 PER BIFCONTAINS: " + bif_word);



                    //String word = "' " + search_word.toUpperCase() + " '";

                    int page = Integer.parseInt(numPage);




                    int numOffset = (page - 1) * numberRecords;
                    int numberFinal = numberRecords * page;

                    String queryString = "";

                    if (search_word.contains(":")) {


                        String[] splitSword = search_word.split(":");
                        String field = splitSword[0];


                        if (field.equals(search_filter.author.toString()) || field.equals(search_filter.format.toString()) || field.equals(search_filter.type.toString()) || field.equals(search_filter.publisher.toString()) || field.equals(search_filter.subject.toString())) {

                            search_filter wordFilter = search_filter.valueOf(field);

                            System.out.println("wordFilter=>>>>>>" + wordFilter);

                            String s = splitSword[1];
                            String search = "'" + s + "'";
                            String search_uppercase = "' " + s.toUpperCase() + " '";



                            switch (wordFilter) {
                                case author:


                                    System.out.println("STRINGA AUTOREEEE" + search.replace("", " "));
//                                    queryString = ""
//                                            + "SELECT ?s FROM <" + graph + ">\n"
//                                            + "WHERE {\n"
//                                            + "?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
//                                            + "FILTER regex(str(?author), " + search + ").\n"
//                                            + "}limit " + numberFinal;


                                    String ss = search.replace("", " ");
                                    queryString = ""
                                            + "SELECT ?s FROM <" + graph + ">\n"
                                            + "WHERE {\n"
                                            + "{"
                                            + "?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
                                            + "?author foaf:name ?name.\n"
                                            + "?name bif:contains \"" + ss + "\".\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
                                            + "}\n"
                                            + "union\n"
                                            + "{"
                                            + "?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
                                            + "?author foaf:name ?name.\n"
                                            + "?name bif:contains \"" + search + "\".\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
                                            + "}"
                                            + "}ORDER BY ASC(?rank)limit " + numberFinal;
                                    break;
                                case subject:
//                                    queryString = ""
//                                            + " SELECT distinct ?s FROM <" + graph + ">  \n"
//                                            + "WHERE {\n"
//                                            + " ?s dc:subject ?subject. \n"
//                                            + " filter regex(str(?subject), " + search + ").\n"
//                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
//                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
//                                            + " }limit " + numberFinal;
                                    queryString = ""
                                            + " SELECT distinct ?s FROM <" + graph + ">  \n"
                                            + "WHERE {\n"
                                            + " ?s dc:subject ?subject. \n"
                                            + " ?subject bif:contains \"" + search + "\".\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
                                            + " }ORDER BY ASC(?rank) limit " + numberFinal;
                                    break;
                                case type:
//                                    queryString = ""
//                                            + " SELECT distinct ?s FROM <" + graph + "> \n"
//                                            + " WHERE { \n"
//                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasDataSet> ?dataset. \n"
//                                            + " ?dataset dc:type ?type.\n"
//                                            + " Filter regex(str(?type), " + search + ").\n"
//                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
//                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
//                                            + " }limit " + numberFinal;

                                    queryString = ""
                                            + " SELECT distinct ?s FROM <" + graph + ">  \n"
                                            + "WHERE {\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasDataSet> ?dataset. \n"
                                            + " ?dataset dc:type ?type.\n"
                                            + " ?type bif:contains \"" + search + "\".\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
                                            + " }ORDER BY ASC(?rank) limit " + numberFinal;
                                    break;
                                case format:

//                                    queryString = ""
//                                            + " SELECT distinct ?s FROM <" + graph + "> \n"
//                                            + " WHERE { \n"
//                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasDataSet> ?dataset. \n"
//                                            + " ?dataset dc:format ?format.\n"
//                                            + " Filter regex(str(?format), " + search + ").\n"
//                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
//                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
//                                            + " }limit " + numberFinal;


                                    queryString = ""
                                            + " SELECT distinct ?s FROM <" + graph + ">  \n"
                                            + "WHERE {\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasDataSet> ?dataset. \n"
                                            + " ?dataset dc:format ?format.\n"
                                            + " ?format bif:contains \"" + search + "\".\n"
                                            + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep. \n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. \n"
                                            + " }ORDER BY ASC(?rank) limit " + numberFinal;
                                    //+ " } limit " + numberFinal;
                                    break;
                                case publisher:
//                                    queryString = "prefix myOnto:<http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#>"
//                                            + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
//                                            + "SELECT distinct ?s FROM <" + graph + ">  WHERE {\n"
//                                            + "?s dc:publisher ?pub.\n"
//                                            + "FILTER (CONTAINS ( UCASE(str(?pub)), " + search_uppercase + ")).\n"
//                                            + "?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
//                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. "
//                                            + "}ORDER BY ASC(?rank) limit " + numberFinal;
                                    queryString = "prefix myOnto:<http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#>"
                                            + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
                                            + "SELECT distinct ?s FROM <" + graph + ">  WHERE {\n"
                                            + "?s dc:publisher ?pub.\n"
                                            + "?pub bif:contains \"" + search + "\".\n"
                                            + "?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
                                            + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. "
                                            + "}ORDER BY ASC(?rank) limit " + numberFinal;

                                default:

                                    break;

                            }
                        } else {
                            queryString = ""
                                    + "SELECT distinct ?s  FROM <" + graph + "> WHERE {"
                                    + "?s dc:title ?title."
                                    + "?title bif:contains " + bif_word + "."
                                    + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep."
                                    + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. "
                                    + "}ORDER BY ASC(?rank) limit " + numberFinal;
                        }




                    } else {
                        queryString = ""
                                + "SELECT distinct ?s  FROM <" + graph + "> WHERE {"
                                + "?s dc:title ?title."
                                + "?title bif:contains " + bif_word + "."
                                + " ?s <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep."
                                + " ?rep <http://www.semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#rank> ?rank. "
                                + "}ORDER BY ASC(?rank) limit " + numberFinal;
                    }


                    System.out.println("QUERY CHAIN: " + queryString);

                    TupleQuery tupleQuery = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);



                    TupleQueryResult result = tupleQuery.evaluate();




                    if (result.hasNext()) {
                        while (result.hasNext()) {


                            BindingSet bindingSet = result.next();
                            String resource = bindingSet.getValue("s").stringValue();

                            arrayVirtuosoResourceDupl.add(resource);
                        }

                    }


                    arrayVirtuosoResource = getListNotDuplicate(arrayVirtuosoResourceDupl);

                } catch (QueryEvaluationException ex) {
                    Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);

                    //arrayVirtuosoResource.add("Exception");

                    System.out.println("CHAIN QueryEvaluationException" + ex.getLocalizedMessage());

                } catch (MalformedQueryException ex) {
                    Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
                    arrayVirtuosoResource.add("Exception");
                    System.out.println(" CHAIN MalformedQueryException");

                } catch (RepositoryException ex) {
                    Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
                    arrayVirtuosoResource.add("Exception");
                    System.out.println(" CHAIN RepositoryException");
                } finally {
                    try {
                        virtuosoConnection.close();
                    } catch (RepositoryException ex) {
                        Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }


        } catch (MalformedURLException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoResource;
    }

    public ArrayList getTitle2(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayVirtuosoTitles = new ArrayList();
        try {
            //RepositoryConnection con = ConnectionToVirtuoso();

            virtuosoConnection = ConnectionToVirtuoso();
            String queryStringTitle = ""
                    + "SELECT ?title FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:title ?title.\n"
                    + "}";

            TupleQuery tupleQuery_title = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();


            //System.out.println("QUERY TITLE HAS NEXT? " + result_title.hasNext() + "   " + queryStringTitle);

            String titles = "";

            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();

                        //byte [] titlesByte= titles.getBytes("UTF-8");
                        String titleFinale = new String(title.stringValue().getBytes("iso-8859-1"), "utf-8");
                        // String titleFinale=new String(titlesByte) ;
                        arrayVirtuosoTitles.add(titleFinale);
                        // System.out.println("TITLE: "+titleFinale);

                    }

                }
            }


            result_title.close();
            // con.close();


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoTitles;
    }

    public String getTitle(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayVirtuosoTitles = new ArrayList();
        String sTitle = "";
        try {
            //RepositoryConnection con = ConnectionToVirtuoso();
            virtuosoConnection = ConnectionToVirtuoso();

            String queryStringTitle = ""
                    + "SELECT distinct ?title FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:title ?title.\n"
                    + "}";

            TupleQuery tupleQuery_title = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringTitle);



            TupleQueryResult result_title = tupleQuery_title.evaluate();


            //System.out.println("QUERY TITLE HAS NEXT? " + result_title.hasNext() + "   " + queryStringTitle);


            String titles = "";
            if (result_title.hasNext()) {
                while (result_title.hasNext()) {
                    BindingSet bindingSet_title = result_title.next();

                    if (bindingSet_title.getValue("title") != null) {

                        Value title = bindingSet_title.getValue("title");

                        titles = title.stringValue();

                        //byte [] titlesByte= titles.getBytes("UTF-8");
                        String titleFinale = new String(title.stringValue().getBytes("iso-8859-1"), "utf-8");
                        // String titleFinale=new String(titlesByte) ;
                        arrayVirtuosoTitles.add(titleFinale);
                        // System.out.println("TITLE: "+titleFinale);

                    }

                }
            } else {
                sTitle = "---";
            }

            arrayVirtuosoTitles = getListNotDuplicate(arrayVirtuosoTitles);


            for (int i = 0; i < arrayVirtuosoTitles.size(); i++) {
                sTitle = sTitle + "##" + arrayVirtuosoTitles.get(i).toString();

            }




            result_title.close();
            // con.close();


        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sTitle;
    }

    public ArrayList getAuthors2(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoAuthors = new ArrayList();
        try {


            // RepositoryConnection con = ConnectionToVirtuoso();

            virtuosoConnection = ConnectionToVirtuoso();
            String queryStringAuthor = ""
                    + "SELECT ?nameAuthor FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
                    + "?author foaf:name ?nameAuthor.\n "
                    + "}";

            TupleQuery tupleQuery_author = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthor);



            TupleQueryResult result_author = tupleQuery_author.evaluate();


            //System.out.println("AUTHOR has next?" + result_author.hasNext() + "   " + queryStringAuthor);

            String authors = "";

            if (result_author.hasNext()) {
                while (result_author.hasNext()) {
                    BindingSet bindingSet_author = result_author.next();




                    if (bindingSet_author.getValue("nameAuthor") != null) {
                        Value author = bindingSet_author.getValue("nameAuthor");
                        //author.stringValue().replace(" ", "");
                        String outhorFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");
                        //authors += outhorFinale.replace(" ", "").replace(",", " ") + "; ";
                        authors += outhorFinale.replace(" ", "").replace(",", " ") + "; ";

                        //  System.out.println("AUTHOR query" + authors);
                    }


                }
                arrayVirtuosoAuthors.add(authors.substring(0, authors.length() - 2));

            } else {
                authors = null;
            }



            //con.close();

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoAuthors;
    }

//    public String getAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
//        // ArrayList arrayVirtuosoAuthors = new ArrayList();
//        String sAuthors = "";
//        try {
//
//            virtuosoConnection = ConnectionToVirtuoso();
//            // RepositoryConnection con = ConnectionToVirtuoso();
//
//
//            String queryStringAuthor = ""
//                    + "SELECT distinct ?nameAuthor FROM <" + graph + ">\n"
//                    + "WHERE {\n "
//                    + "<" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
//                    + "?author foaf:name ?nameAuthor.\n "
//                    + "}";
//
//            TupleQuery tupleQuery_author = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthor);
//
//
//
//            TupleQueryResult result_author = tupleQuery_author.evaluate();
//
//
//            //System.out.println("AUTHOR has next?" + result_author.hasNext() + "   " + queryStringAuthor);
//
//            String authors = "";
//
//            if (result_author.hasNext()) {
//                while (result_author.hasNext()) {
//                    BindingSet bindingSet_author = result_author.next();
//
//
//
//
//                    if (bindingSet_author.getValue("nameAuthor") != null) {
//                        Value author = bindingSet_author.getValue("nameAuthor");
//                        //author.stringValue().replace(" ", "");
//                        String outhorFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");
//                        //authors += outhorFinale.replace(" ", "").replace(",", " ") + "; ";
//                        authors += outhorFinale.replace(" ", "").replace(",", " ") + "; ";
//
//                        //  System.out.println("AUTHOR query" + authors);
//                    }
//
//
//                }
//                //arrayVirtuosoAuthors.add(authors.substring(0, authors.length() - 2));
//                sAuthors = authors;
//
//            } else {
//                sAuthors = "---";
//            }
//
//
//
//            //con.close();
//
//        } catch (QueryEvaluationException ex) {
//            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return sAuthors;
//    }
    
    
    
    
    public  String getAuthors(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
       // String arrayVirtuosoAuthors = new ArrayList();
        
        String authors = "";
        try {

            boolean controlRep=controlRepOpenAccess(resource);
            


            String queryStringAuthor = ""
                    + "SELECT distinct ?nameAuthor FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#hasAuthor> ?author.\n"
                    + "?author foaf:name ?nameAuthor.\n "
                    + "}";

            TupleQuery tupleQuery_author = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringAuthor);



            TupleQueryResult result_author = tupleQuery_author.evaluate();




            

            if (result_author.hasNext()) {
                while (result_author.hasNext()) {
                    BindingSet bindingSet_author = result_author.next();




                    if (bindingSet_author.getValue("nameAuthor") != null) {
                        Value author = bindingSet_author.getValue("nameAuthor");

                        String authorFinale = new String(author.stringValue().getBytes("iso-8859-1"), "utf-8");

                        //authors += outhorFinale.replace(" ", "").replace(",", " ") + "; ";
                        
                        
                        
                        
                        if(controlRep)
                        {  
                        
                        if(authorFinale.contains("()"))
                            authorFinale=authorFinale.replace("()","");
                        
                        authors += authorFinale.replace(",", " ") + "; ";
                        }
                        else
                           authors += authorFinale.replace(" ", "").replace(",", " ") + "; "; 

                    }


                }
                //arrayVirtuosoAuthors.add(authors.substring(0, authors.length() - 2));

            } else {
                authors = "---";
            }





        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return authors;
    }
    
    
    public static boolean controlRepOpenAccess(String resource){
        
        boolean control=false;
        
        try {


            String controlQueryString = ""
                    + "SELECT ?rep FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
                    + "}";
            
            
           // System.out.println("controlQueryString-->"+controlQueryString);

            TupleQuery tupleQuery= virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, controlQueryString);



            TupleQueryResult result = tupleQuery.evaluate();



            if (result.hasNext()) {
                while (result.hasNext()) {
                    BindingSet bindingSet_author = result.next();




                    if (bindingSet_author.getValue("rep") != null) {
                        Value rep = bindingSet_author.getValue("rep");

                        String allNameRepository=rep.stringValue();
                        String idRep=allNameRepository.split("http://openDocuments/repository/Repository_")[1];
                        
                        if(idRep.equals("1309"))
                            control=true;

                        


                    }


                }
                
            } 





        }  catch (RepositoryException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return control;
        
        
    }

    public ArrayList getDescription2(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoDescriptions = new ArrayList();
        try {


            virtuosoConnection = ConnectionToVirtuoso();
            //RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringDesciption = ""
                    + "SELECT ?desc FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:description ?desc.\n"
                    + "}";

            TupleQuery tupleQuery_desc = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDesciption);



            TupleQueryResult result_desc = tupleQuery_desc.evaluate();


            // System.out.println("DESC" + result_desc.hasNext() + "   " + queryStringDesciption);

            String descriptions = "";
            String outDescription = "";

            if (result_desc.hasNext()) {
                while (result_desc.hasNext()) {
                    BindingSet bindingSet_desc = result_desc.next();

                    if (bindingSet_desc.getValue("desc") != null) {
                        Value description = bindingSet_desc.getValue("desc");
                        descriptions = description.stringValue();
                        outDescription = new String(descriptions.getBytes("iso-8859-1"), "utf-8");


                        //  System.out.println("DESC" + descriptions);




                    }
                    arrayVirtuosoDescriptions.add(outDescription);
                }



            }




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoDescriptions;
    }

    public String getDescription(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoDescriptions = new ArrayList();
        String sDesc = "";
        try {


            virtuosoConnection = ConnectionToVirtuoso();
            //RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringDesciption = ""
                    + "SELECT distinct ?desc FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:description ?desc.\n"
                    + "}";

            TupleQuery tupleQuery_desc = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringDesciption);



            TupleQueryResult result_desc = tupleQuery_desc.evaluate();


            // System.out.println("DESC" + result_desc.hasNext() + "   " + queryStringDesciption);

            String descriptions = "";
            String outDescription = "";

            if (result_desc.hasNext()) {
                while (result_desc.hasNext()) {
                    BindingSet bindingSet_desc = result_desc.next();

                    if (bindingSet_desc.getValue("desc") != null) {
                        Value description = bindingSet_desc.getValue("desc");
                        descriptions = description.stringValue();
                        outDescription = new String(descriptions.getBytes("iso-8859-1"), "utf-8");


                        //  System.out.println("DESC" + descriptions);




                    }
                    arrayVirtuosoDescriptions.add(outDescription);
                }



            } else {
                sDesc = "---";
            }
            arrayVirtuosoDescriptions = getListNotDuplicate(arrayVirtuosoDescriptions);

            result_desc.close();




            for (int i = 0; i < arrayVirtuosoDescriptions.size(); i++) {
                sDesc = sDesc + "##" + arrayVirtuosoDescriptions.get(i).toString();

            }






        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sDesc;
    }

    public String getIdentifiers(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoIdentifiers = new ArrayList();

        String sId = "";
        try {

            // RepositoryConnection con = ConnectionToVirtuoso();
            virtuosoConnection = ConnectionToVirtuoso();
            String queryStringIdentifier = ""
                    + "SELECT distinct ?id FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:identifier ?id.\n"
                    + "}";

            TupleQuery tupleQuery_identifier = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringIdentifier);



            TupleQueryResult result_identifier = tupleQuery_identifier.evaluate();


            //System.out.println("IDENTIFIER" + result_identifier.hasNext() + "   " + queryStringIdentifier);

            String identifiers = "";
            String outIdentifiers = "";

            if (result_identifier.hasNext()) {

                while (result_identifier.hasNext()) {
                    BindingSet bindingSet_identifier = result_identifier.next();
                    if (bindingSet_identifier.getValue("id") != null) {
                        Value identifier = bindingSet_identifier.getValue("id");
                        identifiers = identifier.stringValue();
                        outIdentifiers = new String(identifiers.getBytes("iso-8859-1"), "utf-8");

                        //  System.out.println("IDENTIFIERS" + identifiers);




                    }
                    arrayVirtuosoIdentifiers.add(outIdentifiers);
                }
            } else {
                sId = "---";

            }
            result_identifier.close();

            arrayVirtuosoIdentifiers = getListNotDuplicate(arrayVirtuosoIdentifiers);



            for (int i = 0; i < arrayVirtuosoIdentifiers.size(); i++) {
                sId = sId + "##" + arrayVirtuosoIdentifiers.get(i).toString();

            }






        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sId;
    }

    public ArrayList getIdentifiers2(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoIdentifiers = new ArrayList();


        try {
            virtuosoConnection = ConnectionToVirtuoso();
            // RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringIdentifier = ""
                    + "SELECT distinct ?id FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:identifier ?id.\n"
                    + "}";

            TupleQuery tupleQuery_identifier = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringIdentifier);



            TupleQueryResult result_identifier = tupleQuery_identifier.evaluate();


            //System.out.println("IDENTIFIER" + result_identifier.hasNext() + "   " + queryStringIdentifier);

            String identifiers = "";
            String outIdentifiers = "";

            if (result_identifier.hasNext()) {

                while (result_identifier.hasNext()) {
                    BindingSet bindingSet_identifier = result_identifier.next();
                    if (bindingSet_identifier.getValue("id") != null) {
                        Value identifier = bindingSet_identifier.getValue("id");
                        identifiers = identifier.stringValue();
                        outIdentifiers = new String(identifiers.getBytes("iso-8859-1"), "utf-8");

                        //  System.out.println("IDENTIFIERS" + identifiers);




                    }
                    arrayVirtuosoIdentifiers.add(outIdentifiers);
                }
            }


            //con.close();





        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoIdentifiers;
    }

    public ArrayList getSubject(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoSubjects = new ArrayList();


        try {

            //RepositoryConnection con = ConnectionToVirtuoso();
            virtuosoConnection = ConnectionToVirtuoso();
            String queryStringSubject = ""
                    + "SELECT ?subject FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:subject ?subject.\n"
                    + "}";

            TupleQuery tupleQuery_subject = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSubject);



            TupleQueryResult result_subject = tupleQuery_subject.evaluate();


            //System.out.println("SUBJECT" + result_subject.hasNext() + "   " + queryStringSubject);

            String subjects = "";
            String outSubjet = "";

            if (result_subject.hasNext()) {

                while (result_subject.hasNext()) {
                    BindingSet bindingSet_subject = result_subject.next();
                    if (bindingSet_subject.getValue("subject") != null) {
                        Value subject = bindingSet_subject.getValue("subject");
                        subjects = subject.stringValue();
                        outSubjet = new String(subjects.getBytes("iso-8859-1"), "utf-8");

                        // System.out.println("SUBJECT" + subjects);


                    }
                    arrayVirtuosoSubjects.add(outSubjet);
                }
            }


            //  con.close();





        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoSubjects;
    }

    public ArrayList getSources(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoSource = new ArrayList();

        try {

            virtuosoConnection = ConnectionToVirtuoso();
            String sources = "";
            String outSources = "";
            // RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringSource = ""
                    + "SELECT ?source FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:source ?source.\n"
                    + "}";

            TupleQuery tupleQuery_source = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSource);



            TupleQueryResult result_source = tupleQuery_source.evaluate();


            //System.out.println("SOURCE HAS NEXT? " + result_source.hasNext() + "   " + queryStringSource);




            if (result_source.hasNext()) {

                while (result_source.hasNext()) {
                    BindingSet bindingSet_source = result_source.next();
                    if (bindingSet_source.getValue("source") != null) {
                        Value source = bindingSet_source.getValue("source");
                        sources = source.stringValue();
                        outSources = new String(sources.getBytes("iso-8859-1"), "utf-8");

                        //System.out.println("SOURCE" + source);




                    }
                    arrayVirtuosoSource.add(outSources);
                }
            }



            // con.close();




        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoSource;
    }
    //return array contente un subject e il numero di resource che hanno quel subject

    public ArrayList getSubjectFromCodeLanguage(ArrayList arrayCodesLanguage) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ConnectionToVirtuoso();
        ArrayList arraySubjectsFromLanguage = new ArrayList();
        //System.out.println("ARRAYCODE SIZE-> "+arrayCodesLanguage.size());
        int countTotale = 0;
        try {

            //System.out.println("sono in getSubjectFromCodeLanguage");
            String subjects = "";
            String count_source = "";
            virtuosoConnection = ConnectionToVirtuoso();
            //RepositoryConnection con = ConnectionToVirtuoso();

            for (int i = 0; i < arrayCodesLanguage.size(); i++) {

                String code = arrayCodesLanguage.get(i).toString();
                // System.out.println("CODE=> "+code);

                String queryStringSubjectLanguage = ""
                        + "SELECT distinct(lcase(str(?subject))) as ?subj count(?source) as ?num FROM <" + graph + ">\n"
                        + "WHERE {\n "
                        + "?source dc:subject ?subject.\n"
                        + "?source dc:language ?language.\n"
                        + "filter (lcase(str(?language))= '" + code + "' ).\n"
                        + " FILTER (STRLEN(?subject) < 5000).\n"
                        + "}ORDER BY (?subj) limit 100";

                TupleQuery tupleQuery_SubjectLanguage = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSubjectLanguage);


                TupleQueryResult result_SubjectLanguage = tupleQuery_SubjectLanguage.evaluate();


                //System.out.println("SOURCE HAS NEXT? " + result_SubjectLanguage.hasNext() + "   " + queryStringSubjectLanguage);




                if (result_SubjectLanguage.hasNext()) {

                    while (result_SubjectLanguage.hasNext()) {
                        BindingSet bindingSet_SubjectLanguage = result_SubjectLanguage.next();

                        Value subject = bindingSet_SubjectLanguage.getValue("subj");
                        subjects = subject.stringValue();
                        String outSubjects = new String(subjects.getBytes("iso-8859-1"), "utf-8");
                        Value count_sources = bindingSet_SubjectLanguage.getValue("num");
                        count_source = count_sources.stringValue();
                        countTotale = countTotale + Integer.parseInt(count_source);
                        //  System.out.println("COUNT_TOTALE: "+countTotale);
                        // System.out.println("RESULT " + subjects + " # " + count_source);

                        if (!isDouble(outSubjects) && !isInteger(outSubjects) && !isOnlyNumber(outSubjects)) {
                            arraySubjectsFromLanguage.add(outSubjects + "#" + count_source);
                        }

//                        try {
//                            int subjInt = Integer.parseInt(subjects);
//                        } catch (NumberFormatException e) {
//
//                            System.out.println("Numero non valido.");
//                            arraySubjectsFromLanguage.add(subjects + "#" + count_source);
//                        }


                        // if(!subjects.equals()) 

                        //  arraySubjectsFromLanguage.add(subjects + "#" + count_source);
                    }
                    //  arraySubjectsFromLanguage.add(subjects + "#" + count_source);

                }

                // System.out.println("COUNT_FINALE: "+countTotale);

            }
            // arraySubjectsFromLanguage.add(countTotale);
            //  System.out.println("COUNT_FINALE: "+countTotale);
//            for (int i = 0; i < arraySubjectsFromLanguage.size(); i++) {
//                System.out.println("arraySubjectsFromLanguage " + arraySubjectsFromLanguage.get(i).toString());
//            }
            // System.out.println("COUNT_FINALE: "+countTotale);


            //con.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("COUNT_FINALE: "+countTotale);
        arraySubjectsFromLanguage.add(countTotale);

        return arraySubjectsFromLanguage;
    }

    public static boolean isDouble(String elem) {
        try {
            Double.parseDouble(elem);
            return true;
        } catch (Exception e) {
            //System.out.println("STRINGA VALIDA (double)");
            return false;

        }
    }

    public static boolean isInteger(String elem) {
        try {
            Integer.parseInt(elem);
            return true;
        } catch (Exception e) {
            // System.out.println("STRINGA VALIDA (Integer)");
            return false;
        }
    }

    public static boolean isOnlyNumber(String elem) {
        int count = 0;

        char[] notValidChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};
        for (int i = 0; i < notValidChars.length; i++) {
            for (int j = 0; j < elem.length(); j++) {
                if (elem.charAt(j) == notValidChars[i]) {
                    count++;

                }
            }
        }
        if (count == elem.length()) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList getSubject() throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayVirtuosoSubjects = new ArrayList();

        try {

            virtuosoConnection = ConnectionToVirtuoso();
            String subjects = "";
            String count_source = "";
            //RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringSubject = ""
                    + "SELECT distinct(?subject) as ?subj count(?source) as ?num FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "?source dc:subject ?subject.\n"
                    + "}ORDER BY DESC(?subject) LIMIT 30";

            TupleQuery tupleQuery_Subject = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringSubject);



            TupleQueryResult result_Subject = tupleQuery_Subject.evaluate();


            //System.out.println("SOURCE HAS NEXT? " + result_Subject.hasNext() + "   " + queryStringSubject);




            if (result_Subject.hasNext()) {

                while (result_Subject.hasNext()) {
                    BindingSet bindingSet_Subject = result_Subject.next();
                    // System.out.println("Sono qua ");
                    Value subject = bindingSet_Subject.getValue("subj");
                    subjects = subject.stringValue();
                    String outSubjects = new String(subjects.getBytes("iso-8859-1"), "utf-8");
                    Value count_sources = bindingSet_Subject.getValue("num");
                    count_source = count_sources.stringValue();

                    //  System.out.println("RESULT " + subjects + " # " + count_source);







                    arrayVirtuosoSubjects.add(outSubjects + "#" + count_source);
                }

            }
//            for (int i = 0; i < arrayVirtuosoSubjects.size(); i++) {
//                System.out.println("arrayVirtuosoSubjects " + arrayVirtuosoSubjects.get(i).toString());
//            }



            // con.close();



        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoSubjects;
    }

    public ArrayList getPublisher(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {
        ArrayList arrayVirtuosoPublisher = new ArrayList();


        try {

            // RepositoryConnection con = ConnectionToVirtuoso();
            virtuosoConnection = ConnectionToVirtuoso();
            String queryStringPublisher = ""
                    + "SELECT ?publisher FROM <" + graph + ">\n"
                    + "WHERE {\n "
                    + "<" + resource + "> dc:publisher ?publisher.\n"
                    + "}";

            TupleQuery tupleQuery_publisher = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringPublisher);



            TupleQueryResult result_publisher = tupleQuery_publisher.evaluate();


            // System.out.println("PUBLISHER" + result_publisher.hasNext() + "   " + queryStringPublisher);

            String publishers = "";
            String outPublishers = "";

            if (result_publisher.hasNext()) {

                while (result_publisher.hasNext()) {
                    BindingSet bindingSet_publisher = result_publisher.next();
                    if (bindingSet_publisher.getValue("publisher") != null) {
                        Value publisher = bindingSet_publisher.getValue("publisher");
                        publishers = publisher.stringValue();
                        outPublishers = new String(publishers.getBytes("iso-8859-1"), "utf-8");

                        //    System.out.println("PUBLISHER" + publishers);




                    }
                    arrayVirtuosoPublisher.add(outPublishers);
                }
            }


            // con.close();





        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayVirtuosoPublisher;
    }

    public ArrayList getResourceFromSubject(String subject, ArrayList arrayCodesLanguage) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        arrayResourceFromSubject = new ArrayList();

        //RepositoryConnection con = ConnectionToVirtuoso();

        for (int i = 0; i < arrayCodesLanguage.size(); i++) {

            String code = arrayCodesLanguage.get(i).toString();
//                System.out.println("CODE DA SUBJect "+code);
//            
            virtuosoConnection = ConnectionToVirtuoso();
//
//                System.out.println("MIO SUBJECT: "+subject);
//          

            String queryStringResFromSuject = ""
                    + "SELECT ?s FROM <" + graph + ">\n"
                    + "WHERE {\n"
                    + "?s dc:subject ?subject.\n"
                    + "?s dc:language ?language.\n"
                    + "filter (lcase(str(?language))= '" + code + "' ).\n"
                    + "FILTER regex(str(?subject),'" + subject + "','i').\n"
                    + "} LIMIT 50";

            String resource_from_subject = "";


            TupleQuery tupleQuery_ResFromSubj = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringResFromSuject);



            TupleQueryResult result_ResFromSubj = tupleQuery_ResFromSubj.evaluate();


            //System.out.println("RESOURCE HAS NEXT? " + result_ResFromSubj.hasNext() + "   " + queryStringResFromSuject);




            if (result_ResFromSubj.hasNext()) {

                while (result_ResFromSubj.hasNext()) {
                    BindingSet bindingSet_ResFromSubj = result_ResFromSubj.next();

                    Value resource = bindingSet_ResFromSubj.getValue("s");
                    resource_from_subject = resource.stringValue();



                    arrayResourceFromSubject.add(resource_from_subject);

                }

            }
        }
//            for (int i = 0; i < arrayResourceFromSubject.size(); i++) {
//                System.out.println("arrayVirtuosoSubjects " + arrayResourceFromSubject.get(i).toString());
//            }


        //    con.close();

        //System.out.println("NUM DI RECORDS PER SUBJECT: "+subject+"---> "+arrayResourceFromSubject.size());


        return arrayResourceFromSubject;
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
                        // System.out.println("duplicato: "+listOriginal.get(i));
                        duplicato = true;
                    }

                }
                if (!duplicato) {
                    //System.out.println();
                    listNuova.add(listOriginal.get(i));
                    // listAuthor[k]=listAuthor[i];
                    //k++;
                }


            }

//            newArrayAuthors=new String[listNuova.size()];
//            for (int z=0;z<listNuova.size();z++)
//            {
//                newArrayAuthors[z]=listNuova.get(z).toString();
//            }

            return listNuova;
        } else {
            return listOriginal;
        }
    }

    public static ArrayList getCodesLanguage(String language) {


        ArrayList listCode = new ArrayList();
        if (language.equals("Bashkir")) {
            listCode.add("ba");
        }
        if (language.equals("Basque")) {
            listCode.add("baq");
            listCode.add("eus");
        }
        if (language.equals("Catalan")) {
            listCode.add("ca");
            listCode.add("cat");
        }
        if (language.equals("Chinese")) {
            listCode.add("chi");
            listCode.add("zho");
        }
        if (language.equals("Czech")) {
            listCode.add("cze");
            listCode.add("ces");
            listCode.add("cs");
        }
        if (language.equals("Danish")) {
            listCode.add("dan");
            listCode.add("da");
        }
        if (language.equals("Dutch")) {
            listCode.add("nl");
            listCode.add("nld");
            listCode.add("dut");
        }
        if (language.equals("English")) {
            listCode.add("en");
            listCode.add("eng");
        }
        if (language.equals("Finnish")) {
            listCode.add("fin");
            listCode.add("fi");

        }
        if (language.equals("French")) {
            listCode.add("fr");
            listCode.add("fre");
            listCode.add("fra");
        }
        if (language.equals("German")) {
            listCode.add("ger");
            listCode.add("de");
            listCode.add("deu");
            listCode.add("ge");
        }
        if (language.equals("Greek")) {
            listCode.add("grc");
            listCode.add("ell");

        }
        if (language.equals("Hungarian")) {
            listCode.add("hu");
            listCode.add("hun");

        }
        if (language.equals("Indonesian")) {
            listCode.add("id");
            listCode.add("ind");
            listCode.add("in");

        }
        if (language.equals("Irish")) {
            listCode.add("ga");
            listCode.add("gai");
            listCode.add("iri");

        }
        if (language.equals("Italian")) {
            listCode.add("it");
            listCode.add("ita");

        }
        if (language.equals("Japanese")) {
            listCode.add("ja");
            listCode.add("jpn");

        }
        if (language.equals("Latin")) {
            listCode.add("la");
            listCode.add("lat");

        }
        if (language.equals("Lithuania")) {
            listCode.add("lt");
            listCode.add("lit");

        }
        if (language.equals("Norwegian")) {
            listCode.add("no");
            listCode.add("nob");
            listCode.add("nno");
            listCode.add("nor");

        }
        if (language.equals("Polish")) {
            listCode.add("pl");
            listCode.add("po");
            listCode.add("pol");

        }
        if (language.equals("Portuguese")) {
            listCode.add("por");
            listCode.add("pt");

        }
        if (language.equals("Russian")) {
            listCode.add("ru");
            listCode.add("rus");

        }
        if (language.equals("Serbo-Croatian")) {
            listCode.add("scr");
            listCode.add("hr");
            listCode.add("sh");

        }
        if (language.equals("Slovenian")) {
            listCode.add("sl");
            listCode.add("slv");

        }
        if (language.equals("Spanish")) {
            listCode.add("es");
            listCode.add("esl");
            listCode.add("sp");
            listCode.add("spa");

        }
        if (language.equals("Swedish")) {
            listCode.add("sv");
            listCode.add("sve");
            listCode.add("swe");
        }

        return listCode;
    }

    public ArrayList getRepository(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayRepositoryInfo = new ArrayList();

        virtuosoConnection = ConnectionToVirtuoso();
        try {

            //RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringRepository = "PREFIX myOnto:<http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#>"
                    + " SELECT * FROM <" + graph + ">\n"
                    + " WHERE {\n"
                    + " <" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#acronimRepository> ?acronimRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#nameRepository> ?nameRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#longitudeRepository> ?longRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#latitudeRepository> ?latRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#countryCodeRepository> ?countryCodeRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#urlRepository> ?urlRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#addressOAIPMHRepository> ?OAIPMHRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#addressRepository> ?addressRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#acronimRepository> ?acroRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#domainRepository> ?domainRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#projectRepository> ?projectRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isRepositoryOf> ?org.\n"
                    + " ?org <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#nameOrganization> ?nameOrg.\n"
                    + "}";


            TupleQuery tupleQuery_repository = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringRepository);



            TupleQueryResult result_repository = tupleQuery_repository.evaluate();


            //System.out.println("REPOSITORY QUERY HAS NEXT?" + result_repository.hasNext() + "   " + queryStringRepository);


            String nameRep = "";
            String countryCodeRep = "";
            String longitudeRep = "";
            String latitudeRep = "";
            String urlRep = "";
            String addressOAIPMHRep = "";
            String addressRep = "";
            String acronimRep = "";
            String domainRep = "";
            String projectRep = "";
            String nameOrganization = "";


            if (result_repository.hasNext()) {

                while (result_repository.hasNext()) {
                    BindingSet bindingSet_repository = result_repository.next();


                    nameRep = bindingSet_repository.getValue("nameRep").stringValue();
                    String outNameRep = new String(nameRep.getBytes("iso-8859-1"), "utf-8");
                    longitudeRep = bindingSet_repository.getValue("longRep").stringValue();
                    latitudeRep = bindingSet_repository.getValue("latRep").stringValue();
                    countryCodeRep = bindingSet_repository.getValue("countryCodeRep").stringValue();
                    urlRep = bindingSet_repository.getValue("urlRep").stringValue();
                    addressOAIPMHRep = bindingSet_repository.getValue("OAIPMHRep").stringValue();
                    addressRep = bindingSet_repository.getValue("addressRep").stringValue();
                    String outAddressRep = new String(addressRep.getBytes("iso-8859-1"), "utf-8");
                    acronimRep = bindingSet_repository.getValue("acroRep").stringValue();

                    domainRep = bindingSet_repository.getValue("domainRep").stringValue();
                    String outDomainRep = new String(domainRep.getBytes("iso-8859-1"), "utf-8");
                    projectRep = bindingSet_repository.getValue("projectRep").stringValue();
                    String outProjectRep = new String(projectRep.getBytes("iso-8859-1"), "utf-8");
                    nameOrganization = bindingSet_repository.getValue("nameOrg").stringValue();
                    String outNameOrganization = new String(nameOrganization.getBytes("iso-8859-1"), "utf-8");

                    arrayRepositoryInfo.add(0, outNameRep);
                    arrayRepositoryInfo.add(1, countryCodeRep);
                    arrayRepositoryInfo.add(2, longitudeRep);
                    arrayRepositoryInfo.add(3, latitudeRep);
                    arrayRepositoryInfo.add(4, urlRep);
                    arrayRepositoryInfo.add(5, addressOAIPMHRep);
                    arrayRepositoryInfo.add(6, outAddressRep);
                    arrayRepositoryInfo.add(7, acronimRep);
                    arrayRepositoryInfo.add(8, outDomainRep);
                    arrayRepositoryInfo.add(9, outProjectRep);
                    arrayRepositoryInfo.add(10, outNameOrganization);



                }
            }


            //System.out.println("DIM DI REP "+arrayRepositoryInfo.size());

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrayRepositoryInfo;
    }

    public String getNameUrlRepository(String resource) throws RepositoryException, MalformedQueryException, UnsupportedEncodingException {

        ArrayList arrayRepositoryInfo = new ArrayList();
        String sRep = "";
        virtuosoConnection = ConnectionToVirtuoso();
        try {

            //RepositoryConnection con = ConnectionToVirtuoso();

            String queryStringRepository = "PREFIX myOnto:<http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#>"
                    + " SELECT * FROM <" + graph + ">\n"
                    + " WHERE {\n"
                    + " <" + resource + "> <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#isResourceOf> ?rep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#nameRepository> ?nameRep.\n"
                    + " ?rep <http://semanticweb.org/ontologies/2013/2/7/RepositoryOntology.owl#urlRepository> ?urlRep.\n"
                    + "}";


            TupleQuery tupleQuery_repository = virtuosoConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryStringRepository);



            TupleQueryResult result_repository = tupleQuery_repository.evaluate();


            //System.out.println("REPOSITORY QUERY HAS NEXT?" + result_repository.hasNext() + "   " + queryStringRepository);


            String nameRep = "";
            String countryCodeRep = "";
            String longitudeRep = "";
            String latitudeRep = "";
            String urlRep = "no";
            String addressOAIPMHRep = "";
            String addressRep = "";
            String acronimRep = "";
            String domainRep = "";
            String projectRep = "";
            String nameOrganization = "";


            if (result_repository.hasNext()) {

                while (result_repository.hasNext()) {
                    BindingSet bindingSet_repository = result_repository.next();


                    nameRep = bindingSet_repository.getValue("nameRep").stringValue();
                    String outNameRep = new String(nameRep.getBytes("iso-8859-1"), "utf-8");

                    urlRep = bindingSet_repository.getValue("urlRep").stringValue();


                    arrayRepositoryInfo.add(0, outNameRep);

                    arrayRepositoryInfo.add(1, urlRep);




                }
            } else {
                sRep = "---";
            }

            if (arrayRepositoryInfo.size() > 0) {
                sRep = arrayRepositoryInfo.get(0).toString() + "##" + arrayRepositoryInfo.get(1).toString();
            }


            //System.out.println("DIM DI REP "+arrayRepositoryInfo.size());

        } catch (QueryEvaluationException ex) {
            Logger.getLogger(SemanticQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sRep;
    }
}

package org.example;


import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.*;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = Main.class.getClassLoader();
        String filepath = Objects.requireNonNull(classLoader.getResource("6x6-f.owl")).getFile();
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();

        String content = readFileToString(filepath);

        String sameAs = """
                        <SameIndividual>
                            <NamedIndividual IRI="#1"/>
                            <NamedIndividual abbreviatedIRI=":c1_23"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#1"/>
                            <NamedIndividual abbreviatedIRI=":c6_23"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#2"/>
                            <NamedIndividual abbreviatedIRI=":c1_22"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#2"/>
                            <NamedIndividual abbreviatedIRI=":c2_12"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#2"/>
                            <NamedIndividual abbreviatedIRI=":c3_21"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#2"/>
                            <NamedIndividual abbreviatedIRI=":c5_13"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#3"/>
                            <NamedIndividual abbreviatedIRI=":c1_13"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#3"/>
                            <NamedIndividual abbreviatedIRI=":c3_22"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#3"/>
                            <NamedIndividual abbreviatedIRI=":c4_11"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#3"/>
                            <NamedIndividual abbreviatedIRI=":c5_21"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#3"/>
                            <NamedIndividual abbreviatedIRI=":c6_13"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#4"/>
                            <NamedIndividual abbreviatedIRI=":c2_21"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#4"/>
                            <NamedIndividual abbreviatedIRI=":c3_13"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#4"/>
                            <NamedIndividual abbreviatedIRI=":c5_11"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#5"/>
                            <NamedIndividual abbreviatedIRI=":c1_11"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#5"/>
                            <NamedIndividual abbreviatedIRI=":c3_12"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#5"/>
                            <NamedIndividual abbreviatedIRI=":c4_21"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#5"/>
                            <NamedIndividual abbreviatedIRI=":c5_23"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#6"/>
                            <NamedIndividual abbreviatedIRI=":c1_21"/>
                        </SameIndividual>
                        <SameIndividual>
                            <NamedIndividual IRI="#6"/>
                            <NamedIndividual abbreviatedIRI=":c5_22"/>
                        </SameIndividual>
                """;

       content = content.replace(Config.REPLACEMENT_TEMPLATE_STRING,sameAs);

        InputStream inputStream = createInputStreamFromString(content);


        OWLOntology o = m.loadOntologyFromOntologyDocument(inputStream);


        Reasoner hermit =new Reasoner(o);

        hermit.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        hermit.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
        hermit.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
        hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        hermit.precomputeInferences(InferenceType.SAME_INDIVIDUAL);






        printInferredAssertions(hermit, o);


    }

    private static void printInferredAssertions(OWLReasoner reasoner, OWLOntology ontology) {
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            if(individual.toString().contains("http://projet.org#c") ){
                System.out.println("Individual: " + individual);
                Node<OWLNamedIndividual> sameIndividualsNode = reasoner.getSameIndividuals(individual);
                for (OWLNamedIndividual sameIndividual : sameIndividualsNode.getEntities()) {
                    if (!sameIndividual.equals(individual) && !sameIndividual.toString().contains("http://projet.org#c")  ) {
                        System.out.println("  sameAs: " + sameIndividual);
                        break;
                    }
                }

                System.out.println();
            }
        }
    }

    private static String readFileToString(String filepath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }
        return stringBuilder.toString();
    }

    private static InputStream createInputStreamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
}


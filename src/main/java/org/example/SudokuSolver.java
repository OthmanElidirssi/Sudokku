package org.example;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.*;
import java.util.Objects;

public class SudokuSolver {



    private Cell[][] grid;
    private String ontolofyTemplateContent;

    public SudokuSolver(Cell[][] grid){
        this.grid = grid;
        ClassLoader classLoader = Main.class.getClassLoader();
        String filepath = Objects.requireNonNull(classLoader.getResource("6x6-f.owl")).getFile();
        this.ontolofyTemplateContent = readFileToString(filepath);

    }

    private String generateSameAsAxioms() {

        StringBuilder sameAsAxioms = new StringBuilder();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                if (cell.isManuallySet) {
                    sameAsAxioms.append(cell.generateSameAsAxiom()).append("\n");
                }
            }
        }
        return sameAsAxioms.toString();
    }

    private OWLOntology generateOntology(String content) throws OWLOntologyCreationException {
        InputStream inputStream = createInputStreamFromString(this.ontolofyTemplateContent.replace(Config.REPLACEMENT_TEMPLATE_STRING,content));
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        return m.loadOntologyFromOntologyDocument(inputStream);
    }


    public void solve() throws OWLOntologyCreationException {

        String content = this.generateSameAsAxioms();
        OWLOntology ontology = this.generateOntology(content);
        Reasoner hermit =new Reasoner(ontology);
        hermit.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        hermit.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
        hermit.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
        hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        hermit.precomputeInferences(InferenceType.SAME_INDIVIDUAL);

        printInferredAssertions(hermit, ontology);


    }
    private  String readFileToString(String filepath) {
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

    private  InputStream createInputStreamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
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
}

// Ishaan Srivastava 
// 11/21/25 -> 12/2/25
// CSE 123 
// P3: Cornbear's Classifier
// TA: Aidan Suen

import java.io.*;
import java.util.*;

// This class is a text-based machine learning classifier that can be trained on labeled data
// and used to classify new text blocks based on learned patterns.
public class Classifier {
    
    private ClassifierNode overallRoot;

    // Behavior: Constructs a new Classifier from a Scanner input.
    // Exceptions: IllegalArgumentException if the provided Scanner is null
    //             IllegalStateException if the provided Scanner is empty
    // Parameters: input - the Scanner to read the tree structure from
    public Classifier(Scanner input) throws IllegalArgumentException, IllegalStateException {
        if (input == null) {
            throw new IllegalArgumentException("Provided input Scanner is null!");
        }
        if (!input.hasNextLine()) {
            throw new IllegalStateException("Provided Scanner is empty!");
        }

        overallRoot = populateTree(input);
    }

    // Behavior: Private helper method to populate a decision tree from a Scanner input
    // Parameters: input - the Scanner to read the tree structure from
    // Returns: the root node of the populated subtree
    private ClassifierNode populateTree(Scanner input) {
        String check = input.nextLine();
        if (check.contains("Feature")) {
            String feature = check.substring("Feature: ".length());
            double threshold = Double.parseDouble(input.nextLine().substring
                ("Threshold: ".length()));      
            ClassifierNode node = new ClassifierNode(feature, threshold, populateTree(input), 
                populateTree(input));
            return node;        
        }   
        return new ClassifierNode(check);
    }

    // Behavior: Constructs and trains a new Classifier from the provided training data and labels.
    // Exceptions: IllegalArgumentException if data or labels are null, sizes do not match, 
    //             or either is empty.
    // Parameters: data - the list of TextBlocks to train on
    //             labels - the corresponding list of labels for the training data
    public Classifier(List<TextBlock> data, List<String> labels) throws IllegalArgumentException{ 
        if (data == null || labels == null || data.size() != labels.size() || 
            data.isEmpty() || labels.isEmpty()) {
            throw new IllegalArgumentException("Invalid data or labels provided!");
        }

        overallRoot = new ClassifierNode(labels.get(0), data.get(0));
        updateModel(data, labels, 1);
    }

    // Behavior: Private helper method to update a decision tree model with training data.
    // Parameters: data - the list of TextBlock instances to train on
    //             labels - the corresponding list of labels for the training data
    //             index - the current index in the data and labels lists to process
    private void updateModel(List<TextBlock> data, List<String> labels, int index) {
        // Stop recursion when all training examples have been processed
        if (index < data.size()) {
            TextBlock current = data.get(index);
            String expected = labels.get(index);

            // Find where this datapoint would be classified
            ClassifierNode parent = null;
            ClassifierNode leaf = overallRoot;
            boolean isLeft = false;
            while (leaf.left != null && leaf.right != null) {
                parent = leaf;
                double val = current.get(leaf.feature);
                if (val < leaf.threshold) {
                    leaf = leaf.left;
                    isLeft = true;
                } 
                else {
                    leaf = leaf.right;
                    isLeft = false;
                }
            }

            // If prediction is correct, we can stop here
            if (leaf.label.equals(expected)) {
                if (leaf.data == null) {
                    leaf.data = current;
                }
            }

            else {              
                String feature = leaf.data.findBiggestDifference(current);
                double featureStored = leaf.data.get(feature);
                double featureCurrent = current.get(feature);
                double threshold = midpoint(featureStored, featureCurrent);
                ClassifierNode leftChild = null;
                ClassifierNode rightChild = null;

                // Use the same comparison as traverse
                if (featureStored < threshold) {
                    leftChild = leaf;
                } 
                else {
                    rightChild = leaf;
                }

                // Have to create a new leaf node for the current datapoint
                ClassifierNode n = new ClassifierNode(expected, current);

                if (featureCurrent < threshold) {
                    if (leftChild == null) {
                        leftChild = n; 
                    }
                    else {
                        rightChild = n;
                    }
                } 
                else {
                    if (rightChild == null) {
                        rightChild = n; 
                    }
                    else {
                        leftChild = n;
                    }
                }

                // Attach the decision node in place of the original one
                ClassifierNode decision = new ClassifierNode(feature, threshold, leftChild, 
                    rightChild);

                if (parent == null) {
                    overallRoot = decision;
                } 
                else {
                    if (isLeft) {
                        parent.left = decision;
                    }
                    else {
                        parent.right = decision;
                    }
                }                            
            }  
            updateModel(data, labels, index + 1);  
        }
    }

    // Behavior: Finds the label that the classifier predicts. 
    // Exceptions: IllegalArgumentException if the provided TextBlock is null.
    // Parameters: input - the TextBlock to classify
    // Returns: the predicted label for the input TextBlock
    public String classify(TextBlock input) {
        if (input == null) {
            throw new IllegalArgumentException("Provided TextBlock is null!");
        }
        return traverse(overallRoot, input);
    }

    // Behavior: Private helper method to traverse the decision tree for classification.
    // Parameters: node - the current node in the decision tree
    //             input - the TextBlock to classify
    // Returns: the predicted label for the input TextBlock
    private String traverse(ClassifierNode node, TextBlock input) throws IllegalArgumentException {
        if (node.left == null && node.right == null) {
            return node.label;
        }
        if (input.get(node.feature) < node.threshold) {
            return traverse(node.left, input);
        }
        return traverse(node.right, input);
    }

    // Behavior: Saves the decision tree structure to the provided PrintStream in the format: 
    //           Feature: <feature>
    //           Threshold: <threshold>
    //           Labels only print the label on its own line.
    // Exceptions: IllegalArgumentException if the provided PrintStream is null.
    // Parameters: output - the PrintStream to write the tree structure to
    public void save(PrintStream output) throws IllegalArgumentException {
        if (output == null) {
            throw new IllegalArgumentException("Provided PrintStream is null!");
        }
        preOrderTraversal(overallRoot, output);
    }

    // Behavior: Private helper method to do a pre-order traversal of the decision tree
    //           and write its structure to the provided PrintStream.
    // Parameters: node - the current node in the decision tree
    //             output - the PrintStream to write the tree structure to
    // Returns: the current node after writing its information
    private ClassifierNode preOrderTraversal(ClassifierNode node, PrintStream output) {
        if (node.left == null && node.right == null) {
            output.println(node.label);
        }
        else {
            output.println("Feature: " + node.feature);
            output.println("Threshold: " + node.threshold);
            preOrderTraversal(node.left, output);
            preOrderTraversal(node.right, output);
        }
        return node;
    }

    // Private inner class representing a node in the decision tree.
    private static class ClassifierNode {
        public final String feature;
        public final double threshold;
        public final String label;
        public ClassifierNode left;
        public ClassifierNode right;
        public TextBlock data;

        // Behavior: Constructor for branch nodes
        // Parameters: feature - the feature used for splitting
        //             threshold - the threshold value for the feature
        //             left - the left child node
        //             right - the right child node
        public ClassifierNode(String feature, double threshold, 
                              ClassifierNode left, ClassifierNode right) {
            this.feature = feature;
            this.threshold = threshold;
            this.left = left;
            this.right = right;
            this.label = "";
        }

        // Behavior: Constructor for leaf nodes
        // Parameters: label - the label for the leaf node
        public ClassifierNode(String label) {
            this.feature = "";
            this.threshold = 0.0;
            this.label = label;
        }

        // Behavior: Constructor for leaf nodes with associated data
        // Parameters: label - the label for the leaf node
        //             data - the TextBlock associated with this leaf
        public ClassifierNode(String label, TextBlock data) {
            this(label);
            this.data = data;
        }
    }


    ////////////////////////////////////////////////////////////////////
    // PROVIDED METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
    ////////////////////////////////////////////////////////////////////

    // Helper method to calculate the midpoint of two provided doubles.
    private static double midpoint(double one, double two) {
        return Math.min(one, two) + (Math.abs(one - two) / 2.0);
    }

    // Behavior: Calculates the accuracy of this model on provided Lists of 
    //           testing 'data' and corresponding 'labels'. The label for a 
    //           datapoint at an index within 'data' should be found at the 
    //           same index within 'labels'.
    // Exceptions: IllegalArgumentException if the number of datapoints doesn't match the number 
    //             of provided labels
    // Returns: a map storing the classification accuracy for each of the encountered labels when
    //          classifying
    // Parameters: data - the list of TextBlock objects to classify. Should be non-null.
    //             labels - the list of expected labels for each TextBlock object. 
    //             Should be non-null.
    public Map<String, Double> calculateAccuracy(List<TextBlock> data, List<String> labels) {
        // Check to make sure the lists have the same size (each datapoint has an expected label)
        if (data.size() != labels.size()) {
            throw new IllegalArgumentException(
                    String.format("Length of provided data [%d] " +
                                    "doesn't match provided labels [%d]", 
                            data.size(), labels.size()));
        }

        // Create our total and correct maps for average calculation
        Map<String, Integer> labelToTotal = new HashMap<>();
        Map<String, Double> labelToCorrect = new HashMap<>();
        labelToTotal.put("Overall", 0);
        labelToCorrect.put("Overall", 0.0);

        for (int i = 0; i < data.size(); i++) {
            String result = classify(data.get(i));
            String label = labels.get(i);

            // Increment totals depending on resultant label
            labelToTotal.put(label, labelToTotal.getOrDefault(label, 0) + 1);
            labelToTotal.put("Overall", labelToTotal.get("Overall") + 1);
            if (result.equals(label)) {
                labelToCorrect.put(result, labelToCorrect.getOrDefault(result, 0.0) + 1);
                labelToCorrect.put("Overall", labelToCorrect.get("Overall") + 1);
            }
        }

        // Turn totals into accuracy percentage
        for (String label : labelToCorrect.keySet()) {
            labelToCorrect.put(label, labelToCorrect.get(label) / labelToTotal.get(label));
        }

        return labelToCorrect;
    }
}

package com.Rule_Engine_with_AST.Rule.Engine.with.AST.Service;

import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.ASTNode;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.Rule;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    // Save rule to repository
    public Rule createRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    // Get all saved rules from repository
    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    // Parse the rule string into an Abstract Syntax Tree (AST)
    public ASTNode createRule(String ruleString) {
        ruleString = ruleString.trim();

        // Handle "AND" operator
        if (ruleString.contains(" AND ")) {
            return createOperatorNode(ruleString, "AND");
        }

        // Handle "OR" operator
        if (ruleString.contains(" OR ")) {
            return createOperatorNode(ruleString, "OR");
        }

        // Handle simple conditions (like "age > 30")
        if (isValidCondition(ruleString)) {
            return new ASTNode("operand", ruleString);
        }

        // Throw error if the rule is not valid
        throw new IllegalArgumentException("Invalid rule: " + ruleString);
    }

    // Helper method to handle operator nodes
    private ASTNode createOperatorNode(String ruleString, String operator) {
        String[] parts = ruleString.split(" " + operator + " ");
        ASTNode leftNode = createRule(parts[0].trim());
        ASTNode rightNode = createRule(parts[1].trim());

        // If any of the sub-rules is invalid, throw an error
        if (leftNode == null || rightNode == null) {
            throw new IllegalArgumentException("Invalid rule format: " + ruleString);
        }

        // Return the AST node for the operator
        return new ASTNode("operator", leftNode, rightNode, operator);
    }

    // Validate condition (supports "age > 30", "department = 'Sales'")
    private boolean isValidCondition(String condition) {
        // Match conditions with numbers or strings
        return condition.matches("^[a-zA-Z]+\\s*(>|<|>=|<=|=)\\s*\\d+$") || condition.matches("^[a-zA-Z]+\\s*=\\s*'.+'$");
    }

    // Combine multiple rules into a single AST
    public ASTNode combineRules(List<String> ruleStrings) {
        if (ruleStrings == null || ruleStrings.isEmpty()) {
            throw new IllegalArgumentException("No rules provided for combination.");
        }

        // Heuristic: Combine using the most frequent operator
        long andCount = ruleStrings.stream().filter(rule -> rule.contains(" AND ")).count();
        long orCount = ruleStrings.stream().filter(rule -> rule.contains(" OR ")).count();

        String mainOperator = andCount >= orCount ? "AND" : "OR";
        ASTNode combinedAst = null;

        for (String rule : ruleStrings) {
            ASTNode currentAst = createRule(rule);
            if (combinedAst == null) {
                combinedAst = currentAst; // Initialize combined AST
            } else {
                combinedAst = new ASTNode("operator", combinedAst, currentAst, mainOperator); // Combine with main operator
            }
        }

        return combinedAst; // Return the root node of the combined AST
    }

    // Evaluate the AST against user attributes (e.g., age, department)
    public boolean evaluateRule(ASTNode ast, Map<String, Object> userAttributes) {
        if (ast == null) {
            return false; // Invalid AST
        }

        // Evaluate operator nodes
        if ("operator".equals(ast.getType())) {
            if ("AND".equals(ast.getValue())) {
                return evaluateRule(ast.getLeft(), userAttributes) && evaluateRule(ast.getRight(), userAttributes);
            } else if ("OR".equals(ast.getValue())) {
                return evaluateRule(ast.getLeft(), userAttributes) || evaluateRule(ast.getRight(), userAttributes);
            }
        }

        // Evaluate operand nodes
        if ("operand".equals(ast.getType())) {
            return evaluateCondition(ast.getValue(), userAttributes);
        }

        return false; // Default to false if neither operand nor operator
    }


    // Evaluate individual condition against user-provided attributes
    private boolean evaluateCondition(String condition, Map<String, Object> userAttributes) {
        // Handle greater than (e.g., "age > 30")
        if (condition.contains(">")) {
            String[] parts = condition.split(">");
            return compareAttribute(userAttributes, parts, ">");
        }

        // Handle less than (e.g., "age < 30")
        if (condition.contains("<")) {
            String[] parts = condition.split("<");
            return compareAttribute(userAttributes, parts, "<");
        }

        // Handle equality (e.g., "department = 'Sales'")
        if (condition.contains("=")) {
            String[] parts = condition.split("=");
            String attribute = parts[0].trim();
            String expectedValue = parts[1].trim().replaceAll("'", ""); // Remove quotes

            return userAttributes.containsKey(attribute) && userAttributes.get(attribute).equals(expectedValue);
        }

        return false; // Return false if condition is invalid
    }

    // Helper method for numeric comparisons
    private boolean compareAttribute(Map<String, Object> userAttributes, String[] parts, String operator) {
        String attribute = parts[0].trim();
        int threshold = Integer.parseInt(parts[1].trim());

        if (userAttributes.containsKey(attribute) && userAttributes.get(attribute) instanceof Integer) {
            int userValue = (Integer) userAttributes.get(attribute);
            if (operator.equals(">")) {
                return userValue > threshold;
            } else if (operator.equals("<")) {
                return userValue < threshold;
            }
        }

        return false;
    }
}

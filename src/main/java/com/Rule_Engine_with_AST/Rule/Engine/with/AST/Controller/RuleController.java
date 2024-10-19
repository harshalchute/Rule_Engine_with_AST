package com.Rule_Engine_with_AST.Rule.Engine.with.AST.Controller;

import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.ASTNode;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.Rule;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    // Create a new rule
    @PostMapping("/create")
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) {
        Rule savedRule = ruleService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRule);
    }

    // Get all rules
    @GetMapping
    public ResponseEntity<List<Rule>> getAllRules() {
        List<Rule> rules = ruleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    // Combine multiple rules into a single AST
    @PostMapping("/combine")
    public ResponseEntity<ASTNode> combineRules(@RequestBody List<String> rules) {
        ASTNode combinedAst = ruleService.combineRules(rules);
        return ResponseEntity.ok(combinedAst);
    }

    // Evaluate a rule against user attributes
    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> requestData) {
        String ruleString = (String) requestData.get("ruleString");
        Map<String, Object> userAttributes = (Map<String, Object>) requestData.get("attributes");

        if (ruleString == null || userAttributes == null) {
            return ResponseEntity.badRequest().body(false); // Handle bad requests
        }

        ASTNode ast = ruleService.createRule(ruleString);
        boolean result = ruleService.evaluateRule(ast, userAttributes);
        return ResponseEntity.ok(result);
    }

}

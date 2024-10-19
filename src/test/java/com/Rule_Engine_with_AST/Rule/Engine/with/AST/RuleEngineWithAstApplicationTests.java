package com.Rule_Engine_with_AST.Rule.Engine.with.AST;

import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.ASTNode;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.Rule;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Repository.RuleRepository;
import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleServiceTest {

	@InjectMocks
	private RuleService ruleService;

	@Mock
	private RuleRepository ruleRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void test_parse_and_operator() {
		RuleService ruleService = new RuleService();
		String ruleString = "age > 30 AND department = 'Sales'";
		ASTNode result = ruleService.createRule(ruleString);
		assertNotNull(result);
		assertEquals("operator", result.getType());
		assertEquals("AND", result.getValue());
		assertNotNull(result.getLeft());
		assertNotNull(result.getRight());
	}

	@Test
	public void test_empty_rule_string() {
		RuleService ruleService = new RuleService();
		String ruleString = "";
		assertThrows(IllegalArgumentException.class, () -> {
			ruleService.createRule(ruleString);
		});
	}

	@Test
	public void test_combine_rules_with_mixed_operators() {
		RuleService ruleService = new RuleService();
		List<String> rules = Arrays.asList("age > 30 AND department = 'Sales'", "salary < 50000 OR experience > 5");
		ASTNode result = ruleService.combineRules(rules);
		assertNotNull(result);
		assertEquals("operator", result.getType());
		assertTrue(result.getValue().equals("AND") || result.getValue().equals("OR"));
	}

	@Test
	public void test_handle_null_input_for_rule_strings() {
		RuleService ruleService = new RuleService();
		assertThrows(IllegalArgumentException.class, () -> {
			ruleService.combineRules(null);
		});
	}

	@Test
	public void test_evaluate_and_operator() {
		RuleService ruleService = new RuleService();
		Map<String, Object> userAttributes = Map.of("age", 35, "department", "Sales");
		ASTNode leftCondition = new ASTNode("operand", "age > 30");
		ASTNode rightCondition = new ASTNode("operand", "department = 'Sales'");
		ASTNode andNode = new ASTNode("operator", leftCondition, rightCondition, "AND");

		boolean result = ruleService.evaluateRule(andNode, userAttributes);

		assertTrue(result);
	}

	@Test
	public void test_evaluate_unknown_operator() {
		RuleService ruleService = new RuleService();
		Map<String, Object> userAttributes = Map.of("age", 35);
		ASTNode unknownOperatorNode = new ASTNode("operator", null, null, "UNKNOWN");

		boolean result = ruleService.evaluateRule(unknownOperatorNode, userAttributes);

		assertFalse(result);
	}
}

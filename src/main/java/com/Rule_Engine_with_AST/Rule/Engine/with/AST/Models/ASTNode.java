package com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ASTNode {

    private String type; // operator or operand
    private String value; // condition or operator
    private ASTNode left; // Left child for operator nodes
    private ASTNode right; // Right child for operator nodes

    public ASTNode(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public ASTNode(String type, ASTNode left, ASTNode right, String value) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value = value;
    }
}

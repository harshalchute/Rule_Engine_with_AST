package com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rules")
@Getter
@Setter
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rule_string", nullable = false) // Ensure this is marked as not null
    private String ruleString;

}

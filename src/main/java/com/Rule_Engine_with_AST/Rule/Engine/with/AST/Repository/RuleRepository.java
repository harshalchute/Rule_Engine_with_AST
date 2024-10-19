package com.Rule_Engine_with_AST.Rule.Engine.with.AST.Repository;

import com.Rule_Engine_with_AST.Rule.Engine.with.AST.Models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}

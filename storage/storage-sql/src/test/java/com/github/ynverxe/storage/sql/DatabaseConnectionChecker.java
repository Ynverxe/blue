package com.github.ynverxe.storage.sql;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseConnectionChecker implements ExecutionCondition {
  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    if (MySQLConnectionProvider.CONNECTION == null) {
      return ConditionEvaluationResult.disabled("Cannot connect with MySQL Database for storage-sql tests");
    }

    return ConditionEvaluationResult.enabled("Connection with MySQL Database for storage-sql tests established.");
  }
}
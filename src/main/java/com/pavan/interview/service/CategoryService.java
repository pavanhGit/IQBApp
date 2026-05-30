package com.pavan.interview.service;

import com.pavan.interview.model.QuestionCategory;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CategoryService {

    private static final Map<QuestionCategory, List<String>> KEYWORDS = new EnumMap<>(QuestionCategory.class);

    static {
        KEYWORDS.put(QuestionCategory.JAVA, List.of(
                "java", "jvm", "jdk", "jre", "oops", "object", "class", "interface", "inheritance",
                "polymorphism", "collection", "stream", "thread", "executor", "exception", "garbage"
        ));
        KEYWORDS.put(QuestionCategory.SPRING, List.of(
                "spring", "boot", "bean", "ioc", "di", "dependency injection", "controller", "service",
                "repository", "hibernate", "jpa", "transaction", "actuator", "security"
        ));
        KEYWORDS.put(QuestionCategory.DSA, List.of(
                "array", "linked list", "stack", "queue", "tree", "graph", "heap", "trie", "sort",
                "search", "dynamic programming", "recursion", "complexity", "big o", "algorithm"
        ));
        KEYWORDS.put(QuestionCategory.DATABASE, List.of(
                "database", "sql", "postgres", "postgresql", "mysql", "oracle", "index", "join",
                "normalization", "acid", "transaction", "isolation", "query", "table", "schema"
        ));
        KEYWORDS.put(QuestionCategory.SYSTEM_DESIGN, List.of(
                "system design", "scalability", "availability", "load balancer", "cache", "redis",
                "microservice", "message queue", "kafka", "sharding", "replication", "rate limit",
                "consistent hashing", "architecture"
        ));
    }

    public QuestionCategory categorize(String question, String answer) {
        String text = (question + " " + answer).toLowerCase(Locale.ROOT);
        QuestionCategory bestCategory = QuestionCategory.JAVA;
        int bestScore = -1;

        for (Map.Entry<QuestionCategory, List<String>> entry : KEYWORDS.entrySet()) {
            int score = entry.getValue().stream()
                    .mapToInt(keyword -> text.contains(keyword) ? keyword.length() : 0)
                    .sum();
            if (score > bestScore) {
                bestScore = score;
                bestCategory = entry.getKey();
            }
        }

        return bestCategory;
    }
}

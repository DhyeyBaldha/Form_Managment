package com.example.project_interview.generator;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class IdGenerator {



    public String generateFormId(List<String> existingIds) {
        String prefix = "FORM-";

        int nextSequence = existingIds.stream()
                .filter(id -> id.startsWith(prefix))
                .map(id -> Integer.parseInt(id.substring(prefix.length())))
                .max(Integer::compare)
                .orElse(0) + 1;

        return prefix + nextSequence;
    }

}

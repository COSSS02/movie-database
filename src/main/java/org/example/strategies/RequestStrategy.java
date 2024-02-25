package org.example.strategies;

import org.example.interfaces.ExperienceStrategy;

public class RequestStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 3;
    }
}
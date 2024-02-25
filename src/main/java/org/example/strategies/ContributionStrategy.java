package org.example.strategies;

import org.example.interfaces.ExperienceStrategy;

public class ContributionStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 5;
    }
}
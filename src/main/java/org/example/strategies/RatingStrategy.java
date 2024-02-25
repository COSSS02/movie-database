package org.example.strategies;

import org.example.interfaces.ExperienceStrategy;

public class RatingStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 1;
    }
}
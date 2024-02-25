package org.example.utils;

import org.example.IMDB;
import org.example.interfaces.NameField;
import org.example.productions.Rating;
import org.example.users.User;

import java.util.Comparator;

public class RatingsComparator implements Comparator<Rating> {

    @Override
    public int compare(Rating o1, Rating o2) {
        String exp1 = ((User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                o1.getUsername())).getExperience();

        String exp2 = ((User<NameField>) ManageLists.searchInList(IMDB.getInstance().getUsers(),
                o2.getUsername())).getExperience();

        Integer num1 = 0;
        if (exp1 != null)
            num1 = Integer.parseInt(exp1);

        Integer num2 = 0;
        if (exp2 != null)
            num2 = Integer.parseInt(exp2);
        
        return num1.compareTo(num2);
    }
}
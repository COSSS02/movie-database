package org.example.users;

import org.example.enums.AccountType;
import org.example.interfaces.NameField;

public class UserFactory {

    public static User<NameField> getUser(AccountType userType, String username, String experience) {
        User<NameField> user = null;

        switch (userType) {
            case Regular -> user = new Regular(username, experience);

            case Contributor -> user = new Contributor(username, experience);

            case Admin -> user = new Admin(username, experience);
        }

        return user;
    }
}
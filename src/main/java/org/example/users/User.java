package org.example.users;

import org.example.interfaces.ExperienceStrategy;
import org.example.interfaces.NameField;
import org.example.interfaces.Observer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;

public abstract class User<T extends NameField> implements NameField, Observer {
    private String username;
    private String experience;
    private final List<String> notifications;
    private final SortedSet<T> favorites;
    private Information information;

    public User(String username, String experience) {
        this.username = username;
        this.experience = experience;
        notifications = new ArrayList<>();
        favorites = new TreeSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExperience() {
        return experience;
    }

    public void addExperience(ExperienceStrategy strategy) {
        this.experience = String.valueOf(Integer.parseInt(this.experience)
                + strategy.calculateExperience());
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void addFavorite(T favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    @Override
    public String getComparableValue() {
        return getUsername();
    }

    @Override
    public int compareTo(@NotNull NameField o) {
        return username.compareTo(o.getComparableValue());
    }

    @Override
    public void update(String message) {
        addNotification(message);
    }

    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDate birthDate;

        private Information(InformationBuilder builder) {
            credentials = builder.credentials;
            name = builder.name;
            country = builder.country;
            age = builder.age;
            gender = builder.gender;
            birthDate = builder.birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public static class InformationBuilder {
            private final Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthDate;

            public InformationBuilder(String email, String password) {
                credentials = new Credentials(email, password);
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }
}
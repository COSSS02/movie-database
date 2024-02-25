package org.example.interfaces;

import org.example.productions.*;

public interface StaffInterface {

    public void addProductionSystem(Production p);

    public void addActorSystem(Actor a);

    public void removeProductionSystem(String name);

    public void removeActorSystem(String name);
}
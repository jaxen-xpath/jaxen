package org.jaxen.test;

import java.util.Set;
import java.util.HashSet;

public class Person
{
    private String name;
    private int age;

    private Set brothers;

    Person(String name, int age)
    {
        this.name = name;
        this.age  = age;
        this.brothers = new HashSet();
    }

    public String getName()
    {
        return this.name;
    }

    public int getAge()
    {
        return this.age;
    }

    void addBrother(Person brother)
    {
        this.brothers.add( brother );
    }

    public Set getBrothers()
    {
        return this.brothers;
    }

    public String toString()
    {
        return "[Person: " + this.name + "]";
    }
}

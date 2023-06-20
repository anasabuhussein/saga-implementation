package org.saga.orch.example;

public class ExampleModel {

    private String name;
    private String exampleReference;

    public ExampleModel(String name, String exampleReference) {
        this.name = name;
        this.exampleReference = exampleReference;
    }

    public String getName() {
        return name;
    }

    public ExampleModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getExampleReference() {
        return exampleReference;
    }

    public ExampleModel setExampleReference(String exampleReference) {
        this.exampleReference = exampleReference;
        return this;
    }
}

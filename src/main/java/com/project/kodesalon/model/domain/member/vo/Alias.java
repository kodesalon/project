package com.project.kodesalon.model.domain.member.vo;

public class Alias {
    private String alias;

    public Alias (String alias) {
        this.alias = alias;
    }

    public String value() {
        return alias;
    }

    private String validateAlias(String alias) {
        return null;
    }
}

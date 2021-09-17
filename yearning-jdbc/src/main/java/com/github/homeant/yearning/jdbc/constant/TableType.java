package com.github.homeant.yearning.jdbc.constant;

public enum TableType {
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    SYSTEM_TABLE("SYSTEM TABLE"),
    SYSTEM_VIEW("SYSTEM VIEW"),
    TABLE("TABLE", new String[] { "BASE TABLE" }),
    VIEW("VIEW"),
    UNKNOWN("UNKNOWN");

    private String name;
    private String[] synonyms;

    TableType(String tableTypeName) {
        this(tableTypeName, null);
    }

    TableType(String tableTypeName, String[] tableTypeSynonyms) {
        this.name = tableTypeName;
        this.synonyms = tableTypeSynonyms;
    }

    public String getName() {
        return this.name;
    }

    boolean equalsTo(String tableTypeName) {
        return this.name.equalsIgnoreCase(tableTypeName);
    }

    public static TableType getTableTypeEqualTo(String tableTypeName) {
        for (TableType tableType : TableType.values()) {
            if (tableType.equalsTo(tableTypeName)) {
                return tableType;
            }
        }
        return UNKNOWN;
    }

    public boolean compliesWith(String tableTypeName) {
        if (equalsTo(tableTypeName)) {
            return true;
        }
        if (this.synonyms != null) {
            for (String synonym : this.synonyms) {
                if (synonym.equalsIgnoreCase(tableTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TableType getTableTypeCompliantWith(String tableTypeName) {
        for (TableType tableType : TableType.values()) {
            if (tableType.compliesWith(tableTypeName)) {
                return tableType;
            }
        }
        return UNKNOWN;
    }
}

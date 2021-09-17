package com.github.homeant.yearning.jdbc.constant;

import com.github.homeant.yearning.jdbc.result.JdbcType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

@Getter
public class TypeDescriptor {

    Integer datetimePrecision = null;
    Integer columnSize = null;

    Integer decimalDigits = null;

    String isNullable;

    int nullability;

    JdbcType jdbcType;

    public TypeDescriptor(String typeInfo, String nullabilityInfo) throws SQLException {
        if (typeInfo == null) {
            throw new SQLNonTransientConnectionException("类型无法转换", "S1009");
        }

        this.jdbcType = JdbcType.getByName(typeInfo);

        // Figure Out the Size

        java.util.StringTokenizer tokenizer;
        int maxLength = 0;
        int fract;

        switch (this.jdbcType) {
            case ENUM:
                tokenizer = new java.util.StringTokenizer(getContent(typeInfo), ",");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    maxLength = Math.max(maxLength, (nextToken.length() - 2));
                }
                this.columnSize = maxLength;
                break;

            case SET:
                tokenizer = new java.util.StringTokenizer(getContent(typeInfo), ",");

                int numElements = tokenizer.countTokens();
                if (numElements > 0) {
                    maxLength += (numElements - 1);
                }

                while (tokenizer.hasMoreTokens()) {
                    String setMember = tokenizer.nextToken().trim();

                    if (setMember.startsWith("'") && setMember.endsWith("'")) {
                        maxLength += setMember.length() - 2;
                    } else {
                        maxLength += setMember.length();
                    }
                }
                this.columnSize = maxLength;
                break;

            case FLOAT:
            case FLOAT_UNSIGNED:
                if (typeInfo.contains(",")) {
                    // Numeric with decimals
                    this.columnSize = Integer.valueOf(getLength(typeInfo));
                    this.decimalDigits = Integer.valueOf(getPrecision(typeInfo));
                } else if (typeInfo.contains("(")) {
                    int size = Integer.parseInt(getLength(typeInfo));
                    if (size > 23) {
                        this.jdbcType = this.jdbcType == JdbcType.FLOAT ? JdbcType.DOUBLE : JdbcType.DOUBLE_UNSIGNED;
                        this.columnSize = 22;
                        this.decimalDigits = 0;
                    }
                } else {
                    this.columnSize = 12;
                    this.decimalDigits = 0;
                }
                break;
            case DECIMAL:
            case DECIMAL_UNSIGNED:
            case DOUBLE:
            case DOUBLE_UNSIGNED:
                if (typeInfo.contains(",")) {
                    // Numeric with decimals
                    this.columnSize = Integer.valueOf(getLength(typeInfo));
                    this.decimalDigits = Integer.valueOf(getPrecision(typeInfo));
                } else {
                    switch (this.jdbcType) {
                        case DECIMAL:
                        case DECIMAL_UNSIGNED:
                            this.columnSize = 65;
                            break;
                        case DOUBLE:
                        case DOUBLE_UNSIGNED:
                            this.columnSize = 22;
                            break;
                        default:
                            break;
                    }
                    this.decimalDigits = 0;
                }
                break;

            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case JSON:
            case TEXT:
            case TINYBLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BLOB:
            case BINARY:
            case VARBINARY:
            case BIT:
                if (this.jdbcType == JdbcType.CHAR) {
                    this.columnSize = 1;
                }
                if (typeInfo.contains("(")) {
                    int endParenIndex = typeInfo.indexOf(")");

                    if (endParenIndex == -1) {
                        endParenIndex = typeInfo.length();
                    }
                    this.columnSize = Integer.valueOf(typeInfo.substring((typeInfo.indexOf("(") + 1), endParenIndex).trim());
                    if (this.columnSize == 1 && StringUtils.startsWithIgnoreCase(typeInfo, "tinyint")) {
                        this.jdbcType = JdbcType.BIT;
                    }
                }

                break;

            case TINYINT:
                if(typeInfo.contains("(1)")){
                    this.jdbcType = JdbcType.BIT;
                    this.columnSize = 1;
                }else {
                    this.columnSize = 3;
                }
                break;
            case TINYINT_UNSIGNED:
                this.columnSize = 3;
                break;
            case DATE:
                this.datetimePrecision = 0;
                this.columnSize = 10;
                break;

            case TIME:
                this.datetimePrecision = 0;
                this.columnSize = 8;
                if (typeInfo.contains("(")
                        && (fract = Integer.parseInt(getLength(typeInfo))) > 0) {
                    // with fractional seconds
                    this.datetimePrecision = fract;
                    this.columnSize += fract + 1;
                }
                break;

            case DATETIME:
            case TIMESTAMP:
                this.datetimePrecision = 0;
                this.columnSize = 19;
                if (typeInfo.contains("(")
                        && (fract = Integer.parseInt(getLength(typeInfo))) > 0) {
                    // with fractional seconds
                    this.datetimePrecision = fract;
                    this.columnSize += fract + 1;
                }
                break;

            case BOOLEAN:
            case UNKNOWN:
            case GEOMETRY:
            case NULL:
            case YEAR:

            default:
        }

        if(this.columnSize == null){
            this.columnSize = this.jdbcType.getPrecision() > Integer.MAX_VALUE ? Integer.MAX_VALUE : this.jdbcType.getPrecision().intValue();
        }

        // Nullable?
        if (nullabilityInfo != null) {
            if (nullabilityInfo.equals("YES")) {
                this.nullability = java.sql.DatabaseMetaData.columnNullable;
                this.isNullable = "YES";

            } else if (nullabilityInfo.equals("UNKNOWN")) {
                this.nullability = java.sql.DatabaseMetaData.columnNullableUnknown;
                this.isNullable = "";

                // IS_NULLABLE
            } else {
                this.nullability = java.sql.DatabaseMetaData.columnNoNulls;
                this.isNullable = "NO";
            }
        } else {
            this.nullability = java.sql.DatabaseMetaData.columnNoNulls;
            this.isNullable = "NO";
        }
    }

    private String getContent(String typeInfo){
        return typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
    }

    private String getLength(String typeInfo){
        if(typeInfo.contains(",")){
            return typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim();
        }
        return typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(")")).trim();
    }

    private String getPrecision(String typeInfo){
        return typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim();
    }
}

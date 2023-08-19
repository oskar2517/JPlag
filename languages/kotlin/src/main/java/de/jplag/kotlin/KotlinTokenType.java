package de.jplag.kotlin;

import de.jplag.TokenType;

public enum KotlinTokenType implements TokenType {
    PACKAGE("PACKAGE"),
    IMPORT("IMPORT"),
    CLASS_DECLARATION("CLASS"),
    OBJECT_DECLARATION("OBJECT"),
    COMPANION_DECLARATION("COMPANION"),

    TYPE_PARAMETER("<T>"),
    CONSTRUCTOR("CONSTRUCTOR"),

    CLASS_BODY_BEGIN("CLASS{"),
    CLASS_BODY_END("}CLASS"),

    ENUM_CLASS_BODY_BEGIN("ENUM{"),
    ENUM_CLASS_BODY_END("}ENUM"),

    PROPERTY_DECLARATION("PROPERTY"),

    INITIALIZER("INIT"),
    INITIALIZER_BODY_START("INIT{"),
    INITIALIZER_BODY_END("}INIT"),

    FUNCTION("FUN"),

    GETTER("GET"),
    SETTER("SET"),
    FUNCTION_PARAMETER("PARAM"),
    FUNCTION_BODY_BEGIN("FUN{"),
    FUNCTION_BODY_END("}FUN"),

    FUNCTION_LITERAL_BEGIN("FUNC_LIT{"),
    FUNCTION_LITERAL_END("}FUNC_LIT"),

    FOR_EXPRESSION_BEGIN("FOR"),
    FOR_EXPRESSION_END("}FOR"),

    IF_EXPRESSION_BEGIN("IF"),
    IF_EXPRESSION_END("}IF(-ELSE)"),

    WHILE_EXPRESSION_START("WHILE"),
    WHILE_EXPRESSION_END("}WHILE"),

    DO_WHILE_EXPRESSION_START("DO"),
    DO_WHILE_EXPRESSION_END("}DO-WHILE"),

    TRY_EXPRESSION("TRY"),
    TRY_BODY_START("TRY{"),
    TRY_BODY_END("}TRY"),
    CATCH("CATCH"),
    CATCH_BODY_START("CATCH{"),
    CATCH_BODY_END("}CATCH"),
    FINALLY("FINALLY"),
    FINALLY_BODY_START("FINALLY{"),
    FINALLY_BODY_END("}FINALLY"),
    WHEN_EXPRESSION_START("WHEN"),
    WHEN_EXPRESSION_END("}WHEN"),
    WHEN_CONDITION("COND"),

    CONTROL_STRUCTURE_BODY_START("THEN{"),
    CONTROL_STRUCTURE_BODY_END("}THEN"),

    VARIABLE_DECLARATION("VARDECL"),
    ENUM_ENTRY("ENUM_ENTRY"),
    FUNCTION_INVOCATION("INVOC"),
    CREATE_OBJECT("CONST"),
    ASSIGNMENT("ASSIGN"),

    THROW("THROW"),
    RETURN("RETURN"),
    CONTINUE("CONTINUE"),
    BREAK("BREAK");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    KotlinTokenType(String description) {
        this.description = description;
    }
}

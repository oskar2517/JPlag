package de.jplag.rust;

import de.jplag.TokenType;

public enum RustTokenType implements TokenType {
    INNER_ATTRIBUTE("INNER_ATTR"),
    OUTER_ATTRIBUTE("OUTER_ATTR"),

    USE_DECLARATION("USE"),
    USE_ITEM("USE_ITEM"),

    MODULE("MODULE"),
    MODULE_START("MODULE{"),
    MODULE_END("}MODULE"),

    FUNCTION("FUNCTION"),
    TYPE_PARAMETER("<T>"),
    FUNCTION_PARAMETER("PARAM"),
    FUNCTION_BODY_START("FUNC{"),
    FUNCTION_BODY_END("}FUNC"),

    STRUCT("STRUCT"),
    STRUCT_BODY_START("STRUCT{"),
    STRUCT_BODY_END("}STRUCT"),
    STRUCT_INITIALISATION("STRUCT()"),

    STRUCT_FIELD("FIELD"),

    UNION("UNION"),
    UNION_BODY_START("UNION{"),
    UNION_BODY_END("}UNION"),

    TRAIT("TRAIT"),
    TRAIT_BODY_START("TRAIT{"),
    TRAIT_BODY_END("}TRAIT"),

    IMPLEMENTATION("IMPL"),
    IMPLEMENTATION_BODY_START("IMPL{"),
    IMPLEMENTATION_BODY_END("}IMPL"),

    ENUM("ENUM"),
    ENUM_BODY_START("ENUM{"),
    ENUM_BODY_END("}ENUM"),
    ENUM_ITEM("ENUM_ITEM"),

    MACRO_RULES_DEFINITION("MACRO_RULES"),
    MACRO_RULES_DEFINITION_BODY_START("MACRO_RULES{"),
    MACRO_RULES_DEFINITION_BODY_END("}MACRO_RULES"),

    MACRO_RULE("MACRO_RULE"),
    MACRO_RULE_BODY_START("MACRO_RULE{"),
    MACRO_RULE_BODY_END("}MACRO_RULE"),

    MACRO_INVOCATION("MACRO()"),
    MACRO_INVOCATION_BODY_START("MACRO(){"),
    MACRO_INVOCATION_BODY_END("}MACRO()"),

    EXTERN_BLOCK("EXTERN"),
    EXTERN_BLOCK_START("EXTERN{"),
    EXTERN_BLOCK_END("}EXTERN"),
    TYPE_ALIAS("TYPE_ALIAS"),
    STATIC_ITEM("STATIC"),

    EXTERN_CRATE("EXTERN"),

    IF_STATEMENT("IF"),
    IF_BODY_START("IF{"),
    IF_BODY_END("}IF"),
    ELSE_STATEMENT("ELSE"),
    ELSE_BODY_START("ELSE{"),
    ELSE_BODY_END("ELSE}"),

    LABEL("LABEL"),
    LOOP_STATEMENT("LOOP"),
    LOOP_BODY_START("LOOP{"),
    LOOP_BODY_END("}LOOP"),
    FOR_STATEMENT("FOR"),
    FOR_BODY_START("FOR{"),
    FOR_BODY_END("}FOR"),

    BREAK("BREAK"),

    MATCH_EXPRESSION("MATCH"),
    MATCH_BODY_START("MATCH{"),
    MATCH_BODY_END("}MATCH"),
    MATCH_CASE("CASE"),
    MATCH_GUARD("GUARD"),

    INNER_BLOCK_START("INNER{"),
    INNER_BLOCK_END("}INNER"),

    ARRAY_BODY_START("ARRAY{"),
    ARRAY_BODY_END("}ARRAY"),
    ARRAY_ELEMENT("ARRAY_ELEM"),

    TUPLE("TUPLE"),
    TUPLE_START("TUPLE("),
    TUPLE_END(")TUPLE"),
    TUPLE_ELEMENT("T_ELEM"),

    CLOSURE("CLOSURE"),
    CLOSURE_BODY_START("CLOSURE{"),
    CLOSURE_BODY_END("}CLOSURE"),

    APPLY("APPLY"),
    ARGUMENT("ARG"),
    ASSIGNMENT("ASSIGN"),

    VARIABLE_DECLARATION("VAR_DECL"),

    TYPE_ARGUMENT("T_ARG"),

    RETURN("RETURN");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    RustTokenType(String description) {
        this.description = description;
    }

}

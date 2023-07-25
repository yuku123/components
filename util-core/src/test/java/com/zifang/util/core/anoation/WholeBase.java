package com.zifang.util.core.anoation;

@ClassInfo(className = "test")
class WholeBase {

    @FieldInfo(name = "a-private", password = "a-private")
    private String a;
    @FieldInfo(name = "b-private", password = "b-private")
    public String b;

    @ConstructInfo(constructName = "wholeBase constructName")
    public WholeBase(@ParameterInfo(setString = "test b") String b) {
    }

    @ConstructInfo(constructName = "wholeBase private constructName---parameter a")
    private WholeBase(String a, @ParameterInfo(setString = "test b") String b) {
    }

    @Override
    @Deprecated
    @SuppressWarnings({"unchecked", "deprecation"})
    @MethodInfo(author = "Pankaj", comments = "Main method", date = "Nov 17 2012", revision = 1)
    public String toString() {
        return "Overriden toString method";
    }

    @Deprecated
    @SuppressWarnings({"unchecked", "deprecation"})
    @MethodInfo(author = "sddsd", comments = "aa private", date = "sdsadasdsadsadsa", revision = 1)
    private String aa() {
        return "aa private";
    }

}

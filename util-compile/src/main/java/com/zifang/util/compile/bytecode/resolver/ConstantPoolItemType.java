package com.zifang.util.compile.bytecode.resolver;

public interface ConstantPoolItemType {
	
	int UTF8 = 1;
	int INTEGER = 3;
	int FLOAT = 4;
	int LONG = 5;
	int DOUBLE = 6;
	int CLASS = 7;
	int STRING = 8;
	int FIELD_REF = 9;
	int METHOD_REF = 10;
	int INTERFACE_METHOD_REF = 11;
	int NAME_ANT_TYPE = 12;
	int METHOD_HANDLE = 15;
	int METHOD_TYPE = 16;
	int INVOKE_DYNAMIC = 18;
}

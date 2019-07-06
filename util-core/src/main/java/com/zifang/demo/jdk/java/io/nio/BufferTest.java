package com.zifang.demo.jdk.java.io.nio;

import java.nio.CharBuffer;

public class BufferTest {
	//��ʼ��BUFFER
	private CharBuffer buffer=CharBuffer.allocate(100);
	
	//��ĳ��BUFFER�������ݴ���
	private void input(CharBuffer buffer,String str){
		if(str.length()<=0){
			System.out.println("����Ϊ���ֶ�");
		}else if(str.length()>0){
			for(int i=0;i<str.length();i++){
				buffer.put(str.charAt(i));
			}
		}
	}
	//��ĳ��Buffer�����������
	private void output(CharBuffer buffer){
		//��תbuffer,�Ա�������
		buffer.flip();
		//�������ʣ��ֵ����������
		while(buffer.hasRemaining()){
			System.out.print(buffer.get());
		}
		//���buffer��ʹ��������½����������
		buffer.clear();
	}
	public BufferTest(String str){
		this.input(buffer, str);
		this.output(buffer);
	}

	public static void main(String[] args) {
		new BufferTest("�Է�");

	}

}

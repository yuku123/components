һ�������������

    XPath ��һ���� XML �ĵ��в�����Ϣ�����ԣ� �������� XML �ĵ��ж�Ԫ�غ����Խ��б�����XPath �� W3C XSLT ��׼����ҪԪ�أ����� XQuery �� XPointer ͬʱ�������� XPath ���֮�ϡ���ˣ��� XPath ������Ǻܶ�߼� XML Ӧ�õĻ�����
    XPath�ǳ����ƶ����ݿ������SQL���ԣ�����˵JQuery�������Է��㿪����ץ���ĵ�����Ҫ�Ķ�������dom4jҲ֧��xpath

   1.�ڵ�����

    XPath�������ֽ�����ͣ�Ԫ�ء����ԡ��ı��������ռ䡢����ָ�ע���Լ��ĵ��ڵ㣨���Ϊ���ڵ㣩���ĵ��д���Ԫ�ؽڵ㣬���Խڵ㣬���ڵ�

   2.����·�����ʽ


���ʽ
���� 
�ڵ�����(nodename)	ѡȡ�˽ڵ�������ӽڵ�
/	�Ӹ��ڵ�ѡȡ
//	��ƥ��ѡ��ĵ�ǰ�ڵ�ѡ���ĵ��еĽڵ㣬�����������ǵ�λ��
.	ѡȡ��ǰ�ڵ�
..	ѡȡ��ǰ�ڵ�ĸ��ڵ�
@	ѡȡ����
  
ʾ�����£�

//@lang	ѡȡ������Ϊ lang ������
 3.�޶���

��������ĳ���ض��Ľڵ���߰���ĳ��ָ����ֵ�Ľڵ㡣�Է���������

//book[price>35.00]	ѡ������book Ԫ�أ������е� price Ԫ�ص�ֵ����� 35.00
/bookstore/book[1]	ѡȡ���� bookstore ��Ԫ�صĵ�һ�� book Ԫ�ء� 
/bookstore/book[last()]	ѡȡ���� bookstore ��Ԫ�ص����һ�� book Ԫ�ء� 
/bookstore/book[last()-1]	ѡȡ���� bookstore ��Ԫ�صĵ����ڶ��� book Ԫ�ء� 
/bookstore/book[position()<3]	ѡȡ��ǰ����������� bookstore Ԫ�ص���Ԫ�ص� book Ԫ�ء� 
//title[@lang]	ѡȡ����ӵ����Ϊ lang �����Ե� title Ԫ�ء� 
//title[@lang='eng']	ѡȡ���� title Ԫ�أ�����ЩԪ��ӵ��ֵΪ eng �� lang ���ԡ� 
/bookstore/book[price>35.00]	ѡȡ���� bookstore Ԫ�ص� book Ԫ�أ������е� price Ԫ�ص�ֵ����� 35.00�� 
/bookstore/book[price>35.00]/title	ѡȡ���� bookstore Ԫ���е� book Ԫ�ص� title Ԫ�أ������е� price Ԫ�ص�ֵ����� 35.00��  4 .ͨ���
ͨ���	���� 
*	ƥ���κ�Ԫ�ؽڵ� 
@*	ƥ���κ����Խڵ� 
node()	ƥ���κ����͵Ľڵ� 
|	ѡȡ����·��  
ʹ��ʾ��
·�����ʽ	��� 
/bookstore/*	ѡȡ bookstore Ԫ�ص������ӽڵ� 
//*	ѡȡ�ĵ��е�����Ԫ�� 
//title[@*]	ѡȡ���д������Ե� title Ԫ�ء� 
//book/title | //book/price	ѡȡ���� book Ԫ�ص� tilte �� price Ԫ�ء� 
//title | //price	ѡȡ�����ĵ��е� title �� price Ԫ�ء� 
/bookstore/book/title | //price	ѡȡ�������� bookstore Ԫ�ص� book Ԫ�ص� title Ԫ�أ��Լ��ĵ������е� price Ԫ��
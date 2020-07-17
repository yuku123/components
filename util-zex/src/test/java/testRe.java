import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testRe {
    public static void main(String[] args) {
        //查找其中的每个数字
        String str = "findByAAndBAndCOrD";
        String reg = "^findBy(.+And)*?(.*)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            for(int i=0;i<100;i++){
                System.out.println(i+":"+matcher.group(i));
            }
        }
    }
}

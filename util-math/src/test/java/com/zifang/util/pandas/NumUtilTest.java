package com.zifang.util.pandas;

import com.zifang.util.pandas.num.Num;
import com.zifang.util.pandas.num.NumUtil;
import org.junit.Test;

public class NumUtilTest {

    @Test
    public void test(){
        Number[][] arr = {{1,2}, {1,2}, {1,2L,1.2}};
        Num num = NumUtil.array(arr);

        assert num.nDim() == 2;
        assert num.shape() == 2;
        assert num.shape() == 2;
        assert num.size() == 2;
    }


}

package com.zifang.util.core.demo.thirdpart.jar.guava.BasicUtilities;

import com.google.common.base.Preconditions;

public class Precondition {

    private void testPreconditions(boolean preCondition, int[] array, int position)
    {
        Preconditions.checkArgument(preCondition);
        Preconditions.checkNotNull(array);
        Preconditions.checkElementIndex(position, array.length, "position error!");
        //do something...
    }
}

package com.zifang.projects.cube;

import java.util.ArrayList;
import java.util.List;

public class MagicCube {

    private Integer dimension;

    private List<MagicCubeDust> magicCubeDustList = new ArrayList();

    public MagicCube(Integer dimension){
        this.dimension = dimension;
        for(int i = 0; i < dimension ; i++){
            for(int j = 0; j < dimension ; j++){
                for(int k = 0; k < dimension ; k++){
                    magicCubeDustList.add(new MagicCubeDust(i,j,k));
                }
            }
        }
    }

    public void show(){

        System.out.println(magicCubeDustList);
    }

}

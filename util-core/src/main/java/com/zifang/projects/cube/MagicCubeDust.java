package com.zifang.projects.cube;

/**
 * 每个魔方的标准颗粒点
 * */
public class MagicCubeDust extends Point{

    public MagicCubeDust(Integer x,Integer y,Integer z){
        this.x = x;
        this.x_point = x;
        this.y = y;
        this.y_point = y;
        this.z = z;
        this.z_point = z;
    }

    @Override
    public String toString(){
        return "("+x+","+y+","+z+")";
    }

}

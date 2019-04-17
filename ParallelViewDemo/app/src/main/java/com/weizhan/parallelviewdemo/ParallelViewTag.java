package com.weizhan.parallelviewdemo;

/**
 * Created by Administrator on 2019/4/17.
 */

public class ParallelViewTag {
    protected float xIn;
    protected float xOut;
    protected float yIn;
    protected float yOut;
    protected float alphaIn;
    protected float alphaOut;


    @Override
    public String toString() {
        return "ParallaxViewTag [index=" +  ", xIn=" + xIn + ", xOut="
                + xOut + ", yIn=" + yIn + ", yOut=" + yOut + ", alphaIn="
                + alphaIn + ", alphaOut=" + alphaOut + "]";
    }
}

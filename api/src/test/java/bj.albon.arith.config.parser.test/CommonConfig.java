package bj.albon.arith.config.parser.test;

import bj.albon.arith.config.parser.api.annotation.AutoParseField;

import java.math.BigInteger;
import java.util.List;

/**
 * Author: pengyu.song
 * Date: 2018/7/8
 */
public class CommonConfig {

    private static final CommonConfig INSTANE = new CommonConfig();

    public static CommonConfig getINSTANE() {
        return INSTANE;
    }

    @AutoParseField(key = "age")
    private int age;

    @AutoParseField(key = "person.list")
    private List<String> personList;

    @AutoParseField(key = "is.open")
    private boolean isOpen = false;

    @AutoParseField(key = "bint", parser = BigIntegerFieldParser.class)
    private BigInteger bint;

    public BigInteger getBint() {
        return bint;
    }

    public void setBint(BigInteger bint) {
        this.bint = bint;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getPersonList() {
        return personList;
    }

    public void setPersonList(List<String> personList) {
        this.personList = personList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return "CommonConfig{" +
                "age=" + age +
                ", personList=" + personList +
                ", isOpen=" + isOpen +
                '}';
    }
}

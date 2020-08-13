package myTest;

import java.io.*;

public class MyTest {
    public static void main(String[] args) throws IOException {
        String[] strArr;
        String[] arr1 = new String[]{"1", "2"};
        String[] arr2 = new String[]{"3", "4", "5"};

        strArr = arr1;
        System.out.println(strArr.length);
        strArr = arr2;
        System.out.println(strArr.length);
        strArr = arr1;
        System.out.println(strArr.length);
    }
}
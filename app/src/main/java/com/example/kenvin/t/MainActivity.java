package com.example.kenvin.t;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        testBox();
    }

    private void testBox() {

        //<? extends E> 是 Upper Bound（上限） 的通配符，用来限制元素的类型的上限
        Box<? extends Number> box = new Box<>();

        //box.setData(new Integer(1));
        //box.setData(new Float(2));

        //Integer是Number的子类  Box<Number>不是Box<Integer>的父类,所以会报错。
        box = new Box<Integer>();
        box = new Box<Float>();
        box = new Box<Double>();

        //只能装Number以下的类型
        //box = new Box<Object>();

        //<? super E> 是 Lower Bound（下限） 的通配符 ，用来限制元素的类型下限
        Box<? super Number> boxs = new Box<>();
        //boxs = new Box<Integer>();
        //boxs = new Box<Double>();

        //下限只能是Number,上限是Object类型
        boxs = new Box<Object>();

        News news = new News();
        news.setId(1);
        news.setTitle("新年放假通知");
        news.setContent("从今天开始放假啦。");
        news.setAuthor(createAuthor());
        news.setReader(createReaders());
        Log.d("TAG", FastJson.toJson(news));
    }


    private static List<User> createReaders() {
        List<User> readers = new ArrayList<User>();
        User readerA = new User();
        readerA.setId(2);
        readerA.setName("Jack");
        readers.add(readerA);

        User readerB = new User();
        readerB.setId(1);
        readerB.setName("Lucy");
        readers.add(readerB);

        return readers;
    }

    private static User createAuthor() {
        User author = new User();
        author.setId(1);
        author.setName("Fancyy");
        author.setPwd("123456");
        return author;
    }
}

package com.magnify.utils.bean;

import com.example.datautils.RandomUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by heinigger on 16/8/8.
 */
public class MultiLayoutBean {
    //总共五种类型的数据
    private int type;
    private Object object;
    private static transient Random random;
    private static final int TYPECOUNT = 7;

    static {
        random = new Random();
    }

    public MultiLayoutBean() {
        this.type = random.nextInt(TYPECOUNT);
        switch (type) {
            case 0:
                this.object = RandomUtil.getRandomImage();
                break;
            case 1:
                this.object = RandomUtil.getRandomImage(random.nextInt(6));
                break;
            case 2:
                this.object = null;
                break;
            case 3:
                this.object = RandomUtil.createRandomUser();
                break;
            case 4:
                this.object = RandomUtil.createPhoneNumber();
                break;
            case 5:
                this.object = RandomUtil.createRandomUser(random.nextInt(15));
                break;
            case 6:
                this.object = RandomUtil.createRandomUser(random.nextInt(15));
                break;
        }
    }

    public int getType() {
        return type;
    }


    public <E> E getObject() {
        return (E) object;
    }

    public static ArrayList<MultiLayoutBean> createData(int count) {
        ArrayList<MultiLayoutBean> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            datas.add(new MultiLayoutBean());
        }
        return datas;
    }

}

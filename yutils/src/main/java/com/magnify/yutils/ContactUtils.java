package com.magnify.yutils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by 黄艳武 on 2016/3/8.
 * 手机通讯录,联系人查询
 */
public class ContactUtils {
    /***
     * String phoneNumber = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
     * String userName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
     * String userThumBnail = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
     * String rawContactID = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
     */
    private onContactSearchListener onContactSearchListener;

    /*查询所有的联系人*/
    public void queryContacts(Context context, onContactSearchListener onContactSearchListener) {
        //获取用来操作数据的类的对象，对联系人的基本操作都是使用这个对象
        ContentResolver cr = context.getContentResolver();
        //  查询contacts表的所有记录
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        // 如果记录不为空
        if (cursor.getCount() > 0) {
            //游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
            //+下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
            while (cursor.moveToNext()) {
                String rawContactId = "";
                //  从Contacts表当中取得ContactId
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Log.v("contactID", id);
                // 获取RawContacts表的游标
                Cursor rawContactCur = cr.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts._ID + "=?", new String[]{id}, null);
                //该查询结果一般只返回一条记录，所以我们直接让游标指向第一条记录
                if (rawContactCur.moveToFirst()) {
                    //读取第一条记录的RawContacts._ID列的值
                    rawContactId = rawContactCur.getString(rawContactCur.getColumnIndex(ContactsContract.RawContacts._ID));
                    Log.v("rawContactID", rawContactId);
                }
                //关闭游标
                rawContactCur.close();
                //读取号码
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //根据查询RAW_CONTACT_ID查询该联系人的号码
                    Cursor phoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "=?",
                            new String[]{rawContactId}, null);
                    // 上面的ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    //  可以用下面的phoneUri代替
                    Uri phoneUri = Uri.parse("content://com.android.contacts/data/phones");
                    //一个联系人可能有多个号码，需要遍历
                    while (phoneCur.moveToNext()) {
                        onContactSearchListener.query(phoneCur);
                    }
                    phoneCur.close();
                    onContactSearchListener.queryFinish();
                }
            }
            cursor.close();
        }
    }

    /*添加联系人*/
    public void addContact(String name, String phoneNum, Context context) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);

        long rawContactId = ContentUris.parseId(rawContactUri);

        //向data表中插入数据
        if (!TextUtils.isEmpty(name)) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }

        if (TextUtils.isEmpty(phoneNum)) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNum);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }

    }

    /*删除联系人*/
    public void deleteContact(long rawContactId, Context context) {
        context.getContentResolver().delete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId), null, null);
    }


    /***
     *  对数据进行查询
     */
    public interface onContactSearchListener {
        //对查询的细节进行操作
        void query(Cursor phoneCur);

        //查询完成
        void queryFinish();
    }


    /*更新联系人*/
    public void updataCotact(long rawContactId, Context context) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "13800138000");
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        String where = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] selectionArgs = new String[]{String.valueOf(rawContactId), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
        context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, where, selectionArgs);
    }

}

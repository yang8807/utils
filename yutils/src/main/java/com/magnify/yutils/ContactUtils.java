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
 * Created by ������ on 2016/3/8.
 * �ֻ�ͨѶ¼,��ϵ�˲�ѯ
 */
public class ContactUtils {
    /***
     * String phoneNumber = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
     * String userName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
     * String userThumBnail = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
     * String rawContactID = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
     */
    private onContactSearchListener onContactSearchListener;

    /*��ѯ���е���ϵ��*/
    public void queryContacts(Context context, onContactSearchListener onContactSearchListener) {
        //��ȡ�����������ݵ���Ķ��󣬶���ϵ�˵Ļ�����������ʹ���������
        ContentResolver cr = context.getContentResolver();
        //  ��ѯcontacts������м�¼
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        // �����¼��Ϊ��
        if (cursor.getCount() > 0) {
            //�α��ʼָ���ѯ����ĵ�һ����¼���Ϸ���ִ��moveToNext�������ж�
            //+��һ����¼�Ƿ���ڣ�������ڣ�ָ����һ����¼�����򣬷���false��
            while (cursor.moveToNext()) {
                String rawContactId = "";
                //  ��Contacts����ȡ��ContactId
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Log.v("contactID", id);
                // ��ȡRawContacts����α�
                Cursor rawContactCur = cr.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts._ID + "=?", new String[]{id}, null);
                //�ò�ѯ���һ��ֻ����һ����¼����������ֱ�����α�ָ���һ����¼
                if (rawContactCur.moveToFirst()) {
                    //��ȡ��һ����¼��RawContacts._ID�е�ֵ
                    rawContactId = rawContactCur.getString(rawContactCur.getColumnIndex(ContactsContract.RawContacts._ID));
                    Log.v("rawContactID", rawContactId);
                }
                //�ر��α�
                rawContactCur.close();
                //��ȡ����
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //���ݲ�ѯRAW_CONTACT_ID��ѯ����ϵ�˵ĺ���
                    Cursor phoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "=?",
                            new String[]{rawContactId}, null);
                    // �����ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    //  �����������phoneUri����
                    Uri phoneUri = Uri.parse("content://com.android.contacts/data/phones");
                    //һ����ϵ�˿����ж�����룬��Ҫ����
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

    /*�����ϵ��*/
    public void addContact(String name, String phoneNum, Context context) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);

        long rawContactId = ContentUris.parseId(rawContactUri);

        //��data���в�������
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

    /*ɾ����ϵ��*/
    public void deleteContact(long rawContactId, Context context) {
        context.getContentResolver().delete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId), null, null);
    }


    /***
     *  �����ݽ��в�ѯ
     */
    public interface onContactSearchListener {
        //�Բ�ѯ��ϸ�ڽ��в���
        void query(Cursor phoneCur);

        //��ѯ���
        void queryFinish();
    }


    /*������ϵ��*/
    public void updataCotact(long rawContactId, Context context) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "13800138000");
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        String where = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] selectionArgs = new String[]{String.valueOf(rawContactId), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
        context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, where, selectionArgs);
    }

}

package com.magnify.utils.ui.ui_adapter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.magnify.basea_dapter_library.ViewHolder
import com.magnify.basea_dapter_library.recyclerview.CommonAdapter
import com.magnify.basea_dapter_library.recyclerview.DividerItemDecoration
import com.magnify.utils.R
import com.magnify.utils.base.CurrentBaseActivity
import com.magnify.utils.bean.People
import com.magnify.utils.ui.common.ImageBrowseActivity
import java.util.*

/**
 * Created by heinigger on 16/8/3.
 */
class CreateDataUtilsActivity : CurrentBaseActivity() {
    private var peoples: ArrayList<People>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        peoples = People.createPeople(200)
        val recyclerView = findViewById(R.id.recyler) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(self)
        recyclerView.addItemDecoration(DividerItemDecoration(self, LinearLayoutManager.HORIZONTAL))
        recyclerView.adapter = object : CommonAdapter<People>(self, R.layout.item_header_contact, peoples) {
            override fun convert(holder: ViewHolder, position: Int, people: People) {
                holder.setText(R.id.tv_userName, people.userName).setText(R.id.tv_age, "${people.age}岁  ${people.sex}").setText(R.id.tv_phone, people.phone)
                holder.displayRoundImage(people.avators, R.id.img_avators, R.mipmap.ic_launcher)
                holder.convertView.setOnClickListener { self.startActivity(ImageBrowseActivity.getIntent(self, peoples, position)) }
            }
        }
    }
}

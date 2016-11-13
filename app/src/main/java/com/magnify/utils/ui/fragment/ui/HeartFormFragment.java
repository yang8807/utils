package com.magnify.utils.ui.fragment.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.datautils.RandomUtil;
import com.example.datautils.User;
import com.magnify.basea_dapter_library.ViewHolder;
import com.magnify.basea_dapter_library.abslistview.CommonAdapter;
import com.magnify.utils.R;
import com.yan.fastview_library.fragment.BaseFragment;
import com.yan.fastview_library.viewgroup.FavoriateLayout;

/**
 * Created by ${洒笑天涯} on 2016/11/13.
 */

public class HeartFormFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_favoriate_layout, null);
        ListView mListView = (ListView) view.findViewById(R.id.mlistView);
        FavoriateLayout plFavoriate = (FavoriateLayout) view.findViewById(R.id.plFavoriate);
        mListView.setAdapter(new HeartFormAdapter(getContext(), plFavoriate));
        return view;
    }

    private class HeartFormAdapter extends CommonAdapter<User> {
        private FavoriateLayout mFl;

        public HeartFormAdapter(Context context, FavoriateLayout plFavoriate) {
            super(context, R.layout.item_heartform, RandomUtil.createRandomUser(100));
            mFl = plFavoriate;
        }

        @Override
        public void convert(ViewHolder holder, int position, User user) {
            holder.displayRoundImage(user.getImageAvator(), R.id.img)
                    .setText(R.id.tv_text, user.getUserName())
                    .setOnClickListener(R.id.img_favoriate, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mFl.startAnimation(view);
                        }
                    });
        }
    }
}

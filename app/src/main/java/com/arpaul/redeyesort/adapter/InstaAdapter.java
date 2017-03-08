package com.arpaul.redeyesort.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arpaul.redeyesort.R;
import com.arpaul.redeyesort.common.AppConstant;
import com.arpaul.redeyesort.dataobject.ImageCellDO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ARPaul on 08-03-2017.
 */

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageCellDO> arrImageCellDO = new ArrayList<>();

    public InstaAdapter(Context context, ArrayList<ImageCellDO> arrTours) {
        this.context = context;
        this.arrImageCellDO = arrTours;
    }

    public void refresh(ArrayList<ImageCellDO> arrTours) {
        this.arrImageCellDO = arrTours;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_insta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageCellDO objImageCellDO = arrImageCellDO.get(position);

        if(!TextUtils.isEmpty(objImageCellDO.imageURL)) {
            Picasso.with(context).load(objImageCellDO.imageURL).into(holder.ivActualImage);
        }

        if(objImageCellDO.cellBitmap != null) {
            holder.llModified.setVisibility(View.VISIBLE);
            holder.ivModifiedImage.setImageBitmap(objImageCellDO.cellBitmap);
        }
        else
            holder.llModified.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if(arrImageCellDO != null)
            return arrImageCellDO.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivActualImage;
        public final ImageView ivModifiedImage;
        public final LinearLayout llModified;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            llModified              = (LinearLayout) view.findViewById(R.id.llModified);

            ivActualImage           = (ImageView) view.findViewById(R.id.ivActualImage);
            ivModifiedImage         = (ImageView) view.findViewById(R.id.ivModifiedImage);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}

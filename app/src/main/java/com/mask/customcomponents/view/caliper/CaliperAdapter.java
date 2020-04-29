package com.mask.customcomponents.view.caliper;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mask.customcomponents.utils.SizeUtils;

import java.util.List;

/**
 * 卡尺 Adapter
 * Created by lishilin on 2020/04/28
 */
public class CaliperAdapter extends RecyclerView.Adapter<CaliperAdapter.ViewHolder> {

    private Context context;
    private List<String> valueList;

    public CaliperAdapter(Context context, List<String> valueList) {
        this.context = context;
        this.valueList = valueList;
    }

    @Override
    public int getItemCount() {
        return valueList == null ? 0 : valueList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv_value = new TextView(context);
        tv_value.setLayoutParams(new ViewGroup.LayoutParams(70, ViewGroup.LayoutParams.MATCH_PARENT));
        tv_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.dpToPx(9.0f));
        tv_value.setTextColor(Color.WHITE);
        tv_value.setGravity(Gravity.CENTER);
        return new ViewHolder(tv_value);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String value = valueList.get(position);

        holder.tv_value.setText(value);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_value;

        ViewHolder(@NonNull TextView itemView) {
            super(itemView);

            tv_value = itemView;
        }
    }

}

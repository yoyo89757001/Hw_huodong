package megvii.testfacepass.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.view.GlideRoundTransform;


/**
 * Created by xingchaolei on 2017/12/5.
 */

public class UserListAdapter extends BaseAdapter {

    private List<Subject> mGroupNames;
    private LayoutInflater mLayoutInflater;
    private ItemDeleteButtonClickListener mItemDeleteButtonClickListener;
    private FacePassHandler facePassHandler;
    private Context context;
    private RequestOptions myOptions2 =null;


    public UserListAdapter(List<Subject> data, Context context, FacePassHandler facePassHandler) {
        mGroupNames=data;
        this.context=context;
        this.facePassHandler=facePassHandler;
        myOptions2 = new RequestOptions()
                .fitCenter()
                .error(R.drawable.erroy_bg)
                //   .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
                .transform(new GlideRoundTransform(context, 20));
    }

    public List<Subject> getData() {
        return mGroupNames;
    }

    public void setData(List<Subject> data) {
        mGroupNames = data;
    }

    public void setOnItemDeleteButtonClickListener(ItemDeleteButtonClickListener listener) {
        mItemDeleteButtonClickListener = listener;
    }

    @Override
    public int getCount() {
        return mGroupNames == null ? 0 : mGroupNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroupNames == null ? null : mGroupNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_item_group_nameuser, parent, false);
            holder = new ViewHolder();
            holder.groupNameTv =  convertView.findViewById(R.id.tv_group_name);
            holder.deleteGroupIv =  convertView.findViewById(R.id.iv_delete_group);
            holder.touxiang =  convertView.findViewById(R.id.touxiang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.deleteGroupIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemDeleteButtonClickListener != null) {
                    mItemDeleteButtonClickListener.OnItemDeleteButtonClickListener(position);
                }
            }
        });
        holder.groupNameTv.setText(mGroupNames.get(position).getName());
        Bitmap bitmap = null;
        try {
            if (mGroupNames.get(position).getTeZhengMa()!=null){
                bitmap = facePassHandler.getFaceImage(mGroupNames.get(position).getTeZhengMa().getBytes());
                Glide.with(context)
                        .load(new BitmapDrawable(context.getResources(), bitmap))
                        .apply(myOptions2)
                        .into(holder.touxiang);
            }

        } catch (FacePassException e) {
            e.printStackTrace();
        }

        return convertView;
    }


    public static class ViewHolder {
        TextView groupNameTv;
        ImageView deleteGroupIv,touxiang;
    }


    public interface ItemDeleteButtonClickListener {

        void OnItemDeleteButtonClickListener(int position);

    }
}

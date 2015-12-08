package com.horcu.apps.jrbonillacoupon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hacz on 10/14/2015.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.BindingHolder> {
    private List<User> mUsers;
    private Context ctx;

    public static class BindingHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView selected;
        public LetterImageView userImage;

        public BindingHolder(View v) {
            super(v);
        }
    }

    public UserAdapter(List<User> recyclerUsers, Context context) {
        this.mUsers = recyclerUsers;
        this.ctx = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        final BindingHolder holder = new BindingHolder(v);

        holder.userName = (TextView)v.findViewById(R.id.friend);
        holder.selected = (ImageView)v.findViewById(R.id.selected);
        holder.userImage = (LetterImageView)v.findViewById(R.id.user_img);


        return holder;
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        final User user = mUsers.get(position);
        holder.userName.setText(user.getUserName());

            //holder.userImage.setS(true);
            holder.userImage.setLetter(user.getUserName().charAt(0));
            holder.userImage.setOval(true);

        Picasso.with(ctx).load(user.getImageUrl()).into(holder.userImage);

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView friend = (TextView)v.findViewById(R.id.friend);
                String friendsname = friend.getText().toString();
                User client = mUsers.get(position);

                if(v.isSelected())
                {
                    v.setSelected(false);
                    Drawable drawable = ctx.getDrawable(R.drawable.ic_content_add);
                    holder.selected.setImageDrawable(drawable);

                    if(((ClientsActivity) ctx).selectedFriends.contains(client))
                        ((ClientsActivity) ctx).selectedFriends.remove(client);
                }
                else
                {
                    v.setSelected(true);
                    Drawable drawable = ctx.getDrawable(R.drawable.ic_action_done);
                    holder.selected.setImageDrawable(drawable);

                    if(!((ClientsActivity) ctx).selectedFriends.contains(client))
                        ((ClientsActivity) ctx).selectedFriends.add(client);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
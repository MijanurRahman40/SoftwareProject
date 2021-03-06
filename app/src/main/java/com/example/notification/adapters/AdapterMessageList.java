package com.example.notification.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.models.ModelMessage;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMessageList extends RecyclerView.Adapter<AdapterMessageList.MessageViewHolder> {
    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private Context context;

    private FirebaseUser firebaseUser;
    private String hisImage;


    private List<ModelMessage> messageEntities;

    public AdapterMessageList(List<ModelMessage> messageEntities, Context context, String hisImage) {
        this.messageEntities = messageEntities;
        this.context = context;
        this.hisImage = hisImage;

    }


    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (messageEntities.get(position).getSender().equals(firebaseUser.getUid()))
            return SENT;
        return RECEIVED;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = (i == SENT) ? R.layout.sent_message_holder : R.layout.received_message_holder;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);


        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, final int i) {

        String message = messageEntities.get(i).getMessage();

        messageViewHolder.text.setText(message);
        if (getItemViewType(i)==RECEIVED){
            try {
                Picasso.get().load(hisImage).into(messageViewHolder.avatar);
                Log.i("GOT", hisImage);
            } catch (Exception e){
                Picasso.get().load(R.drawable.avatar).into(messageViewHolder.avatar);
            }
        }

        if (messageEntities.get(i).isSeen()){
            messageViewHolder.date.setText("Seen");
        } else {
            messageViewHolder.date.setText("Delivered");
        }

        if (i == messageEntities.size()-1 && getItemViewType(i)==SENT){
            messageViewHolder.date.setVisibility(View.VISIBLE);

        } else {
            messageViewHolder.date.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return messageEntities.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView date;
        ImageView avatar;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
            date = itemView.findViewById(R.id.date);
            avatar = itemView.findViewById(R.id.r_avatar);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Snackbar snackbar = Snackbar.make(v,"Delete message?", Snackbar.LENGTH_LONG)
                            .setAction("DELETE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                    return true;
                }
            });
            ;
        }


    }

}

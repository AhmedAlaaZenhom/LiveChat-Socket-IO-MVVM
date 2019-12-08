package com.intcore.internship.livechat.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intcore.internship.livechat.R;
import com.intcore.internship.livechat.data.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewManager extends RecyclerView.Adapter {

    private final static int VIEW_ITEM_SELF = 0;
    private final static int VIEW_ITEM_BUDDY = 1;

    private ArrayList<ChatMessage> list;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private boolean isCurrentLocaleAR;
    private Listener listener;

    ChatViewManager(RecyclerView recyclerView, Context context, boolean isCurrentLocaleAR, Listener listener) {
        this.list = new ArrayList<>();
        this.isCurrentLocaleAR = isCurrentLocaleAR;
        this.listener = listener;
        this.recyclerView = recyclerView;
        layoutManager = new LinearLayoutManager(context);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (listener == null)
                    return;
                if (isScrolledToBottom())
                    listener.onScrolledToBottom();
                else
                    listener.onScrolledAwayFromBottom();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isByMe())
            return VIEW_ITEM_SELF;
        else
            return VIEW_ITEM_BUDDY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM_SELF) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_chat_message_self, parent, false);
            return new SelfViewHolder(view);
        } else {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_chat_message_buddy, parent, false);
            return new BuddyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage data = list.get(position);
        if (data.isByMe())
            ((SelfViewHolder) holder).bindData(data);
        else
            ((BuddyViewHolder) holder).bindData(data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private class SelfViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextTV;
        private TextView messageTimeTV;

        private SelfViewHolder(View v) {
            super(v);
            messageTextTV = v.findViewById(R.id.messageTextTV);
            messageTimeTV = v.findViewById(R.id.messageTimeTV);
        }

        private void bindData(ChatMessage data) {
            messageTextTV.setBackgroundResource(data.isSent() ? R.drawable.colored_cornered_tv : R.drawable.dark_colored_cornered_tv);
            messageTextTV.setText(data.getMessageText());
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", isCurrentLocaleAR ? new Locale("ar") : Locale.US);
            messageTimeTV.setText(format.format(new Date(data.getTimeStamp())));
        }
    }

    private class BuddyViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTV;
        private TextView messageTextTV;
        private TextView messageTimeTV;

        private BuddyViewHolder(View v) {
            super(v);
            userNameTV = v.findViewById(R.id.userNameTV);
            messageTextTV = v.findViewById(R.id.messageTextTV);
            messageTimeTV = v.findViewById(R.id.messageTimeTV);
        }

        private void bindData(ChatMessage data) {
            userNameTV.setText(data.getUserNickName());
            boolean sameUserAsAbove = getAdapterPosition()>0
                    && list.get(getAdapterPosition() - 1).getUserNickName().equals(data.getUserNickName());
            userNameTV.setVisibility(sameUserAsAbove ? View.GONE : View.VISIBLE);
            messageTextTV.setText(data.getMessageText());
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", isCurrentLocaleAR ? new Locale("ar") : Locale.US);
            messageTimeTV.setText(format.format(new Date(data.getTimeStamp())));
        }
    }

    boolean isEmpty(){
        return list.isEmpty() ;
    }

    long getFirstMessageTimeStamp(){
        if(list.isEmpty())
            return -1L;
        return list.get(0).getTimeStamp();
    }

    void clearMessages(){
        list.clear();
        notifyDataSetChanged();
    }

    void insertMessagesToTop(List<ChatMessage> chatMessages){
        boolean scrollToBottomAfterInsert = list.isEmpty() ;
        list.addAll(0,chatMessages);
        notifyDataSetChanged();
        if(scrollToBottomAfterInsert)
            scrollToBottom();
        else
            scrollToPosition(chatMessages.size());

    }

    void insertMessagesToBottom(boolean scrollToBottomAfterInsert, ChatMessage newChatMessage) {
        boolean currentScrolledToBottom = isScrolledToBottom();
        int position = findIfExisting(newChatMessage);
        if (position != -1) {
            ChatMessage chatMessage = list.get(position);
            chatMessage.setSent(newChatMessage.isSent());
            chatMessage.setRemoved(newChatMessage.isRemoved());
        } else
            list.add(newChatMessage);
        notifyDataSetChanged();
        if (scrollToBottomAfterInsert || currentScrolledToBottom)
            scrollToBottom();
        else if (listener != null)
            listener.onRecyclerViewNeedsToBeScrolledManually();
    }

    int findIfExisting(ChatMessage newChatMessage) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            final ChatMessage chatMessage = list.get(i);
            if (newChatMessage.getId() == chatMessage.getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    void scrollToBottom(){
        if(!list.isEmpty())
            recyclerView.smoothScrollToPosition(list.size()-1);
    }

    private void scrollToPosition(int position){
        if(!list.isEmpty())
            recyclerView.smoothScrollToPosition(position);
    }

    private boolean isScrolledToBottom() {
        return layoutManager.findLastVisibleItemPosition() == list.size() - 1;
    }

    interface Listener {
        void onRecyclerViewNeedsToBeScrolledManually();

        void onScrolledToBottom();

        void onScrolledAwayFromBottom();
    }

}
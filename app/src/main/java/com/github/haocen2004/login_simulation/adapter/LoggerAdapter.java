package com.github.haocen2004.login_simulation.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.haocen2004.login_simulation.R;
import com.github.haocen2004.login_simulation.data.LogData;
import com.github.haocen2004.login_simulation.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class LoggerAdapter extends RecyclerView.Adapter<LoggerAdapter.LoggerViewHolder> {
    public void setAllLogs(List<LogData> allLogs) {
        this.allLogs = allLogs;
    }

    private List<LogData> allLogs = new ArrayList<>();
    private final Activity activity;

    public LoggerAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public LoggerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.log_view, parent, false);
        return new LoggerViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final LoggerViewHolder holder, final int position) {
        LogData LoggerData = allLogs.get(position);
        holder.textViewNumber.setText(String.valueOf(position));
        holder.textViewMessage.setText(LoggerData.getMessage());
        holder.textViewLevel.setText(LoggerData.getLevel() + " - " + LoggerData.getTAG());
//        holder.itemView.lon
        holder.itemView.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (LoggerData.getLevel().equals("长按")) {
                ClipData clip = ClipData.newPlainText("ScannerLog", allLogs.toString());
                clipboard.setPrimaryClip(clip);
                Logger.getLogger(null).makeToast("已复制全部日志到剪贴板");
            } else {
                ClipData clip = ClipData.newPlainText("ScannerLog", LoggerData.toString());
                clipboard.setPrimaryClip(clip);
                Logger.getLogger(null).makeToast("已复制到剪贴板");
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return allLogs.size();
    }

    static class LoggerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewMessage, textViewLevel;

        LoggerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewLevel = itemView.findViewById(R.id.textViewLevel);

        }
    }
}
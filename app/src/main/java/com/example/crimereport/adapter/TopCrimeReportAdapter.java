package com.example.crimereport.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crimereport.R;
import com.example.crimereport.models.Report;

import java.util.List;
import java.util.zip.Inflater;

public class TopCrimeReportAdapter extends RecyclerView.Adapter<TopCrimeReportAdapter.ViewHolder> {

    List<Report> reports;

    public TopCrimeReportAdapter(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder view =  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report,parent,false));

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.descriptionTextView.setText(String.valueOf(report.getReportCount()));
        holder.titleTexView.setText(report.getLocation());
        Log.d("test",String.valueOf(reports.size()));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView descriptionTextView;
        public TextView titleTexView;
        public ImageView logoImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descriptionTextView = itemView.findViewById(R.id.description);
            titleTexView = itemView.findViewById(R.id.title);
            logoImageView = itemView.findViewById(R.id.report_image);
        }
    }
}

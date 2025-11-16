package com.example.libraryapp_madca2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp_madca2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> bookId, bookTitle, bookAuthor, bookCategory, bookStartDate, bookStatus;

    public RVAdapter(Context context,
                     ArrayList<String> bookId,
                     ArrayList<String> bookTitle,
                     ArrayList<String> bookAuthor,
                     ArrayList<String> bookCategory,
                     ArrayList<String> bookStartDate,
                     ArrayList<String> bookStatus) {
        this.context = context;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
        this.bookStartDate = bookStartDate;
        this.bookStatus = bookStatus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvBookId.setText(String.valueOf(bookId.get(position)));
        holder.tvBookTitle.setText(String.valueOf(bookTitle.get(position)));
        holder.tvAuthor.setText(String.valueOf(bookAuthor.get(position)));
        holder.tvCategory.setText(String.valueOf(bookCategory.get(position)));
        holder.tvStartDate.setText(String.valueOf(bookStartDate.get(position)));
        holder.tvStatus.setText(String.valueOf(bookStatus.get(position)));

        if (holder.tvStatus.getText().toString().equals("Finished")) {
            holder.tvStatus.setTextColor(Color.GREEN);
        }
        else {
            holder.tvStatus.setTextColor(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return bookId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookId, tvBookTitle, tvAuthor, tvCategory, tvStartDate, tvStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookId = itemView.findViewById(R.id.rv_book_id);
            tvBookTitle = itemView.findViewById(R.id.rv_booktitle);
            tvAuthor = itemView.findViewById(R.id.rv_bookauthor);
            tvCategory = itemView.findViewById(R.id.rv_category);
            tvStartDate = itemView.findViewById(R.id.rv_startdate);
            tvStatus = itemView.findViewById(R.id.rv_status);
        }
    }
}



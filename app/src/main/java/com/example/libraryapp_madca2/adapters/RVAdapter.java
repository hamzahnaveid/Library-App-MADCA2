package com.example.libraryapp_madca2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp_madca2.R;
import com.example.libraryapp_madca2.UpdateBookActivity;
import com.example.libraryapp_madca2.classes.Book;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> books;

    public RVAdapter(Context context,
                     ArrayList<Book> books) {
        this.context = context;
        this.books = books;
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
        holder.tvBookId.setText(String.valueOf(books.get(position).getId()));
        holder.tvBookTitle.setText(String.valueOf(books.get(position).getTitle()));
        holder.tvAuthor.setText(String.valueOf(books.get(position).getAuthor()));
        holder.tvCategory.setText(String.valueOf(books.get(position).getCategory()));
        holder.tvStartDate.setText(String.valueOf(books.get(position).getStartDate()));
        holder.tvStatus.setText(String.valueOf(books.get(position).getStatus()));

        if (holder.tvStatus.getText().toString().equals("Finished")) {
            holder.tvStatus.setTextColor(Color.GREEN);
        }
        else {
            holder.tvStatus.setTextColor(Color.YELLOW);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBookActivity.class);
                intent.putExtra("BOOK_ID", String.valueOf(books.get(holder.getBindingAdapterPosition()).getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookId, tvBookTitle, tvAuthor, tvCategory, tvStartDate, tvStatus;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookId = itemView.findViewById(R.id.rv_book_id);
            tvBookTitle = itemView.findViewById(R.id.rv_booktitle);
            tvAuthor = itemView.findViewById(R.id.rv_bookauthor);
            tvCategory = itemView.findViewById(R.id.rv_category);
            tvStartDate = itemView.findViewById(R.id.rv_startdate);
            tvStatus = itemView.findViewById(R.id.rv_status);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}



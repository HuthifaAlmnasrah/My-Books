package com.example.mybooks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import static com.example.mybooks.ShowBookActivity.BOOK_ID_KEY;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Book> books;
    private Context context;
    private String parentActivity;

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public BookRecyclerViewAdapter(Context context, String parentActivity) {
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.bookName.setText(books.get(position).getName());
        Glide.with(context)
                .asBitmap()
                .load(books.get(position).getImageUrl())
                .into(holder.bookImage);
        holder.parnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowBookActivity.class);
                intent.putExtra(BOOK_ID_KEY, books.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.authorTxt.setText(books.get(position).getAuthor());
        holder.shortTxt.setText(books.get(position).getShortDesc());

        if(books.get(position).isExpanded()){
            TransitionManager.beginDelayedTransition(holder.parnt);
            holder.expandLayout.setVisibility(View.VISIBLE);
            holder.arrowDown.setVisibility(View.GONE);
            if(parentActivity.equals("allBooks")){
                holder.btnDelete.setVisibility(View.GONE);
            }
            else if(parentActivity.equals("alreadyRead")){
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = books.get(position).getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getString(R.string.areyousure)+"\n\""+name+"\"");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Task<Void> usersRef = database.getReference("Users").child(currentUser.getUid()).child("alreadyRead").child(books.get(position).getId()).removeValue();

                                if(Utils.getInstance().removeFromAlreadyRead(books.get(position))){
                                    Toast.makeText(context, name +context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Something Wrong Happened Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                    }
                });
            } else if(parentActivity.equals("wantToRead")){
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = books.get(position).getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getString(R.string.areyousure)+"\n\""+name+"\"");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Task<Void> usersRef = database.getReference("Users").child(currentUser.getUid()).child("wantToRead").child(books.get(position).getId()).removeValue();

                                if(Utils.getInstance().removeFromWantToRead(books.get(position))){
                                    Toast.makeText(context, name +context.getString(R.string.delete), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Something Wrong Happened Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                    }
                });
            }else if(parentActivity.equals("favorite")){
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = books.get(position).getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getString(R.string.areyousure)+"\n\""+name+"\"");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Task<Void> usersRef = database.getReference("Users").child(currentUser.getUid()).child("favorite").child(books.get(position).getId()).removeValue();

                                if(Utils.getInstance().removeFromFavorite(books.get(position))){

                                    Toast.makeText(context, name +context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Something Wrong Happened Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                    }
                });
            }else if(parentActivity.equals("currentlyReading")){
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = books.get(position).getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(context.getString(R.string.areyousure)+"\n\""+name+"\"");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Task<Void> usersRef = database.getReference("Users").child(currentUser.getUid()).child("currentlyRead").child(books.get(position).getId()).removeValue();

                                if(Utils.getInstance().removeFromCurrentlyReading(books.get(position))){
                                    Toast.makeText(context, name +context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Something Wrong Happened Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                    }
                });
            }
        }else{
            TransitionManager.beginDelayedTransition(holder.parnt);
            holder.expandLayout.setVisibility(View.GONE);
            holder.arrowDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView parnt;
        private ImageView bookImage;
        private TextView bookName;
        private ImageView arrowUp, arrowDown;
        private RelativeLayout expandLayout;
        private TextView authorTxt, shortTxt;
        private ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parnt = itemView.findViewById(R.id.parent);
            bookImage = itemView.findViewById(R.id.imgBook);
            bookName = itemView.findViewById(R.id.bookName);
            arrowDown = itemView.findViewById(R.id.btnArrowDown);
            arrowUp = itemView.findViewById(R.id.btnUpArrow);
            expandLayout = itemView.findViewById(R.id.expandedLayout);
            authorTxt = itemView.findViewById(R.id.txtAuthor);
            shortTxt = itemView.findViewById(R.id.txtShortDesc);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            arrowDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book = books.get(getAdapterPosition());
                    book.setExpanded(!book.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            arrowUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book = books.get(getAdapterPosition());
                    book.setExpanded(!book.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}


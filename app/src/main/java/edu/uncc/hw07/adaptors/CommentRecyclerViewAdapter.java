package edu.uncc.hw07.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import edu.uncc.hw07.MyAlertDialog;
import edu.uncc.hw07.databinding.CommentRowItemBinding;
import edu.uncc.hw07.databinding.ForumRowItemBinding;
import edu.uncc.hw07.models.Forum;
/*
 * In Class 11
 * RecyclerViewAdapter.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    ArrayList<Forum> forums = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;

    public CommentRecyclerViewAdapter(Context context, ArrayList<Forum> forums) {
        this.forums = forums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentRowItemBinding binding = CommentRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forum forum = forums.get(position);
        holder.setupUI(forum);
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CommentRowItemBinding mBinding;

        public ViewHolder(CommentRowItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(Forum forum) {


            mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .collection("forums").document(forum.forumId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                MyAlertDialog.show(context, "Error", e.getMessage());
                                }
                            });
                }
            });
        }
    }
}

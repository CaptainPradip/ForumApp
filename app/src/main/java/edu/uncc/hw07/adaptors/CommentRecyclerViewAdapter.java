package edu.uncc.hw07.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw07.ForumFragment;
import edu.uncc.hw07.ForumsFragment;
import edu.uncc.hw07.MyAlertDialog;
import edu.uncc.hw07.R;
import edu.uncc.hw07.databinding.CommentRowItemBinding;
import edu.uncc.hw07.databinding.ForumRowItemBinding;
import edu.uncc.hw07.models.Comment;
import edu.uncc.hw07.models.Forum;
/*
 * In Class 11
 * RecyclerViewAdapter.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    ArrayList<Comment> comments = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;
    ForumFragment.ForumListener mListener;
    Forum forum;

    public CommentRecyclerViewAdapter(Context context, ArrayList<Comment> comments, ForumFragment.ForumListener mListener, Forum forum) {
        this.comments = comments;
        this.context = context;
        this.mListener = mListener;
        this.forum = forum;
    }

    public HashMap<String, Object> createMap(Comment comment) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", comment.getComment());
        map.put("commentId", comment.getCommentId());
        map.put("commentCreator", comment.getCommentCreator());
        map.put("dateTime", comment.getDateTime());
        map.put("creatorId", comment.getCreatorId());
        return map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentRowItemBinding binding = CommentRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.setupUI(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CommentRowItemBinding mBinding;

        public ViewHolder(CommentRowItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void setupUI(Comment comment) {
            mBinding.textViewCommentText.setText(comment.getComment());
            mBinding.textViewCommentCreatedBy.setText(comment.getCommentCreator());
            mBinding.textViewCommentCreatedAt.setText(comment.getDateTime());
            if (comment.creatorId == null || !comment.getCreatorId().equals(mAuth.getCurrentUser().getUid())) {
                mBinding.imageViewDelete.setVisibility(View.INVISIBLE);
            } else {
                mBinding.imageViewDelete.setVisibility(View.VISIBLE);
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("forum").document(forum.getForumId())
                                .collection("comment").document(comment.getCommentId())
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
}

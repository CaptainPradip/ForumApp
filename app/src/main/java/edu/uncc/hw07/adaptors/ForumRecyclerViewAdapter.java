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

import edu.uncc.hw07.ForumsFragment;
import edu.uncc.hw07.MyAlertDialog;
import edu.uncc.hw07.R;
import edu.uncc.hw07.databinding.ForumRowItemBinding;
import edu.uncc.hw07.models.Forum;
/*
 * In Class 11
 * RecyclerViewAdapter.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ViewHolder> {

    ArrayList<Forum> forums = new ArrayList<>();
    ArrayList<String> likes = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;
    ForumsFragment.ForumsListener mListener;
     

    public ForumRecyclerViewAdapter(Context context, ArrayList<Forum> forums, ForumsFragment.ForumsListener listener) {
        this.forums = forums;
        this.context = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ForumRowItemBinding binding = ForumRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, mListener);
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

    public HashMap<String, Object> createMap(Forum forum) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", forum.getTitle());
        map.put("forumId", forum.getForumId());
        map.put("description", forum.getDescription());
        map.put("forumCreator", forum.getForumCreator());
        map.put("dateTime", forum.getDateTime());
        map.put("likes", forum.getLikes());
        return map;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ForumRowItemBinding mBinding;


        public ViewHolder(ForumRowItemBinding binding, ForumsFragment.ForumsListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        void setupUI(Forum forum) {

            mBinding.textViewForumTitle.setText(forum.getTitle());
            mBinding.textViewForumCreatedBy.setText(forum.getForumCreator());
            mBinding.textViewForumText.setText(forum.getDescription());
            mBinding.textViewForumLikesDate.setText(forum.getLikes().size() + " Likes | " + forum.getDateTime());

            likes = forum.getLikes();
            if (likes.contains(mAuth.getCurrentUser().getUid())) {
                mBinding.imageViewLike.setImageResource(R.drawable.like_favorite);
            } else {
                mBinding.imageViewLike.setImageResource(R.drawable.like_not_favorite);
            }


            mBinding.imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, Object> map = new HashMap<>();
                    likes = forum.getLikes();

                    //Check if current user has liked the forum
                    if (likes.contains(mAuth.getCurrentUser().getUid())) {
                        likes.remove(mAuth.getCurrentUser().getUid());
                        forum.setLikes(likes);
                        map = createMap(forum);
                        db.collection("forum").document(forum.forumId)
                                .set(map)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        likes.add(mAuth.getCurrentUser().getUid());
                        forum.setLikes(likes);
                        map = createMap(forum);
                        db.collection("forum").document(forum.getForumId())
                                .set(map)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

            mBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.gotoForumFragment(forum);
                }
            });


            if (mAuth.getCurrentUser().getUid().equals(forum.getForumCreatorId())) {
                //mBinding.imageViewDelete.setVisibility(View.VISIBLE);
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("forum").document(forum.getForumId())
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
            } else {
                mBinding.imageViewDelete.setVisibility(View.INVISIBLE);
            }
        }
    }

}

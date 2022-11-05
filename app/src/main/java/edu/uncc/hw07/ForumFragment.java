package edu.uncc.hw07;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import edu.uncc.hw07.adaptors.CommentRecyclerViewAdapter;
import edu.uncc.hw07.databinding.FragmentForumBinding;
import edu.uncc.hw07.models.Comment;
import edu.uncc.hw07.models.Forum;

/*
 * Homework 07
 * ForumFragment.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class ForumFragment extends Fragment {
    private static final String ARG_PARAM = "ARG_PARAM";

    private Forum mForum;
    FragmentForumBinding binding;
    CommentRecyclerViewAdapter adapter;
    ArrayList<Comment> mComments = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Forum) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forums");

        binding.recyclerView.setHasFixedSize(true);
        adapter = new CommentRecyclerViewAdapter(getContext(), mComments, mForum);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.textViewForumTitle.setText(mForum.title);
        binding.textViewForumText.setText(mForum.description);
        binding.textViewForumCreatedBy.setText(mForum.forumCreator);
        binding.textViewForumCreatedBy.setText(mForum.forumCreator);

        db.collection("forum").document(mForum.forumId).collection("comment")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mComments.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Comment comment = new Comment();
                            comment.setComment(doc.getString("comment"));
                            comment.setCommentCreator(doc.getString("commentCreator"));
                            comment.setDateTime(doc.getString("dateTime"));
                            comment.setCreatorId(doc.getString("creatorId"));
                            comment.setCommentId(doc.getString("commentId"));
                            mComments.add(comment);
                        }
                        binding.textViewCommentsCount.setText(mComments.size() + " Comments");
                        adapter.notifyDataSetChanged();
                    }
                });

        binding.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String comment = binding.editTextComment.getText().toString();

                if (comment.isEmpty()) {
                    MyAlertDialog.show(getContext(), "Error", "Please enter all the fields");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    String commentId = UUID.randomUUID().toString();
                    map.put("commentId", commentId);
                    map.put("comment", comment);
                    map.put("creatorId", mAuth.getCurrentUser().getUid());
                    map.put("commentCreator", mAuth.getCurrentUser().getDisplayName());
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                    String dateTime = localDateTime.format(formatter);
                    map.put("dateTime", dateTime);
                    db.collection("forum").document(mForum.forumId)
                            .collection("comment").document(commentId)
                            .set(map)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        binding.editTextComment.setText("");
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //Find the currently focused view, so we can grab the correct window token from it.
                                        View view = getActivity().getCurrentFocus();
                                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                                        if (view == null) {
                                            view = new View(getActivity());
                                        }
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    } else {
                                        MyAlertDialog.show(getContext(), "Error", task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }

}
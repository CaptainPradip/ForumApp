package edu.uncc.hw07;

import android.content.Context;
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
import edu.uncc.hw07.adaptors.ForumRecyclerViewAdapter;
import edu.uncc.hw07.databinding.FragmentForumBinding;
import edu.uncc.hw07.databinding.FragmentForumsBinding;
import edu.uncc.hw07.models.Comment;
import edu.uncc.hw07.models.Forum;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "ARG_PARAM";

    ArrayList<Comment> mComments = new ArrayList<>();
    ForumListener mListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CommentRecyclerViewAdapter adapter;
    FragmentForumBinding binding;
    // TODO: Rename and change types of parameters
    private Forum mForum;

    public ForumFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forums");

        binding.recyclerView.setHasFixedSize(true);
        adapter = new CommentRecyclerViewAdapter(getContext(), mComments, mListener, mForum);
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

                                        //mListener.closeCreateForumFragment();
                                    } else {
                                        MyAlertDialog.show(getContext(), "Error", task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumListener) context;
    }

    public interface ForumListener {
        void gotoForumsFragment(Forum forum);
    }
}
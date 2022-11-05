package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.hw07.adaptors.ForumRecyclerViewAdapter;
import edu.uncc.hw07.databinding.FragmentForumsBinding;
import edu.uncc.hw07.models.Forum;

public class ForumsFragment extends Fragment {

    ArrayList<Forum> mForums = new ArrayList<>();
    ForumsListener mListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ForumRecyclerViewAdapter adapter;
    FragmentForumsBinding binding;

    public ForumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForumsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forums");

        binding.recyclerView.setHasFixedSize(true);
        adapter = new ForumRecyclerViewAdapter(getContext(), mForums, mListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);


        db.collection("forum")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mForums.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Forum forum = new Forum();
                            forum.setForumId(doc.getString("forumId"));
                            forum.setForumCreator(doc.getString("forumCreator"));
                            forum.setForumCreatorId(doc.getString("forumCreatorId"));
                            forum.setDateTime(doc.getString("dateTime"));
                            forum.setDescription(doc.getString("description"));
                            forum.setLikes((ArrayList<String>) doc.get("likes"));
                            forum.setTitle(doc.getString("title"));
                            mForums.add(forum);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.logout();
            }
        });

        binding.buttonCreateForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.createForum();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumsListener) context;
    }

    public interface ForumsListener {
        void logout();

        void gotoForumFragment(Forum forum);

        void createForum();
    }
}
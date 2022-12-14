package edu.uncc.hw07;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import edu.uncc.hw07.databinding.FragmentCreateForumBinding;

/*
 * Homework 07
 * CreateForumFragment.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class CreateForumFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CreateForumListener mListener;
    FragmentCreateForumBinding binding;

    public CreateForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Forum");

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.closeCreateForumFragment();
            }
        });

        binding.buttonCreateForum.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String forumTitle = binding.editTextTitle.getText().toString();
                String forumDesc = binding.editTextDesc.getText().toString();

                if (forumTitle.isEmpty() || forumDesc.isEmpty()) {
                    MyAlertDialog.show(getContext(), "Error", "Please enter all the fields");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    String forumId = UUID.randomUUID().toString();
                    map.put("title", forumTitle);
                    map.put("forumId", forumId);
                    map.put("description", forumDesc);
                    map.put("forumCreator", mAuth.getCurrentUser().getDisplayName());
                    map.put("forumCreatorId", mAuth.getCurrentUser().getUid());
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                    String dateTime = localDateTime.format(formatter);
                    map.put("dateTime", dateTime);
                    map.put("likes", new ArrayList<String>());
                    db.collection("forum").document(forumId)
                            .set(map)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mListener.closeCreateForumFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateForumListener) context;
    }

    interface CreateForumListener {
        void closeCreateForumFragment();
    }
}
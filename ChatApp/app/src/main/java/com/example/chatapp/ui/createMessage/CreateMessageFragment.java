package com.example.chatapp.ui.createMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class CreateMessageFragment extends Fragment {

    EditText messageName, message;
    Button createMessage;
    RecyclerView messages;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        messageName = view.findViewById(R.id.messageName);
        message = view.findViewById(R.id.message);
        messages = view.findViewById(R.id.messages);
        createMessage = view.findViewById(R.id.createMessage);
        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        createMessage.setOnClickListener(v -> {
            String messageNameText = messageName.getText().toString();
            String messageText = message.getText().toString();

            if(messageNameText.isEmpty() || messageText.isEmpty()){
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                return;
            }
            createMessage(messageNameText, messageText);


        });

        return view;
    }
    private void createMessage(String messageNameText, String messageText) {

        String userId = fbAuth.getCurrentUser().getUid();

        fbStore.collection("/users/" + userId + "/messages/").add(new HashMap<String, Object>(){{
            put("messageName", messageNameText);
            put("message", messageText);
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Mesaj başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Mesaj oluşturulamadı.", Toast.LENGTH_SHORT).show();
        });
    }
}
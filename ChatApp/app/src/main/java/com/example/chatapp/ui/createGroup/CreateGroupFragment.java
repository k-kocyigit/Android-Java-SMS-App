package com.example.chatapp.ui.createGroup;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {

    EditText groupName,groupExplain;
    Button createGroup;
    RecyclerView groups;
    ImageView groupImage;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;
    FirebaseStorage fbStorage;

    Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupName = view.findViewById(R.id.messageName);
        groupExplain = view.findViewById(R.id.message);
        createGroup = view.findViewById(R.id.createMessage);
        groups = view.findViewById(R.id.messages);
        groupImage = view.findViewById(R.id.groupImage);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        fbStorage = FirebaseStorage.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                filePath = result.getData().getData();
                groupImage.setImageURI(filePath);
            }
        });

        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });

        createGroup.setOnClickListener(v -> {
            String grupAdiText = groupName.getText().toString();
            String grupAciklamaText = groupExplain.getText().toString();
            if (grupAdiText.isEmpty()) {
                Toast.makeText(getContext(), "Grup adı boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }
            if (grupAciklamaText.isEmpty()) {
                Toast.makeText(getContext(), "Grup açıklaması boş bırakılamaz", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filePath != null) {
                StorageReference storageReference = fbStorage.getReference().child("resimler" + UUID.randomUUID().toString());
                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Toast.makeText(getContext(), "resim yüklendi", Toast.LENGTH_SHORT).show();
                        createGroup(grupAdiText, grupAciklamaText, downloadUrl);
                    });
                });
            }
            else{
                createGroup(grupAdiText,grupAciklamaText,null);
            }
        });

        return view;
    }
    private void createGroup(String groupName, String groupExplain, String downloadUrl) {
        String kullaniciId = fbAuth.getCurrentUser().getUid();
        fbStore.collection("/users/" + kullaniciId + "/gruplar/").add(new HashMap<String, Object>(){{
            put("grupAdi", groupName);
            put("grupAciklama", groupExplain);
            put("grupSimgesi", downloadUrl);
            put("numaralar", new ArrayList<String>());
        }
        }).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Grup oluşturuldu", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Grup oluşturulamadı", Toast.LENGTH_SHORT).show();
        });

    }
}
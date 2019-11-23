package com.example.myplanningpokeruser.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myplanningpokeruser.Model.Groups;
import com.example.myplanningpokeruser.Model.User;
import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.Utils.Constant;
import com.example.myplanningpokeruser.Utils.FragmentNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private View view;
    private EditText editText_name, editText_group_id;
    private Button button_login;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mRef2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);

        view = initializeViewElements(view);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.USER);
        mRef2 = mDatabase.getReference(Constant.GROUPS);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }

    private void login(){
        if (isTextLengthOk(editText_group_id.getText().toString()) && isTextLengthOk(editText_name.getText().toString())){

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot users : dataSnapshot.getChildren()){
                        if (users.child(Constant.NAME).getValue().toString().equals(editText_name.getText().toString())){
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        String KEY = mRef.push().getKey();
                        User user = new User(KEY, editText_name.getText().toString());
                        mRef.child(KEY).setValue(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            doesGroupExists();

        } else {
            Toast.makeText(getContext(), R.string.name_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private View initializeViewElements(View view){
        editText_group_id = view.findViewById(R.id.editText_login_group_id);
        editText_name = view.findViewById(R.id.editText_login_name);
        button_login = view.findViewById(R.id.button_admin_login);
        return view;
    }

    private void doesGroupExists(){
        mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean found;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                found = false;
                for (DataSnapshot groups : dataSnapshot.getChildren()){
                    if (groups.child(Constant.ID).getValue().toString().equals(editText_group_id.getText().toString())){
                        found = true;

                        String id = groups.child(Constant.ID).getValue().toString();
                        String key = groups.child(Constant.KEY).getValue().toString();
                        String name = groups.child(Constant.NAME).getValue().toString();
                        boolean active = Boolean.valueOf(groups.child(Constant.ACTIVE).getValue().toString());
                        int active_time = Integer.valueOf(groups.child(Constant.ACTIVE_TIME).getValue().toString());

                        Groups groups1 = new Groups(id, name, active, active_time, key);
                        Constant.ENTERED_GROUP = groups1;

                        Toast.makeText(getContext(),R.string.success, Toast.LENGTH_SHORT).show();
                        FragmentNavigation.getInstance(getContext()).replaceFragment(new HomePageFragment(), R.id.fragment_content);
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 4 ){
            return true;
        }
        else {
            return false;
        }
    }
}

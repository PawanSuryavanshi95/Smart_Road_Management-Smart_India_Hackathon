package com.example.smartroadmanagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentReport extends Fragment {

    private reportFragmentInterface listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container,false);

        Button upload = view.findViewById(R.id.btnUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadBtnSelected();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof reportFragmentInterface){
            listener = (reportFragmentInterface) context;
        }
        else{
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    public interface reportFragmentInterface{
        public void onUploadBtnSelected();
    }
}

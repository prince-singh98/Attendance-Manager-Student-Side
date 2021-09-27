package app.example.studentattendancemanager;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static app.example.studentattendancemanager.MainActivity.selectedClassIndex;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    List<JoinClassModel> joinClassModelList;

    public MainAdapter(List<JoinClassModel> joinClassModelList) {
        this.joinClassModelList = joinClassModelList;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_class_list_layout,parent,false);

        //for random colors
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        view.setBackgroundColor(color);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, int position) {

        String title = joinClassModelList.get(position).getClassName();

        viewHolder.setData(title,position,this);

    }

    @Override
    public int getItemCount() {
        return joinClassModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView classNameTv;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            classNameTv = itemView.findViewById(R.id.className_jc_list);


        }

        public void setData(String title, final int position, MainAdapter mainAdapter){
            classNameTv.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedClassIndex = position;

                    Intent intent = new Intent(itemView.getContext(), ClassOperationActivity.class);
                    intent.putExtra("selectedClass", selectedClassIndex);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}

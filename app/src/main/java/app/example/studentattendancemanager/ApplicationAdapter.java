package app.example.studentattendancemanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static app.example.studentattendancemanager.ApplicationActivity.selectedApplicationIndex;
import static app.example.studentattendancemanager.MainActivity.selectedClassIndex;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    List<ApplicationModel> applicationModelList;

    public ApplicationAdapter(ApplicationActivity applicationActivity, List<ApplicationModel> applicationModelList) {
        this.applicationModelList = applicationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_layout,parent,false);


        return new ApplicationAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String to = applicationModelList.get(position).getTo();
        String subject = applicationModelList.get(position).getSubject();

        viewHolder.setData(to,subject,position);


    }

    @Override
    public int getItemCount() {
        return applicationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView toEtAl, subjectEtAl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            toEtAl = itemView.findViewById(R.id.toTvAl);
            subjectEtAl = itemView.findViewById(R.id.subjectTvAl);
        }

        public void setData(String to, String subject, final int position) {
            toEtAl.setText(to);
            subjectEtAl.setText(subject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedApplicationIndex = position;

                            Intent intent = new Intent(itemView.getContext(), ViewApplicationActivity.class);
                            intent.putExtra("selectedClass", selectedApplicationIndex);
                            itemView.getContext().startActivity(intent);
                        }
                    });

                }
            });
        }
    }
}

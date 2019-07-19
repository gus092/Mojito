package com.example.mojito.Party;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.mojito.R;

import java.util.ArrayList;
//
//import static com.example.week1.ui.party.TabFragment1.REQ_CALL_party;
//import static com.example.week1.ui.party.TabFragment1.REQ_DELETE_party;
//import static com.example.week1.ui.party.TabFragment1.REQ_EDIT_party;


public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.partyviewholder> {
    private ArrayList<PartyItem> mDataset;

    // Set Listener


    public interface OnItemClickListener{
        void onItemClick(View v, int position, int request_code);
    }

    private static OnItemClickListener mListener = null ;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // View Holder
    public static class partyviewholder extends RecyclerView.ViewHolder{
        public TextView destination;
        public TextView attribute;
        public TextView user_id;
        public ImageView user_photo;
        public TextView description;
        public partyviewholder(final View itemView){
            super(itemView);

            destination = itemView.findViewById(R.id.destination);
            attribute= itemView.findViewById(R.id.attribute);
            user_id = itemView.findViewById(R.id.user_id);
            user_photo = itemView.findViewById(R.id.user_photo);
            description = itemView.findViewById(R.id.description);
//            final LinearLayout addView = (LinearLayout) itemView.findViewById(R.id.add_sub);

//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                Boolean onclick =false;
////                PartyItem_sub add_layout = new PartyItem_sub(itemView.getContext());
//
////                @Override
////                public void onClick(View view) {
////                    if (!onclick) {
////                        addView.addView(add_layout);
////                        onclick = true;
////
////                        // Call Button
////                        Button call_party = (Button) add_layout.findViewById(R.id.call_party);
////                        call_party.setOnClickListener(new Button.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                int pos = getAdapterPosition() ;
////                                if(mListener != null){
////                                    mListener.onItemClick(view, pos, REQ_CALL_party);
////                                }
////                            }
////                        });
////
////                        // Edit Button
////                        Button edit_party = (Button) add_layout.findViewById(R.id.edit_party);
////                        edit_party.setOnClickListener(new Button.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                int pos = getAdapterPosition() ;
////                                if(mListener != null){
////                                    mListener.onItemClick(view, pos, REQ_EDIT_party);
////                                }
////                            }
////                        });
////
////                        // Delete Button
////                        Button delete_party = (Button) add_layout.findViewById(R.id.delete_party);
////                        delete_party.setOnClickListener(new Button.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                int pos = getAdapterPosition() ;
////                                if(mListener != null){
////                                    mListener.onItemClick(view, pos, REQ_DELETE_party);
////                                }
////                            }
////                        });
////
////                    } else{
////                        addView.removeView(add_layout);
////                        onclick = false;
////                    }
////                }
//            });
        }
    }

    // Set Dataset
    public PartyAdapter(ArrayList<PartyItem> list){
        mDataset= list;
    }

    // Create View Holder
    @Override
    public PartyAdapter.partyviewholder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.party_item, parent, false);
        partyviewholder vh = new partyviewholder(view);
        return vh;
    }

    // Bind View Holder
    @Override
    public void onBindViewHolder(partyviewholder holder, int position){
        PartyItem PartyItem = mDataset.get(position);
        holder.destination.setText(PartyItem.getDestination());
        holder.attribute.setText(PartyItem.getAttribute());
        holder.user_photo.setImageBitmap(PartyItem.getUser_photo());
        holder.user_id.setText(PartyItem.getUser_Name());
        holder.description.setText(PartyItem.getDescription());
    }

    // Get Item Count
    @Override
    public int getItemCount(){
        return mDataset.size();
    }

    public void onActivityResult(int requestCode, int resultCode) {
        this.notifyDataSetChanged();
    }

}

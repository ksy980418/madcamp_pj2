package com.example.madcamp2;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Phonenumber> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    public RecyclerAdapter(Context context, ArrayList<Phonenumber> list) {
        this.context = context;
        this.listData = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public ItemViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phoneitem, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@Nullable ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position),position);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Phonenumber data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private Button call;
        private Button message;
        private Phonenumber data;
        private int position;
        private ImageView imageView1;
        private LinearLayout imageView2;
        private ContentResolver contentResolver;
        private Bitmap profile;

        GradientDrawable drawable;

        Intent intent;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.name);
            textView2 = itemView.findViewById(R.id.number);
            textView3 = itemView.findViewById(R.id.name2);
            textView4 = itemView.findViewById(R.id.number2);
            call = itemView.findViewById(R.id.call);
            message= itemView.findViewById(R.id.message);
            imageView1=itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            contentResolver = context.getContentResolver();

            drawable=(GradientDrawable) context.getDrawable(R.drawable.background_rounding);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        void onBind(Phonenumber data, int position) {
            this.data = data;
            this.position = position;
            textView1.setText(data.getName());
            textView2.setText(data.getNumber());
            textView3.setText(data.getName());
            textView4.setText(data.getNumber());
            profile=Bitmap.createBitmap(70, 70, Bitmap.Config.ARGB_8888);
            if(data.getPhoto_id().equals('n')) {
                profile = loadContactPhoto(contentResolver, Long.parseLong(data.getPerson_id()), Long.parseLong(data.getPhoto_id()));
                Bitmap resizedprofile;
                resizedprofile = Bitmap.createScaledBitmap(profile, profile.getWidth() * 2 / 3, profile.getHeight() * 2 / 3, true);
                imageView1.setBackground(drawable);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView1.setClipToOutline(true);
                }
                imageView1.setImageBitmap(resizedprofile);
            }
            changeVisibility(selectedItems.get(position));

            itemView.setOnClickListener(this);
            textView2.setOnClickListener(this);
            call.setOnClickListener(this);
            message.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linearItem:
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                    break;
                case R.id.call:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getNumber()));
                    v.getContext().startActivity(intent);
                    break;
                case R.id.message:
                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + data.getNumber()));
                    v.getContext().startActivity(intent);
                    break;

            }
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    imageView2.getLayoutParams().height = value;
                    imageView2.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }

        public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            if (input != null)
                return resizingBitmap(BitmapFactory.decodeStream(input));
            else
                Log.d("PHOTO","first try failed to load photo");

            byte[] photoBytes = null;
            Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
            Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
            try {
                if (c.moveToFirst())
                    photoBytes = c.getBlob(0);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                c.close();
            }

            if (photoBytes != null)
                return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));

            else
                Log.d("PHOTO", "second try also failed");
            return null;
        }

        public Bitmap resizingBitmap(Bitmap oBitmap) {
            if (oBitmap == null)
                return null;
            float width = oBitmap.getWidth();
            float height = oBitmap.getHeight();
            float resizing_size = 120;
            Bitmap rBitmap = null;
            if (width > resizing_size) {
                float mWidth = (float) (width / 100);
                float fScale = (float) (resizing_size / mWidth);
                width *= (fScale / 100);
                height *= (fScale / 100);

            } else if (height > resizing_size) {
                float mHeight = (float) (height / 100);
                float fScale = (float) (resizing_size / mHeight);
                width *= (fScale / 100);
                height *= (fScale / 100);
            }
            rBitmap = Bitmap.createScaledBitmap(oBitmap, (int) width, (int) height, true);
            return rBitmap;
        }



    }
}


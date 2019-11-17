package id.ac.itb.a8eh.radiostreaming;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import id.ac.itb.a8eh.radiostreaming.model.ProgramSchedule;
import id.ac.itb.a8eh.radiostreaming.service.StreamingPlayerService;
import id.ac.itb.a8eh.radiostreaming.util.NonScrollListView;
import id.ac.itb.a8eh.radiostreaming.util.ProgramScheduleArrayAdapter;
import id.ac.itb.a8eh.radiostreaming.util.SliderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private boolean isPlaying;
    private static JSONArray todaySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try {
            Thread.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
        }

        @SuppressLint("WrongViewCast") final RelativeLayout menu = (RelativeLayout) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        showAbout();
                        Toast.makeText(getApplicationContext(), "Version v1.0", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        }); //closing the setOnClickListener method


        final ImageButton imageMenu = (ImageButton) findViewById(R.id.imageMenu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

//                        showAbout();
                        Toast.makeText(getApplicationContext(), "Version v1.0", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }

        }); //closing the setOnClickListener method


//        TextView judulLagu1 = (TextView) findViewById(R.id.judulLagu);
////        judulLagu1.setSelected(true);
//        judulLagu1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_moving));


//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                public void run() {
//
//                    URL url;
//                    try {
//                        url = new URL("http://8eh.itb.ac.id:8000");
//                        IcyStreamMeta icy = new IcyStreamMeta(url);
//
//                        Log.d("SONG",icy.getTitle());
//                        Toast.makeText(getApplicationContext(), "title", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), icy.getTitle(), Toast.LENGTH_SHORT).show();
//                        TextView asd = (TextView) findViewById(R.id.textSchedule);
//                        asd.setText(icy.getTitle());
//
//                        Log.d("ARTITSi",icy.getArtist());
//                        Toast.makeText(getApplicationContext(), icy.getArtist(), Toast.LENGTH_SHORT).show();
//                    } catch (MalformedURLException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            }, 0, 10000);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        final ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance


        /////////////////////////////GET Slider/////////////////////////////////
//        // Load image, decode it to Bitmap and return Bitmap to callback
//        imageLoader.loadImage("http://irfanmim.tk/api/img/program/gocom.jpg", new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                // Do whatever you want with Bitmap

                final ViewPager mPager;
                final int[] currentPage = {0};
                final Integer[] XMEN= {R.drawable.slider2,R.drawable.slider3};
                ArrayList<Integer> XMENArray = new ArrayList<Integer>();


                for(int i=0;i<XMEN.length;i++)
                    XMENArray.add(XMEN[i]);

                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(new SliderAdapter(MainActivity.this, XMENArray));
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                indicator.setViewPager(mPager);

                // Auto start of viewpager
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage[0] == XMEN.length) {
                            currentPage[0] = 0;
                        }
                        mPager.setCurrentItem(currentPage[0]++, true);
                    }
                };
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 5000, 5000);
//            }
//        });




        /////////////////////////////GET Schedule////////////////////////////////
//        final TextView mTextViewJson = (TextView) findViewById(R.id.testJson);
        final JSONArray[] listSchedule = new JSONArray[1];

        // Instantiate the RequestQueue.
        RequestQueue queueProgram = Volley.newRequestQueue(this);
//        String urlGetJsonProgram ="http://irfanmim.tk/api/schedule.json";
        String urlGetJsonProgram ="http://8eh.itb.ac.id/api/public?category=schedule";

        // Request a string response from the provided URL.
        StringRequest stringRequestProgram = new StringRequest(Request.Method.GET, urlGetJsonProgram,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            findViewById(R.id.progressBarSchedule).setVisibility(View.GONE);

                            JSONObject objectProgram = new JSONObject(response);
                            listSchedule[0] = objectProgram.getJSONArray("schedules");
//                            mTextViewJson.setText(listProgram[0].getJSONObject(0).toString() + ':' + objectProgram.getString("todaySchedule"));

                            ArrayList<ProgramSchedule> rentalProperties = new ArrayList<>();
                            for (int i = 0; i < listSchedule[0].length(); i++) {
                                try {
                                    rentalProperties.add(new ProgramSchedule(i,
                                            listSchedule[0].getJSONObject(i).getString("title"),
                                            listSchedule[0].getJSONObject(i).getString("time"),
                                            listSchedule[0].getJSONObject(i).getString("announcer")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //create our new array adapter
                            ArrayAdapter<ProgramSchedule> adapter = new ProgramScheduleArrayAdapter(getApplicationContext(), 0, rentalProperties);

                            NonScrollListView test = (NonScrollListView) findViewById(R.id.programSchedule);
                            test.setDivider(null);
                            test.setAdapter(adapter);

                        } catch (JSONException e) {
//                            mTextViewJson.setText("Can't get todaySchedule");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.progressBarSchedule).setVisibility(View.GONE);
                findViewById(R.id.errorTextSchedule).setVisibility(View.VISIBLE);
//                mTextViewJson.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queueProgram.add(stringRequestProgram);




        ///////////////////////////GET Carousel/////////////////////////
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.program);

        final JSONArray[] listProgram = new JSONArray[1];
//        String urlProgramApi = "http://irfanmim.tk/api/program.json";
        String urlProgramApi = "http://8eh.itb.ac.id/api/public?category=program";
        final DisplayImageOptions options1=new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

//                .showImageOnLoading(R.drawable.stub)
//                .showImageForEmptyUri(R.drawable.stub)
//                .showImageOnFail(R.drawable.stub)


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlProgramApi, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        findViewById(R.id.progressBarProgram).setVisibility(View.GONE);

//                        mTextViewJson.setText("Response: " + response.toString());

                        try {
                            listProgram[0] = response.getJSONArray("programs");

                            for (int i = 0; i < listProgram[0].length(); i++) {
                                String mImageURLString = listProgram[0].getJSONObject(i).getString("url");
                                //New ImageView
                                ImageButton im = new ImageButton(getApplicationContext());

                                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
                                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105, getResources().getDisplayMetrics());
                                im.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(im.getLayoutParams());
                                int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
                                int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
                                marginParams.setMargins(left, 0, right, 0);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
                                im.setLayoutParams(layoutParams);

                                // Drawable testDrawable = new BitmapDrawable(getResources(), response);

                                //Image Request
                                // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
                                //	which implements ImageAware interface)
//                                im.setScaleType(ImageView.ScaleType.FIT_XY);
//                                im.setAdjustViewBounds(true);
                                imageLoader.displayImage(mImageURLString, im, options1);

                                // Drawable testDrawable = new BitmapDrawable(getResources(), response);
                                // im.setBackgroundDrawable(testDrawable);

                                final String programName = listProgram[0].getJSONObject(i).getString("name");

                                im.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this, programName, Toast.LENGTH_SHORT).show();
                                        //Open intent
                                    }
                                });

                                linearLayout.addView(im);
                            }

                        } catch (JSONException e) {
//                            mTextViewJson.setText("Can't get program");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
//                        mTextViewJson.setText("Error");
                        findViewById(R.id.progressBarProgram).setVisibility(View.GONE);
                        findViewById(R.id.errorTextProgram).setVisibility(View.VISIBLE);
                    }
                });

        // Add the request to the RequestQueue.
        queueProgram.add(jsObjRequest);




        /////////////////////GET Now Playing///////////////////////////
        //        TextView judulLagu = (TextView) findViewById(R.id.MarqueeText);
        final TextView judulLagu = (TextView) findViewById(R.id.judulLagu);
        judulLagu.setSelected(true);
//        String urlNowPlayingApi = "http://irfanmim.tk/api/nowPlaying.json";
        String urlNowPlayingApi = "http://8eh.itb.ac.id/api/public?category=nowPlaying";

        JsonObjectRequest jsObjNowPlayingRequest = new JsonObjectRequest
                (Request.Method.GET, urlNowPlayingApi, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String title = response.getString("title");
                        judulLagu.setText(title);
                        judulLagu.setSelected(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

        // Add the request to the RequestQueue.
        queueProgram.add(jsObjNowPlayingRequest);




//        final NonScrollListView lv = (NonScrollListView) findViewById(R.id.todaySchedule);



//        final String url = "http://8eh.itb.ac.id/img/upload/_2_januari.jpg";
//        final String url = "https://www.itb.ac.id/files/images/1503898729.jpg";
//        final ImageView mImageView = (ImageView) findViewById(R.id.radio);

//        final TextView mTxtDisplay;
//        mTxtDisplay = (TextView) findViewById(R.id.testJson);
//        String urlApi = "http://jauhararifin.cf/apis/nim/searcher/students?keyword=irfan&page=1&itemPerPage=30";
//        String urlApi = "http://irfanmim.tk/api/summary.json";
//        String urlApi = "http://192.168.43.173/api/todaySchedule.php";

//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                your_array_list );

//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, urlApi, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
////                        TextView status = (TextView) findViewById(R.id.status);
////                        try {
//////                            status.setText(response.getBoolean("success") ? "true" : "false");
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//
//                        try {
//                            // This is the array adapter, it takes the context of the activity as a
//                            // first parameter, the type of list view as a second parameter and your
//                            // array as a third parameter.
//
//
////                            mTxtDisplay.setText("Response: " + response.getJSONArray("todaySchedule").toString());
////                            todaySchedule = response.getJSONArray("todaySchedule");
//                            JSONArray todaySchedule1 = response.getJSONArray("todaySchedule");
//
//                            int length = todaySchedule1.length();
//                            for (int i=0; i < length; i++) {
//                                your_array_list.add(todaySchedule1.getJSONObject(i).getString("title"));
////                                your_array_list.add("title");
//                            }
////                            your_array_list.add(todaySchedule1.length() + "");
////                            lv.setOnClickListener(null);
////                            lv.setDivider(null);
////                            lv.setAdapter(arrayAdapter);
////                            mTxtDisplay.setText("Response: " + todaySchedule1);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });


// Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this).addToRequestQueue(request);
//        mRequestQueue.add(request);

//        mRequestQueue.add(jsObjRequest);




        final ImageButton playButton = (ImageButton) findViewById(R.id.playMainMenu);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    stopService(new Intent(getApplicationContext(), StreamingPlayerService.class));
                    isPlaying = false;
                    playButton.setBackgroundResource(R.drawable.btn_play);
                } else {
                    startService(new Intent(getApplicationContext(), StreamingPlayerService.class));
                    isPlaying = true;
                    playButton.setBackgroundResource(R.drawable.btn_pause);
                }
            }
        });


        Intent intent = getIntent();
        String status = intent.getStringExtra("isPlaying");
        if (TextUtils.equals(status, "false") || TextUtils.isEmpty(status)) {
            isPlaying = false;
//            Toast toast = Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT);
//            toast.show();
        } else {
            isPlaying = true;
            playButton.setBackgroundResource(R.drawable.btn_pause);
//            Toast toast = Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT);
//            toast.show();
        }


//        ImageButton streaming = (ImageButton) findViewById(R.id.stream);
//        streaming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent stream = new Intent(getApplicationContext(), StreamingScreen.class);
//                if (isPlaying) {
//                    stream.putExtra("isPlaying", "true");
//                } else {
//                    stream.putExtra("isPlaying", "false");
//                }
//                startActivity(stream);
//            }
//        });

        RelativeLayout bawah = (RelativeLayout) findViewById(R.id.bawah);
        bawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stream = new Intent(getApplicationContext(), StreamingScreen.class);
                if (isPlaying) {
                    stream.putExtra("isPlaying", "true");
                } else {
                    stream.putExtra("isPlaying", "false");
                }
                startActivity(stream);
            }
        });





    }

    @Override
    public void onBackPressed() {
        if (isPlaying) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showAbout() {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.about,null);

        //Get the devices screen density to calculate correct pixel sizes
        float density = MainActivity.this.getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, (int)density*240, (int)density*285, true);

        //Set up touch closing outside of pop-up
//                        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        pw.setElevation(10);

        // display the pop-up in the center
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }
}


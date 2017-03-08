package com.arpaul.redeyesort;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arpaul.redeyesort.adapter.InstaAdapter;
import com.arpaul.redeyesort.common.AppConstant;
import com.arpaul.redeyesort.dataobject.ImageCellDO;
import com.arpaul.redeyesort.webservices.FetchDataService;
import com.arpaul.redeyesort.webservices.WEBSERVICE_CALL;
import com.arpaul.redeyesort.webservices.WEBSERVICE_TYPE;
import android.support.design.widget.FloatingActionButton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.arpaul.redeyesort.common.ApplicationInstance.LOADER_FETCH_JSON_DATA;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private FloatingActionButton fab;
    private ProgressBar pbLoading;
    private TextView tvStatus;
    private RecyclerView rvList;

    private InstaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseControls();

        bindControls();
    }

    void bindControls() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLoading.setVisibility(View.VISIBLE);
                adapter.refresh(new ArrayList<ImageCellDO>());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(ImageCellDO objImageCell : arrImageCellDO) {
                            try {
                                URL url = new URL(objImageCell.imageURL);
                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                objImageCell.cellBitmap = AppConstant.splitImage(image);
                            } catch(IOException e) {
                                System.out.println(e);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.refresh(arrImageCellDO);
                                pbLoading.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();

            }
        });

        getSupportLoaderManager().initLoader(LOADER_FETCH_JSON_DATA, null, this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        pbLoading.setVisibility(View.VISIBLE);
        tvStatus.setText("Loading data");
        switch (id){
            case LOADER_FETCH_JSON_DATA:
                return new FetchDataService(this, WEBSERVICE_TYPE.GET, WEBSERVICE_CALL.ALL);

            default:
                return null;
        }
    }

    ArrayList<ImageCellDO> arrImageCellDO;
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()){
            case LOADER_FETCH_JSON_DATA:
                arrImageCellDO = (ArrayList<ImageCellDO>) data;
                if(arrImageCellDO != null && arrImageCellDO.size() > 0) {
                    adapter.refresh(arrImageCellDO);
                    rvList.setVisibility(View.VISIBLE);
                    tvStatus.setVisibility(View.GONE);
                } else {
                    tvStatus.setText("Failure");
                    rvList.setVisibility(View.GONE);
                }
                break;

            default:
                return;
        }
        getSupportLoaderManager().destroyLoader(loader.getId());
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    void initialiseControls() {
        pbLoading   = (ProgressBar) findViewById(R.id.pbLoading);
        tvStatus    = (TextView) findViewById(R.id.tvStatus);
        rvList      = (RecyclerView) findViewById(R.id.rvList);
        fab         = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new InstaAdapter(this, new ArrayList<ImageCellDO>());
        rvList.setAdapter(adapter);
        pbLoading.setVisibility(View.GONE);
    }
}

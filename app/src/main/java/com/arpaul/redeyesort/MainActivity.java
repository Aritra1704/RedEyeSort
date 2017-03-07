package com.arpaul.redeyesort;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ImageView ivActualImage, ivModifiedImage;
    private Button btnSplit;
    private final int RESULT_LOAD_IMAGE = 1;
    int chunkNumbers = 16;
    private int ROWBYCOLUMN = 4;
//    ArrayList<Bitmap> chunkedImages;
    private ArrayList<ImageCellDO> chunkedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseControls();

        bindControls();
    }

    void bindControls() {
        btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chunkNumbers = ROWBYCOLUMN * ROWBYCOLUMN;
                splitImage(ivActualImage, chunkNumbers);
            }
        });

    }

    /**
     * Splits the source image and show them all into a grid in a new activity
     *
     * @param image The source image to split
     * @param chunkNumbers The target number of small image chunks to be formed from the   source image
     */
    private void splitImage(ImageView image, int chunkNumbers) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;

        //To store all the small image chunks in bitmap format in this list
//        chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
        chunkedImages = new ArrayList<>();

        //Getting the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        ImageCellDO objImageCellDO;
        for(int x=0; x < rows; x++){
            int xCoord = 0;
            for(int y=0; y < cols; y++){
                objImageCellDO = new ImageCellDO();
                objImageCellDO.cellBitmap = Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);

                int touchedRGB = objImageCellDO.cellBitmap.getPixel(objImageCellDO.cellBitmap.getWidth() - 10, objImageCellDO.cellBitmap.getHeight() - 10);
                objImageCellDO.redFilter = Color.red(touchedRGB);
                chunkedImages.add(objImageCellDO);
//                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        // Function of merge the chunks images(after image divided in pieces then i can call this function to combine and merge the image as one)
        mergeImage(/*chunkedImages*/);
    }

    void mergeImage(/*ArrayList<Bitmap> imageChunks*/) {

//        Collections.shuffle(imageChunks);

        sortRedFilter(/*chunkedImages*/);
        //Get the width and height of the smaller chunks
        int chunkWidth = chunkedImages.get(0).cellBitmap.getWidth();
        int chunkHeight = chunkedImages.get(0).cellBitmap.getHeight();

        //create a bitmap of a size which can hold the complete image after merging
        Bitmap bitmap = Bitmap.createBitmap(chunkWidth * ROWBYCOLUMN, chunkHeight * ROWBYCOLUMN,  Bitmap.Config.ARGB_4444);

        //create a canvas for drawing all those small images
        Canvas canvas = new Canvas(bitmap);
        int count = 0;
        for(int rows = 0; rows < ROWBYCOLUMN; rows++){
            for(int cols = 0; cols < ROWBYCOLUMN; cols++){
                canvas.drawBitmap(chunkedImages.get(count).cellBitmap, chunkWidth * cols, chunkHeight * rows, null);
                count++;
            }
        }

    /*
     * The result image is shown in a new Activity
     */

    if(bitmap != null)
        ivModifiedImage.setImageBitmap(bitmap);
    else
        Toast.makeText(this, "bitmap null", Toast.LENGTH_SHORT).show();
    }

    void sortRedFilter(/*ArrayList<Bitmap> imageChunks*/) {
//        ArrayList<ImageCellDO> arrImageCellObject = new ArrayList<>();
//        ImageCellDO objImageCell;
//        for(Bitmap bitmap : imageChunks) {
//            int touchedRGB = bitmap.getPixel(bitmap.getWidth() - 10, bitmap.getHeight() - 10);
//
//            objImageCell = new ImageCellDO();
//            objImageCell.redFilter = Color.red(touchedRGB);
//            objImageCell.cellBitmap = bitmap;
//            arrImageCellObject.add(objImageCell);
//        }

        Collections.sort(chunkedImages, new Comparator<ImageCellDO>() {
            @Override
            public int compare(ImageCellDO objImageCellDO1, ImageCellDO objImageCellDO2) {
                return ((Integer) objImageCellDO1.redFilter).compareTo(objImageCellDO2.redFilter);
            }
        });
    }

    void initialiseControls() {
        ivActualImage = (ImageView)findViewById(R.id.ivActualImage);
        ivModifiedImage = (ImageView)findViewById(R.id.ivModifiedImage);

        btnSplit = (Button) findViewById(R.id.btnSplit);
    }
}

package com.arpaul.redeyesort.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.arpaul.redeyesort.dataobject.ImageCellDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ARPaul on 08-03-2017.
 */

public class AppConstant {

    private static int ROWBYCOLUMN = 4;
    private static ArrayList<ImageCellDO> chunkedImages;

    public static final String INSTAGRAM_ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";

    public static final String REST_URL = "https://api.instagram.com/v1/users/self/media/recent?access_token="
            + INSTAGRAM_ACCESS_TOKEN;

    public static Bitmap getBitmapfromView(ImageView image) {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = splitImage(bitmap);

        return bitmap;
    }
    /**
     * Splits the source image and show them all into a grid in a new activity
     * @param bitmap The source image to split
     */
    public static Bitmap splitImage(Bitmap bitmap) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;

        //To store all the small image chunks in bitmap format in this list
//        chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
        chunkedImages = new ArrayList<>();

        //Getting the scaled bitmap of the source image
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        rows = cols = ROWBYCOLUMN;
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        ImageCellDO objImageCellDO;
        for(int x = 0; x < rows; x++){
            int xCoord = 0;
            for(int y=0; y < cols; y++){
                objImageCellDO = new ImageCellDO();
                objImageCellDO.cellBitmap = Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight);

                int touchedRGB = objImageCellDO.cellBitmap.getPixel(objImageCellDO.cellBitmap.getWidth() - 1, objImageCellDO.cellBitmap.getHeight() - 1);
                objImageCellDO.redFilter = Color.red(touchedRGB);
                chunkedImages.add(objImageCellDO);
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        // Function of merge the chunks images(after image divided in pieces then i can call this function to combine and merge the image as one)
        return mergeImage();
    }

    private static Bitmap mergeImage() {

        sortRedFilter();
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

        return bitmap;
    }

    private static void sortRedFilter() {
        Collections.sort(chunkedImages, new Comparator<ImageCellDO>() {
            @Override
            public int compare(ImageCellDO objImageCellDO1, ImageCellDO objImageCellDO2) {
                return ((Integer) objImageCellDO1.redFilter).compareTo(objImageCellDO2.redFilter);
            }
        });
    }
}

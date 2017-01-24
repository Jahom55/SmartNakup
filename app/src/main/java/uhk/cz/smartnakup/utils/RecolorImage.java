package uhk.cz.smartnakup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uhk.cz.smartnakup.R;
import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.controllers.TableControllerProductCart;
import uhk.cz.smartnakup.controllers.TableControllerProductDB;

/**
 * Created by Jaromir on 10.4.2016.
 */
public class RecolorImage {
    static int width = 0;
    static int height = 0;
    static int x;
    static int y;
    static int smer = 0; // 0 vlevo 1 nahoru 2 dolu 3 pravo
    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        RecolorImage.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        RecolorImage.height = height;
    }

    public static int getSmer() {
        return smer;
    }

    public static void setSmer(int smer) {
        RecolorImage.smer = smer;
    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        RecolorImage.x = x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        RecolorImage.y = y;
    }

    public static void initializateWidthAndHeightFromBitmap(Context context){
        Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lidl);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        height =tempBitmap.getHeight();
        width = tempBitmap.getWidth();
    }

    //Method for draw shopping cart and actual position to Plan
    public static void getNakup(ImageView imageView, Context context, boolean canIMove, int x, int y,boolean step, Float azimut){
        List<ObjectCart> cartproducts = new TableControllerProductCart(context).read();
        List<ObjectProduct> products = new ArrayList<>();
        List<int[]> sektors = new ArrayList<>();

        Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lidl);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        height =tempBitmap.getHeight();
        width = tempBitmap.getWidth();
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        for (ObjectCart pr: cartproducts) {
            ObjectProduct objectProduct = new TableControllerProductDB(context).readSingleRecord(pr.getProduct());
            if (pr.getBought() == 0) {
                myPaint.setColor(Color.RED);
            }else {
                myPaint.setColor(Color.GREEN);
            }
            tempCanvas.drawCircle(objectProduct.getXcor(), objectProduct.getYcor(), 30, myPaint);
            products.add(objectProduct);
        }

        for (ObjectProduct pr : products){
            sektors.add(getSectorsPosition(pr.getXcor(),pr.getYcor()));
        }
        //Draw  lines at aisle
        boolean[] isInSector = drawLineInAisle(tempCanvas,myPaint,sektors);
        //Draw joiningLine between Aisle
        drawJoinLine(isInSector,tempCanvas,myPaint);
        //Draw actual position
        drawWhereAmI(tempCanvas,myPaint,canIMove, x, y,tempBitmap,step, azimut);

        //Create new bitmap
        Drawable d = new BitmapDrawable(context.getResources(), tempBitmap);
        Drawable bitmap = ContextCompat.getDrawable(context, R.drawable.lidl);
        imageView.setImageDrawable(d);
    }

    public static boolean[] drawLineInAisle (Canvas tempCanvas, Paint myPaint,List<int[]> sektors){
        boolean[] isInSector = new boolean[8];       //7  5   3    1
        Arrays.fill(isInSector, Boolean.FALSE);      //6  4    2    0

        //Vykresleni jednotlibych car mezi regalama
        for (int[] sektor : sektors) {
            if (sektor[0] == 3) {
                if (sektor[1] == 1) {
                    myPaint.setColor(Color.BLUE);
                    tempCanvas.drawLine(933, 1600, 933, 850, myPaint);
                    isInSector[0]= true;
                }
            }
            if (sektor[0] == 1) {
                if (sektor[1] == 0) {
                    myPaint.setColor(Color.BLUE);
                    tempCanvas.drawLine(405, 850, 405, 120, myPaint);
                    isInSector[5]= true;
                }
            }
            if (sektor[0] == 0) {
                if (sektor[1] == 0) {
                    myPaint.setColor(Color.BLUE);
                    tempCanvas.drawLine(150, 120, 150, 850, myPaint);
                    isInSector[7]= true;
                }
            }

            if (sektor[0] == 2) {
                if (sektor[1] == 1) {
                    myPaint.setColor(Color.BLUE);
                    tempCanvas.drawLine(680, 850, 680, 1600, myPaint);
                    isInSector[2]= true;
                }
            }
        }
        return isInSector;
    }

    //Draw actual position of user
    public static void drawWhereAmI(Canvas tempCanvas, Paint myPaint, boolean canImove, int x, int y, Bitmap tempBitmap, boolean step, Float azimut){
        myPaint.setColor(Color.YELLOW);
        if (canImove) {
            for(int i = 0; i < 100;i++) {
                if (x + i <= 1064) {
                    int color_pixelE = tempBitmap.getPixel(x + i, y);
                    if (color_pixelE == -8684033) {
                        tempCanvas.drawCircle(x + i, y, 15, myPaint);
                        setX(x+i);setY(y);
                        break;
                    }
                }
                if (x - i >= 0) {
                    int color_pixelW = tempBitmap.getPixel(x - i, y);
                    if (color_pixelW == -8684033) {
                        tempCanvas.drawCircle(x - i, y, 15, myPaint);
                        setX(x-i);setY(y);
                        break;
                    }
                }
                if (y + i <= 1704) {
                    int color_pixelS = tempBitmap.getPixel(x, y + i);
                    if (color_pixelS == -8684033) {
                        tempCanvas.drawCircle(x, y + i, 15, myPaint);
                        setX(x);setY(y+i);
                        break;
                    }
                    if (y - i >= 0) {
                        int color_pixelN = tempBitmap.getPixel(x, y - i);
                        if (color_pixelN == -8684033) {
                            tempCanvas.drawCircle(x, y - i, 15, myPaint);
                            setX(x+i);setY(y-i);
                            break;
                        }
                    }
                }
            }
            return;
        } else if (step){
            //First run app azimut is null, so change to unreal value
            if (azimut == null) azimut = 100.0f;

            Point point = checkIfAtCrossroad(tempCanvas,tempBitmap,myPaint,getX(),getY());
            x = point.x;
            y = point.y;

            for(int i = 0; i < 17;i = i + 16) {
                if (x - 16 >= 0) {
                    int color_pixelE = tempBitmap.getPixel(x - i, y);
                    if (color_pixelE == -8684033 && azimut < -1.2 && azimut > -2) {
                        tempCanvas.drawCircle(x - i, y, 15, myPaint);
                        setX(x - i);
                        setY(y);
                        return;

                    }
                }
                if (y - 16 >= 0) {
                    int color_pixelN = tempBitmap.getPixel(x, y - i);
                    if (color_pixelN == -8684033 && color_pixelN != -1 && getSmer() != 2 && azimut < 0.4 && azimut > -0.4) {
                        setX(x);
                        setY(y - i);
                        int color_pixelNNext = tempBitmap.getPixel(x, (int) (y - i));
                        if (color_pixelNNext == -256 && color_pixelNNext == -1) {
                            setSmer(2);
                        }
                        tempCanvas.drawCircle(x, y - i, 15, myPaint);
                        return;
                    }
                }
                if (y + 16 <= 1704) {
                    int color_pixelS = tempBitmap.getPixel(x, y + i);
                    if (color_pixelS == -8684033 && getSmer() != 1 && ((azimut < 3.6 && azimut > 2.6) || (azimut > -3.6 && azimut < -2.6))) {
                        tempCanvas.drawCircle(x, y + i, 15, myPaint);
                        setX(x);
                        setY(y + i);
                        int color_pixelSNext = tempBitmap.getPixel(x, (int) (y + i));
                        if (color_pixelSNext == -256 && color_pixelSNext == -1) {
                            setSmer(1);
                        }
                        return;
                    }
                }
            }
        } else {
            setX(x);setY(y);
            tempCanvas.drawCircle(x, y,15, myPaint);
        }
    }

    private static Point checkIfAtCrossroad(Canvas tempCanvas, Bitmap tempBitmap, Paint myPaint, int x, int y){
        int rozdilNaKrizovatceProX = x - 680;
        int rozdilNaKrizovatceProY = y - 1600;
        int rozdilNaKrizovatceProX2 = x - 680;
        int rozdilNaKrizovatceProY2 = y - 850;
        int rozdilNaKrizovatceProX3 = x - 405;
        int rozdilNaKrizovatceProY3 = y - 120;
        int rozdilNaKrizovatceProX4 = x - 150;
        int rozdilNaKrizovatceProY4 = y - 120;
        int rozdilNaKrizovatceProX5 = x - 405;
        int rozdilNaKrizovatceProY5 = y - 850;

        tempCanvas.drawCircle(x, y, 15, myPaint);
        if (Math.abs(rozdilNaKrizovatceProX) < 15 && Math.abs(rozdilNaKrizovatceProY) < 15 ){
            x = 680;
            y = 1600;
        }
        else if (Math.abs(rozdilNaKrizovatceProX2) < 15 && Math.abs(rozdilNaKrizovatceProY2) < 15 ){
            x = 680;
            y = 850;
            int pixelHorni = tempBitmap.getPixel(x - 16, y);
            if (pixelHorni == -256) {
                setSmer(1);
            } else {
                setSmer(2);
            }
        } else if (Math.abs(rozdilNaKrizovatceProX3) < 15 && Math.abs(rozdilNaKrizovatceProY3) < 15 ){
            x = 405;
            y = 120;
            setSmer(2);
        } else if (Math.abs(rozdilNaKrizovatceProX4) < 15 && Math.abs(rozdilNaKrizovatceProY4) < 15 ){
            x = 150;
            y = 120;
            setSmer(2);
        }else if (Math.abs(rozdilNaKrizovatceProX5) < 15 && Math.abs(rozdilNaKrizovatceProY5) < 15 ){
            x = 405;
            y = 850;
            int pixelHorni = tempBitmap.getPixel(x, y-16);
            if (pixelHorni == -8684033) {
                setSmer(1);
            } else {
                setSmer(2);
            }
        }
        return new Point(x,y);
    }

    public static void drawJoinLine(boolean[] isInSector, Canvas tempCanvas, Paint myPaint){
        if(!isInSector[0] && !isInSector[1]) {
            if (isInSector[2]) {
                tempCanvas.drawLine(933, 1600, 680, 1600, myPaint);
                if (isInSector[3]) {
                    if (isInSector[5]) {
                        tempCanvas.drawLine(680, 120, 405, 120, myPaint);
                        if (isInSector[4]) {
                            if (isInSector[6] || isInSector[7]) {
                                tempCanvas.drawLine(405, 1600, 150, 1600, myPaint);
                            }
                        }
                    }
                } else if (isInSector[5]){
                        tempCanvas.drawLine(680, 850, 405, 850, myPaint);
                        if (isInSector[7] || isInSector[6]) {
                            tempCanvas.drawLine(405, 120, 150, 120, myPaint);
                            tempCanvas.drawLine(150, 1600, 150, 850, myPaint);
                        }

                }
            } else {
                if (isInSector[3]) {
                    tempCanvas.drawLine(933, 850, 680, 850, myPaint);
                    tempCanvas.drawLine(933, 1600, 933, 850, myPaint);
                }
                    if (isInSector[5]) {
                        tempCanvas.drawLine(933, 850, 405, 850, myPaint);
                        tempCanvas.drawLine(933, 1600, 933, 850, myPaint);
                        if (isInSector[7]) {
                            tempCanvas.drawLine(405, 120, 150, 120, myPaint);
                            tempCanvas.drawLine(150, 1600, 150, 850, myPaint);
                        }

                }
            }
        }
    }

    //Metoda, kde lezi jednotlive sektory (ulicky mezi regalama)
    //params - position of product
    public static int[] getSectorsPosition(int posX, int posY){
        int[] sector = new int[2];
        int xStart = 49;
        int yStart = 41;

        for (int height = 0; height < 3;height++) {
            for (int width = 0; width < 5; width++) {
                if (posX < width*250+49){
                    sector[0] = width - 1;
                    break;
                }
            }
            if (posY < height*817+41){
                sector[1] = height;
                sector[1]-= 1;
                break;
            }
        }
        return sector;
    }
}

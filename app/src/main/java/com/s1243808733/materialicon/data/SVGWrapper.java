package com.s1243808733.materialicon.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import com.blankj.utilcode.util.ImageUtils;
import com.caverock.androidsvg.SVG;
import java.io.Serializable;

public class SVGWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

    private final SVG svg;

    public SVGWrapper(SVG svg) {
        this.svg = svg;
    }

    public SVG getSvg() {
        return svg;
    }

	public Picture renderToPicture() {
        return svg.renderToPicture();
	}

	public Picture createPicture(int size) {
		return createPicture(size, size);
	}

	public Picture createPicture(int width, int height) {
        return createPicture(svg.renderToPicture(), width, height);
    }

	public static Picture createPicture(Picture picture, int width, int height) {
        Picture newPicture = new Picture();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas = newPicture.beginRecording(width, height);
        canvas.drawPicture(picture, new Rect(0, 0, width, height));
        newPicture.endRecording();
        return newPicture;
    }

    public PictureDrawable createDrawable(int size) {
        return createDrawable(size, size);
    }

    public PictureDrawable createDrawable(int width, int height) {
        return createDrawable(svg.renderToPicture(), width, height);
    }

    public static PictureDrawable createDrawable(Picture picture, int width, int height) {
        return new PictureDrawable(createPicture(picture, width, height));
    }

    public Bitmap createBitmap(int size) {
        return createBitmap(size, size);
    }

    public Bitmap createBitmap(int width, int height) {
        return ImageUtils.drawable2Bitmap(createDrawable(width, height));
    }

}

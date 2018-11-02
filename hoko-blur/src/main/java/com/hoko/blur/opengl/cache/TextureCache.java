package com.hoko.blur.opengl.cache;

import com.hoko.blur.opengl.texture.Texture;
import com.hoko.blur.opengl.texture.TextureFactory;
import com.hoko.blur.opengl.size.Size;

/**
 * Created by yuxfzju on 17/1/20.
 */

public class TextureCache {

    private static volatile TextureCache sInstance;

    private CachePool<Size, Texture> mCache;

    private TextureCache() {
        mCache = new CachePool<Size, Texture>() {

            @Override
            protected Texture create(Size size) {
                if (size == null) {
                    return null;
                }
                return TextureFactory.create(size.width(), size.height());
            }


            @Override
            protected void entryDeleted(Texture texture) {
                if (texture != null) {
                    texture.delete();
                }
            }

            @Override
            protected boolean checkHit(Size a, Texture b) {
                return a != null && b != null && a.width() == b.width() && a.height() == b.height();
            }
        };
    }

    public static TextureCache getInstance() {
        if (sInstance == null) {
            synchronized (TextureCache.class) {
                if (sInstance == null) {
                    sInstance = new TextureCache();
                }
            }
        }

        return sInstance;
    }

    public Texture getTexture(int width, int height) {

        if (mCache != null) {
            return mCache.get(new Size(width, height));
        }

        return null;
    }

    public void recycleTexture(Texture texture) {
        if (texture != null) {
            mCache.put(texture);
        }
    }

    public void deleteTextures() {
        if (mCache != null) {
            mCache.evictAll();
        }
    }
}

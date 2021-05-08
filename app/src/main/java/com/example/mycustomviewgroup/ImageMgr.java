package com.example.mycustomviewgroup;

import android.widget.ImageView;
import java.util.HashMap;
import java.util.Map;

/*
 * 图片唯一ID与view的对应关系
 */
public class ImageMgr {
    private Map<String, ImageView> map = new HashMap<>();

    // 增加图片及imageView
    public void addImage(String uuid, ImageView imageView) {
        map.put(uuid, imageView);
    }

    // 删除指定图片
    public void deleteImage(String uuid){
        if (map.containsKey(uuid)) {
            map.remove(uuid);
        }
    }

    // 删除所有图片
    public void deleteAllImage(){
        map = new HashMap<>();
    }

    // 获取所有图片
    public Map<String, ImageView> getAllImage(){
        return map;
    }

    // 判断图片是否存在
    public boolean isExistImage(String uuid){
        return map.containsKey(uuid);
    }

    // 获取图片对应的imageView
    public ImageView getImageView(String uuid){
        if (isExistImage(uuid)){
            return map.get(uuid);
        } else {
            return null;
        }
    }
}

package com.sapir.nanittest.repositories;

import com.google.gson.Gson;
import com.sapir.nanittest.db.Storage;
import com.sapir.nanittest.models.BabyUser;

public class UserBabyRepository {

    private static UserBabyRepository instance;
    private final Gson gson = new Gson();

    public static UserBabyRepository getInstance() {
        if (instance == null) {
            instance = new UserBabyRepository();
        }

        return instance;
    }


    public BabyUser getBabyUserFromStorage() {
        String json = Storage.getInstance().getString(Storage.AppKey.BABY_USER, "");

        return gson.fromJson(json, BabyUser.class);
    }

    public void updateBabyUserInStorage(BabyUser babyUser) {
        String json = gson.toJson(babyUser);
        Storage.getInstance().putString(Storage.AppKey.BABY_USER, json);
    }

}

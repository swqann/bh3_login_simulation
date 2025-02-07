package com.github.haocen2004.login_simulation.data.database.sponsor;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SponsorRepo {
    private final List<SponsorData> allSponsors;
    private final SponsorDao sponsorDao;
    private final Context context;

    public SponsorRepo(Context context) {
        this.context = context;
        SponsorDatabase sponsorDatabase = SponsorDatabase.getDatabase(context.getApplicationContext());
        sponsorDao = sponsorDatabase.getSponsorDao();
        allSponsors = sponsorDao.getAllSponsors();
    }

    public void reset() {
        sponsorDao.deleteAllSponsors();
    }

    public SponsorDao getSponsorDao() {
        return sponsorDao;
    }

    public void refreshSponsors() {
        new Thread(() -> {
            AVQuery<AVObject> query = new AVQuery<>("Sponsors");
            query.findInBackground().subscribe(new Observer<List<AVObject>>() {
                public void onSubscribe(Disposable disposable) {
                }

                public void onNext(List<AVObject> Sponsors) {
                    // students 是包含满足条件的 Student 对象的数组
                        new Thread(() -> {
                            for (AVObject object : Sponsors) {
                                boolean hasData = false;
                                for (SponsorData sponsorData : allSponsors) {
                                    if (sponsorData.getScannerKey().equals(object.getString("scannerKey"))) {
                                        hasData = true;
                                        sponsorData.setName(object.getString("name"));
                                        sponsorData.setDesc(object.getString("desc"));
                                        sponsorData.setAvatarImgUrl(object.getString("avatarImgUrl"));
                                        sponsorData.setPersonalPageUrl(object.getString("personalPageUrl"));
                                        sponsorData.setDeviceId(object.getString("deviceId"));
                                        sponsorDao.updateSponsors(sponsorData);
                                    }
                                }
                                if (!hasData) {
                                    sponsorDao.insertSponsors(
                                            new SponsorData(
                                                    object.getString("name"),
                                                    object.getString("desc"),
                                                    object.getString("avatarImgUrl"),
                                                    object.getString("personalPageUrl"),
                                                    object.getString("deviceId"),
                                                    object.getString("scannerKey")
                                            )
                                    );
                                }
                            }

                        }).start();

                }

                public void onError(Throwable throwable) {
                    CrashReport.postCatchedException(throwable);
                }

                public void onComplete() {
                }
            });
        }).start();
    }


    public List<SponsorData> getAllSponsors() {
        return allSponsors;
    }

}

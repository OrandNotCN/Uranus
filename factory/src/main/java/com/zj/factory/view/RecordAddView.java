package com.zj.factory.view;

import com.zj.toolslib.date.TipsCalender;

public interface RecordAddView {
    int getType();

    String getBrief();

    TipsCalender getDate();

    int getColor();

    void setStatus(long status);

//    ContactModel getContact();
}

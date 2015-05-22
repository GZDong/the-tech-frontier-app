/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tech.frontier.db.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tech.frontier.db.PresentableDBAPI;
import com.tech.frontier.db.cmd.Command.RecommendCmd;
import com.tech.frontier.db.helper.DatabaseHelper;
import com.tech.frontier.entities.Recommend;
import com.tech.frontier.listeners.DataListener;

import java.util.ArrayList;
import java.util.List;

class RecommendDBAPIImpl extends PresentableDBAPI<Recommend> {

    public RecommendDBAPIImpl() {
        super(DatabaseHelper.TABLE_RECOMMENDS);
    }

    @Override
    public void loadDatasFromDB(DataListener<List<Recommend>> listener) {
        sDbExecutor.execute(new RecommendCmd(listener) {
            @Override
            protected List<Recommend> doInBackground(SQLiteDatabase database) {
                Cursor cursor = database.query(mTableName, null, null, null,
                        null, null, null);
                List<Recommend> result = queryResult(cursor);
                cursor.close();
                return result;
            }
        });
    }

    private List<Recommend> queryResult(Cursor cursor) {
        List<Recommend> recommends = new ArrayList<Recommend>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String url = cursor.getString(1);
            String imgUrl = cursor.getString(2);
            // 解析数据
            recommends.add(new Recommend(title, url, imgUrl));
        }
        return recommends;
    }

    /**
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Recommend item) {
        ContentValues newValues = new ContentValues();
        newValues.put("title", item.title);
        newValues.put("url", item.url);
        newValues.put("img_url", item.imgUrl);
        return newValues;
    }

}

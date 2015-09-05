/*
 * Copyright (C) 2012-2015 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.money.manager.ex.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.text.TextUtils;

import com.money.manager.ex.core.AccountTypes;
import com.money.manager.ex.core.ExceptionHandler;
import com.money.manager.ex.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Accounts
 */
public class AccountRepository
    extends RepositoryBase {

    public AccountRepository(Context context) {
        super(context, "accountlist_v1", DatasetType.TABLE, "accountlist");

        mAccount = new TableAccountList();
    }

    private TableAccountList mAccount;

    public Account loadModel(int accountId) {
        Cursor c = openCursor(null,
                TableAccountList.ACCOUNTID + "=?",
                new String[] { Integer.toString(accountId) });
        if (c == null) return null;

        Account account = null;

        if (c.moveToNext()) {
            account = new Account();
            account.loadFromCursor(c);
        }

        c.close();

        return account;
    }

    public TableAccountList load(int accountId) {
        TableAccountList result = new TableAccountList();

        String selection = TableAccountList.ACCOUNTID + "=?";

        Cursor cursor = mContext.getContentResolver().query(
                mAccount.getUri(),
                mAccount.getAllColumns(),
                selection,
                new String[] { Integer.toString(accountId) },
                null);
        if (cursor == null) return null;

        if (cursor.moveToFirst()) {
            result.setValueFromCursor(cursor);
        }

        cursor.close();

        return result;
    }

    public QueryAccountBills loadAccountBills(int accountId) {
        QueryAccountBills result = new QueryAccountBills(mContext);

        String selection = QueryAccountBills.ACCOUNTID + "=?";

        Cursor cursor = mContext.getContentResolver().query(
                result.getUri(),
                result.getAllColumns(),
                selection,
                new String[] { Integer.toString(accountId) },
                null);
        if (cursor == null) return null;

        if (cursor.moveToFirst()) {
            result.setValueFromCursor(cursor);
        }

        cursor.close();

        return result;
    }

    public int loadIdByName(String name) {
        int result = -1;

        if(TextUtils.isEmpty(name)) { return result; }

        String selection = TableAccountList.ACCOUNTNAME + "=?";

        Cursor cursor = mContext.getContentResolver().query(
                mAccount.getUri(),
                new String[] { TableAccountList.ACCOUNTID },
                selection,
                new String[] { name },
                null);

        if(cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(TableAccountList.ACCOUNTID));
        }

        cursor.close();

        return result;
    }

    public String loadName(int id) {
        String name = null;

        Cursor cursor = mContext.getContentResolver().query(mAccount.getUri(),
                new String[] { TableAccountList.ACCOUNTNAME },
                TableAccountList.ACCOUNTID + "=?",
                new String[]{Integer.toString(id)},
                null);
        if (cursor == null) return null;

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(TableAccountList.ACCOUNTNAME));
        }
        cursor.close();

        return name;
    }

}

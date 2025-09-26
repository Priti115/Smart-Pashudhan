package com.cattlebreed.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cattlebreed.app.data.converter.Converters;
import com.cattlebreed.app.data.entity.AnimalRecord;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AnimalRecordDao_Impl implements AnimalRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AnimalRecord> __insertionAdapterOfAnimalRecord;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<AnimalRecord> __deletionAdapterOfAnimalRecord;

  private final EntityDeletionOrUpdateAdapter<AnimalRecord> __updateAdapterOfAnimalRecord;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecords;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  public AnimalRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAnimalRecord = new EntityInsertionAdapter<AnimalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `animal_records` (`id`,`animalId`,`date`,`imagePath`,`bodyLength`,`height`,`chestWidth`,`rumpAngle`,`atcScore`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnimalRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getAnimalId());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, entity.getImagePath());
        statement.bindDouble(5, entity.getBodyLength());
        statement.bindDouble(6, entity.getHeight());
        statement.bindDouble(7, entity.getChestWidth());
        statement.bindDouble(8, entity.getRumpAngle());
        statement.bindLong(9, entity.getAtcScore());
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__deletionAdapterOfAnimalRecord = new EntityDeletionOrUpdateAdapter<AnimalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `animal_records` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnimalRecord entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAnimalRecord = new EntityDeletionOrUpdateAdapter<AnimalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `animal_records` SET `id` = ?,`animalId` = ?,`date` = ?,`imagePath` = ?,`bodyLength` = ?,`height` = ?,`chestWidth` = ?,`rumpAngle` = ?,`atcScore` = ?,`synced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnimalRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getAnimalId());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, entity.getImagePath());
        statement.bindDouble(5, entity.getBodyLength());
        statement.bindDouble(6, entity.getHeight());
        statement.bindDouble(7, entity.getChestWidth());
        statement.bindDouble(8, entity.getRumpAngle());
        statement.bindLong(9, entity.getAtcScore());
        final int _tmp_1 = entity.getSynced() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllRecords = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM animal_records";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE animal_records SET synced = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecord(final AnimalRecord record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAnimalRecord.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecord(final AnimalRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAnimalRecord.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRecord(final AnimalRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAnimalRecord.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllRecords(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecords.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllRecords.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsSynced(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AnimalRecord>> getAllRecords() {
    final String _sql = "SELECT * FROM animal_records ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"animal_records"}, new Callable<List<AnimalRecord>>() {
      @Override
      @NonNull
      public List<AnimalRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAnimalId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfBodyLength = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyLength");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfChestWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "chestWidth");
          final int _cursorIndexOfRumpAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rumpAngle");
          final int _cursorIndexOfAtcScore = CursorUtil.getColumnIndexOrThrow(_cursor, "atcScore");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<AnimalRecord> _result = new ArrayList<AnimalRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AnimalRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpAnimalId;
            _tmpAnimalId = _cursor.getString(_cursorIndexOfAnimalId);
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.util.Date', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final double _tmpBodyLength;
            _tmpBodyLength = _cursor.getDouble(_cursorIndexOfBodyLength);
            final double _tmpHeight;
            _tmpHeight = _cursor.getDouble(_cursorIndexOfHeight);
            final double _tmpChestWidth;
            _tmpChestWidth = _cursor.getDouble(_cursorIndexOfChestWidth);
            final double _tmpRumpAngle;
            _tmpRumpAngle = _cursor.getDouble(_cursorIndexOfRumpAngle);
            final int _tmpAtcScore;
            _tmpAtcScore = _cursor.getInt(_cursorIndexOfAtcScore);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _item = new AnimalRecord(_tmpId,_tmpAnimalId,_tmpDate,_tmpImagePath,_tmpBodyLength,_tmpHeight,_tmpChestWidth,_tmpRumpAngle,_tmpAtcScore,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecordById(final long id, final Continuation<? super AnimalRecord> $completion) {
    final String _sql = "SELECT * FROM animal_records WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AnimalRecord>() {
      @Override
      @Nullable
      public AnimalRecord call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAnimalId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfBodyLength = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyLength");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfChestWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "chestWidth");
          final int _cursorIndexOfRumpAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rumpAngle");
          final int _cursorIndexOfAtcScore = CursorUtil.getColumnIndexOrThrow(_cursor, "atcScore");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final AnimalRecord _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpAnimalId;
            _tmpAnimalId = _cursor.getString(_cursorIndexOfAnimalId);
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.util.Date', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final double _tmpBodyLength;
            _tmpBodyLength = _cursor.getDouble(_cursorIndexOfBodyLength);
            final double _tmpHeight;
            _tmpHeight = _cursor.getDouble(_cursorIndexOfHeight);
            final double _tmpChestWidth;
            _tmpChestWidth = _cursor.getDouble(_cursorIndexOfChestWidth);
            final double _tmpRumpAngle;
            _tmpRumpAngle = _cursor.getDouble(_cursorIndexOfRumpAngle);
            final int _tmpAtcScore;
            _tmpAtcScore = _cursor.getInt(_cursorIndexOfAtcScore);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _result = new AnimalRecord(_tmpId,_tmpAnimalId,_tmpDate,_tmpImagePath,_tmpBodyLength,_tmpHeight,_tmpChestWidth,_tmpRumpAngle,_tmpAtcScore,_tmpSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnsyncedRecords(final Continuation<? super List<AnimalRecord>> $completion) {
    final String _sql = "SELECT * FROM animal_records WHERE synced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AnimalRecord>>() {
      @Override
      @NonNull
      public List<AnimalRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAnimalId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfBodyLength = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyLength");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfChestWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "chestWidth");
          final int _cursorIndexOfRumpAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rumpAngle");
          final int _cursorIndexOfAtcScore = CursorUtil.getColumnIndexOrThrow(_cursor, "atcScore");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<AnimalRecord> _result = new ArrayList<AnimalRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AnimalRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpAnimalId;
            _tmpAnimalId = _cursor.getString(_cursorIndexOfAnimalId);
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.util.Date', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final double _tmpBodyLength;
            _tmpBodyLength = _cursor.getDouble(_cursorIndexOfBodyLength);
            final double _tmpHeight;
            _tmpHeight = _cursor.getDouble(_cursorIndexOfHeight);
            final double _tmpChestWidth;
            _tmpChestWidth = _cursor.getDouble(_cursorIndexOfChestWidth);
            final double _tmpRumpAngle;
            _tmpRumpAngle = _cursor.getDouble(_cursorIndexOfRumpAngle);
            final int _tmpAtcScore;
            _tmpAtcScore = _cursor.getInt(_cursorIndexOfAtcScore);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _item = new AnimalRecord(_tmpId,_tmpAnimalId,_tmpDate,_tmpImagePath,_tmpBodyLength,_tmpHeight,_tmpChestWidth,_tmpRumpAngle,_tmpAtcScore,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

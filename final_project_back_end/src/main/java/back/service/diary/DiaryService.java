package back.service.diary;

import java.util.List;

import back.exception.HException;
import back.model.diary.Diary;

public interface DiaryService{
	public boolean createDiary(Diary diary) throws HException;
	public List<Diary> getDiaryList(Diary diary);
	public Diary getDiaryById(int diaryId);
	public boolean updateDiary(Diary diary);
	public boolean deleteDiary(Diary diary);
}
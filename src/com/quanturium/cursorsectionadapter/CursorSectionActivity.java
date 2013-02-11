package com.quanturium.cursorsectionadapter;

import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CursorSectionActivity extends Activity
{
	CursorSectionDirectoryDatabase	directorDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fillDatabase();
		fillListView();
	}

	private void fillDatabase()
	{
		directorDatabase = CursorSectionDirectoryDatabase.getInstance(this);
		directorDatabase.deleteContacts();
		directorDatabase.addContact("John", "Doe");
		directorDatabase.addContact("Armand", "Aswun");
		directorDatabase.addContact("Archid", "Sin");
		directorDatabase.addContact("Peter", "Jackson");
		directorDatabase.addContact("Steve", "Jobs");
		directorDatabase.addContact("Steve", "Ballmer");
	}

	private void fillListView()
	{
		MyAdapter adapter = new MyAdapter(this, directorDatabase.fetchContactsByFirstname());

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
	}

	private class MyAdapter extends CursorSectionAdapter
	{
		private LayoutInflater	inflater;

		public MyAdapter(Context context, Cursor c)
		{
			super(context, c);
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		protected TreeMap<Integer, Object> initializeSections(Cursor c)
		{
			TreeMap<Integer, Object> sections = new TreeMap<Integer, Object>();

			int offset = 0, i = 0;
			while (c.moveToNext())
			{
				String firstname = c.getString(c.getColumnIndex(CursorSectionDirectoryDatabase.COL_FIRSTNAME));
				String firstLetter = firstname.substring(0, 1);

				if (!sections.containsValue(firstLetter))
				{
					sections.put(offset+i, firstLetter);
					offset++;
				}

				i++;
			}

			return sections;
		}
		
		@Override
		public int getItemViewType(int position)
		{
			if(getSections().containsKey(position))
				return 0;
			else
				return 1;
		}

		@Override
		protected boolean isSection(int position)
		{
			return getItemViewType(position) == 0;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			ListItemViewHolder holder = (ListItemViewHolder) view.getTag();
			
			String firstname = cursor.getString(cursor.getColumnIndex(CursorSectionDirectoryDatabase.COL_FIRSTNAME));
			String lastname = cursor.getString(cursor.getColumnIndex(CursorSectionDirectoryDatabase.COL_LASTNAME));
			holder.nameTextView.setText(firstname + " " + lastname);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			ListItemViewHolder holder = new ListItemViewHolder();
			View v;

			v = inflater.inflate(android.R.layout.simple_list_item_1, null);
			v.setTag(holder);

			holder.nameTextView = (TextView) v.findViewById(android.R.id.text1);

			return v;
		}

		@Override
		protected void bindSeparatorView(View v, Context context2, Object item)
		{
			ListItemViewHolder holder = (ListItemViewHolder) v.getTag();

			holder.nameTextView.setText((String) item);
		}

		@Override
		protected View newSeparatorView(Context context2, Object item, ViewGroup parent)
		{
			ListItemViewHolder holder = new ListItemViewHolder();
			View v;

			v = inflater.inflate(android.R.layout.simple_list_item_1, null);
			v.setTag(holder);

			holder.nameTextView = (TextView) v.findViewById(android.R.id.text1);

			return v;
		}

	}
}
